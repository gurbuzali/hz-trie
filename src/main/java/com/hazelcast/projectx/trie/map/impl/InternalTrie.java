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

package com.hazelcast.projectx.trie.map.impl;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.List;

public class InternalTrie implements DataSerializable {

    private final TrieNode root = new TrieNode();

    public InternalTrie() {
    }

    public boolean insert(String word) {
        TrieNode current = root;
        for(Character c: word.toCharArray()) {
            current = current.getOrCreateChild(c);
        }
        if (current.isWord()) {
            return false;
        }
        current.setWord();
        return true;
    }

    public boolean contains(String word) {
        TrieNode trieNode = searchInternal(word);
        return trieNode != null && trieNode.isWord();
    }

    public boolean search(String prefix) {
        TrieNode trieNode = searchInternal(prefix);
        return trieNode != null;
    }

    public List<String> closest(String word, int count) {
        TrieNode node = searchInternal(word);
        return null;
    }

    dfs() {

    }

    private TrieNode searchInternal(String s) {
        TrieNode current = root;
        for(Character c: s.toCharArray()) {
            TrieNode child = current.getChild(c);
            if (child == null) {
                return null;
            }
            current = child;
        }
        return current;
    }

    @Override
    public void writeData(ObjectDataOutput out) throws IOException {
        root.writeData(out);
    }

    @Override
    public void readData(ObjectDataInput in) throws IOException {
        root.readData(in);
    }

}
