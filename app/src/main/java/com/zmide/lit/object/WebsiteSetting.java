package com.zmide.lit.object;

import com.zmide.lit.base.MApplication;
import com.zmide.lit.util.DBC;

import java.util.ArrayList;

public class WebsiteSetting {
	public int id = 0;
	public boolean ad_host;
	public boolean app;
	public boolean js;
	public boolean no_history;
	public boolean no_picture;
	public int clip_enable;
	public String site;
	public boolean state;
	public int ua;
    
	public setting[] get(){
		ArrayList<Diy> diys = DBC.getInstance(MApplication.getContext()).getDiys(Diy.UA,false);
		String[] names = new String[diys.size()+1];
		int[] ids = new int[diys.size()+1];
		names[0] = "跟随默认";
		ids[0] = 0;
		for (int i = 1;i<diys.size()+1;i++){
			names[i] = diys.get(i-1).title;
			ids[i] = diys.get(i-1).id;
		}
        setting[] settings = {new setting("独立网站设置", true, 0, state),
            new setting("User Agent", false, ua, false, names,ids),
            new setting("读写剪贴板",false,clip_enable,false,new String[]{"询问","允许","拒绝","跟随默认"}),
            new setting("打开外部应用", true, 0, app),
            new setting("JavaScript脚本", true, 0, js),
            new setting("无痕浏览", true, 0, no_history),
            new setting("无图模式", true, 0, no_picture)};
		return settings;
	}
	
	public WebsiteSetting set(int p,boolean t){
		switch (p){
			case 0:
				state = t;
				break;
			case 3:
				app = t;
				break;
			case 4:
				js = t;
				break;
			case 5:
				no_history = t;
				break;
			case 6:
				no_picture = t;
				break;
		}
		return this;
	}
	
	public String text(){
	  return "id:"+id+"\n"+
					 "site:"+site+"\n"+
					 "js:"+js+"\n"+
					 "ua:"+ua+"\n"+
					 "app:"+app+"\n"+
				     "state:"+state+"\n"+
					 "no_history:"+no_history+"\n"+
					 "no_picture:"+no_picture;
	}
	public WebsiteSetting set(int p,int t){
		switch (p){
			case 1:
				ua = t;
				break;
			case 2:
				clip_enable = t;
				break;
		}
		return this;
	}
	public static class setting{
		public String name;
		public boolean isSwitch;
		public int text;
		public boolean state;
		public String[] chooses;
		public int[] ids;
		
		public setting(String name, boolean isSwitch, int text, boolean state) {
			this.name = name;
			this.isSwitch = isSwitch;
			this.text = text;
			this.state = state;
		}
		public setting(String name, boolean isSwitch, int text, boolean state,String[] chooses) {
			this.name = name;
			this.isSwitch = isSwitch;
			this.text = text;
			this.state = state;
			this.chooses = chooses;
		}
		public setting(String name, boolean isSwitch, int text, boolean state,String[] chooses,int[] ids) {
			this.name = name;
			this.isSwitch = isSwitch;
			this.text = text;
			this.state = state;
			this.chooses = chooses;
			this.ids = ids;
		}
	}
}
