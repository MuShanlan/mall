package com.pinyougou.seckill.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pojo.TbSeckillGoods;
import com.pinyougou.seckill.service.SeckillGoodsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/seckillGoods")
@RestController
public class SeckillGoodsController {

    @Reference
    private SeckillGoodsService seckillGoodsService;

    /**
     * 根据秒杀商品id到redis查询该秒杀商品并返回
     * @return 秒杀商品
     */
    @RequestMapping("/findOne")
    public TbSeckillGoods findOne(Long id){
        return seckillGoodsService.findOneInRedisById(id);
    }

    /**
     * 查询当前秒杀商品列表
     * @return 秒杀商品列表
     */
    @RequestMapping("/findList")
    public List<TbSeckillGoods> findList(){
        return seckillGoodsService.findList();
    }
}
