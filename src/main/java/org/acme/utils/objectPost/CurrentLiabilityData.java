package org.acme.utils.objectPost;

public class CurrentLiabilityData {
    private long pinjaman_jangka_pendek;
    private long hutang_bank;
    private long hutang_usaha;
    private long hutang_pajak;
    private long badan_yang_harus_dibayar;
    private long kewajiban_lancar_lainnya;
    private long total_kewajiban_lancar;
    private int tahun;
    public long getPinjaman_jangka_pendek() {
        return pinjaman_jangka_pendek;
    }
    public void setPinjaman_jangka_pendek(long pinjaman_jangka_pendek) {
        this.pinjaman_jangka_pendek = pinjaman_jangka_pendek;
    }
    public long getHutang_bank() {
        return hutang_bank;
    }
    public void setHutang_bank(long hutang_bank) {
        this.hutang_bank = hutang_bank;
    }
    public long getHutang_usaha() {
        return hutang_usaha;
    }
    public void setHutang_usaha(long hutang_usaha) {
        this.hutang_usaha = hutang_usaha;
    }
    public long getHutang_pajak() {
        return hutang_pajak;
    }
    public void setHutang_pajak(long hutang_pajak) {
        this.hutang_pajak = hutang_pajak;
    }
    public long getBadan_yang_harus_dibayar() {
        return badan_yang_harus_dibayar;
    }
    public void setBadan_yang_harus_dibayar(long badan_yang_harus_dibayar) {
        this.badan_yang_harus_dibayar = badan_yang_harus_dibayar;
    }
    public long getKewajiban_lancar_lainnya() {
        return kewajiban_lancar_lainnya;
    }
    public void setKewajiban_lancar_lainnya(long kewajiban_lancar_lainnya) {
        this.kewajiban_lancar_lainnya = kewajiban_lancar_lainnya;
    }
    public long getTotal_kewajiban_lancar() {
        return total_kewajiban_lancar;
    }
    public void setTotal_kewajiban_lancar(long total_kewajiban_lancar) {
        this.total_kewajiban_lancar = total_kewajiban_lancar;
    }
    public int getTahun() {
        return tahun;
    }
    public void setTahun(int tahun) {
        this.tahun = tahun;
    }

    
}
