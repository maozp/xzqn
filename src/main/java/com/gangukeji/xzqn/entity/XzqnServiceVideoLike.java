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
@Table(name = "xzqn_service_video_like")
public class XzqnServiceVideoLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private Integer userId;

    @Column
    private Integer videoId;

    @Column(insertable = false, columnDefinition = "timestamp default current_timestamp", updatable = false)
    @CreationTimestamp
    private Date likeTime;
}
