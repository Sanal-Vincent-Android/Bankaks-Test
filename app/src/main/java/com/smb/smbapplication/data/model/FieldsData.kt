package com.smb.smbapplication.data.model

import com.google.gson.annotations.SerializedName

data class FieldsData(
        @field:SerializedName("placeholder")
        var placeholder: String? = null,
        @field:SerializedName("regex")
        var regex: String? = null,
        @field:SerializedName("service_id")
        var service_id: Int? = null,
        @field:SerializedName("hint_text")
        var hintText: String? = null,
        @field:SerializedName("is_mandatory")
        var isMandatory: String? = null,
        @field:SerializedName("type")
        var servicetype: TypeData? = null,
        @field:SerializedName("ui_type")
        var uiType: UiType? = null
)