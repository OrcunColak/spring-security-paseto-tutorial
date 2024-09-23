package com.colak.springtutorial.service;

import com.colak.springtutorial.token.AppToken;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class TokenServiceTest {

    @Autowired
    private TokenService tokenService;

    @Test
    void testGoodToken() {
        final String userId = "1234";
        final String role = "USER";
        final Instant expiresDate = Instant.now().plus(5, ChronoUnit.MINUTES);

        AppToken appToken = new AppToken();
        appToken.setUserId(userId);
        appToken.setRole(role);
        appToken.setExpiresDate(expiresDate);

        Optional<String> optToken = tokenService.encrypt(appToken);
        Assertions.assertTrue(optToken.isPresent());
        String token = optToken.get();
        Assertions.assertNotNull(token);
        log.info(token);

        Optional<AppToken> optAppToken = tokenService.decrypt(token);
        Assertions.assertTrue(optAppToken.isPresent());
        AppToken decodedAppToken = optAppToken.get();

        Assertions.assertNotNull(decodedAppToken);
        Assertions.assertEquals(userId, decodedAppToken.getUserId());
        Assertions.assertEquals(role, decodedAppToken.getRole());
        Assertions.assertEquals(expiresDate, decodedAppToken.getExpiresDate());
    }

    @Test
    void testBadToken() {
        String fakeToken = "v3.local.mu4W-Il_eEMmGFt5Pe5uJrB3Vq3o4XjrdMeUp0grHqf48GgjN_KevFtHwJCEdbTUdiWhL_lQ-B1Qjsl2arf9TRdqw35bwGJgiPn9OAXezvFRhifmRZOTlZB9H_1u-luEzu5Y4SZCcmWtYDKgCt8jUv5KePUBkfWoKtsMmYgoXlSjqIv0bgxEUHG0kYkDUjXwpIc.UE9DLVBBU0VUTw";
        Optional<AppToken> optAppToken = tokenService.decrypt(fakeToken);
        Assertions.assertTrue(optAppToken.isEmpty());
    }
}