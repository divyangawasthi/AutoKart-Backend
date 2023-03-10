package com.fastenal.microservices.userms.dtos;

public class SignInResponseDTO {
    private String status;
    private String token;

    public SignInResponseDTO(String status, String token, User user) {
        this.status = status;
        this.token = token;
        this.user = user;
    }

    public User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
