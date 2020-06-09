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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TrieContainer {
    private final ConcurrentMap<String, TrieImpl> tries = new ConcurrentHashMap<>();

    public TrieImpl getOrCreate(String name) {
        TrieImpl trie = tries.get(name);
        if (trie != null) {
            return trie;
        }
        trie = new TrieImpl();
        TrieImpl existing = tries.putIfAbsent(name, trie);
        if (existing != null) {
            trie = existing;
        }
        return trie;
    }
}
