package com.pinyougou.manager.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.common.PageResult;
import com.pinyougou.manager.service.BrandService;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Service
public class BrandServiceImpl implements BrandService {

    @Autowired
    private TbBrandMapper brandMapper;

    @Override
    public PageResult search(Integer pageNum, Integer pageSize, TbBrand brand) {

        //设置分页属性
        PageHelper.startPage(pageNum,pageSize);

        Page<TbBrand> page = (Page<TbBrand>) brandMapper.search(brand);

        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public List<Map> findMap() {
        return brandMapper.findMap();
    }

    @Override
    public void add(TbBrand brand) {
        brandMapper.add(brand);
    }

    @Override
    public TbBrand findOne(Long id) {
        return brandMapper.findOne(id);
    }

    @Override
    public void delete(Long[] ids) {
        for(Long id : ids){
            brandMapper.delete(id);
        }
    }

    @Override
    public void update(TbBrand brand) {
        brandMapper.update(brand);
    }

    @Override
    public PageResult findPage(Integer pageNum, Integer pageSize) {
        //先设置分页的属性
        PageHelper.startPage(pageNum,pageSize);

        //搜索
        Page<TbBrand> page = (Page<TbBrand>)brandMapper.findAll();

        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public List<TbBrand> findAll() {
        return brandMapper.findAll();
    }
}
