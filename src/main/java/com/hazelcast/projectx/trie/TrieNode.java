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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TrieNode implements Comparable<TrieNode> {
    
    private final Map<Character, TrieNode> children = new HashMap<>();
    private String word;
    private long weight;

    public TrieNode() {
    }

    public boolean isWord() {
        return word != null;
    }

    public void setWord(String word) {
        this.word = word;
        weight += 1;
    }

    public TrieNode getOrCreateChild(Character c) {
        return children.computeIfAbsent(c, k -> new TrieNode());
    }

    public TrieNode getChild(Character c) {
        return children.get(c);
    }

    public Set<Map.Entry<Character, TrieNode>> childrenSet() {
        return children.entrySet();
    }

    public String getValue() {
        return word;
    }

    @Override
    public int compareTo(TrieNode other) {
        return Long.compare(other.weight, weight);
    }
}
