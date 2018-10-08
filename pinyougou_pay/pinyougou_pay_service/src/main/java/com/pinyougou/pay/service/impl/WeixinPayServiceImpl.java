package com.pinyougou.pay.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.wxpay.sdk.WXPayUtil;
import com.pinyougou.common.HttpClient;
import com.pinyougou.pay.service.WeixinPayService;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

@Service
public class WeixinPayServiceImpl implements WeixinPayService {

    @Value("${appid}")
    private String appid;
    @Value("${partner}")
    private String partner;
    @Value("${partnerkey}")
    private String partnerkey;
    @Value("${notifyurl}")
    private String notifyurl;

    @Override
    public Map<String, String> createNative(String out_trade_no, String total_fee) {
        Map<String, String> returnMap = null;
        try {
            //返回的Map
            returnMap = new HashMap<>();

            //组装发送的参数
            Map<String, String> param = new HashMap<>();
            //公众账号ID
            param.put("appid", appid);
            //商户号
            param.put("mch_id", partner);
            //随机字符串
            param.put("nonce_str", WXPayUtil.generateNonceStr());
            //签名；转换的时候自动生成
            //param.put("sign", "");
            //商品描述
            param.put("body", "品优购");
            //商户订单号
            param.put("out_trade_no", out_trade_no);
            //标价金额
            param.put("total_fee", total_fee);
            //终端IP
            param.put("spbill_create_ip", "127.0.0.1");
            //通知地址
            param.put("notify_url", notifyurl);
            //交易类型；扫码支付
            param.put("trade_type", "NATIVE");

            //转换为xml
            String signedXml = WXPayUtil.generateSignedXml(param, partnerkey);

            System.out.println("发送到微信 统一下单 的内容为：" + signedXml);

            //发送统一下单的到微信支付系统
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/unifiedorder");
            httpClient.setHttps(true);
            httpClient.setXmlParam(signedXml);
            httpClient.post();

            //获取返回结果
            String content = httpClient.getContent();
            System.out.println("调用微信 统一下单 的返回内容为：" + content);

            Map<String, String> map = WXPayUtil.xmlToMap(content);

            returnMap.put("out_trade_no", out_trade_no);
            returnMap.put("total_fee", total_fee);
            returnMap.put("code_url", map.get("code_url"));
            returnMap.put("result_code", map.get("result_code"));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnMap;
    }

    @Override
    public Map<String, String> queryPayStatus(String out_trade_no) {
        try {

            //组装发送的参数
            Map<String, String> param = new HashMap<>();
            //公众账号ID
            param.put("appid", appid);
            //商户号
            param.put("mch_id", partner);
            //随机字符串
            param.put("nonce_str", WXPayUtil.generateNonceStr());
            //签名；转换的时候自动生成
            //param.put("sign", "");
            //商户订单号
            param.put("out_trade_no", out_trade_no);

            //转换为xml
            String signedXml = WXPayUtil.generateSignedXml(param, partnerkey);

            System.out.println("发送到微信 查询状态 的内容为：" + signedXml);

            //发送统一下单的到微信支付系统
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/orderquery");
            httpClient.setHttps(true);
            httpClient.setXmlParam(signedXml);
            httpClient.post();

            //获取返回结果
            String content = httpClient.getContent();
            System.out.println("调用微信 查询状态 的返回内容为：" + content);

            return WXPayUtil.xmlToMap(content);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, String> closeOrder(String outTradeNo) {
        try {

            //组装发送的参数
            Map<String, String> param = new HashMap<>();
            //公众账号ID
            param.put("appid", appid);
            //商户号
            param.put("mch_id", partner);
            //随机字符串
            param.put("nonce_str", WXPayUtil.generateNonceStr());
            //签名；转换的时候自动生成
            //param.put("sign", "");
            //商户订单号
            param.put("out_trade_no", outTradeNo);

            //转换为xml
            String signedXml = WXPayUtil.generateSignedXml(param, partnerkey);

            System.out.println("发送到微信 关闭订单 的内容为：" + signedXml);

            //发送统一下单的到微信支付系统
            HttpClient httpClient = new HttpClient("https://api.mch.weixin.qq.com/pay/closeorder");
            httpClient.setHttps(true);
            httpClient.setXmlParam(signedXml);
            httpClient.post();

            //获取返回结果
            String content = httpClient.getContent();
            System.out.println("调用微信 关闭订单 的返回内容为：" + content);

            return WXPayUtil.xmlToMap(content);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
