/*
 * MIT License
 *
 * Copyright (C) 2013 - 2017 Philipp Nowak (https://github.com/xxyy) and contributors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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