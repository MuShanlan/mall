package com.pinyougou.usercenter.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.TbSeckillGoodsMapper;
import com.pinyougou.mapper.TbSeckillOrderMapper;
import com.pinyougou.pojo.TbSeckillGoods;
import com.pinyougou.pojo.TbSeckillOrder;
import com.pinyougou.pojo.TbSeckillOrderExample;
import com.pinyougou.pojo.vo.SeckillGoodsOrderVo;
import com.pinyougou.usercenter.service.SecKillOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SecKillOrderServiceImpl implements SecKillOrderService {

    @Autowired
    private TbSeckillGoodsMapper seckillGoodsMapper;
    @Autowired
    private TbSeckillOrderMapper seckillOrderMapper;

    /**
     * 查询所有秒杀订单
     *
     * @param loginUser 当前登录用户
     * @return List<SeckillGoodsOrderVo> 封装了订单与商品
     */
    @Override
    public List<SeckillGoodsOrderVo> searchSecKillOrderList(String loginUser) {

        // 订单查询条件
        TbSeckillOrderExample orderExample = new TbSeckillOrderExample();
        // 用户名
        orderExample.createCriteria().andUserIdEqualTo(loginUser);
        // 未支付
        orderExample.setOrderByClause("status");
        // 查询
        List<TbSeckillOrder> orders = seckillOrderMapper.selectByExample(orderExample);

        List<SeckillGoodsOrderVo> seckillGoodsOrderVos = new ArrayList<>();

        for (TbSeckillOrder order : orders) {
            // 封装订单
            SeckillGoodsOrderVo goodsOrderVo = new SeckillGoodsOrderVo();
            goodsOrderVo.setSeckillOrder(order);

            // 封装商品集合
            // 根据订单中的商品 id 查询 商品
            TbSeckillGoods goods = seckillGoodsMapper.selectByPrimaryKey(order.getSeckillId());
            goodsOrderVo.setSeckillGoods(goods);

            // 添加进订单集合
            seckillGoodsOrderVos.add(goodsOrderVo);
        }

        return seckillGoodsOrderVos;
    }

    /**
     * 取消订单
     *
     * @param orderId
     */
    @Override
    public void deleteOrder(Long orderId) {
        seckillOrderMapper.deleteByPrimaryKey(orderId);
    }

    /**
     * 根据订单号查询出订单信息
     *
     * @param orderId 订单号
     * @return SeckillGoodsOrderVo 封装了订单与商品信息
     */
    @Override
    public SeckillGoodsOrderVo findSeckillOrderDetail(Long orderId) {
        SeckillGoodsOrderVo seckillGoodsOrderVo = new SeckillGoodsOrderVo();
        // 订单信息
        TbSeckillOrder seckillOrder = seckillOrderMapper.selectByPrimaryKey(orderId);
        seckillGoodsOrderVo.setSeckillOrder(seckillOrder);
        // 商品
        TbSeckillGoods goods = seckillGoodsMapper.selectByPrimaryKey(seckillOrder.getSeckillId());
        seckillGoodsOrderVo.setSeckillGoods(goods);

        return seckillGoodsOrderVo;

    }

}
