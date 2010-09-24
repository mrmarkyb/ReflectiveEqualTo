package org.requal;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by IntelliJ IDEA.
 * User: mburnett
 * Date: Apr 28, 2010
 * Time: 6:52:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReflectiveObjectWalkerTest implements ClassDataCollector {
    private StringBuilder collected = new StringBuilder();

    public static final class MyClass {
        private double value = 1;
        private boolean isChild = true;
    }

    public static final class MyOtherClass {
        private double value = 0;
        private boolean isParent = true;
        private MyClass child = new MyClass();
    }

    @Test
    public void shouldWalkSimpleClass() {
        ReflectiveObjectWalker<MyClass> objectWalker = new ReflectiveObjectWalker<MyClass>(MyClass.class);
        objectWalker.walk(this, new MyClass());
        assertThat(collected.toString(), is("{value: 1.0} {isChild: true} "));
    }

    @Test
    public void shouldWalkCompositeClass() {
        ReflectiveObjectWalker<MyOtherClass> objectWalker = new ReflectiveObjectWalker<MyOtherClass>(MyOtherClass.class);
        objectWalker.walk(this, new MyOtherClass());
        assertThat(collected.toString(), is("{value: 0.0} {isParent: true} {child:{{value: 1.0} {isChild: true} }} "));
    }

    public void appendValueNode(String name, Object value) {
        collected.append(String.format("{%s: %s} ", name, value));
    }

    public void startCompositeNode(String name) {
        collected.append(String.format("{%s:{", name));
    }

    public void stopCompositeNode() {
        collected.append("}} ");
    }
}
