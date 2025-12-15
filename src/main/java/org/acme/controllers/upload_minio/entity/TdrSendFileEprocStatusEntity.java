package org.acme.controllers.upload_minio.entity;

import org.acme.controllers.upload_minio.enumeration.StatusEprocEnum;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tdr_send_file_e_proc_status")
public class TdrSendFileEprocStatusEntity extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_e_proc")
    private StatusEprocEnum status_eproc;


    @Column(name = "nama_jobs", length = 100)
    private String nama_jobs;


    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
    }


    public StatusEprocEnum getStatus_eproc() {
        return status_eproc;
    }


    public void setStatus_eproc(StatusEprocEnum status_eproc) {
        this.status_eproc = status_eproc;
    }


    public String getNama_jobs() {
        return nama_jobs;
    }


    public void setNama_jobs(String nama_jobs) {
        this.nama_jobs = nama_jobs;
    }

    
    
}
