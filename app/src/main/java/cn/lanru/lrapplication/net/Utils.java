package cn.lanru.lrapplication.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class Utils {

  /**
   * 判断网络是否连接
   *
   * @param context
   * @return
   */
  public static boolean isConnected(Context context) {

    ConnectivityManager connectivity = (ConnectivityManager) context
        .getSystemService(Context.CONNECTIVITY_SERVICE);

    if (null != connectivity) {

      NetworkInfo info = connectivity.getActiveNetworkInfo();
      if (null != info && info.isConnected()) {
        if (info.isConnected()) {
          return true;
        }
      }
    }
    return false;
  }

}
