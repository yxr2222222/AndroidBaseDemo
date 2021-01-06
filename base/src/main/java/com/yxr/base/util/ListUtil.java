package com.yxr.base.util;

import java.util.List;

public class ListUtil {
    public static boolean isEmpty(List<?> dataList) {
        return dataList == null || dataList.isEmpty();
    }
}
