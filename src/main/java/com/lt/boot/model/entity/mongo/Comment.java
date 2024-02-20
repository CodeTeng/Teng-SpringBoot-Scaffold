package com.lt.boot.model.entity.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @description: 文章评论实体类
 * @author: ~Teng~
 * @date: 2024/2/16 17:55
 */
//把一个java类声明为mongodb的文档，可以通过collection参数指定这个类对应的文档。
//@Document(collection="mongodb 对应 collection 名")
// 若未加 @Document ，该 bean save 到 mongo 的 comment collection
// 若添加 @Document ，则 save 到 comment collection
@Document(collection = "comment")//可以省略，如果省略，则默认使用类名小写映射集合
//复合索引
// @CompoundIndex( def = "{'userid': 1, 'nickname': -1}")
@Data
public class Comment implements Serializable {
    @Id
    private String id;
    private Integer authorId;
    //该属性对应mongodb的字段的名字，如果一致，则无需该注解
    @Field("authorName")
    private String authorName;
    //添加了一个单字段的索引
    @Indexed
    private Integer entryId;
    private Integer channelId;
    private Boolean type;
    private String content;
    private String image;
    private Integer likes;
    private Integer reply;
    private Byte flag;
    private BigDecimal longitude;
    private BigDecimal latitude;
    private String address;
    private Integer ord;
    private Date createdTime;
    private Date updatedTime;
}
