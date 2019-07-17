package com.lideragro.api.resource;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lideragro.api.config.properties.LiderAgroProperties;

@RestController
@RequestMapping("/token")
public class TokenResource {
	
	@Autowired
	private LiderAgroProperties lideragroProperties;
	
	@DeleteMapping("/revoke")
	public void revoke(HttpServletRequest req, HttpServletResponse resp) {
		Cookie cookie = new Cookie("refreshToken", null);
		cookie.setHttpOnly(true);
		cookie.setSecure(lideragroProperties.getSeguranca().isEnableHttps()); 
		
	}

}
