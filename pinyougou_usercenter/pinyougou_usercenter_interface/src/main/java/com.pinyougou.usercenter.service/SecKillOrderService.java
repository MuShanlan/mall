package com.pinyougou.usercenter.service;

import com.pinyougou.pojo.vo.SeckillGoodsOrderVo;

import java.util.List;

public interface SecKillOrderService {
    /**
     * 查询所有秒杀订单
     * @param loginUser    当前登录用户
     * @return List<SeckillGoodsOrderVo> 封装了订单与商品
     */
    List<SeckillGoodsOrderVo> searchSecKillOrderList(String loginUser);

    /**
     * 取消订单
     * @param orderId
     */
    void deleteOrder(Long orderId);

    /**
     * 根据订单号查询出订单信息
     * @param orderId 订单号
     * @return SeckillGoodsOrderVo 封装了订单与商品信息
     */
    SeckillGoodsOrderVo findSeckillOrderDetail(Long orderId);
}
