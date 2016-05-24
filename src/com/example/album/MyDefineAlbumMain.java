package com.example.album;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.example.album.entity.ImageEntity;
import com.example.album.mydefineAdapter.MyDefineMainAdaper;

public class MyDefineAlbumMain extends Activity implements Serializable {

	private static final long serialVersionUID = -6849794470754667710L;

	private GridView gridView;
	private Map<String, ArrayList<String>> mAlbumPath = new HashMap<String, ArrayList<String>>();
	private ArrayList<ImageEntity> list;
	private ProgressDialog prog;
	private MyHanlder handler;
	public static MyDefineAlbumMain mainView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mydefinealbummain);
		mainView = this;
		init();
	}

	private void init() {
		prog = ProgressDialog.show(this, null, "正在加载.....");
		prog.show();
		gridView = (GridView) findViewById(R.id.main_grid);
		handler = new MyHanlder();
		getImages();
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String path = list.get(position).getFolderName();
				ArrayList<String> imagePath = mAlbumPath.get(path);

				Intent intent = new Intent();
				intent.setClass(MyDefineAlbumMain.this, ShowImage.class);
				Bundle bundle = new Bundle();
				bundle.putStringArrayList("data", imagePath);
				intent.putExtras(bundle);

				startActivityForResult(intent, 200);
			}

		});
	}

	/**
	 * 获取手机中的图片
	 */
	public void getImages() {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			Toast.makeText(this, "暂无外部存储设备", Toast.LENGTH_SHORT).show();
			return;
		}
		new Thread(new Runnable() {

			@Override
			public void run() {
				Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				// 相当于ping SQL
				Cursor cursor = getContentResolver().query(
						uri,
						null,
						MediaStore.Images.Media.MIME_TYPE + "=? or "
								+ MediaStore.Images.Media.MIME_TYPE + "=?",
						new String[] { "image/jpeg", "image/png" },
						MediaStore.Images.Media.DATE_MODIFIED);
				if (null == cursor) {
					return;
				}
				while (cursor.moveToNext()) {
					// 获取图片路径
					String path = cursor.getString(cursor
							.getColumnIndex(MediaStore.Images.Media.DATA));
					// 获取图片父路径
					String parentPath = new File(path).getParentFile()
							.getName();
					if (mAlbumPath.containsKey(parentPath)) {
						mAlbumPath.get(parentPath).add(path);
					} else {
						ArrayList<String> pathList = new ArrayList<String>();
						pathList.add(path);
						mAlbumPath.put(parentPath, pathList);
					}
				}
				cursor.close();
				handler.sendEmptyMessage(0);
			}
		}).start();
	}

	public ArrayList<ImageEntity> getImageEntity() {
		list = new ArrayList<ImageEntity>();
		Iterator<Map.Entry<String, ArrayList<String>>> it = mAlbumPath
				.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, ArrayList<String>> entry = it.next();
			String key = entry.getKey();
			ArrayList<String> value = entry.getValue();

			ImageEntity entity = new ImageEntity();
			entity.setFolderName(key);
			entity.setImageSize(value.size());
			entity.setTopImagePath(value.get(0));
			list.add(entity);
		}

		return list;

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (200 == resultCode) {
			setResult(200, data);
			finish();
		}
	}

	@SuppressLint("HandlerLeak")
	class MyHanlder extends Handler {
		@Override
		public void handleMessage(Message msg) {
			if (0 == msg.what) {
				prog.dismiss();
				MyDefineMainAdaper adapter = new MyDefineMainAdaper(
						getApplicationContext(), getImageEntity(), gridView);
				gridView.setAdapter(adapter);
			}
		}
	}

}
