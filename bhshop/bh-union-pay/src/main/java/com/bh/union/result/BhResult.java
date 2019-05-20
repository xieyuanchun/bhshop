package com.bh.union.result;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
/**
 * 
 * @author xxj
 *
 */

public class BhResult {
	  // 定义jackson对象
    private static final ObjectMapper MAPPER = new ObjectMapper();
    // 响应业务状态
    private Integer status;
    // 响应消息
    private String msg;
    // 响应中的数据
    private Object data;

    public static BhResult build(Integer status, String msg, Object data) {
        return new BhResult(status, msg, data);
    }

    public static BhResult ok(Object data) {
        return new BhResult(data);
    }

    public static BhResult ok() {
        return new BhResult(null);
    }

    public BhResult() {

    }

    public static BhResult build(Integer status, String msg) {
        return new BhResult(status, msg, null);
    }
    public BhResult(Integer status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }
    
    public BhResult(Object data) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

	/**
     * 将json结果集转化为ShqResult对象
     * 
     * @param jsonData json数据
     * @param clazz ShqResult中的object类型
     * @return
     */
    public static BhResult formatToPojo(String jsonData, Class<?> clazz) {
        try {
            if (clazz == null) {
                return MAPPER.readValue(jsonData, BhResult.class);
            }
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode data = jsonNode.get("data");
            Object obj = null;
            if (clazz != null) {
                if (data.isObject()) {
                    obj = MAPPER.readValue(data.traverse(), clazz);
                } else if (data.isTextual()) {
                    obj = MAPPER.readValue(data.asText(), clazz);
                }
            }
            return build(jsonNode.get("status").intValue(), jsonNode.get("msg").asText(), obj);
        } catch (Exception e) {
       
            return null;
        }
    }
    
    /**
     * 将json结果集转化为ShqResult对象
     * 
     * @param jsonData json数据
     * @param clazz ShqResult中的object类型
     * @return
     */
    public static BhResult formatToCookiesPojo(String jsonData, Class<?> clazz) {
        try {
            if (clazz == null) {
                return MAPPER.readValue(jsonData, BhResult.class);
            }
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode data = jsonNode.get("data");
            JsonNode userInfo = data.get("userInfo");
            JsonNode adrCode = data.get("adrCode");
            JsonNode Lnglat = data.get("Lnglat");
            
            Map<String,Object> cookies= new HashMap<String,Object>();
            Object  userInfoObject = null;
            Object  adrCodeObject = null;
            Object  LnglatObject = null;
     
            if(userInfo!=null){
            	  if (userInfo.isObject()) {
            		  userInfoObject = MAPPER.readValue(userInfo.traverse(), HashMap.class);
            		  cookies.put("userInfo", userInfoObject);
            	  }  
            }
            
            if(adrCode!=null){
          	  if (adrCode.isObject()) {
          		  adrCodeObject = MAPPER.readValue(adrCode.traverse(), HashMap.class);
          		  cookies.put("adrCode", adrCodeObject);
          	  }  
            }
            
            if(Lnglat!=null){
          	  if (Lnglat.isObject()) {
          		  LnglatObject = MAPPER.readValue(Lnglat.traverse(), HashMap.class);
          		  cookies.put("Lnglat", LnglatObject);
          	  }  
            }

//            Object obj = null;
//            if (clazz != null) {
//                if (data.isObject()) {
//                    obj = MAPPER.readValue(data.traverse(), clazz);
//                } else if (data.isTextual()) {
//                    obj = MAPPER.readValue(data.asText(), clazz);
//                }
//            }
            return build(jsonNode.get("status").intValue(), jsonNode.get("msg").asText(), cookies);
        } catch (Exception e) {

            return null;
        }
    }
    
    
    

    /**
     * 没有object对象的转化
     * 
     * @param json
     * @return
     */
   
    
    public static BhResult format(String json) {
        try {
            return MAPPER.readValue(json, BhResult.class);
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return null;
    }
    
    
    

    /**
     * Object是集合转化
     * 
     * @param jsonData json数据
     * @param clazz 集合中的类型
     * @return
     */
    public static BhResult formatToList(String jsonData, Class<?> clazz) {
        try {
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode data = jsonNode.get("data");
            Object obj = null;
            if (data.isArray() && data.size() > 0) {
                obj = MAPPER.readValue(data.traverse(),
                        MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
            }
            return build(jsonNode.get("status").intValue(), jsonNode.get("msg").asText(), obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    


}
