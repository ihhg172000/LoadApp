package com.udacity.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import com.udacity.R
import com.udacity.utils.spToPx
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    enum class State {
        NORMAL,
        LOADING
    }

    private var text = "Download"
    private val textRect = Rect()

    private var colorForText = 0
    private var colorForBackground = 0
    private var colorForProgress = 0
    private var colorForProgressCircle = 0

    private var progress = 0f
        set(value) {
            field = value
            invalidate()
        }

    private val paint = Paint().apply {
        isAntiAlias = true
        textAlignment = TEXT_ALIGNMENT_CENTER
        textSize = 24.spToPx().toFloat()
        typeface = Typeface.DEFAULT
    }

    private val valueAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
        duration = 2500
        repeatMode = ValueAnimator.RESTART
        repeatCount = ValueAnimator.INFINITE
        addUpdateListener {
            progress = animatedValue as Float
        }
    }

    var state: State by Delegates.observable(State.NORMAL) { _, _, newValue ->
        when(newValue) {
            State.NORMAL -> {
                valueAnimator.end()
                text = "Download"
                progress = 0f
            }
            State.LOADING -> {
                valueAnimator.start()
                text = "Downloading"
            }
        }
    }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.LoadingButton,
            0,
            0
        ).apply {
            try {
                colorForText = getColor(R.styleable.LoadingButton_colorForText, context.getColor(R.color.white))
                colorForBackground = getColor(
                    R.styleable.LoadingButton_colorForBackground, context.getColor(
                        R.color.colorPrimary
                    ))
                colorForProgress = getColor(
                    R.styleable.LoadingButton_colorForProgress, context.getColor(
                        R.color.colorPrimaryDark
                    ))
                colorForProgressCircle = getColor(
                    R.styleable.LoadingButton_colorForProgressCircle, context.getColor(
                        R.color.colorAccent
                    ))
            } finally {
                recycle()
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            save()

            drawColor(colorForBackground)

            paint.color = colorForProgress
            drawRect(
                0f,
                0f, (width * progress),
                height.toFloat(),
                paint
            )

            paint.getTextBounds(
                text,
                0,
                text.length,
                textRect)

            paint.color = colorForText
            drawText(
                text,
                (width / 2f) - textRect.centerX(),
                (height / 2f) - textRect.centerY(),
                paint
            )


            val progressCircleRadius = textRect.height() / 3f
            paint.color = colorForProgressCircle
            drawArc(
                (width / 2f) + textRect.centerX() + 20f,
                (height / 2f) - progressCircleRadius,
                (width / 2f) + textRect.centerX() + (progressCircleRadius * 2f) + 20,
                ((height / 2f) - progressCircleRadius) + (progressCircleRadius * 2f),
                0f,
                (360f * progress),
                true,
                paint
            )

            restore()
        }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val minh: Int = paddingTop + paddingBottom + suggestedMinimumHeight
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(minh,  heightMeasureSpec, 1)
        setMeasuredDimension(w, h)
    }
}