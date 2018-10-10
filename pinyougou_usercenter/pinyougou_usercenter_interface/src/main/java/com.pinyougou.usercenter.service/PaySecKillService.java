package com.pinyougou.usercenter.service;

import com.pinyougou.pojo.TbSeckillOrder;

public interface PaySecKillService {
    /**
     * 根据订单号查询 一条订单信息
     * @param outTradeNo 订单号
     * @return TbSeckillOrder 订单
     */
    TbSeckillOrder findSeckillOrderInMySQLByOutTradeNo(String outTradeNo);

    /**
     * 支付成功，更新订单状态
     * @param outTradeNo    订单号
     * @param transaction_id  交易流水号
     */
    void saveSeckillOrderToMySQL(String outTradeNo, String transaction_id);





}
