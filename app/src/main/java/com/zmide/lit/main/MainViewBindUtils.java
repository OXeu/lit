package com.zmide.lit.main;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.zmide.lit.R;
import com.zmide.lit.ui.MainActivity;
import com.zmide.lit.view.LitProgressBar;
import android.widget.FrameLayout;

public class MainViewBindUtils {
	private static MainActivity activity;
	@SuppressLint("StaticFieldLeak")
	private static LinearLayout mSearchHolder;
	private static CardView mCardView;
	@SuppressLint("StaticFieldLeak")
	private static LinearLayout mBallWindow;
	@SuppressLint("StaticFieldLeak")
	private static ImageView mMainGestureImage;
	private static CardView mMainGestureColor;
	@SuppressLint("StaticFieldLeak")
	private static RelativeLayout mMainBallParent;
	@SuppressLint("StaticFieldLeak")
	private static LinearLayout mTitleParent;
	@SuppressLint("StaticFieldLeak")
	private static RelativeLayout mSearchParent;
	@SuppressLint("StaticFieldLeak")
	private static TextView mSearchButton;
	@SuppressLint("StaticFieldLeak")
    private static EditText mSearchEdit;
    private static RecyclerView mSugRecyclerView;
	@SuppressLint("StaticFieldLeak")
	private static ImageView mBallImage;
	@SuppressLint("StaticFieldLeak")
	private static RelativeLayout mBall;
	@SuppressLint("StaticFieldLeak")
	private static TextView mBallText;
	private static RecyclerView mWebRecyclerView;
	private static RecyclerView mSearchEngineList;
	private static LitProgressBar mProgressBar;
	@SuppressLint("StaticFieldLeak")
	private static TextView mMainTitle;
	@SuppressLint("StaticFieldLeak")
	private static TextView mMainCode;
	@SuppressLint("StaticFieldLeak")
	private static TextView mjQueryText;
	@SuppressLint("StaticFieldLeak")
	private static LinearLayout mAdMark;
	@SuppressLint("StaticFieldLeak")
	private static RelativeLayout mBaseParent;

	//获取MainActivity各View实例
	public static void init(MainActivity mainActivity) {
		if (activity == null) {
			activity = mainActivity;
			mCardView = activity.findViewById(R.id.Cardview);
			mBall = activity.findViewById(R.id.mainBall);
			mBallImage = activity.findViewById(R.id.mainBallImage);
			mBallWindow = activity.findViewById(R.id.mainBallWindow);
			mMainGestureColor = activity.findViewById(R.id.mainGestureColor);
			mMainGestureImage = activity.findViewById(R.id.mainGestureImage);
			mProgressBar = activity.findViewById(R.id.mainProgressBar);
			mProgressBar.setColor(0xFF5F87FF, 0xFF122EF1);
			mMainBallParent = activity.findViewById(R.id.mainBallParent);
			mMainGestureColor.setVisibility(View.GONE);
			mTitleParent = activity.findViewById(R.id.mainTitleParent);
			mSearchParent = activity.findViewById(R.id.searchParent);
			mSearchButton = activity.findViewById(R.id.searchButton);
			mSearchEdit = activity.findViewById(R.id.searchEdit);
			mSearchEngineList = activity.findViewById(R.id.mainSearchEngineList);
			mSearchHolder = activity.findViewById(R.id.searchHolder);
			mSugRecyclerView = activity.findViewById(R.id.mainSearchSugList);
			mBallText = activity.findViewById(R.id.mainBallText);
			mMainCode = activity.findViewById(R.id.mainCode);
			mjQueryText = activity.findViewById(R.id.mainjQuery);
			mAdMark = activity.findViewById(R.id.mainAdMark);
			mBaseParent = activity.findViewById(R.id.baseParent);
			mMainTitle = activity.findViewById(R.id.mainTitle);
			
			mWebRecyclerView = activity.findViewById(R.id.mainFrame);
		}
	}
	
	public static LinearLayout getSearchHolder() {
		return mSearchHolder;
	}
	
	public static CardView getBallCardView() {
		return mCardView;
	}
	
	public static LinearLayout getBallWindowButton() {
		return mBallWindow;
	}
	
	public static ImageView getMainGestureImage() {
		return mMainGestureImage;
	}
	
	public static CardView getMainGestureColor() {
		return mMainGestureColor;
	}
	
	public static RelativeLayout getMainBallParent() {
		return mMainBallParent;
	}
	
	public static LinearLayout getTitleParent() {
		return mTitleParent;
	}
	
	public static RelativeLayout getSearchParent() {
		return mSearchParent;
    }

    public static TextView getSearchButton() {
        return mSearchButton;
    }

    public static EditText getSearchEdit() {
        return mSearchEdit;
    }

    public static RecyclerView getSugRecyclerView() {
        return mSugRecyclerView;
    }

    public static ImageView getBallImage() {
        return mBallImage;
    }
	
	public static RelativeLayout getBall() {
		return mBall;
	}
	
	public static TextView getBallText() {
		return mBallText;
	}
	
	public static RecyclerView getSearchEngineList() {
		return mSearchEngineList;
	}
	
	public static RecyclerView getWebRecyclerView() {
		return mWebRecyclerView;
	}
	
	public static LitProgressBar getProgressBar() {
		return mProgressBar;
	}
	
	public static TextView getMainTitle() {
		return mMainTitle;
	}
	
	public static TextView getCodeText() {
		return mMainCode;
	}

	public static TextView getjQueryText() {
		return mjQueryText;
	}

	public static LinearLayout getAdMark() {
		return mAdMark;
	}

	public static RelativeLayout getBaseParent() {
		return mBaseParent;
	}
	
	
	
	
	
}
