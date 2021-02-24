package com.xiaoyong.es_api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Create By dongxiaoyong on /2021/2/24
 * description: 主页controller
 *
 * @author dongxiaoyong
 */
@Controller
public class IndexController {

    /**
     * 首页
     *
     * @param
     * @Author :dongxiaoyong
     * @Date : 2021/2/24 11:13
     * @return: java.lang.String
     */

    @GetMapping({"/", "/index"})
    public String indexController() {
        return "index";
    }
}
