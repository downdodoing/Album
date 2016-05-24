package com.example.album;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Handler;
import android.os.Message;
import android.support.v4.util.LruCache;

public class LoadImage {
	private LruCache<String, Bitmap> mBitmapLruCache;
	// 线程池
	private ExecutorService mEservice;
	private static LoadImage mLoadImage;

	private LoadImage() {
		int maxSize = (int) (Runtime.getRuntime().maxMemory() / 4);
		mBitmapLruCache = new LruCache<String, Bitmap>(maxSize) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight();
			}
		};
		getThreadPool();
	}

	public static synchronized LoadImage getInstance() {
		if (null == mLoadImage) {
			mLoadImage = new LoadImage();
		}

		return mLoadImage;
	}

	private void getThreadPool() {
		mEservice = Executors.newFixedThreadPool(2);
	}

	public Bitmap loadImage(final String path, final int width,
			final int height, final OnCallBackListener callback) {
		Bitmap bitmap = getFormCache(path);
		final Handler handle = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				Bitmap bitmap = (Bitmap) msg.obj;
				if (callback != null) {
					callback.setOnCallBackListener(bitmap, path);
				}

			}
		};
		if (null == bitmap) {
			mEservice.execute(new Thread() {
				@Override
				public void run() {
					Bitmap bitmap = getThumbImage(path, width, height);
					Message msg = new Message();
					msg.obj = bitmap;
					msg.what = 0;
					handle.sendMessage(msg);
					addCache(path, bitmap);
				}
			});
			return null;
		} else {

			return bitmap;
		}

	}

	public void cancelTask() {
		if (null != mEservice) {
			mEservice.shutdown();
		}
	}

	// 将图片写入缓存
	public void addCache(String path, Bitmap bitmap) {
		if (null != bitmap) {
			mBitmapLruCache.put(path, bitmap);
		}
	}

	public Bitmap getThumbImage(String path, int width, int height) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		// 设置为true则decode中的bitmap为null只是将图片的宽高放入了option中
		opt.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, opt);
		opt.inSampleSize = getScale(opt, width, height);
		opt.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(path, opt);

	}

	public int getScale(Options opt, int viewWdith, int ViewHeight) {
		int inSimpleSize = 1;
		if (0 == viewWdith || 0 == ViewHeight) {
			return inSimpleSize;
		}
		// 获取图片的大小
		int bitmapWidth = opt.outWidth;
		int bitmapHeight = opt.outHeight;
		if (bitmapWidth > viewWdith || bitmapHeight > ViewHeight) {
			int widthScale = Math
					.round((float) bitmapWidth / (float) viewWdith);
			int heightScale = Math.round((float) bitmapHeight
					/ (float) ViewHeight);

			inSimpleSize = widthScale < heightScale ? widthScale : heightScale;
		}

		return inSimpleSize;

	}

	public Bitmap getFormCache(String path) {
		return mBitmapLruCache.get(path);
	}

	public interface OnCallBackListener {
		public void setOnCallBackListener(Bitmap bitmap, String uri);
	}
}
