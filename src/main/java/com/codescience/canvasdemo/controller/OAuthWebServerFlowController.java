package com.codescience.canvasdemo.controller;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Map;

import org.springframework.boot.json.GsonJsonParser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Controller
@RequestMapping("/oauth/wsf")
@Slf4j
public class OAuthWebServerFlowController {
	private static final String authUrl = "{0}/services/oauth2/authorize?response_type=code&client_id={1}&redirect_uri={2}&scope=api id web";
	private static final String clientId = System.getenv("OAUTH_WSF_CONSUMER_KEY");
	private static final String clientSecret = System.getenv("OAUTH_WSF_CONSUMER_SECRET");
	private static final String callbackUrl = System.getenv("OAUTH_WSF_CALLBACK_URL");
	private static String tokenUrl;

	@GetMapping
	public ModelAndView initWebServerFlow(@RequestParam String loginUrl) {
		log.debug("Processing GET Oauth");
        log.debug(loginUrl);
        tokenUrl = MessageFormat.format("{0}/services/oauth2/token", new Object[] {loginUrl});
		return new ModelAndView("redirect:" + MessageFormat.format(authUrl, new Object[] { loginUrl, clientId, callbackUrl }));
	}

	@GetMapping("/callback")
	public String processCallback(@RequestParam String code, Model model) throws Exception {
		log.debug("Processing POST Oauth (callback)");
		log.debug(code);
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		
		MultipartBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
				.addFormDataPart("code", code).addFormDataPart("grant_type", "authorization_code")
				.addFormDataPart("client_id", clientId).addFormDataPart("client_secret", clientSecret)
				.addFormDataPart("redirect_uri", callbackUrl).addFormDataPart("format", "json")
				.build();
		
		Request request = new Request.Builder().url(tokenUrl).method("POST", requestBody).build();
		
		try (Response response = client.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				log.error("error respose received " + response);
				throw new IOException("Unexpected code " + response);
			}
			
			Map<String, Object> oauth = new GsonJsonParser().parseMap(response.body().string());
			log.debug("parsed resonse");
			log.debug(oauth.toString());
			model.addAttribute("oauth", oauth);
		}

		return "oauth/wsf";
	}
}
