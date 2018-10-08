package com.pinyougou.manager.service;

import com.pinyougou.common.PageResult;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.vo.SpecificationVo;

import java.util.List;
import java.util.Map;

public interface SpecificationService {

    /**
     * 根据条件分页查询
     * @param pageNum
     * @param pageSize
     * @param specification
     * @return
     */
    PageResult search(Integer pageNum, Integer pageSize, TbSpecification specification);

    /**
     * 新增规格
     * @param vo
     */
    void add(SpecificationVo vo);

    /**
     * 根据规格ID查询规格信息
     * @param id
     * @return
     */
    public SpecificationVo findOne(Long id);

    /**
     * 修改
     * @param vo
     */
    void update(SpecificationVo vo);

    /**
     * 根据ID删除规格
     * @param ids
     */
    void delete(Long[] ids);

    /**
     * 获取规格Map数据
     * @return
     */
    List<Map> findMap();
}
