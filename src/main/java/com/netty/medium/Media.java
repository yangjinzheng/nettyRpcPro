package com.netty.medium;

import com.alibaba.fastjson.JSONObject;
import com.netty.handler.param.ServerRequest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class Media {
    public static Map<String, BeanMethod> beanMap;
    private static Media media = null;

    static {
        beanMap = new HashMap<String, BeanMethod>();
    }

    public Media() {
    }

    public static Media newInstance() {
        if (media == null) {
            return new Media();
        }
        return media;
    }


    /**
     * 反射处理业务
     *
     * @param serverRequest
     * @return
     */
    public Object process(ServerRequest serverRequest) {
        Object result = null;
        try {
            String command = serverRequest.getCommand();
            BeanMethod beanMethod = beanMap.get(command);
            if (beanMethod == null) {
                return null;
            }
            Object bean = beanMethod.getBean();
            Method m = beanMethod.getMethod();
            Class paramType = m.getParameterTypes()[0];
            Object content = serverRequest.getContent();
            Object args = JSONObject.parseObject(JSONObject.toJSONString(content), paramType);
            result = m.invoke(bean, args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return result;
    }
}
