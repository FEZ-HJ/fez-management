package com.fez.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by H.J
 * 2019/1/15
 */
public class ReturnJson {

    public static Map<String,Object> SUCCESS(){
        return RETURN("成功！",null,0,0);
    }

    public static Map<String,Object> ERROR(){
        return RETURN("失败！",null,0,500);
    }

    public static Map<String,Object> RETURN(String message,Object data,int count,int code){
        Map<String,Object> map = new HashMap<>();
        map.put("message",message);
        map.put("data",data);
        map.put("count",count);
        map.put("code",code);
        return map;
    }
}
