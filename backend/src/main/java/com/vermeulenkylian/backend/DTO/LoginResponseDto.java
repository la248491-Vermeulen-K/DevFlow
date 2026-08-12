package com.vermeulenkylian.backend.DTO;

public class LoginResponseDto {
    private String token;
    private Long id;
    private String name;
    private String email;
    private String refreshToken;

    public LoginResponseDto() {
    }

    public LoginResponseDto(String token, Long id, String name, String email, String refreshToken) {
        this.token = token;
        this.id = id;
        this.name = name;
        this.email = email;
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
