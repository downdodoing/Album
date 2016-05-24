package com.example.album.mydefineimageview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class MyImageView extends ImageView {

	private OnMeasureListener onMeasurelistener;

	public MyImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// ���ڲ���ImageVIew�ؼ��Ĵ�С
		if (null != onMeasurelistener) {
			onMeasurelistener.OnMeasureSize(getMeasuredWidth(),
					getMeasuredHeight());
		}
	}

	public void setOnMeasurelistener(OnMeasureListener onMeasurelistener) {
		this.onMeasurelistener = onMeasurelistener;
	}

	public interface OnMeasureListener {
		public void OnMeasureSize(int width, int height);
	}
}
