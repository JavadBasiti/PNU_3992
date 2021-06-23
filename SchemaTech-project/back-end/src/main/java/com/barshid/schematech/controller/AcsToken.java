package com.barshid.schematech.controller;

import com.barshid.schematech.controller.dto.LoginParams;
import com.barshid.schematech.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
//@RequestMapping({"acstoken"})
@Slf4j
public class AcsToken {

    TokenEndpoint tokenEndpoint;
    @Autowired
    public void setTokenEndpoint(TokenEndpoint tokenEndpoint) {
        this.tokenEndpoint = tokenEndpoint;
    }
    LoginService loginService;
    @Autowired
    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }

    @ResponseBody
    @RequestMapping(value = "/oauth/token", method=RequestMethod.POST)
    public ResponseEntity<OAuth2AccessToken> getAccessOAuth2Token(Principal principal, @RequestBody
                                            LoginParams loginParams) throws HttpRequestMethodNotSupportedException {
//    public ResponseEntity<OAuth2AccessToken> getAccessOAuth2Token(Principal principal, @RequestParam
//            Map<String, String> parameters) throws HttpRequestMethodNotSupportedException {

        log.debug("Start Login...");
        Map<String, String> parameters = loginService.tokenParameter(loginParams);
        log.debug("grant_type:"+parameters.get("grant_type"));
        return tokenEndpoint.postAccessToken(principal, parameters);
    }

//    @Value("STRIPE_PUBLIC_KEY")
//    private String stripePublishableKey;
//    @ResponseBody
//    @GetMapping({"/stripe-key"})
//    public ResponseEntity<String> getStripekey(){
//        log.debug("Start get public stripe key:"+stripePublishableKey);
//        return ResponseEntity.ok().body("{  \"publishableKey\": \""+stripePublishableKey+"\"  }") ;
//    }

//    @Autowired
//    apl oAuth2AutoConfiguration;
}
