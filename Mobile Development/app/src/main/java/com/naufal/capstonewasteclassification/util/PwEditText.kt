package com.naufal.capstonewasteclassification.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.naufal.capstonewasteclassification.R

class PwEditText : AppCompatEditText {

    private lateinit var editBg: Drawable
    private lateinit var editErrorBg: Drawable
    private var isError = false

    constructor(context: Context) : super(context) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        hint = "Minimum 8 karakter"
        background = if (isError) editErrorBg else editBg
        addTextChangedListener(onTextChanged = { text, _, _, _ ->
            if (!TextUtils.isEmpty(text) && text.toString().length < 8) {
                setError("Password harus lebih dari 8 karakter", null)
                isError = true
            } else {
                error = null
                isError = false
            }
        })
    }

    private fun init() {
        editBg = ContextCompat.getDrawable(context, R.drawable.edit_text) as Drawable
        editErrorBg =
            ContextCompat.getDrawable(context, R.drawable.edit_text_error) as Drawable
        setHintTextColor(ContextCompat.getColor(context, android.R.color.white))
    }

}
