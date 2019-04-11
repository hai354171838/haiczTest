package com.tensquare.sms.listener;

import com.tensquare.sms.util.QcloudSmsUtil;
import com.tensquare.sms.util.SmsUtil;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author:Haicz
 * @date:2019/02/28
 * 短信监听类
 */
@Component
@RabbitListener(queues = "sms",containerFactory = "customContainerFactory")
public class SmsListener {
    @Autowired
    private SmsUtil smsUtil;

    @Value("${aliyun.sms.template_code}")
    private String template_code;//模板编号

    @Value("${aliyun.sms.sign_name}")
    private String sign_name;//签名

    /**
     * 接受短信信息
     * @param map
     */
    @RabbitHandler
    public void receiveSMS(Map<String,String> map ) throws InterruptedException {
        //Thread.sleep(1000*20);
        System.out.println("手机号码:"+map.get("mobile"));
        System.out.println("短信验证码:"+map.get("code"));
        System.out.println("template_code="+template_code);
        System.out.println("sign_name="+sign_name);
        QcloudSmsUtil.sendSms(map);

       /* try {
            //{"":""} --{\"\":\"\"}--{\"code\":\""+map.get("code")+"\"}
            smsUtil.sendSms(map.get("mobile"),template_code,sign_name," {\"code\":\""+map.get("code")+"\"}");
        } catch (ClientException e) {
            e.printStackTrace();
        }*/

    }
}
