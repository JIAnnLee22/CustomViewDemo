package jiannlee22.customviewdemo

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

class CountdownView : View {

    private var mShowProgressText = false
    private var mTextSize = 0F
    private var mTextColor = Color.BLUE
    private var mBorderWidth = 10F
    private var mBorderBgColor = Color.CYAN
    private var mBorderFgColor = Color.BLUE
    private var mProgressMax = 10
    private var mProgressCur = 0
    private var mStartAngle = 0F
    private var mSweepAngle = 360F

    private val mPaintArcBg = Paint()
    private val mPaintArcFg = Paint()
    private val mPaintText = Paint()

    private var size: Int = 0
    private lateinit var rectF: RectF
    private var textBaseline = 0F

    var textFormat: (Int) -> String = { it.toString() }

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        val typedArray =
            context?.theme?.obtainStyledAttributes(
                attrs,
                R.styleable.CustomView,
                defStyleAttr,
                0
            )
        typedArray?.apply {
            mShowProgressText = getBoolean(R.styleable.CustomView_show_text, mShowProgressText)
            mTextSize = getDimension(R.styleable.CustomView_text_size, mTextSize)
            mTextColor = getColor(R.styleable.CustomView_text_color, mTextColor)
            mBorderWidth = getDimension(R.styleable.CustomView_border_width, mBorderWidth)
            mBorderBgColor = getColor(R.styleable.CustomView_border_bg_color, mBorderBgColor)
            mBorderFgColor = getColor(R.styleable.CustomView_border_fg_color, mBorderFgColor)
            mProgressMax = getInt(R.styleable.CustomView_progress_max, mProgressMax)
            mProgressCur = getInt(R.styleable.CustomView_progress_cur, mProgressCur)
            mStartAngle = getFloat(R.styleable.CustomView_start_angle, mStartAngle)
            mSweepAngle = getFloat(R.styleable.CustomView_sweep_angle, mSweepAngle)
            recycle()
        }

        mPaintArcBg.style = Paint.Style.STROKE
        mPaintArcBg.strokeWidth = mBorderWidth
        mPaintArcBg.color = mBorderBgColor
        mPaintArcBg.strokeCap = Paint.Cap.ROUND

        mPaintArcFg.style = Paint.Style.STROKE
        mPaintArcFg.strokeWidth = mBorderWidth
        mPaintArcFg.color = mBorderFgColor
        mPaintArcFg.strokeCap = Paint.Cap.ROUND

        mPaintText.color = mTextColor
        mPaintText.textSize = mTextSize
        mPaintText.textAlign = Paint.Align.CENTER
    }

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val min = min(getSize(widthMeasureSpec), getSize(heightMeasureSpec))
        size = min
        rectF = RectF(
            (mBorderWidth / 2),
            (mBorderWidth / 2),
            (size - mBorderWidth / 2),
            (size - mBorderWidth / 2)
        )
        val dy =
            (mPaintText.fontMetricsInt.bottom - mPaintText.fontMetricsInt.top) / 2F - mPaintText.fontMetricsInt.bottom;
        textBaseline = size / 2F + dy
        setMeasuredDimension(min, min)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawFilter =
            PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG);
        mPaintArcBg.color = mBorderBgColor
        canvas?.drawArc(rectF, mStartAngle, mSweepAngle, false, mPaintArcBg)
        val offset = mSweepAngle * mProgressCur.toFloat() / mProgressMax.toFloat()
        canvas?.drawArc(rectF, mStartAngle, offset, false, mPaintArcFg)
        if (mShowProgressText) {
            canvas?.drawText(textFormat.invoke(mProgressCur), size / 2F, textBaseline, mPaintText)
        }
    }

    fun setProgress(progress: Int) {
        if (progress == mProgressCur || progress > mProgressMax) {
            return
        }
        mProgressCur = progress
        invalidate()
    }

    private fun getSize(measureSpec: Int): Int {
        val mode = MeasureSpec.getMode(measureSpec)
        val size = MeasureSpec.getSize(measureSpec)
        return when (mode) {
            MeasureSpec.AT_MOST -> size
            MeasureSpec.EXACTLY -> size
            else -> size
        }
    }
}