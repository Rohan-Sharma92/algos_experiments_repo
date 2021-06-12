package com.experimentcode.algo.subsequnce.count;

import java.util.Scanner;

public class SubsequenceCount {

	public static void main(String[] args) {
		ISubsequenceCountCalculator calculator = new SubsequenceCountCalculator();
		Scanner scan = new Scanner(System.in);
		String sequence = scan.nextLine();
		String subSequence = scan.nextLine();
		scan.close();
		int subSequenceCount = calculator.calculateSubSequenceCount(sequence, subSequence);
		System.out.println("Count:"+subSequenceCount);
	}

}
