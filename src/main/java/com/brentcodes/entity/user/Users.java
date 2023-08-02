package com.brentcodes.entity.user;

import com.brentcodes.entity.BaseLockingEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class Users extends BaseLockingEntity implements UserDetails {
    @Size(max = 255)
    private String username;
    @Size(max = 255)
    private String password;
    @NotNull
    private UserRole role;
    private boolean enabled;
    private boolean accountNonLocked;
    private boolean accountNonExpired;
    private boolean credentialsNonExpired;

    // https://github.com/react-native-device-info/react-native-device-info#api
    @Size(max = 255)
    private String pushId;

    @Size(max = 255)
    private String brand;

    @Size(max = 255)
    private String buildId;

    @Size(max = 255)
    private String carrier;

    @Size(max = 255)
    private String deviceId;

    @NotBlank
    @Size(max = 255)
    private String deviceToken;

    @Size(max = 255)
    private String manufacturer;

    @Size(max = 255)
    private String model;

    @Size(max = 255)
    private String systemName;

    @Size(max = 255)
    private String systemVersion;

    @NotBlank
    @Size(max = 255)
    private String uniqueId;

    @Size(max = 255)
    private String appVersion;

    @Size(max = 255)
    private String appBuildNumber;

    public Users() {
    }

    public Users(String username, String password, String uniqueId, String pushId, String brand, String buildId,
                 String carrier, String deviceId, String deviceToken, String manufacturer, String model,
                 String systemName, String systemVersion, String appVersion, String appBuildNumber) {
        this.username = username;
        this.password = password;
        this.role = UserRole.USER;
        this.enabled = true;
        this.accountNonLocked = true;
        this.accountNonExpired = true;
        this.credentialsNonExpired = true;
        this.uniqueId = uniqueId;
        this.pushId = pushId;
        this.brand = brand;
        this.buildId = buildId;
        this.carrier = carrier;
        this.deviceId = deviceId;
        this.deviceToken = deviceToken;
        this.manufacturer = manufacturer;
        this.model = model;
        this.systemName = systemName;
        this.systemVersion = systemVersion;
        this.appVersion = appVersion;
        this.appBuildNumber = appBuildNumber;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBuildId() {
        return buildId;
    }

    public void setBuildId(String buildId) {
        this.buildId = buildId;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getSystemVersion() {
        return systemVersion;
    }

    public void setSystemVersion(String systemVersion) {
        this.systemVersion = systemVersion;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getAppBuildNumber() {
        return appBuildNumber;
    }

    public void setAppBuildNumber(String appBuildNumber) {
        this.appBuildNumber = appBuildNumber;
    }
}
