package com.jorge.circleviewpager;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jorge.circlelibrary.ImageCycleView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ImageCycleView imageCycleView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageCycleView= (ImageCycleView) findViewById(R.id.cycleView);

        ArrayList<String> imageDescList=new ArrayList<>();
        ArrayList<String> urlList=new ArrayList<>();


        /**添加数据*/
//        http://attach.bbs.miui.com/forum/month_1012/101203122706c89249c8f58fcc.jpg
//        http://bbsdown10.cnmo.com/attachments/201308/06/091441rn5ww131m0gj55r0.jpg
//        http://kuoo8.com/wall_up/hsf2288/200801/2008012919460743597.jpg
//        http://d.3987.com/taiqiumein_141001/007.jpg
//        http://kuoo8.com/wall_up/hsf2288/200807/2008071115370276173.jpg
        urlList.add("http://attach.bbs.miui.com/forum/month_1012/101203122706c89249c8f58fcc.jpg");
        urlList.add("http://bbsdown10.cnmo.com/attachments/201308/06/091441rn5ww131m0gj55r0.jpg");
        urlList.add("http://kuoo8.com/wall_up/hsf2288/200801/2008012919460743597.jpg");
        urlList.add("http://d.3987.com/taiqiumein_141001/007.jpg");
        urlList.add("http://kuoo8.com/wall_up/hsf2288/200807/2008071115370276173.jpg");

        imageDescList.add("小仓柚子");
        imageDescList.add("抚媚妖娆性感美女");
        imageDescList.add("热血沸腾 比基尼");
        imageDescList.add(" 台球美女");
        imageDescList.add("身材妙曼");

        initCarsuelView(imageDescList, urlList);
    }


    // 初始化轮播图
    public void initCarsuelView(ArrayList<String> imageDescList,ArrayList<String>urlList) {
        LinearLayout.LayoutParams cParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getScreenHeight(MainActivity.this) * 3 / 10);
        imageCycleView.setLayoutParams(cParams);
        ImageCycleView.ImageCycleViewListener mAdCycleViewListener = new ImageCycleView.ImageCycleViewListener() {
            @Override
            public void onImageClick(int position, View imageView) {
                /**实现点击事件*/
            }
            @Override
            public void displayImage(String imageURL, ImageView imageView) {
                ImageLoaderHelper.getInstance().loadImage(imageURL, imageView);
            }
        };
        imageCycleView.setImageResources(imageDescList,urlList, mAdCycleViewListener);
        imageCycleView.startImageCycle();
    }

    /**
     * 得到屏幕的高度
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context){

        if (null == context) {
            return 0;
        }
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getApplicationContext().getResources().getDisplayMetrics();
        return dm.heightPixels;
    }



}
