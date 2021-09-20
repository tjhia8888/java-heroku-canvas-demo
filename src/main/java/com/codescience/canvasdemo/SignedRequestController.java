package com.codescience.canvasdemo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/signedrequest")
public class SignedRequestController {
    @PostMapping
    public String processSignedRequest(@RequestParam("signed_request") String signedRequest, Model model) {
        
        return "index";
    }
}
