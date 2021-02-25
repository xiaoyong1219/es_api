package com.xiaoyong.es_api.controller;

import com.xiaoyong.es_api.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Create By dongxiaoyong on /2021/2/24
 * description: 内容Controller
 *
 * @author dongxiaoyong
 */
@RestController
@RequestMapping("/content")
public class ContentController {

    @Autowired
    private ContentService contentService;

    /**
     * 根据传入的关键字，获取京东页面信息，批量保存到ES索引中
     *
     * @param keyword
     * @Author :dongxiaoyong
     * @Date : 2021/2/24 21:02
     * @return: java.lang.Boolean
     */


    @GetMapping("/parseJdToEs/{keyword}")
    public Boolean parseJdToEs(@PathVariable("keyword") String keyword) throws IOException {
        return contentService.parseJdToEs(keyword);
    }


    /**
     * 据关键字、分页查询ES文档并且高亮关键字返回
     *
     * @param keyword
     * @param pageNo
     * @param pageSize
     * @Author :dongxiaoyong
     * @Date : 2021/2/24 21:02
     * @return: java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     */

    @GetMapping("/search/{keyword}/{pageNo}/{pageSize}")
    public List<Map<String, Object>> search(@PathVariable("keyword") String keyword, @PathVariable("pageNo") int pageNo, @PathVariable("pageSize") int pageSize) throws IOException {
        return contentService.searchHighLight(keyword, pageNo, pageSize);
    }
}
