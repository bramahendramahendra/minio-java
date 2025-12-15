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

    


}
