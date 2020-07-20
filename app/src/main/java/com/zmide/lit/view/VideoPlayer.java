package com.zmide.lit.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ScreenUtils;
import com.shuyu.gsyvideoplayer.player.PlayerFactory;
import com.shuyu.gsyvideoplayer.player.SystemPlayerManager;
import com.shuyu.gsyvideoplayer.utils.CommonUtil;
import com.shuyu.gsyvideoplayer.utils.Debuger;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.shuyu.gsyvideoplayer.video.base.GSYBaseVideoPlayer;
import com.zmide.lit.R;
import com.zmide.lit.skin.SkinManager;


/**
 * 带封面
 * Created by guoshuyu on 2017/9/3.
 */

public class VideoPlayer extends StandardGSYVideoPlayer {
	int mDefaultRes;
	private ImageView mMore;
	
	public VideoPlayer(Context context, Boolean fullFlag) {
		super(context, fullFlag);
	}
	
	public VideoPlayer(Context context) {
		super(context);
	}
	
	public VideoPlayer(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	class vh extends RecyclerView.ViewHolder{
		vh(@NonNull View itemView) {
			super(itemView);
			textView = itemView.findViewById(R.id.textView);
		}
		TextView textView;
	}
	
	@Override
	protected void init(Context context) {
		super.init(context);
		PlayerFactory.setPlayManager(SystemPlayerManager.class);
		mMore = findViewById(R.id.more);
		LinearLayout slideBar = findViewById(R.id.slide_bar);
		slideBar.setVisibility(GONE);
		mMore.setOnClickListener((view)->{
			if (ScreenUtils.isLandscape())
				slideBar.setOrientation(LinearLayout.HORIZONTAL);
			else
				slideBar.setOrientation(LinearLayout.VERTICAL);
			slideBar.setVisibility(VISIBLE);
		});
		findViewById(R.id.slide_mask).setOnClickListener((view)->{
			slideBar.setVisibility(GONE);
		});
		findViewById(R.id.slide_holder).setOnClickListener((view)->{});
		RecyclerView speedRv = findViewById(R.id.speedRv);
		RecyclerView sizeRv = findViewById(R.id.sizeRv);
		LinearLayoutManager lm = new GridLayoutManager(context,6);
		lm.setOrientation(RecyclerView.VERTICAL);
		speedRv.setLayoutManager(lm);
		speedRv.setAdapter(new RecyclerView.Adapter<vh>() {
			float[] speedDouble= {0.5f,0.75f,1.0f,1.25f,1.5f,2.0f};
			@NonNull
			@Override
			public vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
				View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_text, parent, false);
				return new vh(view);
			}
			
			@SuppressLint("SetTextI18n")
			@Override
			public void onBindViewHolder(@NonNull vh holder, int position) {
				if (getSpeed() == speedDouble[position])
					holder.textView.setTextColor(SkinManager.getInstance().getColor(R.color.accentColor));
				else
					holder.textView.setTextColor(0xffffffff);
				holder.textView.setText(speedDouble[position]+"");
				holder.textView.setOnClickListener(view -> {
					setSpeed(speedDouble[position],true);
					//holder.textView.setTextColor(SkinManager.getInstance().getColor(R.color.accentColor));
					notifyDataSetChanged();
				});
			}
			
			@Override
			public int getItemCount() {
				return speedDouble.length;
			}
			
		});
		LinearLayoutManager lm2 = new GridLayoutManager(context,6);
		GSYVideoType.setShowType(GSYVideoType.SCREEN_TYPE_FULL);///////////////////////////////////////////////
		sizeRv.setLayoutManager(lm2);
		sizeRv.setAdapter(new RecyclerView.Adapter<vh>() {
			String[] texts = {"适应","16:9","4:3","18:9","铺满","填充"};
			int[] ints = {
					GSYVideoType.SCREEN_TYPE_DEFAULT,
					GSYVideoType.SCREEN_TYPE_16_9,
					GSYVideoType.SCREEN_TYPE_4_3,
					GSYVideoType.SCREEN_TYPE_18_9,
					GSYVideoType.SCREEN_TYPE_FULL,
					GSYVideoType.SCREEN_MATCH_FULL,
			};
			@NonNull
			@Override
			public vh onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
				View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_text, parent, false);
				return new vh(view);
			}
			
			@SuppressLint("SetTextI18n")
			@Override
			public void onBindViewHolder(@NonNull vh holder, int position) {
				if (GSYVideoType.getShowType() == ints[position])
					holder.textView.setTextColor(SkinManager.getInstance().getColor(R.color.accentColor));
				else
					holder.textView.setTextColor(0xffffffff);
				holder.textView.setText(texts[position]+"");
				holder.textView.setOnClickListener(view -> {
					GSYVideoType.setShowType(ints[position]);
					//holder.textView.setTextColor(SkinManager.getInstance().getColor(R.color.accentColor));
					notifyDataSetChanged();
				});
			}
			
			@Override
			public int getItemCount() {
				return texts.length;
			}
			
		});
		if (mThumbImageViewLayout != null &&
				(mCurrentState == -1 || mCurrentState == CURRENT_STATE_NORMAL || mCurrentState == CURRENT_STATE_ERROR)) {
			mThumbImageViewLayout.setVisibility(VISIBLE);
		}
	}
	
	@Override
	public int getLayoutId() {
		return R.layout.video_player_view;
	}
	
	
	@Override
	public GSYBaseVideoPlayer startWindowFullscreen(Context context, boolean actionBar, boolean statusBar) {
		GSYBaseVideoPlayer gsyBaseVideoPlayer = super.startWindowFullscreen(context, actionBar, statusBar);
		VideoPlayer videoPlayer = (VideoPlayer) gsyBaseVideoPlayer;
		return gsyBaseVideoPlayer;
	}
	
	
	@Override
	public GSYBaseVideoPlayer showSmallVideo(Point size, boolean actionBar, boolean statusBar) {
		//下面这里替换成你自己的强制转化
		VideoPlayer videoPlayer = (VideoPlayer) super.showSmallVideo(size, actionBar, statusBar);
		videoPlayer.mStartButton.setVisibility(GONE);
		videoPlayer.mStartButton = null;
		return videoPlayer;
	}
	
	@Override
	protected void cloneParams(GSYBaseVideoPlayer from, GSYBaseVideoPlayer to) {
		super.cloneParams(from, to);
		VideoPlayer sf = (VideoPlayer) from;
		VideoPlayer st = (VideoPlayer) to;
		
		st.mShowFullAnimation = sf.mShowFullAnimation;
	}
	
	
	/**
	 * 退出window层播放全屏效果
	 */
	@SuppressWarnings("ResourceType")
	@Override
	protected void clearFullscreenLayout() {
		if (!mFullAnimEnd) {
			return;
		}
		mIfCurrentIsFullscreen = false;
		int delay = 0;
		if (mOrientationUtils != null) {
			delay = mOrientationUtils.backToProtVideo();
			mOrientationUtils.setEnable(false);
			if (mOrientationUtils != null) {
				mOrientationUtils.releaseListener();
				mOrientationUtils = null;
			}
		}
		
		if (!mShowFullAnimation) {
			delay = 0;
		}
		
		final ViewGroup vp = (CommonUtil.scanForActivity(getContext())).findViewById(Window.ID_ANDROID_CONTENT);
		final View oldF = vp.findViewById(getFullId());
		if (oldF != null) {
			//此处fix bug#265，推出全屏的时候，虚拟按键问题
			VideoPlayer gsyVideoPlayer = (VideoPlayer) oldF;
			gsyVideoPlayer.mIfCurrentIsFullscreen = false;
		}
		
		if (delay == 0) {
			backToNormal();
		} else {
			postDelayed(() -> backToNormal(), delay);
		}
		
	}
	
	
	/******************* 下方两个重载方法，在播放开始前不屏蔽封面，不需要可屏蔽 ********************/
	@Override
	public void onSurfaceUpdated(Surface surface) {
		super.onSurfaceUpdated(surface);
		if (mThumbImageViewLayout != null && mThumbImageViewLayout.getVisibility() == VISIBLE) {
			mThumbImageViewLayout.setVisibility(INVISIBLE);
		}
	}
	
	@Override
	protected void setViewShowState(View view, int visibility) {
		if (view == mThumbImageViewLayout && visibility != VISIBLE) {
			return;
		}
		super.setViewShowState(view, visibility);
	}
	
	@Override
	public void onSurfaceAvailable(Surface surface) {
		super.onSurfaceAvailable(surface);
		if (GSYVideoType.getRenderType() != GSYVideoType.TEXTURE) {
			if (mThumbImageViewLayout != null && mThumbImageViewLayout.getVisibility() == VISIBLE) {
				mThumbImageViewLayout.setVisibility(INVISIBLE);
			}
		}
	}
	
	/******************* 下方重载方法，在播放开始不显示底部进度和按键，不需要可屏蔽 ********************/
	
	protected boolean byStartedClick;
	
	@Override
	protected void onClickUiToggle() {
		if (mIfCurrentIsFullscreen && mLockCurScreen && mNeedLockFull) {
			setViewShowState(mLockScreen, VISIBLE);
			return;
		}
		byStartedClick = true;
		super.onClickUiToggle();
		
	}
	
	@Override
	protected void changeUiToNormal() {
		super.changeUiToNormal();
		byStartedClick = false;
	}
	
	@Override
	protected void changeUiToPreparingShow() {
		super.changeUiToPreparingShow();
		Debuger.printfLog("Sample changeUiToPreparingShow");
		setViewShowState(mBottomContainer, INVISIBLE);
		setViewShowState(mStartButton, INVISIBLE);
	}
	
	@Override
	protected void changeUiToPlayingBufferingShow() {
		super.changeUiToPlayingBufferingShow();
		Debuger.printfLog("Sample changeUiToPlayingBufferingShow");
		if (!byStartedClick) {
			setViewShowState(mBottomContainer, INVISIBLE);
			setViewShowState(mStartButton, INVISIBLE);
		}
	}
	
	@Override
	protected void changeUiToPlayingShow() {
		super.changeUiToPlayingShow();
		Debuger.printfLog("Sample changeUiToPlayingShow");
		if (!byStartedClick) {
			setViewShowState(mBottomContainer, INVISIBLE);
			setViewShowState(mStartButton, INVISIBLE);
		}
	}
	
	@Override
	public void startAfterPrepared() {
		super.startAfterPrepared();
		Debuger.printfLog("Sample startAfterPrepared");
		setViewShowState(mBottomContainer, INVISIBLE);
		setViewShowState(mStartButton, INVISIBLE);
		setViewShowState(mBottomProgressBar, VISIBLE);
	}
	
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		byStartedClick = true;
		super.onStartTrackingTouch(seekBar);
	}
}