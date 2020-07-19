package com.zmide.lit.bean;

import android.content.Intent;
import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.zmide.lit.BR;

public class Setting extends BaseObservable {
	public String title;
	public Intent intent;
	
	public Setting(String title, Intent intent) {
		this.title = title;
		this.intent = intent;
	}
	
	@Bindable
	public String getTitle() {
		return title;
	}
	
	
	public void setTitle(String title) {
		this.title = title;
		notifyPropertyChanged(BR.title);
	}
	
	public void onclick(View view) {
		view.getContext().startActivity(intent);
	}
	
	
}
