package com.example.hatuan.model;

/**
 * Created by Ha Tuan on 4/1/2018.
 */

public class NhiemVu {
    private String id;
    private String nhiemvu;
    private String checknhiemvu;
    private String tuantrongnam;
    private String ngaytrongtuan;
    private String ngaytrongthang;
    private String idnhanvien;

    public NhiemVu(String id, String nhiemvu, String checknhiemvu,String tuantrongnam, String ngaytrongtuan, String ngaytrongthang, String idnhanvien) {
        this.id = id;
        this.nhiemvu = nhiemvu;
        this.checknhiemvu = checknhiemvu;
        this.tuantrongnam = tuantrongnam;
        this.ngaytrongtuan = ngaytrongtuan;
        this.ngaytrongthang = ngaytrongthang;
        this.idnhanvien = idnhanvien;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNhiemvu() {
        return nhiemvu;
    }

    public void setNhiemvu(String nhiemvu) {
        this.nhiemvu = nhiemvu;
    }

    public String getChecknhiemvu() {
        return checknhiemvu;
    }

    public String getTuantrongnam() {
        return tuantrongnam;
    }

    public void setTuantrongnam(String tuantrongnam) {
        this.tuantrongnam = tuantrongnam;
    }

    public void setChecknhiemvu(String checknhiemvu) {
        this.checknhiemvu = checknhiemvu;
    }

    public String getNgaytrongtuan() {
        return ngaytrongtuan;
    }

    public void setNgaytrongtuan(String ngaytrongtuan) {
        this.ngaytrongtuan = ngaytrongtuan;
    }

    public String getNgaytrongthang() {
        return ngaytrongthang;
    }

    public void setNgaytrongthang(String ngaytrongthang) {
        this.ngaytrongthang = ngaytrongthang;
    }

    public String getIdnhanvien() {
        return idnhanvien;
    }

    public void setIdnhanvien(String idnhanvien) {
        this.idnhanvien = idnhanvien;
    }
}
