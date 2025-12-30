package org.acme.controllers.upload_minio.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tdr_struktur_vendor")
public class TdrOrganisasiVendorSktEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    @Column(name = "id_struktur_organisasi")
    private Long id_struktur_organisasi;

    @Column(name= "id_identitas")
    private Long id_identitas;

    @Column(name = "nama")
    private String nama;

    @Column(name = "kode_posisi", length = 3)
    private Integer kode_posisi;

    @Column(name = "posisi", length = 50)
    private String posisi;

    @Column(name = "jabatan")
    private String jabatan;

    @Column(name = "kode_jenis_kelamin", length = 3)
    private Integer kode_jenis_kelamin;

    @Column(name = "jenis_kelamin", length = 15)
    private String jenis_kelamin;

    @Column(name = "kode_kewarganegaraan", length = 3)
    private Integer kode_kewarganegaraan;

    @Column(name = "kewarganegaraan", length = 50)
    private String kewarganegaraan;

    @Column(name = "nik", length = 20)
    private String nik;

    @Column(name = "npwp", length = 50)
    private String npwp;

    @Column(name = "ktp_kitas", length = 50)
    private String ktp_kitas;

    @Column(name = "tgl_lahir")
    private LocalDate tgl_lahir;

    public Long getId_struktur_organisasi() {
        return id_struktur_organisasi;
    }

    public void setId_struktur_organisasi(Long id_struktur_organisasi) {
        this.id_struktur_organisasi = id_struktur_organisasi;
    }

    public Long getId_identitas() {
        return id_identitas;
    }

    public void setId_identitas(Long id_identitas) {
        this.id_identitas = id_identitas;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public Integer getKode_posisi() {
        return kode_posisi;
    }

    public void setKode_posisi(Integer kode_posisi) {
        this.kode_posisi = kode_posisi;
    }

    public String getPosisi() {
        return posisi;
    }

    public void setPosisi(String posisi) {
        this.posisi = posisi;
    }

    public String getJabatan() {
        return jabatan;
    }

    public void setJabatan(String jabatan) {
        this.jabatan = jabatan;
    }

    public Integer getKode_jenis_kelamin() {
        return kode_jenis_kelamin;
    }

    public void setKode_jenis_kelamin(Integer kode_jenis_kelamin) {
        this.kode_jenis_kelamin = kode_jenis_kelamin;
    }

    public String getJenis_kelamin() {
        return jenis_kelamin;
    }

    public void setJenis_kelamin(String jenis_kelamin) {
        this.jenis_kelamin = jenis_kelamin;
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

    public LocalDate getTgl_lahir() {
        return tgl_lahir;
    }

    public void setTgl_lahir(LocalDate tgl_lahir) {
        this.tgl_lahir = tgl_lahir;
    }

    

}
