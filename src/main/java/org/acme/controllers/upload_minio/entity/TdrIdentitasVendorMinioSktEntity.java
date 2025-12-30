package org.acme.controllers.upload_minio.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "tdr_identitas_vendor_legal_doc_minio")
public class TdrIdentitasVendorMinioSktEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_legal_doc_minio")
    private Long id_legal_doc_minio;

    @Column(name = "id_pengajuan", length = 50)
    private String id_pengajuan;

    @Column(name = "dok")
    private Integer dok;

    @Column(name = "nama_dok", columnDefinition = "TEXT")
    private String nama_dok;

    @Column(name = "path_minio", columnDefinition = "TEXT")
    private String path_minio;

    @Column(name = "is_eproc")
    private Integer is_eproc;

    @Column(name = "status_skt")
    private Integer status_skt;

    @Column(name = "date_upload")
    private LocalDateTime date_upload;

    public Long getId_legal_doc_minio() {
        return id_legal_doc_minio;
    }

    public void setId_legal_doc_minio(Long id_legal_doc_minio) {
        this.id_legal_doc_minio = id_legal_doc_minio;
    }

    public String getId_pengajuan() {
        return id_pengajuan;
    }

    public void setId_pengajuan(String id_pengajuan) {
        this.id_pengajuan = id_pengajuan;
    }

    public Integer getDok() {
        return dok;
    }

    public void setDok(Integer dok) {
        this.dok = dok;
    }

    public String getNama_dok() {
        return nama_dok;
    }

    public void setNama_dok(String nama_dok) {
        this.nama_dok = nama_dok;
    }

    public String getPath_minio() {
        return path_minio;
    }

    public void setPath_minio(String path_minio) {
        this.path_minio = path_minio;
    }

    public Integer getIs_eproc() {
        return is_eproc;
    }

    public void setIs_eproc(Integer is_eproc) {
        this.is_eproc = is_eproc;
    }

    public Integer getStatus_skt() {
        return status_skt;
    }

    public void setStatus_skt(Integer status_skt) {
        this.status_skt = status_skt;
    }

    public LocalDateTime getDate_upload() {
        return date_upload;
    }

    public void setDate_upload(LocalDateTime date_upload) {
        this.date_upload = date_upload;
    }
    
}
