package org.acme.controllers.upload_minio.entity;

import java.time.LocalDateTime;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "tdr_upload_minio_log")
public class TdrUploadMinioLogEntity extends PanacheEntityBase {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "id_pengajuan")
    private String idPengajuan;
    
    @Column(name = "id_identitas")
    private Long idIdentitas;
    
    @Column(name = "dok_type")
    private String dokType; // "identitas", "pengalaman", "perbankan"
    
    @Column(name = "dok_number")
    private Integer dokNumber;
    
    @Column(name = "file_name")
    private String fileName;
    
    @Column(name = "path_minio")
    private String pathMinio;
    
    @Column(name = "status")
    private String status; // "SUCCESS", "FAILED", "SKIPPED"
    
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;
    
    @Column(name = "upload_date")
    private LocalDateTime uploadDate;
    
    @Column(name = "status_skt")
    private Integer statusSkt; // 1 = SKT, 0 = Non-SKT

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdPengajuan() {
        return idPengajuan;
    }

    public void setIdPengajuan(String idPengajuan) {
        this.idPengajuan = idPengajuan;
    }

    public Long getIdIdentitas() {
        return idIdentitas;
    }

    public void setIdIdentitas(Long idIdentitas) {
        this.idIdentitas = idIdentitas;
    }

    public String getDokType() {
        return dokType;
    }

    public void setDokType(String dokType) {
        this.dokType = dokType;
    }

    public Integer getDokNumber() {
        return dokNumber;
    }

    public void setDokNumber(Integer dokNumber) {
        this.dokNumber = dokNumber;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPathMinio() {
        return pathMinio;
    }

    public void setPathMinio(String pathMinio) {
        this.pathMinio = pathMinio;
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

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    public Integer getStatusSkt() {
        return statusSkt;
    }

    public void setStatusSkt(Integer statusSkt) {
        this.statusSkt = statusSkt;
    }
}