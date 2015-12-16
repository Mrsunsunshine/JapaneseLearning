package tan.richo.myview;



import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.ricoh.R;

public class TasksCompletedView extends View {

	// ��ʵ��Բ�Ļ���
	private Paint mCirclePaint;
	// ��Բ���Ļ���
	private Paint mRingPaint;
	// ������Ļ���
	private Paint mTextPaint;
	//������ɵĻ���
	private Paint mStrPaint;
	// Բ����ɫ
	private int mCircleColor;
	// Բ����ɫ
	private int mRingColor;
	// �뾶
	private float mRadius;
	// Բ���뾶
	private float mRingRadius;
	// Բ�����
	private float mStrokeWidth;
	// Բ��x����
	private int mXCenter;
	// Բ��y����
	private int mYCenter;
	// �ֵĳ���
	private float mTxtWidth;
	// �ֵĸ߶�
	private float mTxtHeight;
	// �ܽ���
	private int mTotalProgress = 100;
	// ��ǰ����
	private int mProgress;
	private float mStrWidth;

	public TasksCompletedView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// ��ȡ�Զ��������
		initAttrs(context, attrs);
		initVariable();
	}

	private void initAttrs(Context context, AttributeSet attrs) {
		TypedArray typeArray = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.TasksCompletedView, 0, 0);
		mRadius = typeArray.getDimension(R.styleable.TasksCompletedView_radius, 80);
		mStrokeWidth = typeArray.getDimension(R.styleable.TasksCompletedView_strokeWidth, 10);
		mCircleColor = typeArray.getColor(R.styleable.TasksCompletedView_circleColor, 0xFFFFFFFF);
		mRingColor = typeArray.getColor(R.styleable.TasksCompletedView_ringColor, 0xFFFFFFFF);
		
		mRingRadius = mRadius + mStrokeWidth / 2;
	}

	private void initVariable() {
		mCirclePaint = new Paint();
		mCirclePaint.setAntiAlias(true);
		mCirclePaint.setColor(mCircleColor);
		mCirclePaint.setStyle(Paint.Style.FILL);
		
		mRingPaint = new Paint();
		mRingPaint.setAntiAlias(true);
		mRingPaint.setColor(mRingColor);
		mRingPaint.setStyle(Paint.Style.STROKE);
		mRingPaint.setStrokeWidth(mStrokeWidth);
		
		mTextPaint = new Paint();
		mTextPaint.setAntiAlias(true);
		mTextPaint.setStyle(Paint.Style.FILL);
		mTextPaint.setARGB(255, 0, 80, 255);
		mTextPaint.setTextSize(mRadius / 2);
		
		mStrPaint = new Paint();
		mStrPaint.setAntiAlias(true);
		mStrPaint.setStyle(Paint.Style.FILL);
		mStrPaint.setARGB(255, 0, 0, 0);
		mStrPaint.setTextSize(mRadius / 5);
		
		
		FontMetrics fm = mTextPaint.getFontMetrics();
		mTxtHeight = (int) Math.ceil(fm.descent - fm.ascent);
		
	}

	@Override
	protected void onDraw(Canvas canvas) {

		mXCenter = getWidth() / 2;
		mYCenter = getHeight() / 2;
		
		canvas.drawCircle(mXCenter, mYCenter, mRadius, mCirclePaint);
		
		RectF oval1 = new RectF();
		oval1.left = (mXCenter - mRingRadius);
		oval1.top = (mYCenter - mRingRadius);
		oval1.right = mRingRadius * 2 + (mXCenter - mRingRadius);
		oval1.bottom = mRingRadius * 2 + (mYCenter - mRingRadius);
		mRingPaint.setColor(Color.GRAY);
		canvas.drawArc(oval1, -90, 360, false, mRingPaint); 
		mRingPaint.setColor(mRingColor);
		
		String str = "�����";
		mStrWidth = mTextPaint.measureText(str, 0, str.length());
		canvas.drawText(str, mXCenter-mStrWidth/4  , mYCenter- mRadius / 2, mStrPaint);
		if (mProgress >=0 ) {
			RectF oval = new RectF();
			oval.left = (mXCenter - mRingRadius);
			oval.top = (mYCenter - mRingRadius);
			oval.right = mRingRadius * 2 + (mXCenter - mRingRadius);
			oval.bottom = mRingRadius * 2 + (mYCenter - mRingRadius);
			canvas.drawArc(oval, -90, ((float)mProgress / mTotalProgress) * 360, false, mRingPaint); //
//			canvas.drawCircle(mXCenter, mYCenter, mRadius + mStrokeWidth / 2, mRingPaint);
			String txt = mProgress + "%";
			mTxtWidth = mTextPaint.measureText(txt, 0, txt.length());
			canvas.drawText(txt, mXCenter - mTxtWidth / 2, mYCenter + mTxtHeight / 4, mTextPaint);
		}
	}
	
	public void setProgress(int progress) {
		mProgress = progress;
//		invalidate();
		postInvalidate();
	}

}
