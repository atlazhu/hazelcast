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

package com.hazelcast.test;

import com.hazelcast.test.annotation.QuickTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@RunWith(HazelcastSerialClassRunner.class)
@Category(QuickTest.class)
public class TestCategoriesTest_withSerialTest extends HazelcastTestSupport {

    @Test
    public void testGetTestCategories() {
        Collection<Class<?>> testCategories = getTestCategories();

        assertThat(testCategories).as("@%s annotation did not have expected values", Category.class.getSimpleName())
                .containsExactlyInAnyOrder(QuickTest.class);
    }

    @Test
    public void testAssertThatNotMultithreadedTest() {
        assertThatCode(HazelcastTestSupport::assertThatIsNotMultithreadedTest).as("Expected an exception on this serial test")
                .doesNotThrowAnyException();
    }
}
