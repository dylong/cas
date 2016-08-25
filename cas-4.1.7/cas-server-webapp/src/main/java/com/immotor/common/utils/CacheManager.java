
package com.immotor.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
/**
 * 
 * @author Ashion
 *
 */
public final class CacheManager {

    private static Map<String, Object> cacheMap = new HashMap<String, Object>();

    /**
     * 单实例构造方法   
     * 
     */
    private CacheManager() {
        super();
    }
    /**
     * 添加到缓存中。
     * @param key mapkey
     * @param obj 值
     * @return 如果不存在则添加，成功返回ture,存在不添加，返回false
     */
    public static boolean putCacheMap(final String key, final Object obj){
        // 不存在则添加
        if(!hasCache(key)){
            cacheMap.put(key, obj);
            return true;
        }
        return false;
    }
    /**
     * 根据key查询缓存中的may
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getCacheMap(String key) {
        try {
            return   (Map<String, Object>) cacheMap.get(key);
        } catch (Exception ex) {
            return null;
        }
    }
    @SuppressWarnings("unchecked")
    public static Map<String, List<Object>> getCacheListmap(String key) {
        try {
            return (Map<String, List<Object>>) cacheMap.get(key);
        } catch (Exception ex) {
            return null;
        }
    }
    //获取布尔值的缓存   
    public static boolean getSimpleFlag(String key) {
        try {
            return (Boolean) cacheMap.get(key);
        } catch (NullPointerException e) {
            return false;
        }
    }

    public static long getServerStartdt(String key) {
        try {
            return (Long) cacheMap.get(key);
        } catch (Exception ex) {
            return 0;
        }
    }

    //设置布尔值的缓存   
    public synchronized static boolean setSimpleFlag(String key, boolean flag) {
        if (flag && getSimpleFlag(key)) {//假如为真不允许被覆盖   
            return false;
        } else {
            cacheMap.put(key, flag);
            return true;
        }
    }

    public synchronized static boolean setSimpleFlag(String key, long serverbegrundt) {
        if (cacheMap.get(key) == null) {
            cacheMap.put(key, serverbegrundt);
            return true;
        } else {
            return false;
        }
    }
    

    //   
    /**
     * 判断是否存在一个缓存
     * @param key
     * @return 存在则返回ture ,不存在则返回false
     */
    public synchronized static boolean hasCache(String key) {
        return cacheMap.containsKey(key);
    }

    //清除所有缓存   
    public synchronized static void clearAll() {
        cacheMap.clear();
    }

    //清除某一类特定缓存,通过遍历HASHMAP下的所有对象，来判断它的KEY与传入的TYPE是否匹配   
    public synchronized static void clearAll(String type) {
        Iterator i = cacheMap.entrySet().iterator();
        String key;
        ArrayList<String> arr = new ArrayList<String>();
        try {
            while (i.hasNext()) {
                Entry<String, Object> entry = (Entry<String, Object>) i.next();
                key = (String) entry.getKey();
                if (key.startsWith(type)) { //如果匹配则删除掉   
                    arr.add(key);
                }
            }
            for (int k = 0; k < arr.size(); k++) {
                clearOnly(arr.get(k));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //清除指定的缓存   
    public synchronized static void clearOnly(String key) {
        cacheMap.remove(key);
    }


    //获取缓存对象中的所有键值名称   
    @SuppressWarnings("finally")
    public static ArrayList<String> getCacheAllkey() {
        ArrayList<String> a = new ArrayList<String>();
        try {
            Iterator<Entry<String, Object>> i = cacheMap.entrySet().iterator();
            while (i.hasNext()) {
                final Entry<String, Object> entry = (Entry<String, Object>) i.next();
                a.add((String) entry.getKey());
            }
        } catch (Exception ex) {
        } finally {
            return a;
        }
    }
    /**
     *  获取缓存对象中指定类型 的键值名称   .
     * @param type type.
     * @return s.
     */
    public static ArrayList<String> getCacheListkey(final String type) {
        final ArrayList<String> a = new ArrayList<String>();
        String key;
        try {
            Iterator i = cacheMap.entrySet().iterator();
            while (i.hasNext()) {
                Entry<String, Object> entry = (Entry<String, Object>) i.next();
                key = (String) entry.getKey();
                if (key.indexOf(type) != -1) {
                    a.add(key);
                }
            }
        } catch (final Exception ex) {
        } finally {
            return a;
        }
    }
    /**
     * 
     * @param args s
     */
    public static void main(final String[] args) {
        Map ms = new HashMap<String, Object>();
        ms.put("ss", "f");
        CacheManager.putCacheMap("sss", ms);

        
    }
}
