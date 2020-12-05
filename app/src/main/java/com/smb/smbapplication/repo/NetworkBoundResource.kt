/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smb.smbapplication.repo

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.smb.smbapplication.AppExecutors
import com.smb.smbapplication.data.api.*

/**
 * A generic class that can provide a resource backed by both the sqlite database and the network.
 *
 *
 * You can read more about it in the [Architecture
 * Guide](https://developer.android.com/arch).
 * @param <ResultType>
 * @param <RequestType>
</RequestType></ResultType> */
abstract class NetworkBoundResource<ResultType>
@MainThread constructor(private val appExecutors: AppExecutors) {

    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        result.value = Resource.loading(null)
        fetchFromNetwork()
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun fetchFromNetwork() {
        val apiResponse = createCall()
        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)


            when (response) {
                is Responses.ApiSuccessResponse -> {
                    setValue(Resource.success(response.body))
                    appExecutors.diskIO().execute {
                        saveCallResult(processResponse(response))
                    }
                }
                is Responses.ApiEmptyResponse -> {
                    appExecutors.mainThread().execute {
                        setValue(Resource.success(null))
                    }
                }
                is Responses.ApiErrorResponse -> {
                    onFetchFailed()
                    setValue(Resource.error(response.errorMessage, null))
                }

            }
        }
    }

    protected open fun onFetchFailed() {}

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    @WorkerThread
    protected open fun processResponse(response: Responses.ApiSuccessResponse<ResultType>) = response.body

    @WorkerThread
    protected open fun saveCallResult(item: ResultType) {
    }

    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<ResultType>>

}