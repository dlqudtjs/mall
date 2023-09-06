package com.capstone.mall.security;

import com.capstone.mall.model.user.User;
import com.capstone.mall.repository.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetails implements UserDetailsService {

    private final JpaUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        /*
         1. UserDetails 를 구현한 User 를 반환하는 방법
         2. UserDetails 를 구현하지 않은 User 를 여기서 UserDetails 로 변환하여 반환하는 방법 중
          두번 째 방법을 사용함.
         */
        return org.springframework.security.core.userdetails.User
                .withUsername(username)
                .password(null)
                .authorities(user.getRole().getValue())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
