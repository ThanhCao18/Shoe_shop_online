package com.example.ananas.service.Service;

import com.example.ananas.dto.request.AuthenticationRequest;
import com.example.ananas.dto.response.AuthenticationResponse;
import com.example.ananas.entity.Role;
import com.example.ananas.entity.User;
import com.example.ananas.exception.AppException;
import com.example.ananas.exception.ErrException;
import com.example.ananas.repository.User_Repository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.StringJoiner;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class AuthenticationService {
    User_Repository userRepository;
    PasswordEncoder passwordEncoder;
    JwtDecoder jwtDecoder;

    protected static final String KEY_SIGN = "lQgnbki8rjdh62RZ2FNXZB9KWYB1IjajiY04z011BXjjagnc7a";

    String createToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("Ananas")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(100, ChronoUnit.HOURS).toEpochMilli()
                ))
                .claim("scope", buildScopeToRoles(user))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(KEY_SIGN.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("cant create token", e);
            throw new RuntimeException();
        }
    }

    public AuthenticationResponse authenticationResponse(AuthenticationRequest authenticationRequest) {
        var user = userRepository.findByUsernameOrEmail(
                        authenticationRequest.getIdentifier(), // Tìm cả username và email
                        authenticationRequest.getIdentifier())
                .orElseThrow(() -> new AppException(ErrException.USER_NOT_EXISTED));
        if (user.getIsActive().equals(true)){
            boolean checked = passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword());
            if (!checked) {
                throw new AppException(ErrException.USER_NOT_EXISTED);
            }
            var token = createToken(user);
            return AuthenticationResponse.builder()
                    .token(token)
                    .check(true)
                    .userId(user.getUserId())
                    .username(user.getUsername())
                    .build();
        }
        else {
            throw new AppException(ErrException.USER_NOT_EXISTED);
        }

    }

    private Object buildScopeToRoles(User user) {
        StringJoiner scopeJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(scopeJoiner::add);
        }
        return scopeJoiner.toString();
    }
}
