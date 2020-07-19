package com.zmide.lit.object;

public class Diy {
	public final static int PLUGIN = 0;
	public final static int WEBPAGE = 1;
	public final static int SEARCH_ENGINE = 2;
	public final static int UA = 3;
	public int id;
	public String title;
	public String value;
	public String extra;
	public String time;
	public String description;
	public boolean isrun;
	
	public boolean isLimit(int t) {
		switch (t) {
			case PLUGIN:
				return false;
			case WEBPAGE:
			case SEARCH_ENGINE:
			case UA:
				return true;
		}
		return true;
	}
	
}
