package org.requal;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.stub;

public class YakShavingTest {

    @Test
    public void shouldShaveAYak() {
        Yak yak = mock(Yak.class);
        when(yak.shave()).thenReturn(true);
        assertThat(yak.shave(), is(true));
    }
}
