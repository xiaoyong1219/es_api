package com.xiaoyong.es_api;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class EsApiApplicationTests {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 测试创建索引
     *
     * @param
     * @Author :dongxiaoyong
     * @Date : 2021/2/20 1:08
     * @return: void
     */

    @Test
    void testCreateIndex() throws IOException {
        //1、创建索引请求
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("xiaoyong_index");
        //2、客户端执行请求，请求后获得响应
        CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
        System.out.println(createIndexResponse);
    }


    /**
     * 判断索引是否存在
     *
     * @param
     * @Author :dongxiaoyong
     * @Date : 2021/2/20 1:10
     * @return: void
     */

    @Test
    void testExistIndex() throws IOException {
        //1、创建查找索引请求
        GetIndexRequest getIndexRequest = new GetIndexRequest("xiaoyong_index");
        //2、客户端执行请求，请求后获得响应
        boolean exists = restHighLevelClient.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        System.out.println(exists);
    }


    /**
     * 测试删除索引
     *
     * @param
     * @Author :dongxiaoyong
     * @Date : 2021/2/20 1:12
     * @return: void
     */

    @Test
    void testDeleteIndex() throws IOException {
        //1、创建删除索引请求
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("xiaoyong_index");
        //2、客户端执行请求，请求后获得响应
        AcknowledgedResponse acknowledgedResponse = restHighLevelClient.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
        System.out.println(acknowledgedResponse.isAcknowledged());
    }
}
