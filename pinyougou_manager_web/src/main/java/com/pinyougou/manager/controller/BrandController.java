package com.pinyougou.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.common.PageResult;
import com.pinyougou.common.Result;
import com.pinyougou.manager.service.BrandService;
import com.pinyougou.pojo.TbBrand;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("brand")
public class BrandController {

    @Reference
    private BrandService brandService;

    @RequestMapping("findAll")
    public List<TbBrand> findAll(){
        List<TbBrand> brandList = brandService.findAll();
        return brandList;
    }

    @RequestMapping("findPage")
    public PageResult findPage(Integer pageNum, Integer pageSize){
        return brandService.findPage(pageNum,pageSize);
    }

    @RequestMapping("add")
    public Result add(@RequestBody TbBrand brand){
        try {
            this.brandService.add(brand);
            return new Result(true,"操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"操作失败");
        }
    }

    @RequestMapping("findOne")
    public TbBrand findOne(Long id){
        return brandService.findOne(id);
    }

    @RequestMapping("update")
    public Result update(@RequestBody TbBrand brand){
        try {
            this.brandService.update(brand);
            return new Result(true,"操作成功" );
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"操作失败");
        }
    }

    @RequestMapping("delete")
    public Result delete(Long[] ids){
        try {
            this.brandService.delete(ids);
            return new Result(true,"操作成功" );
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"操作失败");
        }
    }

    @RequestMapping("search")
    public PageResult search(Integer pageNum, Integer pageSize, @RequestBody TbBrand brand){
        return this.brandService.search(pageNum,pageSize,brand);
    }

    @RequestMapping("findMap")
    public List<Map> findMap(){
        return brandService.findMap();
    }
}
