package jiannlee22.customviewdemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;


/**
 * 仪表盘
 *
 * @author xiaolin
 * time 2022-08-09 12:20
 */
public class NetworkDashboard extends View {


    private final Path path = new Path();
    private Paint mPaint;
    private Shader shader;
    private float mStrokeWidth = 0;
    private float padding = 10;
    private int percent = 100;

    public NetworkDashboard(Context context) {
        super(context);
        init(context, null);
    }

    public NetworkDashboard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public NetworkDashboard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);

        if (attrs != null) {
            TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.NetworkDashboard, 0, 0);
            try {
                percent = attributes.getInt(R.styleable.NetworkDashboard_percent, 50);
            } finally {
                attributes.recycle();
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mStrokeWidth = (Math.min(w, h) * 0.08F);
        mPaint.setStrokeWidth(mStrokeWidth);

        int[] colors = {Color.BLACK, Color.RED, Color.GREEN};
        shader = new SweepGradient(w / 2f, h, colors, null);
        mPaint.setShader(shader);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawColor(Color.WHITE);

        int w = getWidth();
        int h = getHeight();

        float centerX = w / 2F;
        float centerY = h * 0.8F;
        float circleR = Math.min(w / 2F - padding - mStrokeWidth,
                centerY - padding - mStrokeWidth - dpToPx(10F, getContext())) * 0.9F;
        float halfStroke = mStrokeWidth / 2F;

        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setShader(shader);
        canvas.drawArc(padding + halfStroke, padding + halfStroke, w - padding - halfStroke, centerY * 2, -180, 180, false, mPaint);


        canvas.save();
        float degrees;
        if (percent < 50) {
            degrees = -90F * (50F - percent) / 50F;
        } else {
            degrees = 90F * (percent - 50F) / 50F;
        }
        canvas.rotate(degrees, centerX, centerY);
        float r = circleR * 0.05F;
        path.reset();
        path.moveTo(centerX - r, centerY);
        path.lineTo(centerX + r, centerY);
        path.lineTo(centerX + r * 0.3F, centerY - circleR);
        path.lineTo(centerX - r * 0.3F, centerY - circleR);
        path.close();
        mPaint.setShader(null);
        mPaint.setColor(0xFFDBD5D5);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawPath(path, mPaint);

        mPaint.setColor(0xFFDBD5D5);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(centerX, centerY, circleR * 0.12F, mPaint);
        canvas.restore();

//        mPaint.setColor(0xFF000000);
//        mPaint.setStrokeWidth(1);
//        mPaint.setStyle(Paint.Style.STROKE);
//        canvas.drawLine(0, centerY, w, centerY, mPaint);
//        canvas.drawLine(centerX, 0, centerX, h, mPaint);
    }

    /**
     * 设置百分比 0-100
     */
    public void setPercent(int percent) {
        this.percent = Math.min(Math.max(percent, 0), 100);
        invalidate();
    }

    /**
     * dp转为像素
     *
     * @param dp      长度
     * @param context 资源
     * @return 像素
     */
    public float dpToPx(float dp, Context context) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }


}
