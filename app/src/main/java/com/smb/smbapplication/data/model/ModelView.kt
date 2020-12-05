package com.smb.smbapplication.data.model

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import com.google.android.material.textfield.TextInputEditText

class ModelView (val view: TextInputEditText,
                 val regex: String,
                 val type : String   )