package org.acme.scheduler;

import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.acme.controllers.upload_minio.repository.TdrPengajuanVendorSktRepository;
import org.acme.controllers.upload_minio.repository.TdrPengajuanVendorNonSktRepository;
import org.acme.controllers.upload_minio.repository.TdrIdentitasVendorSktRepository;
import org.acme.controllers.upload_minio.repository.TdrIdentitasVendorNonSktRepository;
import org.acme.controllers.upload_minio.repository.TdrPengalamanVendorSktRepository;
import org.acme.controllers.upload_minio.repository.TdrPerbankanVendorSktRepository;
import org.acme.controllers.upload_minio.repository.TdrPerbankanVendorNonSktRepository;
import org.acme.controllers.upload_minio.repository.TdrOrganisasiVendorSktRepository;
import org.acme.controllers.upload_minio.repository.TdrOrganisasiVendorNonSktRepository;
import org.acme.controllers.upload_minio.repository.TdrPemegangSahamVendorSktRepository;
import org.acme.controllers.upload_minio.repository.TdrJumlahSdmVendorSktRepository;
import org.acme.controllers.upload_minio.repository.TdrJumlahSdmVendorNonSktRepository;
import org.acme.controllers.upload_minio.repository.TdrReviewVendorSktRepository;
import org.acme.controllers.upload_minio.repository.TdrReviewVendorNonSktRepository;

import org.acme.controllers.upload_minio.repository.TdrIdentitasVendorMinioSktRepository;
import org.acme.controllers.upload_minio.repository.TdrIdentitasVendorMinioNonSktRepository;
import org.acme.controllers.upload_minio.repository.TdrPengalamanVendorMinioSktRepository;
import org.acme.controllers.upload_minio.repository.TdrPerbankanVendorMinioSktRepository;
import org.acme.controllers.upload_minio.repository.TdrPerbankanVendorMinioNonSktRepository;

import org.acme.controllers.upload_minio.repository.TdrMstBidangUsahaRepository;
import org.acme.controllers.upload_minio.repository.TdrSubBidangUsahaRepository;
import org.acme.controllers.upload_minio.repository.TdrMaterialVendorRepository;

import org.acme.controllers.upload_minio.repository.TdrMstFileLegalitasRepository;

import org.acme.utils.httpRequest;
import org.acme.utils.responseConsume;
import org.acme.utils.objectPost.BanksData;
import org.acme.utils.objectPost.BusinessFieldData;
import org.acme.utils.objectPost.CurrentAssetData;
import org.acme.utils.objectPost.CurrentLiabilityData;
import org.acme.utils.objectPost.EquityData;
import org.acme.utils.objectPost.ExperienceData;
import org.acme.utils.objectPost.FixedAssetData;
import org.acme.utils.objectPost.HumanResourcesData;
import org.acme.utils.objectPost.LegalDocData;
import org.acme.utils.objectPost.LiabilitiesAndEquityData;
import org.acme.utils.objectPost.MainData;
import org.acme.utils.objectPost.MaterialTypeData;
import org.acme.utils.objectPost.NoCurrentLiabilityData;
import org.acme.utils.objectPost.OrganizationData;
import org.acme.utils.objectPost.ProfitData;
import org.acme.utils.objectPost.Registration;
import org.acme.utils.objectPost.RegistrationData;
import org.acme.utils.objectPost.ReviewsData;
import org.acme.utils.objectPost.ShareHoldersData;
import org.eclipse.microprofile.config.ConfigProvider;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.logging.Log;
import io.quarkus.panache.common.Parameters;
import io.quarkus.scheduler.Scheduled;
import io.smallrye.common.annotation.NonBlocking;

import org.acme.controllers.upload_minio.entity.TdrLogEprocEntity;
import org.acme.controllers.upload_minio.dto.LapkeuDto;

import org.acme.controllers.upload_minio.entity.TdrPengajuanVendorSktEntity;
import org.acme.controllers.upload_minio.entity.TdrPengajuanVendorNonSktEntity;
import org.acme.controllers.upload_minio.entity.TdrIdentitasVendorSktEntity;
import org.acme.controllers.upload_minio.entity.TdrIdentitasVendorNonSktEntity;
import org.acme.controllers.upload_minio.entity.TdrPengalamanVendorSktEntity;
import org.acme.controllers.upload_minio.entity.TdrPerbankanVendorSktEntity;
import org.acme.controllers.upload_minio.entity.TdrPerbankanVendorNonSktEntity;
import org.acme.controllers.upload_minio.entity.TdrPemegangSahamVendorSktEntity;
import org.acme.controllers.upload_minio.entity.TdrOrganisasiVendorSktEntity;
import org.acme.controllers.upload_minio.entity.TdrOrganisasiVendorNonSktEntity;
import org.acme.controllers.upload_minio.entity.TdrReviewVendorSktEntity;
import org.acme.controllers.upload_minio.entity.TdrReviewVendorNonSktEntity;
import org.acme.controllers.upload_minio.entity.TdrJumlahSdmVendorSktEntity;
import org.acme.controllers.upload_minio.entity.TdrJumlahSdmVendorNonSktEntity;

import org.acme.controllers.upload_minio.entity.TdrIdentitasVendorMinioSktEntity;
import org.acme.controllers.upload_minio.entity.TdrIdentitasVendorMinioNonSktEntity;
import org.acme.controllers.upload_minio.entity.TdrPengalamanVendorMinioSktEntity;
import org.acme.controllers.upload_minio.entity.TdrPerbankanVendorMinioSktEntity;
import org.acme.controllers.upload_minio.entity.TdrPerbankanVendorMinioNonSktEntity;

import org.acme.controllers.upload_minio.entity.TdrMstBidangUsahaEntity;
import org.acme.controllers.upload_minio.entity.TdrSubBidangUsahaEntity;
import org.acme.controllers.upload_minio.entity.TdrMaterialVendorEntity;

import org.acme.controllers.upload_minio.entity.TdrMstFileLegalitasEntity;
import org.acme.controllers.upload_minio.entity.TdrSendFileEprocStatusEntity;
import org.acme.controllers.upload_minio.enumeration.StatusEprocEnum;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;

@ApplicationScoped
public class IprocScheduled {
    @Inject TdrPengajuanVendorSktRepository tdrPengajuanVendorSkt;
    @Inject TdrPengajuanVendorNonSktRepository tdrPengajuanVendorNonSkt;
    
    @Inject TdrIdentitasVendorSktRepository tdrIdentitasVendorSkt;
    @Inject TdrIdentitasVendorNonSktRepository tdrIdentitasVendorNonSkt;

    @Inject TdrPengalamanVendorSktRepository tdrPengalamanVendorSkt;

    @Inject TdrPerbankanVendorSktRepository tdrPerbankanVendorSkt;
    @Inject TdrPerbankanVendorNonSktRepository tdrPerbankanVendorNonSkt;

    @Inject TdrOrganisasiVendorSktRepository tdrOrganisasiVendorSkt;
    @Inject TdrOrganisasiVendorNonSktRepository tdrOrganisasiVendorNonSkt;
    
    @Inject TdrPemegangSahamVendorSktRepository tdrPemegangSahamVendorSkt;
    
    @Inject TdrJumlahSdmVendorSktRepository tdrJumlahSdmVendorSkt;
    @Inject TdrJumlahSdmVendorNonSktRepository tdrJumlahSdmVendorNonSkt;
    
    @Inject TdrReviewVendorSktRepository tdrReviewVendorSkt;
    @Inject TdrReviewVendorNonSktRepository tdrReviewVendorNonSkt;

    @Inject TdrIdentitasVendorMinioSktRepository tdrIdentitasVendorMinioSkt;
    @Inject TdrIdentitasVendorMinioNonSktRepository tdrIdentitasVendorMinioNonSkt;
    
    @Inject TdrPengalamanVendorMinioSktRepository tdrPengalamanVendorMinioSkt;

    @Inject TdrPerbankanVendorMinioSktRepository tdrPerbankanVendorMinioSkt;
    @Inject TdrPerbankanVendorMinioNonSktRepository tdrPerbankanVendorMinioNonSkt;


    @Inject TdrMstBidangUsahaRepository tdrBidangUsaha;
    @Inject TdrSubBidangUsahaRepository tdrSubBidangUsaha;
    @Inject TdrMaterialVendorRepository tdrMaterialVendor;

    @Inject TdrMstFileLegalitasRepository tdrMstFile;

    @Inject UploadScheduled uploadScheduled;

    // private final ObjectMapper objectMapper = new ObjectMapper();
    private final ObjectMapper objectMapper;
    
    private static final Logger LOG = Logger.getLogger(IprocScheduled.class);

    // Constructor untuk init ObjectMapper dengan JavaTimeModule
    public IprocScheduled() {
        this.objectMapper = new ObjectMapper();
        // Register module untuk Java 8 Date/Time
        this.objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        // Disable write dates as timestamps (agar lebih readable)
        this.objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    // @Scheduled(every = "60s")
    @Transactional
    public void send_tdr_skt(){
        send_tdr_iproc_skt(null);
    }

    /**
     * Method untuk upload berdasarkan id_pengajuan tertentu
     */
    @Transactional
    public void send_tdr_skt_by_id_pengajuan(String idPengajuan){
        send_tdr_iproc_skt(idPengajuan);
    }
   
    // @Scheduled(every = "60s")
    @Transactional
    public void send_tdr_iproc_skt(String specificIdPengajuan){
        String mainJobsApps = ConfigProvider.getConfig().getValue("name.jobs", String.class);

        LOG.info("==============================================");
        LOG.info("========== SCHEDULER SEND SKT START ==========");
        LOG.info("==============================================");
        
        TdrSendFileEprocStatusEntity tdrEprocStatus = TdrSendFileEprocStatusEntity.find("nama_jobs = ?1", mainJobsApps).firstResult();
        varDump("TdrSendFileEprocStatusEntity", tdrEprocStatus);
        LOG.info("[STATUS] Scheduler iproc skt = " + (tdrEprocStatus != null ? tdrEprocStatus.getStatus_eproc() : "NULL"));
        
        if (tdrEprocStatus == null || !tdrEprocStatus.getStatus_eproc().equals(StatusEprocEnum.ON)) {
            LOG.info("[STATUS] Scheduler is OFF. Skipping execution.");
            return;
        }
        LOG.info("[STATUS] Scheduler iproc skt = " + tdrEprocStatus.getStatus_eproc());

        try {
            LOG.info("Send Data Eproc SKT begin" + (specificIdPengajuan != null ? " for ID: " + specificIdPengajuan : ""));

            List<TdrPengajuanVendorSktEntity> tdrPengajuanVendorSktList;
             if(specificIdPengajuan != null && !specificIdPengajuan.isEmpty()) {
                // Upload berdasarkan id_pengajuan tertentu
                tdrPengajuanVendorSktList = tdrPengajuanVendorSkt.find("where is_eproc is null AND status_eproc = ?1 AND id_pengajuan = ?2 ORDER BY id_pengajuan DESC", "SCH00", specificIdPengajuan).list();
            } else {
                // Upload scheduled (semua data)
                tdrPengajuanVendorSktList = tdrPengajuanVendorSkt.find("where is_eproc is null AND status_eproc = ?1 ORDER BY id_pengajuan DESC", "SCH00").page(0, 5).list();
            }

            // List<TdrPengajuanVendorEntity> tdrPengajuanList = TdrPengajuanVendorEntity.find("where is_eproc is null AND status_eproc = ?1 ORDER BY id_pengajuan DESC", "SCH00").page(0, 5).list();
            LOG.info("[DATA] Total Pengajuan vendors SKT found: " + tdrPengajuanVendorSktList.size());
            if (tdrPengajuanVendorSktList.isEmpty()) {
                LOG.info("[INFO] No pending vendors to process.");
                LOG.info("Send Data Eproc SKT Finished");
                return;
            }

            int dumpLimit = Math.min(5, tdrPengajuanVendorSktList.size());
            for (int i = 0; i < dumpLimit; i++) {
                varDump("Vendor SKT [" + i + "]", tdrPengajuanVendorSktList.get(i));
            }

            for (TdrPengajuanVendorSktEntity vTdrPengajuanVendorSkt : tdrPengajuanVendorSktList) {
                if (vTdrPengajuanVendorSkt == null || vTdrPengajuanVendorSkt.getId_identitas() == null || vTdrPengajuanVendorSkt.getId_identitas() == 0) {
                    LOG.info("[SKIP] Id Identitas Not Exists for pengajuan: " + (vTdrPengajuanVendorSkt != null ? vTdrPengajuanVendorSkt.getId_pengajuan() : "NULL"));
                    continue;
                }

                try {
                    // TdrIdentitasVendorEntity tdrIdentitas = TdrIdentitasVendorEntity.find("where is_minio_identitas_vendor = ?1 AND id_identitas = ?2", 1, tdrPengajuan.getId_identitas()).firstResult();
                    TdrIdentitasVendorSktEntity vTdrIdentitasVendorSkt = tdrIdentitasVendorSkt.find("where id_identitas = ?1", vTdrPengajuanVendorSkt.getId_identitas()).firstResult();
                    // getIs_minio_identitas_vendor
                    if (vTdrIdentitasVendorSkt == null || vTdrIdentitasVendorSkt.getId_identitas() == null) {
                        LOG.warn("[NOT FOUND] Identitas tidak ditemukan untuk id_identitas: " + vTdrPengajuanVendorSkt.getId_identitas());
                        LOG.info("[INFO] No pending vendors to process.");
                        LOG.info("Send Data Eproc SKT Finished");
                        return;
                    }

                    varDump("Tdr Pengajuan", vTdrPengajuanVendorSkt);
                    varDump("Tdr Identitas", vTdrIdentitasVendorSkt);

                    if (vTdrIdentitasVendorSkt.getIs_minio_identitas_vendor() != 1) {
                        uploadScheduled.upload_skt_by_id_pengajuan(vTdrPengajuanVendorSkt.getId_pengajuan());
                    }

                    MainData MainObject = new MainData();
                    MainObject.setEmail_address(vTdrIdentitasVendorSkt.getEmail());
                    MainObject.setPengajuan_id(vTdrPengajuanVendorSkt.getId_pengajuan());
                    MainObject.setStatus_skt("SKT");
                    MainObject.setVendor_id_tdr(vTdrPengajuanVendorSkt.getId_vendor_eproc());
                    List<String> renewalStatuses = Arrays.asList("SKDC1", "F", "REG05");
                    if(renewalStatuses.contains(vTdrPengajuanVendorSkt.getStatus_mcs()) || renewalStatuses.contains(vTdrPengajuanVendorSkt.getStatus_eproc())){
                        LOG.info("RENEWAL");
                        MainObject.setJenis_registrasi("RENEWAL"); 
                    } else {
                        LOG.info("NEW");
                        MainObject.setJenis_registrasi("NEW");
                    }

                    MainObject.setVendor_name(vTdrIdentitasVendorSkt.getNama_vendor());
                    MainObject.setBentuk_perusahaan_id((vTdrIdentitasVendorSkt.getKode_bentuk_usaha() != null)?vTdrIdentitasVendorSkt.getKode_bentuk_usaha():0);
                    MainObject.setBentuk_perusahaan(vTdrIdentitasVendorSkt.getNama_bentuk_usaha());
                    MainObject.setBadan_usaha_id((vTdrIdentitasVendorSkt.getKode_badan_usaha() != null)?vTdrIdentitasVendorSkt.getKode_badan_usaha():0);
                    MainObject.setBadan_usaha(vTdrIdentitasVendorSkt.getNama_badan_usaha());
                    MainObject.setKegiatan_usaha_id((vTdrIdentitasVendorSkt.getKode_kegiatan_usaha() != null)?vTdrIdentitasVendorSkt.getKode_kegiatan_usaha():0);
                    MainObject.setKegiatan_usaha(vTdrIdentitasVendorSkt.getNama_kegiatan_usaha());
                    MainObject.setJenis_kegiatan_usaha_id((vTdrIdentitasVendorSkt.getKode_jenis_kegiatan_usaha() != null)?vTdrIdentitasVendorSkt.getKode_jenis_kegiatan_usaha():0);
                    MainObject.setJenis_kegiatan_usaha(vTdrIdentitasVendorSkt.getNama_jenis_kegiatan_usaha());
                    // md.setCountry_id(11);
                    MainObject.setCountry(vTdrIdentitasVendorSkt.getNama_negara());
                    MainObject.setCountry_code(vTdrIdentitasVendorSkt.getKode_negara());
                    MainObject.setProvince_code(vTdrIdentitasVendorSkt.getKode_provinsi());
                    MainObject.setProvince(vTdrIdentitasVendorSkt.getNama_provinsi());
                    MainObject.setCity(vTdrIdentitasVendorSkt.getKota());
                    MainObject.setAddress(vTdrIdentitasVendorSkt.getAlamat());
                    MainObject.setBlock_lot_kavling(vTdrIdentitasVendorSkt.getBlok_nomor_kavling());
                    MainObject.setLongitude(vTdrIdentitasVendorSkt.getLongitude());
                    MainObject.setLatitude(vTdrIdentitasVendorSkt.getLatitude());
                    MainObject.setPostal_code(vTdrIdentitasVendorSkt.getKodepos());
                    MainObject.setOffice_phone_number(vTdrIdentitasVendorSkt.getNo_telp());
                    MainObject.setFax_number(vTdrIdentitasVendorSkt.getNo_facs());
                    MainObject.setWebsite(vTdrIdentitasVendorSkt.getWebsite());
                    MainObject.setCurrency_code(vTdrIdentitasVendorSkt.getKode_mata_uang());
                    MainObject.setDirector_name(vTdrIdentitasVendorSkt.getNama_dirut());
                    MainObject.setVendor_pic(vTdrIdentitasVendorSkt.getNama_pic());
                    MainObject.setPic_phone_number(vTdrIdentitasVendorSkt.getNo_telp_pic());

                    String[] bidang_usaha = (vTdrIdentitasVendorSkt.getBidang_usaha() != null)?vTdrIdentitasVendorSkt.getBidang_usaha().split("\\|"):null;
                    String[] sub_bidang_usaha = (vTdrIdentitasVendorSkt.getSub_bidang_usaha() != null)?vTdrIdentitasVendorSkt.getSub_bidang_usaha().split("\\|"):null;
                    String[] material = (vTdrIdentitasVendorSkt.getMaterial() != null)?vTdrIdentitasVendorSkt.getMaterial().split("\\|"):null;
                    String[] kualifikasi = (vTdrIdentitasVendorSkt.getKualifikasi() != null)?vTdrIdentitasVendorSkt.getKualifikasi().split("\\|"):null;

                    List<BusinessFieldData> b_usaha_array = new ArrayList<>();
                    if(bidang_usaha !=null && sub_bidang_usaha !=null && material != null && kualifikasi != null){
                        try{
                            for(Integer i = 0; i < bidang_usaha.length; i++){
                                BusinessFieldData b_usaha = new BusinessFieldData();
                                if(bidang_usaha[i] != null || bidang_usaha.equals("")){
                                    TdrMstBidangUsahaEntity tdrBidang = tdrBidangUsaha.find("kode = :kode", Parameters.with("kode", bidang_usaha[i])).firstResult();
                                    if(tdrBidang != null){
                                        b_usaha.setBidang_usaha(tdrBidang.getDeskripsi());
                                        b_usaha.setBidang_usaha_kode(bidang_usaha[i]);
                                    }
                                }
            
                                if(sub_bidang_usaha[i] != null || sub_bidang_usaha[i].equals("")){
                                    TdrSubBidangUsahaEntity tdrSubBidang = tdrSubBidangUsaha.find("kode = :kode", Parameters.with("kode", sub_bidang_usaha[i])).firstResult();
                                    if(tdrSubBidang != null){
                                        b_usaha.setSub_bidang_usaha_kode(sub_bidang_usaha[i]);
                                        b_usaha.setSub_bidang_usaha(tdrSubBidang.getDeskripsi());
                                    }
                                }
                                
                                if(kualifikasi[i] != null || kualifikasi[i].equals("")){
                                    // b_usaha.setKualifikasi_id(3);
                                    b_usaha.setKualifikasi(kualifikasi[i]);
                                }

                                List<MaterialTypeData> materialType = new ArrayList<>();
                                String[] materialString = (material[i] != null)?material[i].split("\\,"):null;
                                
                                if(materialString != null){
                                    for (String materialS : materialString) {
                                        if(materialS != null){
                                            TdrMaterialVendorEntity tdrMaterialV = tdrMaterialVendor.find("kode = :kode", Parameters.with("kode", materialS)).firstResult();
                                            MaterialTypeData mtd = new MaterialTypeData();
                                            if(tdrMaterialV != null){
                                                mtd.setJenis_material(tdrMaterialV.getDeskripsi());
                                                mtd.setJenis_material_kode(materialS);
                                            }
                                            
                                            Collections.addAll(materialType, mtd);
                                        }
                                    }
                                }
                                
                                b_usaha.setJenis_material(materialType);
                                Collections.addAll(b_usaha_array,b_usaha);
                                
                            }
                        }catch(Exception e){
                            LOG.error("Bidang Usaha "+objectMapper.writeValueAsString(b_usaha_array));
                        }
                    } else{
                        LOG.info("bidang usaha, sub bidang usaha, material, kualifikasi not exist");
                    }

                    List<ExperienceData> pengalamanArr = new ArrayList<>();
                    List<TdrPengalamanVendorMinioSktEntity> tdrPengalamanVendorMinioSktList = tdrPengalamanVendorMinioSkt.find("where id_identitas = :id_identitas", Parameters.with("id_identitas", vTdrIdentitasVendorSkt.getId_identitas())).list();
                    for (TdrPengalamanVendorMinioSktEntity elementPengalaman : tdrPengalamanVendorMinioSktList) {
                            ExperienceData experienceObject = new ExperienceData();
                            TdrMstBidangUsahaEntity tdrBidangUsahaEntity = tdrBidangUsaha.find("where kode = :kode",Parameters.with("kode", elementPengalaman.getBidang_usaha())).firstResult();
                            TdrSubBidangUsahaEntity tdrSubBidangUsahaEntity = tdrSubBidangUsaha.find("where kode = :kode", Parameters.with("kode", elementPengalaman.getSub_bidang_usaha())).firstResult();
                            experienceObject.setJob_name(elementPengalaman.getNama_pekerjaan());
                            experienceObject.setJob_owner_name(elementPengalaman.getNama_pemilik_pekerja());
                            experienceObject.setBidang_usaha_kode(elementPengalaman.getBidang_usaha());
                            experienceObject.setBidang_usaha(tdrBidangUsahaEntity.getDeskripsi());
                            experienceObject.setSub_bidang_usaha(tdrSubBidangUsahaEntity.getDeskripsi());
                            experienceObject.setSub_bidang_usaha_kode(elementPengalaman.getSub_bidang_usaha());
                            experienceObject.setAssignor(elementPengalaman.getPemberi_tugas());
                            experienceObject.setSpk_number(elementPengalaman.getNo_spk());
                            experienceObject.setSpk_issue_date(elementPengalaman.getTgl_mulai_spk().toString());
                            experienceObject.setSpk_end_date(elementPengalaman.getTgl_selesai_spk().toString());
                            experienceObject.setContract_value(elementPengalaman.getNilai_kontrak().longValue());
                            experienceObject.setFile_path(elementPengalaman.getMinio_path());
                            Collections.addAll(pengalamanArr, experienceObject);
                    }

                    List<BanksData> bankArr = new ArrayList<>();
                    List<TdrPerbankanVendorMinioSktEntity> tdrPerbankanVendorMinioSktEntity = tdrPerbankanVendorMinioSkt.find("where id_identitas = :id_identitas", Parameters.with("id_identitas", vTdrIdentitasVendorSkt.getId_identitas())).list();
                   for (TdrPerbankanVendorMinioSktEntity elementPerbankan : tdrPerbankanVendorMinioSktEntity) {
                        BanksData bankObject = new BanksData();
                        bankObject.setHas_bri_account((elementPerbankan.getHave_account_bri()) ? 1 : 0);
                        bankObject.setBank_location(elementPerbankan.getLokasi_bank());
                        bankObject.setBank_name(elementPerbankan.getNama_bank());
                        bankObject.setCity(elementPerbankan.getCity());
                        bankObject.setBank_key(elementPerbankan.getBank_key());
                        bankObject.setCountry(elementPerbankan.getNegara());
                        bankObject.setCountry_code(elementPerbankan.getKode_negara());
                        bankObject.setAccount_number(elementPerbankan.getNo_rek());
                        bankObject.setAccount_owner_name(elementPerbankan.getNama_rek());
                        bankObject.setSwift_code(elementPerbankan.getSwift_kode());
                        bankObject.setBank_branch(elementPerbankan.getCabang_bank());
                        bankObject.setFile_path(elementPerbankan.getMinio_path());
                        Collections.addAll(bankArr, bankObject);
                    }

                    List<OrganizationData> organisasiArr = new ArrayList<>();
                    List<TdrOrganisasiVendorSktEntity> tdrOrganisasiVendorSktEntity = tdrOrganisasiVendorSkt.find("where id_identitas = :id_identitas", Parameters.with("id_identitas", vTdrIdentitasVendorSkt.getId_identitas())).list();
                    for (TdrOrganisasiVendorSktEntity elementOrganisasi : tdrOrganisasiVendorSktEntity) {
                        OrganizationData organisasiObject = new OrganizationData();
                        organisasiObject.setPosition_id(elementOrganisasi.getKode_posisi());
                        organisasiObject.setName(elementOrganisasi.getNama());
                        organisasiObject.setPosition(elementOrganisasi.getPosisi());
                        organisasiObject.setJob_title(elementOrganisasi.getJabatan());
                        organisasiObject.setGender(elementOrganisasi.getJenis_kelamin());
                        organisasiObject.setNationality(elementOrganisasi.getKewarganegaraan());
                        organisasiObject.setNpwp(elementOrganisasi.getNpwp());
                        organisasiObject.setKtp(elementOrganisasi.getKtp_kitas());
                        organisasiObject.setNik(elementOrganisasi.getNik());
                        organisasiObject.setDate_of_birth(elementOrganisasi.getTgl_lahir().toString());
                        Collections.addAll(organisasiArr, organisasiObject);
                    }
        
                    List<ShareHoldersData> pemegangSahamArr = new ArrayList<>();
                    List<TdrPemegangSahamVendorSktEntity> tdrPemegangSahamVendorSktEntity = tdrPemegangSahamVendorSkt.find("where id_identitas = :id_identitas", Parameters.with("id_identitas", vTdrIdentitasVendorSkt.getId_identitas())).list();
                    for (TdrPemegangSahamVendorSktEntity elementPemegangSaham : tdrPemegangSahamVendorSktEntity) {
                        ShareHoldersData ShareHolderObject = new ShareHoldersData();
                        ShareHolderObject.setTipe(elementPemegangSaham.getTipe());
                        ShareHolderObject.setShareholder_name(elementPemegangSaham.getNama());
                        ShareHolderObject.setPosition(elementPemegangSaham.getJabatan());
                        ShareHolderObject.setNationality(elementPemegangSaham.getKewarganegaraan());
                        ShareHolderObject.setNpwp(elementPemegangSaham.getNpwp());
                        ShareHolderObject.setNik(elementPemegangSaham.getNik());
                        ShareHolderObject.setKtp(elementPemegangSaham.getKtp_kitas());
                        ShareHolderObject.setOwnership_type(elementPemegangSaham.getJenis_kepemilikan());
                        ShareHolderObject.setShare_amount(elementPemegangSaham.getJumlah_saham().toString());
                        ShareHolderObject.setNominal(elementPemegangSaham.getNominal_saham().intValue());
                        ShareHolderObject.setOwnership_percentage(elementPemegangSaham.getKepemilikan());
                        Collections.addAll(pemegangSahamArr, ShareHolderObject);
                    }
        
                    TdrJumlahSdmVendorSktEntity tdrJumlahSdmVendorSktEntity = tdrJumlahSdmVendorSkt.find("where id_identitas = :id_identitas", Parameters.with("id_identitas", vTdrIdentitasVendorSkt.getId_identitas())).firstResult();
                    HumanResourcesData HumanResourcesObject = new HumanResourcesData();
                    if(tdrJumlahSdmVendorSktEntity != null){
                        HumanResourcesObject.setManager(tdrJumlahSdmVendorSktEntity.getManajer());
                        HumanResourcesObject.setWorker(tdrJumlahSdmVendorSktEntity.getPekerja());
                        HumanResourcesObject.setExpert(tdrJumlahSdmVendorSktEntity.getTenaga_ahli());
                    }
                    
                    TdrReviewVendorSktEntity tdrReviewsVendorSktEntity = tdrReviewVendorSkt.find("where id_identitas = :id_identitas", Parameters.with("id_identitas", vTdrIdentitasVendorSkt.getId_identitas())).firstResult();
                    ReviewsData ReviewsObject = new ReviewsData();
                    if(tdrReviewsVendorSktEntity != null){
                        ReviewsObject.setTotal_project(Integer.parseInt(tdrReviewsVendorSktEntity.getTotal_project()));
                        ReviewsObject.setAverage_rating(Long.parseLong(tdrReviewsVendorSktEntity.getAverage_rating()));
                        ReviewsObject.setOntime_rate(Long.parseLong(tdrReviewsVendorSktEntity.getOntime_rate()));
                        ReviewsObject.setTotal_revenue(Long.parseLong(tdrReviewsVendorSktEntity.getTotal_revenue()));
                    }

                    List<TdrIdentitasVendorMinioSktEntity> vTdrIdentitasVendorMinioSkt = tdrIdentitasVendorMinioSkt.find("where id_pengajuan = :id ", Parameters.with("id", vTdrPengajuanVendorSkt.getId_pengajuan())).list();
                    List<LegalDocData> legalDoc = new ArrayList<>();
                    for (TdrIdentitasVendorMinioSktEntity e : vTdrIdentitasVendorMinioSkt) {
                        TdrMstFileLegalitasEntity file = tdrMstFile.find("where dok = :dok", Parameters.with("dok", e.getDok())).firstResult();
                        
                        if(e.getDok() == 3 || e.getDok() == 12 || e.getDok() == 13 || e.getDok() == 4 || e.getDok() == 6 || e.getDok() == 5 || e.getDok() == 15 || e.getDok() == 8 || e.getDok() == 9 || e.getDok() == 29){
                            LegalDocData docLegal = new LegalDocData();
                            docLegal.setDocument_name(file.getNama_file());
                            if(e.getDok() == 3){
                                docLegal.setDocument_number(vTdrIdentitasVendorSkt.getNomor_akta_pendirian());
                                docLegal.setDocument_valid_from(vTdrIdentitasVendorSkt.getTgl_terbit_akta_pendirian().toString());
                                docLegal.setDocument_valid_until(vTdrIdentitasVendorSkt.getTgl_akta_pendirian().toString());
                            }
            
                            if(e.getDok() == 12){
                                docLegal.setDocument_number(vTdrIdentitasVendorSkt.getNomor_akta_pengurus());
                                docLegal.setDocument_valid_from(vTdrIdentitasVendorSkt.getTgl_terbit_akta_pengurus().toString());
                                docLegal.setDocument_valid_until(vTdrIdentitasVendorSkt.getTgl_akta_pengurus().toString());
                            }
                            
                            if(e.getDok() == 13){
                                docLegal.setDocument_number(vTdrIdentitasVendorSkt.getNomor_akta_pemilik());
                                docLegal.setDocument_valid_from(vTdrIdentitasVendorSkt.getTgl_terbit_akta_pemilik().toString());
                                docLegal.setDocument_valid_until(vTdrIdentitasVendorSkt.getTgl_akta_pemilik().toString());
                            }
            
                            if(e.getDok() == 4){
                                docLegal.setDocument_number(vTdrIdentitasVendorSkt.getNomor_tanda_daftar());
                                docLegal.setDocument_valid_from(vTdrIdentitasVendorSkt.getTgl_terbit_tanda_daftar().toString());
                                docLegal.setDocument_valid_until(vTdrIdentitasVendorSkt.getTgl_tanda_daftar().toString());
                            }

                            if(e.getDok() == 6){
                                docLegal.setDocument_number(vTdrIdentitasVendorSkt.getNomor_siup());
                                docLegal.setDocument_valid_from(vTdrIdentitasVendorSkt.getTgl_terbit_siup().toString());
                                docLegal.setDocument_valid_until(vTdrIdentitasVendorSkt.getTgl_siup().toString());
                            }
            
                            if(e.getDok() == 5){
                                docLegal.setDocument_number(vTdrIdentitasVendorSkt.getNomor_skdp());
                                docLegal.setDocument_valid_from(vTdrIdentitasVendorSkt.getTgl_terbit_skdp().toString());
                                docLegal.setDocument_valid_until(vTdrIdentitasVendorSkt.getTgl_skdp().toString());
                            }
                            if(e.getDok() == 15){
                                docLegal.setDocument_number(vTdrIdentitasVendorSkt.getNomor_ijin());
                                docLegal.setDocument_valid_from(vTdrIdentitasVendorSkt.getTgl_terbit_ijin().toString());
                                docLegal.setDocument_valid_until(vTdrIdentitasVendorSkt.getTgl_ijin().toString());
                            }
            
                            if(e.getDok() == 8){
                                docLegal.setDocument_number(vTdrIdentitasVendorSkt.getNomor_npwp());
                                docLegal.setDocument_valid_from(vTdrIdentitasVendorSkt.getTgl_terbit_npwp().toString());
                                docLegal.setDocument_valid_until(vTdrIdentitasVendorSkt.getTgl_npwp().toString());
                            }
            
                            if(e.getDok() == 9){
                                docLegal.setDocument_number(vTdrIdentitasVendorSkt.getNomor_pkp());
                                docLegal.setDocument_valid_from(vTdrIdentitasVendorSkt.getTgl_terbit_pkp().toString());
                                docLegal.setDocument_valid_until(vTdrIdentitasVendorSkt.getTgl_pkp().toString());
                            }
            
                            if(e.getDok() == 29){
                                docLegal.setDocument_number(vTdrIdentitasVendorSkt.getNomor_pengalaman());
                                docLegal.setDocument_valid_from(vTdrIdentitasVendorSkt.getTgl_terbit_pengalaman().toString());
                                docLegal.setDocument_valid_until(vTdrIdentitasVendorSkt.getTgl_pengalaman().toString());
                            }

                            docLegal.setFile_path(e.getPath_minio());
                            docLegal.setType(file.getIproc_type());
                            Collections.addAll(legalDoc, docLegal);
                        }else{
                            LegalDocData docLegal = new LegalDocData();
                            docLegal.setDocument_name(file.getNama_file());
                            docLegal.setFile_path(e.getPath_minio());
                            docLegal.setType(file.getIproc_type());
                            Collections.addAll(legalDoc, docLegal);
                        }
                    }

                    List<CurrentAssetData> aktiva_lancar = new ArrayList<>();
                    List<FixedAssetData> aktiva_tetap = new ArrayList<>();
                    List<CurrentLiabilityData> kewajiban_lancar = new ArrayList<>();
                    List<NoCurrentLiabilityData> kewajiban_tidak_lancar = new ArrayList<>();
                    List<EquityData> ekuitas = new ArrayList<>();
                    List<LiabilitiesAndEquityData> kewajiban_equitas = new ArrayList<>();
                    List<ProfitData> laba = new ArrayList<>();
                
                    if(vTdrIdentitasVendorSkt.getLap_keu() != null) {
                        LapkeuDto lapkeu = this.parseJsonLapkeu(vTdrIdentitasVendorSkt.getLap_keu());
                        // Registration regismail = new Registration();
                        if(lapkeu != null){
                            String[] thn_lapkeu_rur = (lapkeu.getThn_lapkeu_rur() != null)?lapkeu.getThn_lapkeu_rur().split("\\|"):null;
                            String[] kas_lapkeu_rur = (lapkeu.getKas_lapkeu_rur() != null)?lapkeu.getKas_lapkeu_rur().split("\\|"):null;
                            String[] piutang_rur = (lapkeu.getPiutang_rur() != null)?lapkeu.getPiutang_rur().split("\\|"):null;
                            String[] persediaan_rur = (lapkeu.getPersediaan_rur() != null )?lapkeu.getPersediaan_rur().split("\\|"): null;
                            String[] pajak_muka_rur = (lapkeu.getPajak_muka_rur() != null)?lapkeu.getPajak_muka_rur().split("\\|"):null;
                            String[] biaya_muka_rur = (lapkeu.getBiaya_muka_rur() != null)?lapkeu.getBiaya_muka_rur().split("\\|"):null;
                            String[] aktiva_lancar_lain_rur = (lapkeu.getAktiva_lancar_lain_rur() != null)?lapkeu.getAktiva_lancar_lain_rur().split("\\|"):null;
                            String[] tot_aktiva_lancar_rur = (lapkeu.getTot_aktiva_lancar_rur() != null)?lapkeu.getTot_aktiva_lancar_rur().split("\\|"):null;
                            String[] aset_tetap_rur = (lapkeu.getAset_tetap_rur() != null)?lapkeu.getAset_tetap_rur().split("\\|"): null;
                            String[] aktiva_nonlancar_lain_rur = (lapkeu.getAktiva_nonlancar_lain_rur() != null)?lapkeu.getAktiva_nonlancar_lain_rur().split("\\|"):null;
                            String[] tot_aktiva_nonlancar_rur = (lapkeu.getTot_aktiva_nonlancar_rur() != null)?lapkeu.getTot_aktiva_nonlancar_rur().split("\\|"):null;
                            String[] tot_aktiva_rur = (lapkeu.getTot_aktiva_rur() != null)?lapkeu.getTot_aktiva_rur().split("\\|"):null;
            
                            String[] pinjaman_pendek_rur = (lapkeu.getPinjaman_pendek_rur() != null)?lapkeu.getPinjaman_pendek_rur().split("\\|"):null;
                            String[] hutang_bank_lancar_rur = (lapkeu.getHutang_bank_lancar_rur() != null)?lapkeu.getHutang_bank_lancar_rur().split("\\|"):null;
                            String[] hutang_usaha_lancar_rur = (lapkeu.getHutang_usaha_lancar_rur() != null)?lapkeu.getHutang_usaha_lancar_rur().split("\\|"):null;
                            String[] hutang_pajak_rur = (lapkeu.getHutang_pajak_rur() != null)?lapkeu.getHutang_pajak_rur().split("\\|"):null;
                            String[] beban_bayar_rur = (lapkeu.getBeban_bayar_rur() != null)?lapkeu.getBeban_bayar_rur().split("\\|"):null;
                            String[] kewajiban_lancar_lain_rur = (lapkeu.getKewajiban_lancar_lain_rur() != null)?lapkeu.getKewajiban_lancar_lain_rur().split("\\|"):null;
                            String[] hutang_bank_nonlancar_rur = (lapkeu.getHutang_bank_nonlancar_rur() != null)?lapkeu.getHutang_bank_nonlancar_rur().split("\\|"): null;
                            String[] hutang_usaha_nonlancar_rur = (lapkeu.getHutang_usaha_nonlancar_rur() != null)?lapkeu.getHutang_usaha_nonlancar_rur().split("\\|"):null;
                            String[] pajak_tangguhan_rur = (lapkeu.getPajak_tangguhan_rur() != null)?lapkeu.getPajak_tangguhan_rur().split("\\|"): null;
                            String[] pinjaman_panjang_rur = (lapkeu.getPinjaman_panjang_rur() != null)?lapkeu.getPinjaman_panjang_rur().split("\\|"): null;
                            String[] sewa_usaha_rur = (lapkeu.getSewa_usaha_rur() != null)?lapkeu.getSewa_usaha_rur().split("\\|"): null;
            
                            String[] kewajiban_nonlancar_lain_rur = (lapkeu.getKewajiban_nonlancar_lain_rur() != null)?lapkeu.getKewajiban_nonlancar_lain_rur().split("\\|"): null;
                            String[] tot_kewajiban_nonlancar_rur = (lapkeu.getTot_aktiva_nonlancar_rur() != null)?lapkeu.getTot_kewajiban_nonlancar_rur().split("\\|"): null;
                            String[] tot_kewajiban_rur = (lapkeu.getTot_kewajiban_rur() != null)?lapkeu.getTot_kewajiban_rur().split("\\|"):null;
                            String[] modal_rur = (lapkeu.getModal_rur() != null)?lapkeu.getModal_rur().split("\\|"):null;
                            String[] laba_berjalan_rur = (lapkeu.getLaba_berjalan_rur() != null)?lapkeu.getLaba_berjalan_rur().split("\\|"):null;
                            String[] laba_ditahan_rur = (lapkeu.getLaba_ditahan_rur() != null)?lapkeu.getLaba_ditahan_rur().split("\\|"):null;
                            String[] ekuitas_lain_rur = (lapkeu.getEkuitas_lain_rur() != null)?lapkeu.getEkuitas_lain_rur().split("\\|"):null;
                            String[] tot_ekuitas_rur = (lapkeu.getTot_ekuitas_rur() != null)?lapkeu.getTot_ekuitas_rur().split("\\|"):null;
                            String[] kewajiban_ekuitas_rur = (lapkeu.getKewajiban_ekuitas_rur() != null)?lapkeu.getKewajiban_ekuitas_rur().split("\\|"): null;
                            String[] control_balance_rur = (lapkeu.getControl_balance_rur() != null)?lapkeu.getControl_balance_rur().split("\\|"): null;
                            String[] pendapatan_rur = (lapkeu.getPendapatan_rur() != null )?lapkeu.getPendapatan_rur().split("\\|"):null;
                            String[] tot_kewajiban_lancar = (lapkeu.getTot_kewajiban_lancar_rur() != null )?lapkeu.getTot_kewajiban_lancar_rur().split("\\|"):null;
            
                            String[] laba_rur = (lapkeu.getLaba_rur() != null)?lapkeu.getLaba_rur().split("\\|"):null;
                            String[] biaya_rur = (lapkeu.getBiaya_rur() != null)?lapkeu.getBiaya_rur().split("\\|"):null;
            
                            for(Integer i = 0; i < thn_lapkeu_rur.length; i++){
                                CurrentAssetData al = new CurrentAssetData();
                                FixedAssetData at = new FixedAssetData();
                                CurrentLiabilityData kl = new CurrentLiabilityData();
                                NoCurrentLiabilityData ketla = new NoCurrentLiabilityData();
                                EquityData eq = new EquityData();
                                LiabilitiesAndEquityData ke = new LiabilitiesAndEquityData();
                                ProfitData lb = new ProfitData();
                                NumberFormat nf = NumberFormat.getInstance(Locale.FRANCE);
            
                                al.setKas_dan_bank(Long.parseLong(nf.parse(kas_lapkeu_rur[i]).toString()));
                                al.setPiutang_usaha(Long.parseLong(nf.parse(piutang_rur[i]).toString()));
                                al.setPersediaan(Long.parseLong(nf.parse(persediaan_rur[i]).toString()));
                                al.setPajak_dibayar_muka(Long.parseLong(nf.parse(pajak_muka_rur[i]).toString()));
                                al.setBiaya_dibayar_muka(Long.parseLong(nf.parse(biaya_muka_rur[i]).toString()));
                                al.setAktiva_lancar_lainnya(Long.parseLong(nf.parse(aktiva_lancar_lain_rur[i]).toString()));
                                al.setTotal_aktiva_lancar(Long.parseLong(nf.parse(tot_aktiva_lancar_rur[i]).toString()));
                                al.setTahun(Integer.parseInt(thn_lapkeu_rur[i]));
                                Collections.addAll(aktiva_lancar, al);
            
                                at.setAsset_tetap(Long.parseLong(nf.parse(aset_tetap_rur[i]).toString()));
                                at.setAktiva_tidak_lancar(Long.parseLong(nf.parse(aktiva_nonlancar_lain_rur[i]).toString()));
                                at.setTotal_aktiva(Long.parseLong(nf.parse(tot_aktiva_rur[i]).toString()));
                                at.setTotal_aktiva_tidak_lancar(Long.parseLong(nf.parse(tot_aktiva_nonlancar_rur[i]).toString()));
                                at.setTahun(Integer.parseInt(thn_lapkeu_rur[i]));
                                Collections.addAll(aktiva_tetap, at);
            
                                kl.setPinjaman_jangka_pendek(Long.parseLong(nf.parse(pinjaman_pendek_rur[i]).toString()));
                                kl.setHutang_bank(Long.parseLong(nf.parse(hutang_bank_lancar_rur[i]).toString()));
                                kl.setHutang_usaha(Long.parseLong(nf.parse(hutang_usaha_lancar_rur[i]).toString()));
                                kl.setHutang_pajak(Long.parseLong(nf.parse(hutang_pajak_rur[i]).toString()));
                                kl.setPinjaman_jangka_pendek(Long.parseLong(nf.parse(pinjaman_pendek_rur[i]).toString()));
                                kl.setBadan_yang_harus_dibayar(Long.parseLong(nf.parse(beban_bayar_rur[i]).toString()));
                                kl.setKewajiban_lancar_lainnya(Long.parseLong(nf.parse(kewajiban_lancar_lain_rur[i]).toString()));
                                kl.setTotal_kewajiban_lancar(Long.parseLong(nf.parse(tot_kewajiban_lancar[i]).toString()));
                                kl.setTahun(Integer.parseInt(thn_lapkeu_rur[i]));
                                Collections.addAll(kewajiban_lancar, kl);
            
                                ketla.setHutang_bank(Long.parseLong(nf.parse(hutang_bank_nonlancar_rur[i]).toString()));
                                ketla.setHutang_usaha(Long.parseLong(nf.parse(hutang_usaha_nonlancar_rur[i]).toString()));
                                ketla.setKewajiban_pajak_tangguhan(Long.parseLong(nf.parse(pajak_tangguhan_rur[i]).toString()));
                                ketla.setPinjaman_jangka_panjang(Long.parseLong(nf.parse(pinjaman_panjang_rur[i]).toString()));
                                ketla.setHutang_sewa_guna(Long.parseLong(nf.parse(sewa_usaha_rur[i]).toString()));
                                ketla.setKewajiban_tidak_lancar_lainnya(Long.parseLong(nf.parse(kewajiban_nonlancar_lain_rur[i]).toString()));
                                ketla.setTotal_kewajiban_tidak_lancar(Long.parseLong(nf.parse(tot_kewajiban_nonlancar_rur[i]).toString()));
                                ketla.setTotal_kewajiban(Long.parseLong(nf.parse(tot_kewajiban_rur[i]).toString()));
                                ketla.setTahun(Integer.parseInt(thn_lapkeu_rur[i]));
                                Collections.addAll(kewajiban_tidak_lancar, ketla);
        
                                eq.setModal(Long.parseLong(nf.parse(modal_rur[i]).toString()));
                                eq.setLaba_rugi_tahun_berjalan(Long.parseLong(nf.parse(laba_berjalan_rur[i]).toString()));
                                eq.setLaba_ditahan(Long.parseLong(nf.parse(laba_ditahan_rur[i]).toString()));
                                eq.setEkuitas_lainnya(Long.parseLong(nf.parse(ekuitas_lain_rur[i]).toString()));
                                eq.setTotal_ekuitas(Long.parseLong(nf.parse(tot_ekuitas_rur[i]).toString()));
                                eq.setTahun(Integer.parseInt(thn_lapkeu_rur[i]));
                                Collections.addAll(ekuitas, eq);
                        
                                ke.setKewajiban_dan_ekuitas(Long.parseLong(nf.parse(kewajiban_ekuitas_rur[i]).toString()));
                                ke.setControl_balance(Long.parseLong(nf.parse(control_balance_rur[i]).toString()));
                                ke.setTahun(Integer.parseInt(thn_lapkeu_rur[i]));
                                Collections.addAll(kewajiban_equitas, ke);
            
                                lb.setLaba(Long.parseLong(nf.parse(laba_rur[i]).toString()));
                                lb.setBiaya(Long.parseLong(nf.parse(biaya_rur[i]).toString()));
                                lb.setPendapatan(Long.parseLong(nf.parse(pendapatan_rur[i]).toString()));
                                lb.setTahun(Integer.parseInt(thn_lapkeu_rur[i]));
                                Collections.addAll(laba, lb);
                            }
                        }

                        
                        // legalDoc.removeIf(param -> param.getDocument_number() == null);
                        // legalDoc.removeIf(param -> param.getDocument_valid_from() == null);
                        Registration regis = new Registration();
                        regis.setEmail(vTdrIdentitasVendorSkt.getEmail());
                    
                        RegistrationData regData = new RegistrationData();
                        regData.setRegistration(regis);
                        regData.setMain(MainObject);
                        regData.setBidang_usaha(b_usaha_array);
                        regData.setLegal_docs(legalDoc);
                        regData.setExperiences(pengalamanArr);
                        regData.setBanks(bankArr);
                        regData.setOrganization_structures(organisasiArr);
                        regData.setShareholders(pemegangSahamArr);
                        regData.setHuman_resources(HumanResourcesObject);
                        regData.setReviews(ReviewsObject);
                        regData.setAktiva_lancar(aktiva_lancar);
                        regData.setAktiva_tetap(aktiva_tetap);
                        regData.setKewajiban_lancar(kewajiban_lancar);
                        regData.setKewajiban_tidak_lancar(kewajiban_tidak_lancar);
                        regData.setEkuitas(ekuitas);
                        regData.setKewajiban_dan_ekuitas(kewajiban_equitas);
                        regData.setLaba(laba);
        
                        String urlApiEproc = ConfigProvider.getConfig().getValue("url.registrasi.eproc", String.class);
                        httpRequest hr = new httpRequest();
                        responseConsume res = hr.postData(urlApiEproc, regData);
                        String request = objectMapper.writeValueAsString(regData);
                        String response = objectMapper.writeValueAsString(res);

                        if(res.getData() != null){
                            String resultData = objectMapper.writeValueAsString(res.getData());
                            JsonNode rootNode = objectMapper.readTree(resultData);

                            vTdrPengajuanVendorSkt.setStatus_eproc(rootNode.get("code").asText());
                            vTdrPengajuanVendorSkt.setMessage_is_eproc(objectMapper.writeValueAsString(res));
                            vTdrPengajuanVendorSkt.setIs_eproc(1);
                            tdrPengajuanVendorSkt.persist(vTdrPengajuanVendorSkt);

                            // TdrIdentitasVendorEntity tdrIdentitasUpdate = tdrIdentitasVendor.find("id_identitas = ?1", p.getId_identitas()).firstResult();
                            vTdrIdentitasVendorSkt.setIs_minio_identitas_vendor(2);
                            tdrIdentitasVendorSkt.persist(vTdrIdentitasVendorSkt);
                            
                            TdrLogEprocEntity tdrLogEproc = new TdrLogEprocEntity();
                            tdrLogEproc.setRequest(request);
                            tdrLogEproc.setResponse(response);
                            tdrLogEproc.setCreated_at(LocalDateTime.now());
                            tdrLogEproc.persist();

                        }else{
                            if(res.getMessage() != null){
                                vTdrPengajuanVendorSkt.setIs_eproc(1);
                                
                                // TdrPengajuanVendorSktEntity tdrPengajuanUpdate = tdrPengajuanVendorSkt.find("id_pengajuan = ?1", vTdrPengajuanVendorSkt.getId_pengajuan()).firstResult();
                                vTdrPengajuanVendorSkt.setStatus_eproc("REG01");
                                vTdrPengajuanVendorSkt.setMessage_is_eproc(objectMapper.writeValueAsString(res));
                                tdrPengajuanVendorSkt.persist(vTdrPengajuanVendorSkt);

                                // tdrPengajuanVendor.persist(tdrPengajuanUpdate); 
                            }else{
                                vTdrPengajuanVendorSkt.setIs_eproc(0);
                                
                                // TdrPengajuanVendorSktEntity tdrPengajuanUpdate = tdrPengajuanVendorSkt.find("id_pengajuan = ?1", vTdrPengajuanVendorSkt.getId_pengajuan()).firstResult();
                                vTdrPengajuanVendorSkt.setStatus_eproc("SCH01");
                                vTdrPengajuanVendorSkt.setMessage_is_eproc(objectMapper.writeValueAsString(res.getValidation()));
                                tdrPengajuanVendorSkt.persist(vTdrPengajuanVendorSkt);
                                // tdrPengajuanVendor.persist(tdrPengajuanUpdate);
                            }

                            TdrLogEprocEntity tdrLogEproc = new TdrLogEprocEntity();
                            tdrLogEproc.setRequest(request);
                            tdrLogEproc.setResponse(response);
                            tdrLogEproc.setCreated_at(LocalDateTime.now());
                            tdrLogEproc.persist();
                        }
                        // LOG.info("request SKT = "+request);
                        // LOG.info("response SKT = "+response);
                    }else{
                        LOG.info("Lapkeu Not Exists");
                    }
                    
                } catch (Exception e) {
                    LOG.error("[ERROR] Failed to process identitas for id_identitas: " + vTdrPengajuanVendorSkt.getId_identitas() + " - Error: " + e.getMessage(), e);
                } 
            }
            LOG.info("Send Data Eproc SKT finished");
        } catch (Exception e) {
            LOG.error("[ERROR] Failed to process SKT vendors - Error: " + e.getMessage(), e);
        }

        LOG.info("==============================================");
        LOG.info("========== SCHEDULER SEND SKT END ============");
        LOG.info("==============================================");
    }

     // @Scheduled(every = "60s")
    @Transactional
    public void send_tdr_nonskt(){
        send_tdr_iproc_nonskt(null);
    }

    /**
     * Method untuk upload berdasarkan id_pengajuan tertentu
     */
    @Transactional
    public void send_tdr_nonskt_by_id_pengajuan(String idPengajuan){
        send_tdr_iproc_nonskt(idPengajuan);
    }

    // @Scheduled(every = "60s")
    @Transactional
    public void send_tdr_iproc_nonskt(String specificIdPengajuan){
        String mainJobsApps = ConfigProvider.getConfig().getValue("name.jobs", String.class);

        LOG.info("==================================================");
        LOG.info("========== SCHEDULER SEND NON SKT START ==========");
        LOG.info("==================================================");

        TdrSendFileEprocStatusEntity tdrEprocStatus = TdrSendFileEprocStatusEntity.find("nama_jobs = ?1", mainJobsApps).firstResult();
        varDump("TdrSendFileEprocStatusEntity", tdrEprocStatus);
        LOG.info("[STATUS] Scheduler iproc nonskt = " + (tdrEprocStatus != null ? tdrEprocStatus.getStatus_eproc() : "NULL"));
        
        if (tdrEprocStatus == null || !tdrEprocStatus.getStatus_eproc().equals(StatusEprocEnum.ON)) {
            LOG.info("[STATUS] Scheduler is OFF. Skipping execution.");
            return;
        }
        LOG.info("[STATUS] Scheduler iproc non skt = " + tdrEprocStatus.getStatus_eproc());

        try {
            LOG.info("Send Data Eproc Non SKT begin" + (specificIdPengajuan != null ? " for ID: " + specificIdPengajuan : ""));

            List<TdrPengajuanVendorNonSktEntity> tdrPengajuanVendorNonSktList;
            if(specificIdPengajuan != null && !specificIdPengajuan.isEmpty()) {
                // Upload berdasarkan id_pengajuan tertentu
                tdrPengajuanVendorNonSktList = tdrPengajuanVendorNonSkt.find("where is_eproc is null AND status_eproc = ?1 AND id_pengajuan = ?2 ORDER BY id_pengajuan DESC", "SCH00", specificIdPengajuan).list();
            } else {
                // Upload scheduled (semua data)
                tdrPengajuanVendorNonSktList = tdrPengajuanVendorNonSkt.find("where is_eproc is null AND status_eproc = ?1 ORDER BY id_pengajuan DESC", "SCH00").page(0, 5).list();
            }

            LOG.info("[DATA] Total Pengajuan vendors NON SKT found: " + tdrPengajuanVendorNonSktList.size());
            if (tdrPengajuanVendorNonSktList.isEmpty()) {
                LOG.info("[INFO] No pending vendors to process.");
                LOG.info("Send Data Eproc Non SKT Finished");
                return;
            }

            int dumpLimit = Math.min(5, tdrPengajuanVendorNonSktList.size());
            for (int i = 0; i < dumpLimit; i++) {
                varDump("Vendor NON SKT[" + i + "]", tdrPengajuanVendorNonSktList.get(i));
            }

            for (TdrPengajuanVendorNonSktEntity vTdrPengajuanVendorNonSkt : tdrPengajuanVendorNonSktList) {
                if (vTdrPengajuanVendorNonSkt == null || vTdrPengajuanVendorNonSkt.getId_identitas() == null || vTdrPengajuanVendorNonSkt.getId_identitas() == 0) {
                    LOG.info("[SKIP] Id Identitas Not Exists for pengajuan: " + (vTdrPengajuanVendorNonSkt != null ? vTdrPengajuanVendorNonSkt.getId_pengajuan() : "NULL"));
                    continue;
                } 

                try {
                    // TdrIdentitasVendorNonSktEntity tdrIdentitas = TdrIdentitasVendorNonSktEntity.find("where is_minio_identitas_vendor = ?1 AND id_identitas = ?2", 1, tdrPengajuan.getId_identitas()).firstResult();
                    TdrIdentitasVendorNonSktEntity vTdrIdentitasVendorNonSkt = tdrIdentitasVendorNonSkt.find("where id_identitas = ?1", vTdrPengajuanVendorNonSkt.getId_identitas()).firstResult();
                    
                    if (vTdrIdentitasVendorNonSkt == null || vTdrIdentitasVendorNonSkt.getId_identitas() == null) {
                        LOG.warn("[NOT FOUND] Identitas tidak ditemukan untuk id_identitas: " + vTdrPengajuanVendorNonSkt.getId_identitas());
                        LOG.info("[INFO] No pending vendors to process.");
                        LOG.info("Send Data Eproc Non SKT Finished");
                        return;
                    }

                    varDump("Tdr Pengajuan", vTdrPengajuanVendorNonSkt);
                    varDump("Tdr Identitas", vTdrIdentitasVendorNonSkt);

                    if (vTdrIdentitasVendorNonSkt.getIs_minio_identitas_vendor() != 1) {
                        uploadScheduled.upload_skt_by_id_pengajuan(vTdrPengajuanVendorNonSkt.getId_pengajuan());
                    }
                    
                    MainData MainObject = new MainData();
                    MainObject.setEmail_address(vTdrIdentitasVendorNonSkt.getEmail());
                    MainObject.setPengajuan_id(vTdrPengajuanVendorNonSkt.getId_pengajuan());
                    MainObject.setStatus_skt("NON_SKT");
                    MainObject.setVendor_id_tdr(vTdrPengajuanVendorNonSkt.getId_vendor_eproc());
                    MainObject.setJenis_registrasi("NEW");
                    MainObject.setVendor_name(vTdrIdentitasVendorNonSkt.getNama_vendor());
                    MainObject.setBentuk_perusahaan_id((vTdrIdentitasVendorNonSkt.getKode_bentuk_usaha() != null)?vTdrIdentitasVendorNonSkt.getKode_bentuk_usaha():0);
                    MainObject.setBentuk_perusahaan(vTdrIdentitasVendorNonSkt.getNama_bentuk_usaha());
                    MainObject.setBadan_usaha_id((vTdrIdentitasVendorNonSkt.getKode_badan_usaha() != null)?vTdrIdentitasVendorNonSkt.getKode_badan_usaha():0);
                    MainObject.setBadan_usaha(vTdrIdentitasVendorNonSkt.getNama_badan_usaha());
                    MainObject.setKegiatan_usaha_id((vTdrIdentitasVendorNonSkt.getKode_kegiatan_usaha() != null)?vTdrIdentitasVendorNonSkt.getKode_kegiatan_usaha():0);
                    MainObject.setKegiatan_usaha(vTdrIdentitasVendorNonSkt.getNama_kegiatan_usaha());
                    MainObject.setJenis_kegiatan_usaha_id((vTdrIdentitasVendorNonSkt.getKode_jenis_kegiatan_usaha() != null)?vTdrIdentitasVendorNonSkt.getKode_jenis_kegiatan_usaha():0);
                    MainObject.setJenis_kegiatan_usaha(vTdrIdentitasVendorNonSkt.getNama_jenis_kegiatan_usaha());
                    // md.setCountry_id(11);
                    MainObject.setCountry(vTdrIdentitasVendorNonSkt.getNama_negara());
                    MainObject.setCountry_code(vTdrIdentitasVendorNonSkt.getKode_negara());
                    MainObject.setProvince_code(vTdrIdentitasVendorNonSkt.getKode_provinsi());
                    MainObject.setProvince(vTdrIdentitasVendorNonSkt.getNama_provinsi());
                    MainObject.setCity(vTdrIdentitasVendorNonSkt.getKota());
                    MainObject.setAddress(vTdrIdentitasVendorNonSkt.getAlamat());
                    MainObject.setBlock_lot_kavling(vTdrIdentitasVendorNonSkt.getBlok_nomor_kavling());
                    MainObject.setLongitude(vTdrIdentitasVendorNonSkt.getLongitude());
                    MainObject.setLatitude(vTdrIdentitasVendorNonSkt.getLatitude());
                    MainObject.setPostal_code(vTdrIdentitasVendorNonSkt.getKodepos());
                    MainObject.setOffice_phone_number(vTdrIdentitasVendorNonSkt.getNo_telp());
                    MainObject.setFax_number(vTdrIdentitasVendorNonSkt.getNo_facs());
                    MainObject.setWebsite(vTdrIdentitasVendorNonSkt.getWebsite());
                    MainObject.setCurrency_code(vTdrIdentitasVendorNonSkt.getKode_mata_uang());
                    MainObject.setDirector_name(vTdrIdentitasVendorNonSkt.getNama_dirut());
                    MainObject.setVendor_pic(vTdrIdentitasVendorNonSkt.getNama_pic());
                    MainObject.setPic_phone_number(vTdrIdentitasVendorNonSkt.getNo_telp_pic());

                    String[] bidang_usaha = (vTdrIdentitasVendorNonSkt.getBidang_usaha() != null)?vTdrIdentitasVendorNonSkt.getBidang_usaha().split("\\|"):null;
                    String[] sub_bidang_usaha = (vTdrIdentitasVendorNonSkt.getSub_bidang_usaha() != null)?vTdrIdentitasVendorNonSkt.getSub_bidang_usaha().split("\\|"):null;
                    String[] material = (vTdrIdentitasVendorNonSkt.getMaterial() != null)?vTdrIdentitasVendorNonSkt.getMaterial().split("\\|"):null;
                    String[] kualifikasi = (vTdrIdentitasVendorNonSkt.getKualifikasi() != null)?vTdrIdentitasVendorNonSkt.getKualifikasi().split("\\|"):null;

                    List<BusinessFieldData> b_usaha_array = new ArrayList<>();
                    if(bidang_usaha !=null && sub_bidang_usaha !=null && material != null && kualifikasi != null){
                        try{
                            for(Integer i = 0; i < bidang_usaha.length; i++){
                                BusinessFieldData b_usaha = new BusinessFieldData();
                                if(bidang_usaha[i] != null || bidang_usaha.equals("")){
                                    TdrMstBidangUsahaEntity tdrBidang = tdrBidangUsaha.find("kode = :kode", Parameters.with("kode", bidang_usaha[i])).firstResult();
                                    if(tdrBidang != null){
                                        b_usaha.setBidang_usaha(tdrBidang.getDeskripsi());
                                        b_usaha.setBidang_usaha_kode(bidang_usaha[i]);
                                    }
                                }
            
                                if(sub_bidang_usaha[i] != null || sub_bidang_usaha[i].equals("")){
                                    TdrSubBidangUsahaEntity tdrSubBidang = tdrSubBidangUsaha.find("kode = :kode", Parameters.with("kode", sub_bidang_usaha[i])).firstResult();
                                    if(tdrSubBidang != null){
                                        b_usaha.setSub_bidang_usaha_kode(sub_bidang_usaha[i]);
                                        b_usaha.setSub_bidang_usaha(tdrSubBidang.getDeskripsi());
                                    }
                                }
                                
                                if(kualifikasi[i] != null || kualifikasi[i].equals("")){
                                    b_usaha.setKualifikasi_id(3);
                                    b_usaha.setKualifikasi(kualifikasi[i]);
                                }

                                List<MaterialTypeData> materialType = new ArrayList<>();
                                String[] materialString = (material[i] != null)?material[i].split("\\,"):null;
                                
                                if(materialString != null){
                                    for (String materialS : materialString) {
                                        if(materialS != null){
                                            TdrMaterialVendorEntity tdrMaterialV = tdrMaterialVendor.find("kode = :kode", Parameters.with("kode", materialS)).firstResult();
                                            MaterialTypeData mtd = new MaterialTypeData();
                                            if(tdrMaterialV != null){
                                                mtd.setJenis_material(tdrMaterialV.getDeskripsi());
                                                mtd.setJenis_material_kode(materialS);
                                            }
                                            
                                            Collections.addAll(materialType, mtd);
                                        }
                                    }
                                }
                                
                                b_usaha.setJenis_material(materialType);
                                Collections.addAll(b_usaha_array,b_usaha);
                                
                            }
                        }catch(Exception e){
                            LOG.error("Bidang Usaha "+objectMapper.writeValueAsString(b_usaha_array));
                        }
                    } else{
                        LOG.info("bidang usaha, sub bidang usaha, material, kualifikasi not exist");
                    }

                    List<BanksData> bankArr = new ArrayList<>();
                    List<TdrPerbankanVendorMinioNonSktEntity> tdrPerbankanVendorMinioNonSktEntity = tdrPerbankanVendorMinioNonSkt.find("where id_identitas = :id_identitas", Parameters.with("id_identitas", vTdrIdentitasVendorNonSkt.getId_identitas())).list();
                    for (TdrPerbankanVendorMinioNonSktEntity elementPerbankan : tdrPerbankanVendorMinioNonSktEntity) {
                        BanksData bankObject = new BanksData();
                        bankObject.setHas_bri_account((elementPerbankan.getHave_account_bri()) ? 1 : 0);
                        bankObject.setBank_location(elementPerbankan.getLokasi_bank());
                        bankObject.setBank_name(elementPerbankan.getNama_bank());
                        bankObject.setCity(elementPerbankan.getCity());
                        bankObject.setBank_key(elementPerbankan.getBank_key());
                        bankObject.setCountry(elementPerbankan.getNegara());
                        bankObject.setCountry_code(elementPerbankan.getKode_negara());
                        bankObject.setAccount_number(elementPerbankan.getNo_rek());
                        bankObject.setAccount_owner_name(elementPerbankan.getNama_rek());
                        bankObject.setSwift_code(elementPerbankan.getSwift_kode());
                        bankObject.setBank_branch(elementPerbankan.getCabang_bank());
                        bankObject.setFile_path(elementPerbankan.getMinio_path());
                        Collections.addAll(bankArr, bankObject);
                    }

                    List<OrganizationData> organisasiArr = new ArrayList<>();
                    List<TdrOrganisasiVendorNonSktEntity> tdrOrganisasiVendorNonSktEntity = tdrOrganisasiVendorNonSkt.find("where id_identitas = :id_identitas", Parameters.with("id_identitas", vTdrIdentitasVendorNonSkt.getId_identitas())).list();
                    for (TdrOrganisasiVendorNonSktEntity elementOrganisasi : tdrOrganisasiVendorNonSktEntity) {
                        OrganizationData organisasiObject = new OrganizationData();
                        organisasiObject.setPosition_id(elementOrganisasi.getKode_posisi());
                        organisasiObject.setName(elementOrganisasi.getNama());
                        organisasiObject.setPosition(elementOrganisasi.getPosisi());
                        organisasiObject.setJob_title(elementOrganisasi.getJabatan());
                        organisasiObject.setGender(elementOrganisasi.getJenis_kelamin());
                        organisasiObject.setNationality(elementOrganisasi.getKewarganegaraan());
                        organisasiObject.setNpwp(elementOrganisasi.getNpwp());
                        organisasiObject.setNpp(elementOrganisasi.getNpp());
                        organisasiObject.setKtp(elementOrganisasi.getKtp_kitas());
                        organisasiObject.setDate_of_birth((elementOrganisasi.getTgl_lahir()!=null)?elementOrganisasi.getTgl_lahir().toString():null);
                        Collections.addAll(organisasiArr, organisasiObject);
                    }

                    TdrJumlahSdmVendorNonSktEntity tdrJumlahSdmVendorNonSktEntity = tdrJumlahSdmVendorNonSkt.find("where id_identitas = :id_identitas", Parameters.with("id_identitas", vTdrIdentitasVendorNonSkt.getId_identitas())).firstResult();
                    HumanResourcesData HumanResourcesObject = new HumanResourcesData();
                    if(tdrJumlahSdmVendorNonSktEntity != null){
                        HumanResourcesObject.setManager(tdrJumlahSdmVendorNonSktEntity.getManajer());
                        HumanResourcesObject.setWorker(tdrJumlahSdmVendorNonSktEntity.getPekerja());
                        HumanResourcesObject.setExpert(tdrJumlahSdmVendorNonSktEntity.getTenaga_ahli());
                    }
                    
                    TdrReviewVendorNonSktEntity tdrReviewsVendorNonSktEntity = tdrReviewVendorNonSkt.find("where id_identitas = :id_identitas", Parameters.with("id_identitas", vTdrIdentitasVendorNonSkt.getId_identitas())).firstResult();
                    ReviewsData ReviewsObject = new ReviewsData();
                    if(tdrReviewsVendorNonSktEntity != null){
                        ReviewsObject.setTotal_project(Integer.parseInt(tdrReviewsVendorNonSktEntity.getTotal_project()));
                        ReviewsObject.setAverage_rating(Long.parseLong(tdrReviewsVendorNonSktEntity.getAverage_rating()));
                        ReviewsObject.setOntime_rate(Long.parseLong(tdrReviewsVendorNonSktEntity.getOntime_rate()));
                        ReviewsObject.setTotal_revenue(Long.parseLong(tdrReviewsVendorNonSktEntity.getTotal_revenue()));
                    }
                    
                    List<TdrIdentitasVendorMinioNonSktEntity> vTdrIdentitasVendorMinioNonSkt = tdrIdentitasVendorMinioNonSkt.find("where id_pengajuan = :id ", Parameters.with("id", vTdrPengajuanVendorNonSkt.getId_pengajuan())).list();
                    List<LegalDocData> legalDoc = new ArrayList<>();
                    for (TdrIdentitasVendorMinioNonSktEntity e : vTdrIdentitasVendorMinioNonSkt) {
                        TdrMstFileLegalitasEntity file = tdrMstFile.find("where dok = :dok", Parameters.with("dok", e.getDok())).firstResult();
                        
                        if(e.getDok() == 3 || e.getDok() == 12 || e.getDok() == 13 || e.getDok() == 4 || e.getDok() == 6 || e.getDok() == 5 || e.getDok() == 15 || e.getDok() == 8 || e.getDok() == 9 || e.getDok() == 29){
                            LegalDocData docLegal = new LegalDocData();
                            docLegal.setDocument_name(file.getNama_file());
                            if(e.getDok() == 3){
                                docLegal.setDocument_number(vTdrIdentitasVendorNonSkt.getNomor_akta_pendirian());
                                docLegal.setDocument_valid_from(vTdrIdentitasVendorNonSkt.getTgl_terbit_akta_pendirian().toString());
                                docLegal.setDocument_valid_until(vTdrIdentitasVendorNonSkt.getTgl_akta_pendirian().toString());
                            }
            
                            if(e.getDok() == 12){
                                docLegal.setDocument_number(vTdrIdentitasVendorNonSkt.getNomor_akta_pengurus());
                                docLegal.setDocument_valid_from(vTdrIdentitasVendorNonSkt.getTgl_terbit_akta_pengurus().toString());
                                docLegal.setDocument_valid_until(vTdrIdentitasVendorNonSkt.getTgl_akta_pengurus().toString());
                            }
        
                            if(e.getDok() == 8){
                                docLegal.setDocument_number(vTdrIdentitasVendorNonSkt.getNomor_npwp());
                                docLegal.setDocument_valid_from(vTdrIdentitasVendorNonSkt.getTgl_terbit_npwp().toString());
                            }
            
                            if(e.getDok() == 9){
                                docLegal.setDocument_number(vTdrIdentitasVendorNonSkt.getNomor_pkp());
                                docLegal.setDocument_valid_from(vTdrIdentitasVendorNonSkt.getTgl_terbit_pkp().toString());
                                docLegal.setDocument_valid_until(vTdrIdentitasVendorNonSkt.getTgl_pkp().toString());
                            }

                            docLegal.setFile_path(e.getPath_minio());
                            docLegal.setType(file.getIproc_type());
                            Collections.addAll(legalDoc, docLegal);
                        }else{
                            LegalDocData docLegal = new LegalDocData();
                            docLegal.setDocument_name(file.getNama_file());
                            docLegal.setFile_path(e.getPath_minio());
                            docLegal.setType(file.getIproc_type());
                            Collections.addAll(legalDoc, docLegal);
                        }
                        
                        
                    }

                    // System.out.println("before request separator = ");
                    // LOG.info("Before Request non skt begin");
                    Registration regis = new Registration();
                    regis.setEmail(vTdrIdentitasVendorNonSkt.getEmail());
                
                    RegistrationData regData = new RegistrationData();
                    regData.setRegistration(regis);
                    regData.setMain(MainObject);
                    regData.setBidang_usaha(b_usaha_array);
                    regData.setLegal_docs(legalDoc);
                    regData.setBanks(bankArr);
                    regData.setOrganization_structures(organisasiArr);
                    regData.setHuman_resources(HumanResourcesObject);
                    regData.setReviews(ReviewsObject);

                    String urlApiEproc = ConfigProvider.getConfig().getValue("url.registrasi.eproc", String.class);
                    httpRequest hr = new httpRequest();
                    responseConsume res = hr.postData(urlApiEproc, regData);
                    String request = objectMapper.writeValueAsString(regData);
                    String response = objectMapper.writeValueAsString(res);
                    
                    if(res.getData() != null){
                        String resultData = objectMapper.writeValueAsString(res.getData());
                        JsonNode rootNode = objectMapper.readTree(resultData);
                        
                        // TdrPengajuanVendorNonSktEntity tdrNonSktUpdate = TdrPengajuanVendorNonSktEntity.find("id_identitas = ?1", tdrPengajuan.getId_identitas()).firstResult();
                        vTdrPengajuanVendorNonSkt.setStatus_eproc(rootNode.get("code").asText());
                        vTdrPengajuanVendorNonSkt.setMessage_is_eproc(objectMapper.writeValueAsString(res));
                        vTdrPengajuanVendorNonSkt.setIs_eproc(1);
                        tdrPengajuanVendorNonSkt.persist(vTdrPengajuanVendorNonSkt);

                        vTdrIdentitasVendorNonSkt.setIs_minio_identitas_vendor(2);
                        tdrIdentitasVendorNonSkt.persist(vTdrIdentitasVendorNonSkt);

                        TdrLogEprocEntity tdrLogEproc = new TdrLogEprocEntity();
                        tdrLogEproc.setRequest(request);
                        tdrLogEproc.setResponse(response);
                        tdrLogEproc.setCreated_at(LocalDateTime.now());
                        tdrLogEproc.persist();
                    }else{
                        if(res.getMessage() != null){
                            vTdrPengajuanVendorNonSkt.setIs_eproc(1);
                            
                            // TdrPengajuanVendorNonSktEntity tdrNonSktUpdate = TdrPengajuanVendorNonSktEntity.find("id_identitas = ?1", tdrPengajuan.getId_identitas()).firstResult();
                            vTdrPengajuanVendorNonSkt.setStatus_eproc("REG01");
                            vTdrPengajuanVendorNonSkt.setMessage_is_eproc(objectMapper.writeValueAsString(res));
                            tdrPengajuanVendorNonSkt.persist(vTdrPengajuanVendorNonSkt);

                            // tdrNonSktUpdate.persist();
                        }else{
                            vTdrPengajuanVendorNonSkt.setIs_eproc(0);

                            // TdrPengajuanVendorNonSktEntity tdrNonSktUpdate = TdrPengajuanVendorNonSktEntity.find("id_identitas = ?1", tdrPengajuan.getId_identitas()).firstResult();
                            vTdrPengajuanVendorNonSkt.setStatus_eproc("SCH01");
                            vTdrPengajuanVendorNonSkt.setMessage_is_eproc(objectMapper.writeValueAsString(res.getValidation()));
                            tdrPengajuanVendorNonSkt.persist(vTdrPengajuanVendorNonSkt);
                        }
                        
                        TdrLogEprocEntity tdrLogEproc = new TdrLogEprocEntity();
                        tdrLogEproc.setRequest(request);
                        tdrLogEproc.setResponse(response);
                        tdrLogEproc.setCreated_at(LocalDateTime.now());
                        tdrLogEproc.persist();
                        // tdrNonSktUpdate
                    }

                } catch (Exception e) {
                    LOG.error("[ERROR] Failed to process identitas for id_identitas: " + vTdrPengajuanVendorNonSkt.getId_identitas() + " - Error: " + e.getMessage(), e);
                }
            }
            LOG.info("Send Data Eproc Non SKT Finished");
        } catch (Exception e) {
            LOG.error("[ERROR] Failed to process Non SKT vendors - Error: " + e.getMessage(), e);
        }

        LOG.info("==================================================");
        LOG.info("========== SCHEDULER SEND NON SKT END ============");
        LOG.info("==================================================");
    }   
                        

                    
                    
                                
                    
           
                    
           
                    
                                
                                
            //                     // System.out.println(response);
        
            //                     // LOG.info("request Non Skt = "+request);
            //                     // LOG.info("response Non Skt = "+response);
            //                     // LOG.info("Send Data Eproc Non SKT begin");
            //                 }
            //             }else{
            //                 LOG.info("Id Identitas not exists");
            //             }

    public LapkeuDto parseJsonLapkeu(String jsonString) {
        try {

            return objectMapper.readValue(jsonString, LapkeuDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON", e);
        }
    }

    public JsonNode getObjectName(String jsonString){
        try {
            
            return objectMapper.readTree(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ========================================
    // HELPER METHOD: VAR_DUMP
    // ========================================
    /**
     * Helper method untuk print object (seperti var_dump di PHP)
     */
    private void varDump(String label, Object obj) {
        try {
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
            LOG.info("==================== VAR_DUMP: " + label + " ====================");
            LOG.info(json);
            LOG.info("==================== END VAR_DUMP ====================");
        } catch (Exception e) {
            LOG.error("Failed to dump object: " + e.getMessage());
        }
    }
}
