package com.nm.pojo;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Operations {
    private int id;
    private String state;
    private String operation;
    private Timestamp dateTime;
    private int operSt;
    private String parkPut;
    private int nPoezd;
    private String idx;
    private String nWags;
    private String golXv;
    private int idWagon;

    public Operations() {
    }

    public Operations(int id, String state, String operation, Timestamp dateTime, int operSt, String parkPut, int nPoezd, String idx, String nWags, String golXv, int idWagon) {
        this.id = id;
        this.state = state;
        this.operation = operation;
        this.dateTime = dateTime;
        this.operSt = operSt;
        this.parkPut = parkPut;
        this.nPoezd = nPoezd;
        this.idx = idx;
        this.nWags = nWags;
        this.golXv = golXv;
        this.idWagon = idWagon;
    }
}
