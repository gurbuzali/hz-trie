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

import com.hazelcast.internal.partition.IPartitionService;

public final class TrieUtil {

    private TrieUtil() {
    }

    public static void checkIfEmpty(String word) {
        if (word == null || word.length() < 2) {
            throw new IllegalArgumentException("Word should contain at least 2 character");
        }
    }

    public static String partitionKey(String word) {
        return word.substring(0, 2);
    }

    public static void checkCount(int n) {
        if (n<1) {
            throw new IllegalArgumentException("Count should be positive integer");
        }
    }
}
