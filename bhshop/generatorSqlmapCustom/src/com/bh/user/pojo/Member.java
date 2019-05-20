package com.bh.user.pojo;

public class Member {
    private Integer id;

    private String username;

    private String password;

    private String phone;

    private String salt;

    private String headimgurl;

    private Integer flagUser;

    private Integer type;

    private String im;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt == null ? null : salt.trim();
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl == null ? null : headimgurl.trim();
    }

    public Integer getFlagUser() {
        return flagUser;
    }

    public void setFlagUser(Integer flagUser) {
        this.flagUser = flagUser;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getIm() {
        return im;
    }

    public void setIm(String im) {
        this.im = im == null ? null : im.trim();
    }
}