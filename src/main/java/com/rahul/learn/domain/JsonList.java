package com.rahul.learn.domain;

import java.util.Collection;

public class JsonList<T> {
	private Collection<T> collection;

	public Collection<T> getCollection() {
		return collection;
	}

	public void setCollection(Collection<T> collection) {
		this.collection = collection;
	}
	
}