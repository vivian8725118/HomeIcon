package com.example.backbroundtest.widget.api;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

public class HomeIcon extends View {
	public static enum StyleColor {
		WHITE_ALPHA, PHOTO;
		public int getColor() {
			if (this.equals(WHITE_ALPHA)) {
				return 0X66ffffff;
			} else if (this.equals(PHOTO)) {
				return 0x00ffffff;
			}
			return 0x00ffffff;
		}
	}

	private final Paint mPaint = new Paint();
	private int backRes;
	private int degree = 0;
	private int degree_temp = 0;
	private StyleColor mStyleColor = StyleColor.WHITE_ALPHA;
	private Bitmap mBitmap;
	private long mUpdateMills = 1000;

	public HomeIcon(Context context) {
		super(context);
	}

	@SuppressLint("Recycle")
	public HomeIcon(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setStyle(StyleColor styleColor) {
		mStyleColor = styleColor;
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		// 先画圆形，在把图像画上去，缩放
		int i = getWidth();
		int j = getHeight();
		if (mStyleColor.equals(StyleColor.WHITE_ALPHA)) {
			mPaint.setColor(mStyleColor.getColor());
			mPaint.setStyle(android.graphics.Paint.Style.FILL);
			mPaint.setAntiAlias(true);// 抗锯齿
			canvas.drawCircle(i / 2f, j / 2f, i / 2f - 20, mPaint);// 画圆
			int si = i - 80;
			if (mBitmap == null || mBitmap.isRecycled()) {
				Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
						backRes);
				mBitmap = Bitmap.createScaledBitmap(bitmap, si, si, true);// 指定大小
			}
			try {
				int bh1, bh2;
				if (degree_temp <= 50) {
					bh1 = si * (100 - degree_temp) / 100;
					bh2 = si - bh1;
				} else {
					bh2 = si * degree_temp / 100;
					bh1 = si - bh2;
				}
				float left = i / 2f - mBitmap.getWidth() / 2;
				float top = j / 2f - mBitmap.getHeight() / 2;

				if (bh1 != 0) {
					Bitmap b_top = Bitmap.createBitmap(mBitmap, 0, 0, si, bh1);
					canvas.drawBitmap(b_top, left, top, null);// 画图
				}
				if (bh2 != 0) {
					Bitmap b_bottom = Bitmap.createBitmap(mBitmap, 0, bh1, si,
							bh2);
					b_bottom = getAlphaBitmap(b_bottom, Color.GREEN);
					// 画绿色
					Paint paint_green = new Paint();
					paint_green.setColor(Color.GREEN);
					paint_green.setStyle(Style.FILL);
					canvas.drawBitmap(b_bottom, left, top + bh1, paint_green);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			degree_temp = degree_temp + 5;
			if (degree_temp < degree) {
				handler.sendEmptyMessageDelayed(0, mUpdateMills);
			}

		} else if (mStyleColor.equals(StyleColor.PHOTO)) {
			if (mBitmap == null || mBitmap.isRecycled()) {
				Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
						backRes);
				mBitmap = Bitmap.createBitmap(bitmap);// 指定大小
			}
			mBitmap = getRoundedCornerBitmap(mBitmap);
			mBitmap = Bitmap.createScaledBitmap(mBitmap,
					mBitmap.getWidth() - 30, mBitmap.getHeight() - 30, true);
			canvas.drawBitmap(mBitmap, i / 2 - mBitmap.getWidth() / 2, j / 2
					- mBitmap.getHeight() / 2, null);
			Paint paint = new Paint();
			paint.setColor(0xDDffffff);
			paint.setAntiAlias(true);
			paint.setStyle(Style.STROKE);
			paint.setStrokeWidth(3);
			canvas.drawCircle(i / 2, j / 2, mBitmap.getWidth() / 2 + 20, paint);
			paint.setStrokeWidth(2);
			canvas.drawCircle(i / 2, j / 2, mBitmap.getWidth() / 2 + 35, paint);
			paint.setStrokeWidth(1);
			canvas.drawCircle(i / 2, j / 2, mBitmap.getWidth() / 2 + 50, paint);
		}
	}

	/**
	 * 得到圆角的bitmap
	 * 
	 * @param bitmap
	 * @return Bitmap
	 */
	public Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(outBitmap);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPX = bitmap.getWidth() / 2;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPX, roundPX, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return outBitmap;
	}

	/**
	 * 提取图像Alpha位图,改变不透明部分bitmap的颜色
	 * 
	 * @param mBitmap
	 * @param mColor
	 * @return
	 */
	public static Bitmap getAlphaBitmap(Bitmap mBitmap, int mColor) {
		Bitmap mAlphaBitmap = Bitmap.createBitmap(mBitmap.getWidth(),
				mBitmap.getHeight(), Config.ARGB_8888);
		Canvas mCanvas = new Canvas(mAlphaBitmap);
		Paint mPaint = new Paint();
		mPaint.setColor(mColor);
		// 从原位图中提取只包含alpha的位图
		Bitmap alphaBitmap = mBitmap.extractAlpha();
		// 在画布上（mAlphaBitmap）绘制alpha位图
		mCanvas.drawBitmap(alphaBitmap, 0, 0, mPaint);
		return mAlphaBitmap;
	}

	/**
	 * 设置背景图片id
	 * 
	 * @param res
	 *            integer
	 */
	public void setImageResource(int res) {
		this.backRes = res;
		postInvalidate();
	}

	/**
	 * 设置百分比
	 * 
	 * @param degree
	 *            范围：0-100
	 */
	public void setDegree(int degree) {
		this.degree = degree;
		postInvalidate();
	}

	public void startDraw(long mills) {
		handler.sendEmptyMessageDelayed(0, 1000);
		mUpdateMills = mills;
	}

	private final Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			postInvalidate();
		}
	};
}
