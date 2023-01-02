package jiannlee22.customviewdemo

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min

class CustomView : View {

    private var mShowProgressText = false
    private var mTextSize = 0F
    private var mBorderWidth = 10F
    private var mBorderBgColor = Color.CYAN
    private var mBorderFgColor = Color.BLUE
    private var mProgressMax = 10
    private var mProgressCur = 0

    private val mPaintArcBg = Paint()
    private val mPaintArcFg = Paint()
    private val mPaintText = Paint()

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        val typedArray =
            context?.theme?.obtainStyledAttributes(attrs, R.styleable.CustomView, defStyleAttr, 0)
        typedArray?.apply {
            mShowProgressText = getBoolean(R.styleable.CustomView_show_progress, mShowProgressText)
            mTextSize = getDimension(R.styleable.CustomView_text_size, mTextSize)
            mBorderWidth = getDimension(R.styleable.CustomView_border_width, mBorderWidth)
            mBorderBgColor = getColor(R.styleable.CustomView_border_bg_color, mBorderBgColor)
            mBorderFgColor = getColor(R.styleable.CustomView_border_fg_color, mBorderFgColor)
            mProgressMax = getInt(R.styleable.CustomView_progress_max, mProgressMax)
            mProgressCur = getInt(R.styleable.CustomView_progress_cur, mProgressCur)
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

        mPaintText.color = mBorderFgColor
        mPaintText.textSize = mTextSize
        mPaintText.textAlign = Paint.Align.CENTER
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val min = min(getSize(widthMeasureSpec), getSize(heightMeasureSpec))
        val r = (min - mBorderWidth / 2) / 2
        val h = r - r * cos(90 * Math.PI / 180 / 2) - 2
        setMeasuredDimension(min, min - h.toInt())
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val size = max(width, height)
        val rectF = RectF(
            (mBorderWidth / 2),
            (mBorderWidth / 2),
            (size - mBorderWidth / 2),
            (size - mBorderWidth / 2)
        )
        mPaintArcBg.color = mBorderBgColor
        canvas?.drawArc(rectF, 135F, 270F, false, mPaintArcBg)
        val offset = 270 * mProgressCur.toFloat() / mProgressMax.toFloat()
        canvas?.drawArc(rectF, 135F, offset, false, mPaintArcFg)
        canvas?.drawText(mProgressCur.toString(), size / 2F, size / 2F, mPaintText)
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