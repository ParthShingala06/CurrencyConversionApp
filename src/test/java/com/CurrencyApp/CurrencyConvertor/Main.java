package com.CurrencyApp.CurrencyConvertor;// Java implementation of counting the

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

// Trie Node
class TrieNode
{

	// Using map to store the pointers
// of children nodes for dynamic
// implementation, for making the
// program space efficient
	Map<Character, TrieNode> children;

	// If isEndOfWord is true, then
// node represents end of word
	boolean isEndOfWord;

	// num represents number of times
// a character has appeared during
// insertion of the words in the
// trie
	Map<Character, Integer> num;

	public TrieNode() {
		this.children = new HashMap<>();
		this.isEndOfWord = false;
		this.num = new HashMap<>();
	}
}

public class Main {
	// Declare root node
	static TrieNode root;

	// Function to create New Trie Node
	static TrieNode getNewTrieNode() {
		TrieNode pNode = new TrieNode();
		pNode.isEndOfWord = false;
		return pNode;
	}

	// Function to insert a string in trie
	static void insertWord(String word) {
		// To hold the value of root
		TrieNode current = root;

		// To hold letters of the word
		char s;

		// Traverse through strings in list
		for (int i = 0; i < word.length(); i++) {
			s = word.charAt(i);

			// If s is not present in the
			// character field of current node
			if (!current.children.containsKey(s)) {
				// Get new node
				TrieNode p = getNewTrieNode();

				// Insert s in character
				// field of current node
				// with reference to node p
				current.children.put(s, p);

				// Insert s in num field
				// of current node with
				// value 1
				current.num.put(s, 1);
			}
			else {
				// Increment the count
				// corresponding to the
				// character s
				current.num.put(s, current.num.get(s) + 1);
			}

			// Go to next node
			current = current.children.get(s);
		}
		current.isEndOfWord = true;
	}

	// Function to count the number of
// words in trie with given prefix
	static int countWords(Vector<String> words, String prefix) {
		root = getNewTrieNode();

		// Size of list of string
		int n = words.size();

		// Construct trie containing
		// all the words
		for (int i = 0; i < n; i++) {
			insertWord(words.get(i));
		}

		TrieNode current = root;
		char s;

		// Initialize the wordCount = 0
		int wordCount = 0;

		for (int i = 0; i < prefix.length(); i++) {
			s = prefix.charAt(i);

			// If the complete prefix is
			// not present in the trie
			if (!current.children.containsKey(s)) {

				// Make wordCount 0 and
				// break out of loop
				wordCount = 0;
				break;
			}

			// Update the wordCount
			wordCount = current.num.get(s);


			// Go to next node
			current = current.children.get(s);
		}

		return wordCount;
	}


	// Driver Code
	public static void main(String[] args)
	{
		// input list of words
		Vector<String> words = new Vector<>();
		words.add("apk");
		words.add("app");
		words.add("apple");
		words.add("arp");
		words.add("array");

		// Given prefix to find
		String prefix = "ap";

		// Print the number of words with
		// given prefix
		System.out.println(countWords(words, prefix));
	}
}

// This code is contributed by Aman Kumar.
