package com.jpa.demo.relationMappingDifference.manyToMany.complexKeyDomain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class Product {
    @Id @Column(name = "PRODUCT_ID")
    private String id;

    private String name;

    // 역방향
    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<MemberProduct> memberProducts;
}
