package cn.chenjianlink.lstore.solrj;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolrJ {
    @Test
    public void addDocument() throws Exception {
        //创建一个SolrServer对象,创建一个链接
        SolrClient solrServer = new HttpSolrClient("http://192.168.58.4:8080/solr/collection1");
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id", "doc01");
        document.addField("item_title", "测试商品1");
        document.addField("item_price", "1000");
        solrServer.add(document);
        solrServer.commit();
    }

    @Test
    public void deleteDocument() throws Exception {
        SolrClient solrServer = new HttpSolrClient("http://192.168.58.4:8080/solr/collection1");
        solrServer.deleteById("doc01");
        solrServer.commit();
    }
}
