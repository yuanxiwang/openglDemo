package cn.e_xiaofu.opengldemo

import android.content.Context
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import cn.e_xiaofu.opengldemo.util.ResourceReadUtil
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
    private var context:Context? = null
    val POSITION_COMPONENT_COUNT = 2
    val BYTE_PER_FLOAT = 4
    private var vertexData:FloatBuffer? = null
    private var tableVertices = floatArrayOf(
        0.0f, 0.0f,
        0.0f, 14.0f,
        9.0f, 14.0f,
        9.0f, 0.0f
    )
    private var tableVerticesWithTriangle = floatArrayOf(
        // triangle 1
        0.0f, 0.0f,
        9.0f, 14.0f,
        0.0f, 14.0f,
        // triangle 2
        0.0f, 0.0f,
        9.0f, 0.0f,
        9.0f, 14.0f,
        // line 1
        0.0f, 7.0f,
        9.0f, 7.0f,
        //mallets
        4.5f, 2.0f,
        4.5f, 12.0f
    )
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
        GLES20.glClearColor(1.0f, 0.0f,0.0f, 0.0f)
        var vertexShaderCode = context?.let { ResourceReadUtil.readTextFileFromResource(it, R.raw.simple_vertex_shader) }
        var fragmentShaderCode = context?.let { ResourceReadUtil.readTextFileFromResource(it, R.raw.simple_fragment_shader) }

    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        //set the opengl viewport to fill the entire surface
        GLES20.glViewport(0,0,width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        //clear the rendering surface
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
    }
}