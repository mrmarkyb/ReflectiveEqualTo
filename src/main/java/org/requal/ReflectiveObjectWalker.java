package org.requal;

import java.lang.reflect.Field;

/**
 * Created by IntelliJ IDEA.
 * User: mburnett
 * Date: Apr 28, 2010
 * Time: 6:51:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReflectiveObjectWalker<T> {
    private Class<T> aClass;

    public ReflectiveObjectWalker(Class<T> aClass) {
        this.aClass = aClass;
    }

    public void walk(ClassDataCollector collector, T o) {
        for (Field field : aClass.getDeclaredFields()) {

            if (isPrimitiveOrEnum(field.getType())) {
                collector.appendValueNode(field.getName(), getFieldValue(field, o));
            } else {
                collector.startCompositeNode(field.getName());
                new ReflectiveObjectWalker(field.getType()).walk(collector, getFieldValue(field, o));
                collector.stopCompositeNode();
            }

        }
    }

    private Object getFieldValue(Field field, T o) {
        boolean accessible = field.isAccessible();
        field.setAccessible(true);
        try {
            return field.get(o);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        finally {
            field.setAccessible(accessible);
        }

    }

    private boolean isPrimitiveOrEnum(Class<?> type) {
        return type.isEnum() || type.isPrimitive();
    }
}
