package com.jpa.demo.relationMappingDifference.manyToMany.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
@Entity
@Getter
@Setter
public class Member {
    @Id
    @Column(name = "MEMBER_ID")
    private String id;

    private String username;

    // 다대다 단방향 설정
    @ManyToMany
    @JoinTable(name = "MEMBER_PRODUCT",
                joinColumns = @JoinColumn(name = "MEMBER_ID"),
                inverseJoinColumns = @JoinColumn(name = "PRODUCT_ID"))
    private List<Product> products = new ArrayList<>();


}
