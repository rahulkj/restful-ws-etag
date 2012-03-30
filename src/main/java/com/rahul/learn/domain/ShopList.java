package com.rahul.learn.domain;

import java.util.Collection;

public class ShopList<Shop> {

	Collection<Shop> shops;

	public Collection<Shop> getShops() {
		return shops;
	}

	public void setShops(Collection<Shop> shops) {
		this.shops = shops;
	}

}
