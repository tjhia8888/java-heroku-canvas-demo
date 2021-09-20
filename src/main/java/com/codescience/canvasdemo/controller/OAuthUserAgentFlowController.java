package com.codescience.canvasdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/oauth/uaf")
public class OAuthUserAgentFlowController {

	@GetMapping
	public String getOauthRequest(Model model) {
		model.addAttribute("consumerKey", System.getenv("OAUTH_UAF_CONSUMER_KEY"));
		return "oauth/uaf";
	}
}
