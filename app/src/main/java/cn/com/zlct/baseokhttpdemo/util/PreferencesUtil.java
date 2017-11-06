package cn.com.zlct.baseokhttpdemo.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import cn.com.zlct.baseokhttpdemo.AppContext;


/**
 * 保存用户信息工具类
 */
public class PreferencesUtil {

    /**
     * 保存用户信息到本地
     */
    public static void saveUserInfo(Context context, String data) {
        SharedPreferences userInfo = context.getSharedPreferences("userInfo", context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userInfo.edit();
        editor.putString("UserInfo", data);
        editor.commit();
    }

    /**
     * 保存用户信息到本地
     */
    public static void saveData(Context context, String preferName, String dataName, String data) {
        SharedPreferences dataInfo = context.getSharedPreferences(preferName, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = dataInfo.edit();
        editor.putString(dataName, data);
        editor.commit();
    }

    /**
     * 保存支付信息到本地
     */
//    public static void saveWeChatPay(Context context, String name, String type, float money){
//        SharedPreferences payInfo = context.getSharedPreferences(name, context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = payInfo.edit();
//        editor.putString("PayType", type);
//        editor.putFloat("PayMoney", money);
//        editor.commit();
//    }
    public static void saveWeChatPay(Context context, String name, String type, String money, boolean isCooper) {
        SharedPreferences payInfo = context.getSharedPreferences(name, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = payInfo.edit();
        editor.putString("PayType", type);
        editor.putString("PayMoney", money);
        editor.putBoolean("isCooper", isCooper);
        editor.commit();
    }

   /* *//**
     * 修改用户数据
     *//*
    public static void submitUserInfo(Context context, String name, String data, OkHttpUtil.OnDataListener dataListener) {
        Gson gson = new GsonBuilder().create();
        String jsonString = gson.toJson(new UpdateUserInfo(getUserId(context), name, data));
        OkHttpUtil.postJson(Constant.URL.UpdateUserEntity, DesUtil.encrypt(jsonString), dataListener);
    }*/

    /**
     * 清除本地保存数据
     */
    public static void clearData(Activity activity) {
        SharedPreferences.Editor editor = activity.getSharedPreferences("user", activity.MODE_PRIVATE).edit();
        editor.putString("UserId", null);
        editor.putBoolean("IsCertify", false);
        try {
            editor.putLong("LoginTime", new SimpleDateFormat("yyyy-MM-dd").parse("2016-01-01").getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        editor.commit();
        ((AppContext) activity.getApplication()).setCartCount(0);
        saveUserInfo(activity, null);


    }

    /**
     * 从本地获取UserId
     */
    public static String getUserId(Context context) {
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

    /**
     * 获取本地的用户信息
     *//*
    public static UserInfoEntity.DataEntity getUserInfo(Context context) {
        SharedPreferences userInfo = context.getSharedPreferences("userInfo", context.MODE_PRIVATE);
        String info = userInfo.getString("UserInfo", null);
        if (info != null) {
            try {
                info = DesUtil.decrypt(info, DesUtil.LOCAL_KEY);
                UserInfoEntity infoEntity = new Gson().fromJson(info, UserInfoEntity.class);
                return infoEntity.getData();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }*/

    public static void showShare(Context context, String title, String url, String text, String imgPath,
                                 View.OnClickListener listener) {
       /* OnekeyShare oks = new OnekeyShare();
        //追加复制按钮
//        Bitmap copy = BitmapFactory.decodeResource(context.getResources(), R.drawable.copy_link);
//        oks.setCustomerLogo(copy, "复制链接", listener);

        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle(title);
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl(url);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(text);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath(imgPath);//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(url);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("大家快来注册！");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(context.getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(url);

        // 启动分享GUI
        oks.show(context);*/
    }
}
