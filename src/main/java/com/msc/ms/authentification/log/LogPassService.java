package com.msc.ms.authentification.log;

import com.msc.ms.authentification.crypto.CryptoService;
import com.msc.ms.authentification.log.model.LogPassEntity;
import com.msc.ms.authentification.message.model.PasswordProccessDTO;
import com.msc.ms.authentification.user.model.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class LogPassService {

    private final ILogPassRepository iLogPassRepository;
    private final PasswordEncoder passwordEncoder;
    private final CryptoService cryptoService;

    public LogPassEntity findAllByUser(UserEntity userEntity) {
        return this.iLogPassRepository.finaAllByUser(userEntity);
    }


    void createPasswordLog(PasswordProccessDTO passwordProccessDTO) {
        try {
            final var decryptedPassword = cryptoService.decrypt(passwordProccessDTO.getPassword());
            final var encriptedPassword = passwordEncoder.encode(decryptedPassword);

            final var mLogPassEntity = LogPassEntity.builder();
        } catch (Exception e) {
            log.error("error in password log creation for userid: {} ", passwordProccessDTO.getIdUSer());
            throw new RuntimeException(e);
        }
    }
}
