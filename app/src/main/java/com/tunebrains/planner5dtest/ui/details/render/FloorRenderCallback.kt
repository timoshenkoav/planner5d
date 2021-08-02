package com.tunebrains.planner5dtest.ui.details.render

import android.graphics.*
import android.view.SurfaceHolder
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

class FloorRenderCallback : SurfaceHolder.Callback {
    private var drawingThread: DrawingThread? = null
    private val renderer = FloorRenderer()
    override fun surfaceCreated(holder: SurfaceHolder) {
        drawingThread?.finish()
        drawingThread = DrawingThread(holder, renderer).apply {
            start()
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        drawingThread?.update(width, height)
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        drawingThread?.finish()
    }

    fun update(data: RenderData) {
        drawingThread?.update(data)
    }

    fun scaleUp() {
        renderer.scale.get()?.let {
            renderer.scale.set(it + 0.2f)
        }
    }

    fun scaleDown() {
        renderer.scale.get()?.let {
            renderer.scale.set(it - 0.2f)
        }
    }

    fun move(diffX: Float, diffY: Float) {
        renderer.translation.get()?.let {
            renderer.translation.set(PointF(it.x + diffX, it.y + diffY))
        }
    }

    fun rotateLeft() {
        renderer.rotation.get()?.let {
            renderer.rotation.set(it - 45f)
        }
    }

    fun rotateRight() {
        renderer.rotation.get()?.let {
            renderer.rotation.set(it + 45f)
        }
    }

    class DrawingThread(
        private val holder: SurfaceHolder,
        private val renderer:FloorRenderer
    ) : Thread() {
        private val atomicBoolean = AtomicBoolean(true)
        private val atomicReference = AtomicReference<RenderData>(null)

        var width = 0
        var height = 0
        fun finish() {
            atomicBoolean.set(false)
        }

        override fun run() {
            super.run()
            while (atomicBoolean.get()) {
                val canvas = holder.lockCanvas() ?: continue
                try {
                    canvas.drawColor(Color.WHITE)
                    val data = atomicReference.get()
                    if (data != null)
                        renderer.render(canvas, data)
                } finally {
                    holder.unlockCanvasAndPost(canvas)
                }
                sleep(100)
            }
        }



        fun update(data: RenderData) {
            val scale = height / data.height
            this.renderer.scale.set(scale)
            this.renderer.translation.set(PointF(0f,0f))
            this.renderer.size.set(PointF(data.width,data.height))
            atomicReference.set(data)
        }

        fun update(width: Int, height: Int) {
            this.width = width
            this.height = height
        }
    }
}