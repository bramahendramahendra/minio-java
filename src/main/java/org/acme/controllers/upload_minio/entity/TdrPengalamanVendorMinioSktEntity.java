package org.acme.controllers.upload_minio.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;



@Entity
@Table(name = "tdr_pengalaman_vendor_minio")
public class TdrPengalamanVendorMinioSktEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pengalaman_minio")
    private Long id_pengalaman_minio;

    @Column(name = "id_pengalaman")
    private Long id_pengalaman;

    @Column(name = "id_identitas")
    private Long id_identitas;

    @Column(name = "nama_pekerjaan")
    private String nama_pekerjaan;

    // @Column(name = "nama_pemilik_pekerja")
    // private String nama_pemilik_pekerja;

    // @Column(name = "bidang_usaha", length = 3)
    // private String bidang_usaha;

    // @Column(name = "sub_bidang_usaha", length = 3)
    // private String sub_bidang_usaha;


    // @Column(name = "pemberi_tugas")
    // private String pemberi_tugas;

    // @Column(name = "no_spk")
    // private String no_spk;

    // @Column(name = "tgl_mulai_spk")
    // private LocalDate tgl_mulai_spk;

    // @Column(name = "tgl_selesai_spk")
    // private LocalDate tgl_selesai_spk;

    // @Column(name = "nilai_kontrak", precision = 20, scale = 2)
    // private BigDecimal nilai_kontrak;

    // @Column(name = "ba_pekerjaan")
    // private String ba_pekerjaan;

    @Column(name = "spk_path")
    private String spk_path;

    @Column(name = "minio_path", columnDefinition = "TEXT")
    private String minio_path;

    @Column(name = "status")
    private String status; // "SUCCESS", "FAILED", "SKIPPED"

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;
    
    @Column(name = "upload_date")
    private LocalDateTime upload_date;

    public Long getId_pengalaman_Minio() {
        return id_pengalaman_minio;
    }

    public void setId_pengalaman_Minio(Long id_pengalaman_minio) {
        this.id_pengalaman_minio = id_pengalaman_minio;
    }

    public Long getId_pengalaman() {
        return id_pengalaman;
    }

    public void setId_pengalaman(Long id_pengalaman) {
        this.id_pengalaman = id_pengalaman;
    }

    public Long getId_identitas() {
        return id_identitas;
    }

    public void setId_identitas(Long id_identitas) {
        this.id_identitas = id_identitas;
    }

    public String getNama_pekerjaan() {
        return nama_pekerjaan;
    }

    public void setNama_pekerjaan(String nama_pekerjaan) {
        this.nama_pekerjaan = nama_pekerjaan;
    }

    public String getSpk_path() {
        return spk_path;
    }

    public void setSpk_path(String spk_path) {
        this.spk_path = spk_path;
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
