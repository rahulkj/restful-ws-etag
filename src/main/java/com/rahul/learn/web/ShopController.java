package com.rahul.learn.web;

import java.math.BigInteger;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rahul.learn.domain.Resource;
import com.rahul.learn.domain.Shop;
import com.rahul.learn.domain.JsonList;
import com.rahul.learn.service.ShopService;

@Controller
@RequestMapping("/shops")
public class ShopController extends BaseController<Resource<Shop>>{
	@Autowired
    ShopService shopService;
	
    @RequestMapping(value = "/{id}", headers = "Accept=application/json", produces="application/json" , method=RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> getShop(@PathVariable("id") BigInteger id) {
        Shop shop = shopService.findShop(id);
        if (shop == null) {
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }
        Resource<Shop> resource = new Resource<Shop>(shop);
        return new ResponseEntity<String>(resource.marshallObject(shop), HttpStatus.OK);
    }
    
    @RequestMapping(headers = "Accept=application/json", produces="application/json", method=RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> listJson() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Type", "application/json; charset=utf-8");
        List<Shop> shops = shopService.findAllShops();
        JsonList shopList = new JsonList();
        shopList.setCollection(shops);
        Resource<JsonList> resource = new Resource<JsonList>();
        String jsonReponse = resource.marshallObject(shopList);
        return new ResponseEntity<String>(jsonReponse, HttpStatus.OK);
    }
    
    @RequestMapping(method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<String> createFromJson(@RequestBody String json) {
        Shop shop = new Shop();
        shopService.saveShop(shop);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
    
    
    @RequestMapping(method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity<String> updateFromJson(@RequestBody String json) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        Shop shop = new Shop();
        if (shopService.updateShop(shop) == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public ResponseEntity<String> deleteFromJson(@PathVariable("id") BigInteger id) {
        Shop shop = shopService.findShop(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        if (shop == null) {
            return new ResponseEntity<String>(headers, HttpStatus.NOT_FOUND);
        }
        shopService.deleteShop(shop);
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

	@Override
	public ResponseEntity<Resource<Shop>> describe(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}
}
