package org.acme.utils.objectPost;

public class CurrentAssetData {
    private long kas_dan_bank;
    private long piutang_usaha;
    private long persediaan;
    private long pajak_dibayar_muka;
    private long biaya_dibayar_muka;
    private long aktiva_lancar_lainnya;
    private long total_aktiva_lancar;
    private int tahun;
    public long getKas_dan_bank() {
        return kas_dan_bank;
    }
    public void setKas_dan_bank(long kas_dan_bank) {
        this.kas_dan_bank = kas_dan_bank;
    }
    public long getPiutang_usaha() {
        return piutang_usaha;
    }
    public void setPiutang_usaha(long piutang_usaha) {
        this.piutang_usaha = piutang_usaha;
    }
    public long getPersediaan() {
        return persediaan;
    }
    public void setPersediaan(long persediaan) {
        this.persediaan = persediaan;
    }
    public long getPajak_dibayar_muka() {
        return pajak_dibayar_muka;
    }
    public void setPajak_dibayar_muka(long pajak_dibayar_muka) {
        this.pajak_dibayar_muka = pajak_dibayar_muka;
    }
    public long getBiaya_dibayar_muka() {
        return biaya_dibayar_muka;
    }
    public void setBiaya_dibayar_muka(long biaya_dibayar_muka) {
        this.biaya_dibayar_muka = biaya_dibayar_muka;
    }
    public long getAktiva_lancar_lainnya() {
        return aktiva_lancar_lainnya;
    }
    public void setAktiva_lancar_lainnya(long aktiva_lancar_lainnya) {
        this.aktiva_lancar_lainnya = aktiva_lancar_lainnya;
    }
    public long getTotal_aktiva_lancar() {
        return total_aktiva_lancar;
    }
    public void setTotal_aktiva_lancar(long total_aktiva_lancar) {
        this.total_aktiva_lancar = total_aktiva_lancar;
    }
    public int getTahun() {
        return tahun;
    }
    public void setTahun(int tahun) {
        this.tahun = tahun;
    }

    
}
