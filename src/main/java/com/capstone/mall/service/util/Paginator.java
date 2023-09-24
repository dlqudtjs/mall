package com.capstone.mall.service.util;

import java.util.List;

public class Paginator<T> {

    public List<T> paginate(List<T> list, int pageNum, int pageSize) {
        pageNum = Math.max(pageNum, 1);
        int endIdx = Math.min(pageNum * pageSize, list.size());

        return list.subList((pageNum - 1) * pageSize, endIdx);
    }
}
