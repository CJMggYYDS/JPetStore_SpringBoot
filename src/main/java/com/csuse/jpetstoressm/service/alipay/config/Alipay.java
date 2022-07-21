package com.csuse.jpetstoressm.service.alipay.config;


import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.csuse.jpetstoressm.service.alipay.bean.AlipayBean;
import org.springframework.stereotype.Component;

@Component
public class Alipay {

    private static final String serverUrl="https://openapi.alipaydev.com/gateway.do";
    private static final String appId = "2021000119641165";
    private static final String privateKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCKA+P0YLzz4bgK5YW3hIzA6Kv0amz01JV+BNIVUEc+sS8k5EJAUVZcTcaSRVf+0rjMwmdDjp9V3WUdyBqyu8sW2yKUMNqaiTYjsymqoEFpIBAB59zZRtVWswSgfGx6/14DTxml9B1kVXPG9x5NVc66tgIGWVB9x5V6cMbe63KNDQc4k5bF5LEtxjA3TvRkAL8PH5gAUkmcSUXuhpOxPACM2PZ1/fRfF+3xoZzgF3d8uIbmHShn3PvwNw/5vn+AgY3VnVjf97nIQA0knVWz9M0PBvocadIfqGCOB1rT6PrNNRBsxaohM+UI5evz57Gvf99rdC0w4Oswbw+4s6cNqE1HAgMBAAECggEARLqDPiMedWSmPebmm+kKHta/KswEzjc+6q6D39FGaVXa4Eg7JcRbeUPR6u1VULSnCnCU8/OGLUA/JoGLqg4SqKMfqSRa+DH0H/sMeMetSyiIUCqitCCuBKrJVxIfA3E/4+30G8xhBqH3MdHF6UUd2aThhT/c8YX+TibzIfel588g3dRPJLIJOYc7zpYYKVtE2YT1WEH+qcVLea9ufHzBIHTP0pW9s642xH/XJocDyyGjAZVkw9tUXczYb+BEf6CRBIq2t/ASyjIv9P6/G8fKyxZVdX9PXdqR5Z7uXnsA7IsJZ0OAPjnoWioRQ5/psXq2Pez97PA1rShrestlX9nr4QKBgQDswYRQtTpJp3Bxpv+umEMxToz3ICFzyFsueojiP3SInz2RT8sh0eNSvWaIgnR1RmjdhtseRScEf+lTwvv0AebXAqX090hwgTtfcKBkj/UabekTfnY0aOWJhJWgtLySfS0F6aPht82ujNYducf3D2fxwyQBNwN1yZ2rj2kogVpkCwKBgQCVO7/CHpZnDrl6vrtXv1nqlrvdrHTOFGirXpHJ0xVU/ymfQ1W9/Sg5ihZnPl5u2g2pCHe80Hj4FrbsNtEx2l84LiX1aW54+60HAbexhVZGxB+/beCARhLB81FApxXSNiVs7AQi0y8AZCJKbtO5nEMqzhirCQjU5pDwa4EGcfQlNQKBgF6MOYbxmMFpOKJqipjy/YJnWCwojLNsQ92f2aJgrO0SisfJEdoDs8CLGrNRRai/4Yj2HfNMsnQ9kLR+nZeoWsNqr1PQnQEw2sEWfTcrtjpDwlyimBn9+gydAhQXmWb2zlAaKfQuLtmSLL/aPV0QrfYCdBJ4Ic2cIncs4lXmsm5ZAoGACLW5BmpkmtU4LBrPslpftBpiEpkLJC/6v+3AEoh4WUKfOkVizrjkzR7rGdGBD3U4kOIGowWD2e7xdC3q3b8CCbRf0Mf7g6IkwiDuiLTgiTbPJ4wRK0trstgxqNVXp7IzUv0fbKzotFwjyDZZsx6UqP3oYKshUdO3J+EOYcLq3fUCgYAEXTCwZ8f63UBEEJIGdlKL/Tr0XICd3sZzIjD6D6ePJpUdbMwRb7GQUugtd3swZHfEN/eCpFwYSSmo3SYuAJZi/tsul8lesJiBK3KURjeWlJ0RyVGARu/ysAJwZYl4aNuQ8FljtAjBmB0B9UrV5tTpweuejlo5hxVIRLCr3JDWCQ==";
    private static final String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtnH8a5kcWv8rjgxiFjWCuVPKuCLBhS40Sx0FrtbBYOeZ6okX3ZTSpys+p6EJScdk6xiyLL+HMy4xNy7yi9NoeIXR+cq7RlUDflpooVuBVdJMi0MLzBttGSYzeGtiINxSesu7ak1McuuABqjHYS1BlIf1TQk7OUqdzw1oUmyw3D4T7QJuulYIAvDWnawpUxfHgYxlpcUp2a4SLA6bumfUdEjEwLzYl0nlNhm3kSRC76NM4zr/UAvnKN6NxH4GkL9H6+GOQnGoWK0ALlY1pivVu95Q7XSXWpGittuKZV6taHLNLsykZ5+lBJuVT9Ug7oKEknReoOtZN3wFzE+hWS0QsQIDAQAB";
    private static final String format = "json";
    private static final String charset = "UTF-8";
    private static final String notifyUrl = "";
    private static final String returnUrl = "http://localhost:8080/order/paySuccess";
    private static final String signType = "RSA2";

    public String pay(AlipayBean alipayBean) throws AlipayApiException {


        // 1、获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(serverUrl, appId, privateKey, format, charset, publicKey, signType);
        // 2、设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        // 3、页面跳转同步通知页面路径
        alipayRequest.setReturnUrl(returnUrl);
        // 4、服务器异步通知页面路径(没用,不设置了)
        //alipayRequest.setNotifyUrl(notifyUrl);
        // 5、封装参数
        alipayRequest.setBizContent(JSON.toJSONString(alipayBean));
        // 6、请求支付宝进行付款，并获取支付结果
        return alipayClient.pageExecute(alipayRequest).getBody();

    }
}
