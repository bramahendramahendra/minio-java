package org.acme.utils.objectPost;

import java.util.List;

public class BusinessFieldData {
    private String bidang_usaha_kode;
    private String bidang_usaha;
    private String sub_bidang_usaha_kode;
    private String sub_bidang_usaha;
    private int kualifikasi_id;
    private String kualifikasi;
    private List<MaterialTypeData> jenis_material;
    public String getBidang_usaha_kode() {
        return bidang_usaha_kode;
    }
    public void setBidang_usaha_kode(String bidang_usaha_kode) {
        this.bidang_usaha_kode = bidang_usaha_kode;
    }
    public String getBidang_usaha() {
        return bidang_usaha;
    }
    public void setBidang_usaha(String bidang_usaha) {
        this.bidang_usaha = bidang_usaha;
    }
    public String getSub_bidang_usaha_kode() {
        return sub_bidang_usaha_kode;
    }
    public void setSub_bidang_usaha_kode(String sub_bidang_usaha_kode) {
        this.sub_bidang_usaha_kode = sub_bidang_usaha_kode;
    }
    public String getSub_bidang_usaha() {
        return sub_bidang_usaha;
    }
    public void setSub_bidang_usaha(String sub_bidang_usaha) {
        this.sub_bidang_usaha = sub_bidang_usaha;
    }
    public int getKualifikasi_id() {
        return kualifikasi_id;
    }
    public void setKualifikasi_id(int kualifikasi_id) {
        this.kualifikasi_id = kualifikasi_id;
    }
    public String getKualifikasi() {
        return kualifikasi;
    }
    public void setKualifikasi(String kualifikasi) {
        this.kualifikasi = kualifikasi;
    }
    public List<MaterialTypeData> getJenis_material() {
        return jenis_material;
    }
    public void setJenis_material(List<MaterialTypeData> jenis_material) {
        this.jenis_material = jenis_material;
    }

    
}
