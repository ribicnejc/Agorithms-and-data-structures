package aps2.binomialheap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Vector;

/**
 * This class is an implementation of the Binomial min-heap.
 */
public class BinomialHeap {
	Vector<BinomialNode> data; // list of root nodes
	int n;                     // number of elements
	
	BinomialHeap(){
		data = new Vector<BinomialNode>();
	}
	
	/**
	 * Inserts a new key to the binomial heap and consolidates the heap.
	 * Duplicates are allowed.
	 * 
	 * @param key Key to be inserted
	 * @return True, if the insertion was successful; False otherwise.
	 */
	public boolean insert(int key) {
		BinomialNode node = new BinomialNode(key);
		this.data.add(node);
		sortData();
		this.n++;
		consolidate();
		return true;
	}
	
	/**
	 * Returns the minimum element in the binomial heap. If the heap is empty,
	 * return the maximum integer value.
	 * 
	 * @return The minimum element in the heap or the maximum integer value, if the heap is empty.
	 */
	public int getMin() {
		if (this.data.isEmpty()) return Integer.MAX_VALUE;
		return this.data.get(0).getKey();
	}
	
	/**
	 * Find and removes the minimum element in the binomial heap and
	 * consolidates the heap.
	 * 
	 * @return True, if the element was deleted; False otherwise.
	 */
	public boolean delMin() {
		if (this.data.isEmpty()) return false;
		this.data.addAll(this.data.get(0).getChildren());
		this.data.remove(0);
		this.n--;
		consolidate();
		return true;
	}
	
	/**
	 * Merges two binomial trees.
	 * 
	 * @param t1 The first tree
	 * @param t2 The second tree
	 * @return Returns the new parent tree
	 */
	public static BinomialNode mergeTrees(BinomialNode t1, BinomialNode t2) {
		if (t1.getKey() <= t2.getKey()){
			t1.addChild(t2);
			return t1;
		}else{
			t2.addChild(t1);
			return t2;
		}
	}
	
	/**
	 * This function consolidates the binomial heap ie. merges the binomial
	 * trees with the same degree into a single one.
	 * 
	 * @return True, if changes were made to the list of root nodes; False otherwise.
	 */
	private boolean consolidate() {
		boolean wasChange = false;
		for (int i = 0; i < data.size(); i++){
			BinomialNode node1 = data.get(i);
			for (int j = 0; j < data.size(); j++){
				BinomialNode node2 = data.get(j);
				if (node1.getChildren().size() == node2.getChildren().size() && i != j){
					node1 = mergeTrees(node1, node2);
					data.set(i, node1);
					data.remove(j);

					i = 0;
					wasChange = true;
					break;
				}
			}
		}
		sortData();
		return wasChange;
	}


	public void sortData(){
		this.data.sort(new Comparator<BinomialNode>() {
			@Override
			public int compare(BinomialNode o1, BinomialNode o2) {
				return o2.compare(o1);
			}
		});
	}
}

