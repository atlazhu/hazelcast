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

package com.hazelcast.spring;

import com.hazelcast.cluster.Member;
import com.hazelcast.cluster.MembershipListener;
import com.hazelcast.collection.IList;
import com.hazelcast.collection.IQueue;
import com.hazelcast.collection.ISet;
import com.hazelcast.collection.QueueStore;
import com.hazelcast.collection.QueueStoreFactory;
import com.hazelcast.config.AttributeConfig;
import com.hazelcast.config.AuditlogConfig;
import com.hazelcast.config.AwsConfig;
import com.hazelcast.config.AzureConfig;
import com.hazelcast.config.CRDTReplicationConfig;
import com.hazelcast.config.CacheDeserializedValues;
import com.hazelcast.config.CacheSimpleConfig;
import com.hazelcast.config.CardinalityEstimatorConfig;
import com.hazelcast.config.ClassFilter;
import com.hazelcast.config.CompactSerializationConfig;
import com.hazelcast.config.CompactSerializationConfigAccessor;
import com.hazelcast.config.Config;
import com.hazelcast.config.ConsistencyCheckStrategy;
import com.hazelcast.config.DataConnectionConfig;
import com.hazelcast.config.DiscoveryConfig;
import com.hazelcast.config.DiscoveryStrategyConfig;
import com.hazelcast.config.DiskTierConfig;
import com.hazelcast.config.DurableExecutorConfig;
import com.hazelcast.config.DynamicConfigurationConfig;
import com.hazelcast.config.EncryptionAtRestConfig;
import com.hazelcast.config.EntryListenerConfig;
import com.hazelcast.config.EurekaConfig;
import com.hazelcast.config.EventJournalConfig;
import com.hazelcast.config.EvictionConfig;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.ExecutorConfig;
import com.hazelcast.config.FlakeIdGeneratorConfig;
import com.hazelcast.config.GcpConfig;
import com.hazelcast.config.GlobalSerializerConfig;
import com.hazelcast.config.IcmpFailureDetectorConfig;
import com.hazelcast.config.InMemoryFormat;
import com.hazelcast.config.IndexConfig;
import com.hazelcast.config.IndexType;
import com.hazelcast.config.InstanceTrackingConfig;
import com.hazelcast.config.IntegrityCheckerConfig;
import com.hazelcast.config.ItemListenerConfig;
import com.hazelcast.config.JavaSerializationFilterConfig;
import com.hazelcast.config.KubernetesConfig;
import com.hazelcast.config.ListConfig;
import com.hazelcast.config.ListenerConfig;
import com.hazelcast.config.LocalDeviceConfig;
import com.hazelcast.config.ManagementCenterConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MapPartitionLostListenerConfig;
import com.hazelcast.config.MapStoreConfig;
import com.hazelcast.config.MaxSizePolicy;
import com.hazelcast.config.MemberAddressProviderConfig;
import com.hazelcast.config.MemberAttributeConfig;
import com.hazelcast.config.MemberGroupConfig;
import com.hazelcast.config.MemcacheProtocolConfig;
import com.hazelcast.config.MemoryTierConfig;
import com.hazelcast.config.MergePolicyConfig;
import com.hazelcast.config.MetadataPolicy;
import com.hazelcast.config.MetricsConfig;
import com.hazelcast.config.MultiMapConfig;
import com.hazelcast.config.NativeMemoryConfig;
import com.hazelcast.config.NearCacheConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.config.OnJoinPermissionOperationName;
import com.hazelcast.config.PNCounterConfig;
import com.hazelcast.config.PartitionGroupConfig;
import com.hazelcast.config.PartitioningAttributeConfig;
import com.hazelcast.config.PermissionConfig;
import com.hazelcast.config.PermissionConfig.PermissionType;
import com.hazelcast.config.PersistenceConfig;
import com.hazelcast.config.PersistentMemoryDirectoryConfig;
import com.hazelcast.config.QueryCacheConfig;
import com.hazelcast.config.QueueConfig;
import com.hazelcast.config.QueueStoreConfig;
import com.hazelcast.config.ReliableTopicConfig;
import com.hazelcast.config.ReplicatedMapConfig;
import com.hazelcast.config.RestApiConfig;
import com.hazelcast.config.RestEndpointGroup;
import com.hazelcast.config.RingbufferConfig;
import com.hazelcast.config.RingbufferStoreConfig;
import com.hazelcast.config.SSLConfig;
import com.hazelcast.config.ScheduledExecutorConfig;
import com.hazelcast.config.SecurityConfig;
import com.hazelcast.config.SerializationConfig;
import com.hazelcast.config.SerializerConfig;
import com.hazelcast.config.SetConfig;
import com.hazelcast.config.SocketInterceptorConfig;
import com.hazelcast.config.SplitBrainProtectionConfig;
import com.hazelcast.config.SqlConfig;
import com.hazelcast.config.TcpIpConfig;
import com.hazelcast.config.TieredStoreConfig;
import com.hazelcast.config.TopicConfig;
import com.hazelcast.config.VaultSecureStoreConfig;
import com.hazelcast.config.WanAcknowledgeType;
import com.hazelcast.config.WanBatchPublisherConfig;
import com.hazelcast.config.WanConsumerConfig;
import com.hazelcast.config.WanCustomPublisherConfig;
import com.hazelcast.config.WanQueueFullBehavior;
import com.hazelcast.config.WanReplicationConfig;
import com.hazelcast.config.WanReplicationRef;
import com.hazelcast.config.WanSyncConfig;
import com.hazelcast.config.cp.CPMapConfig;
import com.hazelcast.config.cp.CPSubsystemConfig;
import com.hazelcast.config.cp.FencedLockConfig;
import com.hazelcast.config.cp.RaftAlgorithmConfig;
import com.hazelcast.config.cp.SemaphoreConfig;
import com.hazelcast.config.security.AccessControlServiceConfig;
import com.hazelcast.config.security.KerberosAuthenticationConfig;
import com.hazelcast.config.security.KerberosIdentityConfig;
import com.hazelcast.config.security.RealmConfig;
import com.hazelcast.config.security.SimpleAuthenticationConfig;
import com.hazelcast.config.tpc.TpcConfig;
import com.hazelcast.config.tpc.TpcSocketConfig;
import com.hazelcast.config.vector.Metric;
import com.hazelcast.config.vector.VectorCollectionConfig;
import com.hazelcast.config.vector.VectorIndexConfig;
import com.hazelcast.core.EntryListener;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.crdt.pncounter.PNCounter;
import com.hazelcast.flakeidgen.FlakeIdGenerator;
import com.hazelcast.instance.impl.HazelcastInstanceFactory;
import com.hazelcast.jet.JetService;
import com.hazelcast.jet.config.EdgeConfig;
import com.hazelcast.jet.config.JetConfig;
import com.hazelcast.map.IMap;
import com.hazelcast.map.MapStore;
import com.hazelcast.map.MapStoreFactory;
import com.hazelcast.memory.Capacity;
import com.hazelcast.memory.MemoryUnit;
import com.hazelcast.multimap.MultiMap;
import com.hazelcast.nio.SocketInterceptor;
import com.hazelcast.nio.serialization.DataSerializableFactory;
import com.hazelcast.nio.serialization.PortableFactory;
import com.hazelcast.nio.serialization.StreamSerializer;
import com.hazelcast.nio.ssl.SSLContextFactory;
import com.hazelcast.replicatedmap.ReplicatedMap;
import com.hazelcast.ringbuffer.RingbufferStore;
import com.hazelcast.ringbuffer.RingbufferStoreFactory;
import com.hazelcast.spi.properties.ClusterProperty;
import com.hazelcast.splitbrainprotection.SplitBrainProtectionOn;
import com.hazelcast.splitbrainprotection.impl.ProbabilisticSplitBrainProtectionFunction;
import com.hazelcast.splitbrainprotection.impl.RecentlyActiveSplitBrainProtectionFunction;
import com.hazelcast.spring.serialization.DummyCompactSerializer;
import com.hazelcast.spring.serialization.DummyDataSerializableFactory;
import com.hazelcast.spring.serialization.DummyPortableFactory;
import com.hazelcast.spring.serialization.DummyReflectiveSerializable;
import com.hazelcast.test.HazelcastTestSupport;
import com.hazelcast.topic.ITopic;
import com.hazelcast.topic.TopicOverloadPolicy;
import com.hazelcast.wan.WanPublisher;
import com.hazelcast.wan.WanPublisherState;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.File;
import java.net.InetSocketAddress;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import static com.hazelcast.config.MaxSizePolicy.USED_NATIVE_MEMORY_PERCENTAGE;
import static com.hazelcast.config.PersistenceClusterDataRecoveryPolicy.PARTIAL_RECOVERY_MOST_COMPLETE;
import static com.hazelcast.dataconnection.impl.DataConnectionTestUtil.DUMMY_TYPE;
import static com.hazelcast.internal.util.CollectionUtil.isNotEmpty;
import static com.hazelcast.jet.impl.JetServiceBackend.SQL_CATALOG_MAP_NAME;
import static com.hazelcast.memory.MemoryUnit.GIGABYTES;
import static com.hazelcast.spi.properties.ClusterProperty.MERGE_FIRST_RUN_DELAY_SECONDS;
import static com.hazelcast.spi.properties.ClusterProperty.MERGE_NEXT_RUN_DELAY_SECONDS;
import static com.hazelcast.spi.properties.ClusterProperty.PARTITION_COUNT;
import static java.lang.Boolean.TRUE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;


@ExtendWith({SpringExtension.class, CustomSpringExtension.class})
@ContextConfiguration(locations = {"fullConfig-applicationContext-hazelcast.xml"})
@SuppressWarnings("unused")
public class TestFullApplicationContext extends HazelcastTestSupport {

    public static final String INTERNAL_JET_OBJECTS_PREFIX = "__jet.";

    private Config config;

    @Autowired
    private HazelcastInstance instance;

    @Autowired
    private JetService jet;

    @Autowired
    @Qualifier(value = "map1")
    private IMap<Object, Object> map1;

    @Autowired
    @Qualifier(value = "map2")
    private IMap<Object, Object> map2;

    @Autowired
    private MultiMap<Object, Object> multiMap;

    @Autowired
    @Qualifier(value = "replicatedMap")
    private ReplicatedMap<Object, Object> replicatedMap;

    @Autowired
    @Qualifier(value = "queue")
    private IQueue<?> queue;

    @Autowired
    @Qualifier(value = "topic")
    private ITopic<?> topic;

    @Autowired
    @Qualifier(value = "set")
    private ISet<?> set;

    @Autowired
    @Qualifier(value = "list")
    private IList<?> list;

    @Autowired
    private ExecutorService executorService;

    @Autowired
    private FlakeIdGenerator flakeIdGenerator;

    @Autowired
    private MapStore<Object, Object> dummyMapStore;

    @Autowired
    private MapStoreFactory<Object, Object> dummyMapStoreFactory;

    @Autowired
    private QueueStore<Object> dummyQueueStore;

    @Autowired
    private QueueStoreFactory<Object> dummyQueueStoreFactory;

    @Autowired
    private RingbufferStore<Object> dummyRingbufferStore;

    @Autowired
    private RingbufferStoreFactory<Object> dummyRingbufferStoreFactory;

    @Autowired
    private WanPublisher<Object> wanReplication;

    @Autowired
    private MembershipListener membershipListener;

    @Autowired
    private EntryListener<Object, Object> entryListener;

    @Autowired
    private SSLContextFactory sslContextFactory;

    @Autowired
    private SocketInterceptor socketInterceptor;

    @Autowired
    private StreamSerializer dummySerializer;

    @Autowired
    private PNCounter pnCounter;

    @BeforeAll
    static void start() {
        // OverridePropertyRule can't be used here since the Spring context
        // with the Hazelcast instance is created before the rules
        System.clearProperty(ClusterProperty.METRICS_COLLECTION_FREQUENCY.getName());
        HazelcastInstanceFactory.terminateAll();
    }


    @AfterAll
    static void stop() {
        HazelcastInstanceFactory.terminateAll();
        System.setProperty(ClusterProperty.METRICS_COLLECTION_FREQUENCY.getName(), "1");
    }

    @BeforeEach
    void before() {
        config = instance.getConfig();
    }

    @Test
    void testCacheConfig() {
        assertNotNull(config);
        assertEquals(1, config.getCacheConfigs().size());
        CacheSimpleConfig cacheConfig = config.getCacheConfig("testCache");
        assertEquals("testCache", cacheConfig.getName());
        assertTrue(cacheConfig.isDisablePerEntryInvalidationEvents());
        assertTrue(cacheConfig.getDataPersistenceConfig().isEnabled());
        assertTrue(cacheConfig.getDataPersistenceConfig().isFsync());
        assertEquals("ns1", cacheConfig.getUserCodeNamespace());

        EventJournalConfig journalConfig = cacheConfig.getEventJournalConfig();
        assertTrue(journalConfig.isEnabled());
        assertEquals(123, journalConfig.getCapacity());
        assertEquals(321, journalConfig.getTimeToLiveSeconds());

        WanReplicationRef wanRef = cacheConfig.getWanReplicationRef();
        assertEquals("testWan", wanRef.getName());
        assertEquals("PutIfAbsentMergePolicy", wanRef.getMergePolicyClassName());
        assertEquals(1, wanRef.getFilters().size());
        assertEquals("com.example.SampleFilter", wanRef.getFilters().get(0));
        assertEquals(Boolean.TRUE, cacheConfig.getMerkleTreeConfig().getEnabled());
        assertEquals(20, cacheConfig.getMerkleTreeConfig().getDepth());
    }

    @Test
    void testMapConfig() {
        assertNotNull(config);
        long mapConfigSize = config.getMapConfigs()
                .keySet().stream()
                .filter(name -> !name.startsWith(INTERNAL_JET_OBJECTS_PREFIX))
                .filter(name -> !name.equals(SQL_CATALOG_MAP_NAME))
                .count();
        assertEquals(28, mapConfigSize);

        MapConfig testMapConfig = config.getMapConfig("testMap");
        assertNotNull(testMapConfig);
        assertEquals("testMap", testMapConfig.getName());
        assertEquals(2, testMapConfig.getBackupCount());
        assertEquals(EvictionPolicy.NONE, testMapConfig.getEvictionConfig().getEvictionPolicy());
        assertEquals(Integer.MAX_VALUE, testMapConfig.getEvictionConfig().getSize());
        assertEquals(0, testMapConfig.getTimeToLiveSeconds());
        assertEquals(Boolean.TRUE, testMapConfig.getMerkleTreeConfig().getEnabled());
        assertEquals(20, testMapConfig.getMerkleTreeConfig().getDepth());
        assertTrue(testMapConfig.getDataPersistenceConfig().isEnabled());
        assertTrue(testMapConfig.getDataPersistenceConfig().isFsync());
        EventJournalConfig journalConfig = testMapConfig.getEventJournalConfig();
        assertTrue(journalConfig.isEnabled());
        assertEquals(123, journalConfig.getCapacity());
        assertEquals(321, journalConfig.getTimeToLiveSeconds());
        assertEquals(MetadataPolicy.OFF, testMapConfig.getMetadataPolicy());
        assertTrue(testMapConfig.isReadBackupData());
        assertEquals("ns1", testMapConfig.getUserCodeNamespace());
        assertEquals(3, testMapConfig.getIndexConfigs().size());
        for (IndexConfig index : testMapConfig.getIndexConfigs()) {
            if ("name".equals(index.getAttributes().get(0))) {
                assertEquals(IndexType.HASH, index.getType());
                assertNull(index.getName());
            } else if ("sortedIndex".equals(index.getName())) {
                assertEquals(IndexType.SORTED, index.getType());
                assertEquals("age", index.getAttributes().get(0));
            } else if ("sortedIndexTiered".equals(index.getName())) {
                assertEquals(IndexType.SORTED, index.getType());
                assertEquals("age", index.getAttributes().get(0));
                assertEquals(Capacity.of(17, GIGABYTES), index.getBTreeIndexConfig().getPageSize());
                assertEquals(Capacity.of(129, GIGABYTES),
                        index.getBTreeIndexConfig().getMemoryTierConfig().getCapacity());
            } else {
                fail("unknown index!");
            }
        }
        assertEquals(2, testMapConfig.getAttributeConfigs().size());
        for (AttributeConfig attribute : testMapConfig.getAttributeConfigs()) {
            if ("power".equals(attribute.getName())) {
                assertEquals("com.car.PowerExtractor", attribute.getExtractorClassName());
            } else if ("weight".equals(attribute.getName())) {
                assertEquals("com.car.WeightExtractor", attribute.getExtractorClassName());
            } else {
                fail("unknown attribute!");
            }
        }
        assertEquals("my-split-brain-protection", testMapConfig.getSplitBrainProtectionName());
        MergePolicyConfig mergePolicyConfig = testMapConfig.getMergePolicyConfig();
        assertNotNull(mergePolicyConfig);
        assertEquals("PassThroughMergePolicy", mergePolicyConfig.getPolicy());
        assertEquals(2342, mergePolicyConfig.getBatchSize());

        // test that the testMapConfig has a mapStoreConfig and it is correct
        MapStoreConfig testMapStoreConfig = testMapConfig.getMapStoreConfig();
        assertNotNull(testMapStoreConfig);
        assertEquals("com.hazelcast.spring.DummyStore", testMapStoreConfig.getClassName());
        assertTrue(testMapStoreConfig.isEnabled());
        assertEquals(0, testMapStoreConfig.getWriteDelaySeconds());
        assertEquals(10, testMapStoreConfig.getWriteBatchSize());
        assertTrue(testMapStoreConfig.isWriteCoalescing());
        assertFalse(testMapStoreConfig.isOffload());
        assertEquals(MapStoreConfig.InitialLoadMode.EAGER, testMapStoreConfig.getInitialLoadMode());

        // test that the testMapConfig has a nearCacheConfig and it is correct
        NearCacheConfig testNearCacheConfig = testMapConfig.getNearCacheConfig();
        assertNotNull(testNearCacheConfig);
        assertEquals(0, testNearCacheConfig.getTimeToLiveSeconds());
        assertEquals(60, testNearCacheConfig.getMaxIdleSeconds());
        assertEquals(EvictionPolicy.LRU, testNearCacheConfig.getEvictionConfig().getEvictionPolicy());
        assertEquals(5000, testNearCacheConfig.getEvictionConfig().getSize());
        assertTrue(testNearCacheConfig.isInvalidateOnChange());
        assertFalse(testNearCacheConfig.isSerializeKeys());

        // test that the testMapConfig2's mapStoreConfig implementation
        MapConfig testMapConfig2 = config.getMapConfig("testMap2");
        assertNotNull(testMapConfig2.getMapStoreConfig().getImplementation());
        assertEquals(dummyMapStore, testMapConfig2.getMapStoreConfig().getImplementation());
        assertEquals(MapStoreConfig.InitialLoadMode.LAZY, testMapConfig2.getMapStoreConfig().getInitialLoadMode());

        // test testMapConfig2's WanReplicationConfig
        WanReplicationRef wanReplicationRef = testMapConfig2.getWanReplicationRef();
        assertEquals("testWan", wanReplicationRef.getName());
        assertEquals("PutIfAbsentMergePolicy", wanReplicationRef.getMergePolicyClassName());
        assertTrue(wanReplicationRef.isRepublishingEnabled());

        assertEquals(1000, testMapConfig2.getEvictionConfig().getSize());
        assertEquals(MaxSizePolicy.PER_NODE, testMapConfig2.getEvictionConfig().getMaxSizePolicy());
        assertEquals(2, testMapConfig2.getEntryListenerConfigs().size());
        for (EntryListenerConfig listener : testMapConfig2.getEntryListenerConfigs()) {
            if (listener.getClassName() != null) {
                assertNull(listener.getImplementation());
                assertTrue(listener.isIncludeValue());
                assertFalse(listener.isLocal());
            } else {
                assertNotNull(listener.getImplementation());
                assertEquals(entryListener, listener.getImplementation());
                assertTrue(listener.isLocal());
                assertTrue(listener.isIncludeValue());
            }
        }

        MapConfig simpleMapConfig = config.getMapConfig("simpleMap");
        assertNotNull(simpleMapConfig);
        assertEquals("simpleMap", simpleMapConfig.getName());
        assertEquals(3, simpleMapConfig.getBackupCount());
        assertEquals(1, simpleMapConfig.getAsyncBackupCount());
        assertEquals(EvictionPolicy.LRU, simpleMapConfig.getEvictionConfig().getEvictionPolicy());
        assertEquals(10, simpleMapConfig.getEvictionConfig().getSize());
        assertEquals(1, simpleMapConfig.getTimeToLiveSeconds());

        // test that the simpleMapConfig does NOT have a nearCacheConfig
        assertNull(simpleMapConfig.getNearCacheConfig());

        MapConfig testMapConfig3 = config.getMapConfig("testMap3");
        assertEquals("com.hazelcast.spring.DummyStoreFactory", testMapConfig3.getMapStoreConfig().getFactoryClassName());
        assertFalse(testMapConfig3.getMapStoreConfig().getProperties().isEmpty());
        assertEquals("value", testMapConfig3.getMapStoreConfig().getProperty("dummy.property"));

        MapConfig testMapConfig4 = config.getMapConfig("testMap4");
        assertEquals(dummyMapStoreFactory, testMapConfig4.getMapStoreConfig().getFactoryImplementation());

        MapConfig mapWithValueCachingSetToNever = config.getMapConfig("mapWithValueCachingSetToNever");
        assertEquals(CacheDeserializedValues.NEVER, mapWithValueCachingSetToNever.getCacheDeserializedValues());

        MapConfig mapWithValueCachingSetToAlways = config.getMapConfig("mapWithValueCachingSetToAlways");
        assertEquals(CacheDeserializedValues.ALWAYS, mapWithValueCachingSetToAlways.getCacheDeserializedValues());

        MapConfig mapWithDefaultValueCaching = config.getMapConfig("mapWithDefaultValueCaching");
        assertEquals(CacheDeserializedValues.INDEX_ONLY, mapWithDefaultValueCaching.getCacheDeserializedValues());

        MapConfig testMapWithPartitionLostListenerConfig = config.getMapConfig("mapWithPartitionLostListener");
        List<MapPartitionLostListenerConfig> partitionLostListenerConfigs
                = testMapWithPartitionLostListenerConfig.getPartitionLostListenerConfigs();
        assertEquals(1, partitionLostListenerConfigs.size());
        assertEquals("DummyMapPartitionLostListenerImpl", partitionLostListenerConfigs.get(0).getClassName());

        MapConfig testMapWithPartitionStrategyConfig = config.getMapConfig("mapWithPartitionStrategy");
        assertEquals("com.hazelcast.spring.DummyPartitionStrategy",
                testMapWithPartitionStrategyConfig.getPartitioningStrategyConfig().getPartitioningStrategyClass());

        MapConfig testMapConfig5 = config.getMapConfig("testMap5");
        TieredStoreConfig tieredStoreConfig = testMapConfig5.getTieredStoreConfig();
        assertTrue(tieredStoreConfig.isEnabled());
        MemoryTierConfig memoryTierConfig = tieredStoreConfig.getMemoryTierConfig();
        assertEquals(MemoryUnit.MEGABYTES, memoryTierConfig.getCapacity().getUnit());
        assertEquals(128L, memoryTierConfig.getCapacity().getValue());

        DiskTierConfig diskTierConfig = tieredStoreConfig.getDiskTierConfig();
        assertTrue(diskTierConfig.isEnabled());
        assertEquals("the-local0751", diskTierConfig.getDeviceName());

        final List<PartitioningAttributeConfig> attributeConfigs = Arrays.asList(
                new PartitioningAttributeConfig("attr1"),
                new PartitioningAttributeConfig("attr2")
        );
        MapConfig testMapWithPartitionAttributes = config.getMapConfig("mapWithPartitionAttributes");
        assertEquals(attributeConfigs, testMapWithPartitionAttributes.getPartitioningAttributeConfigs());
    }

    @Test
    void testMapNoWanMergePolicy() {
        MapConfig testMapConfig2 = config.getMapConfig("testMap2");


        // test testMapConfig2's WanReplicationConfig
        WanReplicationRef wanReplicationRef = testMapConfig2.getWanReplicationRef();
        assertEquals("testWan", wanReplicationRef.getName());
        assertEquals("PutIfAbsentMergePolicy", wanReplicationRef.getMergePolicyClassName());
    }

    @Test
    void testMemberFlakeIdGeneratorConfig() {
        FlakeIdGeneratorConfig c = instance.getConfig().findFlakeIdGeneratorConfig("flakeIdGenerator");
        assertEquals(3, c.getPrefetchCount());
        assertEquals(10L, c.getPrefetchValidityMillis());
        assertEquals(30L, c.getNodeIdOffset());
        assertEquals(22, c.getBitsSequence());
        assertEquals(33, c.getBitsNodeId());
        assertEquals(20000L, c.getAllowedFutureMillis());
        assertFalse(c.isStatisticsEnabled());
        assertEquals("flakeIdGenerator*", c.getName());
        assertFalse(c.isStatisticsEnabled());
    }

    @Test
    void testQueueConfig() {
        QueueConfig testQConfig = config.getQueueConfig("testQ");
        assertNotNull(testQConfig);
        assertEquals("testQ", testQConfig.getName());
        assertEquals(1000, testQConfig.getMaxSize());
        assertEquals(1, testQConfig.getItemListenerConfigs().size());
        assertTrue(testQConfig.isStatisticsEnabled());
        assertEquals("ns1", testQConfig.getUserCodeNamespace());
        ItemListenerConfig listenerConfig = testQConfig.getItemListenerConfigs().get(0);
        assertEquals("com.hazelcast.spring.DummyItemListener", listenerConfig.getClassName());
        assertTrue(listenerConfig.isIncludeValue());

        QueueConfig qConfig = config.getQueueConfig("queueWithSplitBrainConfig");
        assertNotNull(qConfig);
        assertEquals("queueWithSplitBrainConfig", qConfig.getName());
        assertEquals(2500, qConfig.getMaxSize());
        assertFalse(qConfig.isStatisticsEnabled());
        assertEquals(100, qConfig.getEmptyQueueTtl());
        assertEquals("my-split-brain-protection", qConfig.getSplitBrainProtectionName());
        assertEquals("com.hazelcast.collection.impl.queue.model.PriorityElementComparator",
                qConfig.getPriorityComparatorClassName());
        MergePolicyConfig mergePolicyConfig = qConfig.getMergePolicyConfig();
        assertEquals("DiscardMergePolicy", mergePolicyConfig.getPolicy());
        assertEquals(2342, mergePolicyConfig.getBatchSize());

        QueueConfig queueWithStore1 = config.getQueueConfig("queueWithStore1");
        assertNotNull(queueWithStore1);
        QueueStoreConfig storeConfig1 = queueWithStore1.getQueueStoreConfig();
        assertNotNull(storeConfig1);
        assertEquals(DummyQueueStore.class.getName(), storeConfig1.getClassName());

        QueueConfig queueWithStore2 = config.getQueueConfig("queueWithStore2");
        assertNotNull(queueWithStore2);
        QueueStoreConfig storeConfig2 = queueWithStore2.getQueueStoreConfig();
        assertNotNull(storeConfig2);
        assertEquals(DummyQueueStoreFactory.class.getName(), storeConfig2.getFactoryClassName());

        QueueConfig queueWithStore3 = config.getQueueConfig("queueWithStore3");
        assertNotNull(queueWithStore3);
        QueueStoreConfig storeConfig3 = queueWithStore3.getQueueStoreConfig();
        assertNotNull(storeConfig3);
        assertEquals(dummyQueueStore, storeConfig3.getStoreImplementation());

        QueueConfig queueWithStore4 = config.getQueueConfig("queueWithStore4");
        assertNotNull(queueWithStore4);
        QueueStoreConfig storeConfig4 = queueWithStore4.getQueueStoreConfig();
        assertNotNull(storeConfig4);
        assertEquals(dummyQueueStoreFactory, storeConfig4.getFactoryImplementation());
    }

    @Test
    void testRingbufferConfig() {
        RingbufferConfig testRingbuffer = config.getRingbufferConfig("testRingbuffer");
        assertNotNull(testRingbuffer);
        assertEquals("testRingbuffer", testRingbuffer.getName());
        assertEquals(InMemoryFormat.OBJECT, testRingbuffer.getInMemoryFormat());
        assertEquals(100, testRingbuffer.getCapacity());
        assertEquals(1, testRingbuffer.getBackupCount());
        assertEquals(1, testRingbuffer.getAsyncBackupCount());
        assertEquals(20, testRingbuffer.getTimeToLiveSeconds());
        assertEquals("ns1", testRingbuffer.getUserCodeNamespace());
        RingbufferStoreConfig store1 = testRingbuffer.getRingbufferStoreConfig();
        assertNotNull(store1);
        assertEquals(DummyRingbufferStore.class.getName(), store1.getClassName());
        MergePolicyConfig mergePolicyConfig = testRingbuffer.getMergePolicyConfig();
        assertNotNull(mergePolicyConfig);
        assertEquals("PassThroughMergePolicy", mergePolicyConfig.getPolicy());
        assertEquals(2342, mergePolicyConfig.getBatchSize());

        RingbufferConfig testRingbuffer2 = config.getRingbufferConfig("testRingbuffer2");
        assertNotNull(testRingbuffer2);
        RingbufferStoreConfig store2 = testRingbuffer2.getRingbufferStoreConfig();
        assertNotNull(store2);
        assertEquals(DummyRingbufferStoreFactory.class.getName(), store2.getFactoryClassName());
        assertFalse(store2.getProperties().isEmpty());
        assertEquals("value", store2.getProperty("dummy.property"));
        assertEquals("value2", store2.getProperty("dummy.property.2"));

        RingbufferConfig testRingbuffer3 = config.getRingbufferConfig("testRingbuffer3");
        assertNotNull(testRingbuffer3);
        RingbufferStoreConfig store3 = testRingbuffer3.getRingbufferStoreConfig();
        assertNotNull(store3);
        assertEquals(dummyRingbufferStore, store3.getStoreImplementation());

        RingbufferConfig testRingbuffer4 = config.getRingbufferConfig("testRingbuffer4");
        assertNotNull(testRingbuffer4);
        RingbufferStoreConfig store4 = testRingbuffer4.getRingbufferStoreConfig();
        assertNotNull(store4);
        assertEquals(dummyRingbufferStoreFactory, store4.getFactoryImplementation());
    }

    @Test
    void testPNCounterConfig() {
        PNCounterConfig testPNCounter = config.getPNCounterConfig("testPNCounter");
        assertNotNull(testPNCounter);
        assertEquals("testPNCounter", testPNCounter.getName());
        assertEquals(100, testPNCounter.getReplicaCount());
        assertEquals("my-split-brain-protection", testPNCounter.getSplitBrainProtectionName());
        assertFalse(testPNCounter.isStatisticsEnabled());
    }

    @Test
    void testSecurity() {
        SecurityConfig securityConfig = config.getSecurityConfig();
        assertEquals(OnJoinPermissionOperationName.SEND, securityConfig.getOnJoinPermissionOperation());
        assertTrue(securityConfig.isPermissionPriorityGrant());
        final Set<PermissionConfig> clientPermissionConfigs = securityConfig.getClientPermissionConfigs();
        assertFalse(securityConfig.getClientBlockUnmappedActions());
        assertTrue(isNotEmpty(clientPermissionConfigs));
        assertEquals(PermissionType.values().length, clientPermissionConfigs.size());
        final PermissionConfig pnCounterPermission = new PermissionConfig(PermissionType.PN_COUNTER, "pnCounterPermission", "*")
                .addAction("create")
                .setEndpoints(Collections.emptySet());
        assertContains(clientPermissionConfigs, pnCounterPermission);

        PermissionConfig queuePermission = new PermissionConfig(PermissionType.QUEUE, "*", "*")
                .addAction("all");
        assertNotContains(clientPermissionConfigs, queuePermission);
        queuePermission.setDeny(true);
        assertContains(clientPermissionConfigs, queuePermission);

        Set<PermissionType> permTypes = new HashSet<>(Arrays.asList(PermissionType.values()));
        for (PermissionConfig pc : clientPermissionConfigs) {
            permTypes.remove(pc.getType());
        }
        assertTrue(permTypes.isEmpty(), "All permission types should be listed in fullConfig. Not found ones: " + permTypes);
        RealmConfig kerberosRealm = securityConfig.getRealmConfig("kerberosRealm");
        assertNotNull(kerberosRealm);
        KerberosAuthenticationConfig kerbAuthentication = kerberosRealm.getKerberosAuthenticationConfig();
        assertNotNull(kerbAuthentication);
        assertEquals(TRUE, kerbAuthentication.getRelaxFlagsCheck());
        assertEquals(TRUE, kerbAuthentication.getUseNameWithoutRealm());
        assertEquals("krb5Acceptor", kerbAuthentication.getSecurityRealm());
        assertNotNull(kerbAuthentication.getLdapAuthenticationConfig());
        KerberosIdentityConfig kerbIdentity = kerberosRealm.getKerberosIdentityConfig();
        assertNotNull(kerbIdentity);
        assertEquals("HAZELCAST.COM", kerbIdentity.getRealm());
        assertEquals(TRUE, kerbIdentity.getUseCanonicalHostname());

        RealmConfig simpleRealm = securityConfig.getRealmConfig("simpleRealm");
        assertNotNull(simpleRealm);
        SimpleAuthenticationConfig simpleAuthnCfg = simpleRealm.getSimpleAuthenticationConfig();
        assertNotNull(simpleAuthnCfg);
        assertEquals(2, simpleAuthnCfg.getUsernames().size());
        assertTrue(simpleAuthnCfg.getUsernames().contains("test"));
        assertEquals("a1234", simpleAuthnCfg.getPassword("test"));
        Set<String> expectedRoles = new HashSet<>();
        expectedRoles.add("monitor");
        expectedRoles.add("hazelcast");
        assertEquals(expectedRoles, simpleAuthnCfg.getRoles("test"));
        AccessControlServiceConfig acs = simpleRealm.getAccessControlServiceConfig();
        assertNotNull(acs);
        assertEquals("com.acme.access.AccessControlServiceFactory", acs.getFactoryClassName());
        assertEquals("/opt/acl.xml", acs.getProperty("decisionFile"));
    }

    @Test
    void testReliableTopicConfig() {
        ReliableTopicConfig testReliableTopic = config.getReliableTopicConfig("testReliableTopic");
        assertNotNull(testReliableTopic);
        assertEquals("testReliableTopic", testReliableTopic.getName());
        assertEquals(1, testReliableTopic.getMessageListenerConfigs().size());
        assertFalse(testReliableTopic.isStatisticsEnabled());
        ListenerConfig listenerConfig = testReliableTopic.getMessageListenerConfigs().get(0);
        assertEquals("com.hazelcast.spring.DummyMessageListener", listenerConfig.getClassName());
        assertEquals(10, testReliableTopic.getReadBatchSize());
        assertEquals(TopicOverloadPolicy.BLOCK, testReliableTopic.getTopicOverloadPolicy());
        assertEquals("ns1", testReliableTopic.getUserCodeNamespace());
    }

    @Test
    void testMultimapConfig() {
        MultiMapConfig testMultiMapConfig = config.getMultiMapConfig("testMultimap");
        assertEquals(MultiMapConfig.ValueCollectionType.LIST, testMultiMapConfig.getValueCollectionType());
        assertEquals(2, testMultiMapConfig.getEntryListenerConfigs().size());
        assertFalse(testMultiMapConfig.isBinary());
        assertFalse(testMultiMapConfig.isStatisticsEnabled());
        assertEquals("ns1", testMultiMapConfig.getUserCodeNamespace());
        for (EntryListenerConfig listener : testMultiMapConfig.getEntryListenerConfigs()) {
            if (listener.getClassName() != null) {
                assertNull(listener.getImplementation());
                assertTrue(listener.isIncludeValue());
                assertFalse(listener.isLocal());
            } else {
                assertNotNull(listener.getImplementation());
                assertEquals(entryListener, listener.getImplementation());
                assertTrue(listener.isLocal());
                assertTrue(listener.isIncludeValue());
            }
        }
        MergePolicyConfig mergePolicyConfig = testMultiMapConfig.getMergePolicyConfig();
        assertEquals("PassThroughMergePolicy", mergePolicyConfig.getPolicy());
        assertEquals(1234, mergePolicyConfig.getBatchSize());
    }

    @Test
    void testListConfig() {
        ListConfig testListConfig = config.getListConfig("testList");
        assertNotNull(testListConfig);
        assertEquals("testList", testListConfig.getName());
        assertEquals(9999, testListConfig.getMaxSize());
        assertEquals(1, testListConfig.getBackupCount());
        assertEquals(1, testListConfig.getAsyncBackupCount());
        assertFalse(testListConfig.isStatisticsEnabled());
        assertEquals("ns1", testListConfig.getUserCodeNamespace());

        MergePolicyConfig mergePolicyConfig = testListConfig.getMergePolicyConfig();
        assertEquals("DiscardMergePolicy", mergePolicyConfig.getPolicy());
        assertEquals(2342, mergePolicyConfig.getBatchSize());
    }

    @Test
    void testSetConfig() {
        SetConfig testSetConfig = config.getSetConfig("testSet");
        assertNotNull(testSetConfig);
        assertEquals("testSet", testSetConfig.getName());
        assertEquals(7777, testSetConfig.getMaxSize());
        assertEquals(0, testSetConfig.getBackupCount());
        assertEquals(0, testSetConfig.getAsyncBackupCount());
        assertFalse(testSetConfig.isStatisticsEnabled());
        assertEquals("ns1", testSetConfig.getUserCodeNamespace());

        MergePolicyConfig mergePolicyConfig = testSetConfig.getMergePolicyConfig();
        assertEquals("DiscardMergePolicy", mergePolicyConfig.getPolicy());
        assertEquals(2342, mergePolicyConfig.getBatchSize());
    }

    @Test
    void testTopicConfig() {
        TopicConfig testTopicConfig = config.getTopicConfig("testTopic");
        assertNotNull(testTopicConfig);
        assertEquals("testTopic", testTopicConfig.getName());
        assertEquals(1, testTopicConfig.getMessageListenerConfigs().size());
        assertTrue(testTopicConfig.isGlobalOrderingEnabled());
        assertFalse(testTopicConfig.isStatisticsEnabled());
        assertEquals("ns1", testTopicConfig.getUserCodeNamespace());
        ListenerConfig listenerConfig = testTopicConfig.getMessageListenerConfigs().get(0);
        assertEquals("com.hazelcast.spring.DummyMessageListener", listenerConfig.getClassName());
    }

    @Test
    void testClusterNameConfig() {
        assertEquals("spring-cluster-fullConfig", config.getClusterName());
    }

    @Test
    void testExecutorConfig() {
        ExecutorConfig testExecConfig = config.getExecutorConfig("testExec");
        assertNotNull(testExecConfig);
        assertEquals("testExec", testExecConfig.getName());
        assertEquals(2, testExecConfig.getPoolSize());
        assertEquals(100, testExecConfig.getQueueCapacity());
        assertTrue(testExecConfig.isStatisticsEnabled());
        assertEquals("ns1", testExecConfig.getUserCodeNamespace());
        ExecutorConfig testExec2Config = config.getExecutorConfig("testExec2");
        assertNotNull(testExec2Config);
        assertEquals("testExec2", testExec2Config.getName());
        assertEquals(5, testExec2Config.getPoolSize());
        assertEquals(300, testExec2Config.getQueueCapacity());
        assertFalse(testExec2Config.isStatisticsEnabled());
    }

    @Test
    void testDurableExecutorConfig() {
        DurableExecutorConfig testExecConfig = config.getDurableExecutorConfig("durableExec");
        assertNotNull(testExecConfig);
        assertEquals("durableExec", testExecConfig.getName());
        assertEquals(10, testExecConfig.getPoolSize());
        assertEquals(5, testExecConfig.getDurability());
        assertEquals(200, testExecConfig.getCapacity());
        assertFalse(testExecConfig.isStatisticsEnabled());
        assertEquals("ns1", testExecConfig.getUserCodeNamespace());
    }

    @Test
    void testScheduledExecutorConfig() {
        ScheduledExecutorConfig testExecConfig = config.getScheduledExecutorConfig("scheduledExec");
        assertNotNull(testExecConfig);
        assertEquals("scheduledExec", testExecConfig.getName());
        assertEquals(10, testExecConfig.getPoolSize());
        assertEquals(5, testExecConfig.getDurability());
        assertEquals(100, testExecConfig.getCapacity());
        assertEquals(ScheduledExecutorConfig.CapacityPolicy.PER_PARTITION, testExecConfig.getCapacityPolicy());
        MergePolicyConfig mergePolicyConfig = testExecConfig.getMergePolicyConfig();
        assertNotNull(mergePolicyConfig);
        assertEquals("PassThroughMergePolicy", mergePolicyConfig.getPolicy());
        assertEquals(101, mergePolicyConfig.getBatchSize());
        assertFalse(testExecConfig.isStatisticsEnabled());
        assertEquals("ns1", testExecConfig.getUserCodeNamespace());
    }

    @Test
    void testCardinalityEstimatorConfig() {
        CardinalityEstimatorConfig estimatorConfig = config.getCardinalityEstimatorConfig("estimator");
        assertNotNull(estimatorConfig);
        assertEquals("estimator", estimatorConfig.getName());
        assertEquals(4, estimatorConfig.getBackupCount());
        assertEquals("DiscardMergePolicy", estimatorConfig.getMergePolicyConfig().getPolicy());
        assertEquals(44, estimatorConfig.getMergePolicyConfig().getBatchSize());
    }

    @Test
    void testNetworkConfig() {
        NetworkConfig networkConfig = config.getNetworkConfig();
        assertNotNull(networkConfig);
        assertEquals(5700, networkConfig.getPort());
        assertTrue(networkConfig.isPortAutoIncrement());

        Collection<String> allowedPorts = networkConfig.getOutboundPortDefinitions();
        assertEquals(2, allowedPorts.size());
        Iterator<String> portIterator = allowedPorts.iterator();
        assertEquals("35000-35100", portIterator.next());
        assertEquals("36000,36100", portIterator.next());
        assertFalse(networkConfig.getJoin().getAutoDetectionConfig().isEnabled());
        assertFalse(networkConfig.getJoin().getMulticastConfig().isEnabled());
        assertEquals(8, networkConfig.getJoin().getMulticastConfig().getMulticastTimeoutSeconds());
        assertEquals(16, networkConfig.getJoin().getMulticastConfig().getMulticastTimeToLive());
        assertEquals(Boolean.FALSE, networkConfig.getJoin().getMulticastConfig().getLoopbackModeEnabled());
        Set<String> tis = networkConfig.getJoin().getMulticastConfig().getTrustedInterfaces();
        assertEquals(1, tis.size());
        assertEquals("10.10.10.*", tis.iterator().next());
        assertFalse(networkConfig.getInterfaces().isEnabled());
        assertEquals(1, networkConfig.getInterfaces().getInterfaces().size());
        assertEquals("10.10.1.*", networkConfig.getInterfaces().getInterfaces().iterator().next());
        TcpIpConfig tcp = networkConfig.getJoin().getTcpIpConfig();
        assertNotNull(tcp);
        assertFalse(tcp.isEnabled());

        List<String> members = tcp.getMembers();
        assertEquals(2, members.size(), members.toString());
        assertEquals("127.0.0.1:5700", members.get(0));
        assertEquals("127.0.0.1:5701", members.get(1));
        assertEquals("127.0.0.1:5700", tcp.getRequiredMember());
        assertAwsConfig(networkConfig.getJoin().getAwsConfig());
        assertGcpConfig(networkConfig.getJoin().getGcpConfig());
        assertAzureConfig(networkConfig.getJoin().getAzureConfig());
        assertKubernetesConfig(networkConfig.getJoin().getKubernetesConfig());
        assertEurekaConfig(networkConfig.getJoin().getEurekaConfig());

        assertTrue(networkConfig.isReuseAddress(), "reuse-address");

        MemberAddressProviderConfig memberAddressProviderConfig = networkConfig.getMemberAddressProviderConfig();
        assertFalse(memberAddressProviderConfig.isEnabled());
        assertEquals("com.hazelcast.spring.DummyMemberAddressProvider", memberAddressProviderConfig.getClassName());
        assertFalse(memberAddressProviderConfig.getProperties().isEmpty());
        assertEquals("value", memberAddressProviderConfig.getProperties().getProperty("dummy.property"));
        assertEquals("value2", memberAddressProviderConfig.getProperties().getProperty("dummy.property.2"));

        IcmpFailureDetectorConfig icmpFailureDetectorConfig = networkConfig.getIcmpFailureDetectorConfig();
        assertFalse(icmpFailureDetectorConfig.isEnabled());
        assertTrue(icmpFailureDetectorConfig.isParallelMode());
        assertTrue(icmpFailureDetectorConfig.isFailFastOnStartup());
        assertEquals(500, icmpFailureDetectorConfig.getTimeoutMilliseconds());
        assertEquals(1002, icmpFailureDetectorConfig.getIntervalMilliseconds());
        assertEquals(2, icmpFailureDetectorConfig.getMaxAttempts());
        assertEquals(1, icmpFailureDetectorConfig.getTtl());

        TpcSocketConfig tpcSocketConfig = networkConfig.getTpcSocketConfig();
        assertEquals("14000-16000", tpcSocketConfig.getPortRange());
        assertEquals(256, tpcSocketConfig.getReceiveBufferSizeKB());
        assertEquals(256, tpcSocketConfig.getSendBufferSizeKB());
    }

    private void assertAwsConfig(AwsConfig aws) {
        assertFalse(aws.isEnabled());
        assertEquals("sample-access-key", aws.getProperty("access-key"));
        assertEquals("sample-secret-key", aws.getProperty("secret-key"));
        assertEquals("sample-region", aws.getProperty("region"));
        assertEquals("sample-header", aws.getProperty("host-header"));
        assertEquals("sample-group", aws.getProperty("security-group-name"));
        assertEquals("sample-tag-key", aws.getProperty("tag-key"));
        assertEquals("sample-tag-value", aws.getProperty("tag-value"));
        assertEquals("sample-role", aws.getProperty("iam-role"));
    }

    private void assertGcpConfig(GcpConfig gcp) {
        assertFalse(gcp.isEnabled());
        assertEquals("us-east1-b,us-east1-c", gcp.getProperty("zones"));
    }

    private void assertAzureConfig(AzureConfig azure) {
        assertFalse(azure.isEnabled());
        assertEquals("false", azure.getProperty("instance-metadata-available"));
        assertEquals("CLIENT_ID", azure.getProperty("client-id"));
        assertEquals("CLIENT_SECRET", azure.getProperty("client-secret"));
        assertEquals("TENANT_ID", azure.getProperty("tenant-id"));
        assertEquals("SUB_ID", azure.getProperty("subscription-id"));
        assertEquals("RESOURCE-GROUP-NAME", azure.getProperty("resource-group"));
        assertEquals("SCALE-SET", azure.getProperty("scale-set"));
        assertEquals("TAG-NAME=HZLCAST001", azure.getProperty("tag"));
    }

    private void assertKubernetesConfig(KubernetesConfig kubernetes) {
        assertFalse(kubernetes.isEnabled());
        assertEquals("MY-KUBERNETES-NAMESPACE", kubernetes.getProperty("namespace"));
        assertEquals("MY-SERVICE-NAME", kubernetes.getProperty("service-name"));
        assertEquals("MY-SERVICE-LABEL-NAME", kubernetes.getProperty("service-label-name"));
        assertEquals("MY-SERVICE-LABEL-VALUE", kubernetes.getProperty("service-label-value"));
    }

    private void assertEurekaConfig(EurekaConfig eureka) {
        assertFalse(eureka.isEnabled());
        assertEquals("true", eureka.getProperty("self-registration"));
        assertEquals("hazelcast", eureka.getProperty("namespace"));
    }

    private void assertDiscoveryConfig(DiscoveryConfig discoveryConfig) {
        assertInstanceOf(DummyDiscoveryServiceProvider.class, discoveryConfig.getDiscoveryServiceProvider());
        assertInstanceOf(DummyNodeFilter.class, discoveryConfig.getNodeFilter());
        List<DiscoveryStrategyConfig> discoveryStrategyConfigs
                = (List<DiscoveryStrategyConfig>) discoveryConfig.getDiscoveryStrategyConfigs();
        assertEquals(2, discoveryStrategyConfigs.size());
        DiscoveryStrategyConfig discoveryStrategyConfig = discoveryStrategyConfigs.get(0);
        assertInstanceOf(DummyDiscoveryStrategyFactory.class, discoveryStrategyConfig.getDiscoveryStrategyFactory());
        assertEquals(3, discoveryStrategyConfig.getProperties().size());
        assertEquals("foo", discoveryStrategyConfig.getProperties().get("key-string"));
        assertEquals("123", discoveryStrategyConfig.getProperties().get("key-int"));
        assertEquals("true", discoveryStrategyConfig.getProperties().get("key-boolean"));

        DiscoveryStrategyConfig discoveryStrategyConfig2 = discoveryStrategyConfigs.get(1);
        assertEquals(DummyDiscoveryStrategy.class.getName(), discoveryStrategyConfig2.getClassName());
        assertEquals(1, discoveryStrategyConfig2.getProperties().size());
        assertEquals("foo2", discoveryStrategyConfig2.getProperties().get("key-string"));
    }

    @Test
    void testProperties() {
        Properties properties = config.getProperties();
        assertNotNull(properties);
        assertEquals("5", properties.getProperty(MERGE_FIRST_RUN_DELAY_SECONDS.getName()));
        assertEquals("5", properties.getProperty(MERGE_NEXT_RUN_DELAY_SECONDS.getName()));
        assertEquals("277", properties.getProperty(PARTITION_COUNT.getName()));

        Config config2 = instance.getConfig();
        Properties properties2 = config2.getProperties();
        assertNotNull(properties2);
        assertEquals("5", properties2.get(MERGE_FIRST_RUN_DELAY_SECONDS.getName()));
        assertEquals("5", properties2.get(MERGE_NEXT_RUN_DELAY_SECONDS.getName()));
        assertEquals("277", properties2.get(PARTITION_COUNT.getName()));
    }

    @Test
    void testInstance() {
        assertNotNull(instance);
        Set<Member> members = instance.getCluster().getMembers();
        assertEquals(1, members.size());
        Member member = members.iterator().next();
        InetSocketAddress inetSocketAddress = member.getSocketAddress();
        assertEquals(5700, inetSocketAddress.getPort());
        assertEquals("test-instance", config.getInstanceName());
        assertEquals("HAZELCAST_ENTERPRISE_LICENSE_KEY", config.getLicenseKey());
        assertEquals(277, instance.getPartitionService().getPartitions().size());
    }

    @Test
    void testHazelcastInstances() {
        assertNotNull(map1);
        assertNotNull(map2);
        assertNotNull(multiMap);
        assertNotNull(replicatedMap);
        assertNotNull(queue);
        assertNotNull(topic);
        assertNotNull(set);
        assertNotNull(list);
        assertNotNull(executorService);
        assertNotNull(flakeIdGenerator);
        assertNotNull(pnCounter);
        assertNotNull(jet);
        assertEquals(config.getJetConfig(), jet.getConfig());
        assertEquals("map1", map1.getName());
        assertEquals("map2", map2.getName());
        assertEquals("testMultimap", multiMap.getName());
        assertEquals("replicatedMap", replicatedMap.getName());
        assertEquals("testQ", queue.getName());
        assertEquals("testTopic", topic.getName());
        assertEquals("set", set.getName());
        assertEquals("list", list.getName());
        assertEquals("flakeIdGenerator", flakeIdGenerator.getName());
    }

    @Test
    void testWanReplicationConfig() {
        WanReplicationConfig wcfg = config.getWanReplicationConfig("testWan");
        assertNotNull(wcfg);

        WanBatchPublisherConfig pc = wcfg.getBatchPublisherConfigs().get(0);
        assertEquals("tokyo", pc.getClusterName());
        assertEquals("tokyoPublisherId", pc.getPublisherId());
        assertEquals("com.hazelcast.enterprise.wan.impl.replication.WanBatchPublisher", pc.getClassName());
        assertEquals(WanQueueFullBehavior.THROW_EXCEPTION, pc.getQueueFullBehavior());
        assertEquals(WanPublisherState.STOPPED, pc.getInitialPublisherState());
        assertEquals(1000, pc.getQueueCapacity());
        assertEquals(50, pc.getBatchSize());
        assertEquals(3000, pc.getBatchMaxDelayMillis());
        assertTrue(pc.isSnapshotEnabled());
        assertEquals(5000, pc.getResponseTimeoutMillis());
        assertEquals(5, pc.getMaxTargetEndpoints());
        assertEquals(5, pc.getDiscoveryPeriodSeconds());
        assertTrue(pc.isUseEndpointPrivateAddress());
        assertEquals(5, pc.getIdleMinParkNs());
        assertEquals(5, pc.getIdleMaxParkNs());
        assertEquals(5, pc.getMaxConcurrentInvocations());
        assertEquals(WanAcknowledgeType.ACK_ON_RECEIPT, pc.getAcknowledgeType());
        assertEquals(5, pc.getDiscoveryPeriodSeconds());
        assertEquals(5, pc.getMaxTargetEndpoints());
        assertAwsConfig(pc.getAwsConfig());
        assertGcpConfig(pc.getGcpConfig());
        assertAzureConfig(pc.getAzureConfig());
        assertKubernetesConfig(pc.getKubernetesConfig());
        assertEurekaConfig(pc.getEurekaConfig());

        WanCustomPublisherConfig customPublisher = wcfg.getCustomPublisherConfigs().get(0);
        assertEquals("istanbulPublisherId", customPublisher.getPublisherId());
        assertEquals("com.hazelcast.wan.custom.CustomPublisher", customPublisher.getClassName());
        Map<String, Comparable> customPublisherProps = customPublisher.getProperties();
        assertEquals("prop.publisher", customPublisherProps.get("custom.prop.publisher"));

        WanBatchPublisherConfig publisherPlaceHolderConfig = wcfg.getBatchPublisherConfigs().get(1);
        assertEquals(5000, publisherPlaceHolderConfig.getQueueCapacity());

        WanConsumerConfig consumerConfig = wcfg.getConsumerConfig();
        assertEquals("com.hazelcast.wan.custom.WanConsumer", consumerConfig.getClassName());
        Map<String, Comparable> consumerProps = consumerConfig.getProperties();
        assertEquals("prop.consumer", consumerProps.get("custom.prop.consumer"));
        assertTrue(consumerConfig.isPersistWanReplicatedData());
    }

    @Test
    void testWanConsumerWithPersistDataFalse() {
        WanReplicationConfig config2 = config.getWanReplicationConfig("testWan2");
        WanConsumerConfig consumerConfig2 = config2.getConsumerConfig();
        assertInstanceOf(DummyWanConsumer.class, consumerConfig2.getImplementation());
        assertFalse(consumerConfig2.isPersistWanReplicatedData());
    }

    @Test
    void testNoWanConsumerClass() {
        WanReplicationConfig config2 = config.getWanReplicationConfig("testWan3");
        WanConsumerConfig consumerConfig2 = config2.getConsumerConfig();
        assertFalse(consumerConfig2.isPersistWanReplicatedData());
    }

    @Test
    void testWanReplicationSyncConfig() {
        final WanReplicationConfig wcfg = config.getWanReplicationConfig("testWan2");
        final WanConsumerConfig consumerConfig = wcfg.getConsumerConfig();
        final Map<String, Comparable> consumerProps = new HashMap<>();
        consumerProps.put("custom.prop.consumer", "prop.consumer");
        consumerConfig.setProperties(consumerProps);
        assertInstanceOf(DummyWanConsumer.class, consumerConfig.getImplementation());
        assertEquals("prop.consumer", consumerConfig.getProperties().get("custom.prop.consumer"));
        assertFalse(consumerConfig.isPersistWanReplicatedData());

        final List<WanBatchPublisherConfig> publisherConfigs = wcfg.getBatchPublisherConfigs();
        assertNotNull(publisherConfigs);
        assertEquals(1, publisherConfigs.size());

        final WanBatchPublisherConfig pc = publisherConfigs.get(0);
        assertEquals("tokyo", pc.getClusterName());

        final WanSyncConfig wanSyncConfig = pc.getSyncConfig();
        assertNotNull(wanSyncConfig);
        assertEquals(ConsistencyCheckStrategy.MERKLE_TREES, wanSyncConfig.getConsistencyCheckStrategy());
    }

    @Test
    void testConfigListeners() {
        assertNotNull(membershipListener);
        List<ListenerConfig> listenerConfigList = config.getListenerConfigs();
        assertEquals(2, listenerConfigList.size());
        for (ListenerConfig lc : listenerConfigList) {
            if (lc.getClassName() != null) {
                assertNull(lc.getImplementation());
                assertEquals(DummyMembershipListener.class.getName(), lc.getClassName());
            } else {
                assertNotNull(lc.getImplementation());
                assertEquals(membershipListener, lc.getImplementation());
            }
        }
    }

    @Test
    void testPartitionGroupConfig() {
        PartitionGroupConfig pgc = config.getPartitionGroupConfig();
        assertFalse(pgc.isEnabled());
        assertEquals(PartitionGroupConfig.MemberGroupType.CUSTOM, pgc.getGroupType());
        assertEquals(2, pgc.getMemberGroupConfigs().size());
        for (MemberGroupConfig mgc : pgc.getMemberGroupConfigs()) {
            assertEquals(2, mgc.getInterfaces().size());
        }
    }

    @Test
    void testCRDTReplicationConfig() {
        CRDTReplicationConfig replicationConfig = config.getCRDTReplicationConfig();
        assertEquals(10, replicationConfig.getMaxConcurrentReplicationTargets());
        assertEquals(2000, replicationConfig.getReplicationPeriodMillis());
    }

    @Test
    void testSSLConfig() {
        SSLConfig sslConfig = config.getNetworkConfig().getSSLConfig();
        assertNotNull(sslConfig);
        assertFalse(sslConfig.isEnabled());
        assertNotNull(sslContextFactory);
    }

    @Test
    void testSocketInterceptorConfig() {
        SocketInterceptorConfig socketInterceptorConfig = config.getNetworkConfig().getSocketInterceptorConfig();
        assertNotNull(socketInterceptorConfig);
        assertFalse(socketInterceptorConfig.isEnabled());
        assertEquals(socketInterceptor, socketInterceptorConfig.getImplementation());
    }

    @Test
    void testManagementCenterConfig() {
        ManagementCenterConfig managementCenterConfig = config.getManagementCenterConfig();
        assertNotNull(managementCenterConfig);
        assertTrue(managementCenterConfig.isScriptingEnabled());
        assertTrue(managementCenterConfig.isConsoleEnabled());
        assertTrue(managementCenterConfig.isDataAccessEnabled());
        Set<String> tis = managementCenterConfig.getTrustedInterfaces();
        assertEquals(1, tis.size());
        assertEquals("10.1.2.*", tis.iterator().next());
    }

    @Test
    void testMemberAttributesConfig() {
        MemberAttributeConfig memberAttributeConfig = config.getMemberAttributeConfig();
        assertNotNull(memberAttributeConfig);
        assertEquals("spring-cluster", memberAttributeConfig.getAttribute("cluster.name"));
    }

    @Test
    void testSerializationConfig() {
        SerializationConfig serializationConfig = config.getSerializationConfig();
        assertTrue(serializationConfig.isAllowOverrideDefaultSerializers());
        assertEquals(ByteOrder.BIG_ENDIAN, serializationConfig.getByteOrder());
        assertFalse(serializationConfig.isCheckClassDefErrors());
        assertEquals(13, serializationConfig.getPortableVersion());

        Map<Integer, String> dataSerializableFactoryClasses
                = serializationConfig.getDataSerializableFactoryClasses();
        assertFalse(dataSerializableFactoryClasses.isEmpty());
        assertEquals(DummyDataSerializableFactory.class.getName(), dataSerializableFactoryClasses.get(1));

        Map<Integer, DataSerializableFactory> dataSerializableFactories
                = serializationConfig.getDataSerializableFactories();
        assertFalse(dataSerializableFactories.isEmpty());
        assertEquals(DummyDataSerializableFactory.class, dataSerializableFactories.get(2).getClass());

        Map<Integer, String> portableFactoryClasses = serializationConfig.getPortableFactoryClasses();
        assertFalse(portableFactoryClasses.isEmpty());
        assertEquals(DummyPortableFactory.class.getName(), portableFactoryClasses.get(1));

        Map<Integer, PortableFactory> portableFactories = serializationConfig.getPortableFactories();
        assertFalse(portableFactories.isEmpty());
        assertEquals(DummyPortableFactory.class, portableFactories.get(2).getClass());

        Collection<SerializerConfig> serializerConfigs = serializationConfig.getSerializerConfigs();
        assertFalse(serializerConfigs.isEmpty());

        GlobalSerializerConfig globalSerializerConfig = serializationConfig.getGlobalSerializerConfig();
        assertNotNull(globalSerializerConfig);
        assertEquals(dummySerializer, globalSerializerConfig.getImplementation());
    }

    @Test
    void testCompactSerializationConfig() {
        CompactSerializationConfig compactSerializationConfig = config.getSerializationConfig()
                .getCompactSerializationConfig();

        List<String> serializerClassNames
                = CompactSerializationConfigAccessor.getSerializerClassNames(compactSerializationConfig);
        assertEquals(1, serializerClassNames.size());

        List<String> compactSerializableClassNames
                = CompactSerializationConfigAccessor.getCompactSerializableClassNames(compactSerializationConfig);
        assertEquals(1, compactSerializableClassNames.size());

        String reflectivelySerializableClassName = DummyReflectiveSerializable.class.getName();
        assertThat(compactSerializableClassNames)
                .contains(reflectivelySerializableClassName);

        String compactSerializerClassName = DummyCompactSerializer.class.getName();
        assertThat(serializerClassNames)
                .contains(compactSerializerClassName);
    }

    @Test
    void testNativeMemoryConfig() {
        NativeMemoryConfig nativeMemoryConfig = config.getNativeMemoryConfig();
        assertFalse(nativeMemoryConfig.isEnabled());
        assertEquals(GIGABYTES, nativeMemoryConfig.getCapacity().getUnit());
        assertEquals(256, nativeMemoryConfig.getCapacity().getValue());
        assertEquals(20, nativeMemoryConfig.getPageSize());
        assertEquals(NativeMemoryConfig.MemoryAllocatorType.STANDARD, nativeMemoryConfig.getAllocatorType());
        assertEquals(10.2, nativeMemoryConfig.getMetadataSpacePercentage(), 0.1);
        assertEquals(10, nativeMemoryConfig.getMinBlockSize());
        List<PersistentMemoryDirectoryConfig> directoryConfigs = nativeMemoryConfig.getPersistentMemoryConfig()
                .getDirectoryConfigs();

        assertEquals(2, directoryConfigs.size());
        assertEquals("/mnt/pmem0", directoryConfigs.get(0).getDirectory());
        assertEquals(0, directoryConfigs.get(0).getNumaNode());
        assertEquals("/mnt/pmem1", directoryConfigs.get(1).getDirectory());
        assertEquals(1, directoryConfigs.get(1).getNumaNode());
    }

    @Test
    void testReplicatedMapConfig() {
        assertNotNull(config);
        assertEquals(1, config.getReplicatedMapConfigs().size());

        ReplicatedMapConfig replicatedMapConfig = config.getReplicatedMapConfig("replicatedMap");
        assertNotNull(replicatedMapConfig);
        assertEquals("replicatedMap", replicatedMapConfig.getName());
        assertEquals(InMemoryFormat.OBJECT, replicatedMapConfig.getInMemoryFormat());
        assertFalse(replicatedMapConfig.isAsyncFillup());
        assertFalse(replicatedMapConfig.isStatisticsEnabled());
        assertEquals("my-split-brain-protection", replicatedMapConfig.getSplitBrainProtectionName());
        assertEquals("ns1", replicatedMapConfig.getUserCodeNamespace());

        MergePolicyConfig mergePolicyConfig = replicatedMapConfig.getMergePolicyConfig();
        assertNotNull(mergePolicyConfig);
        assertEquals("PassThroughMergePolicy", mergePolicyConfig.getPolicy());
        assertEquals(2342, mergePolicyConfig.getBatchSize());

        replicatedMapConfig.getListenerConfigs();
        for (ListenerConfig listener : replicatedMapConfig.getListenerConfigs()) {
            if (listener.getClassName() != null) {
                assertNull(listener.getImplementation());
                assertTrue(listener.isIncludeValue());
                assertFalse(listener.isLocal());
            } else {
                assertNotNull(listener.getImplementation());
                assertEquals(entryListener, listener.getImplementation());
                assertTrue(listener.isLocal());
                assertTrue(listener.isIncludeValue());
            }
        }
    }

    @Test
    void testSplitBrainProtectionConfig() {
        assertNotNull(config);
        assertEquals(3, config.getSplitBrainProtectionConfigs().size());
        SplitBrainProtectionConfig splitBrainProtectionConfig = config.getSplitBrainProtectionConfig("my-split-brain-protection");
        assertNotNull(splitBrainProtectionConfig);
        assertEquals("my-split-brain-protection", splitBrainProtectionConfig.getName());
        assertEquals("com.hazelcast.spring.DummySplitBrainProtectionFunction", splitBrainProtectionConfig.getFunctionClassName());
        assertTrue(splitBrainProtectionConfig.isEnabled());
        assertEquals(2, splitBrainProtectionConfig.getMinimumClusterSize());
        assertEquals(2, splitBrainProtectionConfig.getListenerConfigs().size());
        assertEquals(SplitBrainProtectionOn.READ, splitBrainProtectionConfig.getProtectOn());
        assertEquals("com.hazelcast.spring.DummySplitBrainProtectionListener", splitBrainProtectionConfig.getListenerConfigs().get(0).getClassName());
        assertNotNull(splitBrainProtectionConfig.getListenerConfigs().get(1).getImplementation());
    }

    @Test
    void testProbabilisticSplitBrainProtectionConfig() {
        SplitBrainProtectionConfig probabilisticSplitBrainProtectionConfig = config.getSplitBrainProtectionConfig("probabilistic-split-brain-protection");
        assertNotNull(probabilisticSplitBrainProtectionConfig);
        assertEquals("probabilistic-split-brain-protection", probabilisticSplitBrainProtectionConfig.getName());
        assertNotNull(probabilisticSplitBrainProtectionConfig.getFunctionImplementation());
        assertInstanceOf(ProbabilisticSplitBrainProtectionFunction.class, probabilisticSplitBrainProtectionConfig.getFunctionImplementation());
        assertTrue(probabilisticSplitBrainProtectionConfig.isEnabled());
        assertEquals(3, probabilisticSplitBrainProtectionConfig.getMinimumClusterSize());
        assertEquals(2, probabilisticSplitBrainProtectionConfig.getListenerConfigs().size());
        assertEquals(SplitBrainProtectionOn.READ_WRITE, probabilisticSplitBrainProtectionConfig.getProtectOn());
        assertEquals("com.hazelcast.spring.DummySplitBrainProtectionListener",
                probabilisticSplitBrainProtectionConfig.getListenerConfigs().get(0).getClassName());
        assertNotNull(probabilisticSplitBrainProtectionConfig.getListenerConfigs().get(1).getImplementation());
        ProbabilisticSplitBrainProtectionFunction splitBrainProtectionFunction =
                (ProbabilisticSplitBrainProtectionFunction) probabilisticSplitBrainProtectionConfig.getFunctionImplementation();
        assertEquals(11, splitBrainProtectionFunction.getSuspicionThreshold(), 0.001d);
        assertEquals(31415, splitBrainProtectionFunction.getAcceptableHeartbeatPauseMillis());
        assertEquals(42, splitBrainProtectionFunction.getMaxSampleSize());
        assertEquals(77123, splitBrainProtectionFunction.getHeartbeatIntervalMillis());
        assertEquals(1000, splitBrainProtectionFunction.getMinStdDeviationMillis());
    }

    @Test
    void testRecentlyActiveSplitBrainProtectionConfig() {
        SplitBrainProtectionConfig recentlyActiveSplitBrainProtectionConfig = config.getSplitBrainProtectionConfig("recently-active-split-brain-protection");
        assertNotNull(recentlyActiveSplitBrainProtectionConfig);
        assertEquals("recently-active-split-brain-protection", recentlyActiveSplitBrainProtectionConfig.getName());
        assertNotNull(recentlyActiveSplitBrainProtectionConfig.getFunctionImplementation());
        assertInstanceOf(RecentlyActiveSplitBrainProtectionFunction.class, recentlyActiveSplitBrainProtectionConfig.getFunctionImplementation());
        assertTrue(recentlyActiveSplitBrainProtectionConfig.isEnabled());
        assertEquals(5, recentlyActiveSplitBrainProtectionConfig.getMinimumClusterSize());
        assertEquals(SplitBrainProtectionOn.READ_WRITE, recentlyActiveSplitBrainProtectionConfig.getProtectOn());
        RecentlyActiveSplitBrainProtectionFunction splitBrainProtectionFunction =
                (RecentlyActiveSplitBrainProtectionFunction) recentlyActiveSplitBrainProtectionConfig.getFunctionImplementation();
        assertEquals(5123, splitBrainProtectionFunction.getHeartbeatToleranceMillis());
    }

    @Test
    void testMapPerEntryStats() {
        MapConfig mapConfig = config.getMapConfig("map-with-per-entry-stats-enabled");
        assertTrue(mapConfig.isPerEntryStatsEnabled());
    }

    @Test
    void testFullQueryCacheConfig() {
        MapConfig mapConfig = config.getMapConfig("map-with-query-cache");
        QueryCacheConfig queryCacheConfig = mapConfig.getQueryCacheConfigs().get(0);
        EntryListenerConfig entryListenerConfig = queryCacheConfig.getEntryListenerConfigs().get(0);

        assertTrue(entryListenerConfig.isIncludeValue());
        assertFalse(entryListenerConfig.isLocal());

        assertEquals("com.hazelcast.spring.DummyEntryListener", entryListenerConfig.getClassName());
        assertFalse(queryCacheConfig.isIncludeValue());

        assertEquals("my-query-cache-1", queryCacheConfig.getName());
        assertEquals(12, queryCacheConfig.getBatchSize());
        assertEquals(33, queryCacheConfig.getBufferSize());
        assertEquals(12, queryCacheConfig.getDelaySeconds());
        assertEquals(InMemoryFormat.OBJECT, queryCacheConfig.getInMemoryFormat());
        assertTrue(queryCacheConfig.isCoalesce());
        assertTrue(queryCacheConfig.isSerializeKeys());
        assertFalse(queryCacheConfig.isPopulate());
        assertIndexesEqual(queryCacheConfig);
        assertEquals("__key > 12", queryCacheConfig.getPredicateConfig().getSql());
        assertEquals(EvictionPolicy.LRU, queryCacheConfig.getEvictionConfig().getEvictionPolicy());
        assertEquals(MaxSizePolicy.ENTRY_COUNT, queryCacheConfig.getEvictionConfig().getMaxSizePolicy());
        assertEquals(111, queryCacheConfig.getEvictionConfig().getSize());
    }

    private void assertIndexesEqual(QueryCacheConfig queryCacheConfig) {
        for (IndexConfig indexConfig : queryCacheConfig.getIndexConfigs()) {
            assertEquals("name", indexConfig.getAttributes().get(0));
            assertNotSame(IndexType.SORTED, indexConfig.getType());
        }
    }

    @Test
    void testMapNativeMaxSizePolicy() {
        MapConfig mapConfig = config.getMapConfig("map-with-native-max-size-policy");
        EvictionConfig evictionConfig = mapConfig.getEvictionConfig();

        assertEquals(USED_NATIVE_MEMORY_PERCENTAGE, evictionConfig.getMaxSizePolicy());
    }

    @Test
    void testPersistence() {
        File dir = new File("/mnt/persistence/");
        File backupDir = new File("/mnt/persistence-backup/");
        PersistenceConfig persistenceConfig = config.getPersistenceConfig();

        assertFalse(persistenceConfig.isEnabled());
        assertEquals(dir.getAbsolutePath(), persistenceConfig.getBaseDir().getAbsolutePath());
        assertEquals(backupDir.getAbsolutePath(), persistenceConfig.getBackupDir().getAbsolutePath());
        assertEquals(1111, persistenceConfig.getValidationTimeoutSeconds());
        assertEquals(2222, persistenceConfig.getDataLoadTimeoutSeconds());
        assertEquals(PARTIAL_RECOVERY_MOST_COMPLETE, persistenceConfig.getClusterDataRecoveryPolicy());
        assertFalse(persistenceConfig.isAutoRemoveStaleData());
        EncryptionAtRestConfig encryptionAtRestConfig = persistenceConfig.getEncryptionAtRestConfig();
        assertNotNull(encryptionAtRestConfig);
        assertTrue(encryptionAtRestConfig.isEnabled());
        assertEquals("AES/CBC/PKCS5Padding", encryptionAtRestConfig.getAlgorithm());
        assertEquals("sugar", encryptionAtRestConfig.getSalt());
        assertEquals(16, encryptionAtRestConfig.getKeySize());
        assertInstanceOf(VaultSecureStoreConfig.class, encryptionAtRestConfig.getSecureStoreConfig());
        VaultSecureStoreConfig vaultConfig = (VaultSecureStoreConfig) encryptionAtRestConfig.getSecureStoreConfig();
        assertEquals("http://localhost:1234", vaultConfig.getAddress());
        assertEquals("secret/path", vaultConfig.getSecretPath());
        assertEquals("token", vaultConfig.getToken());
        SSLConfig sslConfig = vaultConfig.getSSLConfig();
        assertNotNull(sslConfig);
        assertTrue(sslConfig.isEnabled());
        assertNotNull(sslContextFactory);
        assertEquals(60, vaultConfig.getPollingInterval());
        assertEquals(240, persistenceConfig.getRebalanceDelaySeconds());
    }

    @Test
    void testDynamicConfiguration() {
        boolean persistenceEnabled = false;
        File backupDir = new File("/mnt/backup-dir");
        int backupCount = 7;

        DynamicConfigurationConfig dynamicConfigurationConfig = config.getDynamicConfigurationConfig();

        assertEquals(persistenceEnabled, dynamicConfigurationConfig.isPersistenceEnabled());
        assertEquals(backupDir, dynamicConfigurationConfig.getBackupDir());
        assertEquals(backupCount, dynamicConfigurationConfig.getBackupCount());
    }

    @Test
    void testDevice() {
        String deviceName0 = "device0";
        String deviceName1 = "device1";

        File baseDir0 = new File("/dev/devices/tiered_store_device0");
        File baseDir1 = new File("/dev/devices/tiered_store_device1");

        int blockSize = 8192;
        int readIOThreadCount = 16;

        int writeIOThreadCount0 = 8;
        int writeIOThreadCount1 = 16;

        assertEquals(2, config.getDeviceConfigs().size());

        LocalDeviceConfig localDeviceConfig = config.getDeviceConfig(deviceName0);
        assertEquals(deviceName0, localDeviceConfig.getName());
        assertEquals(baseDir0, localDeviceConfig.getBaseDir());
        assertEquals(blockSize, localDeviceConfig.getBlockSize());
        assertEquals(readIOThreadCount, localDeviceConfig.getReadIOThreadCount());
        assertEquals(writeIOThreadCount0, localDeviceConfig.getWriteIOThreadCount());
        assertEquals(new Capacity(9321, MemoryUnit.MEGABYTES), localDeviceConfig.getCapacity());

        localDeviceConfig = config.getDeviceConfig(deviceName1);
        assertEquals(deviceName1, localDeviceConfig.getName());
        assertEquals(baseDir1, localDeviceConfig.getBaseDir());
        assertEquals(blockSize, localDeviceConfig.getBlockSize());
        assertEquals(readIOThreadCount, localDeviceConfig.getReadIOThreadCount());
        assertEquals(writeIOThreadCount1, localDeviceConfig.getWriteIOThreadCount());
        assertEquals(LocalDeviceConfig.DEFAULT_CAPACITY, localDeviceConfig.getCapacity());
    }

    @Test
    void testMapEvictionPolicies() {
        assertEquals(EvictionPolicy.LFU, config.getMapConfig("lfuEvictionMap").getEvictionConfig().getEvictionPolicy());
        assertEquals(EvictionPolicy.LRU, config.getMapConfig("lruEvictionMap").getEvictionConfig().getEvictionPolicy());
        assertEquals(EvictionPolicy.NONE, config.getMapConfig("noneEvictionMap").getEvictionConfig().getEvictionPolicy());
        assertEquals(EvictionPolicy.RANDOM, config.getMapConfig("randomEvictionMap").getEvictionConfig().getEvictionPolicy());
    }

    @Test
    void testMemberNearCacheEvictionPolicies() {
        assertEquals(EvictionPolicy.LFU, getNearCacheEvictionPolicy("lfuNearCacheEvictionMap", config));
        assertEquals(EvictionPolicy.LRU, getNearCacheEvictionPolicy("lruNearCacheEvictionMap", config));
        assertEquals(EvictionPolicy.RANDOM, getNearCacheEvictionPolicy("randomNearCacheEvictionMap", config));
        assertEquals(EvictionPolicy.NONE, getNearCacheEvictionPolicy("noneNearCacheEvictionMap", config));
    }

    private EvictionPolicy getNearCacheEvictionPolicy(String mapName, Config config) {
        return config.getMapConfig(mapName).getNearCacheConfig().getEvictionConfig().getEvictionPolicy();
    }

    @Test
    void testMapEvictionPolicyClassName() {
        MapConfig mapConfig = config.getMapConfig("mapWithComparatorClassName");
        String expectedComparatorClassName = "com.hazelcast.internal.eviction.impl.comparator.LRUEvictionPolicyComparator";

        assertEquals(expectedComparatorClassName, mapConfig.getEvictionConfig().getComparatorClassName());
    }

    @Test
    void testMapEvictionPolicyImpl() {
        MapConfig mapConfig = config.getMapConfig("mapWithComparatorImpl");

        assertEquals(DummyMapEvictionPolicyComparator.class, mapConfig.getEvictionConfig().getComparator().getClass());
    }

    @Test
    void testWhenBothMapEvictionPolicyClassNameAndEvictionPolicySet() {
        MapConfig mapConfig = config.getMapConfig("mapWithBothComparatorClassNameAndEvictionPolicy");
        String expectedComparatorClassName = "com.hazelcast.internal.eviction.impl.comparator.LFUEvictionPolicyComparator";

        EvictionConfig evictionConfig = mapConfig.getEvictionConfig();
        EvictionPolicy evictionPolicy = evictionConfig.getEvictionPolicy();

        assertEquals(EvictionPolicy.LRU, evictionPolicy);
        assertEquals(expectedComparatorClassName, evictionConfig.getComparatorClassName());
    }

    @Test
    void testExplicitPortCountConfiguration() {
        int portCount = instance.getConfig().getNetworkConfig().getPortCount();

        assertEquals(42, portCount);
    }

    @Test
    void testJavaSerializationFilterConfig() {
        JavaSerializationFilterConfig filterConfig = config.getSerializationConfig().getJavaSerializationFilterConfig();
        assertNotNull(filterConfig);
        assertTrue(filterConfig.isDefaultsDisabled());

        ClassFilter blacklist = filterConfig.getBlacklist();
        assertNotNull(blacklist);
        assertEquals(1, blacklist.getClasses().size());
        assertTrue(blacklist.getClasses().contains("com.acme.app.BeanComparator"));
        assertEquals(0, blacklist.getPackages().size());
        Set<String> prefixes = blacklist.getPrefixes();
        assertTrue(prefixes.contains("a.dangerous.package."));
        assertTrue(prefixes.contains("justaprefix"));
        assertEquals(2, prefixes.size());

        ClassFilter whitelist = filterConfig.getWhitelist();
        assertNotNull(whitelist);
        assertEquals(2, whitelist.getClasses().size());
        assertTrue(whitelist.getClasses().contains("java.lang.String"));
        assertTrue(whitelist.getClasses().contains("example.Foo"));
        assertEquals(2, whitelist.getPackages().size());
        assertTrue(whitelist.getPackages().contains("com.acme.app"));
        assertTrue(whitelist.getPackages().contains("com.acme.app.subpkg"));
    }

    @Test
    void testRestApiConfig() {
        RestApiConfig restApiConfig = config.getNetworkConfig().getRestApiConfig();
        assertNotNull(restApiConfig);
        assertFalse(restApiConfig.isEnabled());
        for (RestEndpointGroup group : RestEndpointGroup.values()) {
            assertTrue(restApiConfig.isGroupEnabled(group), "Unexpected status of REST Endpoint group" + group);
        }
    }

    @Test
    void testMemcacheProtocolConfig() {
        MemcacheProtocolConfig memcacheProtocolConfig = config.getNetworkConfig().getMemcacheProtocolConfig();
        assertNotNull(memcacheProtocolConfig);
        assertTrue(memcacheProtocolConfig.isEnabled());
    }

    @Test
    void testCPSubsystemConfig() {
        CPSubsystemConfig cpSubsystemConfig = config.getCPSubsystemConfig();
        assertEquals(0, cpSubsystemConfig.getCPMemberCount());
        assertEquals(0, cpSubsystemConfig.getGroupSize());
        assertEquals(15, cpSubsystemConfig.getSessionTimeToLiveSeconds());
        assertEquals(3, cpSubsystemConfig.getSessionHeartbeatIntervalSeconds());
        assertEquals(120, cpSubsystemConfig.getMissingCPMemberAutoRemovalSeconds());
        assertEquals(30, cpSubsystemConfig.getDataLoadTimeoutSeconds());
        assertEquals(20, cpSubsystemConfig.getCPMapLimit());
        assertTrue(cpSubsystemConfig.isFailOnIndeterminateOperationState());
        assertFalse(cpSubsystemConfig.isPersistenceEnabled());
        assertEquals(new File("/custom-dir").getAbsolutePath(), cpSubsystemConfig.getBaseDir().getAbsolutePath());
        assertEquals(-1, cpSubsystemConfig.getCPMemberPriority());
        RaftAlgorithmConfig raftAlgorithmConfig = cpSubsystemConfig.getRaftAlgorithmConfig();
        assertEquals(500, raftAlgorithmConfig.getLeaderElectionTimeoutInMillis());
        assertEquals(100, raftAlgorithmConfig.getLeaderHeartbeatPeriodInMillis());
        assertEquals(3, raftAlgorithmConfig.getMaxMissedLeaderHeartbeatCount());
        assertEquals(25, raftAlgorithmConfig.getAppendRequestMaxEntryCount());
        assertEquals(250, raftAlgorithmConfig.getCommitIndexAdvanceCountToSnapshot());
        assertEquals(75, raftAlgorithmConfig.getUncommittedEntryCountToRejectNewAppends());
        assertEquals(50, raftAlgorithmConfig.getAppendRequestBackoffTimeoutInMillis());
        SemaphoreConfig semaphoreConfig1 = cpSubsystemConfig.findSemaphoreConfig("sem1");
        SemaphoreConfig semaphoreConfig2 = cpSubsystemConfig.findSemaphoreConfig("sem2");
        assertNotNull(semaphoreConfig1);
        assertNotNull(semaphoreConfig2);
        assertTrue(semaphoreConfig1.isJDKCompatible());
        assertFalse(semaphoreConfig2.isJDKCompatible());
        assertEquals(1, semaphoreConfig1.getInitialPermits());
        assertEquals(2, semaphoreConfig2.getInitialPermits());
        FencedLockConfig lockConfig1 = cpSubsystemConfig.findLockConfig("lock1");
        FencedLockConfig lockConfig2 = cpSubsystemConfig.findLockConfig("lock2");
        assertNotNull(lockConfig1);
        assertNotNull(lockConfig2);
        assertEquals(1, lockConfig1.getLockAcquireLimit());
        assertEquals(2, lockConfig2.getLockAcquireLimit());

        CPMapConfig mapConfig1 = cpSubsystemConfig.findCPMapConfig("map1");
        assertNotNull(mapConfig1);
        assertEquals(50, mapConfig1.getMaxSizeMb());

        CPMapConfig mapConfig2 = cpSubsystemConfig.findCPMapConfig("map2");
        assertNotNull(mapConfig2);
        assertEquals(75, mapConfig2.getMaxSizeMb());

        CPMapConfig mapConfig3 = cpSubsystemConfig.findCPMapConfig("map3");
        assertNotNull(mapConfig3);
        assertEquals(100, mapConfig3.getMaxSizeMb());
    }

    @Test
    void testAuditLogConfig() {
        AuditlogConfig auditlogConfig = config.getAuditlogConfig();
        assertFalse(auditlogConfig.isEnabled());
        assertEquals("com.acme.AuditlogToSyslogFactory", auditlogConfig.getFactoryClassName());
        assertEquals("syslogserver.acme.com", auditlogConfig.getProperty("host"));
        assertEquals("514", auditlogConfig.getProperty("port"));
    }

    @Test
    void testMetricsConfig() {
        MetricsConfig metricsConfig = config.getMetricsConfig();
        assertFalse(metricsConfig.isEnabled());
        assertFalse(metricsConfig.getManagementCenterConfig().isEnabled());
        assertEquals(42, metricsConfig.getManagementCenterConfig().getRetentionSeconds());
        assertFalse(metricsConfig.getJmxConfig().isEnabled());
        assertEquals(24, metricsConfig.getCollectionFrequencySeconds());
    }

    @Test
    void testInstanceTrackingConfig() {
        InstanceTrackingConfig trackingConfig = config.getInstanceTrackingConfig();
        assertTrue(trackingConfig.isEnabled());
        assertEquals("/dummy/file", trackingConfig.getFileName());
        assertEquals("dummy-pattern with $HZ_INSTANCE_TRACKING{placeholder} and $RND{placeholder}",
                trackingConfig.getFormatPattern());
    }

    @Test
    void testSqlConfig() {
        SqlConfig sqlConfig = config.getSqlConfig();
        assertEquals(30L, sqlConfig.getStatementTimeoutMillis());
        assertFalse(sqlConfig.isCatalogPersistenceEnabled());
    }

    @Test
    void testJetConfig() {
        JetConfig jetConfig = config.getJetConfig();
        assertTrue(jetConfig.isEnabled());
        assertTrue(jetConfig.isResourceUploadEnabled());

        assertEquals(4, jetConfig.getCooperativeThreadCount());
        assertEquals(2, jetConfig.getBackupCount());
        assertEquals(200, jetConfig.getFlowControlPeriodMs());
        assertEquals(20000, jetConfig.getScaleUpDelayMillis());
        assertEquals(1000000, jetConfig.getMaxProcessorAccumulatedRecords());
        assertFalse(jetConfig.isLosslessRestartEnabled());

        EdgeConfig edgeConfig = jetConfig.getDefaultEdgeConfig();
        assertEquals(2048, edgeConfig.getQueueSize());
        assertEquals(15000, edgeConfig.getPacketSizeLimit());
        assertEquals(4, edgeConfig.getReceiveWindowMultiplier());
    }

    @Test
    void testIntegrityCheckerConfig() {
        final IntegrityCheckerConfig integrityCheckerConfig = config.getIntegrityCheckerConfig();
        assertFalse(integrityCheckerConfig.isEnabled());
    }

    @Test
    void testDataConnectionConfig() {
        DataConnectionConfig dataConnectionConfig = config.getDataConnectionConfig("my-data-connection");
        assertNotNull(dataConnectionConfig);
        assertEquals("my-data-connection", dataConnectionConfig.getName());
        assertEquals(DUMMY_TYPE, dataConnectionConfig.getType());
        assertFalse(dataConnectionConfig.isShared());
        assertEquals("jdbc:mysql://dummy:3306", dataConnectionConfig.getProperty("jdbcUrl"));
    }

    @Test
    void testTpcConfig() {
        TpcConfig tpcConfig = config.getTpcConfig();

        assertFalse(tpcConfig.isEnabled());
        assertEquals(12, tpcConfig.getEventloopCount());
    }

    @Test
    void testVectorCollectionConfig() {
        var expectedMergePolicyConfig = new MergePolicyConfig("CustomMergePolicy", 132);
        var expectedVectorCollection1 = new VectorCollectionConfig("vector-collection-1")
                .addVectorIndexConfig(
                        new VectorIndexConfig().setName("index-1").setDimension(2).setMetric(Metric.DOT)
                )
                .addVectorIndexConfig(
                        new VectorIndexConfig()
                                .setName("index-2")
                                .setDimension(20)
                                .setMetric(Metric.EUCLIDEAN)
                                .setMaxDegree(10)
                                .setEfConstruction(11)
                                .setUseDeduplication(true)
                );
        var expectedVectorCollection2 = new VectorCollectionConfig("vector-collection-2")
                .addVectorIndexConfig(
                        new VectorIndexConfig()
                                .setName("index-1")
                                .setDimension(200)
                                .setMetric(Metric.COSINE)
                                .setMaxDegree(12)
                                .setEfConstruction(13)
                                .setUseDeduplication(false)
                )
                .setBackupCount(2).setAsyncBackupCount(1)
                .setMergePolicyConfig(expectedMergePolicyConfig)
                .setUserCodeNamespace("ns1")
                .setSplitBrainProtectionName("my-split-brain-protection");
        var expectedVectorCollectionConfigs = Map.of(
                "vector-collection-1", expectedVectorCollection1,
                "vector-collection-2", expectedVectorCollection2
        );
        var actualVectorCollectionConfigs = config.getVectorCollectionConfigs();
        assertThat(
                actualVectorCollectionConfigs.entrySet()
        ).containsExactlyInAnyOrderElementsOf(expectedVectorCollectionConfigs.entrySet());
    }
}
