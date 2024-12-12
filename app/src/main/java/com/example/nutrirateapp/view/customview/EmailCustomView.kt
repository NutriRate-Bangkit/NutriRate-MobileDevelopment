package com.example.nutrirateapp.view.customview


import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Patterns
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.example.nutrirateapp.R


class EmailCustomView : AppCompatEditText {

    private lateinit var emailIcon: Drawable

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
        // Set the email icon drawable
        emailIcon = ContextCompat.getDrawable(context, R.drawable.ic_email) as Drawable
        compoundDrawablePadding = convertDpToPx(8) // Add padding of 8dp
        setEditCompoundDrawables(startOfTheText = emailIcon)
    }

    fun validateEmail(): Boolean {
        val inputText = text?.toString()?.trim() ?: ""
        return if (inputText.isEmpty()) {
            error = resources.getString(R.string.email_required)
            false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(inputText).matches()) {
            error = resources.getString(R.string.invalid_email)
            false
        } else {
            error = null
            true
        }
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

    private fun convertDpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }
}
