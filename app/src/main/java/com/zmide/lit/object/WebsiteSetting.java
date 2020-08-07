package com.zmide.lit.object;

public class WebsiteSetting {
	public int id = 0;
	public boolean ad_host;
	public boolean app;
	public boolean js;
	public boolean no_history;
	public boolean no_picture;
	public String site;
	public boolean state;
	public int ua;
	public setting get(int p){
		setting[] settings = {new setting("独立网站设置", true, 0, state),
				new setting("User Agent", false, ua, false),
				//new setting("广告拦截",true,0,ad_host),
				new setting("打开外部应用", true, 0, app),
				new setting("JavaScript脚本", true, 0, js),
				new setting("无痕浏览", true, 0, no_history),
				new setting("无图模式", true, 0, no_picture)};
		return settings[p];
	}
	public int count(){
		return 6;
	}
	public WebsiteSetting set(int p,boolean t){
		switch (p){
			case 0:
				state = t;
				break;
			/*case 2:
				ad_host = t;
				break;*/
			case 2:
				app = t;
				break;
			case 3:
				js = t;
				break;
			case 4:
				no_history = t;
				break;
			case 5:
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
		}
		return this;
	}
	public static class setting{
		public String name;
		public boolean isSwitch;
		public int text;
		public boolean state;
		
		public setting(String name, boolean isSwitch, int text, boolean state) {
			this.name = name;
			this.isSwitch = isSwitch;
			this.text = text;
			this.state = state;
		}
	}
}
