package org.acme.controllers.upload_minio.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tdr_jumlah_sdm_vendor")
public class TdrJumlahSdmVendorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_jumlah_sdm")
    private Long id_jumlah_sdm;
    

    @Column(name = "id_identitas")
    private Long id_identitas;

    @Column(name = "manajer")
    private String manajer;

    @Column(name = "pekerja")
    private String pekerja;

    @Column(name = "tenaga_ahli")
    private String tenaga_ahli;

    public Long getId_jumlah_sdm() {
        return id_jumlah_sdm;
    }

    public void setId_jumlah_sdm(Long id_jumlah_sdm) {
        this.id_jumlah_sdm = id_jumlah_sdm;
    }

    public Long getId_identitas() {
        return id_identitas;
    }

    public void setId_identitas(Long id_identitas) {
        this.id_identitas = id_identitas;
    }

    public String getManajer() {
        return manajer;
    }

    public void setManajer(String manajer) {
        this.manajer = manajer;
    }

    public String getPekerja() {
        return pekerja;
    }

    public void setPekerja(String pekerja) {
        this.pekerja = pekerja;
    }

    public String getTenaga_ahli() {
        return tenaga_ahli;
    }

    public void setTenaga_ahli(String tenaga_ahli) {
        this.tenaga_ahli = tenaga_ahli;
    }

    

}
