# CircleViewPager
自动轮播图
  ![image](https://github.com/CodingForAndroid/CircleViewPager/blob/master/screenshot/view.gif)
Android  自动轮播图，接入方便 ，欢迎使用~

 使用说明：

 布局文件

    <com.jorge.circlelibrary.ImageCycleView
        android:layout_height="wrap_content"
        android:layout_width="fill_parent"
        android:id="@+id/cycleView"/>



相关Activity中

        /** 找到轮播控件*/
        imageCycleView= (ImageCycleView) findViewById(R.id.cycleView);
        /**装在数据的集合  文字描述*/
        ArrayList<String> imageDescList=new ArrayList<>();
        /**装在数据的集合  图片地址*/
        ArrayList<String> urlList=new ArrayList<>();
        /**添加数据*/
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
    /**初始化轮播图的关键方法*/ 
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
                /**在此方法中，显示图片，可以用自己的图片加载库，也可以用本demo中的（Imageloader）*/
                ImageLoaderHelper.getInstance().loadImage(imageURL, imageView);
            }
        };
        /**设置数据*/
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

内部封装，外部调用起来比较方便
可通过 build.gradle 直接导入

  dependencies {
    compile 'com.jorge.circleview:circlelibrary:1.0.0'
}



友情提示：

一： 不要忘记添加  联网  读写 权限， 可以复制 本项目中的权限 。


二： 不要忘记添加 图片缓存框架， 本Demo 中用的是  Android-Universal-Image-Loader ,本地jar包。

三： 工具类 皆可使用本Demo 中已有的。
