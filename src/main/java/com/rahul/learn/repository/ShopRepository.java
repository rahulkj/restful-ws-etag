package com.rahul.learn.repository;

import com.rahul.learn.domain.Shop;
import java.util.List;
import org.springframework.roo.addon.layers.repository.mongo.RooMongoRepository;

@RooMongoRepository(domainType = Shop.class)
public interface ShopRepository {

    List<com.rahul.learn.domain.Shop> findAll();
}
