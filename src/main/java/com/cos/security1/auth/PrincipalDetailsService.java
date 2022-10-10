package com.cos.security1.auth;

import com.cos.security1.Repository.UserRepository;
import com.cos.security1.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

//시큐리티 설정에서 loginProcessingUrl("/login");
//login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC되어있는 loadByUsername함수가 실행
@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    //시큐리티 session(내부 authentication(내부 UserDetaitls))
    //함수 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("username: "+username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("해당username이 없습니다"));
        return new PrincipalDetails(user);
    }
}
