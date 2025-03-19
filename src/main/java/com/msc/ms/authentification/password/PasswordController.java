package com.msc.ms.authentification.password;

import com.msc.ms.authentification.crypto.CryptoService;
import com.msc.ms.authentification.log.LogPassService;
import com.msc.ms.authentification.log.model.LogPassHistoryRequest;
import com.msc.ms.authentification.log.model.LogPasswordResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/password")
@RequiredArgsConstructor
public class PasswordController {
    private final PasswordGenerator passwordGenerator;
    private final CryptoService cryptoService;
    private final LogPassService logPassService;

    @GetMapping
    public ResponseEntity<String> generateNewPassword(@RequestParam("size") int pSize) throws Exception {
        final var password = passwordGenerator.getRandomPassword(25, 25, 25, 25, pSize);
        return ResponseEntity.ok(cryptoService.encrypt(password));
    }

    @PostMapping("/log")
    public ResponseEntity<LogPasswordResponse> generateNewPasswordLog(@RequestBody LogPassHistoryRequest pPasswordProcessDTO) {
        final var response = logPassService.createPasswordLog(pPasswordProcessDTO);
        return ResponseEntity.ok(response);
    }
}
