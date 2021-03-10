package cn.e_xiaofu.opengldemo.util

import android.content.Context
import android.content.res.Resources
import androidx.annotation.RawRes
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.StringBuilder

/**
 * @author 袁希望
 * created on :2021/3/9 16:49
 */
object ResourceReadUtil {
    fun readTextFileFromResource(context: Context, @RawRes res:Int):String{
        val stringBuilder = StringBuilder()
        try {
            val inputStream = context.resources.openRawResource(res)
            val streamReader = InputStreamReader(inputStream)
            val bufferedReader = BufferedReader(streamReader)
            var textLine: String?
            while (bufferedReader.readLine().also { textLine = it } != null) {
                stringBuilder.append(textLine)
                    .append("\n")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: Resources.NotFoundException) {
            e.printStackTrace()
        }
        return stringBuilder.toString()
    }
}