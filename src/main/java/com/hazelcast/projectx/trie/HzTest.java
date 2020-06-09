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

import com.hazelcast.config.Config;
import com.hazelcast.config.ServiceConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

public class HzTest {
    public static void main(String[] args) {
        startInstance();
        startInstance();

        HazelcastInstance instance = startInstance();
        ITrie trie = instance.getDistributedObject(TrieService.NAME, "test");

        System.out.println(trie.insert("abcd"));

        System.out.println(trie.contains("abcd"));
        System.out.println(trie.contains("abcd1"));


        trie.insert("abce");
        trie.insert("abcf");
        trie.insert("abcf");
        trie.insert("abcf");
        trie.insert("abcf");
        trie.insert("abcg");
        trie.insert("abcg");
        trie.insert("abch");
        trie.insert("abchef");
        trie.insert("abch1");

        System.out.println(trie.closest("abc", 2));
        System.out.println(trie.closest("abc", 5));
        System.out.println(trie.closest("abc", 7));
    }

    private static HazelcastInstance startInstance() {
        return Hazelcast.newHazelcastInstance(new TrieConfig());
    }

    static class TrieConfig extends Config {
        public TrieConfig() {
            getServicesConfig().addServiceConfig(
                    new ServiceConfig()
                            .setEnabled(true)
                            .setName(TrieService.NAME)
                            .setClassName(TrieService.class.getName())
            );
        }
    }
}
