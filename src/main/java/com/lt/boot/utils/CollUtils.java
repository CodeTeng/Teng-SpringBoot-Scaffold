package com.lt.boot.utils;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.IterUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @description: 继承自 hutool 的集合工具类
 * @author: ~Teng~
 * @date: 2024/2/16 21:19
 */
public class CollUtils extends CollectionUtil {
    public static <T> List<T> emptyList() {
        return Collections.emptyList();
    }

    public static <T> Set<T> emptySet() {
        return Collections.emptySet();
    }

    public static <K, V> Map<K, V> emptyMap() {
        return Collections.emptyMap();
    }

    public static <T> Set<T> singletonSet(T t) {
        return Collections.singleton(t);
    }

    public static <T> List<T> singletonList(T t) {
        return Collections.singletonList(t);
    }

    public static List<Integer> convertToInteger(List<String> originList) {
        return CollUtils.isNotEmpty(originList) ? originList.stream().map(NumberUtil::parseInt).collect(Collectors.toList()) : null;
    }

    public static List<Long> convertToLong(List<String> originLIst) {
        return CollUtils.isNotEmpty(originLIst) ? originLIst.stream().map(NumberUtil::parseLong).collect(Collectors.toList()) : null;
    }

    /**
     * 以 conjunction 为分隔符将集合转换为字符串 如果集合元素为数组、Iterable或Iterator，则递归组合其为字符串
     *
     * @param collection  集合
     * @param conjunction 分隔符
     * @param <T>         集合元素类型
     * @return 连接后的字符串
     * See Also: IterUtil.join(Iterator, CharSequence)
     */
    public static <T> String join(Collection<T> collection, CharSequence conjunction) {
        if (null == collection || collection.isEmpty()) {
            return null;
        }
        return IterUtil.join(collection.iterator(), conjunction);
    }

    public static <T> String joinIgnoreNull(Collection<T> collection, CharSequence conjunction) {
        if (null == collection || collection.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (T t : collection) {
            if (t == null) continue;
            sb.append(t).append(",");
        }
        if (sb.length() <= 0) {
            return null;
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }

    /**
     * 将元素加入到集合中，为null的过滤掉
     *
     * @param list 集合
     * @param data 要添加的数据
     * @param <T>  元素类型
     */
    public static <T> void add(Collection<T> list, T... data) {
        if (list == null || ArrayUtil.isEmpty(data)) {
            return;
        }
        for (T t : data) {
            if (ObjectUtil.isNotEmpty(t)) {
                list.add(t);
            }
        }
    }
}
