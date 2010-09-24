package org.requal;

import org.junit.Test;
//import com.singleton.unrighteous.Heretic;

/**
 * Created by IntelliJ IDEA.
 * User: mburnett
 * Date: May 5, 2010
 * Time: 6:56:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class RighteousSingletonDestroyerTest {

    @Test
    public void shouldCleanseSimpleSingletonPatternInstancesInJar() {
        Heretic curseHimOnce = Heretic.getInstance();
        new RighteousSingletonDestroyer("singleton_library.jar").cleanse();
        Heretic curseHimTwice = Heretic.getInstance();
        assertThat(curseHimOnce, is(not(sameInstance(curseHimTwice))));
    }




}
