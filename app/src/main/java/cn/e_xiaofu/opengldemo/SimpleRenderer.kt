package cn.e_xiaofu.opengldemo

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
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
class SimpleRenderer:GLSurfaceView.Renderer {
    private var context:Context?
    val POSITION_COMPONENT_COUNT = 2
    val COLOR_COMPONENT_COUNT = 3
    val BYTE_PER_FLOAT = 4
    private var vertexData:FloatBuffer?
    private var programId:Int = 0
    private var tableVertices = floatArrayOf(
        0.0f, 0.0f,
        0.0f, 14.0f,
        9.0f, 14.0f,
        9.0f, 0.0f
    )
    private var tableVerticesWithTriangle = floatArrayOf(
        //前两个是坐标,后三个是颜色RGB
        // triangle fan
        0.0f, 0.0f, 1.0f,1.0f,1.0f,
        -0.5f, -0.5f, 0.7f,0.7f,0.7f,
        0.5f, -0.5f, 0.7f,0.7f,0.7f,
        0.5f, 0.5f, 0.7f,0.7f,0.7f,
        -0.5f, 0.5f, 0.7f,0.7f,0.7f,
        -0.5f, -0.5f, 0.7f,0.7f,0.7f,
        // line 1
        -0.5f, 0.0f, 1.0f, 0.0f, 0.0f,
        0.5f, 0.0f, 1.0f, 0.0f, 0.0f,
        //mallets
        0.0f, -0.25f, 1.0f, 0.0f, 0.0f,
        0.0f, 0.25f, 0.0f, 0.0f, 1.0f
    )
    private val A_COLOR = "a_Color"
    private var aColorLocation:Int = 0
    private val A_POSITION = "a_Position"
    private var aPositionLocation:Int = 0
    private val STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTE_PER_FLOAT
    constructor(context: Context){
        this.context = context
    }
    init {
        vertexData = ByteBuffer.allocateDirect(tableVerticesWithTriangle.size * BYTE_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        vertexData?.put(tableVerticesWithTriangle)
    }
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES20.glClearColor(0.0f, 0.0f,0.0f, 0.0f)
        var vertexShaderCode = context?.let { ResourceReadUtil.readTextFileFromResource(it, R.raw.simple_vertex_shader) }
        var fragmentShaderCode = context?.let { ResourceReadUtil.readTextFileFromResource(it, R.raw.simple_fragment_shader) }
        val vertexShaderId = vertexShaderCode?.let { ShaderHelper.compileVertexShader(it) }?:0
        val fragShaderId = fragmentShaderCode?.let { ShaderHelper.compileFragmentShader(it) }?:0
        programId = ShaderHelper.linkProgram(vertexShaderId, fragShaderId)
        ShaderHelper.validateProgram(programId)
        GLES20.glUseProgram(programId)

        aColorLocation = GLES20.glGetAttribLocation(programId, A_COLOR)
        aPositionLocation = GLES20.glGetAttribLocation(programId, A_POSITION)
        //告诉OpenGL到哪里找到属性a_Position对应的数据
        vertexData?.position(0)
        GLES20.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GLES20.GL_FLOAT, false, STRIDE, vertexData)
        GLES20.glEnableVertexAttribArray(aPositionLocation)

        vertexData?.position(POSITION_COMPONENT_COUNT)
        GLES20.glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GLES20.GL_FLOAT, false, STRIDE, vertexData)
        GLES20.glEnableVertexAttribArray(aColorLocation)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        //set the opengl viewport to fill the entire surface
        GLES20.glViewport(0,0,width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        //clear the rendering surface
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
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