package com.jpa.demo.relationMappingDifference.oneToOne.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Locker {

    @Id
    @GeneratedValue
    @Column(name = "LOCKER_ID")
    private Long id;

    private String name;

//    @OneToOne(mappedBy = "locker")
//    private Member member;


    @OneToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;
}
