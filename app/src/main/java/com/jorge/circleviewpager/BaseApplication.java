package com.jorge.circleviewpager;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;

/**
 * @author Jorge on 2015/8/26 17:52
 */
public class BaseApplication extends Application {
    /** 全局Context，原理是因为Application类是应用最先运行的，所以在我们的代码调用时，该值已经被赋值过了 */
    private static BaseApplication mInstance;
    /** 主线程ID */
    private static int mMainThreadId = -1;
    /** 主线程ID */
    private static Thread mMainThread;
    /** 主线程Handler */
    private static Handler mMainThreadHandler;
    /** 主线程Looper */
    private static Looper mMainLooper;

    @Override
    public void onCreate() {
        mMainThreadId = android.os.Process.myTid();
        mMainThread = Thread.currentThread();
        mMainThreadHandler = new Handler();
        mMainLooper = getMainLooper();
        mInstance = this;
        super.onCreate();
        initImageLoader();
    }


    public static BaseApplication getApplication() {
        return mInstance;
    }

    /** 获取主线程ID */
    public static int getMainThreadId() {
        return mMainThreadId;
    }

    /** 获取主线程 */
    public static Thread getMainThread() {
        return mMainThread;
    }

    /** 获取主线程的handler */
    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    /** 获取主线程的looper */
    public static Looper getMainThreadLooper() {
        return mMainLooper;
    }




    public void initImageLoader(){
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(mInstance)
                .threadPriority(Thread.NORM_PRIORITY + 2)
                .denyCacheImageMultipleSizesInMemory()
                .threadPoolSize(10)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .diskCache(new UnlimitedDiskCache(new File(FileUtils.getIconDir(this))))
                .memoryCache(new LRULimitedMemoryCache(2*1024*1024))
                .build();

        ImageLoader.getInstance().init(config);
    }
    public void exitApp() {
        System.gc();
        android.os.Process.killProcess(android.os.Process.myPid());
    }


}
