package com.example.SkinSwap.controller;

import com.example.SkinSwap.model.User;
import com.example.SkinSwap.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import jakarta.servlet.http.HttpServletRequest;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.example.SkinSwap.repository.UserRepository;
import com.example.SkinSwap.Service.SteamApiService;
import org.springframework.web.client.RestTemplate;

@Controller
public class SteamOpenIDController
{
    private static final String STEAM_OPENID_URL = "https://steamcommunity.com/openid/login";
    private static final String OPENID_MODE = "checkid_setup";
    private static final String OPENID_NS = "http://specs.openid.net/auth/2.0";
    private static final String OPENID_IDENTIFIER = "http://specs.openid.net/auth/2.0/identifier_select";

    private final UserRepository userRepository;
    private final SteamApiService steamApiService;

    @Autowired
    public SteamOpenIDController(UserRepository userRepository, SteamApiService steamApiService)
    {
        this.userRepository = userRepository;
        this.steamApiService = steamApiService;
    }

    @GetMapping("/login")
    public RedirectView login(HttpServletRequest request)
    {
        String returnUrl = request.getRequestURL().toString().replace("/login", "/verify");
        String redirectUrl = STEAM_OPENID_URL +
                "?openid.ns=" + URLEncoder.encode(OPENID_NS, StandardCharsets.UTF_8) +
                "&openid.mode=" + URLEncoder.encode(OPENID_MODE, StandardCharsets.UTF_8) +
                "&openid.return_to=" + URLEncoder.encode(returnUrl, StandardCharsets.UTF_8) +
                "&openid.claimed_id=" + URLEncoder.encode(OPENID_IDENTIFIER, StandardCharsets.UTF_8) +
                "&openid.identity=" + URLEncoder.encode(OPENID_IDENTIFIER, StandardCharsets.UTF_8);

        return new RedirectView(redirectUrl);
    }

    @GetMapping("/verify")
    public String verify(@RequestParam Map<String, String> params)
    {
        long steamid = Long.parseLong(extractSteamId(params.get("openid.claimed_id")));
        if (steamid != 0)
        {
            Map<String, Object> player = steamApiService.getPlayerSummary(String.valueOf(steamid));
            if (player == null) return null;
            String username = (String) player.get("personaname");
            String avatar = (String) player.get("avatarmedium");
            String profileUrl = (String) player.get("profileurl");
            String countryCode = (String) player.get("loccountrycode");

            User user = userRepository.findBySteamid(steamid).orElseGet(() ->
            {
                User newUser = new User(steamid, username, avatar);
                userRepository.save(newUser);
                return newUser;
            });

            user.setUsername(username);
            user.setAvatar(avatar);
            userRepository.save(user);

            if (user != null)
            {
                return "redirect:/profile/" + user.getSteamid();
            }
        }

        return "redirect:/home";
    }

    private String extractSteamId(String claimedId) {
        if (StringUtils.isNotBlank(claimedId)) {
            String[] parts = claimedId.split("/");
            return parts[parts.length - 1];
        }
        return null;
    }
}