package com.jpa.demo.advancedMapping.embeddedIdentDomain;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class GrandChildId implements Serializable {

    private ChildId childId;

    @Column(name = "GRANDCHILD_ID")
    private String id;


    public GrandChildId(){}

    public GrandChildId(String id){
        this.id = id;
    }



    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
