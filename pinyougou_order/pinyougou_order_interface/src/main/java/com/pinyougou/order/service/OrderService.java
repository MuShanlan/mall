package com.pinyougou.order.service;
import com.pinyougou.common.PageResult;
import com.pinyougou.pojo.TbOrder;
import com.pinyougou.pojo.TbPayLog;

import java.util.List;
/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface OrderService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbOrder> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum, int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(TbOrder order);
	
	
	/**
	 * 修改
	 */
	public void update(TbOrder order);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbOrder findOne(Long id);
	
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void delete(Long[] ids);

	/**
	 * 分页
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage(TbOrder order, int pageNum, int pageSize);

    /**
     * 根据用户id到redis中获取支付日志
     * @param userId 用户id
     * @return 支付日志
     */
    TbPayLog findPayLogInRedis(String userId);

    /**
     * 根据订单号更新支付日志、所有订单的支付状态为已支付
     * @param out_trade_no 订单号
     * @param transaction_id 微信订单号
     */
    void updateOrderStatus(String out_trade_no, String transaction_id);
}
