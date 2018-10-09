package com.pinyougou.seckill.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.common.IdWorker;
import com.pinyougou.common.PageResult;
import com.pinyougou.common.RedisLock;
import com.pinyougou.mapper.TbSeckillGoodsMapper;
import com.pinyougou.mapper.TbSeckillOrderMapper;
import com.pinyougou.pojo.TbSeckillGoods;
import com.pinyougou.pojo.TbSeckillOrder;
import com.pinyougou.pojo.TbSeckillOrderExample;
import com.pinyougou.seckill.service.SeckillOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Date;
import java.util.List;

@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {

    //秒杀订单在redis中的key的名称
    private static final String SECKILL_ORDERS = "SECKILL_ORDERS";
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private TbSeckillGoodsMapper seckillGoodsMapper;

    @Autowired
    private TbSeckillOrderMapper seckillOrderMapper;

    @Override
    public Long submitOrder(Long seckillId, String userId) throws InterruptedException {
        //获取分布式锁
        RedisLock redisLock = new RedisLock(redisTemplate);
        if(redisLock.lock(seckillId.toString())) {
            //1、获取redis中的秒杀商品
            TbSeckillGoods seckillGoods = (TbSeckillGoods) redisTemplate.boundHashOps(SeckillGoodsServiceImpl.SECKILL_GOODS).get(seckillId);

            //2、判断商品是否存在并库存大于0
            if (seckillGoods == null) {
                throw new RuntimeException("商品不存在");
            }
            if (seckillGoods.getStockCount() < 1) {
                throw new RuntimeException("商品已经秒杀完");
            }
            //3、将秒杀商品的库存减1；如果库存减之后为0的话，则需要将该秒杀商品同步更新到mysql中，
            // 并删除redis该秒杀商品；如果库存减了之后是大于0的则直接将最新的秒杀商品更新到redis中
            seckillGoods.setStockCount(seckillGoods.getStockCount() - 1);
            if (seckillGoods.getStockCount() == 0) {
                seckillGoodsMapper.updateByPrimaryKeySelective(seckillGoods);

                //删除redis中的秒杀商品
                redisTemplate.boundHashOps(SeckillGoodsServiceImpl.SECKILL_GOODS).delete(seckillId);
            } else {
                redisTemplate.boundHashOps(SeckillGoodsServiceImpl.SECKILL_GOODS).put(seckillId, seckillGoods);
            }
            //释放分布式锁
            redisLock.unlock(seckillId.toString());

            //4、生成秒杀订单并保存到redis
            TbSeckillOrder seckillOrder = new TbSeckillOrder();
            //未支付
            seckillOrder.setStatus("0");
            seckillOrder.setUserId(userId);
            seckillOrder.setSeckillId(seckillId);
            seckillOrder.setSellerId(seckillGoods.getSellerId());
            seckillOrder.setCreateTime(new Date());
            //秒杀价
            seckillOrder.setMoney(seckillGoods.getCostPrice());
            IdWorker idWorker = new IdWorker();
            seckillOrder.setId(idWorker.nextId());

            //将订单存入redis
            redisTemplate.boundHashOps(SECKILL_ORDERS).put(seckillOrder.getId().toString(), seckillOrder);

            //5、返回秒杀订单id
            return seckillOrder.getId();
        }


        return null;
    }

    @Override
    public TbSeckillOrder findSeckillOrderInRedisByOutTradeNo(String outTradeNo) {
        return (TbSeckillOrder) redisTemplate.boundHashOps(SECKILL_ORDERS).get(outTradeNo);
    }

    @Override
    public void saveSeckillOrderInRedisToDb(String out_trade_no, String transaction_id) {
        //1、获取订单
        TbSeckillOrder seckillOrder = findSeckillOrderInRedisByOutTradeNo(out_trade_no);
        //已支付
        seckillOrder.setStatus("1");
        seckillOrder.setPayTime(new Date());
        seckillOrder.setTransactionId(transaction_id);

        //2、保存到数据库中
        seckillOrderMapper.insertSelective(seckillOrder);

        //3、删除redis中的订单
        redisTemplate.boundHashOps(SECKILL_ORDERS).delete(out_trade_no);
    }

    @Override
    public void deleteSeckillOrderInRedis(String outTradeNo) throws InterruptedException {
        //1、查询订单获取秒杀商品
        TbSeckillOrder seckillOrder = findSeckillOrderInRedisByOutTradeNo(outTradeNo);

        //添加分布式锁
        RedisLock redisLock = new RedisLock(redisTemplate);
        if(redisLock.lock(seckillOrder.getSeckillId().toString())){
            TbSeckillGoods seckillGoods = (TbSeckillGoods) redisTemplate.boundHashOps(SeckillGoodsServiceImpl.SECKILL_GOODS).get(seckillOrder.getSeckillId());
            if (seckillGoods == null) {
                //从mysql中查询
                seckillGoods = seckillGoodsMapper.selectByPrimaryKey(seckillOrder.getSeckillId());
            }
            //2、将秒杀商品库存加1
            seckillGoods.setStockCount(seckillGoods.getStockCount()+1);

            //3、将秒杀商品存入redis
            redisTemplate.boundHashOps(SeckillGoodsServiceImpl.SECKILL_GOODS).put(seckillGoods.getId(), seckillGoods);

            //释放分布式锁
            redisLock.unlock(seckillOrder.getSeckillId().toString());

            //4、删除redis中的秒杀订单
            redisTemplate.boundHashOps(SECKILL_ORDERS).delete(outTradeNo);
        }

    }

    /**
     * 查询全部
     */
    @Override
    public List<TbSeckillOrder> findAll() {
        return seckillOrderMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbSeckillOrder> page=   (Page<TbSeckillOrder>) seckillOrderMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(TbSeckillOrder seckillOrder) {
        seckillOrderMapper.insert(seckillOrder);
    }


    /**
     * 修改
     */
    @Override
    public void update(TbSeckillOrder seckillOrder){
        seckillOrderMapper.updateByPrimaryKey(seckillOrder);
    }

    /**
     * 根据ID获取实体
     * @param id
     * @return
     */
    @Override
    public TbSeckillOrder findOne(Long id){
        return seckillOrderMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        for(Long id:ids){
            seckillOrderMapper.deleteByPrimaryKey(id);
        }
    }


    @Override
    public PageResult findPage(TbSeckillOrder seckillOrder, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbSeckillOrderExample example=new TbSeckillOrderExample();
        TbSeckillOrderExample.Criteria criteria = example.createCriteria();

        if(seckillOrder!=null){
            if(seckillOrder.getUserId()!=null && seckillOrder.getUserId().length()>0){
                criteria.andUserIdLike("%"+seckillOrder.getUserId()+"%");
            }
            if(seckillOrder.getSellerId()!=null && seckillOrder.getSellerId().length()>0){
                criteria.andSellerIdLike("%"+seckillOrder.getSellerId()+"%");
            }
            if(seckillOrder.getStatus()!=null && seckillOrder.getStatus().length()>0){
                criteria.andStatusLike("%"+seckillOrder.getStatus()+"%");
            }
            if(seckillOrder.getReceiverAddress()!=null && seckillOrder.getReceiverAddress().length()>0){
                criteria.andReceiverAddressLike("%"+seckillOrder.getReceiverAddress()+"%");
            }
            if(seckillOrder.getReceiverMobile()!=null && seckillOrder.getReceiverMobile().length()>0){
                criteria.andReceiverMobileLike("%"+seckillOrder.getReceiverMobile()+"%");
            }
            if(seckillOrder.getReceiver()!=null && seckillOrder.getReceiver().length()>0){
                criteria.andReceiverLike("%"+seckillOrder.getReceiver()+"%");
            }
            if(seckillOrder.getTransactionId()!=null && seckillOrder.getTransactionId().length()>0){
                criteria.andTransactionIdLike("%"+seckillOrder.getTransactionId()+"%");
            }

        }

        Page<TbSeckillOrder> page= (Page<TbSeckillOrder>)seckillOrderMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

}
