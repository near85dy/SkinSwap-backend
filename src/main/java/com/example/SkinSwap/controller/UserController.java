package com.example.SkinSwap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import com.example.SkinSwap.model.User;
import org.springframework.ui.Model;
import com.example.SkinSwap.repository.UserRepository;

@Controller
public class UserController
{
    private UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/profile/{userId}")
    public String getUser(@PathVariable long userId, Model model)
    {
        User user = userRepository.findBySteamid(userId).orElseThrow(() -> new IllegalArgumentException("Invalid user steam ID"));
        if (user == null)
        {
            return "redirect:/error";
        }

        model.addAttribute("id", user.getId());
        model.addAttribute("steamid", user.getSteamid());
        model.addAttribute("username", user.getUsername());
        model.addAttribute("avatar", user.getAvatar());
        return "profile.html";
    }
    @PutMapping("/change-tradelink")
    public String changeTradeline(@RequestParam String tradelink, Authentication authentication)
    {
        return "error";
    }
}
