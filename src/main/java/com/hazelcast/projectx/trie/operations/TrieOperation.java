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

package com.hazelcast.projectx.trie.operations;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.projectx.trie.TrieImpl;
import com.hazelcast.projectx.trie.TrieService;
import com.hazelcast.spi.impl.operationservice.Operation;

import java.io.IOException;

public abstract class TrieOperation extends Operation {

    String name;
    String word;

    Object response;

    public TrieOperation() {
    }

    public TrieOperation(String name, String word) {
        this.name = name;
        this.word = word;
    }

    protected TrieImpl getTrie() {
        return this.<TrieService>getService().lazyGet(getPartitionId(), name);
    }

    @Override
    public String getServiceName() {
        return TrieService.NAME;
    }

    @Override
    public Object getResponse() {
        return response;
    }

    protected void writeInternal(ObjectDataOutput out) throws IOException {
        super.writeInternal(out);
        out.writeUTF(name);
        out.writeUTF(word);
    }

    protected void readInternal(ObjectDataInput in) throws IOException {
        super.readInternal(in);
        name = in.readUTF();
        word = in.readUTF();
    }
}
