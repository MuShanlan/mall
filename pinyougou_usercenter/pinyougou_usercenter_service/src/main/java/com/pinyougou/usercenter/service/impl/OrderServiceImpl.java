package com.pinyougou.usercenter.service.impl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.mapper.TbOrderItemMapper;
import com.pinyougou.mapper.TbSellerMapper;
import com.pinyougou.pojo.*;
import com.pinyougou.usercenter.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbOrderMapper;
import com.pinyougou.pojo.TbOrderExample.Criteria;

import com.pinyougou.common.PageResult;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service(timeout = 10000)
@Transactional
public class OrderServiceImpl implements OrderService {

	@Autowired
	private TbOrderMapper orderMapper;
	@Autowired
	private TbOrderItemMapper orderItemMapper;
	@Autowired
	private TbSellerMapper sellerMapper;
	@Autowired
	private TbItemMapper itemMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbOrder> findAll() {
		return orderMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbOrder> page=   (Page<TbOrder>) orderMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(TbOrder order) {
		orderMapper.insert(order);		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(TbOrder order){
		orderMapper.updateByPrimaryKey(order);
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public TbOrder findOne(Long id){
		return orderMapper.selectByPrimaryKey(id);
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			orderMapper.deleteByPrimaryKey(id);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbOrder order, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbOrderExample example=new TbOrderExample();
		Criteria criteria = example.createCriteria();
		
		if(order!=null){			
						if(order.getPaymentType()!=null && order.getPaymentType().length()>0){
				criteria.andPaymentTypeLike("%"+order.getPaymentType()+"%");
			}
			if(order.getPostFee()!=null && order.getPostFee().length()>0){
				criteria.andPostFeeLike("%"+order.getPostFee()+"%");
			}
			if(order.getStatus()!=null && order.getStatus().length()>0){
				criteria.andStatusLike("%"+order.getStatus()+"%");
			}
			if(order.getShippingName()!=null && order.getShippingName().length()>0){
				criteria.andShippingNameLike("%"+order.getShippingName()+"%");
			}
			if(order.getShippingCode()!=null && order.getShippingCode().length()>0){
				criteria.andShippingCodeLike("%"+order.getShippingCode()+"%");
			}
			if(order.getUserId()!=null && order.getUserId().length()>0){
				criteria.andUserIdLike("%"+order.getUserId()+"%");
			}
			if(order.getBuyerMessage()!=null && order.getBuyerMessage().length()>0){
				criteria.andBuyerMessageLike("%"+order.getBuyerMessage()+"%");
			}
			if(order.getBuyerNick()!=null && order.getBuyerNick().length()>0){
				criteria.andBuyerNickLike("%"+order.getBuyerNick()+"%");
			}
			if(order.getBuyerRate()!=null && order.getBuyerRate().length()>0){
				criteria.andBuyerRateLike("%"+order.getBuyerRate()+"%");
			}
			if(order.getReceiverAreaName()!=null && order.getReceiverAreaName().length()>0){
				criteria.andReceiverAreaNameLike("%"+order.getReceiverAreaName()+"%");
			}
			if(order.getReceiverMobile()!=null && order.getReceiverMobile().length()>0){
				criteria.andReceiverMobileLike("%"+order.getReceiverMobile()+"%");
			}
			if(order.getReceiverZipCode()!=null && order.getReceiverZipCode().length()>0){
				criteria.andReceiverZipCodeLike("%"+order.getReceiverZipCode()+"%");
			}
			if(order.getReceiver()!=null && order.getReceiver().length()>0){
				criteria.andReceiverLike("%"+order.getReceiver()+"%");
			}
			if(order.getInvoiceType()!=null && order.getInvoiceType().length()>0){
				criteria.andInvoiceTypeLike("%"+order.getInvoiceType()+"%");
			}
			if(order.getSourceType()!=null && order.getSourceType().length()>0){
				criteria.andSourceTypeLike("%"+order.getSourceType()+"%");
			}
			if(order.getSellerId()!=null && order.getSellerId().length()>0){
				criteria.andSellerIdLike("%"+order.getSellerId()+"%");
			}
	
		}
		
		Page<TbOrder> page= (Page<TbOrder>)orderMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Override
	public Map findPageByOrderStatus(String loginName, Map searchMap) {
		Page page = PageHelper.startPage((Integer)searchMap.get("page"), (Integer)searchMap.get("pageSize"));
		TbOrderExample orderExample = new TbOrderExample();
		Criteria criteria = orderExample.createCriteria().andUserIdEqualTo(loginName);
		String status = (String) searchMap.get("status");
		if (status!=null && !"".equals(status)){
			criteria.andStatusEqualTo(status);
		}
		orderExample.setOrderByClause("create_time desc");
		List<TbOrder> orderList = orderMapper.selectByExample(orderExample);
		List<Map> orders = new ArrayList<>();
		for (TbOrder tbOrder : orderList) {
			Map<String,Object> orderInfo = new HashMap<>();
			orderInfo.put("order",tbOrder);
			TbOrderItemExample orderItemExample = new TbOrderItemExample();
			orderItemExample.createCriteria().andOrderIdEqualTo(tbOrder.getOrderId());
			List<TbOrderItem> orderItemList = orderItemMapper.selectByExample(orderItemExample);
			List<Map> orderItems = new ArrayList<>();
			for (TbOrderItem tbOrderItem : orderItemList) {
				Map<String,Object> orderItemInfo = new HashMap<>();
				orderItemInfo.put("orderItem",tbOrderItem);
				TbItem tbItem = itemMapper.selectByPrimaryKey(tbOrderItem.getItemId());
				if(tbItem.getSpec()!=null && !"".equals(tbItem.getSpec())){
					Map specMap =(Map) JSON.parse(tbItem.getSpec());
					StringBuilder sb = new StringBuilder();
					for (Object o : specMap.keySet()) {
						sb.append(o.toString()+":"+specMap.get(o).toString()+" ");
					}
					tbItem.setSpec(sb.toString());
				}
				orderItemInfo.put("item",tbItem);
				orderItems.add(orderItemInfo);
			}
			orderInfo.put("orderItems",orderItems);
			TbSeller tbSeller = sellerMapper.selectByPrimaryKey(tbOrder.getSellerId());
			orderInfo.put("seller",tbSeller);
			orders.add(orderInfo);
		}

		Map<String,Object> resultMap = new HashMap<>();
		resultMap.put("rows",orders);
		resultMap.put("total",page.getTotal());
		resultMap.put("totalPages",page.getPages());
		return resultMap;
	}
}
