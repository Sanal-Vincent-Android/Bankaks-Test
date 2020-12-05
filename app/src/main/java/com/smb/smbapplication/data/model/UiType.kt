package com.smb.smbapplication.data.model

import androidx.room.Ignore
import com.google.gson.annotations.SerializedName

data class UiType(
        @field:SerializedName("type")
        var type: String? = null,
        @field:SerializedName("service_id")
        var service_id: Int? = null,
        @field:SerializedName("service_description")
        var serviceDescription: String? = null,
        @field:SerializedName("service_status")
        var serviceStatus: String? = null,
        @field:SerializedName("service_price")
        var servicePrice: String? = null,
        @Ignore
        @field:SerializedName("values")
        var values:ArrayList<Values>?,
)