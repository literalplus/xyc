/*
 * Copyright (c) 2013 - 2017 xxyy (Philipp Nowak; xyc@l1t.li). All rights reserved.
 *
 * Any usage, including, but not limited to, compiling, running, redistributing, printing,
 *  copying and reverse-engineering is strictly prohibited without explicit written permission
 *  from the original author and may result in legal steps being taken.
 *
 * See the included LICENSE file (core/src/main/resources) for details.
 */

package li.l1t.common.util.math;

import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class WeightedDistributionTest {
    @Test
    public void testProbabilitySum__one() throws Exception {
        //given
        WeightedDistribution<String> dist = givenSomeDistribution();
        //when
        dist.put("a", 0.5D);
        dist.put("b", 0.5D);
        //then
        assertThat("probability sum", dist.probabilitySum(), is(1D));
    }

    @Test
    public void testProbabilitySum__huge() throws Exception {
        //given
        WeightedDistribution<String> dist = givenSomeDistribution();
        //when
        dist.put("a", 1337.4D);
        dist.put("b", 567.6D);
        //then
        assertThat("probability sum", dist.probabilitySum(), is(1337.4D + 567.6D));
    }

    @Test
    public void put() throws Exception {
        //given
        WeightedDistribution<String> dist = givenSomeDistribution();
        //when
        dist.put("a", 15.4D);
        dist.put("b", 16.5D);
        //then
        thenThereIsAProbabilitySuchThat(dist, 15.4D, "a");
        thenThereIsAProbabilitySuchThat(dist, 15.4D + 16.5D, "b");
    }

    private void thenThereIsAProbabilitySuchThat(WeightedDistribution<String> dist, double key, String value) {
        assertThat("put key", dist.probabilities().entrySet(), hasItem(
                both(
                        hasProperty("key", is(key))
                ).and(
                        hasProperty("value", is(value))

                )
        ));
    }

    private WeightedDistribution<String> givenSomeDistribution() {
        return new WeightedDistribution<>();
    }
}