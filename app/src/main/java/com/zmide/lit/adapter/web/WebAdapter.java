package com.zmide.lit.adapter.web;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.zmide.lit.R;
import com.zmide.lit.main.SearchEnvironment;
import com.zmide.lit.skin.SkinManager;
import com.zmide.lit.ui.MainActivity;

import java.util.ArrayList;
import com.zmide.lit.main.IndexEnvironment;
import android.annotation.SuppressLint;
import com.zmide.lit.main.MWeb;
import android.app.Activity;
import com.zmide.lit.view.LitWebView;

/**
 * Created by xeu on 2020/1/1 23:20.
 写个文档，写了睡觉
 2020 10.09 01:13
 Adapter的职责是加载WebView，将底栏逻辑交由专门的底栏控制类处理
 WebView最好也是交付出去，但WebView以前是由Container进行初始化的
 所以应该单独出一个类

 Adapter需要实现的内容
 获取当前/指定WebView
 添加窗口
 移动窗口相对位置(可选)
 删除窗口(删除后逻辑由Container实现)
 交付WebView给初始化类
 交付底栏给底栏类

 交付方法
 由于Adapter持有实例View，可以考虑传给指定静态类View以初始化View


 存储的数据
 存储MWeb MWeb中存有View 我有一个FrameLayout，直接Attach就行了
 另外 主页也需要放在Adapter里面

 2020 10.09 01:20
 */


public class WebAdapter extends RecyclerView.Adapter<WebAdapter.MyViewHolder> {

	private MainActivity mActivity;

	private LayoutInflater mInflater;

	private ArrayList<MWeb> webs = new ArrayList<>();

	private IndexEnvironment mIndexEnvironment;

	//提供一个合适的构造方法
	public WebAdapter(MainActivity ac) {
		this.mActivity = ac;
		mInflater = LayoutInflater.from(mActivity);
	}

	/**
	 * 将布局转换为View并传递给自定义的MyViewHolder
	 *
	 * @param viewGroup group
	 * @param viewType  int
	 * @return holder
	 */

	@NonNull
	@Override
	public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
		View view = mInflater.inflate(R.layout.web_frame, viewGroup, false);
		MyViewHolder holder = new MyViewHolder(view);
		return holder;
	}

	/**
	 * 建立起MyViewHolder中视图与数据的关联
	 *
	 * @param viewHolder holder
	 * @param position   int
	 */

	@Override
	public void onBindViewHolder(final MyViewHolder viewHolder, int position) {
		//获取当前MWeb
		MWeb web = webs.get(position);

		/*MWeb里包含已经处理的WebView 
		 SwipeRefreshLayout
		 */
		//TODO: MWeb中底栏加载代码


		//初始化首页


		//绑定View
		View view = web.getView();
		LitWebView mWebView = viewHolder.mFrame.findViewById(R.id.mainWebView);
		mIndexEnvironment = new IndexEnvironment(mActivity,viewHolder);
		mWebView.setIndex(mIndexEnvironment);
		
		if (view != null) {
			if (view.getParent() != null) {
				((ViewGroup) view.getParent()).removeView(view);
			} else {
				//mIndexEnvironment.showIndex();
			}
			viewHolder.mFrame.removeAllViews();
			viewHolder.mFrame.addView(view);
		}
	}


	/**
	 * 获取item的数目
	 *
	 * @return int
	 */

	@Override
	public int getItemCount() {
		return webs.size();
	}

	public void createWindow(MWeb mweb) {
		webs.add(mweb);
		notifyDataSetChanged();
	}

	public void createWindow(int windowId, MWeb web) {
		webs.add(windowId, web);
		notifyDataSetChanged();
	}


	public MWeb removeWindow(int position) {
		try {
			MWeb web = webs.remove(position);
			notifyDataSetChanged();
			return web;
		} catch (Exception e) {}

		return null;
	}

	public MWeb removeWindow(MWeb mweb) {
		try {
			int p = webs.indexOf(mweb);

			MWeb web =  webs.remove(p);	
			notifyDataSetChanged();
			return web;
		} catch (Exception e) {}
		notifyDataSetChanged();
		return null;

	}

	public ArrayList<MWeb> getMWebs() {
		return webs;
	}

	public MWeb getMWeb(int p) {
		return webs.get(p);
	}

	public int getIndex(MWeb web) {
		int p = webs.indexOf(web);
		return p;
	}



	public void replaceWindowsList(ArrayList<MWeb> mwebs) {
		webs = mwebs;
	}

	//自定义的ViewHolder，持有item的所有控件
	public class MyViewHolder extends RecyclerView.ViewHolder {
		private FrameLayout mFrame;
		@SuppressLint("StaticFieldLeak")
		private ImageView mIndexWallpaper;
		@SuppressLint("StaticFieldLeak")
		private RelativeLayout mIndexParent;
		@SuppressLint("StaticFieldLeak")
		private RelativeLayout mIndexSearchBar;
		@SuppressLint("StaticFieldLeak")
		private TextView mIndexTitle;
		public MyViewHolder(View view) {
			super(view);
			mFrame = view.findViewById(R.id.mainFrame);
			mIndexParent = view.findViewById(R.id.indexParent);
			mIndexWallpaper = view.findViewById(R.id.indexWallpaper);
			mIndexSearchBar = view.findViewById(R.id.indexSearchBar);
			mIndexTitle = view.findViewById(R.id.indexTitle);
		}

		public ImageView getIndexWallpaper() {
			return mIndexWallpaper;
		}

		public RelativeLayout getIndexParent() {
			return mIndexParent;
		}

		public RelativeLayout getIndexSearchBar() {
			return mIndexSearchBar;
		}

		public TextView getIndexTitle() {
			return mIndexTitle;
		}

		public FrameLayout getWebFrame() {
			return mFrame;
		}
	}


}
