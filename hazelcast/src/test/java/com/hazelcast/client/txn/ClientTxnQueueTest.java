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

package com.hazelcast.client.txn;

import com.hazelcast.client.test.TestHazelcastFactory;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.collection.IQueue;
import com.hazelcast.transaction.TransactionalQueue;
import com.hazelcast.test.HazelcastParallelClassRunner;
import com.hazelcast.test.annotation.ParallelJVMTest;
import com.hazelcast.test.annotation.QuickTest;
import com.hazelcast.transaction.TransactionContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.hazelcast.test.HazelcastTestSupport.randomString;
import static com.hazelcast.test.HazelcastTestSupport.sleepSeconds;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(HazelcastParallelClassRunner.class)
@Category({QuickTest.class, ParallelJVMTest.class})
public class ClientTxnQueueTest {

    private final TestHazelcastFactory hazelcastFactory = new TestHazelcastFactory();

    private HazelcastInstance client;

    @After
    public void tearDown() {
        hazelcastFactory.terminateAll();
    }

    @Before
    public void setup() {
        hazelcastFactory.newHazelcastInstance();
        client = hazelcastFactory.newHazelcastClient();
    }


    @Test
    public void testTransactionalOfferPoll() {
        final String item = "offered";
        final String queueName = randomString();
        final IQueue queue = client.getQueue(queueName);

        final TransactionContext context = client.newTransactionContext();
        context.beginTransaction();
        TransactionalQueue txnQueue = context.getQueue(queueName);
        txnQueue.offer(item);
        assertEquals(item, txnQueue.poll());
        context.commitTransaction();
    }

    @Test
    public void testQueueSizeAfterTxnOfferPoll() {
        final String item = "offered";
        final String queueName = randomString();
        final IQueue queue = client.getQueue(queueName);

        final TransactionContext context = client.newTransactionContext();
        context.beginTransaction();
        TransactionalQueue txnQueue = context.getQueue(queueName);
        txnQueue.offer(item);
        txnQueue.poll();
        context.commitTransaction();

        assertEquals(0, queue.size());
    }

    @Test
    public void testTransactionalOfferTake() throws InterruptedException {
        final String item = "offered";
        final String queueName = randomString();

        final TransactionContext context = client.newTransactionContext();
        context.beginTransaction();
        TransactionalQueue<String> txnQueue = context.getQueue(queueName);
        assertTrue(txnQueue.offer(item));
        assertEquals(1, txnQueue.size());
        assertEquals(item, txnQueue.take());
        context.commitTransaction();
    }

    @Test
    public void testTransactionalQueueGetsOfferedItems_whenBlockedOnPoll() throws InterruptedException {
        final String item = "offered1";
        final String queueName = randomString();
        final IQueue queue1 = client.getQueue(queueName);

        final CountDownLatch justBeforeBlocked = new CountDownLatch(1);

        new Thread(() -> {
            try {
                justBeforeBlocked.await();
                sleepSeconds(1);
                queue1.offer(item);
            } catch (InterruptedException e) {
                fail("failed" + e);
            }
        }).start();


        final TransactionContext context = client.newTransactionContext();
        context.beginTransaction();
        TransactionalQueue txnQueue1 = context.getQueue(queueName);

        justBeforeBlocked.countDown();
        Object result = txnQueue1.poll(5, TimeUnit.SECONDS);

        assertEquals("TransactionalQueue while blocked in pol should get item offered from client queue", item, result);

        context.commitTransaction();
    }

    @Test
    public void testTransactionalPeek() {
        final String item = "offered";
        final String queueName = randomString();
        final IQueue queue = client.getQueue(queueName);

        final TransactionContext context = client.newTransactionContext();
        context.beginTransaction();
        TransactionalQueue txnQueue = context.getQueue(queueName);

        txnQueue.offer(item);
        assertEquals(item, txnQueue.peek());
        assertEquals(item, txnQueue.peek());

        context.commitTransaction();
    }

    @Test
    public void testTransactionalOfferRoleBack() {
        final String name = randomString();
        final IQueue queue = client.getQueue(name);

        final TransactionContext context = client.newTransactionContext();
        context.beginTransaction();
        TransactionalQueue<String> qTxn = context.getQueue(name);
        qTxn.offer("ITEM");
        context.rollbackTransaction();

        assertEquals(0, queue.size());
    }

    @Test
    public void testTransactionalQueueSize() {
        final String item = "offered";
        final String name = randomString();
        final IQueue queue = client.getQueue(name);

        queue.offer(item);

        final TransactionContext context = client.newTransactionContext();
        context.beginTransaction();
        TransactionalQueue<String> txnQueue = context.getQueue(name);

        txnQueue.offer(item);
        assertEquals(2, txnQueue.size());

        context.rollbackTransaction();
    }


    @Test
    public void testTransactionalOfferAndPollWithTimeout() throws InterruptedException {
        final String item = "offered";
        final String name = randomString();
        final TransactionContext context = client.newTransactionContext();
        context.beginTransaction();
        TransactionalQueue<String> txnQueue = context.getQueue(name);
        assertTrue(txnQueue.offer(item));
        assertEquals(1, txnQueue.size());
        assertEquals(item, txnQueue.poll(5, TimeUnit.SECONDS));
        context.commitTransaction();
    }
}
