package com.example.customview

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.annotation.RequiresApi
import kotlin.properties.Delegates


@RequiresApi(Build.VERSION_CODES.O)
class View(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var viewWith by Delegates.notNull<Float>()
    private var viewHeight by Delegates.notNull<Float>()
    private var currentValue = 0f
    private var paint = Paint()
    private var bitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.logo_small);
    private var camera: Camera = Camera()

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        animator()

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        viewWith = w.toFloat()

        val minh: Int = MeasureSpec.getSize(w) - rootView.width + paddingBottom + paddingTop
        val h: Int = resolveSizeAndState(minh, heightMeasureSpec, 0)
        viewHeight = h.toFloat()
        setMeasuredDimension(w, h)
    }

    private fun animator() {
        val valueAnimator = ValueAnimator.ofFloat(0f, 1f)
        valueAnimator.apply {
            duration = 2000
            repeatMode = ValueAnimator.REVERSE
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener {
                currentValue = it.animatedValue as Float
                postInvalidate()
            }
            start()
        }
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var degree= 180*currentValue
        val bitmapWidth = bitmap.width
        val bitmapHeight = bitmap.height
        val centerX = viewWith / 2.toFloat()
        val centerY = viewHeight / 2.toFloat()
        val x = (centerX - bitmapWidth / 2)
        val y = (centerY - bitmapHeight / 2)

        // 第一遍绘制：上半部分
//        canvas!!.save()
        canvas?.clipRect(0f, 0f, width.toFloat(), centerY)
        canvas?.drawBitmap(bitmap, x, y, paint)
        canvas?.restore()

        // 第二遍绘制：下半部分
        canvas?.save()

        if (degree < 90) {
            canvas?.clipRect(0f, centerY, viewWith, viewHeight)
        } else {
            canvas?.clipRect(0f, 0f, viewWith, centerY)
        }
//        camera.save()
        camera.rotateX(degree)
        canvas?.translate(centerX, centerY)
        camera.applyToCanvas(canvas)
        canvas?.translate((-centerX), (-centerY))
        camera.restore()

        canvas?.drawBitmap(bitmap, x, y, paint)
        canvas?.restore()
    }



}