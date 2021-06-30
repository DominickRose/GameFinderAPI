package com.ismadoro.dsa;

import com.ismadoro.exceptions.DuplicateResourceException;
import com.ismadoro.exceptions.InvalidParamException;
import com.ismadoro.exceptions.ResourceNotFound;

import java.util.ArrayList;
import java.util.List;

public class RepeatSafeTrieTree {
    private final RepeatSafeTrieNode root;
    private RepeatSafeTrieNode curNode;
    //Est. 250k * 109 bytes per node = 27MB Capacity
    private final int maxNodes = 250000;
    private int curNodes = 0;


    public RepeatSafeTrieTree() {
        root = new RepeatSafeTrieNode();
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

    private void removeWordAtCurNode(int id) {
        curNode.isWord = false;
        curNode.idList.remove(new Integer(id));
        if (curNode.idList.size() == 0) {
            curNode.idList = null;
        }
    }

    private void recursiveHelper(RepeatSafeTrieNode node, ArrayList<Integer> curList) {
        //BUG - VERY dangerous Stackoverflow is possible for huge strings!
        if(node.idList != null) curList.addAll(node.idList);
        for(int i = 0; i < 26; ++i) {
            if(node.children[i] != null) recursiveHelper(node.children[i], curList);
        }
    }

    public boolean addWord(String word, int id) {
        if(word.isEmpty())
            throw new InvalidParamException("Word cannot be empty!");
        if(word.length() > 100)
            throw new InvalidParamException("Word cannot exceed 100 characters");

        RepeatSafeTrieNode curNode = root;
        for(int i = 0; i < word.length(); ++i) {
            int curIndex = charToIndex(word.charAt(i));
            if(curNode.children[curIndex] == null) {
                if(curNodes >= maxNodes)
                    throw new OutOfMemoryError("Trie is full more words cannot be added");

                curNode.children[curIndex] = new RepeatSafeTrieNode();
                ++curNodes;
            }
            curNode = curNode.children[curIndex];
        }

        if(curNode.idList == null) {
            curNode.idList = new ArrayList<>();
        }

        curNode.isWord = true;
        curNode.idList.add(id);
        return true;
    }

    public boolean removeWord(String word, int id) {
        //Soft delete
        //Can delete nodes later if trie uses up too much memory
        if(traverseTo(word)) {
            removeWordAtCurNode(id);
            return true;
        }
        return false;
    }

    public boolean updateWord(String prevWord, int prevId, String newWord) {
        //This works assuming the ids will always stay the same
        if(traverseTo(prevWord)) {
            List<Integer> curId = curNode.idList;
            if(curId == null || !curId.contains(prevId))
                throw new ResourceNotFound("Previous word does not exist in the Trie");

            //Ensure we are not updating to a duplicate
            RepeatSafeTrieNode nodePos = curNode;
            if(addWord(newWord, prevId)) {
                curNode = nodePos;
                removeWordAtCurNode(prevId);
                return true;
            }
            return false;
        }
        return false;
    }

    public List<Integer> getIdAt(String word) {
        List<Integer> curId = null;
        if(traverseTo(word)) curId = curNode.idList;
        if(curId == null) throw new ResourceNotFound("No id found at position specified");
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
