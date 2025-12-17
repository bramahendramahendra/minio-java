package org.acme.controllers.upload_minio.entity;




import java.time.LocalDate;

import java.time.LocalDateTime;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tdr_identitas_vendor")
public class TdrIdentitasVendorEntity extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_identitas")
    private Long id_identitas;

    @Column(name = "userid", length = 50)
    private String userid;

    @Column(name = "nama_vendor")
    private String nama_vendor;

    @Column(name = "kode_bentuk_usaha")
    private Integer kode_bentuk_usaha; 

    @Column(name = "nama_bentuk_usaha")
    private String nama_bentuk_usaha;

    @Column(name = "kode_badan_usaha", length = 3)
    private Integer kode_badan_usaha;

    @Column(name = "nama_badan_usaha", length = 25)
    private String nama_badan_usaha;

    @Column(name = "kode_kegiatan_usaha", length = 3)
    private Integer kode_kegiatan_usaha;


    @Column(name = "nama_kegiatan_usaha", length = 20)
    private String nama_kegiatan_usaha;

    @Column(name = "kode_jenis_kegiatan_usaha", length = 4)
    private Integer kode_jenis_kegiatan_usaha;

    @Column(name = "nama_jenis_kegiatan_usaha")
    private String nama_jenis_kegiatan_usaha;

    @Column(name = "bidang_usaha", columnDefinition = "TEXT")
    private String bidang_usaha;

    @Column(name = "sub_bidang_usaha", columnDefinition = "TEXT")
    private String sub_bidang_usaha;


    @Column(name = "material", columnDefinition = "TEXT")
    private String material;

    @Column(name = "kualifikasi", columnDefinition = "TEXT")
    private String kualifikasi;

    @Column(name = "kode_negara", length = 3)
    private String kode_negara;

    @Column(name = "nama_negara")
    private String nama_negara;

    @Column(name = "kode_provinsi", length = 3)
    private String kode_provinsi;

    @Column(name = "nama_provinsi")
    private String nama_provinsi;


    @Column(name = "kota")
    private String kota;
    
    @Column(name = "alamat")
    private String alamat;

    @Column(name = "blok_nomor_kavling")
    private String blok_nomor_kavling;

    @Column(name = "longitude")
    private String longitude;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "kodepos", length = 6)
    private String kodepos;

    @Column(name = "no_telp", length = 50)
    private String no_telp;

    @Column(name = "no_facs", length = 50)
    private String no_facs;

    @Column(name = "website")
    private String website;

    @Column(name = "kode_mata_uang", length = 10)
    private String kode_mata_uang;

    @Column(name = "nama_mata_uang")
    private String nama_mata_uang;

    @Column(name = "nama_dirut")
    private String nama_dirut;

    @Column(name = "nama_pic")
    private String nama_pic;

    @Column(name = "no_telp_pic", length = 50)
    private String no_telp_pic;

    @Column(name = "email")
    private String email;

    @Column(name = "dok_legalitas_path", columnDefinition = "TEXT")
    private String dok_legalitas_path;

    @Column(name = "lap_keu", columnDefinition = "TEXT")
    private String lap_keu;

    @Column(name = "nomor_akta_pendirian")
    private String nomor_akta_pendirian;

    @Column(name = "nomor_akta_pengurus")
    private String nomor_akta_pengurus;

    @Column(name = "nomor_akta_pemilik")
    private String nomor_akta_pemilik;

    @Column(name = "nomor_tanda_daftar")
    private String nomor_tanda_daftar;

    @Column(name = "nomor_siup")
    private String nomor_siup;

    @Column(name = "nomor_skdp")
    private String nomor_skdp;

    @Column(name = "nomor_ijin")
    private String nomor_ijin;

    @Column(name = "nomor_npwp")
    private String nomor_npwp;


    @Column(name = "nomor_pkp")
    private String nomor_pkp;

    @Column(name = "nomor_pengalaman")
    private String nomor_pengalaman;

    @Column(name = "tgl_akta_pendirian")
    private LocalDate tgl_akta_pendirian;


    @Column(name = "tgl_akta_pengurus")
    private LocalDate tgl_akta_pengurus;

    @Column(name = "tgl_akta_pemilik")
    private LocalDate tgl_akta_pemilik;

    @Column(name = "tgl_tanda_daftar")
    private LocalDate tgl_tanda_daftar;

    @Column(name = "tgl_siup")
    private LocalDate tgl_siup;

    @Column(name = "tgl_skdp")
    private LocalDate tgl_skdp;

    @Column(name = "tgl_ijin")
    private LocalDate tgl_ijin;

    @Column(name = "tgl_npwp")
    private LocalDate tgl_npwp;


    @Column(name = "tgl_pkp")
    private LocalDate tgl_pkp;

    @Column(name = "tgl_pengalaman")
    private LocalDate tgl_pengalaman;

    @Column(name = "tgl_terbit_akta_pendirian")
    private LocalDate tgl_terbit_akta_pendirian;

    @Column(name = "tgl_terbit_akta_pengurus")
    private LocalDate tgl_terbit_akta_pengurus;

    @Column(name = "tgl_terbit_akta_pemilik")
    private LocalDate tgl_terbit_akta_pemilik;

    @Column(name = "tgl_terbit_tanda_daftar")
    private LocalDate tgl_terbit_tanda_daftar;

    @Column(name = "tgl_terbit_siup")
    private LocalDate tgl_terbit_siup;

    @Column(name = "tgl_terbit_skdp")
    private LocalDate tgl_terbit_skdp;

    @Column(name = "tgl_terbit_ijin")
    private LocalDate tgl_terbit_ijin;

    @Column(name = "tgl_terbit_npwp")
    private LocalDate tgl_terbit_npwp;

    @Column(name = "tgl_terbit_pkp")
    private LocalDate tgl_terbit_pkp;

    @Column(name = "tgl_terbit_pengalaman")
    private LocalDate tgl_terbit_pengalaman;

    @Column(name = "ip_insert", length = 50)
    private String ip_insert;

    @Column(name = "tgl_insert")
    private LocalDateTime tgl_insert;

    @Column(name = "is_minio_identitas_vendor")
    private Integer is_minio_identitas_vendor;

    public Integer getIs_minio_identitas_vendor() {
        return is_minio_identitas_vendor;
    }

    public void setIs_minio_identitas_vendor(Integer is_minio_identitas_vendor) {
        this.is_minio_identitas_vendor = is_minio_identitas_vendor;
    }

    public Long getId_identitas() {
        return id_identitas;
    }

    public void setId_identitas(Long id_identitas) {
        this.id_identitas = id_identitas;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getNama_vendor() {
        return nama_vendor;
    }

    public void setNama_vendor(String nama_vendor) {
        this.nama_vendor = nama_vendor;
    }

    public Integer getKode_bentuk_usaha() {
        return kode_bentuk_usaha;
    }

    public void setKode_bentuk_usaha(Integer kode_bentuk_usaha) {
        this.kode_bentuk_usaha = kode_bentuk_usaha;
    }

    public String getNama_bentuk_usaha() {
        return nama_bentuk_usaha;
    }

    public void setNama_bentuk_usaha(String nama_bentuk_usaha) {
        this.nama_bentuk_usaha = nama_bentuk_usaha;
    }

    public Integer getKode_badan_usaha() {
        return kode_badan_usaha;
    }

    public void setKode_badan_usaha(Integer kode_badan_usaha) {
        this.kode_badan_usaha = kode_badan_usaha;
    }

    public String getNama_badan_usaha() {
        return nama_badan_usaha;
    }

    public void setNama_badan_usaha(String nama_badan_usaha) {
        this.nama_badan_usaha = nama_badan_usaha;
    }

    public Integer getKode_kegiatan_usaha() {
        return kode_kegiatan_usaha;
    }

    public void setKode_kegiatan_usaha(Integer kode_kegiatan_usaha) {
        this.kode_kegiatan_usaha = kode_kegiatan_usaha;
    }

    public String getNama_kegiatan_usaha() {
        return nama_kegiatan_usaha;
    }

    public void setNama_kegiatan_usaha(String nama_kegiatan_usaha) {
        this.nama_kegiatan_usaha = nama_kegiatan_usaha;
    }

    public Integer getKode_jenis_kegiatan_usaha() {
        return kode_jenis_kegiatan_usaha;
    }

    public void setKode_jenis_kegiatan_usaha(Integer kode_jenis_kegiatan_usaha) {
        this.kode_jenis_kegiatan_usaha = kode_jenis_kegiatan_usaha;
    }

    public String getNama_jenis_kegiatan_usaha() {
        return nama_jenis_kegiatan_usaha;
    }

    public void setNama_jenis_kegiatan_usaha(String nama_jenis_kegiatan_usaha) {
        this.nama_jenis_kegiatan_usaha = nama_jenis_kegiatan_usaha;
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

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getKualifikasi() {
        return kualifikasi;
    }

    public void setKualifikasi(String kualifikasi) {
        this.kualifikasi = kualifikasi;
    }

    public String getKode_negara() {
        return kode_negara;
    }

    public void setKode_negara(String kode_negara) {
        this.kode_negara = kode_negara;
    }

    public String getNama_negara() {
        return nama_negara;
    }

    public void setNama_negara(String nama_negara) {
        this.nama_negara = nama_negara;
    }

    public String getKode_provinsi() {
        return kode_provinsi;
    }

    public void setKode_provinsi(String kode_provinsi) {
        this.kode_provinsi = kode_provinsi;
    }

    public String getNama_provinsi() {
        return nama_provinsi;
    }

    public void setNama_provinsi(String nama_provinsi) {
        this.nama_provinsi = nama_provinsi;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getBlok_nomor_kavling() {
        return blok_nomor_kavling;
    }

    public void setBlok_nomor_kavling(String blok_nomor_kavling) {
        this.blok_nomor_kavling = blok_nomor_kavling;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getKodepos() {
        return kodepos;
    }

    public void setKodepos(String kodepos) {
        this.kodepos = kodepos;
    }

    public String getNo_telp() {
        return no_telp;
    }

    public void setNo_telp(String no_telp) {
        this.no_telp = no_telp;
    }

    public String getNo_facs() {
        return no_facs;
    }

    public void setNo_facs(String no_facs) {
        this.no_facs = no_facs;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getKode_mata_uang() {
        return kode_mata_uang;
    }

    public void setKode_mata_uang(String kode_mata_uang) {
        this.kode_mata_uang = kode_mata_uang;
    }

    public String getNama_mata_uang() {
        return nama_mata_uang;
    }

    public void setNama_mata_uang(String nama_mata_uang) {
        this.nama_mata_uang = nama_mata_uang;
    }

    public String getNama_dirut() {
        return nama_dirut;
    }

    public void setNama_dirut(String nama_dirut) {
        this.nama_dirut = nama_dirut;
    }

    public String getNama_pic() {
        return nama_pic;
    }

    public void setNama_pic(String nama_pic) {
        this.nama_pic = nama_pic;
    }

    public String getNo_telp_pic() {
        return no_telp_pic;
    }

    public void setNo_telp_pic(String no_telp_pic) {
        this.no_telp_pic = no_telp_pic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDok_legalitas_path() {
        return dok_legalitas_path;
    }

    public void setDok_legalitas_path(String dok_legalitas_path) {
        this.dok_legalitas_path = dok_legalitas_path;
    }

    public String getLap_keu() {
        return lap_keu;
    }

    public void setLap_keu(String lap_keu) {
        this.lap_keu = lap_keu;
    }

    public String getNomor_akta_pendirian() {
        return nomor_akta_pendirian;
    }

    public void setNomor_akta_pendirian(String nomor_akta_pendirian) {
        this.nomor_akta_pendirian = nomor_akta_pendirian;
    }

    public String getNomor_akta_pengurus() {
        return nomor_akta_pengurus;
    }

    public void setNomor_akta_pengurus(String nomor_akta_pengurus) {
        this.nomor_akta_pengurus = nomor_akta_pengurus;
    }

    public String getNomor_akta_pemilik() {
        return nomor_akta_pemilik;
    }

    public void setNomor_akta_pemilik(String nomor_akta_pemilik) {
        this.nomor_akta_pemilik = nomor_akta_pemilik;
    }

    public String getNomor_tanda_daftar() {
        return nomor_tanda_daftar;
    }

    public void setNomor_tanda_daftar(String nomor_tanda_daftar) {
        this.nomor_tanda_daftar = nomor_tanda_daftar;
    }

    public String getNomor_siup() {
        return nomor_siup;
    }

    public void setNomor_siup(String nomor_siup) {
        this.nomor_siup = nomor_siup;
    }

    public String getNomor_skdp() {
        return nomor_skdp;
    }

    public void setNomor_skdp(String nomor_skdp) {
        this.nomor_skdp = nomor_skdp;
    }

    public String getNomor_ijin() {
        return nomor_ijin;
    }

    public void setNomor_ijin(String nomor_ijin) {
        this.nomor_ijin = nomor_ijin;
    }

    public String getNomor_npwp() {
        return nomor_npwp;
    }

    public void setNomor_npwp(String nomor_npwp) {
        this.nomor_npwp = nomor_npwp;
    }

    public String getNomor_pkp() {
        return nomor_pkp;
    }

    public void setNomor_pkp(String nomor_pkp) {
        this.nomor_pkp = nomor_pkp;
    }

    public String getNomor_pengalaman() {
        return nomor_pengalaman;
    }

    public void setNomor_pengalaman(String nomor_pengalaman) {
        this.nomor_pengalaman = nomor_pengalaman;
    }

    public LocalDate getTgl_akta_pendirian() {
        return tgl_akta_pendirian;
    }

    public void setTgl_akta_pendirian(LocalDate tgl_akta_pendirian) {
        this.tgl_akta_pendirian = tgl_akta_pendirian;
    }

    public LocalDate getTgl_akta_pengurus() {
        return tgl_akta_pengurus;
    }

    public void setTgl_akta_pengurus(LocalDate tgl_akta_pengurus) {
        this.tgl_akta_pengurus = tgl_akta_pengurus;
    }

    public LocalDate getTgl_akta_pemilik() {
        return tgl_akta_pemilik;
    }

    public void setTgl_akta_pemilik(LocalDate tgl_akta_pemilik) {
        this.tgl_akta_pemilik = tgl_akta_pemilik;
    }

    public LocalDate getTgl_tanda_daftar() {
        return tgl_tanda_daftar;
    }

    public void setTgl_tanda_daftar(LocalDate tgl_tanda_daftar) {
        this.tgl_tanda_daftar = tgl_tanda_daftar;
    }

    public LocalDate getTgl_siup() {
        return tgl_siup;
    }

    public void setTgl_siup(LocalDate tgl_siup) {
        this.tgl_siup = tgl_siup;
    }

    public LocalDate getTgl_skdp() {
        return tgl_skdp;
    }

    public void setTgl_skdp(LocalDate tgl_skdp) {
        this.tgl_skdp = tgl_skdp;
    }

    public LocalDate getTgl_ijin() {
        return tgl_ijin;
    }

    public void setTgl_ijin(LocalDate tgl_ijin) {
        this.tgl_ijin = tgl_ijin;
    }

    public LocalDate getTgl_npwp() {
        return tgl_npwp;
    }

    public void setTgl_npwp(LocalDate tgl_npwp) {
        this.tgl_npwp = tgl_npwp;
    }

    public LocalDate getTgl_pkp() {
        return tgl_pkp;
    }

    public void setTgl_pkp(LocalDate tgl_pkp) {
        this.tgl_pkp = tgl_pkp;
    }

    public LocalDate getTgl_pengalaman() {
        return tgl_pengalaman;
    }

    public void setTgl_pengalaman(LocalDate tgl_pengalaman) {
        this.tgl_pengalaman = tgl_pengalaman;
    }

    public LocalDate getTgl_terbit_akta_pendirian() {
        return tgl_terbit_akta_pendirian;
    }

    public void setTgl_terbit_akta_pendirian(LocalDate tgl_terbit_akta_pendirian) {
        this.tgl_terbit_akta_pendirian = tgl_terbit_akta_pendirian;
    }

    public LocalDate getTgl_terbit_akta_pengurus() {
        return tgl_terbit_akta_pengurus;
    }

    public void setTgl_terbit_akta_pengurus(LocalDate tgl_terbit_akta_pengurus) {
        this.tgl_terbit_akta_pengurus = tgl_terbit_akta_pengurus;
    }

    public LocalDate getTgl_terbit_akta_pemilik() {
        return tgl_terbit_akta_pemilik;
    }

    public void setTgl_terbit_akta_pemilik(LocalDate tgl_terbit_akta_pemilik) {
        this.tgl_terbit_akta_pemilik = tgl_terbit_akta_pemilik;
    }

    public LocalDate getTgl_terbit_tanda_daftar() {
        return tgl_terbit_tanda_daftar;
    }

    public void setTgl_terbit_tanda_daftar(LocalDate tgl_terbit_tanda_daftar) {
        this.tgl_terbit_tanda_daftar = tgl_terbit_tanda_daftar;
    }

    public LocalDate getTgl_terbit_siup() {
        return tgl_terbit_siup;
    }

    public void setTgl_terbit_siup(LocalDate tgl_terbit_siup) {
        this.tgl_terbit_siup = tgl_terbit_siup;
    }

    public LocalDate getTgl_terbit_skdp() {
        return tgl_terbit_skdp;
    }

    public void setTgl_terbit_skdp(LocalDate tgl_terbit_skdp) {
        this.tgl_terbit_skdp = tgl_terbit_skdp;
    }

    public LocalDate getTgl_terbit_ijin() {
        return tgl_terbit_ijin;
    }

    public void setTgl_terbit_ijin(LocalDate tgl_terbit_ijin) {
        this.tgl_terbit_ijin = tgl_terbit_ijin;
    }

    public LocalDate getTgl_terbit_npwp() {
        return tgl_terbit_npwp;
    }

    public void setTgl_terbit_npwp(LocalDate tgl_terbit_npwp) {
        this.tgl_terbit_npwp = tgl_terbit_npwp;
    }

    public LocalDate getTgl_terbit_pkp() {
        return tgl_terbit_pkp;
    }

    public void setTgl_terbit_pkp(LocalDate tgl_terbit_pkp) {
        this.tgl_terbit_pkp = tgl_terbit_pkp;
    }

    public LocalDate getTgl_terbit_pengalaman() {
        return tgl_terbit_pengalaman;
    }

    public void setTgl_terbit_pengalaman(LocalDate tgl_terbit_pengalaman) {
        this.tgl_terbit_pengalaman = tgl_terbit_pengalaman;
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

}
