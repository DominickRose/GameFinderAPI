package com.ismadoro.dsa;

import java.util.ArrayList;
import java.util.List;

//A [a b c d e f g h i l]
//                     |
//                     \/
//                      L [a.... e]
//
//Node: -> one character
//Alex
public class RepeatSafeTrieNode {
    public RepeatSafeTrieNode[] children;
    public boolean isWord;
    public List<Integer> idList;

    public RepeatSafeTrieNode() {
        isWord = false;
        idList = null;
        children = new RepeatSafeTrieNode[26];
        for(int i = 0; i < 26; ++i)
            children[i] = null;

    }
}
