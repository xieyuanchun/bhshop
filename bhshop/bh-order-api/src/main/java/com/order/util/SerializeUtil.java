package com.order.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.bh.goods.pojo.TopicPrizeConfig;

import redis.clients.jedis.Jedis;

/**
 * @Description: xieyc
 * @author xieyc
 * @date 2018年1月25日 上午10:35:29 
 */
public class SerializeUtil {
	/**
	 * 序列化
	 * @param object
	 * @return
	 */
	public static byte[] serialize(Object object) {
		if (object == null) {
			return null;
		}
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		byte[] bytes = null;
		try {
			// 序列化
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			bytes = baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(oos);
			close(baos);
		}
		return bytes;
	}

	/**
	 * 反序列化
	 * @param bytes
	 * @return
	 */
	public static Object unserialize(byte[] bytes) {
		if (bytes == null) {
			return null;
		}
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		try {
			// 反序列化
			bais = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(bais);
			close(ois);
		}
		return null;
	}

	/**
	 * 序列化 list 集合
	 * 
	 * @param list
	 * @return
	 */
	public static byte[] serializeList(List<?> list) {

		if (list == null) {
			return null;
		}
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		byte[] bytes = null;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			for (Object obj : list) {
				oos.writeObject(obj);
			}
			bytes = baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(oos);
			close(baos);
		}
		return bytes;
	}

	/**
	 * 反序列化 list 集合
	 * 
	 * @param lb
	 * @return
	 */
	public static List<?> unserializeList(byte[] bytes) {
		if (bytes == null) {
			return null;
		}

		List<Object> list = new ArrayList<Object>();
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		try {
			// 反序列化
			bais = new ByteArrayInputStream(bytes);
			ois = new ObjectInputStream(bais);
			while (bais.available() > 0) {
				Object obj = (Object) ois.readObject();
				if (obj == null) {
					break;
				}
				list.add(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(bais);
			close(ois);
		}
		return list;
	}

	/**
	 * 关闭io流对象
	 * 
	 * @param closeable
	 */
	public static void close(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public static Jedis getJedis() {
		Jedis jedis=JedisUtil.getInstance().getJedis(); 
		return jedis;
	}
	
	/**
	 * 设置对象
	 * @param key
	 * @param obj
	 */
	public static void setObject(String key, Object obj) {
		try {
			obj = obj == null ? new Object() : obj;
			getJedis().set(key.getBytes(), SerializeUtil.serialize(obj));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取对象
	 * @param key
	 * @return Object
	 */
	public static Object getObject(String key) {
		if (getJedis() == null || !getJedis().exists(key)) {
			return null;
		}
		byte[] data = getJedis().get(key.getBytes());
		return (Object) SerializeUtil.unserialize(data);
	}

	/**
	 * 设置List集合
	 * @param key
	 * @param list
	 */
	public static void setList(String key, List<?> list) {
		try {
			if (list!=null && list.size()>0) {
				getJedis().set(key.getBytes(), SerializeUtil.serializeList(list));
			} else {// 如果list为空,则设置一个空
				getJedis().set(key.getBytes(), "".getBytes());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取List集合
	 * @param key
	 * @return
	 */
	public static List<?> getList(String key) {
		if (getJedis() == null || !getJedis().exists(key)) {
			return null;
		}
		byte[] data = getJedis().get(key.getBytes());
		return SerializeUtil.unserializeList(data);
	}

	
	
	/**
	 * @Description: redis测试
	 * @author xieyc
	 * @date 2018年1月25日 上午11:01:51
	 */
	public static void buildTestData() {
		List<TopicPrizeConfig> topicPrizeList = new ArrayList<TopicPrizeConfig>();
		TopicPrizeConfig topicPrizeConfig1 = new TopicPrizeConfig();
		topicPrizeConfig1.setId(100);
		topicPrizeConfig1.setActPrice(100);
		topicPrizeConfig1.setNum(100);
		topicPrizeConfig1.setTgId(100);
		topicPrizeList.add(topicPrizeConfig1);

		TopicPrizeConfig topicPrizeConfig2 = new TopicPrizeConfig();
		topicPrizeConfig2.setId(200);
		topicPrizeConfig2.setActPrice(200);
		topicPrizeConfig2.setNum(200);
		topicPrizeConfig2.setTgId(200);
		topicPrizeList.add(topicPrizeConfig2);
		SerializeUtil.setList("list001", topicPrizeList);
	}

	public static void main(String[] args) {

		/*List<TopicPrizeConfig> topicPrizeConfigList = (List<TopicPrizeConfig>) SerializeUtil.getList("list001");
		for (TopicPrizeConfig topicPrizeConfig : topicPrizeConfigList) {
			System.out.println(topicPrizeConfig.getId() + "   " + topicPrizeConfig.getActPrice());
		}*/
		
	}
	
	
}
