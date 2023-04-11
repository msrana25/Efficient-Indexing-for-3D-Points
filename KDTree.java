package KDtree;

import java.util.ArrayList;
import java.util.List;

public class KDTree {
    
    private static final int K = 3; // number of dimensions
    
    private static class Node {
        double[] point;
        Node left;
        Node right;
        
        public Node(double[] point) {
            this.point = point;
            this.left = null;
            this.right = null;
        }
    }
    
    private Node root;
    
    public KDTree(List<double[]> points) {
        this.root = buildTree(points);
    }
    
    private Node buildTree(List<double[]> points) {
        Node root = null;
        for (double[] point : points) {
            root = insert(root, point, 0);
        }
        return root;
    }

    private Node insert(Node node, double[] point, int depth) {
        if (node == null) {
            return new Node(point);
        }
        
        int axis = depth % K;
        if (point[axis] < node.point[axis]) {
            node.left = insert(node.left, point, depth + 1);
        } else {
            node.right = insert(node.right, point, depth + 1);
        }
        
        return node;
    }
    
    // Alternative build Implementation(Initially the resources required for setting up the tree is very high fir this case.
    /*
     *  public KDTree(List<double[]> points) {
        this.root = buildTree(points, 0);
    }
    
    private Node buildTree(List<double[]> points, int depth) {
        if (points.isEmpty()) {
            return null;
        }
        
        int axis = depth % K;
        points.sort((a, b) -> Double.compare(a[axis], b[axis]));
        
        int medianIndex = points.size() / 2;
        double[] medianPoint = points.get(medianIndex);
        
        Node node = new Node(medianPoint);
        node.left = buildTree(points.subList(0, medianIndex), depth + 1);
        node.right = buildTree(points.subList(medianIndex + 1, points.size()), depth + 1);
        
        return node;
    }
     */

    
    public List<double[]> rangeQuery(double[] min, double[] max) {
        List<double[]> result = new ArrayList<>();
        rangeQuery(root, min, max, result, 0);
        return result;
    }
    
    private void rangeQuery(Node node, double[] min, double[] max, List<double[]> result, int depth) {
        if (node == null) {
            return;
        }
        
        int axis = depth % K;
        
        if (node.point[axis] >= min[axis] && node.point[axis] <= max[axis]) {
            // point is within range
            if (node.point[0] >= min[0] && node.point[1] >= min[1] && node.point[2] >= min[2] &&
                node.point[0] <= max[0] && node.point[1] <= max[1] && node.point[2] <= max[2]) {
                result.add(node.point);
            }
            rangeQuery(node.left, min, max, result, depth + 1);
            rangeQuery(node.right, min, max, result, depth + 1);
        } else if (node.point[axis] < min[axis]) {
            // point is to the left of the range
            rangeQuery(node.right, min, max, result, depth + 1);
        } else {
            // point is to the right of the range
            rangeQuery(node.left, min, max, result, depth + 1);
        }
    }
    
    public List<double[]> findNearestNeighbors(double[] queryPoint) {
        List<Node> nearest = new ArrayList<>();
        List<double[]> result  = new ArrayList<>();
        nearestNeighbor(root, queryPoint, nearest, 0);
        for (Node node:nearest )
        {
        	
        	double x = node.point[0];
        	double y = node.point[1];
        	double z = node.point[2];
        	double[] coordinates = new double[] {x,y,z};
        	result.add(coordinates);
        }
        return result;
    }

    private void nearestNeighbor(Node node, double[] queryPoint, List<Node> nearest, int depth) {
        if (node == null) {
            return;
        }

        if (nearest.isEmpty() || distance(node.point, queryPoint) < distance(nearest.get(0).point, queryPoint)) {
            nearest.clear();
            nearest.add(node);
        } else if (distance(node.point, queryPoint) == distance(nearest.get(0).point, queryPoint)) {
            nearest.add(node);
        }

        int axis = depth % K;
        Node first = null;
        Node second = null;
        if (queryPoint[axis] < node.point[axis]) {
            first = node.left;
            second = node.right;
        } else {
            first = node.right;
            second = node.left;
        }

        nearestNeighbor(first, queryPoint, nearest, depth + 1);
        if (second != null && distance(queryPoint, nearest.get(0).point) >= Math.abs(node.point[axis] - queryPoint[axis])) {
            nearestNeighbor(second, queryPoint, nearest, depth + 1);
        }
    }



    private double distance(double[] p1, double[] p2) {
        double sum = 0.0;
        for (int i = 0; i < K; i++) {
            double diff = p1[i] - p2[i];
            sum += diff * diff;
        }
        return sum;
    }
    
    private int InOrder(Node root, int count)
    {
    	if (root == null) return count;
    	count = InOrder(root.left,count);
    	count ++;
    	count = InOrder(root.right,count);
    	return count;
    }
    
    private int LeafNodes(Node root, int count)
    {
    	if (root == null)
    	{
    		return count;
    	}
    	
    	if (root.left == null && root.right == null) {
            count++;
        }
    	
    	count = LeafNodes(root.left,count);
    	count = LeafNodes(root.right,count);
    	
    	return count;
    }
    
    public double sizeOfIndex()
    {
    	int totalNodes = InOrder(root,0);
    	int leafNodes = LeafNodes(root, 0);
    	// size = (totalNodes * (Overhead of an array object + 3 double values inside array object) + (Number of pointers) * pointer_size) bytes.
    	double size = totalNodes * (12+(3*8)) + 2 *(totalNodes-leafNodes) * 8;
    	return size;
    }
    
    public int maxDepth(Node root)
    {
    	if (root == null) return 0;
    	
    	int left = maxDepth(root.left);
    	int right = maxDepth(root.right);
    	
    	return 1+Math.max(left, right);
    }
    
    public int display()
    {
    	return InOrder(root,0);
    }

}

           
