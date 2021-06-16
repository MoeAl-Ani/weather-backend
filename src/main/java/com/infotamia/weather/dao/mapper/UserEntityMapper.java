package com.infotamia.weather.dao.mapper;

import com.infotamia.weather.exception.BaseErrorCode;
import com.infotamia.weather.exception.ItemNotFoundException;
import com.infotamia.weather.pojos.entities.RoleEntity;
import com.infotamia.weather.pojos.entities.UserEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * General user entity mapper.
 *
 * @author Mohammed Al-Ani
 * @throws ItemNotFoundException
 */
@Service
public class UserEntityMapper {

    public List<UserEntity> applyUserMapper(List<Object[]> resultSet) throws ItemNotFoundException {
        Map<Integer, UserEntity> userEntityMap = new HashMap<>();
        for (Object[] o : resultSet) {
            UserEntity userEntity = null;
            RoleEntity role = null;

            for (Object result : o) {
                if (result instanceof UserEntity) {
                    userEntity = (UserEntity) result;
                } else if (result instanceof RoleEntity) {
                    role = (RoleEntity) result;
                }
            }
            if (userEntity == null) {
                throw new ItemNotFoundException("user not found", BaseErrorCode.USER_NOT_FOUND);
            }
            Integer userId = userEntity.getId();
            userEntity = userEntityMap.get(userId) == null ? userEntity : userEntityMap.get(userId);
            if (role != null) userEntity.getRoles().add(role);
            userEntityMap.put(userEntity.getId(), userEntity);
        }
        return new ArrayList<>(userEntityMap.values());
    }
}
