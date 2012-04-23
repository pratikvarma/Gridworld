package org.thimm.gridworlds;

import java.lang.Comparable;
import java.util.ArrayList;

/**
 * Implementation of a generic Priority Queue as a Heap.
 * @author pvarma
 *
 * @param <T>
 */

public class PriorityHeap<T extends Comparable<? super T>> {
    
    protected ArrayList<T> tree = null;
    
    public PriorityHeap() {
        tree = new ArrayList<T>();
        //We will work with 1 based index, so set the first item to null.
        tree.add(null);
    }
    
    public PriorityHeap(int size) {
        tree = new ArrayList<T>(size+1);
        //We will work with 1 based index, so set the first item to null.
        tree.add(null);
    }
    
    protected void Heapify_up(int i) {
        if (i > 1) {
            int j = i/2;
            if ((tree.get(i)).compareTo(tree.get(j)) < 0) {
                T tmp = tree.get(i);
                tree.set(i, tree.get(j));
                tree.set(j, tmp);
                Heapify_up(j);
            }
        }
    }
    
    protected void Heapify_down(int i) {
        int n = getCount();
        int j = -1;
        if (2*i > n) {
            return;
        } else if (2*i < n) {
            int left = 2*i;
            int right = left + 1;
            if ((tree.get(left)).compareTo(tree.get(right)) < 0) {
                j = left;
            } else {
                j = right;
            }
        } else if (2*i == n) {
            j = 2*i;
        }
        if ((tree.get(j)).compareTo(tree.get(i)) < 0) {
            T tmp = tree.get(i);
            tree.set(i, tree.get(j));
            tree.set(j, tmp);
            Heapify_down(j);
        }
        
    }
    
    public void insert(T v) {
        tree.add(v);
        Heapify_up(getCount());       
    }
    
    public Object findMin() {
        return tree.get(1);
    }
    
    public void Delete(int i) {
        tree.set(i, tree.get(getCount()));
        tree.remove(getCount());
        Heapify_down(i);
        
    }
    
    public Object extractMin() {
        Object o = tree.get(1);
        Delete(1);
        return o;
    }
    
    public void outputTree() {
        for (int i=1; i<tree.size(); i++) {
            Gridcell g = (Gridcell) tree.get(i);
            System.out.print(g.getF_value());
            System.out.print(" ");
        }
        System.out.println("END");
    }
    
    public int getCount() {
    	return tree.size() - 1;
    }
    
    
    public static void main(String[] args) {
        PriorityHeap<Gridcell> heap = new PriorityHeap<Gridcell>(250);
        heap.insert(new Gridcell(1,1,5));
        heap.insert(new Gridcell(1,1,6));
        heap.insert(new Gridcell(1,1,3));
        heap.insert(new Gridcell(1,1,7));
        heap.insert(new Gridcell(1,1,2));
        heap.insert(new Gridcell(1,1,5));
        heap.insert(new Gridcell(1,1,4));
        heap.outputTree();
        heap.Delete(1);
        heap.outputTree();
        Gridcell g = (Gridcell) heap.extractMin();
        System.out.println(g.getF_value());
    }
}
