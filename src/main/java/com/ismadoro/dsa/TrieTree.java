package com.ismadoro.dsa;

import com.ismadoro.dsa.TrieNode;
import com.ismadoro.entities.Player;
import com.ismadoro.exceptions.DuplicateResourceException;
import com.ismadoro.exceptions.ResourceNotFound;
import com.ismadoro.exceptions.InvalidParamException;

import java.util.ArrayList;
import java.util.List;

public class TrieTree {
    private final TrieNode root;
    private TrieNode curNode;
    //Est. 250k * 109 bytes per node = 27MB Capacity
    private final int maxNodes = 250000;
    private int curNodes = 0;


    public TrieTree() {
        root = new TrieNode();
    }
    private int charToIndex(char c) {
        //Convert letter to lowercase and check if its valid
        int curIndex = (c | 32) - 'a';
        if(curIndex < 0 || curIndex > 25)
            throw new InvalidParamException("Word must consist of letters only!");
        return curIndex;
    }
    private boolean traverseTo(String word) {
        curNode = root;
        for(int i = 0; i < word.length(); ++i) {
            int curIndex = charToIndex(word.charAt(i));
            if(curNode.children[curIndex] == null)
                throw new ResourceNotFound("This word does not exist in the Trie");
            curNode = curNode.children[curIndex];
        }
        return true;
    }
    private void removeWordAtCurNode() {
        curNode.isWord = false;
        curNode.id = -1;
    }
    private void recursiveHelper(TrieNode node, ArrayList<Integer> curList) {
        //BUG - VERY dangerous Stackoverflow is possible for huge strings!
        if(node.id != -1) curList.add(node.id);
        for(int i = 0; i < 26; ++i) {
            if(node.children[i] != null) recursiveHelper(node.children[i], curList);
        }
    }

    public boolean addWord(String word, int id) {
        if(word.isEmpty())
            throw new InvalidParamException("Word cannot be empty!");
        if(word.length() > 100)
            throw new InvalidParamException("Word cannot exceed 100 characters");

        TrieNode curNode = root;
        for(int i = 0; i < word.length(); ++i) {
            int curIndex = charToIndex(word.charAt(i));
            if(curNode.children[curIndex] == null) {
                if(curNodes >= maxNodes)
                    throw new OutOfMemoryError("Trie is full more words cannot be added");

                curNode.children[curIndex] = new TrieNode();
                ++curNodes;
            }
            curNode = curNode.children[curIndex];
        }

        if(curNode.isWord)
.
        throw new DuplicateResourceException("This entry already exists!");

        curNode.isWord = true;
        curNode.id = id;
        return true;
    }
    public boolean removeWord(String word) {
        //Soft delete
        //Can delete nodes later if trie uses up too much memory
        if(traverseTo(word)) {
            removeWordAtCurNode();
            return true;
        }
        return false;
    }
    public boolean updateWord(String prevWord, String newWord) {
        //This works assuming the ids will always stay the same
        if(traverseTo(prevWord)) {
            int curId = curNode.id;
            if(curId == -1)
                throw new ResourceNotFound("Previous word does not exist in the Trie");

            //Ensure we are not updating to a duplicate
            TrieNode nodePos = curNode;
            if(addWord(newWord, curId)) {
                curNode = nodePos;
                removeWordAtCurNode();
                return true;
            }
            return false;
        }
        return false;
    }
    public int getIdAt(String word) {
        int curId = -1;
        if(traverseTo(word)) curId = curNode.id;
        if(curId == -1) throw new ResourceNotFound("No id found at position specified");
        return curId;
    }
    public ArrayList<Integer> getAllIdsStartingWith(String word) {
        ArrayList<Integer> foundIds = new ArrayList<>();
        if(traverseTo(word)) {
            recursiveHelper(curNode, foundIds);
        }
        return foundIds;
    }
}
