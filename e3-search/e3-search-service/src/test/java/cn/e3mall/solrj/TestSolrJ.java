package cn.e3mall.solrj;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolrJ {
	
	@Test
	public void addDocument() throws Exception {
		//创建SolrServer对象,创建连接,参数:solr服务url
		SolrServer solrServer = new HttpSolrServer("http://192.168.0.103:8080/solr/collection1");
		//创建文档对象SolrInputDocument
		SolrInputDocument document = new SolrInputDocument();
		//向文档对象中添加域,必须包含id域,所以域的域名必须在schema.xml中定义
		document.addField("id", "doc01");
		document.addField("item_title", "测试商品01");
		document.addField("item_price", 10000);
		//把文档写入solr库
		solrServer.add(document);
		//提交
		solrServer.commit();
	}
	
	@Test
	public void deleteDocument() throws Exception {
		SolrServer solrServer = new HttpSolrServer("http://192.168.0.103:8080/solr/collection1");
		solrServer.deleteById("doc01");
		//solrServer.deleteByQuery("id:doc01");
		solrServer.commit();
	}
	
	@Test
	public void queryIndex() throws Exception {
		SolrServer solrServer = new HttpSolrServer("http://192.168.0.103:8080/solr/collection1");
		SolrQuery query = new SolrQuery();
		//query.setQuery("*:*");
		query.set("q", "*:*");
		QueryResponse queryResponse = solrServer.query(query);
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		System.out.println("总记录数:" + solrDocumentList.getNumFound());
		for (SolrDocument solrDocument : solrDocumentList) {
			System.out.println(solrDocument.get("id"));
			System.out.println(solrDocument.get("item_title"));
		}
	}
	
	@Test
	public void queryIndex2() throws Exception {
		SolrServer solrServer = new HttpSolrServer("http://192.168.0.103:8080/solr/collection1");
		SolrQuery query = new SolrQuery();
		query.setQuery("手机");
		query.setStart(0);
		query.setRows(20);
		query.set("df", "item_title");
		query.setHighlight(true);
		query.addHighlightField("item_title");
		query.setHighlightSimplePre("<em>");
		query.setHighlightSimplePost("</em>");
		
		QueryResponse queryResponse = solrServer.query(query);
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		System.out.println("总记录数:" + solrDocumentList.getNumFound());
		for (SolrDocument solrDocument : solrDocumentList) {
			System.out.println(solrDocument.get("id"));
			System.out.println(solrDocument.get("item_title"));
			//取高亮显示
			Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
			Map<String, List<String>> map = highlighting.get(solrDocument.get("id"));
			List<String> list = map.get("item_title");
			String title = "";
			if(null != list && list.size() > 0) {
				title = list.get(0);
			} else {
				title = (String) solrDocument.get("item_title");
			}
			System.out.println(title);
		}
	}

}
