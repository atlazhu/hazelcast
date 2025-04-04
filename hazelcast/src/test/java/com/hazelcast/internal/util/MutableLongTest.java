/*
 * Copyright (c) 2008-2025, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.internal.util;

import com.hazelcast.test.HazelcastParallelClassRunner;
import com.hazelcast.test.annotation.ParallelJVMTest;
import com.hazelcast.test.annotation.QuickTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(HazelcastParallelClassRunner.class)
@Category({QuickTest.class, ParallelJVMTest.class})
public class MutableLongTest {

    @Test
    public void testAddAndGet() {
        MutableLong mutableLong = MutableLong.valueOf(13);

        assertEquals(24L, mutableLong.addAndGet(11));
        assertEquals(24L, mutableLong.value);
    }

    @Test
    public void testGetAndInc() {
        MutableLong mutableLong = MutableLong.valueOf(13);

        assertEquals(13L, mutableLong.getAndInc());
        assertEquals(14L, mutableLong.value);
    }

    @Test
    public void testToString() {
        MutableLong mutableLong = new MutableLong();
        String s = mutableLong.toString();
        assertEquals("MutableLong{value=0}", s);
    }

    @Test
    public void testEquals() {
        assertEquals(MutableLong.valueOf(0), MutableLong.valueOf(0));
        assertEquals(MutableLong.valueOf(10), MutableLong.valueOf(10));
        assertNotEquals(MutableLong.valueOf(0), MutableLong.valueOf(10));
        assertNotEquals(null, MutableLong.valueOf(0));
        assertNotEquals("foo", MutableLong.valueOf(0));


        MutableLong self = MutableLong.valueOf(0);
        assertEquals(self, self);
    }

    @Test
    public void testHash() {
        assertEquals(MutableLong.valueOf(10).hashCode(), MutableLong.valueOf(10).hashCode());
    }
}
