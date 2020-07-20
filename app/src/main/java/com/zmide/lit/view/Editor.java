package com.zmide.lit.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zmide.lit.R;
import com.zmide.lit.util.MWindowsUtils;

public class Editor extends LinearLayout {
	EditText et;
	private Drawable bDrawable, lDrawable;
	private Drawable afterDrawable;
	private Drawable beforeDrawable;
	private ImageView etd;
	private TextView etBt;
	private RelativeLayout etl;
	
	public Editor(Context context) {
		super(context);
	}
	
	@SuppressLint({"Recycle", "SetTextI18n"})
	public Editor(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.editor_bt, this, true);
		init();
		TypedArray t = getContext().obtainStyledAttributes(attrs,
				R.styleable.Editor);
		lDrawable = getResources().getDrawable(R.drawable.box_gray_15);
		bDrawable = getResources().getDrawable(R.drawable.box_blue_15);
		int textColor = t.getColor(R.styleable.Editor_textColor, getResources().getColor(R.color.accent));
		float textSize = t.getDimension(R.styleable.Editor_textSize, 10);
		beforeDrawable = t.getDrawable(R.styleable.Editor_drawableLeft);
		afterDrawable = t.getDrawable(R.styleable.Editor_drawableLeftAfter);
		et.setSingleLine(t.getBoolean(R.styleable.Editor_singleLine, false));
		et.setHint(t.getString(R.styleable.Editor_hint) + "");
		if (t.getString(R.styleable.Editor_text) != null)
		et.setText(t.getString(R.styleable.Editor_text) + "");
		if (t.getString(R.styleable.Editor_button_text) != null) {
			etBt.setText(t.getString(R.styleable.Editor_button_text) + "");
			etBt.setVisibility(VISIBLE);
		}
		et.setTextColor(textColor);
		//et.setTextSize(textSize);
		et.setHintTextColor(getResources().getColor(R.color.light));
		et.setMaxHeight(MWindowsUtils.getHeight() / 3);
		beforeDrawable.setTint(getResources().getColor(R.color.light));
		setLeftDrawable(beforeDrawable);
		bindAfterDrawable();
	}
	
	private void init() {
		et = findViewById(R.id.et);
		etd = findViewById(R.id.etDrawable);
		etl = findViewById(R.id.etl);
		etBt = findViewById(R.id.etBt);
		/*et.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int count, int after) {
				
				int mTextMaxlenght = 0;
				Editable editable = et.getText();
				String str = editable.toString().trim();
				//得到最初字段的长度大小，用于光标位置的判断
				int selEndIndex = Selection.getSelectionEnd(editable);
				// 取出每个字符进行判断，如果是字母数字和标点符号则为一个字符加1，
				//如果是汉字则为两个字符
				for (int i = 0; i < str.length(); i++) {
					char charAt = str.charAt(i);
					//32-122包含了空格，大小写字母，数字和一些常用的符号，
					//如果在这个范围内则算一个字符，
					// 如果不在这个范围比如是汉字的话就是两个字符
					if (charAt >= 32 && charAt <= 122) {
						mTextMaxlenght++;
					} else {
						mTextMaxlenght += 2;
					}
					// 当最大字符大于40时，进行字段的截取，并进行提示字段的大小
					if (mTextMaxlenght > 40) {
					// 截取最大的字段
						String newStr = str.substring(0, i);
						et.setText(newStr);
						// 得到新字段的长度值
						editable = et.getText();
						int newLen = editable.length();
						if (selEndIndex > newLen) {
							selEndIndex = editable.length();
						}
						// 设置新光标所在的位置
						Selection.setSelection(editable, selEndIndex);
						ToastUtil.makeText(context, "最大长度为40个字符或20个汉字！", ToastUtil.LENGTH_SHORT).show();
					}
					
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			
			}
			
			@Override
			public void afterTextChanged(Editable s) {
			
			}
		});*/
		
	}
	
	public void setBtDrawable(Drawable drawable) {
		etBt.setBackground(drawable);
	}
	
	public String getBtText() {
		return etBt.getText() + "";
	}
	
	public void setBtText(String text) {
		etBt.setText(text);
	}
	
	public void setBtOnClick(OnClickListener click) {
		etBt.setOnClickListener(click);
	}
	
	
	public EditText getEditor() {
		return et;
	}
	
	public void setLeftDrawable(int resource) {
		Drawable drawable = getResources().getDrawable(
				resource);
		setLeftDrawable(drawable);
	}
	
	public void setHint(String text) {
		et.setHint(text);
	}
	
	public void setLeftDrawable(Drawable drawable) {
		//这一步必须要做,否则不会显示.
		/*drawable.setBounds(0, 0, drawable.getMinimumWidth(),
				drawable.getMinimumHeight());
		et.setCompoundDrawables(drawable ,null, null, null);*/
		etd.setImageDrawable(drawable);
	}
	
	public void setLeftDrawableAfterFocus(int resource) {
		afterDrawable = getResources().getDrawable(
				resource);
		bindAfterDrawable();
	}
	
	public void setLeftDrawableAfterFocus(Drawable drawable) {
		afterDrawable = drawable;
		bindAfterDrawable();
	}
	
	public Editable getText() {
		return et.getText();
	}
	
	public void setText(String str) {
		et.setText(str);
	}
	
	private void bindAfterDrawable() {
		et.setOnFocusChangeListener((v, hasFocus) -> {
			if (hasFocus) {
				etl.setBackground(bDrawable);
				if (afterDrawable != null) {
					afterDrawable.setTint(getResources().getColor(R.color.accentColor));
					setLeftDrawable(afterDrawable);
				} else if (beforeDrawable != null) {
					beforeDrawable.setTint(getResources().getColor(R.color.accentColor));
					setLeftDrawable(beforeDrawable);
				}
			} else {
				etl.setBackground(lDrawable);
				if (beforeDrawable != null) {
					beforeDrawable.setTint(getResources().getColor(R.color.light));
					setLeftDrawable(beforeDrawable);
				}
			}
			
		});
	}
}