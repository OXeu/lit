package com.zmide.lit.view;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zmide.lit.R;
import com.zmide.lit.main.MainViewBindUtils;
import com.zmide.lit.ui.MainActivity;

public class YogurtFrameLayout {
    private static MainActivity activity;
    private static RecyclerView recyclerView;

    public static void init(MainActivity mainActivity) {
        activity = mainActivity;
        recyclerView = MainViewBindUtils.getWebFrame();
    }

    private static void showAnimation() {

    }

    public static FrameLayout getFrame() {
        return recyclerView.getAdapter();
    }

    public static void setFrame(View view) {

    }

    public static class Adapter extends RecyclerView.Adapter<View> {

        @NonNull
        @Override
        public View onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull View holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 1;
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            private FrameLayout mFrame;

            ViewHolder(View view) {
                super(view);
                mFrame = view.findViewById(R.id.webFrame);
            }
        }
    }

    public static class A {
        int currentItem = mViewPager.getCurrentItem();
        private List<String> mPathList;

//1.使用FragmentStatePagerAdapter
        mPagerAdapter =new
        private FragmentStatePagerAdapter mPagerAdapter;)

        {
            @Override
            public int getCount () {
            return mPathList.size();
        }

            @Override
            public Fragment getItem ( int i){
            return PhotoPreviewFragment.newInstance(mPathList.get(i));
        }

            @Override
            public int getItemPosition (Object object){
            //2.重写getItemPostman方法返回POSITION_NONE，这样添加或者删除数据后刷新才有效
            return PagerAdapter.POSITION_NONE;
        }

        }

    mViewPager.setAdapter(mPagerAdapter)

        FragmentStatePagerAdapter(getSupportFragmentManager()
mPathList.remove(currentItem)
                mPagerAdapter.notifyDataSetChanged()

    }
}
