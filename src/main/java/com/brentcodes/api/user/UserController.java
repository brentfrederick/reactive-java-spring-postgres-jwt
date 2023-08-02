package com.brentcodes.api.user;

import com.brentcodes.entity.user.Users;
import com.brentcodes.service.user.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
public class UserController {
    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("/rest/chat/user")
    Mono<CreateUserResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
        return userService.create(request.toUser()).map(user -> new CreateUserResponse(user.getId(), user.getUniqueId()));
    }

    record CreateUserRequest(@NotBlank @Size(max = 255) String username, @NotBlank @Size(max = 255) String password,
                             @NotBlank @Size(max = 255) String uniqueId, @Size(max = 255) String pushId,
                             @Size(max = 255) String brand,
                             @Size(max = 255) String buildId, @Size(max = 255) String carrier,
                             @NotBlank @Size(max = 255) String deviceId,
                             @NotBlank @Size(max = 255) String deviceToken, @Size(max = 255) String manufacturer,
                             @Size(max = 255) String model, @Size(max = 255) String systemName,
                             @Size(max = 255) String systemVersion,
                             @NotBlank @Size(max = 255) String appVersion,
                             @NotBlank @Size(max = 255) String appBuildNumber) {

        Users toUser() {
            return new Users(username, password, uniqueId, pushId, brand, buildId, carrier, deviceId, deviceToken,
                    manufacturer, model, systemName, systemVersion, appVersion, appBuildNumber);
        }
    }

    record CreateUserResponse(@NotNull UUID userId, @NotNull String uniqueId) {
    }
}
