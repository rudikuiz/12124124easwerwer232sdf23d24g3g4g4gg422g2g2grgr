package com.piramidsoft.wablastadmin.Model;

/**
 * Created by Ayo Maju on 10/07/2018.
 * Updated by Muhammad Iqbal on 10/07/2018.
 */

public class ProcessModel {
    String id, tgl, teks, imsi, nomor, status, foto, nama, lokasi, wa, tg, waSend, tgSend;
    boolean selected;

    public ProcessModel() {
    }

    public ProcessModel(String id, String tgl, String teks, String imsi, String nomor, String status, String foto, String nama, String lokasi, String wa, String tg, String waSend, String tgSend, boolean selected) {
        this.id = id;
        this.tgl = tgl;
        this.teks = teks;
        this.imsi = imsi;
        this.nomor = nomor;
        this.status = status;
        this.foto = foto;
        this.nama = nama;
        this.lokasi = lokasi;
        this.wa = wa;
        this.tg = tg;
        this.waSend = waSend;
        this.tgSend = tgSend;
        this.selected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTgl() {
        return tgl;
    }

    public void setTgl(String tgl) {
        this.tgl = tgl;
    }

    public String getTeks() {
        return teks;
    }

    public void setTeks(String teks) {
        this.teks = teks;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getNomor() {
        return nomor;
    }

    public void setNomor(String nomor) {
        this.nomor = nomor;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getWa() {
        return wa;
    }

    public void setWa(String wa) {
        this.wa = wa;
    }

    public String getTg() {
        return tg;
    }

    public void setTg(String tg) {
        this.tg = tg;
    }

    public String getWaSend() {
        return waSend;
    }

    public void setWaSend(String waSend) {
        this.waSend = waSend;
    }

    public String getTgSend() {
        return tgSend;
    }

    public void setTgSend(String tgSend) {
        this.tgSend = tgSend;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
