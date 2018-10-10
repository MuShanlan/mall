package com.pinyougou.usercenter.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.Result;
import com.pinyougou.pojo.vo.SeckillGoodsOrderVo;
import com.pinyougou.usercenter.service.SecKillOrderService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/seckillOrder")
public class SecKillOrderController {

    @Reference
    private SecKillOrderService secKillOrderService;

    /**
     * 查询所有秒杀订单
     * @return List<SeckillGoodsOrderVo> 封装了订单与商品
     */
    @RequestMapping("/searchSecKillOrderList")
    public List<SeckillGoodsOrderVo> searchSecKillOrderList(){
        String loginUser = SecurityContextHolder.getContext().getAuthentication().getName();
//        String loginUser = "JTNine";
        return secKillOrderService.searchSecKillOrderList(loginUser);
    }

    /**
     * 取消订单
     * @param orderId
     * @return
     */
    @RequestMapping("/delete")
    public Result deleteOrder(Long orderId){
        try{
            secKillOrderService.deleteOrder(orderId);
            return new Result(true,"取消订单成功！");
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"取消订单失败！");
        }
    }

    /**
     * 查询当前登录用户
     * @return String 用户名
     */
    @RequestMapping("/checkLoginUser")
    public String checkLoginUser(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    /**
     * 根据订单号查询出订单信息
     * @param orderId 订单号
     * @return SeckillGoodsOrderVo 封装了订单与商品信息
     */
    @RequestMapping("/findSeckillOrderDetail")
    public SeckillGoodsOrderVo findSeckillOrderDetail(Long orderId){
        return secKillOrderService.findSeckillOrderDetail(orderId);
    }
}
