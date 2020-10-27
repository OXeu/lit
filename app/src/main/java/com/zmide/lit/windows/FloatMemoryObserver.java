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
import android.widget.FrameLayout;
import android.os.Debug;
import android.content.Context;
import android.util.Log;
public class FloatMemoryObserver {
	private View floatView;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        public void run() {
            update();
            handler.postDelayed(this, 1000);// 间隔1秒
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
		if(floatView != null && contentView != null)
		contentView.removeView(floatView);
	}
	
    private void open(Activity activity){
		this.activity = activity;
		View rootView = activity.getWindow().getDecorView().getRootView();
		contentView = rootView.findViewById(android.R.id.content);
		floatView = LayoutInflater.from(activity).inflate(R.layout.float_memory, null);
		// 添加视图
		contentView.addView(floatView);
		FrameLayout.LayoutParams lm = (FrameLayout.LayoutParams)floatView.getLayoutParams();
		lm.setMargins(50, 80, 50, 50);
		floatView.setLayoutParams(lm);
		handler.postDelayed(runnable,1000);
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
		text += "\nMemory:"+ getRunningMemory() + "MBytes";
		return text;
	}
	
	private double getRunningMemory() {
        double mem = 0.0D;
        try {
            // 统计进程的内存信息 totalPss
			ActivityManager mActivityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
            Debug.MemoryInfo[] memInfo = mActivityManager.getProcessMemoryInfo(new int[]{android.os.Process.myPid()});
            if (memInfo.length > 0) {

                /**
                 * 读取内存信息,跟Android Profiler 分析一致
                 */
                String java_mem = memInfo[0].getMemoryStat("summary.java-heap");

                String native_mem = memInfo[0].getMemoryStat("summary.native-heap");

                String graphics_mem = memInfo[0].getMemoryStat("summary.graphics");

                String stack_mem = memInfo[0].getMemoryStat("summary.stack");

                String code_mem = memInfo[0].getMemoryStat("summary.code");

                String others_mem = memInfo[0].getMemoryStat("summary.system");

                int dalvikPss = convertToInt(java_mem,0)
					+ convertToInt(native_mem,0)
					+ convertToInt(graphics_mem,0)
					+ convertToInt(stack_mem,0)
					+ convertToInt(code_mem,0)
					+ convertToInt(others_mem,0);

                if (dalvikPss >= 0) {
                    // Mem in MB
                    mem = dalvikPss / 1024.0D;
                }
				Log.i("Memory",mem+"");
				Log.e("memory",memInfo+"");
            }
        } catch (Exception e) {
			Log.e("Error",e.getMessage());
			Log.e("Error",e.getStackTrace().toString()+"");
            e.printStackTrace();
        }
        return mem;
    }

    /**
     * 转化为int
     * @param value 传入对象
     * @param defaultValue 发生异常时，返回默认值
     * @return
     */
    public static int convertToInt(Object value, int defaultValue){

        if (value == null || "".equals(value.toString().trim())){
            return defaultValue;
        }

        try {
            return Integer.valueOf(value.toString());
        }catch (Exception e){

            try {
                return Integer.valueOf(String.valueOf(value));
            }catch (Exception e1) {

                try {
                    return Double.valueOf(value.toString()).intValue();
                }catch (Exception e2){
                    return defaultValue;
                }
            }
        }
    }
}
