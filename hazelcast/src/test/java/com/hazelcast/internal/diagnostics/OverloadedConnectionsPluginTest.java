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

package com.hazelcast.internal.diagnostics;

import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.config.TcpIpConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.internal.nio.Packet;
import com.hazelcast.internal.serialization.InternalSerializationService;
import com.hazelcast.map.IMap;
import com.hazelcast.map.impl.operation.GetOperation;
import com.hazelcast.spi.impl.operationservice.impl.DummyOperation;
import com.hazelcast.spi.impl.operationservice.impl.operations.Backup;
import com.hazelcast.spi.properties.ClusterProperty;
import com.hazelcast.test.HazelcastSerialClassRunner;
import com.hazelcast.test.annotation.SlowTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import static com.hazelcast.test.Accessors.getAddress;
import static com.hazelcast.test.Accessors.getNodeEngineImpl;
import static com.hazelcast.test.Accessors.getSerializationService;
import static org.junit.Assert.assertEquals;

/**
 * This test can't run with mocked Hazelcast instances, since we rely on a real TcpIpConnectionManager.
 */
@RunWith(HazelcastSerialClassRunner.class)
@Category(SlowTest.class)
public class OverloadedConnectionsPluginTest extends AbstractDiagnosticsPluginTest {

    private HazelcastInstance local;
    private InternalSerializationService serializationService;
    private OverloadedConnectionsPlugin plugin;
    private String remoteKey;

    private volatile boolean stop;

    @Before
    public void setup() {
        Hazelcast.shutdownAll();

        Config config = new Config()
                .setProperty(OverloadedConnectionsPlugin.PERIOD_SECONDS.getName(), "1")
                .setProperty(OverloadedConnectionsPlugin.SAMPLES.getName(), "10")
                .setProperty(OverloadedConnectionsPlugin.THRESHOLD.getName(), "2")
                .setProperty(ClusterProperty.IO_OUTPUT_THREAD_COUNT.getName(), "1")
                .setProperty(ClusterProperty.SOCKET_BIND_ANY.getName(), "false");

        NetworkConfig networkConfig = config.getNetworkConfig();
        networkConfig.getInterfaces().setEnabled(true).addInterface("127.0.0.1");
        JoinConfig joinConfig = networkConfig.getJoin();
        joinConfig.getMulticastConfig().setEnabled(false);
        TcpIpConfig tcpIpConfig = joinConfig.getTcpIpConfig().setEnabled(true);
        tcpIpConfig.addMember("127.0.0.1:5701");
        tcpIpConfig.addMember("127.0.0.1:5702");

        local = Hazelcast.newHazelcastInstance(config);
        serializationService = getSerializationService(local);
        HazelcastInstance remote = Hazelcast.newHazelcastInstance(config);

        assertClusterSizeEventually(2, local, remote);

        plugin = new OverloadedConnectionsPlugin(getNodeEngineImpl(local));
        plugin.onStart();

        warmUpPartitions(local, remote);
        remoteKey = generateKeyOwnedBy(remote);
    }

    @After
    public void tearDown() {
        stop = true;
        Hazelcast.shutdownAll();
    }

    @Test
    public void test() {
        spawn(() -> {
            IMap<String, String> map = local.getMap(getClass().getName());
            while (!stop) {
                map.getAsync(remoteKey);
            }
        });

        assertTrueEventually(() -> {
            plugin.run(logWriter);

            assertContains(GetOperation.class.getSimpleName() + " sampleCount=");
        });
    }

    @Test
    @SuppressWarnings("UnnecessaryBoxing")
    public void toKey() {
        assertToKey(DummyOperation.class.getName(), new DummyOperation());
        assertToKey(Integer.class.getName(), Integer.valueOf(10));
        assertToKey("Backup(" + DummyOperation.class.getName() + ")",
                new Backup(new DummyOperation(), getAddress(local), new long[0], true));
    }

    private void assertToKey(String key, Object object) {
        Packet packet = new Packet(serializationService.toBytes(object));
        assertEquals(key, plugin.toKey(packet));
    }
}
