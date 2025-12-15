package org.acme.controllers.upload_minio.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tdr_pemegang_saham_vendor")
public class TdrPemegangSahamVendorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pemegang_saham")
    private Long id_pemegang_saham;


    @Column(name = "id_identitas")
    private Long id_identitas;

    @Column(name = "kode_tipe", length = 3)
    private Integer kode_tipe;

    @Column(name = "tipe", length = 20)
    private String tipe;

    @Column(name = "nama")
    private String nama;

    @Column(name = "jabatan")
    private String jabatan;

    @Column(name = "kode_kewarganegaraan", length = 3)
    private Integer kode_kewarganegaraan;

    @Column(name = "kewarganegaraan", length = 50)
    private String kewarganegaraan;

    @Column(name = "kode_jenis_kepemilikan", length = 3)
    private Integer kode_jenis_kepemilikan;
    
    @Column(name = "jenis_kepemilikan", length = 25)
    private String jenis_kepemilikan;

    @Column(name = "nik", length = 20)
    private String nik;

    @Column(name = "npwp")
    private String npwp;

    @Column(name = "ktp_kitas", length = 50)
    private String ktp_kitas;

    @Column(name = "jumlah_saham")
    private Integer jumlah_saham;

    @Column(name = "nominal", precision = 20, scale = 2)
    private BigDecimal nominal_saham;

    @Column(name = "kepemilikan", length = 5)
    private Integer kepemilikan;

    public Long getId_pemegang_saham() {
        return id_pemegang_saham;
    }

    public void setId_pemegang_saham(Long id_pemegang_saham) {
        this.id_pemegang_saham = id_pemegang_saham;
    }

    public Long getId_identitas() {
        return id_identitas;
    }

    public void setId_identitas(Long id_identitas) {
        this.id_identitas = id_identitas;
    }

    public Integer getKode_tipe() {
        return kode_tipe;
    }

    public void setKode_tipe(Integer kode_tipe) {
        this.kode_tipe = kode_tipe;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJabatan() {
        return jabatan;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }

    public Integer getKode_kewarganegaraan() {
        return kode_kewarganegaraan;
    }

    public void setKode_kewarganegaraan(Integer kode_kewarganegaraan) {
        this.kode_kewarganegaraan = kode_kewarganegaraan;
    }

    public String getKewarganegaraan() {
        return kewarganegaraan;
    }

    public void setKewarganegaraan(String kewarganegaraan) {
        this.kewarganegaraan = kewarganegaraan;
    }

    public Integer getKode_jenis_kepemilikan() {
        return kode_jenis_kepemilikan;
    }

    public void setKode_jenis_kepemilikan(Integer kode_jenis_kepemilikan) {
        this.kode_jenis_kepemilikan = kode_jenis_kepemilikan;
    }

    public String getJenis_kepemilikan() {
        return jenis_kepemilikan;
    }

    public void setJenis_kepemilikan(String jenis_kepemilikan) {
        this.jenis_kepemilikan = jenis_kepemilikan;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getNpwp() {
        return npwp;
    }

    public void setNpwp(String npwp) {
        this.npwp = npwp;
    }

    public String getKtp_kitas() {
        return ktp_kitas;
    }

    public void setKtp_kitas(String ktp_kitas) {
        this.ktp_kitas = ktp_kitas;
    }

    public Integer getJumlah_saham() {
        return jumlah_saham;
    }

    public void setJumlah_saham(Integer jumlah_saham) {
        this.jumlah_saham = jumlah_saham;
    }

    public BigDecimal getNominal_saham() {
        return nominal_saham;
    }

    public void setNominal_saham(BigDecimal nominal_saham) {
        this.nominal_saham = nominal_saham;
    }

    public Integer getKepemilikan() {
        return kepemilikan;
    }

    public void setKepemilikan(Integer kepemilikan) {
        this.kepemilikan = kepemilikan;
    }


    

}
