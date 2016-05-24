package com.example.album;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;

import com.example.album.LoadImage.OnCallBackListener;
import com.example.album.mydefineimageview.MyImageView;
import com.example.album.mydefineimageview.MyImageView.OnMeasureListener;

public class Show_Image extends Activity {

	private int mWidth;
	private int mHeight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.show_single_image);

		String path = this.getIntent().getStringExtra("path");
		MyImageView image = (MyImageView) this.findViewById(R.id.s_s_image);

		image.setOnMeasurelistener(new OnMeasureListener() {

			@Override
			public void OnMeasureSize(int width, int height) {
				mWidth = width;
				mHeight = height;
			}
		});

		Bitmap bm = LoadImage.getInstance().loadImage(path, mWidth, mHeight,
				new OnCallBackListener() {

					@Override
					public void setOnCallBackListener(Bitmap bitmap, String uri) {

					}
				});
		image.setImageBitmap(bm);
	}
}
