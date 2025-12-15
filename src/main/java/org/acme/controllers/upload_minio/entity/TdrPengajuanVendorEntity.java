package org.acme.controllers.upload_minio.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.time.LocalDate;

@Entity
@Table(name = "tdr_pengajuan_vendor")
public class TdrPengajuanVendorEntity {
    
    @Id
    @Column(name = "id_pengajuan", length = 15)
    private String id_pengajuan;

    @Column(name = "userid", length = 50)
    private String userid;

    @Column(name = "id_identitas")
    private Long id_identitas;

    @Column(name = "nama_vendor")
    private String nama_vendor;

    @Column(name = "bidang_usaha", columnDefinition = "TEXT")
    private String bidang_usaha;

    @Column(name = "email")
    private String email;

    @Column(name = "ip_insert", length = 50)
    private String ip_insert;

    @Column(name = "tgl_insert")
    private LocalDateTime tgl_insert;

    @Column(name = "tgl_update")
    private LocalDateTime tgl_update;

    @Column(name = "usulan_ke")
    private Integer usulan_ke;
    
    @Column(name = "status_mcs", length = 5)
    private String status_mcs;

    @Column(name = "tahapan_mcs", length = 2)
    private String tahapan_mcs;

    @Column(name = "alasan_tolak", columnDefinition = "TEXT")
    private String alasan_tolak;


    @Column(name = "alasan_dikembalikan_vendor", columnDefinition = "TEXT")
    private String alasan_dikembalikan_vendor;

    @Column(name = "tgl_terima_maker")
    private LocalDate tgl_terima_maker;

    @Column(name = "lkr_path")
    private String lkr_path;


    @Column(name = "maker_rurptrskt", columnDefinition = "TEXT")
    private String maker_rurptrskt;

    @Column(name = "id_rurptrskt")
    private Long id_rurptrskt;

    @Column(name = "skt_path")
    private String skt_path;

    @Column(name = "profile_path")
    private String profile_path;

    @Column(name = "status_realisasi")
    private Integer status_realisasi;

    @Column(name = "id_vendor")
    private Integer id_vendor;

    @Column(name = "job_status", length = 2)
    private String job_status;

    @Column(name = "job_desc")
    private String job_desc;

    @Column(name = "job_last_update")
    private LocalDateTime job_last_update;

    @Column(name = "SAP_BP_NUM")
    private String sap_bp_num;

    @Column(name = "id_vendor_group")
    private String id_vendor_group;

    @Column(name = "id_vendor_eproc")
    private String id_vendor_eproc;

    @Column(name = "status_eproc", length = 6)
    private String status_eproc;

    @Column(name = "is_eproc")
    private Integer is_eproc;

    @Column(name = "message_is_eproc", columnDefinition = "TEXT")
    private String message_is_eproc;
    

    

    public String getId_pengajuan() {
        return id_pengajuan;
    }

    public void setId_pengajuan(String id_pengajuan) {
        this.id_pengajuan = id_pengajuan;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Long getId_identitas() {
        return id_identitas;
    }

    public void setId_identitas(Long id_identitas) {
        this.id_identitas = id_identitas;
    }

    public String getNama_vendor() {
        return nama_vendor;
    }

    public void setNama_vendor(String nama_vendor) {
        this.nama_vendor = nama_vendor;
    }

    public String getBidang_usaha() {
        return bidang_usaha;
    }

    public void setBidang_usaha(String bidang_usaha) {
        this.bidang_usaha = bidang_usaha;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIp_insert() {
        return ip_insert;
    }

    public void setIp_insert(String ip_insert) {
        this.ip_insert = ip_insert;
    }

    public LocalDateTime getTgl_insert() {
        return tgl_insert;
    }

    public void setTgl_insert(LocalDateTime tgl_insert) {
        this.tgl_insert = tgl_insert;
    }

    public LocalDateTime getTgl_update() {
        return tgl_update;
    }

    public void setTgl_update(LocalDateTime tgl_update) {
        this.tgl_update = tgl_update;
    }

    public Integer getUsulan_ke() {
        return usulan_ke;
    }

    public void setUsulan_ke(Integer usulan_ke) {
        this.usulan_ke = usulan_ke;
    }

    public String getStatus_mcs() {
        return status_mcs;
    }

    public void setStatus_mcs(String status_mcs) {
        this.status_mcs = status_mcs;
    }

    public String getTahapan_mcs() {
        return tahapan_mcs;
    }

    public void setTahapan_mcs(String tahapan_mcs) {
        this.tahapan_mcs = tahapan_mcs;
    }

    public String getAlasan_tolak() {
        return alasan_tolak;
    }

    public void setAlasan_tolak(String alasan_tolak) {
        this.alasan_tolak = alasan_tolak;
    }

    public String getAlasan_dikembalikan_vendor() {
        return alasan_dikembalikan_vendor;
    }

    public void setAlasan_dikembalikan_vendor(String alasan_dikembalikan_vendor) {
        this.alasan_dikembalikan_vendor = alasan_dikembalikan_vendor;
    }

    public LocalDate getTgl_terima_maker() {
        return tgl_terima_maker;
    }

    public void setTgl_terima_maker(LocalDate tgl_terima_maker) {
        this.tgl_terima_maker = tgl_terima_maker;
    }

    public String getLkr_path() {
        return lkr_path;
    }

    public void setLkr_path(String lkr_path) {
        this.lkr_path = lkr_path;
    }

    public String getMaker_rurptrskt() {
        return maker_rurptrskt;
    }

    public void setMaker_rurptrskt(String maker_rurptrskt) {
        this.maker_rurptrskt = maker_rurptrskt;
    }

    public Long getId_rurptrskt() {
        return id_rurptrskt;
    }

    public void setId_rurptrskt(Long id_rurptrskt) {
        this.id_rurptrskt = id_rurptrskt;
    }

    public String getSkt_path() {
        return skt_path;
    }

    public void setSkt_path(String skt_path) {
        this.skt_path = skt_path;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public void setProfile_path(String profile_path) {
        this.profile_path = profile_path;
    }

    public Integer getStatus_realisasi() {
        return status_realisasi;
    }

    public void setStatus_realisasi(Integer status_realisasi) {
        this.status_realisasi = status_realisasi;
    }

    public Integer getId_vendor() {
        return id_vendor;
    }

    public void setId_vendor(Integer id_vendor) {
        this.id_vendor = id_vendor;
    }

    public String getJob_status() {
        return job_status;
    }

    public void setJob_status(String job_status) {
        this.job_status = job_status;
    }

    public String getJob_desc() {
        return job_desc;
    }

    public void setJob_desc(String job_desc) {
        this.job_desc = job_desc;
    }

    public LocalDateTime getJob_last_update() {
        return job_last_update;
    }

    public void setJob_last_update(LocalDateTime job_last_update) {
        this.job_last_update = job_last_update;
    }

    public String getSap_bp_num() {
        return sap_bp_num;
    }

    public void setSap_bp_num(String sap_bp_num) {
        this.sap_bp_num = sap_bp_num;
    }

    public String getId_vendor_group() {
        return id_vendor_group;
    }

    public void setId_vendor_group(String id_vendor_group) {
        this.id_vendor_group = id_vendor_group;
    }

    public String getStatus_eproc() {
        return status_eproc;
    }

    public void setStatus_eproc(String status_eproc) {
        this.status_eproc = status_eproc;
    }

    public String getId_vendor_eproc() {
        return id_vendor_eproc;
    }

    public void setId_vendor_eproc(String id_vendor_eproc) {
        this.id_vendor_eproc = id_vendor_eproc;
    }

    public Integer getIs_eproc() {
        return is_eproc;
    }

    public void setIs_eproc(Integer is_eproc) {
        this.is_eproc = is_eproc;
    }

    public String getMessage_is_eproc() {
        return message_is_eproc;
    }

    public void setMessage_is_eproc(String message_is_eproc) {
        this.message_is_eproc = message_is_eproc;
    }

    // public String getNo_skt() {
    //     return no_skt;
    // }

    // public void setNo_skt(String no_skt) {
    //     this.no_skt = no_skt;
    // }


    


}
