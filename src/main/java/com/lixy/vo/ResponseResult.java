package com.lixy.vo;


import com.lixy.enums.ErrorState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseResult {
    private boolean success;
    private Object obj;
    private List<Object> list;
    private Map<String, Object> data;
    private Integer code;
    private String msg;

    private ResponseResult(boolean success){
        this.success = success;
    }

    private ResponseResult(boolean success, Map<String, Object> data){
        this.success = success;
        this.data = data;
    }
    private ResponseResult(boolean success, List<Object> datas){
        this.success = success;
        this.list = datas;
    }
    private ResponseResult(boolean success, Object o){
        this.success = success;
        this.obj = o;
    }
    private ResponseResult(boolean success, Integer code, String msg){
        this.success = success;
        this.code = code;
        this.msg = msg;
    }

    public static ResponseResult success(Map<String, Object> data){
        return new ResponseResult(true, data);
    }

    public static ResponseResult success(){
        return new ResponseResult(true);
    }

    public static ResponseResult success(String key, Object value){
        Map<String, Object> data = new HashMap<>(1);
        data.put(key, value);
        return new ResponseResult(true, data);
    }
    public static ResponseResult success(List<Object> objects){
        return new ResponseResult(true, objects);
    }
    public static ResponseResult success(Object o){
        return new ResponseResult(true, o);
    }
    public static ResponseResult error(ErrorState errorState){
        return new ResponseResult(false, errorState.getCode(), errorState.getMsg());
    }

}
