package com.ismadoro.dsa;

import com.ismadoro.exceptions.DuplicateResourceException;
import com.ismadoro.exceptions.InvalidParamException;
import com.ismadoro.exceptions.ResourceNotFound;

import java.util.*;

public class RepeatSafeTrieTree {
    private final RepeatSafeTrieNode root;
    private RepeatSafeTrieNode curNode;
    //Est. 125k * 213 bytes per node = 27MB Capacity
    private final int maxNodes = 125000;
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
        curNode.idList.remove(new Integer(id));
        if (curNode.idList.size() == 0) {
            curNode.isWord = false;
            curNode.idList = null;
        }
    }

    private void iterativeHelper(RepeatSafeTrieNode node, ArrayList<Integer> curList) {
        LinkedList<RepeatSafeTrieNode> callStack = new LinkedList<>();
        HashMap<RepeatSafeTrieNode, Integer> nodeToCurChildIndex = new HashMap<>();
        HashSet<Integer> seenIds = new HashSet<>();

        callStack.addLast(node);
        nodeToCurChildIndex.put(node, 0);
        RepeatSafeTrieNode currentNode = null;
        while(!callStack.isEmpty()) {
            //Populate the stack
            while(callStack.getLast() != currentNode) {
                //Process current node
                currentNode = callStack.getLast();
                //Add our id if we havent seen it before
                for(int i = 0; currentNode.idList != null && i < currentNode.idList.size(); ++i) {
                    if(!seenIds.contains(currentNode.idList.get(i))) {
                        curList.add(currentNode.idList.get(i));
                        seenIds.add(currentNode.idList.get(i));
                    }
                }

                //Iterate to find a child to add to the stack
                for(int i = nodeToCurChildIndex.get(currentNode); i < 26; ++i) {
                    //If we find a child node add it to the stack
                    if(currentNode.children[i] != null) {
                        callStack.addLast(currentNode.children[i]);
                        nodeToCurChildIndex.replace(currentNode, i+1);
                        nodeToCurChildIndex.put(currentNode.children[i], 0);
                        break;
                    }
                }
            }
            //Remove top node from the stack (it has no more children)
            callStack.removeLast();
            currentNode = null;
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
        //if(word.length() > 100)
        //    throw new InvalidParamException("Word cannot exceed 100 characters");

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
        if(prevWord.equals(newWord))
            return false;
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
            //recursiveHelper(curNode, foundIds);
            iterativeHelper(curNode, foundIds);
        }
        return foundIds;
    }
}
