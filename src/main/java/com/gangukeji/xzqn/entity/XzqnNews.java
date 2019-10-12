package com.gangukeji.xzqn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "xzqn_news")
public class XzqnNews {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column
    private String newsTitle;

    @CreationTimestamp
    private Date newsDate;
    @Column(name = "news_content",columnDefinition = "MEDIUMTEXT")
    @JsonIgnore
    private String newsContent;
    @Column
    private Integer newsReadNums;
    @Column
    private Integer newsCommentNums;
    @Column
    private Integer newsLikeNums;
    @Column(columnDefinition = "TEXT")
    private String titleFirstImg;
    @Column
    private String titleSecondImg;
    @Column
    private String titleThirdImg;
    @Column
    private Integer newsType;
    @Column
    private String newsUrl;
}
