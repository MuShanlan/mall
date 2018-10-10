package com.pinyougou.usercenter.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.TbSeckillOrderMapper;
import com.pinyougou.pojo.TbSeckillOrder;
import com.pinyougou.usercenter.service.PaySecKillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Transactional
public class PaySecKillServiceImpl implements PaySecKillService {

    @Autowired
    private TbSeckillOrderMapper seckillOrderMapper;

    /**
     * 根据订单号查询 一条订单信息
     * @param outTradeNo 订单号
     * @return TbSeckillOrder 订单
     */
    @Override
    public TbSeckillOrder findSeckillOrderInMySQLByOutTradeNo(String outTradeNo) {
        return seckillOrderMapper.selectByPrimaryKey(Long.parseLong(outTradeNo));
    }

    /**
     * 支付成功，更新订单状态
     * @param outTradeNo    订单号
     * @param transaction_id  交易流水号
     */
    @Override
    public void saveSeckillOrderToMySQL(String outTradeNo, String transaction_id) {
        //1、获取订单
        TbSeckillOrder seckillOrder = seckillOrderMapper.selectByPrimaryKey(Long.parseLong(outTradeNo));;
        //已支付
        seckillOrder.setStatus("1");
        seckillOrder.setPayTime(new Date());
        seckillOrder.setTransactionId(transaction_id);

        //2、保存到数据库中
        seckillOrderMapper.updateByPrimaryKeySelective(seckillOrder);
    }
}
