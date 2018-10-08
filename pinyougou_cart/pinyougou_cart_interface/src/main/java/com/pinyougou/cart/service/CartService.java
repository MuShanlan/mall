package com.pinyougou.cart.service;

import com.pinyougou.pojo.vo.Cart;

import java.util.List;

public interface CartService {

    /**
     * 根据商品SKUID和num数量添加到购物车集合中
     * @param cartList
     * @param itemId
     * @param num
     * @return
     */
    public List<Cart> addGoodsToCartList(List<Cart> cartList, Long itemId, Integer num);

    /**
     * 根据Key来获取购物车集合数据
     * @param key
     * @return
     */
    List<Cart> queryCartListByRedis(String key);

    /**
     * 根据Key把购物车集合数据放在redis中
     * @param key
     * @param cartList
     */
    void addCartListToRedis(String key, List<Cart> cartList);

    /**
     * 用户未登录到登录状态：把sessionId对应的购物车集合数据和用户名对应购物车集合数据整合
     * @param cartList
     * @param cartList_session
     * @return
     */
    List<Cart> mergeCartList(List<Cart> cartList, List<Cart> cartList_session);

    /**
     * 整合完后，把sessionID对应的购物车集合清空
     * @param key
     */
    void delCartListToRedis(String key);
}
