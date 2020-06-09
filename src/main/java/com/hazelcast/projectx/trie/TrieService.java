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

import com.hazelcast.core.DistributedObject;
import com.hazelcast.internal.services.ManagedService;
import com.hazelcast.internal.services.RemoteService;
import com.hazelcast.spi.impl.NodeEngine;

import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TrieService implements ManagedService, RemoteService {
    public static final String NAME = "TrieService";
    private NodeEngine nodeEngine;

    private final ConcurrentMap<Integer, TrieContainer> containers = new ConcurrentHashMap<>();

    @Override
    public void init(NodeEngine nodeEngine, Properties properties) {
        this.nodeEngine = nodeEngine;
    }

    @Override
    public void reset() {

    }

    @Override
    public void shutdown(boolean b) {

    }

    @Override
    public DistributedObject createDistributedObject(String name, UUID uuid, boolean b) {
        return new ServerTrieProxy(name, nodeEngine, this);
    }

    @Override
    public void destroyDistributedObject(String name, boolean b) {

    }

    public TrieImpl lazyGet(int partitionId, String trieName) {
        return getOrCreateContainer(partitionId).getOrCreate(trieName);
    }

    private TrieContainer getOrCreateContainer(int partitionId) {
        TrieContainer container = containers.get(partitionId);
        if (container != null) {
            return container;
        }
        container = new TrieContainer();
        TrieContainer existing = containers.putIfAbsent(partitionId, container);
        if (existing != null) {
            container = existing;
        }
        return container;
    }
}
