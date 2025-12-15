package org.acme.controllers.upload_minio.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "mst_bidang_usaha_vendor")
public class TdrMstBidangUsahaEntity {
    @Id
    @Column(name = "kode", length = 3)
    private String kode;

    @Column(name = "deskripsi")
    private String deskripsi;

    @Column(name = "activated")
    private Boolean activated;

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    

}
