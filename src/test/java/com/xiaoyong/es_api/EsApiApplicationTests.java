package com.xiaoyong.es_api;

import com.alibaba.fastjson.JSON;
import com.xiaoyong.es_api.pojo.User;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
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

    /**
     * 测试创建文档
     *
     * @param
     * @Author :dongxiaoyong
     * @Date : 2021/2/20 9:38
     * @return: void
     */

    @Test
    void testAddDocument() throws IOException {
        //创建实体
        User user = new User("xiaoyong", 26);
        //创建请求
        IndexRequest indexRequest = new IndexRequest("xiaoyong_index");
        //规则 PUT /xiaoyong_index/_doc/1
        indexRequest.id("1");
        indexRequest.timeout(TimeValue.timeValueSeconds(1));
        indexRequest.timeout("1s");
        //将我们的数据放入请求 json
        indexRequest.source(JSON.toJSONString(user), XContentType.JSON);
        //客户端发送请求，获取响应的结果
        IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
        System.out.println(indexResponse.toString());
        System.out.println(indexResponse.status());
    }

    /**
     * 获取文档，判断是否存在 GET /xiaoyong_index/_doc/1
     *
     * @param
     * @Author :dongxiaoyong
     * @Date : 2021/2/20 9:56
     * @return: void
     */
    @Test
    void testIsExistDocument() throws IOException {
        GetRequest getRequest = new GetRequest("xiaoyong_index", "1");
        //不获取返回的_source的上下文了
        getRequest.fetchSourceContext(new FetchSourceContext(false));
        getRequest.storedFields("_none_");
        boolean exists = restHighLevelClient.exists(getRequest, RequestOptions.DEFAULT);
        System.out.println(exists);
    }

    /**
     * 获取文档的信息
     *
     * @param
     * @Author :dongxiaoyong
     * @Date : 2021/2/20 10:09
     * @return: void
     */

    @Test
    void testGetDocument() throws IOException {
        GetRequest getRequest = new GetRequest("xiaoyong_index", "1");
        GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        System.out.println(getResponse.getSourceAsString());//打印文档的内容
        System.out.println(getResponse);
    }

    /**
     * 更新文档的信息
     *
     * @param
     * @Author :dongxiaoyong
     * @Date : 2021/2/20 10:09
     * @return: void
     */

    @Test
    void testUpdateDocument() throws IOException {
        UpdateRequest updateRequest = new UpdateRequest("xiaoyong_index", "1");
        updateRequest.timeout("1s");
        User user = new User("董晓勇", 26);
        updateRequest.doc(JSON.toJSONString(user), XContentType.JSON);
        UpdateResponse updateResponse = restHighLevelClient.update(updateRequest, RequestOptions.DEFAULT);
        System.out.println(updateResponse.status());
    }

    /**
     * 测试删除文档
     *
     * @param
     * @Author :dongxiaoyong
     * @Date : 2021/2/20 10:18
     * @return: void
     */

    @Test
    void testDeleteDocument() throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest("xiaoyong_index","1");
        DeleteResponse deleteResponse = restHighLevelClient.delete(deleteRequest,RequestOptions.DEFAULT);
        System.out.println(deleteResponse.status());
        System.out.println(deleteResponse);
    }
}
