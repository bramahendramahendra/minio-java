package org.acme.controllers.upload_minio.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

import java.time.LocalDateTime;

import java.math.BigDecimal;



@Entity
@Table(name = "tdr_pengalaman_vendor_minio")
public class TdrPengalamanVendorMinioSktEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pengalaman")
    private Long id_pengalaman;

    @Column(name = "id_identitas")
    private Long id_identitas;

    @Column(name = "nama_pekerjaan")
    private String nama_pekerjaan;

    @Column(name = "nama_pemilik_pekerja")
    private String nama_pemilik_pekerja;

    @Column(name = "bidang_usaha", length = 3)
    private String bidang_usaha;

    @Column(name = "sub_bidang_usaha", length = 3)
    private String sub_bidang_usaha;


    @Column(name = "pemberi_tugas")
    private String pemberi_tugas;

    @Column(name = "no_spk")
    private String no_spk;

    @Column(name = "tgl_mulai_spk")
    private LocalDate tgl_mulai_spk;

    @Column(name = "tgl_selesai_spk")
    private LocalDate tgl_selesai_spk;

    @Column(name = "nilai_kontrak", precision = 20, scale = 2)
    private BigDecimal nilai_kontrak;

    @Column(name = "ba_pekerjaan")
    private String ba_pekerjaan;

    @Column(name = "spk_path")
    private String spk_path;

    @Column(name = "minio_path", columnDefinition = "TEXT")
    private String minio_path;

    @Column(name = "upload_date")
    private LocalDateTime upload_minio;

    

   

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

    public String getNama_pemilik_pekerja() {
        return nama_pemilik_pekerja;
    }

    public void setNama_pemilik_pekerja(String nama_pemilik_pekerja) {
        this.nama_pemilik_pekerja = nama_pemilik_pekerja;
    }

    public String getBidang_usaha() {
        return bidang_usaha;
    }

    public void setBidang_usaha(String bidang_usaha) {
        this.bidang_usaha = bidang_usaha;
    }

    public String getSub_bidang_usaha() {
        return sub_bidang_usaha;
    }

    public void setSub_bidang_usaha(String sub_bidang_usaha) {
        this.sub_bidang_usaha = sub_bidang_usaha;
    }

    public String getPemberi_tugas() {
        return pemberi_tugas;
    }

    public void setPemberi_tugas(String pemberi_tugas) {
        this.pemberi_tugas = pemberi_tugas;
    }

    public String getNo_spk() {
        return no_spk;
    }

    public void setNo_spk(String no_spk) {
        this.no_spk = no_spk;
    }

    public LocalDate getTgl_mulai_spk() {
        return tgl_mulai_spk;
    }

    public void setTgl_mulai_spk(LocalDate tgl_mulai_spk) {
        this.tgl_mulai_spk = tgl_mulai_spk;
    }

    public LocalDate getTgl_selesai_spk() {
        return tgl_selesai_spk;
    }

    public void setTgl_selesai_spk(LocalDate tgl_selesai_spk) {
        this.tgl_selesai_spk = tgl_selesai_spk;
    }

    public BigDecimal getNilai_kontrak() {
        return nilai_kontrak;
    }

    public void setNilai_kontrak(BigDecimal nilai_kontrak) {
        this.nilai_kontrak = nilai_kontrak;
    }

    public String getBa_pekerjaan() {
        return ba_pekerjaan;
    }

    public void setBa_pekerjaan(String ba_pekerjaan) {
        this.ba_pekerjaan = ba_pekerjaan;
    }

    public String getSpk_path() {
        return spk_path;
    }

    public void setSpk_path(String spk_path) {
        this.spk_path = spk_path;
    }

}
