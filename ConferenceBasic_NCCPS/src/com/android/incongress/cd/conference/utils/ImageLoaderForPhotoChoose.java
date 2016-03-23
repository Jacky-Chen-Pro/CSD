package com.android.incongress.cd.conference.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * Created by Jacky on 2015/12/20.
 * 图片加载类
 */
public class ImageLoaderForPhotoChoose {
    private static ImageLoaderForPhotoChoose mInstance;

    private Semaphore mSemaphorePoolThreadHandler = new Semaphore(0);
    private Semaphore mSemaphoreThreadPool;

    private void init(int threadCount, Type type) {
        mPoolThread = new Thread(){
            @Override
            public void run() {
                Looper.prepare();
                mPoolThreadHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        //线程池去取出一个任务进行执行
                        mThreadPool.execute(getTask());

                        try{
                            mSemaphoreThreadPool.acquire();
                        }catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                //释放一个信号量
                mSemaphorePoolThreadHandler.release();
                Looper.loop();
            }
        };

        mPoolThread.start();

        int maxMemory = (int)Runtime.getRuntime().maxMemory();
        int cacheMemory = maxMemory/8;

        mLruCache = new LruCache<String, Bitmap>(cacheMemory){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes()*value.getHeight();
            }
        };

        mThreadPool = Executors.newFixedThreadPool(threadCount);
        mTaskQueue = new LinkedList<Runnable>();
        mType = type;

        mSemaphoreThreadPool = new Semaphore(threadCount);
    }

    /**
     * 从任务队列取出一个方法
     * @return
     */
    private Runnable getTask() {
        if(mType == Type.FIFO) {
            return mTaskQueue.removeFirst();
        }else{
            return mTaskQueue.removeLast();
        }
    }

    private ImageLoaderForPhotoChoose(){
        init(DEFAULT_THREAD_COUNT, Type.LIFO);
    }

    private ImageLoaderForPhotoChoose(int threadCount, Type type) {
        init(threadCount, type);
    }

    /**
     * 单例
     * 效率的提升，避免同时到达new代码，生成多个对象。懒加载的一种方式
     * @return
     */
    public static ImageLoaderForPhotoChoose getInstance(){
        if(mInstance == null) {
            synchronized ((ImageLoaderForPhotoChoose.class)) {
                if(mInstance == null) {
                    mInstance = new ImageLoaderForPhotoChoose();
                }
            }
        }
        return mInstance;
    }

    /**
     * 单例
     * 效率的提升，避免同时到达new代码，生成多个对象。懒加载的一种方式
     * @return
     */
    public static ImageLoaderForPhotoChoose getInstance(int threadCount, Type type){
        if(mInstance == null) {
            synchronized ((ImageLoaderForPhotoChoose.class)) {
                if(mInstance == null) {
                    mInstance = new ImageLoaderForPhotoChoose(threadCount, type);
                }
            }
        }

        return mInstance;
    }

    /**
     * 图片缓存的核心对象
     */
    private LruCache<String, Bitmap> mLruCache;

    /**
     * 线程池
     */
    private ExecutorService mThreadPool;
    private static final int DEFAULT_THREAD_COUNT = 1;
    /**
     * 队列的调度方式
     */
    private Type mType = Type.LIFO;

    /**
     * 任务队列
     */
    private LinkedList<Runnable> mTaskQueue;

    /**
     * 后台轮询线程
     */
    private Thread mPoolThread;
    private Handler mPoolThreadHandler;

    /**
     * 线程中的Handler
     */
    private Handler mUIHandler;


    public enum Type{
        FIFO,LIFO;
    }


    /**
     * 根据path为Imageview设置图片
     * @param path
     * @param imageView
     */
    public void loadImage(final String path, final ImageView imageView) {
        imageView.setTag(path);

        if(mUIHandler == null) {
            mUIHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    //获取得到的图片，为Image回调设置图片
                    ImgBeanHolder holder = (ImgBeanHolder)msg.obj;
                    Bitmap bm = holder.bitmap;
                    ImageView imageView = holder.imageView;
                    String path = holder.path;

                    if(imageView.getTag().toString().equals(path)) {
                        imageView.setImageBitmap(bm);
                    }
                }
            };
        }

        //根据path在缓存中获取bitmap
        Bitmap bm = getBitmapFromLruCache(path);
        if(bm!=null) {
            refreshBitmap(path, imageView, bm);
        }else {
            addTask(new Runnable(){
                @Override
                public void run() {
                    //加载图片
                    //图片的压缩
                    //1.获取图片需要显示的大小
                    ImageSize imageSize = getImageViewSize(imageView);
                    //2.压缩图片
                    Bitmap bm = decodeSampledBitmapFromPath(path, imageSize.width, imageSize.height);
                    //3.把图片加入到缓存
                    addBitmapToLruCache(path, bm);
                    refreshBitmap(path, imageView, bm);

                    mSemaphoreThreadPool.release();
                }
            });
        }
    }

    private void refreshBitmap(String path, ImageView imageView, Bitmap bm) {
        Message message = Message.obtain();
        ImgBeanHolder holder = new ImgBeanHolder();
        holder.bitmap = bm;
        holder.imageView = imageView;
        holder.path = path;
        message.obj = holder;
        mUIHandler.sendMessage(message);
    }

    /**
     * 将图片加入LruCache
     * @param path
     * @param bmp
     */
    private void addBitmapToLruCache(String path, Bitmap bmp) {
        if(getBitmapFromLruCache(path) == null) {
            if(bmp != null)
                 mLruCache.put(path, bmp);
        }
    }

    /**
     * 根据图片需要显示的宽和高对图片进行压缩
     * @param path
     * @param width
     * @param height
     * @return
     */
    protected  Bitmap decodeSampledBitmapFromPath(String path, int width, int height) {
        //获取图片的宽和高，不加载到内存中
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        options.inSampleSize = calculateInSampleSize(options, width, height);

        //使用获取到的inSampleSize再次解析图片
        options.inJustDecodeBounds = false;
        Bitmap bmp = BitmapFactory.decodeFile(path,options);
        return bmp;
    }


    /**
     * 根据图片的宽高和需要的宽高进行比较，得到适当的压缩比
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int width = options.outWidth;
        int height = options.outHeight;

        int inSampleSize = 1;

        if(width>reqWidth || height>reqHeight) {
            int widthRadio = Math.round(width*1.0f/reqWidth);
            int heightRadeo = Math.round(height*1.0f/reqHeight);

            inSampleSize = Math.max(widthRadio,heightRadeo);
        }

        return inSampleSize;
    }

    protected ImageSize getImageViewSize(ImageView imageView) {
        ImageSize imageSize = new ImageSize();

        DisplayMetrics displayMetrics= imageView.getContext().getResources().getDisplayMetrics();

        ViewGroup.LayoutParams lp = imageView.getLayoutParams();
        int width = imageView.getWidth();//获取imageview的实际宽度

        if(width <= 0) {
            width = lp.width;//获取Imageview在layout中申明的宽度
        }

        if(width <= 0) {
            width = getImageViewFieldValue(imageView, "mMaxWidth");//检查最大值
        }

        if(width <=0) {
            width = displayMetrics.widthPixels; //最后没有办法了，只能为屏幕的宽度
        }

        int height = imageView.getHeight();//获取imageview的实际宽度
        if(height <= 0) {
            height = lp.height;//获取Imageview在layout中申明的宽度
        }

        if(height <= 0) {
            height = getImageViewFieldValue(imageView, "mMaxHeight");//检查最大值
        }

        if(height <=0) {
            height = displayMetrics.heightPixels; //最后没有办法了，只能为屏幕的宽度
        }
        imageSize.height = height;
        imageSize.width = width;

        return imageSize;
    }

    private class ImageSize {
        int width;
        int height;
    }

    private synchronized  void addTask(Runnable runnable) {
        mTaskQueue.add(runnable);

        try {
            if(mPoolThreadHandler == null) {
                mSemaphorePoolThreadHandler.acquire();;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        mPoolThreadHandler.sendEmptyMessage(0x110);
    }

    /**
     * 根据path在缓存中获取bitmap
     * @param path
     * @return
     */
    private Bitmap getBitmapFromLruCache(String path) {
        return mLruCache.get(path);
    }

    private class ImgBeanHolder {
        Bitmap bitmap;
        ImageView imageView;
        String path;
    }

    /**
     * 通过反射获取ImageView的某个值
     * @param obj
     * @param fieldName
     * @return
     */
    private static int getImageViewFieldValue(Object obj, String fieldName) {
        int value = 0;

        try {
            Field field = ImageView.class.getDeclaredField(fieldName);
            field.setAccessible(true);

            int fieldValue = field.getInt(obj);

            if(fieldValue >0 && fieldValue < Integer.MAX_VALUE) {
                value = fieldValue;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return value;
    }
}
