package com.immotor.common.utils;

import java.util.HashMap;
import java.util.Map;


public class Result {
    private static final int SUCCESS=0;
    private static final int FAIL=1;
    public static Map<String ,Object> NO(){
        Map<String,Object> returnmap = new HashMap<String, Object>();
        returnmap.put("rtcode", FAIL);
        return returnmap;
    }
    public static Map<String ,Object> NO(Map<String, Object> obj){
        Map<String,Object> returnmap = new HashMap<String, Object>();
        returnmap.put("rtcode", FAIL);
        returnmap.putAll(obj);
        return returnmap;
    }
    public static Map<String ,Object> OK(Map<String, Object> obj){
        Map<String,Object> returnmap = new HashMap<String, Object>();
        returnmap.put("rtcode", SUCCESS);
        returnmap.putAll(obj);
        return returnmap;
    }
    public static Map<String ,Object> OK(){
        Map<String,Object> returnmap = new HashMap<String, Object>();
        returnmap.put("rtcode", SUCCESS);
        return returnmap;
    }
}
