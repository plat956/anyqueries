package by.latushko.anyqueries.model.entity;

import java.time.LocalDateTime;

public class UserHash extends BaseEntity<Long>{
    private String hash;
    private LocalDateTime expires;
    private User user;

    public UserHash() {
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public LocalDateTime getExpires() {
        return expires;
    }

    public void setExpires(LocalDateTime expires) {
        this.expires = expires;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserHash userHash = (UserHash) o;

        if (hash != null ? !hash.equals(userHash.hash) : userHash.hash != null) return false;
        if (expires != null ? !expires.equals(userHash.expires) : userHash.expires != null) return false;
        return user != null ? user.equals(userHash.user) : userHash.user == null;
    }

    @Override
    public int hashCode() {
        int result = hash != null ? hash.hashCode() : 0;
        result = 31 * result + (expires != null ? expires.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("UserHash{");
        sb.append("hash='").append(hash).append('\'');
        sb.append(", expires=").append(expires);
        sb.append(", user=").append(user);
        sb.append('}');
        return sb.toString();
    }
}
