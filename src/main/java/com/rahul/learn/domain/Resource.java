package com.rahul.learn.domain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class Resource<T> {
	private T entity;
	private List<T> entities;

	public Resource() {
		this.entities = new LinkedList<T>();
	}

	public Resource(T entity) {
		this.entity = entity;
		this.entities = new LinkedList<T>();
	}

	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}

	public void addEntity(T entity) {
		this.entities.add(entity);
	}

	public List<T> getEntities() {
		return entities;
	}

	public void setEntities(List<T> entities) {
		this.entities = entities;
	}

	public String marshallObject(T value) {
		String jsonString = null;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			jsonString = objectMapper.writeValueAsString(value);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonString;
	}
	
	public T unmarshallObject(String jsonString, Class<T> class1) {
		T obj = null;
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			obj = objectMapper.readValue(jsonString, class1);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	public static void main(String[] args) {
		Shop shop = new Shop();
		shop.setName("Rahul");
		String marshallString = new Resource<Shop>().marshallObject(shop);
		System.out.println(marshallString);
		
		Collection<Shop> shops = new ArrayList<Shop>();
		shops.add(shop);
		shop = new Shop();
		shop.setName("Jain");
		
		shops.add(shop);
		
		ShopList<Shop> shopList = new ShopList<Shop>();
		shopList.setShops(shops);
		
		String collectionJson = new Resource<ShopList<Shop>>().marshallObject(shopList);
		System.out.println(collectionJson);
		
		Shop unmarshallString = new Resource<Shop>().unmarshallObject(marshallString, Shop.class);
		System.out.println(unmarshallString.getName());
		
		ShopList<Shop> unmarshallCollection = new Resource<ShopList>().unmarshallObject(collectionJson, ShopList.class);
		for (Shop shop2 : shopList.getShops()) {
			System.out.println(shop2.getName());
		}
		
		String parsedString = new Resource<Collection<Shop>>().marshallObject(shops);
		System.out.println(parsedString);
		
		ShopList<Shop> unmarshall = new Resource<ShopList>().unmarshallObject(collectionJson, ShopList.class);
		for (Shop shop2 : unmarshall.getShops()) {
			System.out.println(shop2.getName());
		}
	}
}
