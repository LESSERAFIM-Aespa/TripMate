package kr.sparta.tripmate.util.method

import android.app.Activity
import android.content.Context
import android.view.WindowManager
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

fun removeHtmlTags(input: String): String {
    val pattern = Pattern.compile("<.*?>")
    return pattern.matcher(input).replaceAll("")
}

fun Int.toMoneyFormat(): String = DecimalFormat("#,###").format(this)
fun Long.toMoneyFormat(): String = DecimalFormat("#,###").format(this)
fun Int.toTimeFormat(): String = DecimalFormat("00").format(this)


/**
 * 작성자: 서정한
 * 내용: 로딩중 화면터치 컨트롤
 * */
fun isWindowTouchable(context: Context, isTouchable: Boolean) {
    if(isTouchable) {
        // 화면터치 막기
        (context as Activity).window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }else {
        // 화면터치 해제
        (context as Activity).window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
}