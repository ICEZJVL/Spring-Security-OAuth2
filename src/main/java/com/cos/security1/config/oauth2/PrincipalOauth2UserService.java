package com.cos.security1.config.oauth2;

import com.cos.security1.repository.UserRepository;
import com.cos.security1.auth.PrincipalDetails;
import com.cos.security1.config.oauth2.provider.GoolgeUserInfo;
import com.cos.security1.config.oauth2.provider.OAuth2UserInfo;
import com.cos.security1.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    //구글로 부터 받은 userRequest 데이터에 대한 후처리되는 함수

    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("userRequest: "+userRequest.getClientRegistration());//registrationId로 어떤 Oauth로 로그인했는지 판별 가능
        System.out.println("userRequest: "+userRequest.getAccessToken());

        OAuth2User oAuth2User=super.loadUser(userRequest);
        //code를 리턴 받은 것을 Oauth-client라이브러리에서 받아서 accesss토큰을 요청
        //userRequest정보(access_token)->loadUser함수 호출->구글로 부터 회원프로필 받아준다.
        System.out.println("userRequest: "+oAuth2User.getAttributes());
        OAuth2UserInfo oAuth2UserInfo=null;
        if(userRequest.getClientRegistration().getRegistrationId().equals("google")){
            oAuth2UserInfo = new GoolgeUserInfo(oAuth2User.getAttributes());
        }
        String provider= oAuth2UserInfo.getProvider();//google
        String probiderId = oAuth2UserInfo.getProviderId();
        String username=provider+"_"+probiderId;
        String password = bCryptPasswordEncoder.encode("겟인데어");
        String role="ROLE_USER";
        String email = oAuth2UserInfo.getEmail();

        User userEntity = userRepository.findByUsername(username)
                .orElse(null);

        if (userEntity == null) {
            System.out.println("로그인이 최초입니다");
            userEntity=User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(probiderId)
                    .build();
        }
        return new PrincipalDetails(userEntity,oAuth2User.getAttributes());
    }
}
