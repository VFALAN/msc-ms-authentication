package com.msc.ms.authentification.authentication;

import com.msc.ms.authentification.authentication.error.*;
import com.msc.ms.authentification.authentication.model.request.LoginRequestDTO;
import com.msc.ms.authentification.authentication.model.response.LoginResponseDTO;
import com.msc.ms.authentification.jwt.JwtService;
import com.msc.ms.authentification.log.LogPassService;
import com.msc.ms.authentification.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.HashMap;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final LogPassService logPassService;
    private final AuthenticationManager authenticationManager;
    private final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private final PasswordEncoder passwordEncoder;


    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) throws MscAuthenticationException {
        if (validUser(loginRequestDTO.getUsername()) && validPassword(loginRequestDTO.getUsername(), loginRequestDTO.getPassword())) {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword()));
            UserDetails user = userService.buildUserDetails(loginRequestDTO.getUsername());
            final var mUserEntity = userService.findUser(user.getUsername());
            final var hash = new HashMap<String, String>();
            hash.put("DashBoard", "General");
            hash.put("profile", mUserEntity.getProfile().getName());
            final var accessToken = jwtService.getToken(hash, user);
            return LoginResponseDTO.builder().accessToken(accessToken).build();
        } else return null;


    }

    private boolean validUser(String username) throws MscAuthenticationException {
        final var mUserEntity = this.userService.findUser(username);
        if (!mUserEntity.getActive()) {
            throw new DisabledUserException();
        }
        return true;
    }

    private boolean validPassword(String username, String password) throws MscAuthenticationException {
        final var mUserEntity = this.userService.findUser(username);
        final var logPassHistory = this.logPassService.findAllByUser(mUserEntity);
        if (logPassHistory != null) {
            if (logPassHistory.getExpired()) {
                throw new ExpiredPasswordException(simpleDateFormat.format(logPassHistory.getExpiredDate()));
            } else if (!passwordEncoder.matches(password, logPassHistory.getPassword())) {
                throw new WrongPasswordException();
            }

            return true;
        } else {
            throw new NoPasswordHistoryException(username);
        }

    }

}
