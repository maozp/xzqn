package com.gangukeji.xzqn.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

/**
 * @Author: mao
 * @Date: 2019/8/11 16:43
 * @Description: 视频信息
 */
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "xzqn_service_videos")
public class XzqnServiceVideos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "video_name")
    private String videoName;
    @Column(name = "video_des")
    private String videoDes;
    @Column(name = "video_type")
    private String videoType;
    @Column(insertable = false, columnDefinition = "timestamp default current_timestamp", updatable = false)
    @CreationTimestamp
    private Date videoDate;
    @Column(name = "video_addr")
    private String videoAddr;
    @Column
    private Integer LikeNums;
    @Column
    private String videoImg;
    @Column
    private Integer videoReadNums;
}
