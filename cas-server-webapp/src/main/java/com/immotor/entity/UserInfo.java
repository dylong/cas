package com.immotor.entity;

import java.sql.Date;

import org.bouncycastle.jcajce.provider.digest.MD5;
import org.jasig.cas.authentication.handler.DefaultPasswordEncoder;


public class UserInfo {

    private String id;
    private String area_code;
    private String phone;
    private String password;
    private String email;
    private String nickname;
    private String gender;
    private String level;
    private String avatar;
    private String statement;
    private String active;
    private String remember_token;
    private Date created_at;
    private Date updated_at;
    private Date deleted_at;
    private String device_type;
    private String scene;
    
    public String getScene() {
        return scene;
    }
    
    public void setScene(String scene) {
        this.scene = scene;
    }

    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }

    public String getArea_code() {
        return area_code;
    }
    
    public void setArea_code(String area_code) {
        this.area_code = area_code;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        DefaultPasswordEncoder dpe = new DefaultPasswordEncoder("MD5");
        this.password = dpe.encode(password);
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getNickname() {
        return nickname;
    }
    
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public String getLevel() {
        return level;
    }
    
    public void setLevel(String level) {
        this.level = level;
    }
    
    public String getAvatar() {
        return avatar;
    }
    
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    
    public String getStatement() {
        return statement;
    }
    
    public void setStatement(String statement) {
        this.statement = statement;
    }
    
    public String getActive() {
        return active;
    }
    
    public void setActive(String active) {
        this.active = active;
    }
    
    public String getRemember_token() {
        return remember_token;
    }
    
    public void setRemember_token(String remember_token) {
        this.remember_token = remember_token;
    }
    
    public Date getCreated_at() {
        return created_at;
    }
    
    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
    
    public Date getUpdated_at() {
        return updated_at;
    }
    
    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }
    
    public Date getDeleted_at() {
        return deleted_at;
    }
    
    public void setDeleted_at(Date deleted_at) {
        this.deleted_at = deleted_at;
    }
    
    public String getDevice_type() {
        return device_type;
    }
    
    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    @Override
    public String toString() {
        return "UserInfo [id=" + id + ", area_code=" + area_code + ", phone=" + phone + ", password=" + password + ", email=" + email + ", nickname=" + nickname + ", gender=" + gender + ", level=" + level + ", avatar=" + avatar + ", statement=" + statement + ", active=" + active + ", remember_token=" + remember_token + ", created_at=" + created_at + ", updated_at=" + updated_at + ", deleted_at=" + deleted_at + ", device_type=" + device_type + "]";
    }
    public static void main(String[] args) {
        DefaultPasswordEncoder dpe = new DefaultPasswordEncoder("MD5");
        System.out.println(dpe.encode("123456"));
    }
}
