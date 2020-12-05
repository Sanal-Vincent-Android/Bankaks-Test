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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.smb.smbapplication.AppExecutors
import com.smb.smbapplication.data.api.BaseResponse
import com.smb.smbapplication.data.api.WebService
import com.smb.smbapplication.data.api.Resource
import com.smb.smbapplication.data.db.AppDb
import com.smb.smbapplication.data.db.UMSDao
import com.smb.smbapplication.data.model.BillData
import com.smb.smbapplication.data.model.User
import java.util.HashMap
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository that handles User instances.
 *
 */
@Singleton
class UMSRepository @Inject constructor(
        private val appExecutors: AppExecutors,
        private val db: AppDb,
        private val umsoDao: UMSDao,
        private val webService: WebService
) {
    //ApiResponse<BaseResponse<List<User>>>
    fun login(data: HashMap<String, String>): LiveData<Resource<BaseResponse<BillData>>> {
        return object : NetworkBoundResource<BaseResponse<BillData>>(appExecutors) {
            override fun createCall() = webService.login(
                    data["type"]!!.toInt()
            )

        }.asLiveData()
    }


}
