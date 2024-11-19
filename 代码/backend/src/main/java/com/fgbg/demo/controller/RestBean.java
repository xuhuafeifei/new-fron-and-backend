package com.fgbg.demo.controller;


import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;

@Data
public class RestBean<T> extends HashMap<String, String> {
    private int code;
    private T data;
    private String massage;

    private static final long serialVersionUID = 2L;

    public RestBean() {

    }

    public RestBean(int code, T data, String massage) {
        this.code = code;
        this.data = data;
        this.massage = massage;
    }



    public static <T> RestBean<T> success(T data){
        RestBean<T> bean = new RestBean<>();
        bean.put("code", String.valueOf(200));
        bean.put("message", String.valueOf("成功"));
        bean.put("data", (String) data);
        return bean;
    }

    public static <T> RestBean<T> success(int code,String message){

        return new RestBean<>(code,null,"登录成功");
    }

    public static <T> RestBean<T> success(){
        return RestBean.success(null);
    }


    public static <T> RestBean<T> failure(int code,String massage){
        return new RestBean<>(code,null,massage);

    }

//    public String JsObject(){
//       return JSONObject.toJSONString(this, JSONWriter.Feature.WriteNulls);
//    }

}
