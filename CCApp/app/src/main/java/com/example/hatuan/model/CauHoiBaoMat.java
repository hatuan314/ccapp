package com.example.hatuan.model;

/**
 * Created by Ha Tuan on 25/03/2018.
 */

public class CauHoiBaoMat {
    private String id;
    private String cauhoi;
    private String cautraloi;
    private String idnhanvien;

    public CauHoiBaoMat(String id, String cauhoi, String cautraloi, String idnhanvien) {
        this.id = id;
        this.cauhoi = cauhoi;
        this.cautraloi = cautraloi;
        this.idnhanvien = idnhanvien;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCauhoi() {
        return cauhoi;
    }

    public void setCauhoi(String cauhoi) {
        this.cauhoi = cauhoi;
    }

    public String getCautraloi() {
        return cautraloi;
    }

    public void setCautraloi(String cautraloi) {
        this.cautraloi = cautraloi;
    }

    public String getIdnhanvien() {
        return idnhanvien;
    }

    public void setIdnhanvien(String idnhanvien) {
        this.idnhanvien = idnhanvien;
    }
}
