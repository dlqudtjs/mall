package com.capstone.mall.service.user;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.token.TokenResponse;
import com.capstone.mall.model.user.User;
import com.capstone.mall.model.user.UserRequestDto;
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
import java.util.Base64;

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

        User user = userRepository.findByMetaIdNative(metaId).orElse(null);

        // 이미 존재하는 MetaId 인지 확인 후 없으면 생성
        if (user == null) {
            user = User.builder()
                    .metaId(metaId)
                    .userId(getNanoId())
                    .role("ROLE_USER")
                    .build();

            userRepository.save(user);
        }

        // DB 반영
        userRepository.flush();

        TokenResponse tokenResponse = TokenResponse.builder()
                .accessToken(tokenProvider.createAccessToken(user.getUserId(), user.getRole()))
                .refreshToken(tokenProvider.createRefreshToken(user.getUserId(), user.getRole()))
                .userId(user.getUserId())
                .build();

        return responseService.createResponseDto(200, "", tokenResponse);
    }

    @Override
    public ResponseDto refresh(String refreshToken, HttpServletRequest request) {
        String token = tokenProvider.resolveToken(request);

        if (!tokenProvider.validateToken(token)) {
            return responseService.createResponseDto(401, "Invalid Token", null);
        }

        String userId = tokenProvider.getUserId(token);

        User user = userRepository.findByUserId(userId).orElse(null);

        if (user == null) {
            return responseService.createResponseDto(401, "ID does not exist during token reissuance process", null);
        }

        TokenResponse tokenResponse = TokenResponse.builder()
                .accessToken(tokenProvider.createAccessToken(user.getUserId(), user.getRole()))
                .refreshToken(tokenProvider.createRefreshToken(user.getUserId(), user.getRole()))
                .userId(user.getUserId())
                .build();

        return responseService.createResponseDto(200, "", tokenResponse);
    }

    @Override
    public ResponseDto update(String userId, UserRequestDto userRequestDto) {
        User user = userRepository.findByUserId(userId).orElse(null);

        if (user == null) {
            return responseService.createResponseDto(400, "ID does not exist", null);
        }

        if (!checkRole(userRequestDto.getRole())) {
            return responseService.createResponseDto(200, "Invalid Role", null);
        }

        user.setRole(userRequestDto.getRole());

        return responseService.createResponseDto(200, "", user.getUserId());
    }

    private boolean checkRole(String role) {
        if (role.equals("ROLE_USER") || role.equals("ROLE_ADMIN") || role.equals("ROLE_SELLER")) {
            return true;
        }

        return false;
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
        return decrypt(checkId).equals(metaId);
    }
}
