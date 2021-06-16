package com.infotamia.weather.rest.user;

import com.infotamia.weather.access.AbstractPrincipal;
import com.infotamia.weather.access.AuthenticationProvider;
import com.infotamia.weather.exception.ItemNotFoundException;
import com.infotamia.weather.pojos.common.user.FacebookExternalAccount;
import com.infotamia.weather.pojos.entities.CurrentUser;
import com.infotamia.weather.pojos.entities.UserEntity;
import com.infotamia.weather.services.oauth.BaseOAuth20Service;
import com.infotamia.weather.services.user.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author Mohammed Al-Ani
 */
@RestController
@RequestMapping(path = "user")
public class UserResource {

    AbstractPrincipal user;
    UserService userService;
    BaseOAuth20Service facebookAuthenticationService;


    public UserResource(
            AbstractPrincipal user,
            UserService userService,
            @Qualifier("facebook") BaseOAuth20Service facebookAuthenticationService) {
        this.user = user;
        this.userService = userService;
        this.facebookAuthenticationService = facebookAuthenticationService;
    }

    @GetMapping(path = "profile")
    public UserEntity getProfile() throws ItemNotFoundException {
        UserEntity userEntity = userService.findUserById(this.user.getId());

        AuthenticationProvider provider = user.getAuthenticationProvider();
        if (provider == null) return userEntity;
        String accessToken = user.getAuthenticationProviderAccessToken();
        String imageUrl;
        if (provider == AuthenticationProvider.FACEBOOK) {
            FacebookExternalAccount accountDetails = facebookAuthenticationService.getAccountDetails(accessToken);
            imageUrl = accountDetails.getPicture() == null ? null : accountDetails.getPicture().getData().getUrl();
        } else {
            imageUrl = "";
        }
        userEntity.setProfileImageUrl(imageUrl);
        return userEntity;
    }

    @GetMapping(path = "current")
    public CurrentUser getCurrentUser() {
        CurrentUser currentUser = new CurrentUser();
        currentUser.setId(user.getId());
        currentUser.setSessionType(user.getSessionType().name());
        return currentUser;
    }
}
