package com.example.album.mydefineAdapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.album.LoadImage;
import com.example.album.R;
import com.example.album.entity.ImageEntity;
import com.example.album.mydefineimageview.MyImageView;
import com.example.album.mydefineimageview.MyImageView.OnMeasureListener;

public class MyDefineMainAdaper extends BaseAdapter {
	private ArrayList<ImageEntity> mData;
	private GridView gridView;
	private LayoutInflater inflater;
	private int width, height;

	public MyDefineMainAdaper(Context context, ArrayList<ImageEntity> mData,
			GridView gridView) {
		inflater = LayoutInflater.from(context);
		this.mData = mData;
		this.gridView = gridView;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Holder holder = null;
		ImageEntity entity = mData.get(position);
		final String path = entity.getTopImagePath();
		if (null == convertView) {
			convertView = inflater.inflate(R.layout.main_grid_item, null);
			holder = new Holder();
			holder.image = (MyImageView) convertView
					.findViewById(R.id.main_grid_image);
			holder.t1 = (TextView) convertView
					.findViewById(R.id.main_grid_showNumber);
			holder.t2 = (TextView) convertView
					.findViewById(R.id.main_grid_showFolder);
			holder.image.setOnMeasurelistener(new OnMeasureListener() {

				@Override
				public void OnMeasureSize(int width, int height) {
					MyDefineMainAdaper.this.width = width;
					MyDefineMainAdaper.this.height = height;
				}
			});
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
			// holder.image.setImageResource(R.drawable.friends_sends_pictures_no);
		}
		holder.image.setTag(path);
		Bitmap bitmap = LoadImage.getInstance().loadImage(path, width, height,
				new LoadImage.OnCallBackListener() {
					// 在没有缓存一张图片时会被调用
					@Override
					public void setOnCallBackListener(Bitmap bitmap, String uri) {
						ImageView image = (ImageView) gridView
								.findViewWithTag(path);
						if (null != image && null != bitmap) {
							image.setImageBitmap(bitmap);
						}
					}
				});
		if (null != bitmap) {
			holder.image.setImageBitmap(bitmap);
		} else {
			holder.image.setImageResource(R.drawable.friends_sends_pictures_no);
		}
		holder.t1.setText(entity.getImageSize() + "");
		holder.t2.setText(mData.get(position).getFolderName());
		return convertView;
	}

	public class Holder {
		MyImageView image;
		TextView t1, t2;
	}
}
