package cn.e_xiaofu.opengldemo.util

import android.opengl.GLES20
import android.util.Log

/**
 * @author 袁希望
 * created on :2021/3/9 17:51
 * description:// TODO 2021/3/9 17:51
 */
object ShaderHelper {
    val TAG = "ShaderHelper"
    fun compileVertexShader(vertexShaderCode:String):Int{
        return compileShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
    }

    fun compileFragmentShader(fragmentShaderCode:String):Int{
        return compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)
    }

    fun compileShader(type:Int, shaderCode:String):Int{
        val glCreateShader = GLES20.glCreateShader(type)
        if (glCreateShader == 0) {
            Log.d(TAG, "Could not create new shader.")
            return 0
        }
        GLES20.glShaderSource(glCreateShader, shaderCode)
        GLES20.glCompileShader(glCreateShader)
        var compileState = IntArray(1)
        GLES20.glGetShaderiv(glCreateShader, GLES20.GL_COMPILE_STATUS, compileState, 0)
        Log.d(TAG, GLES20.glGetShaderInfoLog(glCreateShader))
        if (compileState[0] == 0) {
            GLES20.glDeleteShader(glCreateShader)
            Log.d(TAG, "Compilation of shader failed.")
            return 0
        }
        return glCreateShader
    }
}