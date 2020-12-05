package com.smb.smbapplication



import android.app.Activity
import android.app.Application
import com.smb.smbapplication.di.AppInjector
import com.smb.smbapplication.utils.logger.Log
import com.smb.smbapplication.utils.logger.LogWrapper
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class SmbApplication : Application(), HasAndroidInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>



    override fun onCreate() {
        super.onCreate()

        AppInjector.init(this)
        Log.setLogNode(LogWrapper()) // Initialise logging




    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector
}
