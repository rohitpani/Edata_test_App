package com.edatasolutions.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class NetworkConectivity {
	/**
	 * Checks Internet Connectivity.
	 *
	 * @param context Context reference of calling class
	 * @return returns true if connected else false.
	 */

	public static boolean checkConnectivity(Context context) {
			ConnectivityManager connectivityManager
					= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
			return activeNetworkInfo != null && activeNetworkInfo.isConnected();
		}
}
