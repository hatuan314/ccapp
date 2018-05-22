package com.example.hatuan.model;

/**
 * Created by Ha Tuan on 4/3/2018.
 */

public class NhiemVuNgay {
    private String id;
    private String nhiemVu;
    private String checkNhiemVu;

    public NhiemVuNgay(String id, String nhiemVu, String checkNhiemVu) {
        this.id = id;
        this.nhiemVu = nhiemVu;
        this.checkNhiemVu = checkNhiemVu;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNhiemVu() {
        return nhiemVu;
    }

    public void setNhiemVu(String nhiemVu) {
        this.nhiemVu = nhiemVu;
    }

    public String getCheckNhiemVu() {
        return checkNhiemVu;
    }

    public void setCheckNhiemVu(String checkNhiemVu) {
        this.checkNhiemVu = checkNhiemVu;
    }
}
