package com.smb.smbapplication.data.api


import com.google.gson.annotations.SerializedName


/**
 * Base Gson class structure of all api responses.
 */

data class BaseResponse<T>(

        @field:SerializedName("function")
        val function: String,

        @field:SerializedName("status")
        val status: Boolean,

        @field:SerializedName("status_code")
        val statusCode: Int,

        @field:SerializedName("message")
        val message: String,

        @field:SerializedName("result")
        var result: T?

)