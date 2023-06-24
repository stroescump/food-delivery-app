package com.adelinarotaru.fooddelivery.utils

import android.app.AlertDialog
import android.content.Context
import android.content.res.Resources
import android.graphics.Outline
import android.graphics.Paint
import android.graphics.Rect
import android.text.Editable
import android.util.TypedValue
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.EditText
import androidx.appcompat.R
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.adelinarotaru.fooddelivery.R.dimen
import com.adelinarotaru.fooddelivery.R.font
import com.google.android.material.snackbar.Snackbar

/**
 * ViewUtils - provides a facade to all the boilerplate leveraging KT Extension Functions
 */
fun View.setBottomCorners() {
    clipToOutline = true
    outlineProvider = object : ViewOutlineProvider() {
        override fun getOutline(view: View?, outline: Outline?) {
            val cornerRadius =
                resources.getDimensionPixelSize(dimen.rounded_corner_radius) // Replace with your desired corner radius in pixels
            view?.apply {
                val left = 0
                val top = 0
                val right = view.width
                val bottom = view.height
                outline?.setRoundRect(
                    left,
                    top - cornerRadius,
                    right,
                    bottom,
                    cornerRadius.toFloat()
                )
                clipToOutline = true
            }
        }
    }
}

fun View.setHorizontalCorners() {
    clipToOutline = true
    outlineProvider = object : ViewOutlineProvider() {
        override fun getOutline(view: View?, outline: Outline?) {
            val cornerRadius =
                resources.getDimensionPixelSize(dimen.rounded_corner_radius)
                    .toFloat() // Replace with your desired corner radius in pixels
            view?.apply {
                val left = 0
                val top = 0
                val right = view.width
                val bottom = view.height
                outline?.setRoundRect(left, top, right, bottom, cornerRadius)
                clipToOutline = true
            }
        }
    }
}

fun pxToDp(px: Int, context: Context): Int {
    return (px / context.resources.displayMetrics.density).toInt()
}

fun ViewBinding.getString(res: Int, args: Double): String {
    return root.context.getString(res, args)
}

fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun AppCompatTextView.applyStrikeThrough() {
    paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
}

fun AppCompatTextView.resetPaintFlags() {
    paintFlags = Paint.ANTI_ALIAS_FLAG
}

fun AppCompatTextView.setFontSizeTo(fontSize: Float) {
    setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize)
}

fun AppCompatTextView.setFontFamily(resFont: Int, textStyle: Int) {
    setTypeface(
        ResourcesCompat.getFont(
            this.context,
            resFont
        ), textStyle
    )
}

fun getBitmapFromDrawable(
    resources: Resources,
    resInt: Int,
    theme: Resources.Theme
) =
    ResourcesCompat.getDrawable(resources, resInt, theme)?.toBitmap()

fun AppCompatEditText.value() = text?.let {
    if (it.trim().toString()
            .isBlank()
    ) throw IllegalArgumentException("Please fill in $hint") else return@let this.text.toString()
} ?: throw IllegalArgumentException("Please fill in $hint")

fun String.toEditable() = Editable.Factory.getInstance().newEditable(this)

fun showMessage(context: Context, message: String) {
    val alertDialogBuilder = AlertDialog.Builder(context)
    alertDialogBuilder.setMessage(message)
    alertDialogBuilder.setPositiveButton("OK") { dialog, _ ->
        dialog.dismiss()
    }
    val alertDialog = alertDialogBuilder.create()
    alertDialog.show()
}

fun showPermanentMessage(view: View, message: String): Snackbar {
    return Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE).run {
        show()
        this
    }
}

fun SearchView.changeColorTo(color: Int) {
    val editText = this.findViewById<EditText>(R.id.search_src_text)
    editText.setTextColor(ContextCompat.getColor(context, color))
    editText.setHintTextColor(ContextCompat.getColor(context, color))
    editText.typeface = ResourcesCompat.getFont(
        editText.context,
        font.creato_display_regular
    )
}