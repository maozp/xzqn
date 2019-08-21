package com.gangukeji.xzqn.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
public class XzqnPreServiceImage {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private int orderId;
  private String images;
  private String address;
  private BigDecimal lat;
  private BigDecimal lng;
}
