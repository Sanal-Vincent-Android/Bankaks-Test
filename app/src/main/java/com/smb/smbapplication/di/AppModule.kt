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

package com.smb.smbapplication.di

import android.app.Application
import androidx.room.Room

import com.smb.smbapplication.common.LiveDataCallAdapterFactory
import com.smb.smbapplication.data.api.AuthorizationInterceptor
import com.smb.smbapplication.data.api.WebService
import com.smb.smbapplication.data.db.AppDb
import com.smb.smbapplication.data.db.UMSDao
import com.smb.smbapplication.utils.AppConstants
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module(includes = [ViewModelModule::class])
class AppModule {

    private val BASE_URL = "https://api-staging.bankaks.com/"



    @Singleton @Provides
    fun provideWebService(app: Application): WebService {

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)

                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()


        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .build()
                .create(WebService::class.java)
    }

    @Singleton @Provides
    fun provideDb(app: Application): AppDb {
        return Room
                .databaseBuilder(app, AppDb::class.java, AppConstants.DATABASE)
                .fallbackToDestructiveMigration()
                .build()
    }


    @Singleton @Provides
    fun provideUMSDao(db: AppDb): UMSDao {
        return db.umsDao()
    }

    @Singleton @Provides
    fun getBaseUrl(): String {
        return BASE_URL
    }
}
