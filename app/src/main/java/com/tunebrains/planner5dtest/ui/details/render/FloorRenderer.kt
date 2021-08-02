package com.tunebrains.planner5dtest.ui.details.render

import android.graphics.*
import com.tunebrains.planner5dtest.ui.details.RoomData
import java.util.concurrent.atomic.AtomicReference

class FloorRenderer {
    val scale = AtomicReference(1f)
    val rotation = AtomicReference(0f)
    private val path = Path()
    private val rect = RectF()
    val translation = AtomicReference(PointF(0f, 0f))
    val size = AtomicReference(PointF(0f, 0f))
    private val wallActive = Paint().apply {
        color = Color.BLACK
    }

    private val roomNormal = Paint().apply {
        color = Color.argb(50, 255, 0, 0)
        style = Paint.Style.FILL
    }

    fun render(canvas: Canvas, data: RenderData) {


        data.floor.rooms.forEach { room ->
            val c = canvas.save()
            val matrix = applyTransformation()

            canvas.concat(matrix)
            drawRoom(
                room, canvas
            )
            canvas.restoreToCount(c)
        }
    }

    private fun applyTransformation(): Matrix {
        val scale = scale.get()
        val matrix = Matrix()

        matrix.postScale(scale, scale)
        matrix.postRotate(rotation.get())

        translation.get().let { tr ->
            matrix.postTranslate(tr.x, tr.y)
        }
        return matrix
    }

    private fun drawRoom(
        room: RoomData,
        canvas: Canvas
    ) {
        path.reset()
        room.walls.forEachIndexed { index, wallData ->
            val start = wallData.points[0]
            val end = wallData.points[1]

            val startX = room.x + start.x
            val startY = room.y + start.y

            if (index == 0)
                path.moveTo(startX, startY)
            else {
                path.lineTo(startX, startY)
            }
            val endX = room.x + end.x
            val endY = room.y + end.y
            path.lineTo(endX, endY)
        }
        path.close()
        canvas.drawPath(path, roomNormal)

        room.walls.filterNot { it.hidden }.forEach { wallData ->
            val start = wallData.points[0]
            val end = wallData.points[1]
            rect.set(
                room.x + start.x,
                room.y + start.y,
                room.x + end.x + wallData.width,
                room.y + end.y + wallData.width
            )
            canvas.drawRect(
                rect, wallActive
            )
        }
    }
}