package cn.e3mall.solrj;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolrCloud {
	
	@Test
	public void testAddDocument() throws Exception {
		//创建集群连接,使用CloudSolrServer创建
		CloudSolrServer solrServer = new CloudSolrServer("192.168.0.103:2181,192.168.0.103:2182,192.168.0.103:2183");
		//zkHost:zookeeper地址列表
		//设置defaultCollection属性
		solrServer.setDefaultCollection("collection2");
		SolrInputDocument document = new SolrInputDocument();
		document.setField("id", "solrcloud01");
		document.setField("item_title", "测试商品01");
		document.setField("item_price", 456);
		solrServer.add(document);
		solrServer.commit();
	}
	
	@Test
	public void testQueryDocument() throws Exception {
		//创建集群连接,使用CloudSolrServer创建
		CloudSolrServer solrServer = new CloudSolrServer("192.168.0.103:2181,192.168.0.103:2182,192.168.0.103:2183");
		//zkHost:zookeeper地址列表
		//设置defaultCollection属性
		solrServer.setDefaultCollection("collection2");
		SolrQuery query = new SolrQuery();
		query.set("q", "*:*");
		QueryResponse queryResponse = solrServer.query(query);
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		System.out.println("总记录数:" + solrDocumentList.getNumFound());
		for (SolrDocument solrDocument : solrDocumentList) {
			System.out.println(solrDocument.get("id"));
			System.out.println(solrDocument.get("item_title"));
			System.out.println(solrDocument.get("item_price"));
		}
	}
	
}
