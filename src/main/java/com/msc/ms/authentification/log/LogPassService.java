package com.msc.ms.authentification.log;

import com.msc.ms.authentification.crypto.CryptoService;
import com.msc.ms.authentification.log.model.LogPassEntity;
import com.msc.ms.authentification.log.model.LogPassHistoryRequest;
import com.msc.ms.authentification.log.model.LogPasswordResponse;
import com.msc.ms.authentification.user.UserService;
import com.msc.ms.authentification.user.model.UserEntity;
import com.msc.ms.authentification.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class LogPassService {

    private final ILogPassRepository iLogPassRepository;
    private final PasswordEncoder passwordEncoder;
    private final CryptoService cryptoService;
    private final UserService userService;

    public LogPassEntity findAllByUser(UserEntity userEntity) {
        return this.iLogPassRepository.finaAllByUser(userEntity);
    }


    public LogPasswordResponse createPasswordLog(LogPassHistoryRequest passwordProccessDTO) {
        try {
            final var decryptedPassword = cryptoService.decrypt(passwordProccessDTO.getPassword());
            final var encryptedPassword = passwordEncoder.encode(decryptedPassword);
            final var mUserEntity = userService.findEntityById(passwordProccessDTO.getIdUser());
            final var mLogPassEntity = LogPassEntity.builder().password(encryptedPassword)
                    .expired(false)
                    .expiredDate(DateUtils.addMonths(new Date(), 3))
                    .idUser(mUserEntity)
                    .build();
            final var savedLog = iLogPassRepository.save(mLogPassEntity);
            log.info("Password Log For: {} created with id {}", mUserEntity.getUserName(), savedLog.getIdLogPass());
            return LogPasswordResponse.builder()
                    .username(mUserEntity.getUserName())
                    .expirationDate(savedLog.getExpiredDate())
                    .build();
        } catch (Exception e) {
            log.error("error in password log creation for userid: {} ", passwordProccessDTO.getIdUser());
            throw new RuntimeException(e);
        }
    }


}
