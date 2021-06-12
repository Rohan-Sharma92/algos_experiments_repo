package com.experimentcode.algo.subsequnce.count;

import java.util.Arrays;

public class SubsequenceCountCalculator implements ISubsequenceCountCalculator{

	public int calculateSubSequenceCount(String sequence, String subsequence) {
		Integer[] subSequenceCharCount = new Integer[256];
		Integer[] subSequenceWindowCharCount = new Integer[256];
		Arrays.fill(subSequenceCharCount,0);
		Arrays.fill(subSequenceWindowCharCount,0);
		int count=0;
		char[] subSequenceCharArray = subsequence.toCharArray();
		for(int i=0;i<subSequenceCharArray.length;i++) {
			subSequenceCharCount[subSequenceCharArray[i]]++;
			subSequenceWindowCharCount[sequence.charAt(i)]++;
		}
		for(int i=subsequence.length();i<sequence.length();i++){
			if(compareArrays(subSequenceWindowCharCount,subSequenceCharCount)) {
				count++;
			}
			subSequenceWindowCharCount[sequence.charAt(i)]++;
			subSequenceWindowCharCount[sequence.charAt(i-subsequence.length())]--;
		}
		if(compareArrays(subSequenceWindowCharCount,subSequenceCharCount)) {
			count++;
		}
		return count;
	}

	private boolean compareArrays(Integer[] extracted, Integer[] subSequenceCharCount) {
		for(int i=0;i<subSequenceCharCount.length;i++) {
			if(!extracted[i].equals(subSequenceCharCount[i])) {
				return false;
			}
		}
		return true;
	}

	private Integer[] extracted(String sequence, int j, int size) {
		 Integer[] sequenceWindowCharCount= new Integer[256];
		 Arrays.fill(sequenceWindowCharCount,0);
		char[] charArray = sequence.substring(j, size+1).toCharArray();
		for(int i=0;i<charArray.length;i++) {
			sequenceWindowCharCount[charArray[i]]++;
		}
		return sequenceWindowCharCount;
	}

}
