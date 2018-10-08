package com.pinyougou.seckill.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.TbSeckillGoodsMapper;
import com.pinyougou.pojo.TbSeckillGoods;
import com.pinyougou.pojo.TbSeckillGoodsExample;
import com.pinyougou.seckill.service.SeckillGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.List;

@Service
public class SeckillGoodsServiceImpl implements SeckillGoodsService {

    //存入到redis中的秒杀商品对应的key的名称
    public static final String SECKILL_GOODS = "SECKILL_GOODS";

    @Autowired
    private TbSeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据秒杀商品审核通过，库存大于0，开始时间小于等于当前时间，结束时间大于当前时间并且根据开始时间降序排序
     * <p>
     * select * from tb_seckill_goods where status=? and start_time >= ?
     * and end_time < ?  and stock_count>0 order by start_time
     *
     * @return
     */
    @Override
    public List<TbSeckillGoods> findList() {
        List<TbSeckillGoods> seckillGoodsList = null;

        //从缓存中查询数据；如果查询到则直接返回
        seckillGoodsList = redisTemplate.boundHashOps(SECKILL_GOODS).values();

        if (seckillGoodsList == null || seckillGoodsList.size() == 0) {

            //创建查询对象
            TbSeckillGoodsExample example = new TbSeckillGoodsExample();

            //创建查询条件对象
            TbSeckillGoodsExample.Criteria criteria = example.createCriteria();

            criteria.andStatusEqualTo("1");
            criteria.andStockCountGreaterThan(0);
            criteria.andStartTimeLessThanOrEqualTo(new Date());
            criteria.andEndTimeGreaterThan(new Date());

            //排序
            example.setOrderByClause("start_time");

            //查询
            seckillGoodsList = seckillGoodsMapper.selectByExample(example);

            //将秒杀商品存入到redis中
            for (TbSeckillGoods seckillGoods : seckillGoodsList) {
                redisTemplate.boundHashOps(SECKILL_GOODS).put(seckillGoods.getId(), seckillGoods);
            }

        } else {
            System.out.println("从缓存中读取了秒杀商品列表...");
        }
        return seckillGoodsList;
    }

    @Override
    public TbSeckillGoods findOneInRedisById(Long id) {
        return (TbSeckillGoods) redisTemplate.boundHashOps(SECKILL_GOODS).get(id);
    }
}
