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
import java.util.Objects;
import java.util.Queue;

public class TrieImpl {
    private TrieNode root = new TrieNode();

    public boolean insert(String word) {
        TrieNode current = root;
        for(Character c: word.toCharArray()) {
            current = current.getOrCreateChild(c);
        }
        if (current.isWord()) {
            return false;
        }
        current.setWord(word);
        return true;
    }

    public boolean contains(String word) {
        TrieNode trieNode = searchInternal(word);
        return trieNode != null && trieNode.isWord();
    }

    private TrieNode searchInternal(String word) {
        TrieNode current = root;
        for(Character c: word.toCharArray()) {
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
            TrieNode start = Objects.requireNonNull(toTraverse.poll());
            for (Character childKey : start.childrenKeys()) {
                if (n == closest.size()) return;
                TrieNode child = start.getChild(childKey);
                if (child != null) {
                    if (child.isWord()) {
                        closest.add(child.getValue());
                    }
                    toTraverse.add(child);
                }
            }
        }
    }
}
