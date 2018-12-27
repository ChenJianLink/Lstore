package cn.chenjianlink.lstore.search.service.impl;

import cn.chenjianlink.lstore.common.pojo.SearchItem;
import cn.chenjianlink.lstore.common.utils.LstoreResult;
import cn.chenjianlink.lstore.search.mapper.ItemMapper;
import cn.chenjianlink.lstore.search.service.SearchItemService;


import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 索引库维护service
 */
@Service
public class SearchItemServiceImpl implements SearchItemService {

    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private SolrClient solrClient;

    @Override
    public LstoreResult importAllItem() {
        try {
            //查询商品列表
            List<SearchItem> itemList = itemMapper.getItemList();
            //遍历商品列表
            for (SearchItem searchItem : itemList){
                //创建文档对象
                SolrInputDocument document = new SolrInputDocument();
                //向文档对象中添加域
                document.addField("id", searchItem.getId());
                document.addField("item_title", searchItem.getTitle());
                document.addField("item_sell_point", searchItem.getSell_point());
                document.addField("item_price", searchItem.getPrice());
                document.addField("item_image", searchItem.getImage());
                document.addField("item_category_name", searchItem.getCategory_name());
                //把文档对象写入索引库
                solrClient.add(document);
            }
            //提交
            solrClient.commit();
            //返回导入成功
            return LstoreResult.ok();
        }catch (Exception e){
            e.printStackTrace();
            return LstoreResult.build(500, "数据导入异常");
        }
    }
}
