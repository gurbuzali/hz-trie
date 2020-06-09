/*
 * Copyright (c) 2008-2020, Hazelcast, Inc. All Rights Reserved.
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

package com.hazelcast.projectx.trie;

import com.hazelcast.projectx.trie.operations.TrieClosestOperation;
import com.hazelcast.projectx.trie.operations.TrieContainsOperation;
import com.hazelcast.projectx.trie.operations.TrieInsertOperation;
import com.hazelcast.spi.impl.AbstractDistributedObject;
import com.hazelcast.spi.impl.NodeEngine;
import com.hazelcast.spi.impl.operationservice.Operation;

import java.util.Collection;

import static com.hazelcast.projectx.trie.TrieUtil.checkCount;
import static com.hazelcast.projectx.trie.TrieUtil.checkIfEmpty;
import static com.hazelcast.projectx.trie.TrieUtil.partitionKey;

public class ServerTrieProxy extends AbstractDistributedObject<TrieService> implements ITrie {

    private final String name;

    public ServerTrieProxy(String name, NodeEngine nodeEngine, TrieService service) {
        super(nodeEngine, service);
        this.name = name;
    }

    @Override
    public boolean insert(String value) {
        checkIfEmpty(value);

        int partitionId = partitionId(partitionKey(value));
        Operation operation = new TrieInsertOperation(name, value).setPartitionId(partitionId);
        return this.<Boolean>invokeOnPartition(operation).joinInternal();
    }

    @Override
    public boolean contains(String value) {
        checkIfEmpty(value);

        int partitionId = partitionId(partitionKey(value));
        Operation operation = new TrieContainsOperation(name, value).setPartitionId(partitionId);
        return this.<Boolean>invokeOnPartition(operation).joinInternal();
    }

    @Override
    public Collection<String> closest(String prefix, int n) {
        checkIfEmpty(prefix);
        checkCount(n);

        int partitionId = partitionId(partitionKey(prefix));
        Operation operation = new TrieClosestOperation(name, prefix, n).setPartitionId(partitionId);
        return this.<Collection<String>>invokeOnPartition(operation).joinInternal();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getServiceName() {
        return TrieService.NAME;
    }


    private int partitionId(String partitionKey) {
        return getNodeEngine().getPartitionService().getPartitionId(partitionKey);
    }

}
