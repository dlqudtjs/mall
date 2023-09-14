package com.capstone.mall.security;

import com.capstone.mall.model.user.User;
import com.capstone.mall.repository.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyUserDetails implements UserDetailsService {

    private final JpaUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> findUser = userRepository.findByUserId(username);

        if (findUser.isPresent()) {
            User user = User.builder()
                    .userId(findUser.get().getUserId())
                    .metaId(findUser.get().getMetaId())
                    .role(findUser.get().getRole())
                    .build();

            return user;
        }

        return null;
    }
}
