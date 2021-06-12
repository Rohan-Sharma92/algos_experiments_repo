package com.experimentcode.ds;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Sorting {

	public static void main(String[] args) {
		int[] arr = new int[] { 4, 5, 6, 5, 4, 3 };
		Map<Integer, Integer> freq = new HashMap<>();
		for (int i : arr)
			freq.put(i, freq.getOrDefault(i, 0) + 1);
		List<Map.Entry<Integer, Integer>> l = new ArrayList<>();
		l.addAll(freq.entrySet());
		Collections.sort(l,
				(a, b) -> a.getValue() != b.getValue() ? a.getValue() - b.getValue() : a.getKey() - b.getKey());
		
		System.out.println("Ans:");
		for(Map.Entry<Integer,Integer> e: l) {
			for(int j=0;j<e.getValue();j++)
				System.out.print(e.getKey()+" ");
		}
		
	}
}
