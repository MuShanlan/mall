package com.pinyougou.usercenter.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.Result;
import com.pinyougou.pay.service.WeixinPayService;
import com.pinyougou.pojo.TbSeckillOrder;
import com.pinyougou.usercenter.service.PaySecKillService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping("/pay")
@RestController
public class PayController {

    @Reference
    private WeixinPayService weixinPayService;

    @Reference
    private PaySecKillService paySecKillService;

    /**
     * 到支付系统获取支付二维码等信息
     * @return
     */
    @RequestMapping(value = "/createNative", method = RequestMethod.GET)
    public Map<String, String> createNative(String outTradeNo){
        //1、获取订单号
        TbSeckillOrder seckillOrder = paySecKillService.findSeckillOrderInMySQLByOutTradeNo(outTradeNo);

        String out_trade_no = outTradeNo;

        //2、总金额；单位为分
        String total_fee= (long)(seckillOrder.getMoney().doubleValue() *100) + "";

        //3、调用支付系统方法获取支付二维码地址
        return weixinPayService.createNative(out_trade_no, total_fee);
    }

    /**
     * 根据订单号查询该订单的支付状态
     * @param outTradeNo 订单号
     * @return 查询结果
     */
    @RequestMapping("/queryPayStatus")
    public Result queryPayStatus(String outTradeNo){

        Result result = new Result(false, "查询支付状态失败");

        //支付二维码超时时间；1分钟
        int count = 0;
        while (true){

            //到支付系统查询支付状态
            Map<String, String> resultMap = weixinPayService.queryPayStatus(outTradeNo);
            if (resultMap == null) {
                break;
            }

            if ("SUCCESS".equals(resultMap.get("trade_state"))) {
                //支付成功
                result = new Result(true, "支付成功");

                //更新订单状态；参数2:微信订单号
                paySecKillService.saveSeckillOrderToMySQL(outTradeNo, resultMap.get("transaction_id"));

                break;
            }

            count++;
            if (count > 20) {
                result = new Result(false, "支付超时");

                //到微信支付系统关闭订单
                resultMap = weixinPayService.closeOrder(outTradeNo);
                //如果在关闭订单的过程中被人支付了则也需要将订单保存到数据库
                if(resultMap != null && "ORDERPAID".equals(resultMap.get("err_code"))){
                    //更新订单状态；参数2:微信订单号
                    paySecKillService.saveSeckillOrderToMySQL(outTradeNo, resultMap.get("transaction_id"));

                    result = new Result(true, "支付成功");
                    break;
                }
                break;
            }

            try {
                //每隔3秒查询
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }

        return result;
    }
}
