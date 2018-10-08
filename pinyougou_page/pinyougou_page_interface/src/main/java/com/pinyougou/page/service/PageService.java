package com.pinyougou.page.service;

public interface PageService {
    /**
     * 根据商品ID生成静态页面
     * @param goodsId
     */
    void creataHtml(Long goodsId);
}
