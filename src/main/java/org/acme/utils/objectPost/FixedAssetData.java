package org.acme.utils.objectPost;

public class FixedAssetData {
    private long asset_tetap;
    private long aktiva_tidak_lancar;
    private long total_aktiva_tidak_lancar;
    private long total_aktiva;
    private int tahun;
    
    public long getAsset_tetap() {
        return asset_tetap;
    }
    public void setAsset_tetap(long asset_tetap) {
        this.asset_tetap = asset_tetap;
    }
    public long getAktiva_tidak_lancar() {
        return aktiva_tidak_lancar;
    }
    public void setAktiva_tidak_lancar(long aktiva_tidak_lancar) {
        this.aktiva_tidak_lancar = aktiva_tidak_lancar;
    }
    public long getTotal_aktiva_tidak_lancar() {
        return total_aktiva_tidak_lancar;
    }
    public void setTotal_aktiva_tidak_lancar(long total_aktiva_tidak_lancar) {
        this.total_aktiva_tidak_lancar = total_aktiva_tidak_lancar;
    }
    public long getTotal_aktiva() {
        return total_aktiva;
    }
    public void setTotal_aktiva(long total_aktiva) {
        this.total_aktiva = total_aktiva;
    }
    public int getTahun() {
        return tahun;
    }
    public void setTahun(int tahun) {
        this.tahun = tahun;
    }


    
}
