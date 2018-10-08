package com.pinyougou.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.PageResult;
import com.pinyougou.common.Result;
import com.pinyougou.manager.service.GoodsService;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.List;
/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {

	@Reference
	private GoodsService goodsService;


	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbGoods> findAll(){			
		return goodsService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return goodsService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody GoodsVo vo){
		try {

			//获取当前登录者（商家）名
            String loginName = SecurityContextHolder.getContext().getAuthentication().getName();
            vo.getGoods().setSellerId(loginName);

            goodsService.addVo(vo);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param goods
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody TbGoods goods){
		try {
			goodsService.update(goods);
			return new Result(true, "修改成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}	
	
	/**
	 * 获取实体
	 * @param id
	 * @return
	 */
	@RequestMapping("/findOne")
	public TbGoods findOne(Long id){
		return goodsService.findOne(id);		
	}
	
	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long [] ids){
		try {
			goodsService.delete(ids);
			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	
		/**
	 * 查询+分页
	 * @param
	 * @param page
	 * @param rows
	 * @return
	 */
	@RequestMapping("/search")
	public PageResult search(@RequestBody TbGoods goods, int page, int rows  ){

		//根据当前商家登录者来查询
        String loginName = SecurityContextHolder.getContext().getAuthentication().getName();
        goods.setSellerId(loginName);

        return goodsService.findPage(goods, page, rows);
	}

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private Destination topicItemDestination;

	@Autowired
	private Destination topicDeleteDestination;

	@RequestMapping("marketableStatus")
    public Result marketableStatus(final Long[] ids, String status){
        try {
            goodsService.marketableStatus(ids,status);

            //如果状态为1：商品上架，调用生成静态页面的服务
			//如果删除了或者下架了商品
			if("1".equals(status)){

				//商品上架了，我们应该把商品同步到索引库中，同步生成静态页面
				jmsTemplate.send(topicItemDestination, new MessageCreator() {
					@Override
					public Message createMessage(Session session) throws JMSException {
						return session.createObjectMessage(ids);
					}
				});

			    /*//1、生成静态页面
				for(Long goodsId : ids){
					pageService.creataHtml(goodsId);
				}
				//2、同步搜索库
                //3、同步购物车
                //4、同步个人中心
                //...*/
			}
			if("0".equals(status)){
				//发送消息
				jmsTemplate.send(topicDeleteDestination, new MessageCreator() {
					@Override
					public Message createMessage(Session session) throws JMSException {
						return session.createObjectMessage(ids);
					}
				});
			}
            return new Result(true,"操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"操作失败");
        }
    }




}
