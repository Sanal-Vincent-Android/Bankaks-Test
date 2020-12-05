package com.smb.smbapplication.data.model


import androidx.annotation.NonNull
import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["id"])
  data class User(

        @field:SerializedName("speciality")
        var name: String? = null,

        @NonNull
        @field:SerializedName("spec_id")
        var id: Int = 0

)