package com.example.alarmdrools.service;




import com.clarity.cloud.common.feign.service.SsoService;
import com.clarity.cloud.common.security.UsernamePasswordToken;
import com.example.alarmdrools.inteface.TokenManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;


@Service
@Slf4j
public class TokenManagerServiceImpl implements TokenManagerService {

    @Value("${sso.username}")
    String user;
    @Value("${sso.password}")
    String pass;

    private final SsoService ssoService;
    private String token;
    private final UsernamePasswordToken userPass;


    public TokenManagerServiceImpl(SsoService ssoService) {
        this.ssoService = ssoService;
        userPass = new UsernamePasswordToken();
        token = "";
    }

    @PostConstruct
    public void getInitialToken() {
        userPass.setUserName(user);
        userPass.setPassWord(pass);
    }

    @Override
    public String getToken() {
        while (true) {
            try {
                if (tryGetToken()){

                    return token;
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public boolean tryGetToken() throws InterruptedException {
        try {
            if (tokenIsValid()) return true;
            token = ssoService.tokens(userPass, "");
            return true;
        } catch (Exception ex) {
//            log.error("Acquiring new token failed.");
            Thread.sleep(1000);
        }
        return false;
    }

    public boolean tokenIsValid() {
        try {
            if (Boolean.TRUE.equals(ssoService.authenticate(token)))
                return true;
        } catch (Exception ex) {
//            log.error("Token Expired, acquiring new token.");
        }
        return false;
    }
}
