package cn.com.zlct.baseokhttpdemo.util;

import android.content.Context;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.util.HashMap;
import java.util.Map;

import cn.com.zlct.baseokhttpdemo.model.GetSecretKeyEntity;

/**
 * 微信支付工具类
 */
public class WeChatUtil {

    public static String weChatPay(Context context, GetSecretKeyEntity.DataEntity userRecharge){
        String payInfo = userRecharge.getPrivateKey();
        Map<String, String> mapInfo = splitInfo(context, payInfo);
        String appid = mapInfo.get("appid");
        /*Log.e("logp", "wechatPay: " + mapInfo.get("appid") + "\n" + mapInfo.get("partnerid") + "\n"
                + mapInfo.get("prepayid") + "\n" + mapInfo.get("noncestr") + "\n" + mapInfo.get("timestamp")
                + "\n" + mapInfo.get("sign"));*/
        IWXAPI api = WXAPIFactory.createWXAPI(context, appid);
        api.registerApp(appid);
        if (!api.isWXAppInstalled()) {//未安装微信
            Toast.makeText(context, "您未安装微信", Toast.LENGTH_SHORT).show();
            return null;
        }
        if(!api.isWXAppSupportAPI()) {
            Toast.makeText(context, "当前版本不支持支付功能", Toast.LENGTH_SHORT).show();
            return null;
        }
        if (mapInfo != null) {
            PayReq req = new PayReq();
            // req.appId = Constant.IdString.WeChat_APP_ID;
            req.appId = appid;
//          req.partnerId = Contants.IdString.WeChatPartnerId;
            req.partnerId = mapInfo.get("partnerid");
            req.prepayId = mapInfo.get("prepayid");//预支付交易会话ID 统一下单时返回
            req.packageValue = "Sign=WXPay";
            req.nonceStr = mapInfo.get("noncestr");//随机字符串 统一下单时返回
            req.timeStamp = mapInfo.get("timestamp");//时间戳 统一下单时返回
            req.sign = mapInfo.get("sign");//签名
            api.sendReq(req);
            return "suc";
        } else {
            Toast.makeText(context, "服务器异常，请稍候再试", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    /**
     * 处理支付信息
     */
    public static Map<String, String> splitInfo(Context context, String payInfo) {
        try{
            String[] info = payInfo.split("&");
            Map<String, String> map = new HashMap<>();
            for (int i = 0; i < info.length; i++) {
                String[] items = info[i].split("=");
                map.put(items[0], items[1]);
            }
            return map;
        } catch (Exception e){
            Toast.makeText(context, "数据异常", Toast.LENGTH_SHORT).show();
        }
        return null;
    }
}
