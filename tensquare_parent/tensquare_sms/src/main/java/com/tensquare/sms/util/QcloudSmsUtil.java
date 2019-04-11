package com.tensquare.sms.util;

import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsMultiSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import org.json.JSONException;

import java.io.IOException;
import java.util.Map;

/**
 * @author:Haicz
 * @date:2019/01/15
 */
public class QcloudSmsUtil {

    public static void sendSms(Map<String,String> map) {
        // 短信应用SDK AppID
        int appid =1400178622; // 1400开头
        //int appid = Integer.parseInt(map.get("appid"));
        // 短信应用SDK AppKey
        String appkey = "7b5c71e54955ea4ce3c80780f4566dda";
        //String appkey = map.get("appkey");
        // 需要发送短信的手机号码
        //String[] phoneNumbers = {"15220091463"};
         String[] phoneNumbers= new String[1];
          phoneNumbers[0]=map.get("mobile");
        // 短信模板ID，需要在短信应用中申请
        // NOTE: 这里的模板ID`7839`只是一个示例，
        // 真实的模板ID需要在短信控制台中申请
        int templateId =266748;
       // int templateId = Integer.parseInt(map.get("templateId"));

        // 签名
        // NOTE: 这里的签名"腾讯云"只是一个示例，
        // 真实的签名需要在短信控制台中申请，另外
        // 签名参数使用的是`签名内容`，而不是`签名ID`
        String smsSign = "小海哥";
        //String smsSign= map.get("smsSign");

        // 指定模板ID单发短信
        String[] params = new String[1] ;
        String code = map.get("code");
        params[0]=code;
        try {
            SmsMultiSender msender = new SmsMultiSender(appid, appkey);
            SmsMultiSenderResult result =  msender.sendWithParam("86", phoneNumbers,
                    templateId, params, smsSign, "", "");  // 签名参数未提供或者为空时，会使用默认签名发送短信
            System.out.print(result);
        } catch (HTTPException e) {
            // HTTP响应码错误
            e.printStackTrace();
        } catch (JSONException e) {
            // json解析错误
            e.printStackTrace();
        } catch (IOException e) {
            // 网络IO错误
            e.printStackTrace();
        }

    }
}
