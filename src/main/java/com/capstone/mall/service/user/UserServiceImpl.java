package com.capstone.mall.service.user;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.token.TokenResponse;
import com.capstone.mall.model.user.User;
import com.capstone.mall.model.user.UserRequestDto;
import com.capstone.mall.model.user.UserResponseDto;
import com.capstone.mall.repository.JpaUserRepository;
import com.capstone.mall.security.JwtTokenProvider;
import com.capstone.mall.service.response.ResponseService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService {

    @Value("${secret.encryption.key}")
    private String encryptionKey;

    private final JpaUserRepository userRepository;
    private final ResponseService responseService;
    private final JwtTokenProvider tokenProvider;

    @Override
    public ResponseDto login(String metaId, UserRequestDto userRequestDto) throws Exception {
        if (!validateMetaId(metaId, userRequestDto.getCheckId())) {
            return responseService.createResponseDto(401, "Invalid User Id", null);
        }

        Optional<User> user = userRepository.findByMetaIdNative(metaId);

        // 이미 존재하는 MetaId 인지 확인 후 없으면 생성
        if (user.isEmpty()) {
            user = Optional.of(User.builder()
                    .metaId(metaId)
                    .userId(getNanoId())
                    .role("ROLE_USER")
                    .build());

            userRepository.save(user.get());
        }

        // DB 반영
        userRepository.flush();

        TokenResponse tokenResponse = TokenResponse.builder()
                .accessToken(tokenProvider.createAccessToken(user.get().getUserId(), user.get().getRole()))
                .refreshToken(tokenProvider.createRefreshToken(user.get().getUserId(), user.get().getRole()))
                .userId(user.get().getUserId())
                .build();

        return responseService.createResponseDto(200, "", tokenResponse);
    }

    @Override
    public ResponseDto refresh(HttpServletRequest request) {
        String token = tokenProvider.resolveToken(request);

        if (!tokenProvider.validateToken(token)) {
            return responseService.createResponseDto(401, "Invalid Token", null);
        }

        String userId = tokenProvider.getUserId(token);

        Optional<User> user = userRepository.findByUserId(userId);

        if (user.isEmpty()) {
            return responseService.createResponseDto(401, "user does not exist during token reissuance process", null);
        }

        TokenResponse tokenResponse = TokenResponse.builder()
                .accessToken(tokenProvider.createAccessToken(user.get().getUserId(), user.get().getRole()))
                .refreshToken(tokenProvider.createRefreshToken(user.get().getUserId(), user.get().getRole()))
                .userId(user.get().getUserId())
                .build();

        return responseService.createResponseDto(200, "", tokenResponse);
    }

    @Override
    public ResponseDto update(String userId, UserRequestDto userRequestDto) {
        Optional<User> user = userRepository.findByUserId(userId);

        if (user.isEmpty()) {
            return responseService.createResponseDto(400, "user does not exist", null);
        }

        // Role 유효성 검사
        if (!checkRole(userRequestDto.getRole())) {
            return responseService.createResponseDto(200, "Invalid Role", null);
        }

        user.get().setRole(userRequestDto.getRole());

        return responseService.createResponseDto(200, "", userId);
    }

    @Override
    public ResponseDto readAllUserList() {
        List<User> userList = userRepository.findAll();

        List<UserResponseDto> users = new ArrayList<>();
        for (User user : userList) {
            users.add(UserResponseDto.builder()
                    .userId(user.getUserId())
                    .metaId(user.getMetaId())
                    .role(user.getRole())
                    .build());
        }

        return responseService.createResponseDto(200, "", users);
    }

    private boolean checkRole(String role) {
        return role.equals("ROLE_USER") || role.equals("ROLE_ADMIN") || role.equals("ROLE_SELLER");
    }


    public String decrypt(String clientKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(encryptionKey.getBytes(), "AES");
        String iv = encryptionKey.substring(0, 16);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);

        byte[] decoedBytes = Base64.getDecoder().decode(clientKey.getBytes());
        byte[] decrypted = cipher.doFinal(decoedBytes);
        return new String(decrypted).trim();
    }

    private String getNanoId() {
        return NanoIdUtils.randomNanoId();
    }

    private boolean validateMetaId(String metaId, String checkId) throws Exception {
        try {
            return decrypt(checkId).equals(metaId);
        } catch (Exception e) {
            log.error("Decryption failed");
            return false;
        }
    }
}
