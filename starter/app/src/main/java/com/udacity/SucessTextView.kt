package com.udacity

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import kotlin.properties.Delegates

class SuccessTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr){

    init {
        adaptText(false)
    }

    var isSuccess: Boolean by Delegates.observable(false) { property, oldValue, newValue ->
        adaptText(isSuccess)
    }

    private fun adaptText(isSuccess: Boolean) {
        if (isSuccess) {
            text =  context.getString(R.string.success)
            setTextColor(Color.GREEN)
        } else {
            text =  context.getString(R.string.failure)
            setTextColor(Color.RED)
        }
    }
}