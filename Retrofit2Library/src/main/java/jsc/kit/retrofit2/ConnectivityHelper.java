package jsc.kit.retrofit2;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.Objects;

/**
 * 
 * ConnectivityHelper 网络工具
 *
 *
 */
public class ConnectivityHelper {
	
	// 运营商判断
	public static final int CHINA_MOBILE_TYPE = 0;
	public static final int CHINA_UNICOM_TYPE = 1;
	public static final int CHINA_TELECOM_TYPE = 2;

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		Objects.requireNonNull(cm);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		return networkInfo != null && networkInfo.isAvailable();
	}
	
	/**
	 * 判断网络是否可用
	 * 
	 * @param context context
	 * @return boolean
	 */
	public static boolean isConnectivityAvailable(Context context) {
		// 判断网络是否可用
		NetworkInfo info = null;
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		Objects.requireNonNull(cm);
		info = cm.getActiveNetworkInfo();
		if (info == null || !info.isConnected()) {
			return false;
		}
		if (info.isAvailable()) {
			return true;
		}
		return info.isRoaming();
	}

	/**
	 * 判断wifi网络是否可用
	 * 
	 * @param context context
	 * @return boolean
	 */
	public static boolean WifiIsAvailable(Context context) {
		// 判断网络是否可用
		NetworkInfo info = null;
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		Objects.requireNonNull(cm);
		info = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return info != null && info.isAvailable();
	}
	
	/**
	 * 判断moblie网络是否可用
	 * 
	 * @param context context
	 * @return boolean
	 */
	public static boolean MobileIsAvailable(Context context) {
		// 判断网络是否可用
		NetworkInfo info = null;
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		Objects.requireNonNull(cm);
		info = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		return info != null && info.isAvailable();
	}
	
	/**
	 * 
	 * 获得手机网络类别
	 * @param IMSI IMSI
     * @return net work type
	 */

	public static int getPhoneNetworkType(String IMSI) {

		// IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。

		System.out.println(IMSI);

		if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {

			return CHINA_MOBILE_TYPE;

		} else if (IMSI.startsWith("46001")) {

			return CHINA_UNICOM_TYPE;

		} else if (IMSI.startsWith("46003")) {

			return CHINA_TELECOM_TYPE;

		}
		return CHINA_UNICOM_TYPE;
	}

	public static final int NONET = -1;
	public static final int CMNET = 3;
	public static final int CMWAP = 2;
	public static final int WIFI = 1;

	/**
	 * 获取当前的网络状态 -1：没有网络 1：WIFI网络2：wap网络3：net网络
	 * 
	 * @param context context
	 * @return net work type
	 */
	public static int getAPNType(Context context) {
		int netType = NONET;
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		Objects.requireNonNull(cm);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();

		if (networkInfo == null) {
			return netType;
		}
		int nType = networkInfo.getType();
		System.out.println("networkInfo.getExtraInfo() is " + networkInfo.getExtraInfo());
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			if( networkInfo.getExtraInfo() == null){
				return netType;
			}
			if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
				netType = CMNET;
			} else {
				netType = CMWAP;
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = WIFI;
		}
		return netType;
	}


}
