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

import com.hazelcast.client.impl.protocol.ClientMessage;
import com.hazelcast.client.impl.spi.ClientContext;
import com.hazelcast.client.impl.spi.ClientProxy;
import com.hazelcast.internal.serialization.Data;
import com.hazelcast.partition.strategy.StringPartitioningStrategy;
import com.hazelcast.projectx.service.codec.TrieClosestCodec;
import com.hazelcast.projectx.service.codec.TrieContainsCodec;
import com.hazelcast.projectx.service.codec.TrieInsertCodec;

import java.util.Collection;

public class ClientTrieProxy extends ClientProxy implements ITrie {
    protected ClientTrieProxy(String serviceName, String name, ClientContext context) {
        super(serviceName, name, context);
    }

    @Override
    public boolean insert(String value) {
        ClientMessage request = TrieInsertCodec.encodeRequest(name, value);
        ClientMessage response = invoke(request);
        return TrieInsertCodec.decodeResponse(response).response;
    }

    @Override
    public boolean contains(String value) {
        ClientMessage request = TrieContainsCodec.encodeRequest(name, value);
        ClientMessage response = invoke(request);
        return TrieContainsCodec.decodeResponse(response).response;
    }

    @Override
    public Collection<String> closest(String prefix, int n) {
        Data data = toData(prefix);
        ClientMessage request = TrieClosestCodec.encodeRequest(name, data, n);
        ClientMessage response = invoke(request);
        return TrieClosestCodec.decodeResponse(response).response;
    }

    @Override
    public String getPartitionKey() {
        return StringPartitioningStrategy.getPartitionKey(name);
    }
}
