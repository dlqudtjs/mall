package com.capstone.mall.service.user;

import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.repository.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final JpaUserRepository userRepository;

    @Override
    public ResponseDto login(String userId) {
        Nano

        return null;
    }
}
