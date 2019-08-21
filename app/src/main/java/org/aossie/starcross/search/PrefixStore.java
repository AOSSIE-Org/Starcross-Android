package org.aossie.starcross.search;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PrefixStore {

    static private class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        Set<String> results = new HashSet<>();
    }

    private TrieNode root = new TrieNode();

    private static final Set<String> EMPTY_SET = Collections.unmodifiableSet(new HashSet<String>());

    public Set<String> queryByPrefix(String prefix) {
        prefix = prefix.toLowerCase();
        TrieNode n = root;
        for (int i = 0; i < prefix.length(); i++) {
            TrieNode c = n.children.get(prefix.charAt(i));
            if (c == null) {
                return EMPTY_SET;
            }
            n = c;
        }
        Set<String> coll = new HashSet<String>();
        collect(n, coll);
        return coll;
    }

    private void collect(TrieNode n, Collection<String> coll) {
        coll.addAll(n.results);
        for (Character ch : n.children.keySet()) {
            collect(n.children.get(ch), coll);
        }
    }

    public void add(String string) {
        TrieNode n = root;
        String lower = string.toLowerCase();
        for (int i = 0; i < lower.length(); i++) {
            TrieNode c = n.children.get(lower.charAt(i));
            if (c == null) {
                c = new TrieNode();
                n.children.put(lower.charAt(i), c);
            }
            n = c;
        }
        n.results.add(string);
    }

    public void addAll(Collection<String> strings) {
        for (String string : strings) {
            add(string);
        }
    }
}
