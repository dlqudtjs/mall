package com.capstone.mall.service.user;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.token.TokenResponse;
import com.capstone.mall.model.user.Role;
import com.capstone.mall.model.user.User;
import com.capstone.mall.repository.JpaUserRepository;
import com.capstone.mall.security.JwtTokenProvider;
import com.capstone.mall.service.response.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final JpaUserRepository userRepository;
    private final ResponseService responseService;
    private final PasswordEncoder BCryptPasswordEncoder;
    private final JwtTokenProvider tokenProvider;

    @Override
    public ResponseDto login(String metaId, String checkId) {

        if (!validateMetaId(metaId, checkId)) {
            return responseService.createResponseDto(401, "Invalid User Id", null);
        }

        // 이미 존재하는 MetaId 인지 확인 후 없으면 생성
        if (userRepository.findById(metaId).isEmpty()) {
            User user = User.builder()
                    .metaId(metaId)
                    .userId(getNanoId())
                    .role(Role.USER)
                    .build();

            userRepository.save(user);
        }

        // MetaId 에 해당하는 User 조회 (userId, role 을 token 에 담기 위해)
        User user = userRepository.findById(metaId).get();

        TokenResponse tokenResponse = TokenResponse.builder()
                .accessToken(tokenProvider.createAccessToken(metaId, user.getRole()))
                .refreshToken(tokenProvider.createRefreshToken(metaId, user.getRole()))
                .build();

        return responseService.createResponseDto(200, "", tokenResponse);
    }

    private String getNanoId() {
        return NanoIdUtils.randomNanoId();
    }

    private boolean validateMetaId(String metaId, String checkId) {
        log.info("encoded metaId: " + BCryptPasswordEncoder.encode(metaId));

        return BCryptPasswordEncoder.matches(metaId, checkId);
    }
}
