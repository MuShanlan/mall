package com.pinyougou.seckill.service;

import com.pinyougou.pojo.TbSeckillGoods;

import java.util.List;

public interface SeckillGoodsService {
    /**
     * 查询当前秒杀商品列表
     * @return 秒杀商品列表
     */
    List<TbSeckillGoods> findList();

    /**
     * 根据秒杀商品id到redis查询该秒杀商品并返回
     * @return 秒杀商品
     */
    TbSeckillGoods findOneInRedisById(Long id);
}
