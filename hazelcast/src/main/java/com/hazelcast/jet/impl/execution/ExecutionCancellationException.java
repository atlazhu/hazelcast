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

import java.io.Serial;
import java.util.concurrent.CancellationException;

public class ExecutionCancellationException extends CancellationException {
    @Serial
    private static final long serialVersionUID = 1L;

    private final Throwable cause;

    public ExecutionCancellationException(Throwable cause) {
        super(cause.getMessage());
        this.cause = cause;
    }

    @Override
    public synchronized Throwable getCause() {
        return cause;
    }
}
