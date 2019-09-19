package com.gangukeji.xzqn.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @Column(name = "news_date")
    private Date newsDate;
    @Column(name = "news_content",columnDefinition = "TEXT")
    private String newsContent;
    @Column
    private Integer newsReadNums;
    @Column
    private Integer newsCommentNums;
    @Column
    private Integer newsLikeNums;
    @Column
    private String titleFirstImg;
    @Column
    private String titleSecondImg;
    @Column
    private String titleThirdImg;
    @Column
    private Integer newsType;
}
