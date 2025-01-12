package com.pvt.blog.pojo;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Table(name = "comment")
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    /**
     * 内容
     */
    @Column(name = "content")
    private String content;

    /**
     * 评论时间
     */
    @CreatedDate
    @Column(name = "date", nullable = false)
    private Date date;

    /**
     * 点赞数量
     */
    @Column(name = "likes")
    private Integer likes;

    @Column(name = "rating")
    private Integer rating;

    /**
     * user id
     */
    @CreatedBy
    @LastModifiedBy
    @Column(name = "user_id")
    private Integer userId;
    /**
     * post id
     */
    @Column(name = "post_id")
    private Integer postId;

    /**
     * title
     */
    @Column(name = "title")
    private String title;

    /**
     * parent ID
     */
    @Column(name = "parent_id")
    private String parentId;
}
