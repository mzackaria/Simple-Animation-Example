package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private val valueAnimator = ValueAnimator.ofFloat(0f, 1f)

    private val textSize = context.resources.getDimension(R.dimen.default_text_size)
    private val radiusLoadingArc = context.resources.getDimension(R.dimen.radius_loading_arc)
    private val marginLeftLoadingArc = context.resources.getDimension(R.dimen.margin_left_loading_arc)
    private val colorBackground = context.getColor(R.color.colorPrimary)
    private val colorLoading = context.getColor(R.color.colorPrimaryDark)
    private val colorArc = context.getColor(R.color.colorAccent)

    private val paint = Paint().also {
        // Smooth out edges of what is drawn without affecting shape.
        it.isAntiAlias = true
        it.color = colorBackground
        it.style = Paint.Style.FILL_AND_STROKE
        it.textAlign = Paint.Align.CENTER
        it.textSize = textSize
    }

    private lateinit var label : String

    private lateinit var mainRect : Rect
    private lateinit var loadingRect : Rect
    private val textBound = Rect()

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        setUp()
        invalidate()
    }


    init {
        setUp()
        valueAnimator.duration =  2000
        valueAnimator.interpolator = AccelerateDecelerateInterpolator()
        valueAnimator.addUpdateListener {
            setUp()
            invalidate()
            if (it.animatedValue == 1f) {
                buttonState = ButtonState.Completed
            }
        }
    }

    private fun setUp() {
        if (buttonState == ButtonState.Completed) label = context.getString(R.string.download)
        else if (buttonState == ButtonState.Loading) {
            label = context.getString(R.string.we_are_loading)
            loadingRect = Rect(
                0,
                0,
                (widthSize * valueAnimator.animatedFraction).toInt(),
                heightSize
            )
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mainRect = Rect(0,0, widthSize, heightSize)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //draw the button rect
        paint.color =  colorBackground
        canvas.drawRect(mainRect, paint)

        if (buttonState == ButtonState.Loading) {
            //draw loading rectangle
            paint.color =  colorLoading
            canvas.drawRect(loadingRect, paint)

            //drawing the loading circle
            paint.color =  colorArc
            paint.getTextBounds(label, 0, label.length, textBound)

            val left = textBound.right.toFloat()/2 + widthSize/2 + marginLeftLoadingArc
            val top = textBound.top.toFloat() + heightSize/2
            val right = left + 2 * radiusLoadingArc
            val bottom = top + 2 * radiusLoadingArc

            canvas.drawArc(left, top, right, bottom,
            0f, 360*valueAnimator.animatedFraction,
            true, paint)

        }

        //draw text
        paint.color =  Color.WHITE
        canvas.drawText(label, widthSize/2f, heightSize/2f + textSize/2f, paint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

    override fun performClick(): Boolean {
        return if (buttonState == ButtonState.Completed) {
            valueAnimator.start()
            buttonState = ButtonState.Loading
            super.performClick()
        } else false
    }

}