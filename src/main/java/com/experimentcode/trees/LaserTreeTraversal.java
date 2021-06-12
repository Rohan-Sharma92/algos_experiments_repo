package com.experimentcode.trees;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class LaserTreeTraversal {

	public static void main(String[] args) {
		Node root = new Node();
		root.val = 5;
		createTree(root, 3, 6);
		createTree(root.left, 2, 4);
		createTree(root.left.left, 1, 0);
		createTree(root.right, 7, 8);
		createTree(root.right.right, 0, 9);
		Map<Integer, Node> map = new HashMap<>();
		// int height = height(root);
//		Wrap dfsWrap = dfs(root);
//		Map<Node, Coordinate> cartesianPlane = dfsWrap.cartesianPlane;
//		List<Node> dfsTraversal = dfsWrap.dfsTraversal;
//		int start = cartesianPlane.get(dfsTraversal.get(0)).x;
//		Map<Integer, Node> map = new HashMap<>();
//		for (Node e : dfsTraversal) {
//			Coordinate coordinate = cartesianPlane.get(e);
//			int slope=coordinate.y-coordinate.x;
//			System.out.println(e.val+":Slope:"+slope);
//			if (!map.containsKey(slope)) {
//				map.put(slope, e);
//				
//			}
//		}
		postOrder(root, 0, 0, map);
		for (Node n : map.values())
			System.out.println(n.val);
	}

	private static int height(Node root) {
		if (root == null)
			return 0;
		return Math.max((1 + height(root.left)), (1 + height(root.right)));
	}

	private static Wrap dfs(Node root) {
		Map<Node, Coordinate> dfsMap = new HashMap<>();
		List<Node> dfsSet = new ArrayList<>();
		Stack<Node> stack = new Stack<Node>();
		int i = 0;
		Coordinate coordinate = new Coordinate();
		coordinate.x = i;
		coordinate.y = i;
		dfsMap.put(root, coordinate);
		stack.push(root);
		while (!stack.isEmpty()) {
			Node pop = stack.pop();
			Coordinate parentCoordinate = dfsMap.get(pop);
			if (pop.left != null) {
				stack.push(pop.left);
				Coordinate childCoordinate = new Coordinate();
				childCoordinate.x = parentCoordinate.x - 1;
				childCoordinate.y = parentCoordinate.y - 1;
				dfsMap.put(pop.left, childCoordinate);
			} else if (pop.right != null) {
				stack.push(pop.right);
				Coordinate childCoordinate = new Coordinate();
				childCoordinate.x = parentCoordinate.x + 1;
				childCoordinate.y = parentCoordinate.y - 1;
				dfsMap.put(pop.right, childCoordinate);
			} else
				dfsSet.add(pop);
		}
		Wrap wrap = new Wrap();
		wrap.cartesianPlane = dfsMap;
		wrap.dfsTraversal = dfsSet;
		return wrap;
	}

	private static void createTree(Node root, int left, int right) {
		if (left != 0) {
			Node leftNode = new Node();
			leftNode.val = left;
			root.left = leftNode;
		}
		if (right != 0) {
			Node rightNode = new Node();
			rightNode.val = right;
			root.right = rightNode;
		}
	}

	private static void postOrder(Node n, int x, int y, Map<Integer, Node> map) {
		if(n==null)
			return;
		if(n.left!=null)
		postOrder(n.left, x - 1, y - 1, map);
		if(n.right!=null)
		postOrder(n.right, x + 1, y - 1, map);
		int c = y - x;
		if (!map.containsKey(c)) {
			map.put(c, n);
		}
	}
}

class Node {
	int val;
	Node left;
	Node right;
}

class Coordinate {
	int x, y;
}

class Line {
	int x, y, c;
}

class Wrap {
	List<Node> dfsTraversal;
	Map<Node, Coordinate> cartesianPlane;
}