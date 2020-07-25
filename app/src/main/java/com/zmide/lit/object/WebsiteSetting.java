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
		switch (p){
			case 0:
				return new setting("独立网站设置",true,"",state);
			case 1:
				return new setting("广告拦截",true,"",ad_host);
			case 2:
				return new setting("打开外部应用",true,"",app);
			case 3:
				return new setting("JavaScript脚本",true,"",js);
			case 4:
				return new setting("无痕浏览",true,"",no_history);
			case 5:
				return new setting("无图模式",true,"",no_picture);
		}
		return null;
	}
	public WebsiteSetting set(int p,boolean t){
		switch (p){
			case 0:
				state = t;
			case 1:
				ad_host = t;
			case 2:
				app = t;
			case 3:
				js = t;
			case 4:
				no_history = t;
			case 5:
				no_picture = t;
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
	public class setting{
		public String name;
		public boolean isSwitch;
		public String text;
		public boolean state;
		
		public setting(String name, boolean isSwitch, String text, boolean state) {
			this.name = name;
			this.isSwitch = isSwitch;
			this.text = text;
			this.state = state;
		}
	}
}
