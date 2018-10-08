package com.pinyougou.page.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.TbGoodsDescMapper;
import com.pinyougou.mapper.TbGoodsMapper;
import com.pinyougou.mapper.TbItemCatMapper;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.page.service.PageService;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbGoodsDesc;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PageServiceImpl implements PageService {

    @Autowired
    private FreeMarkerConfig freemarkerConfig;

    @Autowired
    private TbGoodsMapper goodsMapper;

    @Autowired
    private TbGoodsDescMapper goodsDescMapper;

    @Autowired
    private TbItemCatMapper itemCatMapper;

    @Autowired
    private TbItemMapper itemMapper;

    //实现生成静态页面的
    @Override
    public void creataHtml(Long goodsId) {

        try {
            //1、只根据freemarkerConfig获取一个对象就可以了
            Configuration configuration = freemarkerConfig.getConfiguration();
            //2、获取模板
            Template template = configuration.getTemplate("item.ftl");
            //3、根据商品Id获取商品的信息
            TbGoods goods = goodsMapper.selectByPrimaryKey(goodsId);
            //根据商品的ID查询商品详细信息
            TbGoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);

            //根据goods对象中的categoryID查询分类的对象name
            String itemCat1Name = itemCatMapper.selectByPrimaryKey(goods.getCategory1Id()).getName();
            String itemCat2Name = itemCatMapper.selectByPrimaryKey(goods.getCategory2Id()).getName();
            String itemCat3Name = itemCatMapper.selectByPrimaryKey(goods.getCategory3Id()).getName();

            //根据商品ID查询SKU列表
            TbItemExample example = new TbItemExample();
            TbItemExample.Criteria criteria = example.createCriteria();
            criteria.andGoodsIdEqualTo(goodsId);
            criteria.andStatusEqualTo("1");
            example.setOrderByClause("is_default desc");
            List<TbItem> itemList = itemMapper.selectByExample(example);

            //4、用Map对象封装商品的信息给静态页面传递
            Map map = new HashMap<>();
            map.put("goods",goods);
            map.put("goodsDesc",goodsDesc);
            map.put("itemCat1Name",itemCat1Name);
            map.put("itemCat2Name",itemCat2Name);
            map.put("itemCat3Name",itemCat3Name);
            map.put("itemList",itemList);
            //5、指定输出的路径
            FileWriter fileWriter = new FileWriter(new File("D:\\java_87\\item\\" + goodsId + ".html"));
            //6、开始输出
            template.process(map,fileWriter);
            //7、关闭流
            fileWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
