package org.requal;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;
import static org.requal.ReflectiveEqualTo.reflectiveEqualTo;


/**
 * Created by IntelliJ IDEA.
 * User: mburnett
 * Date: Apr 22, 2010
 * Time: 6:50:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class ReflectiveEqualToTest {

    class MyClass {
        double value = 1;
    }


    @Test
    public void shouldDetectEqualLists() {
        String one = new String("1");
        String two = new String("1");
        assertThat(one, not(sameInstance(two)));
        assertThat(one, is(two));
        assertThat(asList(one), is(asList(two)));
        Object lhs = asList(one);
        Object rhs = asList(two);
        assertThat(lhs, reflectiveEqualTo(rhs));
    }
    
   @Test
    public void shouldDetectInequalBigDecimal() {
        assertThat(new BigDecimal(2f), not(reflectiveEqualTo(new BigDecimal(2.1f))));
    }

   @Test
    public void shouldDetectEqualBigDecimal() {
        assertThat(new BigDecimal(2f), reflectiveEqualTo(new BigDecimal(2f)));
    }

    @Test
    public void shouldDetectCharArrayEqual() {
        Object lhs = new char[]{'f', 'o', 'o'};
        Object rhs = new char[]{'f', 'o', 'o'};
        assertThat(lhs, reflectiveEqualTo(rhs));
    }

    @Test
    public void shouldDetectCharEqual() {
        assertThat('f', reflectiveEqualTo('f'));
    }

    @Test
    public void shouldDetectNotEqualLists() {
        Object lhs = asList(new String("1"), new String("2"));
        Object rhs = asList(new String("1"));
        assertThat(lhs, not(reflectiveEqualTo(rhs)));
    }

    @Test
    public void shouldDetectEqualBooleans() {
        assertThat(true, reflectiveEqualTo(true));
    }

    @Test
    public void shouldDetectEqualArrays() {
        assertThat(new MyClass(), is(not(new MyClass())));
        assertThat(new MyClass[]{new MyClass()}, is(not(new MyClass[]{new MyClass()})));
        MyClass[] lhs = {new MyClass(), new MyClass()};
        MyClass[] rhs = {new MyClass(), new MyClass()};
        assertThat(lhs, reflectiveEqualTo(rhs));
    }

    @Test
    public void shouldDetectNonEqualArrays() {
        MyClass[] lhs = {new MyClass(), new MyClass()};
        MyClass[] rhs = {new MyClass()};
        assertThat(lhs, not(reflectiveEqualTo(rhs)));
    }

    @Test
    public void shouldDetectNotEqualBooleans() {
        assertThat(true, not(reflectiveEqualTo(false)));
    }



    @Test
    public void shouldDetectEqualMaps() {
        HashMap<String, String> lhs = getMap();
        HashMap<String, String> rhs = getMap();
        assertThat(lhs, reflectiveEqualTo(rhs));
    }
    @Test
    public void shouldDetectNonEqualMaps() {
        HashMap<String, String> lhs = getMap();
        HashMap<String, String> rhs = getMap();
        rhs.put("two", new String("BOOBOO"));
        assertThat(lhs, reflectiveEqualTo(rhs));
    }



    private HashMap<String, String> getMap() {
        HashMap<String, String> lhs = new HashMap<String, String>();
        lhs.put("one",new String("one-val"));
        lhs.put("two",new String("two-val"));
        return lhs;
    }

}

