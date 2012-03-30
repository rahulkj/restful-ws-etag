package com.rahul.learn.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

public interface ETagService {
	@Cacheable(value = "tags", key = "#url")
	public String generate(String url, Object entity);
	@Cacheable(value = "tags", key = "#url")
	public String get(String url) throws Exception;
	@CacheEvict(value = "tags", key = "#url")
	public void remove(String url);
	@CacheEvict(value = "tags", key = "#url")
	public String update(String url, Object entity);
}