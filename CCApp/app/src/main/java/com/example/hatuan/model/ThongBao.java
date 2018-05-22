package com.example.hatuan.model;

/**
 * Created by Ha Tuan on 4/10/2018.
 */

public class ThongBao {
    private String id;
    private String thoiGian;
    private String hinhAnh;
    private String tieude;
    private String noiDung;

    public ThongBao(String id, String thoiGian, String tieude, String noiDung) {
        this.id = id;
        this.thoiGian = thoiGian;
        this.tieude = tieude;
        this.noiDung = noiDung;
    }

    public ThongBao(String id, String thoiGian, String hinhAnh, String tieude, String noiDung) {
        this.id = id;
        this.thoiGian = thoiGian;
        this.hinhAnh = hinhAnh;
        this.tieude = tieude;
        this.noiDung = noiDung;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(String thoiGian) {
        this.thoiGian = thoiGian;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public String getTieude() {
        return tieude;
    }

    public void setTieude(String tieude) {
        this.tieude = tieude;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }
}
