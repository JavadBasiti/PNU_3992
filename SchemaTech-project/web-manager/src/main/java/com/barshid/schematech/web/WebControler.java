package com.barshid.schematech.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class WebControler {

//    @GetMapping({"/success"})
//    public String toIndexPage(){
////        return "redirect:index.jsf";
//        return "index.jsf";
//
//    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String redirect() {
        return "redirect:login.jsf";
    }

}
