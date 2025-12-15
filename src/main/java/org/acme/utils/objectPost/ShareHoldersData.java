package org.acme.utils.objectPost;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ShareHoldersData {
    private String tipe;
    private String shareholder_name;
    private String ownership_type;
    private String position;
    private String nationality;
    private String npp;
    private String nik;
    private String npwp;
    private String ktp;
    private String share_amount;
    private long nominal;
    private int ownership_percentage;

    


    public String getTipe() {
        return tipe;
    }
    public void setTipe(String tipe) {
        this.tipe = tipe;
    }
    public String getShareholder_name() {
        return shareholder_name;
    }
    public void setShareholder_name(String shareholder_name) {
        this.shareholder_name = shareholder_name;
    }
    public String getPosition() {
        return position;
    }
    public void setPosition(String position) {
        this.position = position;
    }
    public String getNationality() {
        return nationality;
    }
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
    public String getNpp() {
        return npp;
    }
    public void setNpp(String npp) {
        this.npp = npp;
    }
    public String getNpwp() {
        return npwp;
    }
    public void setNpwp(String npwp) {
        this.npwp = npwp;
    }
    public String getKtp() {
        return ktp;
    }
    public void setKtp(String ktp) {
        this.ktp = ktp;
    }
    public String getShare_amount() {
        return share_amount;
    }
    public void setShare_amount(String share_amount) {
        this.share_amount = share_amount;
    }
    public long getNominal() {
        return nominal;
    }
    public void setNominal(long nominal) {
        this.nominal = nominal;
    }
    public int getOwnership_percentage() {
        return ownership_percentage;
    }
    public void setOwnership_percentage(int ownership_percentage) {
        this.ownership_percentage = ownership_percentage;
    }
    public String getOwnership_type() {
        return ownership_type;
    }
    public void setOwnership_type(String ownership_type) {
        this.ownership_type = ownership_type;
    }
    public String getNik() {
        return nik;
    }
    public void setNik(String nik) {
        this.nik = nik;
    }

    
}
