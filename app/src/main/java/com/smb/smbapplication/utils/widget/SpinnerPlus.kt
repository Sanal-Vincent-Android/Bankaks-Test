package com.smb.smbapplication.utils.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatSpinner

class SpinnerPlus(context: Context?, attrs: AttributeSet?) : AppCompatSpinner(context!!, attrs) {
    var listener: OnItemSelectedListener? = null

    override fun setSelection(position: Int) {
        super.setSelection(position)
        if (listener != null) listener!!.onItemSelected(this, selectedView, position, 0)
    }

    override fun setOnItemSelectedListener(listener: OnItemSelectedListener?) {
        this.listener = listener
    }
}