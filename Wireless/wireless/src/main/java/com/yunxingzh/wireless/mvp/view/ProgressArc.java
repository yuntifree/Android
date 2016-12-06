package com.yunxingzh.wireless.mvp.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;

import com.yunxingzh.wireless.R;
import com.yunxingzh.wireless.mview.BView;

import java.lang.ref.WeakReference;

public class ProgressArc extends BView {

    private float startAngle = 0;
    private float sweepAngle = 360;
    private float firstsweepAngle = 0;
    private float secondsweepAngle = 0;
    private float space = 0;

    private RotatePointer pointer;
    private float pointer_rotate = 0;// 由于图片初始位置而引起的偏差

    private int layout_height = 0;
    private int layout_width = 0;

    private float paddingTop = 0;
    private float paddingBottom = 0;
    private float paddingLeft = 0;
    private float paddingRight = 0;

    private int barColor = 0xFF000000;
    private int firstspinColor = 0xFF000000;
    private int secondspinColor = 0xFF000000;
    private int borderColor = 0xFF000000;
    private int circleColor = 0xFFFFFFFF;

    private float barWidth = 0;
    private float borderWidth = 0;
    private float spinWidth = 0;
    private float spinHeight = 0;

    private RectF spinBounds = new RectF();

    private Paint barPaint = new Paint();
    private Paint spinPaint = new Paint();
    private Paint borderPaint = new Paint();
    private Paint circlePaint = new Paint();

    private float progress = 0;
    private float progress_target = 0;

    private boolean clockwise = true;

    private PaintFlagsDrawFilter pfd;

    private int spinSpeed = 1;
    private int delayMillis = 20;
    private SpinHandler spinHandler = new SpinHandler(this);

    private static class SpinHandler extends Handler {
        WeakReference<ProgressArc> view;

        SpinHandler(ProgressArc v) {
            view = new WeakReference<ProgressArc>(v);
        }

        @Override
        public void handleMessage(Message msg) {
        	if(view==null)
        		return ;
            ProgressArc p = view.get();
            if(p==null)
            	return ;
            if (Math.abs(p.progress - p.progress_target) < p.spinSpeed) {
                p.progress = p.progress_target;
                p.invalidate();
            } else {
                if (p.progress > p.progress_target) {
                    p.progress = p.progress - p.spinSpeed;
                    p.invalidate();
                    p.spinHandler.sendEmptyMessageDelayed(0, p.delayMillis);
                } else if (p.progress < p.progress_target) {
                    p.progress = p.progress + p.spinSpeed;
                    p.invalidate();
                    p.spinHandler.sendEmptyMessageDelayed(0, p.delayMillis);
                }
            }
        }
    }

    ;

    public ProgressArc(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttributes(context.obtainStyledAttributes(attrs,
                R.styleable.Progress_Arc));

        pfd = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
                | Paint.FILTER_BITMAP_FLAG);

        spinHandler.sendEmptyMessageDelayed(0, 200);
    }

    private void parseAttributes(TypedArray a) {
        startAngle = a.getFloat(R.styleable.Progress_Arc_startAngle,
                startAngle);
        sweepAngle = a.getFloat(R.styleable.Progress_Arc_sweepAngle,
                sweepAngle);
        firstsweepAngle = a.getFloat(
                R.styleable.Progress_Arc_firstsweepAngle, firstsweepAngle);
        secondsweepAngle = a.getFloat(
                R.styleable.Progress_Arc_secondsweepAngle,
                secondsweepAngle);
        space = a.getFloat(R.styleable.Progress_Arc_space, space);
        barColor = a.getColor(R.styleable.Progress_Arc_barColor, barColor);
        borderColor = a.getColor(R.styleable.Progress_Arc_borderColor,
                borderColor);
        circleColor = a.getColor(R.styleable.Progress_Arc_circleColor,
                circleColor);
        barWidth = a.getDimension(R.styleable.Progress_Arc_barWidth,
                barWidth);
        borderWidth = a.getDimension(R.styleable.Progress_Arc_borderWidth,
                borderWidth);
        firstspinColor = a.getColor(
                R.styleable.Progress_Arc_firstspinColor, firstspinColor);
        secondspinColor = a.getColor(
                R.styleable.Progress_Arc_secondspinColor, secondspinColor);
        spinWidth = a.getDimension(R.styleable.Progress_Arc_spinWidth,
                barWidth);
        spinHeight = a.getDimension(R.styleable.Progress_Arc_spinHeight,
                spinHeight);
        float prog = a.getFloat(R.styleable.Progress_Arc_progress,
                progress_target);
        progress_target = prog * 360 / 100;
        spinSpeed = (int) Math.ceil(prog / 10);
        clockwise = a.getBoolean(R.styleable.Progress_Arc_clockwise,
                clockwise);

        a.recycle();
    }

    private void setupBounds() {
        int minValue = Math.min(layout_width, layout_height);

        int xOffset = layout_width - minValue;
        int yOffset = layout_height - minValue;

        paddingTop = this.getPaddingTop() + (yOffset / 2);
        paddingBottom = this.getPaddingBottom() + (yOffset / 2);
        paddingLeft = this.getPaddingLeft() + (xOffset / 2);
        paddingRight = this.getPaddingRight() + (xOffset / 2);

        spinBounds = new RectF(paddingLeft + (barWidth + borderWidth + spinWidth) / 2 + 1,
                paddingTop + (barWidth + borderWidth + spinWidth) / 2 + 1, layout_width
                - paddingRight - (barWidth + borderWidth + spinWidth) / 2 - 1,
                layout_height - paddingBottom - (barWidth + borderWidth + spinWidth) / 2
                        - 1);
    }

    private void setupPaints() {
        circlePaint.setColor(circleColor);
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Style.FILL);

        barPaint.setColor(barColor);
        barPaint.setAntiAlias(true);
        barPaint.setStyle(Style.STROKE);
        barPaint.setStrokeWidth(barWidth + 1);
        barPaint.setStrokeCap(Cap.ROUND);

        borderPaint.setColor(borderColor);
        borderPaint.setAntiAlias(true);
        borderPaint.setStyle(Style.STROKE);
        borderPaint.setStrokeWidth(borderWidth + 1);

        spinPaint.setColor(firstspinColor);
        spinPaint.setAntiAlias(true);
        spinPaint.setStyle(Style.STROKE);
        spinPaint.setStrokeWidth(spinWidth + borderWidth + 1);
        spinPaint.setStrokeCap(Cap.ROUND);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        layout_width = w;
        layout_height = h;
        draw();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // canvas.drawCircle(
        // circleBounds.width() / 2 + paddingLeft + borderWidth + barWidth,
        // circleBounds.height() / 2 + paddingTop + borderWidth + barWidth,
        // circleBounds.width() / 2, circlePaint);
//		canvas.drawBitmap(mBitmap, paddingLeft + barWidth + borderWidth + 1, paddingTop + barWidth + borderWidth + 1, null) ;
//		canvas.drawBitmap(mBitmap, innerBoundss, innerBounds, null) ;
//		canvas.drawArc(barBounds, startAngle, sweepAngle, false, barPaint);
        if (firstsweepAngle > 0) {
            spinPaint.setColor(firstspinColor);
            canvas.drawArc(spinBounds, startAngle, firstsweepAngle, false,
                    spinPaint);
        }
//		if (borderWidth > 0)
//			canvas.drawArc(borderBounds, 0, 360, false, borderPaint);
//
        if (clockwise)
            if (secondsweepAngle > 0) {
                spinPaint.setColor(secondspinColor);
                canvas.drawArc(spinBounds,
                        startAngle, secondsweepAngle,
                        false, spinPaint);
            } else {
                spinPaint.setColor(secondspinColor);
                canvas.drawArc(spinBounds, startAngle + firstsweepAngle + space,
                        progress, false, spinPaint);
            }
/*        if (clockwise)
            if (secondsweepAngle > 0) {
                spinPaint.setColor(secondspinColor);
                canvas.drawArc(spinBounds,
                        startAngle + firstsweepAngle + space, secondsweepAngle,
                        false, spinPaint);
            } else {
                spinPaint.setColor(secondspinColor);
                canvas.drawArc(spinBounds, startAngle + firstsweepAngle + space,
                        progress, false, spinPaint);
            }*/
        /*
		 * if(clockwise) if(spinHeight > 0) canvas.drawArc(spinBounds, progress
		 * - 90, spinHeight, false, spinPaint); else canvas.drawArc(spinBounds,
		 * -90, progress, false, spinPaint); else if(spinHeight > 0)
		 * canvas.drawArc(spinBounds, -progress - 90, spinHeight, false,
		 * spinPaint); else canvas.drawArc(spinBounds, -90, -progress, false,
		 * spinPaint);
		 */

        canvas.setDrawFilter(pfd);

        canvas.restore();
    }

    public void setProgress(float i) {
        if (i < 0)
            i = 0;
        if (i > 100)
            i = 100;
        progress_target = i * 360 / 100;
        // spinSpeed = (int) Math.ceil(Math.abs(progress_target - progress) /
        // 10);
        spinSpeed = (int) Math.ceil(i / 10);
        spinHandler.sendEmptyMessage(0);
    }

    public void setProgressStatic(float i) {
        progress_target = i;
        progress = i;
        invalidate();
    }

    public float getProgress() {
        return progress * 100 / 360;
    }

    public void setCircleColor(int color) {
        this.circleColor = color;
    }

    public void setBarColor(int color) {
        this.barColor = color;
    }

    public void setBorderColor(int color) {
        this.borderColor = color;
    }

    // public void setSpinColor(int color){
    // this.spinColor = color;
    // }

    public void setBarWidth(float w) {
        this.borderWidth = w;
    }

    public void setBorderWidth(float w) {
        this.borderWidth = w;
    }

    //
    public void setStartAngle(float angle) {
        this.startAngle = angle;
    }

    public float getStartAngle() {
        return this.startAngle;
    }

    public void setSweepAngle(float angle) {
        this.sweepAngle = angle;
    }

    public void setfirstSweepAngle(float angle) {
        this.firstsweepAngle = angle;
        if (pointer != null) {
            pointer.setRotate(angle - pointer_rotate);
        }
        invalidate();
    }

    public void setsecondSweepAngle(float angle) {
        this.secondsweepAngle = angle;
        if (pointer != null) {
            // pointer.setRotate(180 + startAngle + getfirstSweepAngle() +
            // getSpace()
            // + pointer_rotate + angle);
            if (getfirstSweepAngle() == 0 || getsecondSweepAngle() == 0)
                pointer.setRotate(getfirstSweepAngle() + angle - pointer_rotate);
            else
                pointer.setRotate(getfirstSweepAngle() + getSpace() + angle
                        - pointer_rotate);
        }
        invalidate();
    }

    public float getfirstSweepAngle() {
        return this.firstsweepAngle;
    }

    public float getsecondSweepAngle() {
        return this.secondsweepAngle;
    }

    public void setFirstSpinColor(int color) {
        this.firstspinColor = color;
    }

    public void setSecondSpinColor(int color) {
        this.secondspinColor = color;
    }

    public void setSpace(float angle) {
        this.space = angle;
    }

    public float getSweepAngle() {
        return this.sweepAngle;
    }

    public float getSpace() {
        return this.space;
    }

    public void setPointer(RotatePointer pointer) {
        this.pointer = pointer;
    }

    public void setPointerRotate(float rotate) {
        this.pointer_rotate = rotate;
    }

    public void draw() {
        setupBounds();
        setupPaints();
        invalidate();
    }

}