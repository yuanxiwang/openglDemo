package cn.e_xiaofu.opengldemo.util

import android.opengl.GLES20

/**
 * @author 袁希望
 * created on :2021/3/9 17:51
 * description:// TODO 2021/3/9 17:51
 */
object ShaderHelper {
    fun compileVertexShader(vertexShaderCode:String):Int{
        return compileShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
    }

    fun compileFragmentShader(fragmentShaderCode:String):Int{
        return compileShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)
    }

    fun compileShader(type:Int, shaderCode:String):Int{

        return 0
    }
}