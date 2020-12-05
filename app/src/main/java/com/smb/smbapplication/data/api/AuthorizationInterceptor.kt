package com.smb.smbapplication.data.api

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import java.io.IOException
import java.nio.charset.Charset
import kotlin.jvm.Throws

class AuthorizationInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var firstRequest = chain.request()
        if (SessionUtils.authToken != "") {
            firstRequest = chain.request().newBuilder()
                    .addHeader("auth-token", SessionUtils.authToken!!)
                    .build()
        }
        requestLog(firstRequest)
        var response = chain.proceed(firstRequest)
        responseLog(response)
        var authentication = response.header("Authorization")
        if (authentication != null && authentication == "false") {
            val builder = firstRequest.newBuilder().header("refresh-token", SessionUtils.refreshToken!!).method(firstRequest.method(), firstRequest.body())
            val secondRequest = builder.build()
            requestLog(secondRequest)
            response = chain.proceed(secondRequest)
            responseLog(response)
            authentication = response.header("Authorization")
            if (authentication != null && authentication == "false") {
                SessionUtils.clearSession()
                Log.e("Clear ", "Session")
            }
        }
        val authToken = response.header("auth_token")
        val refreshToken = response.header("refresh_token")
        if (refreshToken != null && authToken != null) {
            SessionUtils.saveToken(authToken, refreshToken)
        }
        return response
    }

    private fun requestLog(request: Request) {
        val log = StringBuilder()
        log.append("URL {)")
        log.append("""
  ${String.format("%s", request.url())}""")
        log.append("\n}\n")
        val header = StringBuilder()
        val headers = request.headers()
        var size = headers.size()
        for (i in 0 until size) {
            header.append("""
  ${headers.name(i)}:${headers.value(i)}""")
        }
        if (header.length > 0) {
            log.append("Header {")
            log.append(header)
            log.append("\n}\n")
        }
        try {
            val body = StringBuilder()
            if (request.body() != null) {
                val buffer = Buffer()
                request.newBuilder().build().body()!!.writeTo(buffer)
                val req = buffer.readUtf8()
                val reqArray = req.split("&".toRegex()).toTypedArray()
                size = reqArray.size
                for (i in 0 until size) {
                    body.append("""
  ${reqArray[i].replace("=".toRegex(), ":")}""")
                }
            }
            if (body.length > 0) {
                log.append("Body {")
                log.append(body)
                log.append("\n}\n")
            }
        } catch (e: IOException) {
            log.append("Body {")
            log.append("\n Invalid Body")
            log.append("\n}\n\n")
        }
        Log.i("API Request", log.toString())
    }

    private fun responseLog(response: Response) {
        val log = StringBuffer("\n")
        log.append("Header {")
        val headers = response.headers()
        val size = headers.size()
        for (i in 0 until size) {
            log.append("""
  ${headers.name(i)}:${headers.value(i)}""")
        }
        log.append("\n}\n")
        try {
            val responseBody = response.body()
            val contentLength = responseBody!!.contentLength()
            val source = responseBody.source()
            source.request(Long.MAX_VALUE) // Buffer the entire body.
            val buffer = source.buffer
            var charset = Charset.forName("UTF-8")
            val contentType = responseBody.contentType()
            if (contentType != null) {
                charset = contentType.charset(Charset.forName("UTF-8"))
            }
            if (contentLength != 0L) {
                log.append("Body {")
                log.append("""
  ${buffer.clone().readString(charset)}""")
                log.append("\n}")
            }
        } catch (e: Exception) {
            log.append("\nBody {")
            log.append("\n Invalid Body")
            log.append("\n}\n\n")
        }
        log.append("\n_________________________")
        Log.i("API Response", log.toString())
    }
}
