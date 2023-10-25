package com.firebaseclound.chatonline;

class Message{
    private String avatar;
    private String name;
    private String username;
    private String taikhoan;
    private String noidung;
    private String time;

    public Message(String avatar, String name, String username, String taikhoan, String noidung, String time) {
        this.avatar = avatar;
        this.name = name;
        this.username = username;
        this.taikhoan = taikhoan;
        this.noidung = noidung;
        this.time = time;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTaikhoan() {
        return taikhoan;
    }

    public void setTaikhoan(String taikhoan) {
        this.taikhoan = taikhoan;
    }

    public String getNoidung() {
        return noidung;
    }

    public void setNoidung(String noidung) {
        this.noidung = noidung;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
