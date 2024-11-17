package com.example.SkinSwap.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long steamid;
    private String username;
    private String avatar;
    private String tradelink;

    public User() {}

    public User(long steamId, String username, String avatarUrl) {
        this.steamid = steamId;
        this.username = username;
        this.avatar = avatarUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getSteamid() {
        return steamid;
    }

    public void setSteamid(long steamId) {
        this.steamid = steamId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
