package com.infotamia.weather.dao;

import com.infotamia.weather.pojos.entities.RoleEntity;
import com.infotamia.weather.pojos.entities.UserEntity;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mohammed Al-Ani
 */
@Service
public class UserDao {

    private Integer idCount = 0;
    private final Map<String, UserEntity> emailUserMap = new ConcurrentHashMap<>();
    private final Map<Integer, UserEntity> idUserMap = new ConcurrentHashMap<>();
    public UserDao() {
        // base data!
        UserEntity u1 = new UserEntity();
        u1.setId(++idCount);
        u1.setEmail("mohammedalanny@gmail.com");
        u1.setFirstName("moe");
        u1.setLastName("al-ani");
        RoleEntity roleEntity = new RoleEntity();
        roleEntity.setId(1);
        roleEntity.setName("USER");
        roleEntity.setPriority(-1);
        u1.getRoles().add(roleEntity);

        emailUserMap.put(u1.getEmail(), u1);
        idUserMap.put(u1.getId(), u1);
    }

    /**
     * Create single user entity.
     *
     * @param userEntity
     * @return created UserEntity
     */
    public UserEntity insertUser(UserEntity userEntity) {
        if (!emailUserMap.containsKey(userEntity.getEmail())) {
            userEntity.setId(++idCount);
            idUserMap.put(userEntity.getId(), userEntity);
            emailUserMap.put(userEntity.getEmail(), userEntity);
            return userEntity;
        }
        return emailUserMap.get(userEntity.getEmail());
    }

    /**
     * Fetch user entity by id.
     *
     * @param id
     * @return Optional<UserEntity>
     */
    public Optional<UserEntity> findUserById(Integer id) {
        return Optional.ofNullable(idUserMap.get(id));
    }

    /**
     * Fetch user entity by email
     *
     * @param email
     * @return Optional<UserEntity>
     */
    public Optional<UserEntity> findUserByEmail(String email) {
        return Optional.ofNullable(emailUserMap.get(email));
    }
}