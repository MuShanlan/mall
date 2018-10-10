package com.pinyougou.shop.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.Result;
import com.pinyougou.manager.service.SellerService;
import com.pinyougou.pojo.TbSeller;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/passWord")
public class passWordController {

    @Reference

    private SellerService sellerService;

    @RequestMapping("/update")
    public Result update(HttpSession session, String password, String newpassword) {

        //获取当前登录的商家名称
        String loginName = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            //根据登陆的商家id去数据库查询商家信息

            TbSeller seller = sellerService.findOne(loginName);
            //数据库原密码
            String oldpassword = seller.getPassword();

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            //判断后台查询的密码与用户输入的是否一致
            if (encoder.matches(password, oldpassword)) {

                //如果相等,把新密码加密后设置到用户的信息中
                String newpassword2 = encoder.encode(newpassword);

                seller.setPassword(newpassword2);

                sellerService.update(seller);
                session.invalidate();
                return new Result(true, "修改成功");

            } else {
                return new Result(false, "原密码错误,请重新输入");
            }

        } catch (Exception e) {

            e.printStackTrace();
            return new Result(false, "修改失败..");
        }


    }
}
