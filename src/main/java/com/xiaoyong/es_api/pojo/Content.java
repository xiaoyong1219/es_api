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
    private String title;
    private String price;
    private String img;
}
