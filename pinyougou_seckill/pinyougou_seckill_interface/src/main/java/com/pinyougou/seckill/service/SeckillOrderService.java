package com.pinyougou.seckill.service;

import com.pinyougou.pojo.TbSeckillOrder;

public interface SeckillOrderService {

    /**
     * 生成秒杀订单
     * @param seckillId 秒杀商品id
     * @param userId 购买者
     * @return 秒杀订单id
     */
    Long submitOrder(Long seckillId, String userId) throws InterruptedException;

    /**
     * 根据订单号获取在redis中的订单
     * @param outTradeNo 订单号
     * @return 订单
     */
    TbSeckillOrder findSeckillOrderInRedisByOutTradeNo(String outTradeNo);

    /**
     * 将redis中的订单保存到mysql数据库中
     * @param out_trade_no 订单号
     * @param transaction_id 微信交易号
     */
    void saveSeckillOrderInRedisToDb(String out_trade_no, String transaction_id);

    /**
     * 将redis中的订单删除并将秒杀商品库存加1
     * @param outTradeNo  订单号
     */
    void deleteSeckillOrderInRedis(String outTradeNo) throws InterruptedException;
}
