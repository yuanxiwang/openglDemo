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
    fun compileVertexShader(vertexShaderCode: String): Int {
        return compileShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
    }

    fun compileFragmentShader(fragmentShaderCode: String): Int {
        return compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)
    }

    private fun compileShader(type: Int, shaderCode: String): Int {
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

    fun linkProgram(vertexShaderId: Int, fragmentShaderId: Int): Int {
        val programObjectId = GLES20.glCreateProgram()
        if (programObjectId == 0) {
            Log.d(TAG, "Could not create new program")
            return 0
        }
        GLES20.glAttachShader(programObjectId, vertexShaderId)
        GLES20.glAttachShader(programObjectId, fragmentShaderId)
        GLES20.glLinkProgram(programObjectId)
        val linkState = IntArray(1)
        GLES20.glGetProgramiv(programObjectId, GLES20.GL_LINK_STATUS, linkState, 0)
        Log.d(TAG, "Results of linking program:\n" + GLES20.glGetProgramInfoLog(programObjectId))
        if (linkState[0] == 0) {
            Log.d(TAG, "Linking of program failed.")
            return 0
        }
        return programObjectId
    }

    fun validateProgram(programId:Int):Boolean{
        GLES20.glValidateProgram(programId)
        val validateState = IntArray(1)
        GLES20.glGetProgramiv(programId, GLES20.GL_VALIDATE_STATUS, validateState, 0)
        Log.d(TAG, "Results of validating program : " + validateState[0] + "\n info : " + GLES20.glGetProgramInfoLog(programId))
        return validateState[0] != 0
    }
}