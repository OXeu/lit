package com.zmide.lit.ui;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;

import com.zmide.lit.R;
import com.zmide.lit.base.BaseActivity;
import com.zmide.lit.object.Diy;
import com.zmide.lit.util.DBC;
import com.zmide.lit.util.MDialogUtils;
import com.zmide.lit.util.MToastUtils;
import com.zmide.lit.util.MWindowsUtils;
import com.zmide.lit.view.Editor;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class DiyNewActivity extends BaseActivity {
	private Editor mEditTitle;
	private Editor mEditDescription;
	private Editor mEditValue;
	private MDialogUtils.Builder dialog;
	private boolean isNew = true;
	
	private int id;
	
	private int type;
	
	private Editor mEditExtra;
	
	private LinearLayout mExtraParent;
	
	private Switch mSwitchSe;
	
	private PopupWindow pop;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_diy_new);
		initView();
		initPop();
		String title = getIntent().getStringExtra("title");
		if (title != null)
			mEditTitle.setText(title);
		String description = getIntent().getStringExtra("description");
		if (description != null)
			mEditDescription.setText(description);
		String url = getIntent().getStringExtra("url");
		if (url != null)
			mEditValue.setText(url);
		int id = getIntent().getIntExtra("id", -1);
		if (id != -1) {
			isNew = false;
			this.id = id;
		}
		String extra = getIntent().getStringExtra("extra");
		if (extra != null) {
			if (extra.startsWith("[#]")) {
				extra = extra.replace("[#]", "");
				mSwitchSe.setChecked(false);
			} else {
				mSwitchSe.setChecked(true);
			}
			mEditExtra.setText(extra);
		}
		type = getIntent().getIntExtra("type", Diy.WEBPAGE);
		switch (type) {
			case Diy.PLUGIN:
				mExtraParent.setVisibility(View.VISIBLE);
				break;
			default:
				mExtraParent.setVisibility(View.GONE);
				break;
		}
		dialog = new MDialogUtils.Builder(this);
		//mSharedPreferences = getSharedPreferences("setting", Context.MODE_PRIVATE);
	}
	
	
	private void initView() {
		mEditExtra = findViewById(R.id.diynewEditExtra);
		ImageView mBack = findViewById(R.id.diynewBack);
		ImageView mGo = findViewById(R.id.diynewGo);
		mEditTitle = findViewById(R.id.diynewEditTitle);
		mEditDescription = findViewById(R.id.diynewEditDescription);
		mEditValue = findViewById(R.id.diynewEditValue);
		mExtraParent = findViewById(R.id.diyNewExtraParent);
		mSwitchSe = findViewById(R.id.diyNewSwitchSe);
		ImageView mQuestion = findViewById(R.id.diyNewQuestion);
		mQuestion.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				pop.showAsDropDown(view);
			}
		});
		mBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
		mGo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				final String title = mEditTitle.getText().toString();
				final String description = mEditDescription.getText().toString();
				final String value = mEditValue.getText().toString();
				final String extra = mEditExtra.getText().toString();
				if (title.equals("")) {
					MToastUtils.makeText("标题不能为空", MToastUtils.LENGTH_SHORT).show();
					return;
				} else if (value.equals("")) {
					MToastUtils.makeText("值不能为空", MToastUtils.LENGTH_SHORT).show();
					return;
				}
				switch (type) {
					case Diy.WEBPAGE:
						Pattern httpPattern;
						//初始化正则
						httpPattern = Pattern.compile("(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]");
						//开始判断了
						if (httpPattern.matcher(value).matches()) {
							//这是一个网址链接
							saveInfo(title, description, extra, value);
						} else {
							dialog.setTitle("错误")
									.setMessage("输入的链接不是有效的链接，是否继续保存？")
									.setNegativeButton("取消", new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface di, int p2) {
											di.cancel();
										}
									})
									.setPositiveButton("继续", new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface di, int p2) {
											saveInfo(title, description, extra, value);
											di.cancel();
										}
									}).create().show();
						}
						break;
					default:
						saveInfo(title, description, extra, value);
						break;
				}
			}
		});
		
		
	}
	
	/**
	 * 创建PopupWindow
	 */
	public void initPop() {
		//加载布局
		View contentView = View.inflate(this, R.layout.text, null);
		//创建pop窗口
		//1.contentView 内部布局
		//2.pop窗口的宽度与高度一般设置成 WRAP_CONTENT
		//3.最后一个参数 代表是否聚集
		pop = new PopupWindow(contentView,
				(int) (MWindowsUtils.getWidth() * 0.6),
				ViewGroup.LayoutParams.WRAP_CONTENT, true);
		TextView mPopText = contentView.findViewById(R.id.textText);
		mPopText.setText("正则表达式匹配 : 作用域为纯正则表达式\n普通匹配 : *代表全局\n*.abc.com代表abc.com的所有子域名(包括abc.com)\n多个域名用,(英文逗号)分割");
		//在此pop的区域 外点击关闭此窗口
		pop.setOutsideTouchable(true);
		//设置一个背景
		//pop.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_launcher));
		//设置一个空背景
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), 100);
		pop.setBackgroundDrawable(new BitmapDrawable(getResources(), bitmap));
		
	}
	
	private void saveInfo(String title, String description, String extra, String value) {
		String regex = extra;
		//初始化正则 
		if (regex == null)
			regex = "";
		switch (type) {
			case Diy.PLUGIN:
				if (mSwitchSe.isChecked()) {//正则表达式匹配
					if (regex.equals(""))
						regex = "[^\\s]*";
					try {
						Pattern.compile(regex + "");
					} catch (PatternSyntaxException e) {
						MToastUtils.makeText("作用域语法错误", MToastUtils.LENGTH_SHORT).show();
						return;
					}
				} else {
					regex = "[#]" + regex;
				}
				break;
			case Diy.SEARCH_ENGINE:
				if (regex.equals(""))
					regex = "[^\\s]*";
				if (!value.contains("%s")) {
					MToastUtils.makeText("搜索引擎地址必须包含搜索关键字占用符 %s ", MToastUtils.LENGTH_SHORT).show();
					return;
				}
				break;
		}
		if (isNew) {
			DBC.getInstance(this).addDiy(title, description, value, regex, type, false);
			MToastUtils.makeText("添加成功", MToastUtils.LENGTH_SHORT).show();
		} else {
			DBC.getInstance(this).modDiy(title, description, value, regex, id + "");
			MToastUtils.makeText("修改成功", MToastUtils.LENGTH_SHORT).show();
		}
		finish();
	}
}
