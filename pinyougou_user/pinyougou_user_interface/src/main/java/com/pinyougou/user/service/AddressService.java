package com.pinyougou.user.service;
import com.pinyougou.common.PageResult;
import com.pinyougou.pojo.TbAddress;

import java.util.List;
/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface AddressService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbAddress> findAll(String name);
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum, int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(TbAddress address);
	
	
	/**
	 * 修改
	 */
	public void update(TbAddress address);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbAddress findOne(Long id);
	
	
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
	public PageResult findPage(TbAddress address, int pageNum, int pageSize);

	/**
	 * 根据当前登录者名查询收货人地址列表
	 * @param loginUser
	 * @return
	 */
	List<TbAddress> findAddressByLoginUser(String loginUser);

	/**
	 * 根据当前登录用户查询出该用户所拥有的地址
	 * @param name
	 * @return  List<TbAddress> 地址集合
	 */
	List<TbAddress> findUserAddressAll(String name);

    /**
     * 删除地址
     * @param id
     * @return
     */
    void deleteAddress(Long id);
}
