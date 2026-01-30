package org.acme.controllers.upload_minio.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "mst_file_legalitas")
public class TdrMstFileLegalitasEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dok")
    private Byte dok;

    @Column(name = "nama_file", length = 225)
    private String nama_file;

    @Column(name = "iproc_type", length = 100)
    private String iproc_type;

    @Column(name = "status_upload_minio")
    private String status_upload_minio;

    @Column(name = "status_skt")
    private String status_skt;

    @Column(name = "status_non_skt")
    private String status_non_skt;

    public String getIproc_type() {
        return iproc_type;
    }

    public void setIproc_type(String iproc_type) {
        this.iproc_type = iproc_type;
    }

    public Byte getDok() {
        return dok;
    }

    public void setDok(Byte dok) {
        this.dok = dok;
    }

    public String getNama_file() {
        return nama_file;
    }

    public void setNama_file(String nama_file) {
        this.nama_file = nama_file;
    }

    public String getStatus_upload_minio() {
        return status_upload_minio;
    }

    public void setStatus_upload_minio(String status_upload_minio) {
        this.status_upload_minio = status_upload_minio;
    }

    public String getStatus_skt() {
        return status_skt;
    }

    public void setStatus_skt(String status_skt) {
        this.status_skt = status_skt;
    }

    public String getStatus_non_skt() {
        return status_non_skt;
    }

    public void setStatus_non_skt(String status_non_skt) {
        this.status_non_skt = status_non_skt;
    }
}
