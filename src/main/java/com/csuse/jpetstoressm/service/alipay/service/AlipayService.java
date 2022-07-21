package com.csuse.jpetstoressm.service.alipay.service;

import com.alipay.api.AlipayApiException;
import com.csuse.jpetstoressm.service.alipay.bean.AlipayBean;
import com.csuse.jpetstoressm.service.alipay.config.Alipay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AlipayService {

    @Autowired
    private Alipay alipay;

    public String aliPay(AlipayBean alipayBean) throws AlipayApiException {
        return alipay.pay(alipayBean);
    }
}
