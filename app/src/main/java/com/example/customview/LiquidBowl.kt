package com.example.customview

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.annotation.RequiresApi
import kotlin.properties.Delegates
@RequiresApi(Build.VERSION_CODES.O)
class LiquidBowl(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var viewWith by Delegates.notNull<Float>()
    private var viewHeight by Delegates.notNull<Float>()
    private var currentValue = 0f

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


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        bowl(canvas)
    }

   private fun animator(){
        val valueAnimator = ValueAnimator.ofFloat(-1f, 1f)
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


    private fun bowl(canvas: Canvas?) {
        val gray = Color.GRAY
        val dkGray = Color.DKGRAY
        val ltGRAY = Color.LTGRAY
        val transparentGray = Color.argb(50,220,220,220)
        val waterColor =Color.rgb(0f,190+currentValue,254-currentValue)

        val radius = viewWith / 4
        val circleCenterX = viewWith / 2
        val circleCenterY = viewHeight / 2
        val bowlBottomX = viewWith / 2
        val bowlBottomY = viewHeight / 2 + radius
        val rotateDegrees = 0f + currentValue*20
        val unit = radius / 12
        val paint = Paint()

        canvas?.apply {

            //0.bowl shadow
            paint.color=dkGray
            paint.style=Paint.Style.FILL
            drawOval(circleCenterX-11*unit+rotateDegrees,circleCenterY+11*unit,circleCenterX+11*unit+rotateDegrees,circleCenterY+13*unit,paint)

            rotate(rotateDegrees, bowlBottomX, bowlBottomY)

            //1.bowl
            paint.color = gray
            paint.style = Paint.Style.FILL
            drawCircle(circleCenterX, circleCenterY, radius, paint)

            //2.mouth of bowl
            paint.color = ltGRAY
            paint.strokeWidth = 30f
            paint.style = Paint.Style.STROKE
            paint.setShadowLayer(20.0f, 0.0f, 20.0f, Color.DKGRAY)
            drawOval(
                circleCenterX - 6 * unit,
                circleCenterY - 12 * unit,
                circleCenterX + 6 * unit,
                circleCenterY - 8 * unit,
                paint
            )


            rotate(-rotateDegrees, circleCenterX, circleCenterY)

            //3.liquid
            paint.color=waterColor
            paint.style=Paint.Style.FILL
            paint.setShadowLayer(500.0f, 0.0f, 0.0f, waterColor)
            val oval = RectF(circleCenterX-11*unit, circleCenterY-radius, circleCenterX+11*unit,circleCenterY+11*unit)
            drawArc(oval,0f,180f,true,paint)

            //4.liquid surface
            paint.color=waterColor
            paint.style=Paint.Style.FILL
            drawOval(circleCenterX-11*unit,circleCenterY-unit-10,circleCenterX+11*unit,circleCenterY+unit-10,paint)


            rotate(rotateDegrees, circleCenterX, circleCenterY)

            //5.reflective
            paint.color= transparentGray
            paint.strokeWidth = 15f
            paint.style=Paint.Style.FILL
            paint.clearShadowLayer()
            drawOval(circleCenterX-6*unit,circleCenterY-6*unit,circleCenterX+6*unit,circleCenterY,paint)

        }
    }

}