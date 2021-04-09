package cn.e_xiaofu.opengldemo.util

import kotlin.math.tan

/**
 * @author 袁希望
 * created on :2021/3/30 15:37
 */
object MatrixHelper {
    fun perspectiveM(m: FloatArray, yFovInDegrees: Float, aspect: Float, n: Float, f: Float) {
        //计算焦距
        val angleInRadians = yFovInDegrees * Math.PI / 180.0
        val a = 1.0 / tan(angleInRadians / 2.0)

        //输出矩阵
        m[0] = (a / aspect).toFloat()
        m[1] = 0.0f
        m[2] = 0.0f
        m[3] = 0.0f

        m[4] = 0.0f
        m[5] = a.toFloat()
        m[6] = 0.0f
        m[7] = 0.0f

        m[8] = 0.0f
        m[9] = 0.0f
        m[10] = -((f + n) / (f - n))
        m[11] = -1.0f

        m[12] = 0.0f
        m[13] = 0.0f
        m[14] = -((2f * f * n) / (f - n))
        m[15] = 0.0f
    }
}