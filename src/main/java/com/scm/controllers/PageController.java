package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.scm.entities.User;
import com.scm.forms.UserForm;
import com.scm.helpers.Message;
import com.scm.helpers.MessageType;
import com.scm.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @RequestMapping("/home")
    public String home(Model model) {
        System.out.println("Home page handler");
        
        model.addAttribute("name", "ABC");
        model.addAttribute("youtubeChannel", "XYZ");
        model.addAttribute("githubRepo", "https://github.com/gasmi26/");
        return "home";
    }

    // about route

    @RequestMapping("/about")
    public String aboutPage(Model model) {
        model.addAttribute("isLogin", true);
        System.out.println("About page loading");
        return "about";
    }

    // services

    @RequestMapping("/services")
    public String servicesPage() {
        System.out.println("services page loading");
        return "services";
    }

    // contact page

    @GetMapping("/contact")
    public String contact() {
        return new String("contact");
    }

    // this is showing login page
    @GetMapping("/login")
    public String login() {
        return new String("login");
    }

    // registration page
    @GetMapping("/register")
    public String register(Model model) {

        UserForm userForm = new UserForm();
      
        model.addAttribute("userForm", userForm);

        return "register";
    }

   

    @RequestMapping(value = "/do-register", method = RequestMethod.POST)
    public String processRegister(@Valid @ModelAttribute UserForm userForm, BindingResult rBindingResult,
            HttpSession session) {
        System.out.println("Processing registration");
        
        System.out.println(userForm);

      
        if (rBindingResult.hasErrors()) {
            return "register";
        }

       
        User user = new User();
        user.setName(userForm.getName());
        user.setEmail(userForm.getEmail());
        user.setPassword(userForm.getPassword());
        user.setAbout(userForm.getAbout());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setEnabled(false);
        user.setProfilePic(
                "data:image/png;base64,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");

        User savedUser = userService.saveUser(user);

        System.out.println("user saved :");

      

        Message message = Message.builder().content("Registration Successful").type(MessageType.green).build();

        session.setAttribute("message", message);

     
        return "redirect:/register";
    }

}
