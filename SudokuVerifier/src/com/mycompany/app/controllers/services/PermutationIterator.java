package com.mycompany.app.controllers.services;

import java.util.Iterator;

/**
 * Iterator pattern: Generates permutations on demand.
 * 
 * Generates all possible combinations of n positions with values 1-9.
 * For 5 positions: generates [1,1,1,1,1] through [9,9,9,9,9]
 * Total combinations: 9^5 = 59,049
 * 
 * Memory efficient: generates one at a time, doesn't store all combinations.
 * 
 * @author Zeyad
 */
public class PermutationIterator implements Iterator<int[]>{
    private int[] current;
    private int numPositions;
    private int maxValue;
    private boolean hasMore;
    
    /**
     * Creates an iterator for generating permutations.
     * @param numPositions Number of positions (5 for empty cells)
     */
    public PermutationIterator(int numPositions) {
        this.numPositions = numPositions;
        this.maxValue = 9;
        this.current = new int[numPositions];
        this.hasMore = true;
        
        // Initialize to first permutation: [1,1,1,1,1]
        for (int i = 0; i < numPositions; i++) {
            current[i] = 1;
        }
    }
    
    /**
     * Checks if there are more permutations to generate.
     * @return true if more permutations available
     */
    @Override
    public boolean hasNext() {
        return hasMore;
    }
    
    /**
     * Gets the next permutation.
     * 
     * @return Copy of the next permutation, or null if no more
     */
    @Override
    public int[] next() {
        if (!hasMore) {
            return null;
        }
        
        // Make a copy to return (current state BEFORE incrementing)
        int[] result = current.clone();
        
        // increment current for next call
        int position = numPositions - 1;
        
        while (position >= 0) {
            if (current[position] < maxValue) {
                // Can increment this position
                current[position]++;
                break;  // Done
            } else {
                // This position is at max (9)
                current[position] = 1;
                position--;
            }
        }
        
        if (position < 0) {
            hasMore = false;
        }
        
        return result;
    }
    
}
