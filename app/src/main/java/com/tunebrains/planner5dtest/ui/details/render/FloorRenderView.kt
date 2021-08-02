package com.tunebrains.planner5dtest.ui.details.render

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import com.tunebrains.planner5dtest.databinding.ViewFloorRenderBinding
import com.tunebrains.planner5dtest.ui.details.FloorData

data class RenderData(
    val width: Float,
    val height: Float,
    val floor: FloorData
)

class FloorRenderView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val callback = FloorRenderCallback()

    private val binding = ViewFloorRenderBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        with(binding) {
            surface.holder.addCallback(callback)
            scaleDown.setOnClickListener {
                callback.scaleDown()
            }
            scaleUp.setOnClickListener {
                callback.scaleUp()
            }
            rotateLeft.setOnClickListener {
                callback.rotateLeft()
            }
            rotateRight.setOnClickListener {
                callback.rotateRight()
            }
        }
    }

    fun update(data: RenderData) {
        callback.update(data)
        with (binding){
            scaleDown.visibility = View.VISIBLE
            scaleUp.visibility = View.VISIBLE
            rotateLeft.visibility = View.VISIBLE
            rotateRight.visibility = View.VISIBLE
        }
    }
    private var isScrolling = false
    private var curX:Float=0f
    private var curY:Float=0f

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return when (ev.actionMasked) {
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                isScrolling = false
                false
            }
            MotionEvent.ACTION_DOWN -> {
                isScrolling = true
                curX = ev.x
                curY = ev.y
                true
            }
            MotionEvent.ACTION_MOVE -> {
                val diffX = ev.x - curX
                val diffY = ev.y - curY
                callback.move(diffX,diffY)
                curX = ev.x
                curY = ev.y

                return isScrolling
            }
            else->false
        }
    }

}