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
import com.hazelcast.internal.util.ExceptionUtil;
import com.hazelcast.spi.properties.ClusterProperty;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class HzTest {

    public static void main(String[] args) throws IOException {

        startInstance();
        HazelcastInstance instance = startInstance();

        ITrie trie = instance.getDistributedObject(TrieService.NAME, "test");

        long begin = System.currentTimeMillis();
        Path books = Paths.get("books");
        Pattern delimiter = Pattern.compile("\\W+");
        Files.list(books)
             .parallel()
             .flatMap(HzTest::lines)
             .flatMap(line -> Arrays.stream(delimiter.split(line)))
             .filter(word -> word.length() > 1)
             .forEach(trie::insert);
        long elapsed = System.currentTimeMillis() - begin;
        System.out.println("populated in " + elapsed + " ms.");



        System.out.println(trie.closest("sto", 5));

        System.out.println(trie.closest("fak", 5));
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

    static Stream<String> lines(Path path) {
        try {
            return Files.lines(path);
        } catch (IOException e) {
            throw ExceptionUtil.sneakyThrow(e);
        }
    }
}
