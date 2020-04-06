package com.nm.db;

import com.nm.pojo.Operations;
import com.nm.pojo.Wagon;

import java.util.List;

public interface WagData {
    int persistData(Wagon w);

    List<Operations> getOpersByWagon(int idWagon);
}
