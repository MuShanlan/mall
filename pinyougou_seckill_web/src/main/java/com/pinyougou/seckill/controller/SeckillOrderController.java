package com.pinyougou.seckill.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.Result;
import com.pinyougou.pojo.TbSeckillGoods;
import com.pinyougou.seckill.service.SeckillGoodsService;
import com.pinyougou.seckill.service.SeckillOrderService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/seckillOrder")
@RestController
public class SeckillOrderController {

    @Reference
    private SeckillOrderService seckillOrdersService;

    /**
     *判断当前用户是否已经登录，如果没有登录则提示需要登录；如果已经登录则调用秒杀订
     * 单业务对象的方法生成具体的订单并返回 秒杀订单id；并输出该秒杀订单id，
     * 然后前端接收id并跳转到支付页面进行支付
     * @param seckillId 秒杀商品id
     * @return 操作结果
     */
    @RequestMapping("/submitOrder")
    public Result submitOrder(Long seckillId){
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            if(!"anonymousUser".equals(userId)){
                Long orderId = seckillOrdersService.submitOrder(seckillId, userId);

                if (orderId != null) {
                    return new Result(true, orderId.toString());
                }
            } else {
                return new Result(false, "请登录之后再进行秒杀！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, "秒杀失败！");
    }

}
