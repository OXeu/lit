package com.zmide.lit.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.zmide.lit.R;
import com.zmide.lit.view.Editor;

import java.util.Objects;
import android.view.View.OnClickListener;

/**
 * Copyright (C), 2019-2020, DreamStudio
 * Author: Xeu
 * Date: 2020/9/10 23:28
 * Description:
 * History:
 * <author> <time> <version> <desc>
 * 作者姓名 修改时间 版本号 描述
 */
public class InputDialog {

    //创建一个编辑对话框
    public static void create(Activity activity,String titleText,int... drawable,String hint,String okText,String cancelText,OnClickListener listener) {
        Dialog dialog = new Dialog(activity);
        @SuppressLint("InflateParams") View layout = LayoutInflater.from(activity).inflate(R.layout.dialog_input, null);
        dialog.setContentView(layout);
        TextView ok = layout.findViewById(R.id.editDialogOk);//书签编辑确认
        TextView cancel = layout.findViewById(R.id.editDialogCancel);//放弃按钮
        TextView title = layout.findViewById(R.id.editDialogTitle);//标题（书签）
        Editor inputEditor = layout.findViewById(R.id.editDialogInput);//链接编辑框
        inputEditor.setVisibility(View.VISIBLE);
        inputEditor.setHint(hint);
        if(drawable.length>=1)
            inputEditor.setLeftDrawable(drawable[0]);
        if(drawable.length>=2)
            inputEditor.setLeftDrawableAfterFocus(drawable[1]);
        ok.setText(okText);
        title.setText(titleText);//设置标题（新建目录）
        ok.setOnClickListener(listener);
        cancel.setText(cancelText);
        cancel.setOnClickListener((view2) -> dialog.cancel());
        WindowManager.LayoutParams lp = Objects.requireNonNull(dialog.getWindow()).getAttributes();
        lp.gravity = Gravity.BOTTOM;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.dimAmount = 0.2f;
        dialog.getWindow().setAttributes(lp);
        //设置该属性，dialog可以铺满屏幕
        dialog.getWindow().setBackgroundDrawable(null);
        FrameLayout.LayoutParams lps = (FrameLayout.LayoutParams) layout.getLayoutParams();
        lps.setMargins(30, 30, 30, 30);
        layout.setLayoutParams(lps);
        dialog.show();
    }

 

    
    
}
