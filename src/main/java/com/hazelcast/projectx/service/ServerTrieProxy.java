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

package com.hazelcast.projectx.service;

import com.hazelcast.internal.util.ExceptionUtil;
import com.hazelcast.projectx.service.operations.TrieClosestOperation;
import com.hazelcast.projectx.service.operations.TrieContainsOperation;
import com.hazelcast.projectx.service.operations.TrieInsertOperation;
import com.hazelcast.spi.impl.AbstractDistributedObject;
import com.hazelcast.spi.impl.NodeEngine;
import com.hazelcast.spi.impl.operationservice.Operation;

import java.util.Collection;
import java.util.concurrent.ExecutionException;

public class ServerTrieProxy extends AbstractDistributedObject<TrieService> implements ITrie {
    private final String name;

    public ServerTrieProxy(String name, NodeEngine nodeEngine, TrieService service) {
        super(nodeEngine, service);
        this.name = name;
    }

    @Override
    public boolean insert(String value) {
        Operation operation = new TrieInsertOperation(name, value).setPartitionId(getPartitionId(toData(value))).setServiceName(getServiceName());
        try {
            return this.<Boolean>invokeOnPartition(operation).get();
        } catch (InterruptedException | ExecutionException e) {
            throw ExceptionUtil.sneakyThrow(e);
        }
    }

    @Override
    public boolean contains(String value) {
        Operation operation = new TrieContainsOperation(name, value).setPartitionId(getPartitionId(toData(value))).setServiceName(getServiceName());
        try {
            return this.<Boolean>invokeOnPartition(operation).get();
        } catch (InterruptedException | ExecutionException e) {
            throw ExceptionUtil.sneakyThrow(e);
        }
    }

    @Override
    public Collection<String> closest(String prefix, int n) {
        Operation operation = new TrieClosestOperation(name, prefix, n).setPartitionId(getPartitionId(toData(prefix + n))).setServiceName(getServiceName());
        try {
            return this.<Collection<String>>invokeOnPartition(operation).get();
        } catch (InterruptedException | ExecutionException e) {
            throw ExceptionUtil.sneakyThrow(e);
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getServiceName() {
        return TrieService.NAME;
    }
}
