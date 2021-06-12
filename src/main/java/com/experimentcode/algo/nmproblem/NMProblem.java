package com.experimentcode.algo.nmproblem;

import java.util.concurrent.PriorityBlockingQueue;

public class NMProblem {
	static PriorityBlockingQueue<Integer> heap = new PriorityBlockingQueue<Integer>();
	public static void main(String[] args) {
		System.out.println("Rohan");
		String pattern="NN";
		for(int k=1;k<=pattern.length()+1;k++){
			heap.add(k);
		}
		String res="";
		for(int i=0;i<pattern.length();i++) {
			if(pattern.charAt(i)=='M') {
				int c=0;
				for(int j=i;j<pattern.length();j++) {
					if(pattern.charAt(j)=='M'){
						c++;
					}else {
						break;
					}
				}
				res+= getElement(c+1);
			}
			else {
				res+=getElement(1);
			}
		}
		System.out.println(res+heap.poll());
		
	}

	private static Integer getElement(int i) {
		PriorityBlockingQueue<Integer> q1 =  new PriorityBlockingQueue<Integer>();
		Integer num=0;
		while(i-- > 0) {
			num=heap.poll();
			if(i>0)
				q1.add(num);
		}
		heap.addAll(q1);
		return num;
	}
}

