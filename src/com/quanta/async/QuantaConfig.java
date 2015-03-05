package com.quanta.async;

/**
 * 针对quanta包的全局配置文件
 * @author wangjiewen
 *
 */
public class QuantaConfig {

	/**
	 * 每个应用放实体model的包名，必须设置，否则将会找不到路径
	 */
	public final static String MODEL_PACKAGE = "com.gw.smstransmit.model";
	
	/**
	 * 全局sharepreference的名字, 建议根据自己的应用设置
	 */
	public final static String SHARE_PREFERENCE_NAME = "com.gw.smstransmit.async.sp.global";
	
	/**
	 * 持久化cookie的键值，保存在sharepreference中
	 */
	public final static String PERSISTENT_COOKIE = "_persistent_cookie";
}
