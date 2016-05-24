package com.example.album;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.example.album.mydefineAdapter.MyDefineShowImageAdapter;

public class ShowImage extends Activity implements OnClickListener {
	private GridView gridView;
	private TextView back, ok_bnt;
	private ArrayList<String> image_path;
	private ArrayList<String> choosedImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 必须在setcontentview之前
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.show_image);
		init();
	}

	private void init() {
		image_path = getIntent().getExtras().getStringArrayList("data");

		gridView = (GridView) findViewById(R.id.show_image_grid);
		back = (TextView) findViewById(R.id.show_image_back);
		ok_bnt = (TextView) findViewById(R.id.show_image_ok);
		choosedImage = new ArrayList<String>();

		MyDefineShowImageAdapter adapter = new MyDefineShowImageAdapter(this,
				image_path, gridView, choosedImage);
		gridView.setAdapter(adapter);

		back.setOnClickListener(this);
		ok_bnt.setOnClickListener(this);

		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String path = image_path.get(position);

				Intent intent = new Intent();
				intent.setClass(ShowImage.this, Show_Image.class);
				intent.putExtra("path", path);
				startActivity(intent);
			}
		});

	}

	@SuppressLint("NewApi")
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.show_image_back:
			finish();
			break;
		case R.id.show_image_ok:

			Intent intent = new Intent();
			intent.putExtra("data", choosedImage);
			setResult(200, intent);

			finish();
			break;

		default:
			break;
		}
	}

}
