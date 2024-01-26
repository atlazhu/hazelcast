/*
 * Copyright (c) 2008-2024, Hazelcast, Inc. All Rights Reserved.
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

package com.hazelcast.internal.tpcengine.net;

import com.hazelcast.internal.tpcengine.Reactor;
import com.hazelcast.internal.tpcengine.ReactorBuilder;
import com.hazelcast.internal.tpcengine.util.CloseUtil;
import org.junit.After;
import org.junit.Test;

import java.io.UncheckedIOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import static com.hazelcast.internal.tpcengine.TpcTestSupport.assertCompletesEventually;
import static com.hazelcast.internal.tpcengine.TpcTestSupport.assertTrueEventually;
import static com.hazelcast.internal.tpcengine.TpcTestSupport.terminate;
import static com.hazelcast.internal.tpcengine.TpcTestSupport.terminateAll;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public abstract class AsyncServerSocketTest {

    private final Collection<Reactor> reactors = new ArrayList<>();

    public abstract ReactorBuilder newReactorBuilder();

    private Reactor newReactor() {
        ReactorBuilder reactorBuilder = newReactorBuilder();
        Reactor reactor = reactorBuilder.build();
        reactors.add(reactor);
        return reactor.start();
    }

    private static AsyncServerSocket newAsyncServerSocket(Reactor reactor) {
        return reactor.newAsyncServerSocketBuilder()
                .setAcceptConsumer(acceptRequest -> {
                })
                .build();
    }

    @After
    public void after() {
        terminateAll(reactors);
    }

    @Test
    public void test_construction() {
        Reactor reactor = newReactor();
        try (AsyncServerSocket socket = newAsyncServerSocket(reactor)) {
            assertSame(reactor, socket.getReactor());
            assertNotNull(socket.metrics());
        }
    }

    @Test
    public void test_getLocalPort_whenNotYetBound() {
        Reactor reactor = newReactor();
        try (AsyncServerSocket socket = newAsyncServerSocket(reactor)) {
            int localPort = socket.getLocalPort();
            assertEquals(-1, localPort);
        }
    }

    @Test
    public void test_bind_whenLocalAddressNull() {
        Reactor reactor = newReactor();
        try (AsyncServerSocket socket = newAsyncServerSocket(reactor)) {
            assertThrows(NullPointerException.class, () -> socket.bind(null));
        }
    }

    @Test
    public void test_getLocalAddress_whenNotBound() {
        Reactor reactor = newReactor();
        try (AsyncServerSocket socket = newAsyncServerSocket(reactor)) {
            assertNull(socket.getLocalAddress());
        }
    }

    @Test
    public void test_server_andNoBind() {
        Reactor reactor = newReactor();
        try (AsyncServerSocket socket = newAsyncServerSocket(reactor)) {
            socket.start();
        }
    }

    @Test
    public void test_bind_whenBacklogNegative() {
        Reactor reactor = newReactor();
        try (AsyncServerSocket socket = newAsyncServerSocket(reactor)) {
            SocketAddress localAddress = new InetSocketAddress("127.0.0.1", 0);

            assertThrows(IllegalArgumentException.class, () -> socket.bind(localAddress, -1));
        }
    }

    @Test
    public void test_bind() {
        Reactor reactor = newReactor();
        try (AsyncServerSocket socket = reactor.newAsyncServerSocketBuilder()
                .setAcceptConsumer(acceptRequest -> {
                })
                .build()) {
            InetSocketAddress local = new InetSocketAddress("127.0.0.1", 5000);
            socket.bind(local);

            assertEquals(local, socket.getLocalAddress());
            assertEquals(local.getPort(), socket.getLocalPort());
        }
    }

    @Test
    public void test_bind_whenAlreadyBound() {
        Reactor reactor = newReactor();
        try (AsyncServerSocket socket = newAsyncServerSocket(reactor)) {
            InetSocketAddress local = new InetSocketAddress("127.0.0.1", 0);
            socket.bind(local);

            assertThrows(UncheckedIOException.class, () -> socket.bind(local));
        }
    }

    @Test
    public void test_connect() {
        Reactor reactor = newReactor();
        try (AsyncServerSocket serverSocket = reactor.newAsyncServerSocketBuilder()
                .setAcceptConsumer(acceptRequest -> {
                    AsyncSocket socket = reactor.newAsyncSocketBuilder(acceptRequest)
                            .setReader(new DevNullAsyncSocketReader())
                            .build();
                    socket.start();
                })
                .build()) {
            serverSocket.bind(new InetSocketAddress("127.0.0.1", 0));
            serverSocket.start();

            int clients = 5;
            for (int k = 0; k < clients; k++) {
                AsyncSocket clientSocket = reactor.newAsyncSocketBuilder()
                        .setReader(new DevNullAsyncSocketReader())
                        .build();
                clientSocket.start();

                CompletableFuture<Void> connect = clientSocket.connect(serverSocket.getLocalAddress());
                assertCompletesEventually(connect);
            }

            assertEquals(clients, serverSocket.metrics.accepted());
        }
    }

    @Test
    public void test_accept_withException() {
        Reactor reactor = newReactor();
        try (AsyncServerSocket serverSocket = reactor.newAsyncServerSocketBuilder()
                .setAcceptConsumer(acceptRequest -> {
                    throw new RuntimeException();
                })
                .build()) {
            serverSocket.bind(new InetSocketAddress("127.0.0.1", 0));
            serverSocket.start();

            AsyncSocket clientSocket = reactor.newAsyncSocketBuilder()
                    .setReader(new DevNullAsyncSocketReader())
                    .build();
            clientSocket.start();

            CompletableFuture<Void> connect = clientSocket.connect(serverSocket.getLocalAddress());
            assertCompletesEventually(connect);
            assertTrueEventually(() -> assertTrue(clientSocket.isClosed()));
        }
    }

    @Test
    public void test_acceptWithExplicitClose() {
        Reactor reactor = newReactor();
        SocketAddress serverAddress;
        try (AsyncServerSocket serverSocket = reactor.newAsyncServerSocketBuilder()
                .setAcceptConsumer(CloseUtil::closeQuietly)
                .build()) {
            serverSocket.bind(new InetSocketAddress("127.0.0.1", 0));
            serverSocket.start();
            serverAddress = serverSocket.getLocalAddress();
        }

        AsyncSocket clientSocket = reactor.newAsyncSocketBuilder()
                .setReader(new DevNullAsyncSocketReader())
                .build();
        clientSocket.start();

        CompletableFuture<Void> connect = clientSocket.connect(serverAddress);
        assertCompletesEventually(connect);
        assertTrueEventually(() -> assertTrue(clientSocket.isClosed()));
    }

    @Test
    public void test_createCloseLoop_withSameReactor() {
        Reactor reactor = newReactor();
        SocketAddress local = new InetSocketAddress("127.0.0.1", 5001);
        for (int k = 0; k < 1000; k++) {
            try (AsyncServerSocket serverSocket = reactor.newAsyncServerSocketBuilder()
                    .setAcceptConsumer(acceptRequest -> {
                        AsyncSocket clientSocket = reactor.newAsyncSocketBuilder(acceptRequest)
                                .setReader(new DevNullAsyncSocketReader())
                                .build();
                        clientSocket.start();
                    })
                    .build()) {
                serverSocket.bind(local);
                serverSocket.start();
            }
        }
    }

    @Test
    public void test_createCloseLoop_withNewReactor() {
        SocketAddress local = new InetSocketAddress("127.0.0.1", 5003);
        for (int k = 0; k < 1000; k++) {
            Reactor reactor = newReactor();
            try (AsyncServerSocket serverSocket = reactor.newAsyncServerSocketBuilder()
                    .setAcceptConsumer(acceptRequest -> {
                        AsyncSocket clientSocket = reactor.newAsyncSocketBuilder(acceptRequest)
                                .setReader(new DevNullAsyncSocketReader())
                                .build();
                        clientSocket.start();
                    })
                    .build()) {
                serverSocket.bind(local);
                serverSocket.start();
                terminate(reactor);
                reactors.remove(reactor);
            }
        }
    }
}
