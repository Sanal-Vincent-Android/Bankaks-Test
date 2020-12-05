import com.smb.smbapplication.utils.AppConstants



import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import javax.inject.Singleton

@Singleton
class SessionUtils {

    companion object{
        private var preferences: SharedPreferences? = null

        fun init(context: Context) {
            preferences = context.getSharedPreferences(AppConstants.PREFERENCE_NAME, Context.MODE_PRIVATE)
        }


        fun saveSession(session: LoginSession?) {
            if (session == null) return
            val prefsEditor = preferences!!.edit()
            val gson = Gson()
            val json = gson.toJson(session)
            prefsEditor.putString(AppConstants.PRE_SESSION, json)
            prefsEditor.apply()
        }

        fun hasSession(): Boolean {
            return loginSession != null
        }

         val loginSession: LoginSession?
            get() = try {
                val gson = Gson()
                val json = preferences!!.getString(AppConstants.PRE_SESSION, "")
                gson.fromJson(json, LoginSession::class.java)
            } catch (e: Exception) {
                null
            }

        fun clearSession() {
            preferences!!.edit().remove(AppConstants.PRE_AUTH_TOKEN)
                    .remove(AppConstants.PRE_REFRESH_TOKEN)
                    .remove(AppConstants.PRE_SESSION).apply()
        }

        fun saveFCMToken(token: String?) {
            preferences!!.edit().putString(AppConstants.PRE_FCM, token).apply()
        }


        val fCMToken: String?
            get() = preferences!!.getString(AppConstants.PRE_FCM, "")

        fun saveToken(auth: String?, refresh: String?) {
            preferences!!.edit().putString(AppConstants.PRE_AUTH_TOKEN, auth)
                    .putString(AppConstants.PRE_REFRESH_TOKEN, refresh).apply()
        }

        val  authToken: String? get() = preferences?.getString(AppConstants.PRE_AUTH_TOKEN, "")

        val refreshToken: String?
            get() = preferences!!.getString(AppConstants.PRE_REFRESH_TOKEN, "")

    }

    class LoginSession {
        @SerializedName("user_id")
        @Expose
        var userId = ""

        @SerializedName("name")
        @Expose
        var name = ""

        @SerializedName("phone")
        @Expose
        var phone = ""

        @SerializedName("email")
        @Expose
        var email: String? = null

        @SerializedName("userimage")
        @Expose
        var userimage: String? = null

        @SerializedName("usertype")
        @Expose
        var usertype:Int = 1

        @SerializedName("verified")
        @Expose
        var verified = ""

        @SerializedName("profile_completion_status")
        @Expose
        var profileCompletionStatus = ""


    }
}