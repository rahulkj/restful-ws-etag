package com.rahul.learn.web;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.rahul.learn.service.ETagService;

public abstract class BaseController<T> {

	@Autowired
	protected ETagService eTagService;

	@SuppressWarnings("rawtypes")
	@ExceptionHandler(EntityNotFoundException.class)
	protected ResponseEntity<T> entityNotFound() {
		return new ResponseEntity(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(consumes = "application/json", produces = "application/json", method = RequestMethod.HEAD, value = "/{id}")
	public ResponseEntity head(HttpServletRequest request) {
		String url = ServletUriComponentsBuilder.fromRequest(request).build()
				.toString();
		try {
			String tag = eTagService.get(url);
			HttpHeaders headers = new HttpHeaders();
			headers.add("Etag", tag);
			return new ResponseEntity(null, headers, HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(consumes = "application/json", produces = "application/json", method = RequestMethod.OPTIONS, value = "/")
	public abstract ResponseEntity<T> describe(HttpServletRequest request);
}