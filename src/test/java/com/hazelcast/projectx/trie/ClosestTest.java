package com.hazelcast.projectx.trie;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ClosestTest {
    private TrieImpl trie;

    @BeforeEach
    void setUp() {
        trie = new TrieImpl();
    }

    @Test
    void closestFromEmpty() {
        assertThat(trie.closest("abc", 100)).isEmpty();
    }

    @Test
    void closestToEqual() {
        trie.insert("abc");

        assertThat(trie.closest("abc", 1)).contains("abc");
    }

    @Test
    void firstClosest() {
        trie.insert("abcd");
        trie.insert("abcde");
        trie.insert("abce");
        trie.insert("abce");
        trie.insert("abce");

        assertThat(trie.closest("abc", 1)).contains("abce");
    }

    @Test
    void notEnoughClosest() {
        trie.insert("abcd");
        trie.insert("abcde");
        trie.insert("abce");

        assertThat(trie.closest("abc", 5)).contains("abcd", "abce", "abcde");
    }
}
