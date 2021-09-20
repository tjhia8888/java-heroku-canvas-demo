package com.codescience.canvasdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import canvas.SignedRequest;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/signedrequest")
@Slf4j
public class SignedRequestController {
    @PostMapping
    public String processSignedRequest(@RequestParam("signed_request") String signedRequest, Model model) {
        log.debug("Processing Signed Request");
        log.debug(signedRequest);
        String consumerSecret = System.getenv("SIGNED_REQUEST_CONSUMER_SECRET");
        String signedRequestJson = SignedRequest.verifyAndDecodeAsJson(signedRequest, consumerSecret);
        log.debug("Verified Signed Request");
        log.debug(signedRequestJson);
        model.addAttribute("signedRequest", signedRequestJson);
        return "index";
    }
}
