package uk.co.chrisjenx.calligraphy.sample;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author Dmitriy Tarasov
 */
public class CustomViewWithTypefaceSupport extends View {

    private Paint paint;
    private Rect textBounds;
    private int width;
    private int height;

    public CustomViewWithTypefaceSupport(Context context) {
        super(context);
        init();
    }

    public CustomViewWithTypefaceSupport(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomViewWithTypefaceSupport(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomViewWithTypefaceSupport(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setTextSize(50);
        textBounds = new Rect();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        String text = "This is a custom view with setTypeface support";
        Paint.FontMetrics fm = paint.getFontMetrics();
        paint.getTextBounds(text, 0, text.length(), textBounds);

        width = textBounds.left + textBounds.right + getPaddingLeft() + getPaddingRight();
        height = (int) (Math.abs(fm.top) + fm.bottom);

        canvas.drawText(text, 0, -fm.top + getPaddingTop(), paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(width, height);
    }

    /**
     * Used by Calligraphy to change view's typeface
     */
    @SuppressWarnings("unused")
    public void setTypeface(Typeface tf) {
        paint.setTypeface(tf);
        invalidate();
    }
}
