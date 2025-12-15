package org.acme.utils.objectPost;

public class EquityData {
    private long modal;
    private long laba_rugi_tahun_berjalan;
    private long laba_ditahan;
    private long ekuitas_lainnya;
    private long total_ekuitas;
    private int tahun;
    
    public long getModal() {
        return modal;
    }
    public void setModal(long modal) {
        this.modal = modal;
    }
    public long getLaba_rugi_tahun_berjalan() {
        return laba_rugi_tahun_berjalan;
    }
    public void setLaba_rugi_tahun_berjalan(long laba_rugi_tahun_berjalan) {
        this.laba_rugi_tahun_berjalan = laba_rugi_tahun_berjalan;
    }
    public long getLaba_ditahan() {
        return laba_ditahan;
    }
    public void setLaba_ditahan(long laba_ditahan) {
        this.laba_ditahan = laba_ditahan;
    }
    public long getEkuitas_lainnya() {
        return ekuitas_lainnya;
    }
    public void setEkuitas_lainnya(long ekuitas_lainnya) {
        this.ekuitas_lainnya = ekuitas_lainnya;
    }
    public long getTotal_ekuitas() {
        return total_ekuitas;
    }
    public void setTotal_ekuitas(long total_ekuitas) {
        this.total_ekuitas = total_ekuitas;
    }
    public int getTahun() {
        return tahun;
    }
    public void setTahun(int tahun) {
        this.tahun = tahun;
    }

    
}
