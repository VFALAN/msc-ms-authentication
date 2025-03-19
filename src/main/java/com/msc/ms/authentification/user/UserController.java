package com.msc.ms.authentification.user;

import com.msc.ms.authentification.user.model.UserRequest;
import com.msc.ms.authentification.user.model.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;



    public ResponseEntity<UserResponse> registryUserWithOutProfile(@Valid @RequestBody UserRequest pUserRequest){
        final var result = userService;
        return null;
    }


}
