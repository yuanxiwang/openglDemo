package cn.e_xiaofu.opengldemo

import android.app.ActivityManager
import android.content.Context
import android.opengl.GLSurfaceView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    private val glSurfaceView: GLSurfaceView
        get() {
            return GLSurfaceView(this)
        }
    private var renderSet: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val judgeSupportGl20 = judgeSupportGl20()
        if (judgeSupportGl20) {
            glSurfaceView.setEGLContextClientVersion(2)
            glSurfaceView.setRenderer(SimpleRenderer(this))
        } else {
            println("不支持openGL20")
            return
        }
        setContentView(glSurfaceView)
    }

    override fun onResume() {
        super.onResume()
        if (renderSet) {
            glSurfaceView.onResume()
        }
    }

    override fun onPause() {
        super.onPause()
        if (renderSet){
            glSurfaceView.onPause()
        }
    }

    private fun judgeSupportGl20(): Boolean {
        val systemService = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val deviceConfigurationInfo = systemService.deviceConfigurationInfo
        return deviceConfigurationInfo.reqGlEsVersion >= 0x20000
    }
}