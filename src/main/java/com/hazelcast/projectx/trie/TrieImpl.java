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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;

public class TrieImpl {

    private final TrieNode root = new TrieNode();

    public void insert(String word) {
        TrieNode current = root;
        for (Character c : word.toCharArray()) {
            current = current.getOrCreateChild(c);
        }
        current.setWord(word);
    }

    public boolean contains(String word) {
        TrieNode trieNode = searchInternal(word);
        return trieNode != null && trieNode.isWord();
    }

    private TrieNode searchInternal(String word) {
        TrieNode current = root;
        for (Character c : word.toCharArray()) {
            TrieNode child = current.getChild(c);
            if (child == null) {
                return null;
            }
            current = child;
        }
        return current;
    }

    public Collection<String> closest(String prefix, int n) {
        TrieNode trieNode = searchInternal(prefix);
        if (trieNode != null) {
            List<String> closest = new ArrayList<>();
            if (trieNode.isWord()) {
                closest.add(prefix);
            }
            Queue<TrieNode> queue = new ArrayDeque<>();
            queue.add(trieNode);
            closest(n, closest, queue);
            return closest;
        }

        return Collections.emptyList();
    }

    private void closest(int n, List<String> closest, Queue<TrieNode> toTraverse) {
        while (!toTraverse.isEmpty() && closest.size() < n) {
            TrieNode node = Objects.requireNonNull(toTraverse.poll());
            PriorityQueue<TrieNode> children = new PriorityQueue<>();
            for (Map.Entry<Character, TrieNode> entry : node.childrenSet()) {
                TrieNode child = entry.getValue();
                children.add(child);
                toTraverse.add(child);
            }
            while (!children.isEmpty()) {
                TrieNode child = children.poll();
                if (child.isWord()) {
                    closest.add(child.getValue());
                }
                if (n == closest.size()) {
                    return;
                }
            }
        }
    }
}
