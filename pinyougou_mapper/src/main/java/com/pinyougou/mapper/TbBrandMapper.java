package com.pinyougou.mapper;

import com.pinyougou.pojo.TbBrand;

import java.util.List;
import java.util.Map;

public interface TbBrandMapper {

    public List<TbBrand> findAll();

    void add(TbBrand brand);

    void update(TbBrand brand);

    TbBrand findOne(Long id);

    void delete(Long id);

    List<TbBrand> search(TbBrand brand);

    List<Map> findMap();
}
