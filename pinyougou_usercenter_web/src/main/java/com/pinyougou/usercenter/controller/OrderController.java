package com.pinyougou.usercenter.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.PageResult;
import com.pinyougou.common.Result;
import com.pinyougou.pojo.TbOrder;
import com.pinyougou.usercenter.service.OrderService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/order")
public class OrderController {

	@Reference(timeout = 10000)
	private OrderService orderService;
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbOrder> findAll(){			
		return orderService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return orderService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param order
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody TbOrder order){
		try {
			orderService.add(order);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param order
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody TbOrder order){
		try {
			orderService.update(order);
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
	public TbOrder findOne(Long id){
		return orderService.findOne(id);		
	}
	
	/**
	 * 订单取消
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long ids){
		try {
			orderService.delete(ids);
			return new Result(true, "关闭成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "关闭失败");
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
	public PageResult search(@RequestBody TbOrder order, int page, int rows  ){
		return orderService.findPage(order, page, rows);		
	}

	@RequestMapping("/getLoginName")
	public String getLoginName(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return name;
	}

	/**
	 * 按订单状态分页查询订单列表
	 * @param searchMap
	 * @return
	 */
	@RequestMapping("/findPageByOrderStatus")
	public Map findPageByOrderStatus(@RequestBody Map searchMap){
        String loginName = getLoginName();
        return orderService.findPageByOrderStatus(loginName,searchMap);
	}
	
}
