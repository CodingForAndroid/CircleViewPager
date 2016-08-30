package com.jorge.circlelibrary;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 *
 *
 */
public class ImageCycleView extends LinearLayout {
	/**
	 * 上下文
	 */
	private Context mContext;
	/**
	 * 图片轮播视图
	 */
	private ViewPager mAdvPager = null;
	/**
	 * 滚动图片视图适配
	 */
	private ImageCycleAdapter mAdvAdapter;
	/**
	 * 图片轮播指示器控件
	 */
	private ViewGroup mGroup;

	/**
	 * 图片轮播指示个图
	 */
	private ImageView mImageView = null;

	/**
	 * 滚动图片指示视图列表
	 */
	private ImageView[] mImageViews = null;

	/**
	 * 图片滚动当前图片下标
	 */
	private int mImageIndex = 0;
	/**
	 * 手机密度
	 */
	private float mScale;
	private boolean isStop;
	private TextView imageName;
	private ArrayList<String> mImageDescList;

	/**
	 * @param context
	 */
	public ImageCycleView(Context context) {
		super(context);
		init(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public ImageCycleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		mContext = context;
		mScale = context.getResources().getDisplayMetrics().density;
		LayoutInflater.from(context).inflate(R.layout.block_ad_cycle_view, this);
		mAdvPager = (ViewPager) findViewById(R.id.adv_pager);
		mAdvPager.setOffscreenPageLimit(3);
		mAdvPager.addOnPageChangeListener(new GuidePageChangeListener());
		mAdvPager.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
					case MotionEvent.ACTION_UP:
						// 开始图片滚动
						startImageTimerTask();
						break;
					default:
						// 停止图片滚动
						stopImageTimerTask();
						break;
				}
				return false;
			}
		});
		// 滚动图片右下指示器视
		mGroup = (ViewGroup) findViewById(R.id.circles);
		imageName = (TextView) findViewById(R.id.viewGroup2);
	}
	RelativeLayout.LayoutParams imageParams;
	public void setCycle_T(CYCLE_T T) {

		switch (T) {
			case CYCLE_VIEW_NORMAL:
				imageParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);

				break;
			case CYCLE_VIEW_THREE_SCALE:
				imageParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
				mAdvPager.setPageTransformer(false, new ViewPager.PageTransformer() {
					@Override
					public void transformPage(View page, float position) {
						final float normalizedPosition = Math.abs(Math.abs(position) - 1);
						page.setScaleX(normalizedPosition / 2 + 0.5f);
						page.setScaleY(normalizedPosition / 2 + 0.5f);
					}
				});
				break;
			case CYCLE_VIEW_ZOOM_IN:
				imageParams=null;
				mAdvPager.setPageMargin(-DemiUitls.dip2px(mContext,60));
				mAdvPager.setPageTransformer(true, new ZoomOutPageTransformer());
				imageParams=null;
				break;
		}
	}

	/**
	 * 装填图片数据
	 *
	 *
	 */
	public void setImageResources(ArrayList<String> imageDesList, ArrayList<String> imageUrlList, ImageCycleViewListener imageCycleViewListener) {
		mImageDescList = imageDesList;
		if (imageUrlList != null && imageUrlList.size() > 0) {
			this.setVisibility(View.VISIBLE);
		} else {
			this.setVisibility(View.GONE);
			return;
		}

		// 清除
		mGroup.removeAllViews();
		// 图片广告数量
		final int imageCount = imageUrlList.size();
		mImageViews = new ImageView[imageCount];
		for (int i = 0; i < imageCount; i++) {
			mImageView = new ImageView(mContext);
			int imageParams = (int) (mScale * 10 + 0.5f);// XP与DP转换，适应应不同分辨率
			int imagePadding = (int) (mScale * 5 + 0.5f);
			LayoutParams params=new LayoutParams(imageParams,imageParams);
			params.leftMargin=10;
			mImageView.setScaleType(ScaleType.CENTER_CROP);
			mImageView.setLayoutParams(params);
			mImageView.setPadding(imagePadding, imagePadding, imagePadding, imagePadding);

			mImageViews[i] = mImageView;
			if (i == 0) {
				mImageViews[i].setBackgroundResource(R.mipmap.banner_dot_focus);
			} else {
				mImageViews[i].setBackgroundResource(R.mipmap.banner_dot_normal);
			}
			mGroup.addView(mImageViews[i]);
		}

		imageName.setText(imageDesList.get(0));
		imageName.setTextColor(getResources().getColor(R.color.blue));
		mAdvAdapter = new ImageCycleAdapter(mContext, imageUrlList, imageDesList, imageCycleViewListener);
		mAdvPager.setAdapter(mAdvAdapter);
		startImageTimerTask();
	}

	/**
	 * 图片轮播(手动控制自动轮播与否，便于资源控件）
	 */
	public void startImageCycle() {
		startImageTimerTask();
	}

	/**
	 * 暂停轮播—用于节省资源
	 */
	public void pushImageCycle() {
		stopImageTimerTask();
	}

	/**
	 * 图片滚动任务
	 */
	private void startImageTimerTask() {
		stopImageTimerTask();
		// 图片滚动
		mHandler.postDelayed(mImageTimerTask, 3000);
	}

	/**
	 * 停止图片滚动任务
	 */
	private void stopImageTimerTask() {
		isStop = true;
		mHandler.removeCallbacks(mImageTimerTask);
	}

	private Handler mHandler = new Handler();

	/**
	 * 图片自动轮播Task
	 */
	private Runnable mImageTimerTask = new Runnable() {
		@Override
		public void run() {
			if (mImageViews != null) {
				mAdvPager.setCurrentItem(mAdvPager.getCurrentItem() + 1);
				if (!isStop) {  //if  isStop=true   //当你退出后 要把这个给停下来 不然 这个一直存在 就一直在后台循环
					mHandler.postDelayed(mImageTimerTask, 3000);
				}

			}
		}
	};

	/**
	 * 轮播图片监听
	 *
	 * @author minking
	 */
	private final class GuidePageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int state) {
			if (state == ViewPager.SCROLL_STATE_IDLE)
				startImageTimerTask();
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int index) {
			index = index % mImageViews.length;
			// 设置当前显示的图片
			mImageIndex = index;
			// 设置图片滚动指示器背
			mImageViews[index].setBackgroundResource(R.mipmap.banner_dot_focus);
			imageName.setText(mImageDescList.get(index));
			for (int i = 0; i < mImageViews.length; i++) {
				if (index != i) {
					mImageViews[i].setBackgroundResource(R.mipmap.banner_dot_normal);
				}
			}
		}
	}

	private class ImageCycleAdapter extends PagerAdapter {

		/**
		 * 图片视图缓存列表
		 */
		private ArrayList<View> mImageViewCacheList;

		/**
		 * 图片资源列表
		 */
		private ArrayList<String> mAdList = new ArrayList<String>();
		private ArrayList<String> nameList = new ArrayList<String>();

		/**
		 * 广告图片点击监听
		 */
		private ImageCycleViewListener mImageCycleViewListener;

		private Context mContext;

		public ImageCycleAdapter(Context context, ArrayList<String> adList, ArrayList<String> nameList, ImageCycleViewListener imageCycleViewListener) {
			this.mContext = context;
			this.mAdList = adList;
			this.nameList = nameList;
			mImageCycleViewListener = imageCycleViewListener;
			mImageViewCacheList = new ArrayList<View>();
		}

		@Override
		public int getCount() {
//			return mAdList.size();
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View view, Object obj) {
			return view == obj;
		}

		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			String imageUrl = mAdList.get(position % mAdList.size());
			View view;
			ClickableImageView imageView;
			if (mImageViewCacheList.isEmpty()) {
				view = View.inflate(mContext, R.layout.item_vp, null);
				imageView = (ClickableImageView) view.findViewById(R.id.iv);
				if(imageParams==null){

				}else{
					imageView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

				}
				imageView.setScaleType(ScaleType.CENTER_CROP);
			} else {
				view = mImageViewCacheList.remove(0);
				imageView = (ClickableImageView) view.findViewById(R.id.iv);
			}
			// 设置图片点击监听
			imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mImageCycleViewListener.onImageClick(position % mAdList.size(), v);
				}
			});
			view.setTag(imageUrl);
			container.addView(view);
			mImageCycleViewListener.displayImage(imageUrl, imageView);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			View view = (View) object;
			mAdvPager.removeView(view);
			mImageViewCacheList.add(view);

		}

	}

	/**
	 * 轮播控件的监听事件
	 *
	 * @author minking
	 */
	public interface ImageCycleViewListener {
		/**
		 * 加载图片资源
		 *
		 * @param imageURL :url
		 * @param imageView: image
		 */
		void displayImage(String imageURL, ImageView imageView);

		/**
		 * 单击图片事件
		 *
		 * @param position :position
		 * @param imageView :image
		 */
		void onImageClick(int position, View imageView);
	}

	/**
	 * vp 效果
	 */
	public static enum CYCLE_T {

		/********
		 * 普通的ViewPager
		 *****/
		CYCLE_VIEW_NORMAL,
		/********
		 * 放大进入
		 *****/
		CYCLE_VIEW_ZOOM_IN,
		/********
		 * 展示左右
		 *****/
		CYCLE_VIEW_THREE_SCALE
	}

	/**
	 * 是否隐藏底部
	 * @param hideBottom
	 */
	public void hideBottom(boolean hideBottom){
		if(hideBottom){
			mGroup.setVisibility(View.GONE);
			imageName.setVisibility(View.GONE);
		}else{
			mGroup .setVisibility(View.VISIBLE);
			imageName.setVisibility(View.VISIBLE);
		}
	}
}




