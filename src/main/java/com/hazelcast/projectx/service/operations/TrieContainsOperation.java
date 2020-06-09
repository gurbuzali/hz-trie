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

package com.hazelcast.projectx.service.operations;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.projectx.service.TrieImpl;
import com.hazelcast.projectx.service.TrieService;
import com.hazelcast.spi.impl.operationservice.Operation;

import java.io.IOException;

public class TrieContainsOperation extends Operation {
    private String name;
    private String word;
    private boolean response;

    public TrieContainsOperation() {
    }

    public TrieContainsOperation(final String name, final String word) {
        this.name = name;
        this.word = word;
    }

    @Override
    public Object getResponse() {
        return response;
    }

    @Override
    public void run() {
        TrieService service = getService();
        TrieImpl trie = service.getTrie(name);
        response = trie.contains(word);
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
