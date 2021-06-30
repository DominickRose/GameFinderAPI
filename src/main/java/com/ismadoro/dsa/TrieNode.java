package com.ismadoro.dsa;

//A [a b c d e f g h i l]
//                     |
//                     \/
//                      L [a.... e]
//
//Node: -> one character
//Alex
public class TrieNode {
    public TrieNode[] children;
    public boolean isWord;
    public int id;

    public TrieNode() {
        isWord = false;
        id = -1;
        children = new TrieNode[26];
        for(int i = 0; i < 26; ++i)
            children[i] = null;
    }
}
