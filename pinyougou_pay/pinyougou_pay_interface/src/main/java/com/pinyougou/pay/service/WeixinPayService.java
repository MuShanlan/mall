package com.pinyougou.pay.service;

import java.util.Map;

public interface WeixinPayService {
    /**
     * 根据商户订单号和支付总金额到微信支付系统生成订单并返回支付二维码等信息
     * @param out_trade_no 商户订单号
     * @param total_fee 总金额
     * @return result_code（SUCCESS/FAIL），code_url（二维码地址），out_trade_no订单号，total_fee总金额
     */
    Map<String, String> createNative(String out_trade_no, String total_fee);

    /**
     * 根据订单号查询该订单的支付状态
     * @param out_trade_no 订单号
     * @return 查询结果
     */
    Map<String, String> queryPayStatus(String out_trade_no);

    /**
     * 到微信支付系统关闭订单
     * @param outTradeNo 订单号
     * @return 关闭结果
     */
    Map<String, String> closeOrder(String outTradeNo);


}
