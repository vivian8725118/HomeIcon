package com.example.backbroundtest.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class HomeIcon extends View{
	public static enum Style{
		
	}
	public static enum StyleColor{
		WHITE_ALPHA,TRASPARENT;
		public int getColor(){
			if(this.equals(WHITE_ALPHA)){
				return 0X66ffffff;
			}else if(this.equals(TRASPARENT)){
				return 0x00ffffff;
			}
			return 0x00ffffff;
		}
	}
	private final Paint mPaint = new Paint();
	private int backRes;
	private StyleColor mStyleColor = StyleColor.WHITE_ALPHA;
	private Bitmap mBitmap;
	public HomeIcon(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		int i=getWidth();
		int j=getHeight();
		
		mPaint.setColor(mStyleColor.getColor());
		mPaint.setStyle(android.graphics.Paint.Style.FILL);
		mPaint.setAntiAlias(true);//øπæ‚≥›
		canvas.drawCircle(i / 2f, j / 2f, i / 2f, mPaint);//ª≠‘≤
		if(mBitmap == null || mBitmap.isRecycled()){	
		 Bitmap	bitmap = BitmapFactory.decodeResource(getResources(), backRes);
		 int bi = bitmap.getWidth();
		 int bj = bitmap.getHeight();
		 int si = i - 40;
		 int sj = j - 40;
		 mBitmap = Bitmap.createScaledBitmap(bitmap, si, si, true);//÷∏∂®¥Û–°
		}
		canvas.drawBitmap(mBitmap, 20, 20, null);//ª≠Õº
	}
	public void setImageResource(int res){
		this.backRes = res;
		postInvalidate();
	}
	
}
