package com.nm.pojo;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class Wagon {
    private int id;
    private int wagNo;
    private Timestamp fromDate;
    private Timestamp toDate;
    private List<Operations> list;

    public Wagon() {
    }
}
