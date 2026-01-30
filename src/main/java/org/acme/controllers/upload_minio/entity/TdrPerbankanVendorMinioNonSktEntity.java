package org.acme.controllers.upload_minio.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "tdr_perbankan_vendor_minio_nonskt")
public class TdrPerbankanVendorMinioNonSktEntity {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "id_perbankan_minio")
    private Long id_perbankan_minio;

    @Column(name = "id_perbankan")
    private Long id_perbankan;

    @Column(name = "id_identitas")
    private Long id_identitas;

    @Column(name = "nama_bank")
    private String nama_bank;

    @Column(name = "nama_rek")
    private String nama_rek;

    @Column(name = "koran_path")
    private String koran_path;

    @Column(name = "minio_path", columnDefinition = "TEXT")
    private String minio_path;

    @Column(name = "status")
    private String status; // "SUCCESS", "FAILED", "SKIPPED"

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "upload_date")
    private LocalDateTime upload_date;

    public Long getId_perbankan_minio() {
        return id_perbankan_minio;
    }

    public void setId_perbankan_minio(Long id_perbankan_minio) {
        this.id_perbankan_minio = id_perbankan_minio;
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

    public String getNama_bank() {
        return nama_bank;
    }

    public void setNama_bank(String nama_bank) {
        this.nama_bank = nama_bank;
    }

    public String getNama_rek() {
        return nama_rek;
    }

    public void setNama_rek(String nama_rek) {
        this.nama_rek = nama_rek;
    }

    public String getKoran_path() {
        return koran_path;
    }

    public void setKoran_path(String koran_path) {
        this.koran_path = koran_path;
    }

    public String getMinio_path() {
        return minio_path;
    }

    public void setMinio_path(String minio_path) {
        this.minio_path = minio_path;
    }

     public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public LocalDateTime getUpload_date() {
        return upload_date;
    }

    public void setUpload_date(LocalDateTime upload_date) {
        this.upload_date = upload_date;
    }
}
