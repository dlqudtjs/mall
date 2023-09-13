package com.capstone.mall.service.user;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.capstone.mall.model.ResponseDto;
import com.capstone.mall.model.token.TokenResponse;
import com.capstone.mall.model.user.Role;
import com.capstone.mall.model.user.User;
import com.capstone.mall.model.user.UserRequestDto;
import com.capstone.mall.repository.JpaUserRepository;
import com.capstone.mall.security.JwtTokenProvider;
import com.capstone.mall.service.response.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    @Value("${secret.encryption.key}")
    private String encryptionKey;

    private final JpaUserRepository userRepository;
    private final ResponseService responseService;
    private final PasswordEncoder BCryptPasswordEncoder;
    private final JwtTokenProvider tokenProvider;

    @Override
    public ResponseDto login(String metaId, UserRequestDto userRequestDto) throws Exception {
        if (!validateMetaId(metaId, userRequestDto.getCheckId())) {
            return responseService.createResponseDto(401, "Invalid User Id", null);
        }

        // 이미 존재하는 MetaId 인지 확인 후 없으면 생성
        if (userRepository.findByMetaId(metaId).isEmpty()) {
            User user = User.builder()
                    .metaId(metaId)
                    .userId(getNanoId())
                    .role(Role.USER)
                    .build();

            userRepository.save(user);
        }

        // MetaId 에 해당하는 User 조회 (userId, role 을 token 에 담기 위해)
        User user = userRepository.findByMetaId(metaId).get();

        TokenResponse tokenResponse = TokenResponse.builder()
                .accessToken(tokenProvider.createAccessToken(metaId, user.getRole()))
                .refreshToken(tokenProvider.createRefreshToken(metaId, user.getRole()))
                .build();

        return responseService.createResponseDto(200, "", tokenResponse);
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
