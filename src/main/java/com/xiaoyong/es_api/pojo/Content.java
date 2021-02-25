package com.xiaoyong.es_api.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Create By dongxiaoyong on /2021/2/24
 * description: Book对象
 *
 * @author dongxiaoyong
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Content {
    /**
     * 标题
     */
    private String title;
    /**
     * 价格
     */
    private String price;
    /**
     * 照片
     */
    private String img;
    /**
     * 商店名称
     */
    private String shopName;
}
