package org.ipman.web.projects.user.management;

import javax.management.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ipipman on 2021/3/18.
 *
 * @version V1.0
 * @Package org.ipman.web.projects.user.manager
 * @Description: (用一句话描述该文件做什么)
 * @date 2021/3/18 6:20 下午
 */

// 动态结构，无固定接口类型（运行时确定）
public class UserMXBean implements DynamicMBean {

    // 五个属性
    // id、name、password、email、phoneNumber
    private Map<String, Object> attributes = new HashMap<>();

    /**
     * 根据MBean的名称，获取 MBean 属性
     */
    @Override
    public Object getAttribute(String attribute) throws AttributeNotFoundException, MBeanException, ReflectionException {
        if (!attributes.containsKey(attribute)) {
            throw new AttributeNotFoundException("...");
        }
        return attributes.get(attribute);
    }

    @Override
    public void setAttribute(Attribute attribute) throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
        attributes.put(attribute.getName(), attribute.getValue());
    }

    /**
     * 获取 MBean 属性
     */
    @Override
    public AttributeList getAttributes(String[] attributes) {
        AttributeList attributeList = new AttributeList();
        for (String attribute : attributes) {
            try {
                Object attributeValue = getAttribute(attribute);
                attributeList.add(new Attribute(attribute, attributeValue));
            } catch (AttributeNotFoundException | MBeanException | ReflectionException e) {
            }
        }
        return attributeList;
    }

    @Override
    public AttributeList setAttributes(AttributeList attributes) {
        return null;
    }

    @Override
    public Object invoke(String actionName, Object[] params, String[] signature) throws MBeanException, ReflectionException {
        // 方法被调用时

        return null;
    }

    @Override
    public MBeanInfo getMBeanInfo() {
        return null;
    }
}
