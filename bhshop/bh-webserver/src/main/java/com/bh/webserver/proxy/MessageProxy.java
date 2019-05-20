package com.bh.webserver.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;


import javax.websocket.Session;

import com.bh.webserver.common.CMD;
import com.bh.webserver.common.NickPool;
import com.bh.webserver.entity.Text;
import com.bh.webserver.util.JsonUtil;

import java.lang.reflect.Method;

/**
 * Created by 一线生 on 2015/11/22.
 * 说明：消息发送代理
 */
public class MessageProxy implements MethodInterceptor {

    private static MessageProxy messageProxy = new MessageProxy();

    public <T> T getProxy(Class<T> clazz) {
        return (T) Enhancer.create(clazz, this);
    }

    public static MessageProxy getInstance() {
        return messageProxy;
    }

    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        Object result = methodProxy.invokeSuper(o, objects);

        //如果是发送消息方法
        if(method.getName().equals("send")) {
            return nickNameIntercept(result, o, objects, methodProxy);
        }

        return result;
    }

    /**
     * 创建昵称拦截
     * @param o
     * @param objects
     * @param methodProxy
     * @return
     * @throws Throwable
     */
    private Object nickNameIntercept(Object result, Object o, Object[] objects, MethodProxy methodProxy) throws Throwable {
        String message = (String) objects[0];
        Session session = (Session) objects[1];
        Text text = JsonUtil.getBean(message, Text.class);
        message = text.getMessage();
        if(!message.startsWith(CMD.USER_NAME_CREATE)) {
            return result;
        }
        String nickName = message.replace(CMD.USER_NAME_CREATE, "");
        text.setMessage(nickName + " is Joined");
        NickPool.add(session.getId(), nickName);

        objects[0] = JsonUtil.getString(text);
        return  methodProxy.invokeSuper(o, objects);
    }

}
