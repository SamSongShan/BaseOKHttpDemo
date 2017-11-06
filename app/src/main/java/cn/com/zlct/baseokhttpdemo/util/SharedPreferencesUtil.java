package cn.com.zlct.baseokhttpdemo.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;

/**
 * 保存用户信息工具类
 */
public class SharedPreferencesUtil {

    /**
     * 保存用户信息到本地
     */
    public static void saveUserInfo(Context context, String data){
        SharedPreferences userInfo = context.getSharedPreferences("userInfo", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString("UserInfo", data);
        editor.commit();
    }

    /**
     * 保存用户账户信息到本地
     */
    public static void saveUserAccount(Context context, String data){
        SharedPreferences accountInfo = context.getSharedPreferences("userAccount", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = accountInfo.edit();
        editor.putString("UserAccount", data);
        editor.commit();
    }

    /**
     * 保存店铺信息到本地
     */
    public static void saveStoreInfo(Context context, String data){
        SharedPreferences userInfo = context.getSharedPreferences("storeInfo", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString("StoreInfo", data);
        editor.commit();
    }

    /**
     * 保存用户信息到本地
     */
    public static void saveData(Context context, String preferName, String dataName, String data){
        SharedPreferences dataInfo = context.getSharedPreferences(preferName, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = dataInfo.edit();
        editor.putString(dataName, data);
        editor.commit();
    }

    /**
     * 保存支付信息到本地
     */
    public static void saveWeChatPay(Context context, String name, String type, float money){
        SharedPreferences payInfo = context.getSharedPreferences(name, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = payInfo.edit();
        editor.putString("PayType", type);
        editor.putFloat("PayMoney", money);
        editor.commit();
    }
    /**
     * 保存支付信息到本地
     * @param context 上下文对象
     * @param name SharedPreferences名字
     */
    public static void saveWeChatPay(Context context, String name, String type, String money, boolean isCooper){
        SharedPreferences payInfo = context.getSharedPreferences(name, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = payInfo.edit();
        editor.putString("PayType", type);
        editor.putString("PayMoney", money);
        editor.putBoolean("isCooper", isCooper);
        editor.commit();
    }
    /**
     * 修改用户数据
     *//*
    public static void SubmitUserInfo(Context context, String name, String data, OkHttpUtil.OnDataListener dataListener){
        Gson gson = new GsonBuilder().create();
        String jsonString = gson.toJson(new UpUserBasicInfo(getUserId(context), name, data));
        OkHttpUtil.postJson(Constant.URL.UpdateUserBasicInformation, DesUtil.encrypt(jsonString), dataListener);
    }

    *//**
     * 修改店铺数据
     *//*
    public static void SubmitStoreInfo(Context context, String mallId, String name, String data, OkHttpUtil.OnDataListener dataListener){
        Gson gson = new GsonBuilder().create();
        String jsonString = gson.toJson(new UpMallBasicInfo(getUserId(context), mallId, name, data));
        OkHttpUtil.postJson(Constant.URL.UpdateMallBasicInformation, DesUtil.encrypt(jsonString), dataListener);
    }*/

    /**
     * 从本地获取UserId
     */
    public static String getUserId(Context context){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SharedPreferences user = context.getSharedPreferences("user", context.MODE_PRIVATE);
        try {
            long loginTime = user.getLong("LoginTime", sdf.parse("2016-01-01").getTime());
            if ((System.currentTimeMillis() - loginTime) > (30 * 24 * 60 * 60 * 1000L)) {//距离上次登录已超30天
                return "default";
            } else {//距离上次登录未超30天
                String userId = user.getString("UserId", "default");
                if (!"default".equals(userId)) {
                    userId = DesUtil.decrypt(userId, DesUtil.LOCAL_KEY);
                }
                return userId;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "default";
    }


    public static String getMallId(Context context) {
        SharedPreferences user = context.getSharedPreferences("user", context.MODE_PRIVATE);
        String mallId = user.getString("MallId", "default");
        try {
            if (!"default".equals(mallId)) {
                mallId = DesUtil.decrypt(mallId, DesUtil.LOCAL_KEY);
            }
            return mallId;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "default";
    }
}
