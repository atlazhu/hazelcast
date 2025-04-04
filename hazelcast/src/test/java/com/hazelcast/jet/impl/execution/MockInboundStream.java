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

package com.hazelcast.jet.impl.execution;

import com.hazelcast.jet.impl.util.ProgressState;

import javax.annotation.Nonnull;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.function.Consumer;

import static com.hazelcast.jet.impl.execution.DoneItem.DONE_ITEM;
import static com.hazelcast.jet.impl.util.ProgressState.DONE;
import static com.hazelcast.jet.impl.util.ProgressState.MADE_PROGRESS;
import static com.hazelcast.jet.impl.util.ProgressState.NO_PROGRESS;
import static com.hazelcast.jet.impl.util.ProgressState.WAS_ALREADY_DONE;

public class MockInboundStream implements InboundEdgeStream {
    private int ordinal;
    private final int priority;
    private final Deque<Object> mockData;
    private final int chunkSize;
    private SpecialBroadcastItem pendingItem = null;

    private boolean done;

    MockInboundStream(int priority, List<?> mockData, int chunkSize) {
        this.priority = priority;
        this.chunkSize = chunkSize;
        this.mockData = new ArrayDeque<>(mockData);
    }

    void push(Object... items) {
        mockData.addAll(Arrays.asList(items));
    }

    public void setOrdinal(int ordinal) {
        this.ordinal = ordinal;
    }

    @Nonnull @Override
    public ProgressState drainTo(@Nonnull Consumer<Object> dest) {
        if (done) {
            return WAS_ALREADY_DONE;
        }

        if (pendingItem != null) {
            dest.accept(pendingItem);
            pendingItem = null;
            return MADE_PROGRESS;
        }

        if (mockData.isEmpty()) {
            return NO_PROGRESS;
        }
        for (int i = 0; i < chunkSize && !mockData.isEmpty(); i++) {
            final Object item = mockData.poll();
            if (item == DONE_ITEM) {
                done = true;
                break;
            }
            if (item instanceof SpecialBroadcastItem broadcastItem) {
                if (i == 0) {
                    // if we meet special item first, just forward it and stop draining iteration.
                    dest.accept(item);
                } else {
                    // here, if we meet special item after normal items, stop draining iteration without skipping.
                    pendingItem = broadcastItem;
                }
                break;
            } else {
                dest.accept(item);
            }
        }
        return done ? DONE : MADE_PROGRESS;
    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public int ordinal() {
        return ordinal;
    }

    @Override
    public int priority() {
        return priority;
    }

    public Deque<Object> remainingItems() {
        return mockData;
    }

    @Override
    public int sizes() {
        return mockData.size();
    }

    @Override
    public int capacities() {
        return Integer.MAX_VALUE;
    }
}
