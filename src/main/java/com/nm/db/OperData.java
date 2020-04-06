package com.nm.db;

import com.nm.pojo.Operations;

import java.util.List;

public interface OperData {
    boolean persistOpers(List<Operations> oList, int idWagon);

    List<Operations> getOpersByWagon(int idWagon);
}
