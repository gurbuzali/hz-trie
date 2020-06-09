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
import com.hazelcast.config.MapConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.hazelcast.projectx.trie.impl.InsertEntryProcessor;
import com.hazelcast.projectx.trie.impl.InternalTrie;

import static com.hazelcast.config.InMemoryFormat.OBJECT;

public class TheTest {

    public static void main(String[] args) throws Exception {
        Config config = new Config();
        config.addMapConfig(new MapConfig().setName("trie").setInMemoryFormat(OBJECT));
        HazelcastInstance instance = Hazelcast.newHazelcastInstance(config);

        IMap<String, InternalTrie> map = instance.getMap("trie");

        map.put("a", new InternalTrie());

        Boolean inserted = map.executeOnKey("a", new InsertEntryProcessor("ali"));
        System.out.println(inserted);

        inserted = map.executeOnKey("a", new InsertEntryProcessor("ali"));
        System.out.println(inserted);

    }


}
