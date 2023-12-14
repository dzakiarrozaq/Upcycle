package com.bangkit.upcycle.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.bangkit.upcycle.R

class CVPassword : AppCompatEditText {
    private lateinit var iconFormInput: Drawable
    private var characterLength = 0


    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    constructor(context: Context) : super(context) {
        init()
    }





    private fun init() {
        iconFormInput = ContextCompat.getDrawable(context, R.drawable.lock) as Drawable
        showIconFormInput()
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                characterLength = s.length
                if (!s.isNullOrEmpty() && characterLength < 8) error = context.getString(R.string.label_validation_password)
            }
            override fun afterTextChanged(edt: Editable?) {

            }
        })
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ){
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    private fun showIconFormInput() {
        setButtonDrawables(startOfTheText = iconFormInput)
    }



    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        context.apply {
            setTextColor(ContextCompat.getColor(this, R.color.black))
            setHintTextColor(ContextCompat.getColor(this, R.color.black))
            background = ContextCompat.getDrawable(this, R.drawable.form_input)
        }
        maxLines = 1
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        transformationMethod = PasswordTransformationMethod.getInstance()
    }
}