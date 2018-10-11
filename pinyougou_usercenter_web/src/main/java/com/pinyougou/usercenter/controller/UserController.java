package com.pinyougou.usercenter.controller;
import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.PageResult;
import com.pinyougou.common.PhoneFormatCheckUtils;
import com.pinyougou.common.Result;
import com.pinyougou.pojo.TbAddress;
import com.pinyougou.pojo.TbUser;
import com.pinyougou.user.service.AddressService;
import com.pinyougou.user.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * controller
 * @author Administrator
 *
 */
@RestController
@RequestMapping("/user")
public class UserController {

	@Reference(timeout = 30000)
	private UserService userService;
	@Reference(timeout = 30000)
	private AddressService addressService;


	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findAll")
	public List<TbUser> findAll(){
		return userService.findAll();
	}
	
	
	/**
	 * 返回全部列表
	 * @return
	 */
	@RequestMapping("/findPage")
	public PageResult  findPage(int page,int rows){			
		return userService.findPage(page, rows);
	}
	
	/**
	 * 增加
	 * @param user
	 * @return
	 */
	@RequestMapping("/add")
	public Result add(@RequestBody TbUser user, String smsCode){

		//验证手机号
		if(!PhoneFormatCheckUtils.isPhoneLegal(user.getPhone())){
			return new Result(false,"请输入正确的手机号");
		}
		//验证验证码
		if(!userService.checkSmsCode(user.getPhone(),smsCode)){
			return new Result(false,"验证码不正确");
		}
		try {
			userService.add(user);
			return new Result(true, "增加成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "增加失败");
		}
	}
	
	/**
	 * 修改
	 * @param user
	 * @return
	 */
	@RequestMapping("/update")
	public Result update(@RequestBody TbUser user){
		try {
			String name = SecurityContextHolder.getContext().getAuthentication().getName();
			TbUser tbUser = userService.findOne(name);
			user.setId(tbUser.getId());
			userService.update(user);
			return new Result(true, "修改成功，请重新登录！");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "修改失败");
		}
	}	
	
	/**
	 * 获取实体
	 * @return
	 */
	@RequestMapping("/findOne")
	public TbUser findOne(){
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		return userService.findOne(name);
	}

	/**
	 * 获取实体
	 * @return
	 */
	@RequestMapping("/findAddressOne")
	public TbAddress findAddressOne(Long id){
		return addressService.findOne(id);
	}

	/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
	@RequestMapping("/delete")
	public Result delete(Long [] ids){
		try {
			userService.delete(ids);
			return new Result(true, "删除成功"); 
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false, "删除失败");
		}
	}
	/**
	 * 删除地址
	 * @param id
	 * @return
	 */
	@RequestMapping("/deleteAddress")
	public Result deleteAddress(Long id){
		try {
			addressService.deleteAddress(id);
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
	public PageResult search(@RequestBody TbUser user, int page, int rows  ){
		return userService.findPage(user, page, rows);		
	}

	@RequestMapping("sendSms")
	public Result sendSms(String phone){
		try {
			userService.sendSms(phone);
			return new Result(true,"操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			return new Result(false,"操作失败");
		}
	}

	//获取当前登录者用户信息的
	@RequestMapping("/showName")
	public String showName(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return name;
    }

	@RequestMapping("/findUserAddressAll")
	public List<TbAddress> findUserAddressAll(){
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		return addressService.findUserAddressAll(name);
	}
	@RequestMapping("/addAddress")
	public Result addAddress(@RequestBody TbAddress address){
		String name = SecurityContextHolder.getContext().getAuthentication().getName();
		try{
			address.setUserId(name);
			addressService.add(address);
			return new Result(true,"添加地址成功！");
		}catch (Exception e){
			e.printStackTrace();
			return new Result(false,"添加地址失败！");
		}
	}


}
