package org.requal;

import org.hamcrest.Matcher;
import org.hamcrest.Description;
import org.junit.internal.matchers.TypeSafeMatcher;

import static org.hamcrest.CoreMatchers.is;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User: mburnett
 * Date: Apr 27, 2010
 * Time: 7:08:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReflectiveEqualTo<T> extends TypeSafeMatcher {
    private T rhs;
    private Object lhs;

    public ReflectiveEqualTo(T rhs) {
        this.rhs = rhs;
    }

    public static <T> Matcher<T> reflectiveEqualTo(T rhs) {
        return new ReflectiveEqualTo(rhs);
    }

    private Boolean refEq(Object lhs, Object rhs) {
        return getEqualityCheck(lhs.getClass()).eq(lhs, rhs);
    }

    private EqualityCheck getEqualityCheck(Class<?> type) {
        if (!type.isArray() && isNotGeneric(type) && (type.isEnum() || type.isPrimitive() || isCore(type))) {
            return new EqualityCheck() {

                public boolean eq(Object lhs, Object rhs) {
                    return is(rhs).matches(lhs);
                }
            };
        }


        return new EqualityCheck() {
            public boolean eq(Object lhs, Object rhs) {
                if (lhs == null || rhs == null) {
                    return lhs == null && rhs == null;
                }
                if (lhs.getClass().isArray()) {
                    if (Array.getLength(lhs) != Array.getLength(rhs)) {
                        return false;
                    }
                    boolean result = true;
                    for (int i = 0; i < Array.getLength(lhs); i++) {
                        result &= recursiveEq(Array.get(lhs, i), Array.get(rhs, i));
                    }
                    return result;
                } else {
                    return recursiveEq(lhs, rhs);
                }
            }
        };

    }

    private boolean isNotGeneric(Class<?> type) {
        return type.getTypeParameters().length == 0;
    }

    private boolean isCore(Class<?> type) {
        String packageName = type.getPackage().getName();
        return !(packageName == null) || packageName.startsWith("java.") || packageName.startsWith("javax.");
    }

    private boolean recursiveEq(Object lhs, Object rhs) {
        boolean result = true;

        for (Field field : lhs.getClass().getDeclaredFields()) {
            if (uncheckedField(field)) {
                continue;
            }
            result &= getFieldEqualityCheck(field).eq(lhs, rhs);
        }
        return result;
    }

    private boolean uncheckedField(Field field) {
        return Modifier.isStatic(field.getModifiers()) || Modifier.isTransient(field.getModifiers()) || Modifier.isVolatile(field.getModifiers());
    }

    private EqualityCheck getFieldEqualityCheck(final Field field) {                        
        return new EqualityCheck() {

            public boolean eq(Object lhs, Object rhs) {
                field.setAccessible(true);
                try {
                    return getEqualityCheck(field.getType()).eq(field.get(lhs), field.get(rhs));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        };

    }

    public boolean matchesSafely(Object lhs) {
        this.lhs = lhs;
        return refEq(lhs, rhs);
    }

    public void describeTo(Description description) {
        description.appendText(String.format("expected: %s", rhs));
    }
}

interface EqualityCheck {

    boolean eq(Object lhs, Object rhs);
}
