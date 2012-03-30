package com.rahul.learn.aspects;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.rahul.learn.domain.Resource;
import com.rahul.learn.service.ETagService;
import com.rahul.learn.util.AppConfig;

@Component
@Aspect
public class ResponseEnhancerAspect {
 
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object handlePOST(ProceedingJoinPoint pjp) throws Throwable{
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		Object retVal = pjp.proceed();
		ResponseEntity entity = (ResponseEntity)retVal;
		HttpHeaders headers = new HttpHeaders();
		Resource resource = (Resource) entity.getBody();
		Object id = ReflectionUtils.findMethod(resource.getEntity().getClass(), "getId").invoke(resource.getEntity());
		headers.add("Location", ServletUriComponentsBuilder.fromRequest(request).path("{id}").build().expand(id).toString());
		return new ResponseEntity(null,headers,entity.getStatusCode());
	}
 
 
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object handleGET(ProceedingJoinPoint pjp) throws Throwable {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		Object retVal = pjp.proceed();
		ResponseEntity entity = (ResponseEntity)retVal;
		HttpHeaders headers = new HttpHeaders();
		String url = ServletUriComponentsBuilder.fromRequest(request).build().toString();
		String tag = null;
		try{
			tag = getETagService().get(url);
		}catch (Exception e) {
			tag = getETagService().generate(url, entity.getBody());
		}
		headers.add("Etag", tag);
		return new ResponseEntity(entity.getBody(),headers,entity.getStatusCode());
	}
 
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Object handlePUT(ProceedingJoinPoint pjp) throws Throwable {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		ResponseEntity responseEntity = (ResponseEntity) pjp.proceed();
		String url = ServletUriComponentsBuilder.fromRequest(request).build().toString();
		HttpHeaders headers = new HttpHeaders();
		Resource resource = (Resource)responseEntity.getBody();
		String newTag = getETagService().update(url, resource.getEntity());
		headers.add("Etag", newTag);
		return new ResponseEntity(responseEntity.getBody(),headers,responseEntity.getStatusCode());
	}
 
	private boolean checkEtag() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String url = ServletUriComponentsBuilder.fromRequest(request).build().toString();
		String storedTag = null;
		try {
			storedTag = getETagService().get(url);
			String providedTag = request.getHeader("If-Match");
			if(providedTag == null || !storedTag.equals(providedTag)){
				return false;
			}else{
				return true;
			}
		} catch (Exception e) {
			return false;
		}
	}
 
 
	private Object handleDELETE(ProceedingJoinPoint pjp) throws Throwable{
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		String url = ServletUriComponentsBuilder.fromRequest(request).build().toString();
		pjp.proceed();
		getETagService().remove(url);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}
 
	@Around("execution(public org.springframework.http.ResponseEntity com.rahul.learn.web.*.*(..)) && @annotation(requestMapping)")
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object handleMethod(ProceedingJoinPoint pjp, RequestMapping requestMapping) throws Throwable{
		Object retVal = null;
		switch (requestMapping.method()[0]) {
		case GET :
			retVal = handleGET(pjp);
		break;
 
		case POST:
			retVal = handlePOST(pjp);
		break;	
 
		case PUT:
			if(checkEtag()){
				retVal = handlePUT(pjp);
			}else{
				retVal = new ResponseEntity(HttpStatus.PRECONDITION_FAILED);
			}
		break;
 
		case DELETE:
			if(checkEtag()){
				retVal =  handleDELETE(pjp);
			}else{
				retVal = new ResponseEntity(HttpStatus.PRECONDITION_FAILED);
			}
		break;
 
 
		}
		return retVal;
	}


	public ETagService getETagService() {
		ETagService eTagService = AppConfig.getAppCtx().getBean(ETagService.class);
		return eTagService;
	}
	
	
}