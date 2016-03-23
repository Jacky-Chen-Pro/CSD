package com.android.incongress.cd.conference.utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.widget.ImageView;

public class AsyncImageLoader {
	private static HashMap<String, WeakReference<Bitmap>> cache = new HashMap<String, WeakReference<Bitmap>>();
	private static ExecutorService executorService = Executors
			.newFixedThreadPool(5); // 固定五个线
	final Handler handler = new Handler();

	public Bitmap getCachBitmap(String imageUrl) {
		WeakReference<Bitmap> bitmap = cache.get(imageUrl);
		if (bitmap == null || bitmap.get() == null) {
			if (cache.containsKey(imageUrl)) {
				cache.remove(imageUrl);
			}
			return null;
		}
		Bitmap mbitmap = bitmap.get();
		return mbitmap;

	}

	public void loadBitmap(final ImageView imageView, final String imageUrl,
			final ImageLoadCallback imageCallback) {
		executorService.submit(new Runnable() {
			@Override
			public void run() {
				final Bitmap bitmap = loadImageFromUrl(imageUrl);
				putCache(imageUrl, bitmap);
				handler.post(new Runnable() {
					@Override
					public void run() {
						imageCallback.imageLoaded(imageView, bitmap, imageUrl);
					}

				});
			}

		});
	}

	private void putCache(String key, Bitmap bmp) {
		if (bmp == null || key == null) {
			return;
		}
		if (!cache.containsKey(key)) {
			System.out.println("put cache " + key);
			WeakReference<Bitmap> bitmap = new WeakReference<Bitmap>(bmp);
			cache.put(key, bitmap);
		}
	}

	public static Bitmap loadImageFromUrl(String filepath) {
		Bitmap bmp = null;
		System.out.println(Thread.currentThread());

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filepath, options);
		bmp = BitmapDecodeTool.decodeBitmap(filepath, options.outWidth,
				options.outHeight, 3, Bitmap.Config.RGB_565, false);
		if (bmp == null) {
			System.out.println("--未解出图片----");
		} else {
			System.out.println("--解出图片----");
		}
		return bmp;
	}

	public interface ImageLoadCallback {
		public void imageLoaded(ImageView imageView, Bitmap imageBitmap,
				String imageUrl);
	}
}
