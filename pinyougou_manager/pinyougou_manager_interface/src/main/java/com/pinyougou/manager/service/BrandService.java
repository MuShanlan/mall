package com.pinyougou.manager.service;

import com.pinyougou.common.PageResult;
import com.pinyougou.pojo.TbBrand;

import java.util.List;
import java.util.Map;

public interface BrandService {

    /**
     * 好习惯：在接口上加注释
     * 品牌列表
     * @return
     */
    public List<TbBrand> findAll();

    /**
     * 分页查询
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageResult findPage(Integer pageNum, Integer pageSize);

    /**
     * 新增品牌
     * @param brand
     * @return
     */
    void add(TbBrand brand);

    /**
     * 根据品牌ID查询品牌对象
     * @param id
     * @return
     */
    public TbBrand findOne(Long id);

    /**
     * 修改
     * @param brand
     */
    public void update(TbBrand brand);

    /**
     * 根据多个ID来删除品牌
     * @param ids
     */
    public void delete(Long[] ids);

    /**
     * 根据对象分页搜索
     * @param pageNum
     * @param pageSize
     * @param brand
     * @return
     */
    PageResult search(Integer pageNum, Integer pageSize, TbBrand brand);

    /**
     * 获取Map数据
     * @return
     */
    List<Map> findMap();
}
