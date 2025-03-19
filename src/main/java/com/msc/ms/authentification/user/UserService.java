package com.msc.ms.authentification.user;

import com.msc.ms.authentification.authentication.error.UsernameOrEmailInvalidException;
import com.msc.ms.authentification.common.error.DataBaseObjectNotFound;
import com.msc.ms.authentification.log.ILogPassRepository;
import com.msc.ms.authentification.log.model.LogPassEntity;
import com.msc.ms.authentification.user.model.UserDetailDTO;
import com.msc.ms.authentification.user.model.UserEntity;
import com.msc.ms.authentification.user.model.UserRequest;
import com.msc.ms.authentification.user.model.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Slf4j
public class UserService {
    private final IUserRepository iUserRepository;
    private final ILogPassRepository iLogPassRepository;

    public UserService(IUserRepository iUserRepository, ILogPassRepository iLogPassRepository) {
        this.iUserRepository = iUserRepository;
        this.iLogPassRepository = iLogPassRepository;
    }

    public UserResponse registryNewUser(UserRequest pUSerRequest) {
        return null;
    }

    public UserEntity findUser(String pEmailOrUsername) throws UsernameOrEmailInvalidException {
        var optionalUser = this.iUserRepository.findByUserName(pEmailOrUsername);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            optionalUser = this.iUserRepository.findByEmail(pEmailOrUsername);
            if (optionalUser.isPresent()) {
                return optionalUser.get();
            }
        }
        throw new UsernameOrEmailInvalidException(pEmailOrUsername);
    }

    public UserDetailDTO buildUserDetails(String username) {
        try {
            log.info("trying to log in for user {}", username);
            final var user = this.findUser(username);
            final var logPassword = getPassword(user);

            return new UserDetailDTO(user.getUserName(), logPassword.getPassword(), user.getActive(), logPassword.getExpired(), List.of("test"));
        } catch (UsernameOrEmailInvalidException e) {
            log.info("user {} not found", username);
            throw new UsernameNotFoundException(username);
        }
    }

    public Boolean validIsUsernameACurrentUser(String username) {
        final var mOptionalUserEntity = iUserRepository.findByUserName(username);
        return mOptionalUserEntity.isPresent();
    }

    public Boolean validIsEmailACurrentUser(String pEmail) {
        final var mOptionUserEntity = iUserRepository.findByEmail(pEmail);
        return mOptionUserEntity.isPresent();
    }

    public UserEntity findEntityById(Integer pUserId) throws DataBaseObjectNotFound {
        final var mOptionalUserEntity = iUserRepository.findById(pUserId);
        if (mOptionalUserEntity.isPresent()) {
            return mOptionalUserEntity.get();
        } else {
            throw new DataBaseObjectNotFound("UserId", "UserNot Found by id", pUserId.toString());
        }
    }

    protected LogPassEntity getPassword(UserEntity userEntity) {
        return this.iLogPassRepository.finaAllByUser(userEntity);
    }

}