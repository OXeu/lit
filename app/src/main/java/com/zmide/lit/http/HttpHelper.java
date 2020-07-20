package com.zmide.lit.http;

public class HttpHelper {
	public final static int FAIL = -1;//失败
	public final static int SUCCESS = 0;//成功
	public final static int UNKNOWN_ERROR = 1;//未知错误
	public final static int PARAM_ERROR = 2;//参数错误
	//101～199 用户登录错误
	public final static int PW_ERROR = 101;//密码错误
	public final static int USER_UNKNOWN = 102;//用户不存在
	public final static int USER_EXIST = 103;//用户已存在
	public final static int TOKEN_EXPIRED = 104;//令牌过期
	public final static int NO_USER = 105;//请输入用户名密码
	public final static int LOGIN_NEEDED = 106;//请先登录
	//201-299同步错误
	public final static int SYN_FAIL = 200;//同步失败，未知错误
	public final static int CREATE_ERROR = 201;//创建错误
	public final static int DESTROY_ERROR = 202;//删除错误
	//public final static int THREAD_NIL=203;//帖子不存在
	//301-399操作错误
	public final static int OPERATE_FAIL = 301;//操作失败
	private final static String HTTP_MAIN = "http://106.15.249.189/public/index.php";//网址
	//public final static String HTTP_IMAGE = HTTP_MAIN+"/image";//图片文件夹
	public final static String LOGIN = HTTP_MAIN + "/login"; //登录
	public final static String REG = HTTP_MAIN + "/register";//注册
	public static final String FORGET = HTTP_MAIN + "/forget";
	public static final String SEND_MAIL = HTTP_MAIN + "/send_mail";
	//public final static String POST = HTTP_MAIN + "/post";//回复
	//public final static String BBS_PLATE = HTTP_MAIN + "/bbs_k";//论坛板块
	public final static String SYN = HTTP_MAIN + "/syn";//同步书签
	public final static String GBM = HTTP_MAIN + "/gbm";//获取基础书签
	public final static String UBM = HTTP_MAIN + "/ubm";//更新基础书签
	//public final static String ME=HTTP_MAIN +"/me";//我的
	public static final String UPDATE = HTTP_MAIN + "/update";
	public static final String NEWS = HTTP_MAIN + "/news";
	public static final String MARK = HTTP_MAIN + "/m";

	
}


