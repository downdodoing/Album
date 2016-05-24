package com.example.album.mydefineAdapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.album.LoadImage;
import com.example.album.LoadImage.OnCallBackListener;
import com.example.album.R;
import com.example.album.mydefineimageview.MyImageView;
import com.example.album.mydefineimageview.MyImageView.OnMeasureListener;

public class MyDefineShowImageAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList<String> imagePath;
	private GridView gridView;
	private int viewWidth, viewHeight;
	private ArrayList<String> choosed_image;
	private ArrayList<Boolean> isCheckedBox;

	public MyDefineShowImageAdapter(Context context,
			ArrayList<String> imagePath, GridView gridView,
			ArrayList<String> choosed_image) {

		this.inflater = LayoutInflater.from(context);
		this.imagePath = imagePath;
		this.gridView = gridView;
		this.choosed_image = choosed_image;

		isCheckedBox = new ArrayList<Boolean>();
		for (int i = 0; i < getCount(); i++) {
			isCheckedBox.add(false);
		}
	}

	@Override
	public int getCount() {
		return imagePath.size();
	}

	@Override
	public Object getItem(int position) {
		return imagePath.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		final String path = imagePath.get(position);
		if (null == convertView) {
			holder = new Holder();
			convertView = inflater.inflate(R.layout.show_image_item, null);
			holder.image = (MyImageView) convertView
					.findViewById(R.id.show_image_item_image);
			holder.checkbox = (CheckBox) convertView
					.findViewById(R.id.show_image_checkBox);

			holder.image.setOnMeasurelistener(new OnMeasureListener() {
				// 获取ImageView组件的大小
				@Override
				public void OnMeasureSize(int width, int height) {
					viewWidth = width;
					viewHeight = height;
				}
			});
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		holder.image.setTag(path);
		Bitmap bitmap = LoadImage.getInstance().loadImage(path, viewWidth,
				viewHeight, new OnCallBackListener() {

					@Override
					public void setOnCallBackListener(Bitmap bitmap, String uri) {
						ImageView imageView = (ImageView) gridView
								.findViewWithTag(uri);
						if (null != bitmap && null != imageView) {
							imageView.setImageBitmap(bitmap);
						}
					}
				});
		if (null != bitmap) {
			holder.image.setImageBitmap(bitmap);
		} else {
			holder.image.setImageResource(R.drawable.friends_sends_pictures_no);
		}
		holder.checkbox
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {

						isCheckedBox.set(position, isChecked);

						if (isChecked && !choosed_image.contains(path)) {
							choosed_image.add(path);
						} else if (!isChecked && choosed_image.contains(path)) {
							choosed_image.remove(path);
						}
					}
				});
		if (isCheckedBox.get(position)) {
			holder.checkbox.setChecked(true);
		} else {
			holder.checkbox.setChecked(false);
		}
		return convertView;
	}

	static class Holder {
		MyImageView image;
		CheckBox checkbox;
	}
}
