package by.latushko.anyqueries.model.entity;

import java.time.LocalDateTime;

public class User extends BaseEntity<Long> {
    public enum Status {
        ACTIVE, INACTIVE, BANNED
    }
    public enum Role {
        ADMIN("danger"),
        USER("warning"),
        MODERATOR("success");

        private final String color;

        Role(String color) {
            this.color = color;
        }

        public String getColor() {
            return color;
        }
    }
    private String firstName;
    private String lastName;
    private String middleName;
    private String login;
    private String password;
    private String email;
    private String telegram;
    private String avatar;
    private String credentialKey;
    private LocalDateTime lastLoginDate;
    private Status status;
    private Role role;

    public User() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelegram() {
        return telegram;
    }

    public void setTelegram(String telegram) {
        this.telegram = telegram;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCredentialKey() {
        return credentialKey;
    }

    public void setCredentialKey(String credentialKey) {
        this.credentialKey = credentialKey;
    }

    public LocalDateTime getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(LocalDateTime lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getFio() {
        String firstName = this.getFirstName();
        String lastName = this.getLastName();
        String middleName = this.getMiddleName();
        return lastName.substring(0, 1).toUpperCase() + lastName.substring(1) + " "
                + firstName.substring(0, 1).toUpperCase() + ". "
                + middleName.substring(0, 1).toUpperCase() + ".";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (firstName != null ? !firstName.equals(user.firstName) : user.firstName != null) return false;
        if (lastName != null ? !lastName.equals(user.lastName) : user.lastName != null) return false;
        if (middleName != null ? !middleName.equals(user.middleName) : user.middleName != null) return false;
        if (login != null ? !login.equals(user.login) : user.login != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (telegram != null ? !telegram.equals(user.telegram) : user.telegram != null) return false;
        if (avatar != null ? !avatar.equals(user.avatar) : user.avatar != null) return false;
        if (credentialKey != null ? !credentialKey.equals(user.credentialKey) : user.credentialKey != null)
            return false;
        if (lastLoginDate != null ? !lastLoginDate.equals(user.lastLoginDate) : user.lastLoginDate != null)
            return false;
        if (status != user.status) return false;
        return role == user.role;
    }

    @Override
    public int hashCode() {
        int result = firstName != null ? firstName.hashCode() : 0;
        result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
        result = 31 * result + (middleName != null ? middleName.hashCode() : 0);
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (telegram != null ? telegram.hashCode() : 0);
        result = 31 * result + (avatar != null ? avatar.hashCode() : 0);
        result = 31 * result + (credentialKey != null ? credentialKey.hashCode() : 0);
        result = 31 * result + (lastLoginDate != null ? lastLoginDate.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (role != null ? role.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("User{");
        sb.append("firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", middleName='").append(middleName).append('\'');
        sb.append(", login='").append(login).append('\'');
        sb.append(", password='").append(password).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append(", telegram='").append(telegram).append('\'');
        sb.append(", avatar='").append(avatar).append('\'');
        sb.append(", credentialKey='").append(credentialKey).append('\'');
        sb.append(", lastLoginDate=").append(lastLoginDate);
        sb.append(", status=").append(status);
        sb.append(", role=").append(role);
        sb.append('}');
        return sb.toString();
    }
}
