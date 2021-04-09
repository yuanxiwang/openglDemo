package cn.e_xiaofu.opengldemo

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import cn.e_xiaofu.opengldemo.util.MatrixHelper
import cn.e_xiaofu.opengldemo.util.ResourceReadUtil
import cn.e_xiaofu.opengldemo.util.ShaderHelper
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * @author 袁希望
 * created on :2021/3/9 13:28
 */
class SimpleRenderer : GLSurfaceView.Renderer {
    private var context: Context?
    val POSITION_COMPONENT_COUNT = 4
    val COLOR_COMPONENT_COUNT = 3
    val BYTE_PER_FLOAT = 4
    private var vertexData: FloatBuffer?
    private var programId: Int = 0
    private var tableVertices = floatArrayOf(
        0.0f, 0.0f,
        0.0f, 14.0f,
        9.0f, 14.0f,
        9.0f, 0.0f
    )
    private var tableVerticesWithTriangle = floatArrayOf(
        //前两个是坐标,后三个是颜色RGB
        // triangle fan
         0.0f,  0.0f, 0.0f, 1.5f, 1.0f, 1.0f, 1.0f,
        -0.5f, -0.8f, 0.0f, 1.0f, 0.7f, 0.7f, 0.7f,
         0.5f, -0.8f, 0.0f, 1.0f, 0.7f, 0.7f, 0.7f,
         0.5f,  0.8f, 0.0f, 2.0f, 0.7f, 0.7f, 0.7f,
        -0.5f,  0.8f, 0.0f, 2.0f, 0.7f, 0.7f, 0.7f,
        -0.5f, -0.8f, 0.0f, 1.0f, 0.7f, 0.7f, 0.7f,
        // line 10.0f,
        -0.5f,  0.0f, 0.0f, 1.5f, 1.0f, 0.0f, 0.0f,
         0.5f,  0.0f, 0.0f, 1.5f, 1.0f, 0.0f, 0.0f,
        //mallets0.0f,
        0.0f, -0.25f, 0.0f, 1.25f, 1.0f, 0.0f, 0.0f,
        0.0f,  0.25f, 0.0f, 1.75f, 0.0f, 0.0f, 1.0f
    )
    private val A_COLOR = "a_Color"
    private var aColorLocation: Int = 0
    private val A_POSITION = "a_Position"
    private var aPositionLocation: Int = 0
    private val U_MATRIX = "u_Matrix"
    private var uMatrixLocation: Int = 0
    private var projectionMatrix = FloatArray(16)
    private val STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTE_PER_FLOAT
    private val modelMatrix = FloatArray(16)

    constructor(context: Context) {
        this.context = context
    }

    init {
        vertexData = ByteBuffer.allocateDirect(tableVerticesWithTriangle.size * BYTE_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        vertexData?.put(tableVerticesWithTriangle)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
        var vertexShaderCode = context?.let {
            ResourceReadUtil.readTextFileFromResource(
                it,
                R.raw.simple_vertex_shader
            )
        }
        var fragmentShaderCode = context?.let {
            ResourceReadUtil.readTextFileFromResource(
                it,
                R.raw.simple_fragment_shader
            )
        }
        val vertexShaderId = vertexShaderCode?.let { ShaderHelper.compileVertexShader(it) } ?: 0
        val fragShaderId = fragmentShaderCode?.let { ShaderHelper.compileFragmentShader(it) } ?: 0
        programId = ShaderHelper.linkProgram(vertexShaderId, fragShaderId)
        ShaderHelper.validateProgram(programId)
        GLES20.glUseProgram(programId)

        aColorLocation = GLES20.glGetAttribLocation(programId, A_COLOR)
        aPositionLocation = GLES20.glGetAttribLocation(programId, A_POSITION)
        uMatrixLocation = GLES20.glGetUniformLocation(programId, U_MATRIX)
        //告诉OpenGL到哪里找到属性a_Position对应的数据
        vertexData?.position(0)
        GLES20.glVertexAttribPointer(
            aPositionLocation,
            POSITION_COMPONENT_COUNT,
            GLES20.GL_FLOAT,
            false,
            STRIDE,
            vertexData
        )
        GLES20.glEnableVertexAttribArray(aPositionLocation)

        vertexData?.position(POSITION_COMPONENT_COUNT)
        GLES20.glVertexAttribPointer(
            aColorLocation,
            COLOR_COMPONENT_COUNT,
            GLES20.GL_FLOAT,
            false,
            STRIDE,
            vertexData
        )
        GLES20.glEnableVertexAttribArray(aColorLocation)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        //set the opengl viewport to fill the entire surface
        GLES20.glViewport(0, 0, width, height)

//        val aspectRatio =
//            if (width > height) width.toFloat() / height.toFloat() else height.toFloat() / width.toFloat()
//        if (width > height) {
//            //landscape
//            Matrix.orthoM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f)
//        } else {
//            //portrait or square
//            Matrix.orthoM(projectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f)
//        }
        MatrixHelper.perspectiveM(projectionMatrix, 45f, width.toFloat() / height.toFloat(), 1f, 10f)
        //设置获取单位阵
        Matrix.setIdentityM(modelMatrix, 0)
        //平移沿Z轴矩阵
        Matrix.translateM(modelMatrix, 0, 0f, 0f, -2f)
        val temp = FloatArray(16)
        //两个矩阵相乘 projectionMatrix * modelMatrix
        Matrix.multiplyMM(temp, 0, projectionMatrix, 0, modelMatrix, 0)
        //将temp的数据复制到 projectionMatrix 中
        System.arraycopy(temp, 0, projectionMatrix, 0, temp.size)

    }

    override fun onDrawFrame(gl: GL10?) {
        //clear the rendering surface
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        GLES20.glUniformMatrix4fv(uMatrixLocation, 1, false, projectionMatrix, 0)
        //画桌子
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 6)
        //画中间分割线
        GLES20.glDrawArrays(GLES20.GL_LINES, 6, 2)
        //画第一个点
        GLES20.glDrawArrays(GLES20.GL_POINTS, 8, 1)
        //画第二个点
        GLES20.glDrawArrays(GLES20.GL_POINTS, 9, 1)
    }
}