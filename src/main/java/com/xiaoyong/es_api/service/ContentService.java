package com.xiaoyong.es_api.service;

import com.alibaba.fastjson.JSON;
import com.xiaoyong.es_api.pojo.Content;
import com.xiaoyong.es_api.util.ESConstant;
import com.xiaoyong.es_api.util.HtmlParseUtil;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Create By dongxiaoyong on /2021/2/24
 * description: 内容Service
 *
 * @author dongxiaoyong
 */
@Service
public class ContentService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 根据传入的关键字，获取京东页面信息，批量保存到ES索引中
     *
     * @param keyword
     * @Author :dongxiaoyong
     * @Date : 2021/2/24 20:41
     * @return: java.lang.Boolean
     */

    public Boolean parseJdToEs(String keyword) throws IOException {
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("2m");

        ArrayList<Content> contents = HtmlParseUtil.parseJdHtml(keyword);
        if (contents != null && contents.size() > 0) {
            for (int i = 0; i < contents.size(); i++) {
                bulkRequest.add(
                        new IndexRequest(ESConstant.ES_JD_GOODS_INDEX)
                                .id(String.valueOf(i + 1))
                                .source(JSON.toJSONString(contents.get(i)), XContentType.JSON));
            }
            BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);
            return !bulkResponse.hasFailures();
        }
        return false;
    }


    /**
     * 根据关键字、分页查询ES文档
     *
     * @param keyword
     * @param pageNo
     * @param pageSize
     * @Author :dongxiaoyong
     * @Date : 2021/2/24 21:01
     * @return: java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     */

    public ArrayList<Map<String, Object>> search(String keyword, int pageNo, int pageSize) throws IOException {
        if (pageNo < 0) {
            pageNo = 0;
        }
        if (pageSize < 1) {
            pageSize = 10;
        }

        //条件搜索
        SearchRequest searchRequest = new SearchRequest(ESConstant.ES_JD_GOODS_INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //分页
        searchSourceBuilder.from(pageNo);
        searchSourceBuilder.size(pageSize);

        //精准匹配查询
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("title", keyword);
        searchSourceBuilder.query(termQueryBuilder);
        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        //执行搜索
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
        //解析结果
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            list.add(hit.getSourceAsMap());
        }
        //es中没有匹配的数据，再去访问京东界面，将信息保存到es，再进行查询解析
        if (list.size() < 1) {
            if (parseJdToEs(keyword)) {
                list = search(keyword, pageNo, pageSize);
            }
        }
        return list;
    }

    /**
     * 根据关键字、分页查询ES文档并且高亮关键字返回
     *
     * @param keyword
     * @param pageNo
     * @param pageSize
     * @Author :dongxiaoyong
     * @Date : 2021/2/25 15:12
     * @return: java.util.ArrayList<java.util.Map < java.lang.String, java.lang.Object>>
     */

    public ArrayList<Map<String, Object>> searchHighLight(String keyword, int pageNo, int pageSize) throws IOException {
        if (pageNo < 0) {
            pageNo = 0;
        }
        if (pageSize < 1) {
            pageSize = 10;
        }
        SearchRequest searchRequest = new SearchRequest(ESConstant.ES_JD_GOODS_INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //分页
        searchSourceBuilder.from(pageNo);
        searchSourceBuilder.size(pageSize);

        //精准匹配查询
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("title", keyword);
        searchSourceBuilder.query(termQueryBuilder);
        searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        //高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title");
        highlightBuilder.requireFieldMatch(false);//关闭多个高亮显示
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.postTags("</span>");
        searchSourceBuilder.highlighter(highlightBuilder);

        //执行搜索
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        //解析结果
        ArrayList<Map<String, Object>> list = new ArrayList<>();
        for (SearchHit hit : searchResponse.getHits().getHits()) {
            //解析高亮的字段，将原来的字段替换为我们高亮的字段即可
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();//原来的结果
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();//高亮的字段结果
            HighlightField highlightField = highlightFields.get("title");
            if (highlightField != null) {
                Text[] fragments = highlightField.getFragments();
                String highlightTitle = "";
                for (Text text : fragments) {
                    highlightTitle = text.string();
                }
                sourceAsMap.put("title", highlightTitle);//高亮字段替换原来的内容
            }
            list.add(sourceAsMap);
        }
        //es中没有匹配的数据，再去访问京东界面，将信息保存到es，再进行查询解析
        if (list.size() < 1) {
            if (parseJdToEs(keyword)) {
                list = search(keyword, pageNo, pageSize);
            }
        }
        return list;
    }


}