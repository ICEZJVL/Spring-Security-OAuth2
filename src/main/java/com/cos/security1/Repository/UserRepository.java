package com.cos.security1.Repository;

import com.cos.security1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//@Repository 어노테이션이 없어도 jparepository를 상속받기 떄문에 빈이 자동 주입됨
public interface UserRepository extends JpaRepository<User, Integer> {
    public Optional<User> findByUsername(String username);
}
