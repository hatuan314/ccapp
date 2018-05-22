package com.example.hatuan.model;

/**
 * Created by Ha Tuan on 23/03/2018.
 */

public class NhanVien {
    private String id;
    private String username;
    private String password;
    private String fullname;
    private String email;
    private String phone;
    private String birthday;
    private String address;
    private String rom;
    private String chưcvu;
    private String imei;
    private String avatar;
    private String cover;

    public NhanVien(String id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public NhanVien(String id, String username, String password, String email, String fullname, String phone, String birthday,
                    String address, String rom, String chưcvu, String imei, String avatar, String cover) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.email = email;
        this.phone = phone;
        this.birthday = birthday;
        this.address = address;
        this.rom = rom;
        this.chưcvu = chưcvu;
        this.imei = imei;
        this.avatar = avatar;
        this.cover = cover;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRom() {
        return rom;
    }

    public void setRom(String rom) {
        this.rom = rom;
    }

    public String getChưcvu() {
        return chưcvu;
    }

    public void setChưcvu(String chưcvu) {
        this.chưcvu = chưcvu;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
}
