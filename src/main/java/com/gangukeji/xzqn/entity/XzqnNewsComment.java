package com.gangukeji.xzqn.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "xzqn_news_comment")
public class XzqnNewsComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private Integer userId;
    @Column
    private String userName;
    @Column(insertable = false, columnDefinition = "timestamp default current_timestamp", updatable = false)
    @CreationTimestamp
    private Date commentTime;
    @Column(name = "comment",columnDefinition = "TEXT")
    private String comment;
    @Column
    private String userHeadImg;
    @Column
    private Integer newsId;
}
