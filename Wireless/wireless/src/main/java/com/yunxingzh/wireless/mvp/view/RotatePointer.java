package com.yunxingzh.wireless.mvp.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.widget.ImageView;

import java.lang.ref.WeakReference;
import com.yunxingzh.wireless.R;

public class RotatePointer extends ImageView {

	// private Bitmap bitmap;
	//private BitmapDrawable bmd;
	private Drawable bmd;
	private float rotate;
	
	private Bitmap bkgndBmp	;
	private int spinSpeed = 1;
    private int delayMillis = 20;
    
	private float startAngle = 0;
    private float sweepAngle = 180;
    private float firstsweepAngle = 0;
    private float secondsweepAngle = 0;
    private float space = 0;
    
    private int layout_height = 0;
    private int layout_width = 0;

    private float paddingTop = 0;
    private float paddingBottom = 0;
    private float paddingLeft = 0;
    private float paddingRight = 0;
    
    private float progress = 0;
    private float progress_target = 0;

    private boolean clockwise = true;

    private PaintFlagsDrawFilter pfd;
    
    private RotatePointer pointer;
    private float pointer_rotate = 0;// 由于图片初始位置而引起的偏差
    
	private float barWidth = 0;
    private float borderWidth = 0;
    private float spinWidth = 0;
    private float spinHeight = 0;

    private RectF spinBounds = new RectF();
    
	private Paint barPaint = new Paint();
    private Paint spinPaint = new Paint();
    private Paint borderPaint = new Paint();
    private Paint circlePaint = new Paint();
    private Paint whitePaint = new Paint();
    
    private int barColor = 0xFF000000;
    private int firstspinColor = 0x00000000;
    private int secondspinColor = 0xFF000000;
    private int borderColor = 0xFF000000;
    private int circleColor = 0xFFFFFFFF;
    
    private SpinHandler spinHandler ;

    private static class SpinHandler extends Handler {
        WeakReference<RotatePointer> view;

        SpinHandler(RotatePointer v) {
            view = new WeakReference<RotatePointer>(v);
        }

        @Override
        public void handleMessage(Message msg) {
        	if(view==null)
        		return ;
        	RotatePointer p = view.get();
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
    };
	
	public RotatePointer(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		parseAttributes(context.obtainStyledAttributes(attrs,
				R.styleable.RotatePointer));
		
		parseArcNewAttributes(context.obtainStyledAttributes(attrs,
                R.styleable.Progress_Arc));
		pfd = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
                | Paint.FILTER_BITMAP_FLAG);
		
		spinHandler = new SpinHandler(this);
		spinHandler.sendEmptyMessageDelayed(0, 200);
	}
		
	private void parseArcNewAttributes(TypedArray a)
	{
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
        progress_target = prog * 180 / 100;
        spinSpeed = (int) Math.ceil(prog / 10);
        clockwise = a.getBoolean(R.styleable.Progress_Arc_clockwise,
                clockwise);

        a.recycle();
	}

	public void setRotate(float rotate){
		this.rotate = rotate;
		invalidate();
	}
	
	
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		
		layout_width = w;
        layout_height = h;
        
        this.setupBounds();
        this.setupPaints();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int mTempWidth = bmd.getIntrinsicWidth();
		int mTempHeigh = bmd.getIntrinsicHeight();
		int centerX = this.getWidth() / 2;
		int centerY = this.getHeight() / 2;
				
		canvas.save();
		
		//RectF spinBounds = new RectF(0,0,getWidth(),getHeight());
		
		if(this.bkgndBmp==null || bkgndBmp.isRecycled())
			bkgndBmp = BitmapFactory.decodeResource(getResources(), R.drawable.speed_test);

        //改变指针整体大小
		bmd.setBounds(centerX - (mTempWidth / 4), centerY
				- (mTempHeigh / 4),  centerX + (mTempWidth / 4),
				centerY + (mTempHeigh / 4));
		
		//绘制刻度
//		if(bkgndBmp!=null && bkgndBmp.isRecycled()==false)
//			canvas.drawBitmap(bkgndBmp, centerX - bkgndBmp.getWidth()/2,centerY -  bkgndBmp.getHeight(), null);
				
		//绘制背景
		DrawAngle(canvas);
		
		//绘制刻度
		if(bkgndBmp!=null && bkgndBmp.isRecycled()==false)
			canvas.drawBitmap(bkgndBmp, centerX - bkgndBmp.getWidth()/2,centerY -  bkgndBmp.getHeight(), spinPaint);
		
		//绘制指针
		canvas.rotate(rotate, centerX, centerY);
		bmd.draw(canvas);
		canvas.restore();		
	}
	
	private void setupPaints() {
        circlePaint.setColor(circleColor);
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Style.FILL);
        
        whitePaint.setColor(Color.WHITE);

        barPaint.setColor(barColor);
        barPaint.setAntiAlias(true);
        barPaint.setStyle(Style.FILL);
        barPaint.setStrokeWidth(barWidth + 1);
        barPaint.setStrokeCap(Cap.ROUND);

        borderPaint.setColor(borderColor);
        borderPaint.setAntiAlias(true);
        borderPaint.setStyle(Style.FILL);
        borderPaint.setStrokeWidth(borderWidth + 1);

        spinPaint.setColor(firstspinColor);
        spinPaint.setAntiAlias(true);
        spinPaint.setStyle(Style.FILL);
        spinPaint.setStrokeWidth(spinWidth + borderWidth + 1);
        //spinPaint.setStrokeCap(Cap.ROUND);
    }
	
	private void parseAttributes(TypedArray a) {
		
		bmd = a.getDrawable(R.styleable.RotatePointer_rotateImageDrawable);
		a.recycle();
    }
	
	private void setupBounds() {
        int minValue = Math.min(layout_width, layout_height);

        int xOffset = layout_width - minValue;
        int yOffset = layout_height - minValue;

        //调整扇形大小
        paddingTop = this.getPaddingTop() + (yOffset / 2);
        paddingBottom = this.getPaddingBottom() + (yOffset / 2);
        paddingLeft = this.getPaddingLeft() + (xOffset / 2);
        paddingRight = this.getPaddingRight() + (xOffset / 2);

        spinBounds = new RectF(paddingLeft + (barWidth + borderWidth + spinWidth) / 2 ,
                paddingTop + (barWidth + borderWidth + spinWidth) / 2 , layout_width
                - paddingRight - (barWidth + borderWidth + spinWidth) / 2 ,
                layout_height - paddingBottom - (barWidth + borderWidth + spinWidth) / 2
                        );
    }
	
	protected void DrawAngle(Canvas canvas)
	{
		canvas.save();
        // canvas.drawCircle(
        // circleBounds.width() / 2 + paddingLeft + borderWidth + barWidth,
        // circleBounds.height() / 2 + paddingTop + borderWidth + barWidth,
        // circleBounds.width() / 2, circlePaint);
//		canvas.drawBitmap(mBitmap, paddingLeft + barWidth + borderWidth + 1, paddingTop + barWidth + borderWidth + 1, null) ;
//		canvas.drawBitmap(mBitmap, innerBoundss, innerBounds, null) ;
//		canvas.drawArc(barBounds, startAngle, sweepAngle, false, barPaint);
        if (firstsweepAngle > 0) {
            spinPaint.setColor(firstspinColor);
            canvas.drawArc(spinBounds, startAngle, firstsweepAngle, true,
                    spinPaint);
        }
//		if (borderWidth > 0)
//			canvas.drawArc(borderBounds, 0, 360, false, borderPaint);
//
        if (clockwise)
            if (secondsweepAngle > 0) {
                spinPaint.setColor(secondspinColor);
                canvas.drawArc(spinBounds,
                        180, rotate,
                        true, spinPaint);
            } else {
                spinPaint.setColor(secondspinColor);
                canvas.drawArc(spinBounds, 180,
                		rotate, true, spinPaint);
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
	
	public void setPointer(RotatePointer pointer) {
        this.pointer = pointer;
    }
	
	public void setPointerRotate(float rotate) {
        this.pointer_rotate = rotate;
    }
	
	public void setsecondSweepAngle(float angle) {
//        Logger.e("SpeedTestActivityNew", "angle:" + angle);
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
    
    public void setfirstSweepAngle(float angle) {
        this.firstsweepAngle = angle;
        if (pointer != null) {
            pointer.setRotate(angle - pointer_rotate);
        }
        invalidate();
    }
}
