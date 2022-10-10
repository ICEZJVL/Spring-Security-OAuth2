package com.cos.security1.auth;

//시큐리티가 login 주소요청이 오면 낚아채서 로그인을 진행시킨다
//로그인을 진행이 완료가 되면  시큐리티 session을 만들어줍니다.(Security ContextHolder)
//오브젝트 -> Authentication 타입 객체
//Aurthentication 안에 User정보가 있어야됨
//User오브젝트타입 -> ㅇUserDetails타입 객체

import com.cos.security1.model.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

//Security Session -> Authentication -> UserDetails
@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

    private User user;//컴포지션
    private Map<String, Object> attributes;

    public PrincipalDetails(User user) {
        this.user = user;
    }

    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public String getName() {
        return null;
    }

    //해당 User의 권한을 리턴하는 곳!!
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    //계정이 만료됬는지
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //계정이 잠겼는지
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //비밀번호 기간이 지났는지
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //계정이 활성화 되있는지
    @Override
    public boolean isEnabled() {

        //우리 사이트!! 1년동안 로그인을 안하면 휴면계정으로 하기로함
        //현재시간-로긴시간 -> 1년을 초과하면 return false
        return true;
    }
}
