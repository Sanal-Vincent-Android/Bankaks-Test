package com.smb.smbapplication.utils

import android.content.Context
import com.smb.smbapplication.BuildConfig
import java.util.*
import java.util.regex.Pattern



object AppConstants {

    const val DATABASE = "app.db"

    private const val PACKAGE_NAME = BuildConfig.APPLICATION_ID
    const val TIMESTAMP_FORMAT = "yyyyMMdd_HHmmss"
    const val PREFERENCE_NAME = PACKAGE_NAME + "_pref"
    const val PRE_SESSION = "$PACKAGE_NAME.login_session"
    const val PRE_AUTH_TOKEN = "$PACKAGE_NAME.auth"
    const val PRE_LAUNCHER = "$PACKAGE_NAME.launcher"
    const val PRE_REFRESH_TOKEN = "$PACKAGE_NAME.refresh"
    const val PRE_FCM = "$PACKAGE_NAME.fcm"
    const val UPLOADPIC = "LyfSkillsAndroidPic"
    const val TEMP_PHOTO_FILE = "LyfSkillsProfile.jpeg"

    const val BUCKET_NAME = "lyfskills"
    const val COGNITO_POOL_REGION = "us-east-2"
    const val COGNITO_POOL_ID = "us-east-2:c43f15fc-8558-498b-8337-79bb80a6f3fd"
    const val S3URL = "https://lyfskills.s3.amazonaws.com/"

    private var lyfSkillsDir: String? = null
    fun init(LyfSkillsApplication: Context) {
        lyfSkillsDir = "" + LyfSkillsApplication.getExternalFilesDir(null) + "/LyfSkills/";
    }

    fun getAppDirectory(context: Context?): String? {
        if (lyfSkillsDir == null && context != null) {
            if (context.applicationContext != null) {
                lyfSkillsDir = context.getExternalFilesDir(null).toString() + "/LyfSkills/"
            }
        }
        return lyfSkillsDir
    }

    const val SignUpToVerification = "signup"
    const val ForgotToVerification = "forgot"
    const val ResendToVerification = "resend"

    const val EMAIL = "email"
    const val PHONE = "phone"

    const val PRIVACY = "privacy"
    const val TERMS = "terms"

    const val PENDING = "pending"
    const val PROGRESSING = "progressing"
    const val SUCCESS = "success"

    enum class RequestStatus {
        REQUESTED,STARTED,PROGRESSING,COMPLETED,INVOICED,PAID
    }

    enum class RequestAction{
        REQUEST,REQUESTED,CANCEL
    }

    val statusList = arrayOf("REQUESTED","INVOICED","PAID","STARTED","PROGRESSING","COMPLETED","NEW")

    val NUMBER = Pattern.compile("^[6-9]d{9}\$")
}
