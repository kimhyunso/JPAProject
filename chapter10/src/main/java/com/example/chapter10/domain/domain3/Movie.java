package com.example.chapter10.domain.domain3;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Setter
@DiscriminatorValue("M")
@Entity
public class Movie extends Item{

    private String director;
    private String actor;

}
