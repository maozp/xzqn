package com.gangukeji.xzqn.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String videoname;
    @Column(name = "video_des")
    private String videodes;
    @Column(name = "video_type")
    private String videotype;
    @Column(name = "video_date")
    private Date videodate;
    @Column(name = "video_addr")
    private String videoaddr;
}
