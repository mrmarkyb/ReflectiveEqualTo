package org.requal;

import java.util.List;
import java.util.ArrayList;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * Created by IntelliJ IDEA.
 * User: mburnett
 * Date: May 5, 2010
 * Time: 7:18:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class RighteousSingletonDestroyer {
    private JarWalker jarWalker;

    public RighteousSingletonDestroyer(String jarName) {
        this(new JarWalker(jarName));
    }

    public RighteousSingletonDestroyer(JarWalker jarWalker) {
        this.jarWalker = jarWalker;
    }

    public void cleanse() {
        List<Field> fields = getStaticFieldsWithTypeOfEnclosingClass(getClasses());
        setStaticFieldsToNull(fields);
    }

    private void setStaticFieldsToNull(List<Field> fields) {
        for (Field field : fields) {
            setStaticFieldToNull(field);
        }
    }

    private void setStaticFieldToNull(Field field) {
        boolean accessible = field.isAccessible();
        try {
            field.setAccessible(true);
            field.set(null, null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        finally {
            field.setAccessible(accessible);
        }
    }

    private List<Field> getStaticFieldsWithTypeOfEnclosingClass(List<Class> classes) {
        List<Field> fields = new ArrayList<Field>();
        for(Class clazz: classes) {
            fields.addAll(getStaticFieldsWithTypeOfEnclosingClass(clazz));
        }
        return fields;
    }

    private List<Field> getStaticFieldsWithTypeOfEnclosingClass(Class clazz) {
        List<Field> fields = new ArrayList<Field>();
        for(Field field : clazz.getDeclaredFields()) {
            if(Modifier.isStatic(field.getModifiers()) && field.getType().equals(clazz)) {
                fields.add(field);
            }
        }
        return fields;
    }

    private List<Class> getClasses() {
        List<Class> classes = new ArrayList<Class>();
        for(String className : jarWalker.listClassNames()) {
            try {
                classes.add(Class.forName(className));
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return classes;
    }
}
