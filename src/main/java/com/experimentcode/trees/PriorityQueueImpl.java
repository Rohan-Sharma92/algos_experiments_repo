package com.experimentcode.trees;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class PriorityQueueImpl {

	public static void main(String[] args) {
		PriorityQueue<Count> queue = new PriorityQueue<>(new Comparator<Count>() {

			public int compare(Count c, Count c1) {
				if(c.freq!=c1.freq)
					return c1.freq-c.freq;
				return c.pos-c1.pos;
			}
		});
		Count c1 = new Count();
		c1.pos = 0;
		c1.freq = 1;

		Count c2 = new Count();
		c2.pos = 1;
		c2.freq = 0;

		Count c3 = new Count();
		c3.pos = 2;
		c3.freq = 2;
		
		queue.add(c1);
		queue.add(c2);
		queue.add(c3);

		while(!queue.isEmpty()) {
			Count c = queue.poll();
			System.out.println("Pos:" + c.pos + ":Freq:" + c.freq);	
		}
//		
//		
//		System.out.println("Next :");
//		Count c4 = new Count();
//		c4.pos = 0;
//		c4.freq = 5;
//		List<Count> l = new ArrayList<>();
//		while(!queue.isEmpty() && queue.peek().pos!=c4.pos) {
//			l.add(queue.poll());
//		}
//		if(!queue.isEmpty())
//		queue.poll();
//		queue.add(c4);
//		queue.addAll(l);
//		for(Count c: queue) {
//			System.out.println("Pos:" + c.pos + ":Freq:" + c.freq);	
//		}

	}
}

class Count {
	int freq = 0;
	int pos = 0;
}
