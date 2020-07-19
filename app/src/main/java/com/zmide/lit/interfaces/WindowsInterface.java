package com.zmide.lit.interfaces;

public interface WindowsInterface {
	void onWindowsStateChanged();//标签页内容发生变化监听器
	
	void onWindowsCountChanged(int count);//标签页数量变化监听器
}