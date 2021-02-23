package com.xiaoyong.es_api.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Create By dongxiaoyong on /2021/2/20
 * description: User实体
 *
 * @author dongxiaoyong
 */
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    /**
     * 姓名
     */
    private String name;
    /**
     * 年龄
     */
    private int age;
}
