package com.piramidsoft.wablastadmin.Model;

/**
 * Created by Ayo Maju on 10/07/2018.
 * Updated by Muhammad Iqbal on 10/07/2018.
 */

public class LogModel {
    String tgl,pengirim,teks,count,status,interval;

    public LogModel() {
    }

    public LogModel(String tgl, String pengirim, String teks, String count) {
        this.tgl = tgl;
        this.pengirim = pengirim;
        this.teks = teks;
        this.count = count;
    }

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }

    public String getPengirim() {
        return pengirim;
    }

    public void setPengirim(String pengirim) {
        this.pengirim = pengirim;
    }

    public String getTeks() {
        return teks;
    }

    public void setTeks(String teks) {
        this.teks = teks;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }
}
