package com.zmide.lit.bean;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.zmide.lit.BR;

public class AboutBean extends BaseObservable {
	private String version;
	
	public AboutBean(String ver) {
		this.setVersion(ver);
	}
	
	@Bindable
	public String getVersion() {
		return version;
	}
	
	
	public void setVersion(String version) {
		this.version = version;
		notifyPropertyChanged(BR.version);
	}
	
	
}
