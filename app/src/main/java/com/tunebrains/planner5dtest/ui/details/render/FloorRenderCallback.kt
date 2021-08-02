package com.tunebrains.planner5dtest.ui.details.render

import android.graphics.*
import android.view.SurfaceHolder
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

class FloorRenderCallback : SurfaceHolder.Callback {
    private var drawingThread: DrawingThread? = null
    override fun surfaceCreated(holder: SurfaceHolder) {
        drawingThread?.finish()
        drawingThread = DrawingThread(holder).apply {
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
        drawingThread?.scale?.get()?.let {
            drawingThread?.scale?.set(it + 0.2f)
        }
    }

    fun scaleDown() {
        drawingThread?.scale?.get()?.let {
            drawingThread?.scale?.set(it - 0.2f)
        }
    }

    fun move(diffX: Float, diffY: Float) {
        drawingThread?.translation?.get()?.let {
            drawingThread?.translation?.set(PointF(it.x + diffX, it.y + diffY))
        }
    }

    fun rotateLeft() {
        drawingThread?.rotation?.get()?.let {
            drawingThread?.rotation?.set(it - 45f)
        }
    }

    fun rotateRight() {
        drawingThread?.rotation?.get()?.let {
            drawingThread?.rotation?.set(it + 45f)
        }
    }

    class DrawingThread(
        private val holder: SurfaceHolder
    ) : Thread() {
        private val atomicBoolean = AtomicBoolean(true)
        private val atomicReference = AtomicReference<RenderData>(null)
        val scale = AtomicReference(1f)
        val rotation = AtomicReference(0f)
        val translation = AtomicReference(PointF(0f, 0f))

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
                        render(canvas, data)
                } finally {
                    holder.unlockCanvasAndPost(canvas)
                }
                sleep(100)
            }
        }

        private val wallPaint = Paint().apply {
            color = Color.BLACK
        }

        private fun render(canvas: Canvas, data: RenderData) {
            translation.get().let {
                canvas.translate(it.x, it.y)
            }
            val scale = scale.get()
            canvas.rotate(rotation.get())
            canvas.scale(scale, scale)
            data.floor.walls.forEach { room ->
                room.walls.forEach { wallData ->
                    val start = wallData.points[0]
                    val end = wallData.points[1]
                    canvas.drawRect(
                        RectF(
                            room.x + start.x,
                            room.y + start.y,
                            room.x + end.x + wallData.width,
                            room.y + end.y + wallData.width
                        ), wallPaint
                    )
                }
            }
        }

        fun update(data: RenderData) {
            val scale = width / data.width
            this.scale.set(scale)
            this.translation.set(PointF(0f, 0f))
            atomicReference.set(data)
        }

        fun update(width: Int, height: Int) {
            this.width = width
            this.height = height
        }
    }
}