package com.codegym.controller;

import com.codegym.model.User;
import com.codegym.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public ModelAndView getViewRegister(){
        return new ModelAndView("register","user",new User());
    }
    @PostMapping("/register")
    public ModelAndView createUser(@ModelAttribute("user") User user){
        //Ma hoa password

        User userCheck = userService.findByUserName(user.getUsername());
        if(userCheck!=null){
            ModelAndView modelAndView = new ModelAndView("register","message", "User name already exists");
            modelAndView.addObject("user",user);
            return modelAndView;
        }
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setRole("USER");
        user.setEnable(1);
        userService.save(user);
        return null;
    }
}
