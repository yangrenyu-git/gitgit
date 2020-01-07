package com.javaxxz.core.toolbox;

import java.io.Serializable;
import java.util.Arrays;


@SuppressWarnings("serial")
public class Tuple implements Cloneable, Serializable {

	private Object[] values;
	private int length;

	public Tuple(Object... values) {
		this.values = values;
		this.length = values.length;
	}

	public int length() {
		return length;
	}

	@SuppressWarnings("unchecked")
	public <T> T get(int index) {
		return (T) values[index];
	}

	@Override
	public String toString() {
		return Arrays.toString(values);
	}

	@Override
	public Tuple clone() {
		try {
			return (Tuple) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
}
