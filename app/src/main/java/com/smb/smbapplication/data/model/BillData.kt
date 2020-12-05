package com.smb.smbapplication.data.model

import androidx.room.Ignore
import com.google.gson.annotations.SerializedName

data class BillData(
        @field:SerializedName("number_of_fields")
        var numberFields: Int? = null,
        @field:SerializedName("screen_title")
        var screenTitle: String? = null,
        @field:SerializedName("service_id")
        var service_id: Int? = null,
        @field:SerializedName("service_description")
        var serviceDescription: String? = null,
        @field:SerializedName("service_status")
        var serviceStatus: String? = null,
        @field:SerializedName("service_price")
        var servicePrice: String? = null,
        @field:SerializedName("fields")
        var fields:ArrayList<FieldsData>?,
)