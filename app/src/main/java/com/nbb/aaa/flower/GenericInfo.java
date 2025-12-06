package com.nbb.aaa.flower;

import java.io.Serializable;

public class GenericInfo implements Serializable {
    long id;

    public GenericInfo(long id){
        this.id = id;
    }

    public GenericInfo(){
    }
    public long get_id() {
        return id;
    }

    public void set_id(int _id) {
        this.id = _id;
    }
}
