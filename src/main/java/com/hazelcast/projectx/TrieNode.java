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

package com.hazelcast.projectx;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TrieNode implements DataSerializable {

    private Map<Character, TrieNode> children;

    public TrieNode() {
    }

    public TrieNode(Map<Character, TrieNode> children) {
        this.children = children;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        if (children == null) {
            out.writeInt(0);
        } else {
            out.writeInt(children.size());
            for (Map.Entry<Character, TrieNode> entry : children.entrySet()) {
                out.writeChar(entry.getKey());
                entry.getValue().writeData(out);
            }
        }
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        int size = in.readInt();
        children = new HashMap<>(size);
        for (int i = 0; i < size; i++) {
            Character key = in.readChar();
            TrieNode trieNode = new TrieNode();
            trieNode.readData(in);
            children.put(key, trieNode);
        }
    }
}
