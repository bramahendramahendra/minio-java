package org.acme.controllers.upload_minio.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "tdr_perbankan_vendor_minio")
public class TdrPerbankanVendorMinioEntity {
      @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "id_perbankan")
    private Long id_perbankan;

    @Column(name = "id_identitas")
    private Long id_identitas;

    @Column(name = "have_bri_account", length = 1)
    private Boolean have_account_bri;

    @Column(name = "lokasi_bank", length = 2)
    private String lokasi_bank;

    @Column(name = "kode_bank", length = 50)
    private String kode_bank;

    @Column(name = "nama_bank")
    private String nama_bank;

    @Column(name = "city")
    private String city;

    @Column(name = "bank_key")
    private String bank_key;

    @Column(name = "negara")
    private String negara;

    @Column(name = "kode_negara", length = 5)
    private String kode_negara;

    @Column(name = "no_rek", length = 50)
    private String no_rek;

    @Column(name = "nama_rek")
    private String nama_rek;

    @Column(name = "swift_code")
    private String swift_kode;

    @Column(name = "cabang_bank")
    private String cabang_bank;

    @Column(name = "koran_path")
    private String koran_path;

    @Column(name = "minio_path", columnDefinition = "TEXT")
    private String minio_path;

    @Column(name = "upload_date")
    private LocalDateTime upload_minio;

    public String getKode_negara() {
        return kode_negara;
    }

    public void setKode_negara(String kode_negara) {
        this.kode_negara = kode_negara;
    }

    public String getMinio_path() {
        return minio_path;
    }

    public void setMinio_path(String minio_path) {
        this.minio_path = minio_path;
    }

    public LocalDateTime getUpload_minio() {
        return upload_minio;
    }

    public void setUpload_minio(LocalDateTime upload_minio) {
        this.upload_minio = upload_minio;
    }

    public Long getId_perbankan() {
        return id_perbankan;
    }

    public void setId_perbankan(Long id_perbankan) {
        this.id_perbankan = id_perbankan;
    }

    public Long getId_identitas() {
        return id_identitas;
    }

    public void setId_identitas(Long id_identitas) {
        this.id_identitas = id_identitas;
    }

    public Boolean getHave_account_bri() {
        return have_account_bri;
    }

    public void setHave_account_bri(Boolean have_account_bri) {
        this.have_account_bri = have_account_bri;
    }

    public String getLokasi_bank() {
        return lokasi_bank;
    }

    public void setLokasi_bank(String lokasi_bank) {
        this.lokasi_bank = lokasi_bank;
    }

    public String getKode_bank() {
        return kode_bank;
    }

    public void setKode_bank(String kode_bank) {
        this.kode_bank = kode_bank;
    }

    public String getNama_bank() {
        return nama_bank;
    }

    public void setNama_bank(String nama_bank) {
        this.nama_bank = nama_bank;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBank_key() {
        return bank_key;
    }

    public void setBank_key(String bank_key) {
        this.bank_key = bank_key;
    }

    public String getNegara() {
        return negara;
    }

    public void setNegara(String negara) {
        this.negara = negara;
    }

    public String getNo_rek() {
        return no_rek;
    }

    public void setNo_rek(String no_rek) {
        this.no_rek = no_rek;
    }

    public String getNama_rek() {
        return nama_rek;
    }

    public void setNama_rek(String nama_rek) {
        this.nama_rek = nama_rek;
    }

    public String getSwift_kode() {
        return swift_kode;
    }

    public void setSwift_kode(String swift_kode) {
        this.swift_kode = swift_kode;
    }

    public String getCabang_bank() {
        return cabang_bank;
    }

    public void setCabang_bank(String cabang_bank) {
        this.cabang_bank = cabang_bank;
    }

    public String getKoran_path() {
        return koran_path;
    }

    public void setKoran_path(String koran_path) {
        this.koran_path = koran_path;
    }
}
