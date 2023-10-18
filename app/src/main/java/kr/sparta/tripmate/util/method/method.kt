package kr.sparta.tripmate.util.method

import android.content.Context
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayout
import java.text.DecimalFormat
import java.util.regex.Pattern

fun Context.shortToast(message: String, time: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, time).show()
}

fun Context.longToast(message: String, time: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, message, time).show()
}

fun TabLayout.Tab.setIcon(context: Context, @DrawableRes resourceId: Int) {
    this.icon = ContextCompat.getDrawable(context, resourceId)
}

fun removeHtmlTags(input: String): String {
    val pattern = Pattern.compile("<.*?>")
    return pattern.matcher(input).replaceAll("")
}

fun Int.toMoneyFormat(): String = DecimalFormat("#,###").format(this)