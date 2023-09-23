package com.capstone.mall.security;

import com.capstone.mall.model.token.Token;
import com.capstone.mall.repository.JpaTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.Date;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.token.access.validity}")
    private long accessTokenValidityInMilliseconds;

    @Value("${jwt.token.refresh.validity}")
    private long refreshTokenValidityInMilliseconds;

    private final MyUserDetails myUserDetails;
    private final JpaTokenRepository tokenRepository;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // 헤더에서 JWT 를 추출함
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

    // accessToken 생성
    public String createAccessToken(String userId, String role) {
        Claims claims = Jwts.claims().setSubject(userId);
        claims.put("roles", role);

        Date now = new Date();
        Date validity = new Date(now.getTime() + accessTokenValidityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims) // 데이터
                .setIssuedAt(now) // 토큰 발행일자
                .setExpiration(validity) // 토큰 만료일자
                .signWith(HS256, secretKey) // 암호화 알고리즘, secret 값 세팅
                .compact();
    }

    // refreshToken 생성
    @Transactional
    public String createRefreshToken(String userId, String role) {
        Claims claims = Jwts.claims().setSubject(userId);
        claims.put("roles", role);

        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshTokenValidityInMilliseconds);

        String token = Jwts.builder()
                .setClaims(claims) // 데이터
                .setIssuedAt(now) // 토큰 발행일자
                .setExpiration(validity) // 토큰 만료일자
                .signWith(HS256, secretKey) // 암호화 알고리즘, secret 값 세팅
                .compact();

        // DB 에 refreshToken 저장 (존재하면 update, 없으면 insert)
        Token refreshToken = tokenRepository.findByUserId(userId).orElse(null);
        if (refreshToken == null) {
            Token saveToken = Token.builder()
                    .refreshToken(token)
                    .userId(userId)
                    .build();

            tokenRepository.save(saveToken);
        } else {
            refreshToken.setRefreshToken(token);
        }

        return token;
    }


    // 인증 객체 생성
    public Authentication getAuthentication(String token) {
        UserDetails userdetails = myUserDetails.loadUserByUsername(this.getUserId(token));
        return new UsernamePasswordAuthenticationToken(userdetails, "", userdetails.getAuthorities());
    }

    public String getUserId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUserIdByBearerToken(String bearerToken) {
        String token = bearerToken.substring(7);
        return getUserId(token);
    }
}
