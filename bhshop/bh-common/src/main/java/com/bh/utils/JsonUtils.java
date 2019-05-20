package com.bh.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.bh.result.BhResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.json.JSONObject;

/**
 * 自定义响应结构
 */
public class JsonUtils {

    // 定义jackson对象
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 将对象转换成json字符串。
     * <p>Title: pojoToJson</p>
     * <p>Description: </p>
     * @param data
     * @return
     */
    public static String objectToJson(Object data) {
    	try {
			String string = MAPPER.writeValueAsString(data);
			return string;
		} catch (JsonProcessingException e) {
			LoggerUtil.error(e);
		}
    	return null;
    }
    
    /**
     * 将json结果集转化为对象
     * 
     * @param jsonData json数据
     * @param clazz 对象中的object类型
     * @return
     */
    public static <T> T jsonToPojo(String jsonData, Class<T> beanType) {
        try {
            T t = MAPPER.readValue(jsonData, beanType);
            return t;
        } catch (Exception e) {
        	LoggerUtil.error(e);
        }
        return null;
    }
    
    /**
     * 将json数据转换成pojo对象list
     * <p>Title: jsonToList</p>
     * <p>Description: </p>
     * @param jsonData
     * @param beanType
     * @return
     */
    public static <T>List<T> jsonToList(String jsonData, Class<T> beanType) {
    	JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
    	try {
    		List<T> list = MAPPER.readValue(jsonData, javaType);
    		return list;
		} catch (Exception e) {
			e.printStackTrace();
			LoggerUtil.error(e);
		}
    	
    	return null;
    }
    
    public static <T>List<T> stringToList(String string){
    	try {
			/**scj-批量删除
				int batchDelete(List<String> list);
			 * <update id="batchDelete" parameterType="java.util.List">
      				update order_main set is_del = '1'
         			where id in
         			<foreach item="id" collection="list" open="(" close=")" separator=",">
             		#{id}
         			</foreach>
   			</update>
			 * 
			 * */
    		List<T> list = new ArrayList<T>();
    		String[] str = string.split(",");
    		for (int i = 0; i < str.length; i++) {
    		         list.add((T) str[i]);
    		}
    		return list;
    		//List<String> result = Arrays.asList(string.split(",")); //string转list
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
    	
    }
    
    /**2017-11-6chengfengyun添加，将string转为JSONObject,在skuValue时用到
     * 将string转为object
     * <p>Title: stringToObject</p>
     * <p>Description: </p>
     * @param string
     * @return JSONObject
     */
    public static JSONObject stringToObject(String string){
    	try {
    		JSONObject jsonObject = JSONObject.fromObject(string);
    		return jsonObject;
		} catch (Exception e) {
			// TODO: handle exception
			return null;
		}
    	
    }
    
    
    
}
