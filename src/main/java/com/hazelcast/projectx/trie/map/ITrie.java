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

package com.hazelcast.projectx.trie.map;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.projectx.trie.map.impl.ContainsEntryProcessor;
import com.hazelcast.projectx.trie.map.impl.InsertEntryProcessor;
import com.hazelcast.projectx.trie.map.impl.InternalTrie;
import com.hazelcast.projectx.trie.map.impl.SearchEntryProcessor;
import com.hazelcast.projectx.trie.map.impl.TrieKey;

import java.util.List;

import static com.hazelcast.config.InMemoryFormat.OBJECT;

public final class ITrie {

    private final IMap<TrieKey, InternalTrie> map;

    private ITrie(IMap<TrieKey, InternalTrie> map) {
        this.map = map;
    }

    public static ITrie trie(HazelcastInstance instance, String name) {
        Config config = instance.getConfig();
        config.addMapConfig(new MapConfig().setName(name).setInMemoryFormat(OBJECT));
        return new ITrie(instance.getMap(name));
    }

    public boolean insert(String word) {
        checkEmpty(word);
        TrieKey key = key(word);
        return map.executeOnKey(key, new InsertEntryProcessor(word));
    }

    public boolean contains(String word) {
        checkEmpty(word);
        TrieKey key = key(word);
        return map.executeOnKey(key, new ContainsEntryProcessor(word));
    }

    public boolean search(String prefix) {
        checkEmpty(prefix);
        TrieKey key = key(prefix);
        return map.executeOnKey(key, new SearchEntryProcessor(prefix));
    }

    public List<String> closest(String prefix, int count) {
        checkEmpty(prefix);
        checkCount(count);
        TrieKey key = key(prefix);

    }

    private TrieKey key(String word) {
        if (word.length() == 1) {
            return new TrieKey(word.charAt(0), ' ');
        }
        return new TrieKey(word.charAt(0), word.charAt(1));
    }

    private void checkEmpty(String word) {
        if (word.isEmpty()) {
            throw new IllegalArgumentException("Word cannot be empty");
        }
    }

    private void checkCount(int count) {
        if (count<1) {
            throw new IllegalArgumentException("Count should be a positive integer");
        }
    }

}
