package com.infotamia.weather.services.user;

import com.infotamia.weather.dao.UserDao;
import com.infotamia.weather.exception.BaseErrorCode;
import com.infotamia.weather.exception.ItemNotFoundException;
import com.infotamia.weather.pojos.entities.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * User service for general CRUD for {@link UserEntity}
 *
 * @author Mohammed Al-Ani
 */
@Service
@RequestScope
public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    /**
     * Fetch single user entity by email.
     *
     * @param email
     * @return Optional<UserEntity>
     */
    public Optional<UserEntity> findUserByEmail(String email) {
        return userDao.findUserByEmail(email);
    }

    /**
     * Fetch single user entity by user id.
     *
     * @param id
     * @return UserEntity
     * @throws ItemNotFoundException
     */
    public UserEntity findUserById(Integer id) throws ItemNotFoundException {
        return userDao.findUserById(id)
                .orElseThrow(() -> new ItemNotFoundException("driver not found", BaseErrorCode.USER_NOT_FOUND));
    }

    /**
     * Create single user entity.
     *
     * @param user
     * @return UserEntity
     */
    public UserEntity insertUser(@NotNull(message = "user was null") UserEntity user) {
        UserEntity userEntity = userDao.findUserByEmail(user.getEmail()).orElse(null);
        if (userEntity == null) {
            userEntity = userDao.insertUser(user);
        }
        return userEntity;
    }
}
