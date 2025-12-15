package org.acme.controllers.upload_minio.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "mst_material_vendor")
public class TdrMaterialVendorEntity {
    @Id
    @Column(name = "kode", length = 5)
    private String kode;

    @Column(name = "kode_sub_bidang_usaha", length = 3)
    private String kode_sub_bidang_usaha;

    @Column(name = "deskripsi")
    private String deskripsi;

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getKode_sub_bidang_usaha() {
        return kode_sub_bidang_usaha;
    }

    public void setKode_bidang_usaha(String kode_sub_bidang_usaha) {
        this.kode_sub_bidang_usaha = kode_sub_bidang_usaha;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    
}
