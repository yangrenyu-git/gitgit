package com.javaxxz.core.toolbox.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;


public class BoundedPriorityQueue<E> extends PriorityQueue<E>{
	private static final long serialVersionUID = 3794348988671694820L;
	
	//容量
	private int capacity;
	private Comparator<? super E> comparator;
	
	public BoundedPriorityQueue(int capacity) {
		this(capacity, null);
	}
	

	public BoundedPriorityQueue(int capacity, final Comparator<? super E> comparator) {
		super(capacity, new Comparator<E>(){

			@Override
			public int compare(E o1, E o2) {
				int cResult = 0;
				if(comparator != null) {
					cResult = comparator.compare(o1, o2);
				}else {
					@SuppressWarnings("unchecked")
					Comparable<E> o1c = (Comparable<E>)o1;
					cResult = o1c.compareTo(o2);
				}
				
				return - cResult;
			}
			
		});
		this.capacity = capacity;
		this.comparator = comparator;
	}


	@Override
	public boolean offer(E e) {
		if(size() >= capacity) {
			E head = peek();
			if (this.comparator().compare(e, head) <= 0){
				return true;
			}
			//当队列满时，就要淘汰顶端队列
			poll();
		}
		return super.offer(e);
	}
	

	public boolean addAll(E[] c) {
		return this.addAll(Arrays.asList(c));
	}
	

	public ArrayList<E> toList() {
		final ArrayList<E> list = new ArrayList<E>(this);
		Collections.sort(list, comparator);
		
		return list;
	}
	
	@Override
	public Iterator<E> iterator() {
		return toList().iterator();
	}
}
