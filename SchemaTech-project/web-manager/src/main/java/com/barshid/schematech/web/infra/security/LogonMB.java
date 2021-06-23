package com.barshid.schematech.web.infra.security;

import com.barshid.schematech.web.Token;
import com.barshid.schematech.web.dto.LoginParams;
import com.github.adminfaces.template.session.AdminSession;
import org.omnifaces.util.Faces;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Specializes;
import javax.faces.application.FacesMessage;
import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;

import static com.barshid.schematech.web.util.Utils.addDetailMessage;

/**
 *
 * This is just a login .
 *
 * AdminSession uses isLoggedIn to determine if user must be redirect to login page or not.
 * By default AdminSession isLoggedIn always resolves to true so it will not try to redirect user.
 *
 * If you already have your authorization mechanism which controls when user must be redirect to initial page or logon
 * you can skip this class.
 */
@Named
@SessionScoped
@Specializes
@Primary
@Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class LogonMB extends AdminSession implements Serializable {

    private String currentUser;
    private String email;
    private String password;
    private boolean remember;

    @Autowired
    private WebClient webClient;

    @Autowired
    private Token token;

    public void login() throws IOException {
        try {
            LoginParams loginParams = new LoginParams();
            loginParams.setEmail(email);
            loginParams.setPassword(password);

//         WebClient.ResponseSpec responseSpec
//        Optional<Token> optionalToken
            Mono<Token> tokenMono = webClient.post()
                .uri("/oauth/token")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header("Authorization","Basic YW5kcm9pZGNsaWVudDptb2p0YWJhNjA2MA==")
                .body(Mono.just(loginParams), LoginParams.class)
//                    .exchangeToMono(Token.class)
                .retrieve()
    //        .onStatus(HttpStatus.FORBIDDEN, (ClientResponse clientResponse) -> {
    //            addDetailMessage("login Error!");
    //            return null;
    //        })
    //                .onStatus( httpStatus -> {addDetailMessage("login Error!");return null;}/*HttpStatus.NOT_FOUND.equals(httpStatus)*/,
    //                        clientResponse -> Mono.empty())

                .bodyToMono( Token.class);
//        token = tokenMono.doOnSuccess( HttpStatus.FORBIDDEN, (ClientResponse clientResponse) -> {
//        }).block();
//        tokenMono.subscribe().  {
//        },
            // blockOptional();
            Token block = tokenMono.block();
            if(block==null){
                addDetailMessage("Login Error!  <b> check user & password"  + "</b>");
                return;
            }
            BeanUtils.copyProperties(block,token);
            currentUser = email;
            addDetailMessage("Logged in successfully as <b>" + email + "</b>");
            Faces.getExternalContext().getFlash().setKeepMessages(true);
            Faces.redirect("index.jsf");
        } catch (Exception e) {

            addDetailMessage("Login Error!  check user & password", FacesMessage.SEVERITY_ERROR);
            e.printStackTrace();
//            Faces.getExternalContext().getFlash().setKeepMessages(true);
//            Faces.refresh();//redirect("login.jsf");
        }

    }

    @Override
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRemember() {
        return remember;
    }

    public void setRemember(boolean remember) {
        this.remember = remember;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }
}
