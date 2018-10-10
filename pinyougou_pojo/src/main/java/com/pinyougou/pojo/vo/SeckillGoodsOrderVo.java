package com.pinyougou.pojo.vo;

import com.pinyougou.pojo.TbSeckillGoods;
import com.pinyougou.pojo.TbSeckillOrder;

import java.io.Serializable;

/**
 * 封装秒杀商品与秒杀订单集合
 */
public class SeckillGoodsOrderVo implements Serializable {
    // 订单
    private TbSeckillOrder seckillOrder;

    // 商品
    private TbSeckillGoods seckillGoods;

    public TbSeckillOrder getSeckillOrder() {
        return seckillOrder;
    }

    public void setSeckillOrder(TbSeckillOrder seckillOrder) {
        this.seckillOrder = seckillOrder;
    }

    public TbSeckillGoods getSeckillGoods() {
        return seckillGoods;
    }

    public void setSeckillGoods(TbSeckillGoods seckillGoods) {
        this.seckillGoods = seckillGoods;
    }
}
