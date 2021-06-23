package com.barshid.schematech.controller;

import com.barshid.schematech.controller.pagebean.NewPassPage;
import com.barshid.schematech.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class UserLogin {

    @GetMapping({"/login"})
    public String defLoginPage() {
        return "customLogin";
    }

    @GetMapping({"/customlogin"})
    public String loginPage() {
        return "customLogin";
    }

    @GetMapping({"/resetpass"})
    public String resetpassword(String signature, String email, Model model) {
        NewPassPage newPassPage = new NewPassPage();
        newPassPage.setHash(signature);
        newPassPage.setEmail(email);
        model.addAttribute("newPassPage",newPassPage);
        return "resetpass";
    }

    @Autowired
    public UserService userService;

    @PostMapping({"/resetpass"})
    public String setNewPassword(@ModelAttribute NewPassPage newPassPage,Model model) {
        userService.setNewPassword(newPassPage);
        model.addAttribute("newPassPage",newPassPage);
        System.out.println(newPassPage);
        return "resetpass";
    }


}
