package com.cos.security1.controller;

import com.cos.security1.Repository.UserRepository;
import com.cos.security1.auth.PrincipalDetails;
import com.cos.security1.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String index(){
        //머스테치 기본폴더 src/main/resource
        return "index";
    }

    @GetMapping("/test/login")
    public @ResponseBody String testLogin(Authentication authentication,
                                     @AuthenticationPrincipal PrincipalDetails userDetails    ){ //DI(의존성 주입)
        System.out.println("/test/login=================");
        PrincipalDetails principalDetails=(PrincipalDetails) authentication.getPrincipal();
        System.out.println(userDetails.getUser());
        System.out.println("authentication : "+principalDetails.getUser());
        return "세션정보 확인하기";
    }

    @GetMapping("/test/oauth/login")
    public @ResponseBody String testOAuthLogin(Authentication authentication,
                                               @AuthenticationPrincipal OAuth2User oauth){ //DI(의존성 주입)
        System.out.println("/test/login=================");
        OAuth2User oAuth2User= (OAuth2User) authentication.getPrincipal();
        System.out.println(oauth.getAttributes());
        System.out.println("authentication : "+oAuth2User.getAttributes());
        return "Oauth 세션정보 확인하기";
    }
    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails){
        System.out.println(principalDetails.getUser());
        return "user";
    }

    @GetMapping("/admin")
    public @ResponseBody String admin(){
        return "admin";
    }

    @GetMapping("/manager")
    public @ResponseBody String manager(){
        return "manager";
    }

    //springsecutiry에서 낚아챔 -> securityconfig생성 이후 안낚아챔
    @GetMapping("/loginForm")
    public String loginForm(){
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm(){
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user){
        System.out.println(user);
        user.setRole("ROLE_USER");
        String encodedPassword=passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);// 이렇게 되면 로그인을 할 수 없음 왜나하면 패스워드가 암호화가 안되있기 때문에
        return "redirect:/loginForm";
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    public @ResponseBody String info(){
        return "개인정보";
    }

    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/data")
    public @ResponseBody String data(){
        return "data정보";
    }
}
