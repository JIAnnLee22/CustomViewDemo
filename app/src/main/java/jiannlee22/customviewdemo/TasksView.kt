package jiannlee22.customviewdemo

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat

class TasksView : View {

    private var mSize = 3
    private var mCurProgress = 1
    private var mIcNormal = R.drawable.ic_normal
    private var mIcDone = R.drawable.ic_done

    private var mIcNormalDraw: Drawable
    private var mIcDoneDraw: Drawable

    private var mCellWidth = 0F
    private var mLashX = 0F

    private var mPaintNormal = Paint()
    private var mPaintDone = Paint()

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        context?.theme?.obtainStyledAttributes(
            attrs,
            R.styleable.TasksView,
            defStyleAttr,
            0
        )?.apply {
            mSize = getInt(R.styleable.TasksView_task_size, mSize)
            mCurProgress = getInt(R.styleable.TasksView_task_progress, mCurProgress)
            mIcNormal = getResourceId(R.styleable.TasksView_task_ic_normal, mIcNormal)
            mIcDone = getResourceId(R.styleable.TasksView_task_ic_done, mIcDone)
            recycle()
        }
        mIcNormalDraw = ContextCompat.getDrawable(context!!, mIcNormal)!!
        mIcDoneDraw = ContextCompat.getDrawable(context, mIcDone)!!

        mPaintNormal.style = Paint.Style.STROKE
        mPaintNormal.color = ContextCompat.getColor(context, R.color.normal)
        mPaintNormal.strokeWidth = 2F

        mPaintDone.style = Paint.Style.STROKE
        mPaintDone.color = ContextCompat.getColor(context, R.color.done)
        mPaintDone.strokeWidth = 2F
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)

        Log.d("TAG", "onMeasure: msize is $mSize")
        mCellWidth = width.toFloat() / mSize.toFloat()
        setMeasuredDimension(width, mCellWidth.toInt())
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawFilter =
            PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG);
        repeat(mSize) {
            canvas?.let { c ->
                val rect = Rect(
                    (it * mCellWidth + mCellWidth / 4).toInt(),
                    (mCellWidth / 4).toInt(),
                    (it * mCellWidth + mCellWidth / 4 + mCellWidth / 2).toInt(),
                    (mCellWidth / 4 + mCellWidth / 2).toInt()
                )
                val isNormal = mCurProgress <= it
                if (isNormal) {
                    mIcNormalDraw.bounds = rect
                    mIcNormalDraw.draw(c)
                } else {
                    mIcDoneDraw.bounds = rect
                    mIcDoneDraw.draw(c)
                }
                if (it != 0) {
                    c.drawLine(
                        mLashX,
                        rect.centerY().toFloat(),
                        rect.left.toFloat(),
                        rect.centerY().toFloat(),
                        if (isNormal) mPaintNormal else mPaintDone
                    )
                }
                mLashX = rect.right.toFloat()
            }
        }
    }

    fun setProgress(progress: Int) {
        mCurProgress = progress
        invalidate()
    }

    fun getProgress() = mCurProgress

    fun getSize() = mSize
}