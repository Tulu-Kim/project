package com.project.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name="cat")
@Getter @Setter
@ToString
public class Cart extends BaseEntity {

    @Id
    @Column(name ="cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY) //1.
    @JoinColumn(name="member_id") //2.
    private Member member;

}
