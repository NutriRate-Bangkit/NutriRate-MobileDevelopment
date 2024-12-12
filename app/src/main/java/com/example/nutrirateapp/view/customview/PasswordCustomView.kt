package com.example.nutrirateapp.view.customview

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.nutrirateapp.R

class PasswordCustomView : AppCompatEditText, View.OnTouchListener {

    private lateinit var passwordButtonImage: Drawable
    private lateinit var passwordIcon: Drawable
    private var isPasswordVisible = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        setOnTouchListener(this)
        passwordButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_eye) as Drawable
        passwordIcon = ContextCompat.getDrawable(context, R.drawable.ic_lock) as Drawable
        setEditCompoundDrawables(endOfTheText = passwordButtonImage, startOfTheText = passwordIcon)
    }

    private fun setEditCompoundDrawables(
        startOfTheText: Drawable? = null,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val passwordButtonStart: Float
            val passwordButtonEnd: Float
            var isPasswordButtonClicked = false

            if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                passwordButtonEnd = (passwordButtonImage.intrinsicWidth + paddingStart).toFloat()
                if (event.x < passwordButtonEnd) isPasswordButtonClicked = true
            } else {
                passwordButtonStart = (width - paddingEnd - passwordButtonImage.intrinsicWidth).toFloat()
                if (event.x > passwordButtonStart) isPasswordButtonClicked = true
            }

            if (isPasswordButtonClicked) {
                if (event.action == MotionEvent.ACTION_DOWN) {
                    togglePasswordVisibility()
                }
                return true
            }
        }
        return false
    }

    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible

        // Update the icon based on visibility
        passwordButtonImage = ContextCompat.getDrawable(
            context,
            if (isPasswordVisible) R.drawable.ic_hide_eye else R.drawable.ic_eye
        ) as Drawable
        setEditCompoundDrawables(endOfTheText = passwordButtonImage, startOfTheText = passwordIcon)

        // Toggle password visibility
        transformationMethod = if (isPasswordVisible) null else PasswordTransformationMethod.getInstance()

        // Move cursor to the end of the text
        setSelection(text?.length ?: 0)
    }

    fun validatePassword(): Boolean {
        val inputText = text?.toString() ?: ""
        return if (inputText.isEmpty()) {
            error = resources.getString(R.string.login_password)
            false
        } else if (inputText.length < 8) {
            error = resources.getString(R.string.minimumcharacters)
            false
        } else {
            error = null
            true
        }
    }
}
