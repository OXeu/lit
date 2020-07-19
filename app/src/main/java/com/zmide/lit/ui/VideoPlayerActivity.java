package com.zmide.lit.ui;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.ScreenUtils;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.OrientationUtils;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.zmide.lit.R;
import com.zmide.lit.base.BaseActivity;

import java.util.Objects;


public class VideoPlayerActivity extends BaseActivity {
	
	StandardGSYVideoPlayer videoPlayer;
	
	OrientationUtils orientationUtils;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_player);
		ScreenUtils.setFullScreen(this);
		init();
	}
	
	private void init() {
		videoPlayer = findViewById(R.id.video_player);
		
		String source1 = Objects.requireNonNull(getIntent().getData()).toString();
		videoPlayer.setUp(source1, true, Objects.requireNonNull(getIntent().getStringExtra("title")));
		
		//增加封面
		ImageView imageView = new ImageView(this);
		imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
		imageView.setImageResource(0);
		videoPlayer.setThumbImageView(imageView);
		//增加title
		videoPlayer.getTitleTextView().setVisibility(View.VISIBLE);
		//设置返回键
		videoPlayer.getBackButton().setVisibility(View.VISIBLE);
		//设置旋转
		orientationUtils = new OrientationUtils(this, videoPlayer);
		//设置全屏按键功能,这是使用的是选择屏幕，而不是全屏
		videoPlayer.getFullscreenButton().setOnClickListener(v -> orientationUtils.resolveByClick());
		//是否可以滑动调整
		videoPlayer.setIsTouchWiget(true);
		//设置返回按键功能
		videoPlayer.getBackButton().setOnClickListener(v -> onBackPressed());
		videoPlayer.startPlayLogic();
	}
	
	
	@Override
	protected void onPause() {
		super.onPause();
		videoPlayer.onVideoPause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		videoPlayer.onVideoResume();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		GSYVideoManager.releaseAllVideos();
		if (orientationUtils != null)
			orientationUtils.releaseListener();
	}
	
	@Override
	public void onBackPressed() {
		//先返回正常状态
		if (orientationUtils.getScreenType() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
			videoPlayer.getFullscreenButton().performClick();
			return;
		}
		//释放所有
		videoPlayer.setVideoAllCallBack(null);
		super.onBackPressed();
	}
}