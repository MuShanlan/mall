package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import com.pinyougou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightPage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SolrTemplate solrTemplate;

    @Autowired
    private TbItemMapper itemMapper;

    @Override
    public Map search(Map map) {

        //1、创建搜索条件对象
        //SimpleQuery query = new SimpleQuery("*:*");
        //设置高亮的条件对象
        SimpleHighlightQuery query = new SimpleHighlightQuery();

        //2 设置参数
        //2.1 设置关键字
        /*String keywords = (String) map.get("keywords");
        if(!"".equals(keywords) && keywords != null){
            //在创建条对象的时候，直接指定什么域条件
            Criteria criteria = new Criteria("item_keywords");
            //如果is:那么是根据分词来搜索的，找的是分词后的数据
            //包含：搜索的时候，是不分词搜索，只要title或者域中包含就可以了；
            criteria.is(keywords);
            query.addCriteria(criteria);
        } else{
            Criteria criteria = new Criteria("item_keywords");
            criteria.expression("*:*");

        }*/
        String keywords = (String) map.get("keywords");
        Criteria criteria1 = null;
        // 判断参数是否为null
        if (keywords != null && !"".equals(keywords)) {
            criteria1 = new Criteria("item_keywords").is(keywords);
        } else {
            criteria1 = new Criteria().expression("*:*");
        }
        // 添加查询条件
        query.addCriteria(criteria1);

        //2.2设置高亮
        HighlightOptions options = new HighlightOptions();
        //设置高亮域
        options.addField("item_title");
        //设置高亮的前缀&后缀
        options.setSimplePrefix("<span style=\"color:red\">");
        options.setSimplePostfix("</span>");
        query.setHighlightOptions(options);

        //2.3 设置分类过滤条件的
        String category = (String) map.get("category");
        if(category != null && !"".equals(category)){
            //创建一个过滤条件对象
            SimpleFilterQuery filterQuery = new SimpleFilterQuery();
            Criteria criteria = new Criteria("item_category");
            criteria.is(category);
            filterQuery.addCriteria(criteria);
            query.addFilterQuery(filterQuery);
        }
        //2.4 设置品牌过滤条件
        String brand = (String) map.get("brand");
        if(brand != null && !"".equals(brand)){
            //创建一个过滤条件对象
            SimpleFilterQuery filterQuery = new SimpleFilterQuery();
            Criteria criteria = new Criteria("item_brand");
            criteria.is(brand);
            filterQuery.addCriteria(criteria);
            query.addFilterQuery(filterQuery);
        }
        //2.5 设置价格过滤条件
        String prices = (String) map.get("price");//0-500， 500-1000  3000-*
        if(prices != null && !"".equals(prices)){
            String[] price = prices.split("-");
            if(!"0".equals(price[0])){
                SimpleFilterQuery filterQuery = new SimpleFilterQuery();
                Criteria criteria = new Criteria("item_price");
                criteria.greaterThanEqual(price[0]);
                filterQuery.addCriteria(criteria);
                query.addFilterQuery(filterQuery);
            }
            if(!"*".equals(price[1])){
                SimpleFilterQuery filterQuery = new SimpleFilterQuery();
                Criteria criteria = new Criteria("item_price");
                criteria.lessThanEqual(price[1]);
                filterQuery.addCriteria(criteria);
                query.addFilterQuery(filterQuery);
            }
        }
        //2.6 设置规格过滤条件
        //spec:{"网络":"移动3G"}//根据动态域来查询
        Map<String,String> m = (Map<String,String>)map.get("spec");
        if(m != null){
            for(String key : m.keySet()){
                SimpleFilterQuery filterQuery = new SimpleFilterQuery();
                Criteria criteria = new Criteria("item_spec_"+key).is(m.get(key));
                filterQuery.addCriteria(criteria);
                query.addFilterQuery(filterQuery);
            }
        }

        //2.7 设置排序
        String sort = (String) map.get("sort");
        String sortField = (String) map.get("sortField");
        if(!"".equals(sortField) && sortField != null){
            if("ASC".equals(sort)){
                Sort sort1 =new Sort(Sort.Direction.ASC, "item_"+sortField);
                query.addSort(sort1);
            }
            if("DESC".equals(sort)){
                Sort sort1 =new Sort(Sort.Direction.DESC, "item_"+sortField);
                query.addSort(sort1);
            }
        }
        //注意：咱们在这里只写了价格，其实还有新品，销量等等，新品举例：1、加入业务域；2、把item中的time映射到时间业务域中；3、重新导入索引库


        //2.8最好是设置分页
        Integer page = (Integer)map.get("page");
        Integer pageSize = (Integer) map.get("pageSize");
        if(page == null){
            page = 1;
        }
        if(pageSize == null){
            pageSize = 20;
        }

        query.setOffset((page-1) * pageSize);
        query.setRows(pageSize);
        //3、执行查询
        //ScoredPage<TbItem> resultPage = solrTemplate.queryForPage(query, TbItem.class);
        HighlightPage<TbItem> resultPage = solrTemplate.queryForHighlightPage(query, TbItem.class);

        //获取高亮域的数据
        for(HighlightEntry<TbItem> h: resultPage.getHighlighted()){//循环高亮入口集合
            TbItem item = h.getEntity();//获取原实体类
            if(h.getHighlights().size()>0 && h.getHighlights().get(0).getSnipplets().size()>0){
                item.setTitle(h.getHighlights().get(0).getSnipplets().get(0));//设置高亮的结果
            }
        }

        //4、获取结果集
        List<TbItem> itemList = resultPage.getContent();

        //5、封装返回值
        Map resultMap = new HashMap();
        resultMap.put("rows",itemList);
        resultMap.put("total",resultPage.getTotalElements());
        resultMap.put("totalPages",resultPage.getTotalPages());

        return resultMap;
    }

    @Override
    public void importItem(Long goodsId) {

        //1、根据goodsId获取itemList
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsIdEqualTo(goodsId);
        criteria.andStatusEqualTo("1");
        List<TbItem> itemList = itemMapper.selectByExample(example);
        //2、itemList遍历了把item.spec设置到specMap属性中：因为这个属性已经和业务域映射了
        for(TbItem item : itemList){
            Map map = JSON.parseObject(item.getSpec(), Map.class);
            item.setSpecMap(map);
        }
        //3、使用solrTemplate把SKU集合到索引库
        solrTemplate.saveBeans(itemList);
        solrTemplate.commit();
    }

    @Override
    public void deleteItem(Long goodsId) {

        SimpleQuery query = new SimpleQuery("item_goodsid:" + goodsId);

        solrTemplate.delete(query);
        solrTemplate.commit();
    }
}
