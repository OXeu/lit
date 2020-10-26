package com.zmide.lit.windows;
import com.zmide.lit.R;
import android.app.Activity;
import android.app.ActivityManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Handler;
import com.zmide.lit.base.MApplication;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
public class FloatMemoryObserver {
	private View floatView;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        public void run() {
            update();
            handler.postDelayed(this, 1000 * 120);// 间隔120秒
        }
    };

	private Activity activity;
	
	private static FloatMemoryObserver instance = new FloatMemoryObserver();

	private ViewGroup contentView;
	
	public static void start(Activity a){
		instance.open(a);
	}
	
	public static void end(){
		instance.close();
	}

	private void close() {
		contentView.removeView(floatView);
	}
	
    private void open(Activity activity){
		this.activity = activity;
		View rootView = activity.getWindow().getDecorView().getRootView();
		contentView = rootView.findViewById(android.R.id.content);
		floatView = LayoutInflater.from(activity).inflate(R.layout.float_memory, null);
		// 添加视图
		contentView.addView(floatView);
		LinearLayout.LayoutParams lm = new LinearLayout.LayoutParams();
		lm.setMargins(50, 50, 50, 50);
		floatView.setLayoutParams(lm);
		handler.postDelayed(runnable,floatView,1000);
		// 添加动画
		//floatView.startAnimation(AnimationUtils.loadAnimation(floatView.getContext(), R.anim.anim));
	}
	
	private void update(){
		if(floatView != null)
		{
			TextView text = floatView.findViewById(R.id.memory_text);
			text.setText(getMemoryInfo());
		}
	}
    
	
	private String getMemoryInfo(){
		ActivityManager activityManager = (ActivityManager) activity.getSystemService(Activity.ACTIVITY_SERVICE);
        //最大分配内存
        int memory = activityManager.getMemoryClass();
        String text = "memory: "+memory;
        //最大分配内存获取方法2
		float maxMemory = (float) (Runtime.getRuntime().maxMemory() * 1.0/ (1024 * 1024));
        //当前分配的总内存
        float totalMemory = (float) (Runtime.getRuntime().totalMemory() * 1.0/ (1024 * 1024));
        //剩余内存
        float freeMemory = (float) (Runtime.getRuntime().freeMemory() * 1.0/ (1024 * 1024));
        text += "MBytes\nmaxMemory: "+maxMemory;
        text += "MBtyes\ntotalMemory: "+totalMemory;
        text += "MBytes\nfreeMemory: "+freeMemory+"MBytes";
		return text;
	}
}
