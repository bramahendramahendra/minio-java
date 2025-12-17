package org.acme.scheduler;

import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.acme.controllers.upload_minio.repository.TdrIdentitasVendorMinioRepository;
import org.acme.controllers.upload_minio.repository.TdrIdentitasVendorRepository;
import org.acme.controllers.upload_minio.repository.TdrJumlahSdmVendorRepository;
import org.acme.controllers.upload_minio.repository.TdrMaterialVendorRepository;
import org.acme.controllers.upload_minio.repository.TdrMstBidangUsahaRepository;
import org.acme.controllers.upload_minio.repository.TdrMstFileLegalitasRepository;
import org.acme.controllers.upload_minio.repository.TdrOrganisasiVendorRepository;
import org.acme.controllers.upload_minio.repository.TdrPemegangSahamVendorRepository;
import org.acme.controllers.upload_minio.repository.TdrPengajuanVendorRepository;
import org.acme.controllers.upload_minio.repository.TdrPengalamanVendorMinioRepository;
import org.acme.controllers.upload_minio.repository.TdrPerbankanVendorMinioRepository;
import org.acme.controllers.upload_minio.repository.TdrReviewsVendorRepository;
import org.acme.controllers.upload_minio.repository.TdrSubBidangUsahaRepository;
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

import org.acme.controllers.upload_minio.dto.LapkeuDto;
import org.acme.controllers.upload_minio.entity.TdrIdentitasVendorEntity;
import org.acme.controllers.upload_minio.entity.TdrIdentitasVendorMinioEntity;
import org.acme.controllers.upload_minio.entity.TdrIdentitasVendorMinioNonSktEntity;
import org.acme.controllers.upload_minio.entity.TdrIdentitasVendorNonSktEntity;
import org.acme.controllers.upload_minio.entity.TdrJumlahSdmVendorEntity;
import org.acme.controllers.upload_minio.entity.TdrJumlahSdmVendorNonSktEntity;
import org.acme.controllers.upload_minio.entity.TdrLogEprocEntity;
import org.acme.controllers.upload_minio.entity.TdrMaterialVendorEntity;
import org.acme.controllers.upload_minio.entity.TdrMstBidangUsahaEntity;
import org.acme.controllers.upload_minio.entity.TdrMstFileLegalitasEntity;
import org.acme.controllers.upload_minio.entity.TdrOrganisasiVendorEntity;
import org.acme.controllers.upload_minio.entity.TdrOrganisasiVendorNonSktEntity;
import org.acme.controllers.upload_minio.entity.TdrPemegangSahamVendorEntity;
import org.acme.controllers.upload_minio.entity.TdrPengajuanVendorEntity;
import org.acme.controllers.upload_minio.entity.TdrPengajuanVendorNonSktEntity;
import org.acme.controllers.upload_minio.entity.TdrPengalamanVendorMinioEntity;
import org.acme.controllers.upload_minio.entity.TdrPerbankanVendorMinioEntity;
import org.acme.controllers.upload_minio.entity.TdrPerbankanVendorMinioNonSktEntity;
import org.acme.controllers.upload_minio.entity.TdrReviewVendorNonSktEntity;
import org.acme.controllers.upload_minio.entity.TdrReviewsVendorEntity;
import org.acme.controllers.upload_minio.entity.TdrSendFileEprocStatusEntity;
import org.acme.controllers.upload_minio.entity.TdrSubBidangUsahaEntity;
import org.acme.controllers.upload_minio.enumeration.StatusEprocEnum;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;

@ApplicationScoped
public class IprocScheduled {
    
    @Inject TdrIdentitasVendorRepository tdrIdentitasVendor;

    @Inject TdrPengajuanVendorRepository tdrPengajuanVendor;

    @Inject TdrPengalamanVendorMinioRepository tdrPengalamanVendor;

    @Inject TdrIdentitasVendorMinioRepository tdrIdentitasVendorMinio;

    @Inject TdrPerbankanVendorMinioRepository tdrPerbankanVendorMinio;

    @Inject TdrMstBidangUsahaRepository tdrBidangUsaha;

    @Inject TdrSubBidangUsahaRepository tdrSubBidangUsaha;

    @Inject TdrMaterialVendorRepository tdrMaterialVendor;

    @Inject TdrPemegangSahamVendorRepository tdrPemegangSahamVendor;

    @Inject TdrOrganisasiVendorRepository tdrOrganisasiVendor;

    @Inject TdrReviewsVendorRepository tdrReviewsVendor;

    @Inject TdrJumlahSdmVendorRepository tdrJumlahSdmVendor;

    @Inject TdrMstFileLegalitasRepository tdrMstFile;

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
    public void send_tdr_iproc_skt(){

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
            LOG.info("Send Data Eproc SKT begin");

            List<TdrPengajuanVendorEntity> tdrPengajuanList = TdrPengajuanVendorEntity.find("where is_eproc is null AND status_eproc = ?1 ORDER BY id_pengajuan DESC", "SCH00").page(0, 5).list();
            LOG.info("[DATA] Total Pengajuan vendors SKT found: " + tdrPengajuanList.size());
            if (tdrPengajuanList.isEmpty()) {
                LOG.info("[INFO] No pending vendors to process.");
                LOG.info("Send Data Eproc SKT Finished");
                return;
            }

            int dumpLimit = Math.min(5, tdrPengajuanList.size());
            for (int i = 0; i < dumpLimit; i++) {
                varDump("Vendor SKT [" + i + "]", tdrPengajuanList.get(i));
            }

            for (TdrPengajuanVendorEntity tdrPengajuan : tdrPengajuanList) {
                if (tdrPengajuan == null || tdrPengajuan.getId_identitas() == null || tdrPengajuan.getId_identitas() == 0) {
                    LOG.info("[SKIP] Id Identitas Not Exists for pengajuan: " + (tdrPengajuan != null ? tdrPengajuan.getId_pengajuan() : "NULL"));
                    continue;
                }

                try {
                    TdrIdentitasVendorEntity tdrIdentitas = TdrIdentitasVendorEntity.find("where is_minio_identitas_vendor = ?1 AND id_identitas = ?2", 1, tdrPengajuan.getId_identitas()).firstResult();
                    if (tdrIdentitas == null || tdrIdentitas.getId_identitas() == null) {
                        LOG.warn("[NOT FOUND] Identitas tidak ditemukan untuk id_identitas: " + tdrPengajuan.getId_identitas());
                        LOG.info("[INFO] No pending vendors to process.");
                        LOG.info("Send Data Eproc SKT Finished");
                        return;
                    }

                    varDump("Tdr Pengajuan", tdrPengajuan);
                    varDump("Tdr Identitas", tdrIdentitas);


                    
                } catch (Exception e) {
                    LOG.error("[ERROR] Failed to process identitas for id_identitas: " + tdrPengajuan.getId_identitas() + " - Error: " + e.getMessage(), e);
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

            //                 if(tdrPengajuan != null && tdrPengajuan.getId_pengajuan() != null && tdrPengajuan.getId_vendor_eproc() != null){
            //                     List<TdrPengajuanVendorEntity> tdrPengajuanListCheck = tdrPengajuanVendor.find("where userid = ?1 and is_eproc = ?2 and status_eproc = ?3", p.getUserid(), 1, "REG05").list();
            //                     MainData md = new MainData();
            //                     md.setEmail_address(p.getEmail());
            //                     md.setPengajuan_id(tdrPengajuan.getId_pengajuan());
            //                     md.setStatus_skt("SKT");

            //                     md.setVendor_id_tdr(tdrPengajuan.getId_vendor_eproc());
            //                     if(tdrPengajuanListCheck.size() >= 1){
            //                         LOG.info("RENEWAL");
            //                         md.setJenis_registrasi("RENEWAL"); 
            //                     }else{
            //                         LOG.info("NEW");
            //                         md.setJenis_registrasi("NEW");
            //                     }
            
                                
            //                     md.setVendor_name(p.getNama_vendor());
            //                     md.setBentuk_perusahaan_id((p.getKode_bentuk_usaha() != null)?p.getKode_bentuk_usaha():0);
            //                     md.setBentuk_perusahaan(p.getNama_bentuk_usaha());
            //                     md.setBadan_usaha_id((p.getKode_badan_usaha() != null)?p.getKode_badan_usaha():0);
            //                     md.setBadan_usaha(p.getNama_badan_usaha());
            //                     md.setKegiatan_usaha_id((p.getKode_kegiatan_usaha() != null)?p.getKode_kegiatan_usaha():0);
            //                     md.setKegiatan_usaha(p.getNama_kegiatan_usaha());
            //                     md.setJenis_kegiatan_usaha_id((p.getKode_jenis_kegiatan_usaha() != null)?p.getKode_jenis_kegiatan_usaha():0);
            //                     md.setJenis_kegiatan_usaha(p.getNama_jenis_kegiatan_usaha());
            //                     // md.setCountry_id(11);
            //                     md.setCountry(p.getNama_negara());
            //                     md.setCountry_code(p.getKode_negara());
            //                     md.setProvince_code(p.getKode_provinsi());
            //                     md.setProvince(p.getNama_provinsi());
            //                     md.setCity(p.getKota());
            //                     md.setAddress(p.getAlamat());
            //                     md.setBlock_lot_kavling(p.getBlok_nomor_kavling());
            //                     md.setLongitude(p.getLongitude());
            //                     md.setLatitude(p.getLatitude());
            //                     md.setPostal_code(p.getKodepos());
            //                     md.setOffice_phone_number(p.getNo_telp());
            //                     md.setFax_number(p.getNo_facs());
            //                     md.setWebsite(p.getWebsite());
            //                     md.setCurrency_code(p.getKode_mata_uang());
            //                     md.setDirector_name(p.getNama_dirut());
            //                     md.setVendor_pic(p.getNama_pic());
            //                     md.setPic_phone_number(p.getNo_telp_pic());
                            
                                
                    
                    
            //                     String[] bidang_usaha = (p.getBidang_usaha() != null)?p.getBidang_usaha().split("\\|"):null;
            //                     String[] sub_bidang_usaha = (p.getSub_bidang_usaha() != null)?p.getSub_bidang_usaha().split("\\|"):null;
            //                     String[] material = (p.getMaterial() != null)?p.getMaterial().split("\\|"):null;
            //                     String[] kualifikasi = (p.getKualifikasi() != null)?p.getKualifikasi().split("\\|"):null;
                                
            //                     List<BusinessFieldData> b_usaha_array = new ArrayList<>();
                               
            //                     if(bidang_usaha !=null && sub_bidang_usaha !=null && material != null && kualifikasi != null){

            //                         try{
            //                             for(Integer i = 0; i < bidang_usaha.length; i++){
            //                                 BusinessFieldData b_usaha = new BusinessFieldData();
            //                                 if(bidang_usaha[i] != null || bidang_usaha.equals("")){
            //                                     TdrMstBidangUsahaEntity tdrBidang = tdrBidangUsaha.find("kode = :kode", Parameters.with("kode", bidang_usaha[i])).firstResult();
            //                                     if(tdrBidang != null){
            //                                         b_usaha.setBidang_usaha(tdrBidang.getDeskripsi());
            //                                         b_usaha.setBidang_usaha_kode(bidang_usaha[i]);
            //                                     }
                                                
            //                                 }
                        
            //                                 if(sub_bidang_usaha[i] != null || sub_bidang_usaha[i].equals("")){
                                                
            //                                     TdrSubBidangUsahaEntity tdrSubBidang = tdrSubBidangUsaha.find("kode = :kode", Parameters.with("kode", sub_bidang_usaha[i])).firstResult();
            //                                     if(tdrSubBidang != null){
            //                                         b_usaha.setSub_bidang_usaha_kode(sub_bidang_usaha[i]);
            //                                         b_usaha.setSub_bidang_usaha(tdrSubBidang.getDeskripsi());
            //                                     }
            //                                 }
                                            
            //                                 if(kualifikasi[i] != null || kualifikasi[i].equals("")){
            //                                     // b_usaha.setKualifikasi_id(3);
            //                                     b_usaha.setKualifikasi(kualifikasi[i]);
            //                                 }
            
            //                                 List<MaterialTypeData> materialType = new ArrayList<>();
            //                                 String[] materialString = (material[i] != null)?material[i].split("\\,"):null;
                                            
            //                                 if(materialString != null){
            //                                     for (String materialS : materialString) {
            //                                         if(materialS != null){
            //                                             TdrMaterialVendorEntity tdrMaterialV = tdrMaterialVendor.find("kode = :kode", Parameters.with("kode", materialS)).firstResult();
            //                                             MaterialTypeData mtd = new MaterialTypeData();
            //                                             if(tdrMaterialV != null){
            //                                                 mtd.setJenis_material(tdrMaterialV.getDeskripsi());
            //                                                 mtd.setJenis_material_kode(materialS);
            //                                             }
                                                        
            //                                             Collections.addAll(materialType, mtd);
            //                                         }
                                                    
            //                                     }
            //                                 }
                                            
            //                                 b_usaha.setJenis_material(materialType);
            //                                 Collections.addAll(b_usaha_array,b_usaha);
                                            
            //                             }
            //                         }catch(Exception e){
            //                             LOG.error("Bidang Usaha "+objectMapper.writeValueAsString(b_usaha_array));
            //                         }
                                    
            //                     }else{
            //                         LOG.info("bidang usaha, sub bidang usaha, material, kualifikasi not exist");
            //                     }
            //                     // System.err.println();
                    
                    
            //                     List<ExperienceData> pengalaman = new ArrayList<>();
            //                     List<TdrPengalamanVendorMinioEntity> getTdrPengalaman = tdrPengalamanVendor.find("where id_identitas = :id_identitas", Parameters.with("id_identitas", p.getId_identitas())).list();
                    
            //                     for (TdrPengalamanVendorMinioEntity foreachPengalaman : getTdrPengalaman) {
            //                             ExperienceData exp = new ExperienceData();
            //                             TdrMstBidangUsahaEntity tdrBidangUsahaEntity = tdrBidangUsaha.find("where kode = :kode",Parameters.with("kode", foreachPengalaman.getBidang_usaha())).firstResult();
            //                             TdrSubBidangUsahaEntity tdrSubBidangUsahaEntity = tdrSubBidangUsaha.find("where kode = :kode", Parameters.with("kode", foreachPengalaman.getSub_bidang_usaha())).firstResult();
            //                             exp.setJob_name(foreachPengalaman.getNama_pekerjaan());
            //                             exp.setJob_owner_name(foreachPengalaman.getNama_pemilik_pekerja());
            //                             exp.setBidang_usaha_kode(foreachPengalaman.getBidang_usaha());
            //                             exp.setBidang_usaha(tdrBidangUsahaEntity.getDeskripsi());
            //                             exp.setSub_bidang_usaha(tdrSubBidangUsahaEntity.getDeskripsi());
            //                             exp.setSub_bidang_usaha_kode(foreachPengalaman.getSub_bidang_usaha());
            //                             exp.setAssignor(foreachPengalaman.getPemberi_tugas());
            //                             exp.setSpk_number(foreachPengalaman.getNo_spk());
            //                             exp.setSpk_issue_date(foreachPengalaman.getTgl_mulai_spk().toString());
            //                             exp.setSpk_end_date(foreachPengalaman.getTgl_selesai_spk().toString());
            //                             exp.setContract_value(foreachPengalaman.getNilai_kontrak().longValue());
            //                             exp.setFile_path(foreachPengalaman.getMinio_path());
            //                             Collections.addAll(pengalaman, exp);
                    
            //                     }
                    
            //                     List<BanksData> bank = new ArrayList<>();
            //                     List<TdrPerbankanVendorMinioEntity> tdrPerbankanVendorMinioEntity = tdrPerbankanVendorMinio.find("where id_identitas = :id_identitas", Parameters.with("id_identitas", p.getId_identitas())).list();
            //                     // System.out.println("id_identitas "+ p.getId_identitas());
            //                     for (TdrPerbankanVendorMinioEntity element : tdrPerbankanVendorMinioEntity) {
            //                         // System.out.println("bankers "+element.getKode_bank());
            //                         BanksData bankObject = new BanksData();
            //                         bankObject.setHas_bri_account((element.getHave_account_bri()) ? 1 : 0);
            //                         bankObject.setBank_location(element.getLokasi_bank());
            //                         bankObject.setBank_name(element.getNama_bank());
            //                         bankObject.setCity(element.getCity());
            //                         bankObject.setBank_key(element.getBank_key());
            //                         bankObject.setCountry(element.getNegara());
            //                         bankObject.setCountry_code(element.getKode_negara());
            //                         bankObject.setAccount_number(element.getNo_rek());
            //                         bankObject.setAccount_owner_name(element.getNama_rek());
            //                         bankObject.setSwift_code(element.getSwift_kode());
            //                         bankObject.setBank_branch(element.getCabang_bank());
            //                         bankObject.setFile_path(element.getMinio_path());
            //                         Collections.addAll(bank, bankObject);
            //                     }
                    
            //                     List<OrganizationData> organisasi = new ArrayList<>();
            //                     List<TdrOrganisasiVendorEntity> tdrOrganisasi = tdrOrganisasiVendor.find("where id_identitas = :id_identitas", Parameters.with("id_identitas", p.getId_identitas())).list();
            //                     for (TdrOrganisasiVendorEntity e : tdrOrganisasi) {
            //                         // String[] kewarganegaraan = (e.getKewarganegaraan() != null)?e.getKewarganegaraan().split(" "):null;
            //                         OrganizationData od = new OrganizationData();
            //                         od.setPosition_id(e.getKode_posisi());
            //                         od.setName(e.getNama());
            //                         od.setPosition(e.getPosisi());
            //                         od.setJob_title(e.getJabatan());
            //                         od.setGender(e.getJenis_kelamin());
            //                         od.setNationality(e.getKewarganegaraan());
            //                         // od.setNationality((kewarganegaraan != null)?kewarganegaraan[3].replace("(", "").replace(")", ""):null);
            //                         od.setNpwp(e.getNpwp());
            //                         od.setKtp(e.getKtp_kitas());
            //                         od.setNik(e.getNik());
            //                         od.setDate_of_birth(e.getTgl_lahir().toString());
            //                         Collections.addAll(organisasi, od);
            //                     }
                    
                    
            //                     List<ShareHoldersData> pemegangSaham = new ArrayList<>();
            //                     List<TdrPemegangSahamVendorEntity> tdrPemegangSaham = tdrPemegangSahamVendor.find("where id_identitas = :id_identitas", Parameters.with("id_identitas", p.getId_identitas())).list();
                                
            //                     for (TdrPemegangSahamVendorEntity t : tdrPemegangSaham) {
            //                         // String[] kewarganegaraan = (t.getKewarganegaraan() != null)?t.getKewarganegaraan().split(" "):null;
            //                         ShareHoldersData shd = new ShareHoldersData();
            //                         shd.setTipe(t.getTipe());
            //                         shd.setShareholder_name(t.getNama());
            //                         shd.setPosition(t.getJabatan());
            //                         shd.setNationality(t.getKewarganegaraan());
            //                         // shd.setNationality((kewarganegaraan != null)?kewarganegaraan[3].replace("(", "").replace(")", ""):null);
            //                         shd.setNpwp(t.getNpwp());
            //                         shd.setNik(t.getNik());
            //                         shd.setKtp(t.getKtp_kitas());
            //                         shd.setOwnership_type(t.getJenis_kepemilikan());
            //                         shd.setShare_amount(t.getJumlah_saham().toString());
            //                         shd.setNominal(t.getNominal_saham().intValue());
            //                         shd.setOwnership_percentage(t.getKepemilikan());
            //                         Collections.addAll(pemegangSaham, shd);
            //                     }
                    
                    
            //                     TdrJumlahSdmVendorEntity tdrJumlahSdmEntity = tdrJumlahSdmVendor.find("where id_identitas = :id_identitas", Parameters.with("id_identitas", p.getId_identitas())).firstResult();
            //                     HumanResourcesData hrd = new HumanResourcesData();
            //                     if(tdrJumlahSdmEntity != null){
            //                         hrd.setManager(tdrJumlahSdmEntity.getManajer());
            //                         hrd.setWorker(tdrJumlahSdmEntity.getPekerja());
            //                         hrd.setExpert(tdrJumlahSdmEntity.getTenaga_ahli());
            //                     }
                                
                    
                    
            //                     TdrReviewsVendorEntity tdrReviewsVendorEntity = tdrReviewsVendor.find("where id_identitas = :id_identitas", Parameters.with("id_identitas", p.getId_identitas())).firstResult();
            //                     ReviewsData rd = new ReviewsData();
            //                     if(tdrReviewsVendorEntity != null){
            //                         rd.setTotal_project(Integer.parseInt(tdrReviewsVendorEntity.getTotal_project()));
            //                         rd.setAverage_rating(Long.parseLong(tdrReviewsVendorEntity.getAverage_rating()));
            //                         rd.setOntime_rate(Long.parseLong(tdrReviewsVendorEntity.getOntime_rate()));
            //                         rd.setTotal_revenue(Long.parseLong(tdrReviewsVendorEntity.getTotal_revenue()));
            //                     }
                                
                    
                                
            //                     List<TdrIdentitasVendorMinioEntity> tdrIdentitasVendorM = tdrIdentitasVendorMinio.find("where id_pengajuan = :id ", Parameters.with("id", tdrPengajuan.getId_pengajuan())).list();
            //                     List<LegalDocData> legalDoc = new ArrayList<>();
            //                     for (TdrIdentitasVendorMinioEntity e : tdrIdentitasVendorM) {
            //                         TdrMstFileLegalitasEntity file = tdrMstFile.find("where dok = :dok", Parameters.with("dok", e.getDok())).firstResult();
                                    
            //                         if(e.getDok() == 3 || e.getDok() == 12 || e.getDok() == 13 || e.getDok() == 4 || e.getDok() == 6 || e.getDok() == 5 || e.getDok() == 15 || e.getDok() == 8 || e.getDok() == 9 || e.getDok() == 29){
            //                             LegalDocData docLegal = new LegalDocData();
            //                             docLegal.setDocument_name(file.getNama_file());
            //                             if(e.getDok() == 3){
            //                                 docLegal.setDocument_number(p.getNomor_akta_pendirian());
            //                                 docLegal.setDocument_valid_from(p.getTgl_terbit_akta_pendirian().toString());
            //                                 docLegal.setDocument_valid_until(p.getTgl_akta_pendirian().toString());
            //                             }
                        
            //                             if(e.getDok() == 12){
            //                                 docLegal.setDocument_number(p.getNomor_akta_pengurus());
            //                                 docLegal.setDocument_valid_from(p.getTgl_terbit_akta_pengurus().toString());
            //                                 docLegal.setDocument_valid_until(p.getTgl_akta_pengurus().toString());
            //                             }
            //                             if(e.getDok() == 13){
            //                                 docLegal.setDocument_number(p.getNomor_akta_pemilik());
            //                                 docLegal.setDocument_valid_from(p.getTgl_terbit_akta_pemilik().toString());
            //                                 docLegal.setDocument_valid_until(p.getTgl_akta_pemilik().toString());
            //                             }
                        
            //                             if(e.getDok() == 4){
            //                                 docLegal.setDocument_number(p.getNomor_tanda_daftar());
            //                                 docLegal.setDocument_valid_from(p.getTgl_terbit_tanda_daftar().toString());
            //                                 docLegal.setDocument_valid_until(p.getTgl_tanda_daftar().toString());
            //                             }
            //                             if(e.getDok() == 6){
            //                                 docLegal.setDocument_number(p.getNomor_siup());
            //                                 docLegal.setDocument_valid_from(p.getTgl_terbit_siup().toString());
            //                                 docLegal.setDocument_valid_until(p.getTgl_siup().toString());
            //                             }
                        
            //                             if(e.getDok() == 5){
            //                                 docLegal.setDocument_number(p.getNomor_skdp());
            //                                 docLegal.setDocument_valid_from(p.getTgl_terbit_skdp().toString());
            //                                 docLegal.setDocument_valid_until(p.getTgl_skdp().toString());
            //                             }
            //                             if(e.getDok() == 15){
            //                                 docLegal.setDocument_number(p.getNomor_ijin());
            //                                 docLegal.setDocument_valid_from(p.getTgl_terbit_ijin().toString());
            //                                 docLegal.setDocument_valid_until(p.getTgl_ijin().toString());
            //                             }
                        
            //                             if(e.getDok() == 8){
            //                                 docLegal.setDocument_number(p.getNomor_npwp());
            //                                 docLegal.setDocument_valid_from(p.getTgl_terbit_npwp().toString());
            //                                 docLegal.setDocument_valid_until(p.getTgl_npwp().toString());
            //                             }
                        
            //                             if(e.getDok() == 9){
            //                                 docLegal.setDocument_number(p.getNomor_pkp());
            //                                 docLegal.setDocument_valid_from(p.getTgl_terbit_pkp().toString());
            //                                 docLegal.setDocument_valid_until(p.getTgl_pkp().toString());
            //                             }
                        
            //                             if(e.getDok() == 29){
            //                                 docLegal.setDocument_number(p.getNomor_pengalaman());
            //                                 docLegal.setDocument_valid_from(p.getTgl_terbit_pengalaman().toString());
            //                                 docLegal.setDocument_valid_until(p.getTgl_pengalaman().toString());
            //                             }
            //                             docLegal.setFile_path(e.getPath_minio());
            //                             docLegal.setType(file.getIproc_type());
            //                             Collections.addAll(legalDoc, docLegal);
            //                         }else{
            //                             LegalDocData docLegal = new LegalDocData();
            //                             docLegal.setDocument_name(file.getNama_file());
            //                             docLegal.setFile_path(e.getPath_minio());
            //                             docLegal.setType(file.getIproc_type());
            //                             Collections.addAll(legalDoc, docLegal);
            //                         }
                                    
                                    
            //                     }
            //                     List<CurrentAssetData> aktiva_lancar = new ArrayList<>();
            //                     List<FixedAssetData> aktiva_tetap = new ArrayList<>();
            //                     List<CurrentLiabilityData> kewajiban_lancar = new ArrayList<>();
            //                     List<NoCurrentLiabilityData> kewajiban_tidak_lancar = new ArrayList<>();
            //                     List<EquityData> ekuitas = new ArrayList<>();
            //                     List<LiabilitiesAndEquityData> kewajiban_equitas = new ArrayList<>();
            //                     List<ProfitData> laba = new ArrayList<>();
                            
            //                     if(p.getLap_keu() != null){
        
                                   
            //                         LapkeuDto lapkeu = this.parseJsonLapkeu(p.getLap_keu());
            //                         // Registration regismail = new Registration();
            //                         if(lapkeu != null){
            //                             String[] thn_lapkeu_rur = (lapkeu.getThn_lapkeu_rur() != null)?lapkeu.getThn_lapkeu_rur().split("\\|"):null;
            //                             String[] kas_lapkeu_rur = (lapkeu.getKas_lapkeu_rur() != null)?lapkeu.getKas_lapkeu_rur().split("\\|"):null;
            //                             String[] piutang_rur = (lapkeu.getPiutang_rur() != null)?lapkeu.getPiutang_rur().split("\\|"):null;
            //                             String[] persediaan_rur = (lapkeu.getPersediaan_rur() != null )?lapkeu.getPersediaan_rur().split("\\|"): null;
            //                             String[] pajak_muka_rur = (lapkeu.getPajak_muka_rur() != null)?lapkeu.getPajak_muka_rur().split("\\|"):null;
            //                             String[] biaya_muka_rur = (lapkeu.getBiaya_muka_rur() != null)?lapkeu.getBiaya_muka_rur().split("\\|"):null;
            //                             String[] aktiva_lancar_lain_rur = (lapkeu.getAktiva_lancar_lain_rur() != null)?lapkeu.getAktiva_lancar_lain_rur().split("\\|"):null;
            //                             String[] tot_aktiva_lancar_rur = (lapkeu.getTot_aktiva_lancar_rur() != null)?lapkeu.getTot_aktiva_lancar_rur().split("\\|"):null;
            //                             String[] aset_tetap_rur = (lapkeu.getAset_tetap_rur() != null)?lapkeu.getAset_tetap_rur().split("\\|"): null;
            //                             String[] aktiva_nonlancar_lain_rur = (lapkeu.getAktiva_nonlancar_lain_rur() != null)?lapkeu.getAktiva_nonlancar_lain_rur().split("\\|"):null;
            //                             String[] tot_aktiva_nonlancar_rur = (lapkeu.getTot_aktiva_nonlancar_rur() != null)?lapkeu.getTot_aktiva_nonlancar_rur().split("\\|"):null;
            //                             String[] tot_aktiva_rur = (lapkeu.getTot_aktiva_rur() != null)?lapkeu.getTot_aktiva_rur().split("\\|"):null;
                        
            //                             String[] pinjaman_pendek_rur = (lapkeu.getPinjaman_pendek_rur() != null)?lapkeu.getPinjaman_pendek_rur().split("\\|"):null;
            //                             String[] hutang_bank_lancar_rur = (lapkeu.getHutang_bank_lancar_rur() != null)?lapkeu.getHutang_bank_lancar_rur().split("\\|"):null;
            //                             String[] hutang_usaha_lancar_rur = (lapkeu.getHutang_usaha_lancar_rur() != null)?lapkeu.getHutang_usaha_lancar_rur().split("\\|"):null;
            //                             String[] hutang_pajak_rur = (lapkeu.getHutang_pajak_rur() != null)?lapkeu.getHutang_pajak_rur().split("\\|"):null;
            //                             String[] beban_bayar_rur = (lapkeu.getBeban_bayar_rur() != null)?lapkeu.getBeban_bayar_rur().split("\\|"):null;
            //                             String[] kewajiban_lancar_lain_rur = (lapkeu.getKewajiban_lancar_lain_rur() != null)?lapkeu.getKewajiban_lancar_lain_rur().split("\\|"):null;
            //                             String[] hutang_bank_nonlancar_rur = (lapkeu.getHutang_bank_nonlancar_rur() != null)?lapkeu.getHutang_bank_nonlancar_rur().split("\\|"): null;
            //                             String[] hutang_usaha_nonlancar_rur = (lapkeu.getHutang_usaha_nonlancar_rur() != null)?lapkeu.getHutang_usaha_nonlancar_rur().split("\\|"):null;
            //                             String[] pajak_tangguhan_rur = (lapkeu.getPajak_tangguhan_rur() != null)?lapkeu.getPajak_tangguhan_rur().split("\\|"): null;
            //                             String[] pinjaman_panjang_rur = (lapkeu.getPinjaman_panjang_rur() != null)?lapkeu.getPinjaman_panjang_rur().split("\\|"): null;
            //                             String[] sewa_usaha_rur = (lapkeu.getSewa_usaha_rur() != null)?lapkeu.getSewa_usaha_rur().split("\\|"): null;
                        
            //                             String[] kewajiban_nonlancar_lain_rur = (lapkeu.getKewajiban_nonlancar_lain_rur() != null)?lapkeu.getKewajiban_nonlancar_lain_rur().split("\\|"): null;
            //                             String[] tot_kewajiban_nonlancar_rur = (lapkeu.getTot_aktiva_nonlancar_rur() != null)?lapkeu.getTot_kewajiban_nonlancar_rur().split("\\|"): null;
            //                             String[] tot_kewajiban_rur = (lapkeu.getTot_kewajiban_rur() != null)?lapkeu.getTot_kewajiban_rur().split("\\|"):null;
            //                             String[] modal_rur = (lapkeu.getModal_rur() != null)?lapkeu.getModal_rur().split("\\|"):null;
            //                             String[] laba_berjalan_rur = (lapkeu.getLaba_berjalan_rur() != null)?lapkeu.getLaba_berjalan_rur().split("\\|"):null;
            //                             String[] laba_ditahan_rur = (lapkeu.getLaba_ditahan_rur() != null)?lapkeu.getLaba_ditahan_rur().split("\\|"):null;
            //                             String[] ekuitas_lain_rur = (lapkeu.getEkuitas_lain_rur() != null)?lapkeu.getEkuitas_lain_rur().split("\\|"):null;
            //                             String[] tot_ekuitas_rur = (lapkeu.getTot_ekuitas_rur() != null)?lapkeu.getTot_ekuitas_rur().split("\\|"):null;
            //                             String[] kewajiban_ekuitas_rur = (lapkeu.getKewajiban_ekuitas_rur() != null)?lapkeu.getKewajiban_ekuitas_rur().split("\\|"): null;
            //                             String[] control_balance_rur = (lapkeu.getControl_balance_rur() != null)?lapkeu.getControl_balance_rur().split("\\|"): null;
            //                             String[] pendapatan_rur = (lapkeu.getPendapatan_rur() != null )?lapkeu.getPendapatan_rur().split("\\|"):null;
            //                             String[] tot_kewajiban_lancar = (lapkeu.getTot_kewajiban_lancar_rur() != null )?lapkeu.getTot_kewajiban_lancar_rur().split("\\|"):null;
                        
            //                             String[] laba_rur = (lapkeu.getLaba_rur() != null)?lapkeu.getLaba_rur().split("\\|"):null;
            //                             String[] biaya_rur = (lapkeu.getBiaya_rur() != null)?lapkeu.getBiaya_rur().split("\\|"):null;
                        
                                        

                                        


                        
            //                             for(Integer i = 0; i < thn_lapkeu_rur.length; i++){
            //                                 CurrentAssetData al = new CurrentAssetData();
            //                                 FixedAssetData at = new FixedAssetData();
            //                                 CurrentLiabilityData kl = new CurrentLiabilityData();
            //                                 NoCurrentLiabilityData ketla = new NoCurrentLiabilityData();
            //                                 EquityData eq = new EquityData();
            //                                 LiabilitiesAndEquityData ke = new LiabilitiesAndEquityData();
            //                                 ProfitData lb = new ProfitData();

            //                                 NumberFormat nf = NumberFormat.getInstance(Locale.FRANCE);
                        
            //                                 // al.setKas_dan_bank(Long.parseLong(kas_lapkeu_rur[i].replace(",", "").replace(".", "").substring(kas_lapkeu_rur[i].length() - 2)));
            //                                 // al.setPiutang_usaha(Long.parseLong(piutang_rur[i].replace(",", "").replace(".", "").substring(piutang_rur[i].length() - 2)));
            //                                 // al.setPersediaan(Long.parseLong(persediaan_rur[i].replace(",", "").replace(".", "").substring(persediaan_rur[i].length() - 2)));
            //                                 // al.setPajak_dibayar_muka(Long.parseLong(pajak_muka_rur[i].replace(",", "").replace(".", "").substring(pajak_muka_rur[i].length() - 2)));
            //                                 // al.setBiaya_dibayar_muka(Long.parseLong(biaya_muka_rur[i].replace(",", "").replace(".", "").substring(biaya_muka_rur[i].length() - 2)));
            //                                 // al.setAktiva_lancar_lainnya(Long.parseLong(aktiva_lancar_lain_rur[i].replace(",", "").replace(".", "").substring(aktiva_lancar_lain_rur[i].length() - 2)));
            //                                 // al.setTotal_aktiva_lancar(Long.parseLong(tot_aktiva_lancar_rur[i].replace(",", "").replace(".", "").substring(tot_aktiva_lancar_rur[i].length() - 2)));

            //                                 al.setKas_dan_bank(Long.parseLong(nf.parse(kas_lapkeu_rur[i]).toString()));
            //                                 al.setPiutang_usaha(Long.parseLong(nf.parse(piutang_rur[i]).toString()));
            //                                 al.setPersediaan(Long.parseLong(nf.parse(persediaan_rur[i]).toString()));
            //                                 al.setPajak_dibayar_muka(Long.parseLong(nf.parse(pajak_muka_rur[i]).toString()));
            //                                 al.setBiaya_dibayar_muka(Long.parseLong(nf.parse(biaya_muka_rur[i]).toString()));
            //                                 al.setAktiva_lancar_lainnya(Long.parseLong(nf.parse(aktiva_lancar_lain_rur[i]).toString()));
            //                                 al.setTotal_aktiva_lancar(Long.parseLong(nf.parse(tot_aktiva_lancar_rur[i]).toString()));
            //                                 al.setTahun(Integer.parseInt(thn_lapkeu_rur[i]));
            //                                 Collections.addAll(aktiva_lancar, al);
                        
            //                                 // at.setAsset_tetap(Long.parseLong(aset_tetap_rur[i].replace(",", "").replace(".", "").substring(aset_tetap_rur[i].length() - 2)));
            //                                 // at.setAktiva_tidak_lancar(Long.parseLong(aktiva_nonlancar_lain_rur[i].replace(",", "").replace(".", "").substring(aktiva_nonlancar_lain_rur[i].length() - 2)));
            //                                 // at.setTotal_aktiva(Long.parseLong(tot_aktiva_rur[i].replace(",", "").replace(".", "").substring(tot_aktiva_rur[i].length() - 2)));
            //                                 // at.setTotal_aktiva_tidak_lancar(Long.parseLong(tot_aktiva_nonlancar_rur[i].replace(",", "").replace(".", "").substring(tot_aktiva_nonlancar_rur[i].length() - 2)));
            //                                 // at.setTahun(Integer.parseInt(thn_lapkeu_rur[i].replace(",", "").replace(".", "")));

            //                                 at.setAsset_tetap(Long.parseLong(nf.parse(aset_tetap_rur[i]).toString()));
            //                                 at.setAktiva_tidak_lancar(Long.parseLong(nf.parse(aktiva_nonlancar_lain_rur[i]).toString()));
            //                                 at.setTotal_aktiva(Long.parseLong(nf.parse(tot_aktiva_rur[i]).toString()));
            //                                 at.setTotal_aktiva_tidak_lancar(Long.parseLong(nf.parse(tot_aktiva_nonlancar_rur[i]).toString()));
            //                                 at.setTahun(Integer.parseInt(thn_lapkeu_rur[i]));

            //                                 Collections.addAll(aktiva_tetap, at);
                        
            //                                 kl.setPinjaman_jangka_pendek(Long.parseLong(nf.parse(pinjaman_pendek_rur[i]).toString()));
            //                                 kl.setHutang_bank(Long.parseLong(nf.parse(hutang_bank_lancar_rur[i]).toString()));
            //                                 kl.setHutang_usaha(Long.parseLong(nf.parse(hutang_usaha_lancar_rur[i]).toString()));
            //                                 kl.setHutang_pajak(Long.parseLong(nf.parse(hutang_pajak_rur[i]).toString()));
            //                                 kl.setPinjaman_jangka_pendek(Long.parseLong(nf.parse(pinjaman_pendek_rur[i]).toString()));
            //                                 kl.setBadan_yang_harus_dibayar(Long.parseLong(nf.parse(beban_bayar_rur[i]).toString()));
            //                                 kl.setKewajiban_lancar_lainnya(Long.parseLong(nf.parse(kewajiban_lancar_lain_rur[i]).toString()));
            //                                 kl.setTotal_kewajiban_lancar(Long.parseLong(nf.parse(tot_kewajiban_lancar[i]).toString()));
            //                                 kl.setTahun(Integer.parseInt(thn_lapkeu_rur[i]));
            //                                 Collections.addAll(kewajiban_lancar, kl);
                        
            //                                 ketla.setHutang_bank(Long.parseLong(nf.parse(hutang_bank_nonlancar_rur[i]).toString()));
            //                                 ketla.setHutang_usaha(Long.parseLong(nf.parse(hutang_usaha_nonlancar_rur[i]).toString()));
            //                                 ketla.setKewajiban_pajak_tangguhan(Long.parseLong(nf.parse(pajak_tangguhan_rur[i]).toString()));
            //                                 ketla.setPinjaman_jangka_panjang(Long.parseLong(nf.parse(pinjaman_panjang_rur[i]).toString()));
            //                                 ketla.setHutang_sewa_guna(Long.parseLong(nf.parse(sewa_usaha_rur[i]).toString()));
            //                                 ketla.setKewajiban_tidak_lancar_lainnya(Long.parseLong(nf.parse(kewajiban_nonlancar_lain_rur[i]).toString()));
            //                                 ketla.setTotal_kewajiban_tidak_lancar(Long.parseLong(nf.parse(tot_kewajiban_nonlancar_rur[i]).toString()));
            //                                 ketla.setTotal_kewajiban(Long.parseLong(nf.parse(tot_kewajiban_rur[i]).toString()));
            //                                 ketla.setTahun(Integer.parseInt(thn_lapkeu_rur[i]));
            //                                 Collections.addAll(kewajiban_tidak_lancar, ketla);
                        
                        
                        
            //                                 eq.setModal(Long.parseLong(nf.parse(modal_rur[i]).toString()));
            //                                 eq.setLaba_rugi_tahun_berjalan(Long.parseLong(nf.parse(laba_berjalan_rur[i]).toString()));
            //                                 eq.setLaba_ditahan(Long.parseLong(nf.parse(laba_ditahan_rur[i]).toString()));
            //                                 eq.setEkuitas_lainnya(Long.parseLong(nf.parse(ekuitas_lain_rur[i]).toString()));
            //                                 eq.setTotal_ekuitas(Long.parseLong(nf.parse(tot_ekuitas_rur[i]).toString()));
            //                                 eq.setTahun(Integer.parseInt(thn_lapkeu_rur[i]));
            //                                 Collections.addAll(ekuitas, eq);
                                    
                        
            //                                 ke.setKewajiban_dan_ekuitas(Long.parseLong(nf.parse(kewajiban_ekuitas_rur[i]).toString()));
            //                                 ke.setControl_balance(Long.parseLong(nf.parse(control_balance_rur[i]).toString()));
            //                                 ke.setTahun(Integer.parseInt(thn_lapkeu_rur[i]));
            //                                 Collections.addAll(kewajiban_equitas, ke);
                        
                        
            //                                 lb.setLaba(Long.parseLong(nf.parse(laba_rur[i]).toString()));
            //                                 lb.setBiaya(Long.parseLong(nf.parse(biaya_rur[i]).toString()));
            //                                 lb.setPendapatan(Long.parseLong(nf.parse(pendapatan_rur[i]).toString()));
            //                                 lb.setTahun(Integer.parseInt(thn_lapkeu_rur[i]));
            //                                 Collections.addAll(laba, lb);
            //                             }
        
                                    
            //                         }
        
                                    
            //                         // legalDoc.removeIf(param -> param.getDocument_number() == null);
            //                         // legalDoc.removeIf(param -> param.getDocument_valid_from() == null);
            //                         Registration regis = new Registration();
            //                         regis.setEmail(p.getEmail());
                                
            //                         RegistrationData regData = new RegistrationData();
            //                         regData.setRegistration(regis);;
            //                         regData.setMain(md);
            //                         regData.setBidang_usaha(b_usaha_array);
            //                         regData.setLegal_docs(legalDoc);
            //                         regData.setExperiences(pengalaman);
            //                         regData.setBanks(bank);
            //                         regData.setOrganization_structures(organisasi);
            //                         regData.setShareholders(pemegangSaham);
            //                         regData.setHuman_resources(hrd);
            //                         regData.setReviews(rd);
            //                         regData.setAktiva_lancar(aktiva_lancar);
            //                         regData.setAktiva_tetap(aktiva_tetap);
            //                         regData.setKewajiban_lancar(kewajiban_lancar);
            //                         regData.setKewajiban_tidak_lancar(kewajiban_tidak_lancar);
            //                         regData.setEkuitas(ekuitas);
            //                         regData.setKewajiban_dan_ekuitas(kewajiban_equitas);
            //                         regData.setLaba(laba);
                    
    
            //                         String urlApiEproc = ConfigProvider.getConfig().getValue("url.registrasi.eproc", String.class);
            //                         httpRequest hr = new httpRequest();
            //                         responseConsume res = hr.postData(urlApiEproc, regData);
            //                         String request = objectMapper.writeValueAsString(regData);
            //                         String response = objectMapper.writeValueAsString(res);
        
                                    
                                    
            //                         if(res.getData() != null){
            //                             TdrLogEprocEntity tdrLogEproc = new TdrLogEprocEntity();
            //                             tdrLogEproc.setRequest(request);
            //                             tdrLogEproc.setResponse(response);
            //                             tdrLogEproc.setCreated_at(LocalDateTime.now());
            //                             tdrLogEproc.persist();

            //                             TdrPengajuanVendorEntity tdrPengajuanUpdate = tdrPengajuanVendor.find("id_pengajuan = ?1", tdrPengajuan.getId_pengajuan()).firstResult();
            //                             String resultData = objectMapper.writeValueAsString(res.getData());
            //                             JsonNode rootNode = objectMapper.readTree(resultData);
            //                             tdrPengajuanUpdate.setStatus_eproc(rootNode.get("code").asText());
            //                             tdrPengajuanUpdate.setMessage_is_eproc(objectMapper.writeValueAsString(res));
            //                             tdrPengajuanVendor.persist(tdrPengajuanUpdate); 

            //                             TdrIdentitasVendorEntity tdrIdentitas = tdrIdentitasVendor.find("id_identitas = ?1", p.getId_identitas()).firstResult();
            //                             tdrIdentitas.setIs_minio_identitas_vendor(2);
            //                             tdrIdentitasVendor.persist(tdrIdentitas);
                                        
            //                             tdrPengajuan.setIs_eproc(1);
            //                             tdrPengajuanVendor.persist(tdrPengajuan);
            //                         }else{
            //                             if(res.getMessage() != null){
            //                                 tdrPengajuan.setIs_eproc(1);
            //                                 tdrPengajuanVendor.persist(tdrPengajuan);
            //                                 TdrPengajuanVendorEntity tdrPengajuanUpdate = tdrPengajuanVendor.find("id_pengajuan = ?1", tdrPengajuan.getId_pengajuan()).firstResult();
                                            
            //                                 tdrPengajuanUpdate.setStatus_eproc("REG01");
            //                                 tdrPengajuanUpdate.setMessage_is_eproc(objectMapper.writeValueAsString(res));
            //                                 tdrPengajuanVendor.persist(tdrPengajuanUpdate); 

            //                             }else{
            //                                 tdrPengajuan.setIs_eproc(0);
            //                                 tdrPengajuanVendor.persist(tdrPengajuan);
            //                                 TdrPengajuanVendorEntity tdrPengajuanUpdate = tdrPengajuanVendor.find("id_pengajuan = ?1", tdrPengajuan.getId_pengajuan()).firstResult();
            //                                 tdrPengajuanUpdate.setStatus_eproc("SCH01");
            //                                 tdrPengajuanUpdate.setMessage_is_eproc(objectMapper.writeValueAsString(res.getValidation()));
            //                                 tdrPengajuanVendor.persist(tdrPengajuanUpdate);
            //                             }
                                        


            //                             TdrLogEprocEntity tdrLogEproc = new TdrLogEprocEntity();
            //                             tdrLogEproc.setRequest(request);
            //                             tdrLogEproc.setResponse(response);
            //                             tdrLogEproc.setCreated_at(LocalDateTime.now());
            //                             tdrLogEproc.persist();
            //                         }
        
                                    
        
            //                         // LOG.info("request SKT = "+request);
            //                         // LOG.info("response SKT = "+response);
        
            //                     }else{
            //                         LOG.info("Lapkeu Not Exists");
            //                     }
                                                
            //                 }else{
            //                     LOG.info("Id Pengajuan and ID Vendor Eproc Not Exists");
            //                 }



            //             } catch (Exception e) {
            //                 // TODO: handle exception
            //                 LOG.error(p.getId_identitas()+""+e);
            //             }
                        
    
    
    
    
    
            //         }else{
            //             LOG.info("Id Identitas Not Exists");
            //         }
                        

    // @Scheduled(every = "60s")
    @Transactional
    public void send_tdr_iproc_nonskt(){
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
            LOG.info("Send Data Eproc Non SKT begin");

            List<TdrPengajuanVendorNonSktEntity> tdrPengajuanList = TdrPengajuanVendorNonSktEntity.find("WHERE is_eproc is null AND status_eproc = ?1 ORDER BY id_pengajuan DESC").page(0, 5).list();
            LOG.info("[DATA] Total Pengajuan vendors NON SKT found: " + tdrPengajuanList.size());
            if (tdrPengajuanList.isEmpty()) {
                LOG.info("[INFO] No pending vendors to process.");
                LOG.info("Send Data Eproc Non SKT Finished");
                return;
            }

            int dumpLimit = Math.min(5, tdrPengajuanList.size());
            for (int i = 0; i < dumpLimit; i++) {
                varDump("Vendor NON SKT[" + i + "]", tdrPengajuanList.get(i));
            }
        
            for (TdrPengajuanVendorNonSktEntity tdrPengajuan : tdrPengajuanList) {
                if (tdrPengajuan == null || tdrPengajuan.getId_identitas() == null || tdrPengajuan.getId_identitas() == 0) {
                    LOG.info("[SKIP] Id Identitas Not Exists for pengajuan: " + (tdrPengajuan != null ? tdrPengajuan.getId_pengajuan() : "NULL"));
                    continue;
                } 

                try {
                    TdrIdentitasVendorNonSktEntity tdrIdentitas = TdrIdentitasVendorNonSktEntity.find("where is_minio_identitas_vendor = ?1 AND id_identitas = ?2", 1, tdrPengajuan.getId_identitas()).firstResult();
                   if (tdrIdentitas == null || tdrIdentitas.getId_identitas() == null) {
                        LOG.warn("[NOT FOUND] Identitas tidak ditemukan untuk id_identitas: " + tdrPengajuan.getId_identitas());
                        LOG.info("[INFO] No pending vendors to process.");
                        LOG.info("Send Data Eproc SKT Finished");
                        return;
                    }

                    varDump("Tdr Pengajuan", tdrPengajuan);
                    varDump("Tdr Identitas", tdrIdentitas);

                } catch (Exception e) {
                    LOG.error("[ERROR] Failed to process identitas for id_identitas: " + tdrPengajuan.getId_identitas() + " - Error: " + e.getMessage(), e);
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
                        

            //                     // System.out.println("before request = "+tdrPengajuan);
            //                     MainData md = new MainData();
            //                     md.setEmail_address(p.getEmail());
            //                     md.setPengajuan_id(tdrPengajuan.getId_pengajuan());
            //                     md.setStatus_skt("NON_SKT");
            //                     md.setVendor_id_tdr(tdrPengajuan.getId_vendor_eproc());
            //                     md.setJenis_registrasi("NEW");
            //                     md.setVendor_name(p.getNama_vendor());
            //                     md.setBentuk_perusahaan_id((p.getKode_bentuk_usaha() != null)?p.getKode_bentuk_usaha():0);
            //                     md.setBentuk_perusahaan(p.getNama_bentuk_usaha());
            //                     md.setBadan_usaha_id((p.getKode_badan_usaha() != null)?p.getKode_badan_usaha():0);
            //                     md.setBadan_usaha(p.getNama_badan_usaha());
            //                     md.setKegiatan_usaha_id((p.getKode_kegiatan_usaha() != null)?p.getKode_kegiatan_usaha():0);
            //                     md.setKegiatan_usaha(p.getNama_kegiatan_usaha());
            //                     md.setJenis_kegiatan_usaha_id((p.getKode_jenis_kegiatan_usaha() != null)?p.getKode_jenis_kegiatan_usaha():0);
            //                     md.setJenis_kegiatan_usaha(p.getNama_jenis_kegiatan_usaha());
            //                     // md.setCountry_id(11);
            //                     md.setCountry(p.getNama_negara());
            //                     md.setCountry_code(p.getKode_negara());
            //                     md.setProvince_code(p.getKode_provinsi());
            //                     md.setProvince(p.getNama_provinsi());
            //                     md.setCity(p.getKota());
            //                     md.setAddress(p.getAlamat());
            //                     md.setBlock_lot_kavling(p.getBlok_nomor_kavling());
            //                     md.setLongitude(p.getLongitude());
            //                     md.setLatitude(p.getLatitude());
            //                     md.setPostal_code(p.getKodepos());
            //                     md.setOffice_phone_number(p.getNo_telp());
            //                     md.setFax_number(p.getNo_facs());
            //                     md.setWebsite(p.getWebsite());
            //                     md.setCurrency_code(p.getKode_mata_uang());
            //                     md.setDirector_name(p.getNama_dirut());
            //                     md.setVendor_pic(p.getNama_pic());
            //                     md.setPic_phone_number(p.getNo_telp_pic());
                            
                                
                    
                    
            //                     String[] bidang_usaha = (p.getBidang_usaha() != null)?p.getBidang_usaha().split("\\|"):null;
            //                     String[] sub_bidang_usaha = (p.getSub_bidang_usaha() != null)?p.getSub_bidang_usaha().split("\\|"):null;
            //                     String[] material = (p.getMaterial() != null)?p.getMaterial().split("\\|"):null;
            //                     String[] kualifikasi = (p.getKualifikasi() != null)?p.getKualifikasi().split("\\|"):null;
                                
            //                     List<BusinessFieldData> b_usaha_array = new ArrayList<>();
                    
            //                     if(bidang_usaha !=null && sub_bidang_usaha !=null && material != null && kualifikasi != null){
            //                         for(Integer i = 0; i < bidang_usaha.length; i++){
            //                             BusinessFieldData b_usaha = new BusinessFieldData();
            //                             if(bidang_usaha[i] != null || bidang_usaha.equals("")){
            //                                 TdrMstBidangUsahaEntity tdrBidang = tdrBidangUsaha.find("kode = :kode", Parameters.with("kode", bidang_usaha[i])).firstResult();
            //                                 if(tdrBidang != null){
            //                                     b_usaha.setBidang_usaha(tdrBidang.getDeskripsi());
            //                                     b_usaha.setBidang_usaha_kode(bidang_usaha[i]);
            //                                 }
                                            
            //                             }
                    
            //                             if(sub_bidang_usaha[i] != null || sub_bidang_usaha[i].equals("")){
                                            
            //                                 TdrSubBidangUsahaEntity tdrSubBidang = tdrSubBidangUsaha.find("kode = :kode", Parameters.with("kode", sub_bidang_usaha[i])).firstResult();
            //                                 if(tdrSubBidang != null){
            //                                     b_usaha.setSub_bidang_usaha_kode(sub_bidang_usaha[i]);
            //                                     b_usaha.setSub_bidang_usaha(tdrSubBidang.getDeskripsi());
            //                                 }
            //                             }
                                        
            //                             if(kualifikasi[i] != null || kualifikasi[i].equals("")){
            //                                 b_usaha.setKualifikasi_id(3);
            //                                 b_usaha.setKualifikasi(kualifikasi[i]);
            //                             }
        
            //                             List<MaterialTypeData> materialType = new ArrayList<>();
            //                             String[] materialString = (material[i] != null)?material[i].split("\\,"):null;
                                        
            //                             if(materialString != null){
            //                                 for (String materialS : materialString) {
            //                                     if(materialS != null){
            //                                         TdrMaterialVendorEntity tdrMaterialV = tdrMaterialVendor.find("kode = :kode", Parameters.with("kode", materialS)).firstResult();
            //                                         MaterialTypeData mtd = new MaterialTypeData();
            //                                         if(tdrMaterialV != null){
            //                                             mtd.setJenis_material(tdrMaterialV.getDeskripsi());
            //                                             mtd.setJenis_material_kode(materialS);
            //                                         }
                                                    
            //                                         Collections.addAll(materialType, mtd);
            //                                     }
                                                
            //                                 }
            //                             }
                                        
            //                             b_usaha.setJenis_material(materialType);
            //                             Collections.addAll(b_usaha_array,b_usaha);
                                        
            //                         }
            //                     }
                    
                    
                                
                    
            //                     List<BanksData> bank = new ArrayList<>();
            //                     List<TdrPerbankanVendorMinioNonSktEntity> tdrPerbankanVendorMinioEntity = TdrPerbankanVendorMinioNonSktEntity.find("where id_identitas = :id_identitas", Parameters.with("id_identitas", p.getId_identitas())).list();
            //                     for (TdrPerbankanVendorMinioNonSktEntity element : tdrPerbankanVendorMinioEntity) {
            //                         // System.out.println("bankers "+element.getKode_bank());
            //                         BanksData bankObject = new BanksData();
            //                         bankObject.setHas_bri_account((element.getHave_account_bri()) ? 1 : 0);
            //                         bankObject.setBank_location(element.getLokasi_bank());
            //                         bankObject.setBank_name(element.getNama_bank());
            //                         bankObject.setCity(element.getCity());
            //                         bankObject.setBank_key(element.getBank_key());
            //                         bankObject.setCountry(element.getNegara());
            //                         bankObject.setCountry_code(element.getKode_negara());
            //                         bankObject.setAccount_number(element.getNo_rek());
            //                         bankObject.setAccount_owner_name(element.getNama_rek());
            //                         bankObject.setSwift_code(element.getSwift_kode());
            //                         bankObject.setBank_branch(element.getCabang_bank());
            //                         bankObject.setFile_path(element.getMinio_path());
            //                         Collections.addAll(bank, bankObject);
            //                     }
                    
            //                     List<OrganizationData> organisasi = new ArrayList<>();
            //                     List<TdrOrganisasiVendorNonSktEntity> tdrOrganisasi = TdrOrganisasiVendorNonSktEntity.find("where id_identitas = :id_identitas", Parameters.with("id_identitas", p.getId_identitas())).list();
            //                     for (TdrOrganisasiVendorNonSktEntity e : tdrOrganisasi) {
            //                         // String[] kewarganegaraan = (e.getKewarganegaraan() != null)?e.getKewarganegaraan().split(" "):null;
            //                         OrganizationData od = new OrganizationData();
            //                         od.setPosition_id(e.getKode_posisi());
            //                         od.setName(e.getNama());
            //                         od.setPosition(e.getPosisi());
            //                         od.setJob_title(e.getJabatan());
            //                         od.setGender(e.getJenis_kelamin());
            //                         od.setNationality(e.getKewarganegaraan());
            //                         // od.setNationality((kewarganegaraan != null)?kewarganegaraan[3].replace("(", "").replace(")", ""):null);
            //                         od.setNpwp(e.getNpwp());
            //                         od.setNpp(e.getNpp());
            //                         od.setKtp(e.getKtp_kitas());
            //                         od.setDate_of_birth((e.getTgl_lahir()!=null)?e.getTgl_lahir().toString():null);
            //                         Collections.addAll(organisasi, od);
            //                     }
                    
                    
                                
                    
                    
            //                     TdrJumlahSdmVendorNonSktEntity tdrJumlahSdmEntity = TdrJumlahSdmVendorNonSktEntity.find("where id_identitas = :id_identitas", Parameters.with("id_identitas", p.getId_identitas())).firstResult();
            //                     HumanResourcesData hrd = new HumanResourcesData();
            //                     if(tdrJumlahSdmEntity != null){
            //                         hrd.setManager(tdrJumlahSdmEntity.getManajer());
            //                         hrd.setWorker(tdrJumlahSdmEntity.getPekerja());
            //                         hrd.setExpert(tdrJumlahSdmEntity.getTenaga_ahli());
            //                     }
                                
                    
                    
            //                     TdrReviewVendorNonSktEntity tdrReviewsVendorEntity = TdrReviewVendorNonSktEntity.find("where id_identitas = :id_identitas", Parameters.with("id_identitas", p.getId_identitas())).firstResult();
            //                     ReviewsData rd = new ReviewsData();
            //                     if(tdrReviewsVendorEntity != null){
            //                         rd.setTotal_project(Integer.parseInt(tdrReviewsVendorEntity.getTotal_project()));
            //                         rd.setAverage_rating(Long.parseLong(tdrReviewsVendorEntity.getAverage_rating()));
            //                         rd.setOntime_rate(Long.parseLong(tdrReviewsVendorEntity.getOntime_rate()));
            //                         rd.setTotal_revenue(Long.parseLong(tdrReviewsVendorEntity.getTotal_revenue()));
            //                     }
                                
                    
                                
            //                     List<TdrIdentitasVendorMinioNonSktEntity> tdrIdentitasVendorM = TdrIdentitasVendorMinioNonSktEntity.find("where id_pengajuan = :id ", Parameters.with("id", tdrPengajuan.getId_pengajuan())).list();
            //                     List<LegalDocData> legalDoc = new ArrayList<>();
            //                     for (TdrIdentitasVendorMinioNonSktEntity e : tdrIdentitasVendorM) {
            //                         TdrMstFileLegalitasEntity file = tdrMstFile.find("where dok = :dok", Parameters.with("dok", e.getDok())).firstResult();
                                    
            //                         if(e.getDok() == 3 || e.getDok() == 12 || e.getDok() == 13 || e.getDok() == 4 || e.getDok() == 6 || e.getDok() == 5 || e.getDok() == 15 || e.getDok() == 8 || e.getDok() == 9 || e.getDok() == 29){
            //                             LegalDocData docLegal = new LegalDocData();
            //                             docLegal.setDocument_name(file.getNama_file());
            //                             if(e.getDok() == 3){
            //                                 docLegal.setDocument_number(p.getNomor_akta_pendirian());
            //                                 docLegal.setDocument_valid_from(p.getTgl_terbit_akta_pendirian().toString());
            //                                 docLegal.setDocument_valid_until(p.getTgl_akta_pendirian().toString());
            //                             }
                        
            //                             if(e.getDok() == 12){
            //                                 docLegal.setDocument_number(p.getNomor_akta_pengurus());
            //                                 docLegal.setDocument_valid_from(p.getTgl_terbit_akta_pengurus().toString());
            //                                 docLegal.setDocument_valid_until(p.getTgl_akta_pengurus().toString());
            //                             }
                                        
                        
            //                             if(e.getDok() == 8){
            //                                 docLegal.setDocument_number(p.getNomor_npwp());
            //                                 docLegal.setDocument_valid_from(p.getTgl_terbit_npwp().toString());
            //                             }
                        
            //                             if(e.getDok() == 9){
            //                                 docLegal.setDocument_number(p.getNomor_pkp());
            //                                 docLegal.setDocument_valid_from(p.getTgl_terbit_pkp().toString());
            //                                 docLegal.setDocument_valid_until(p.getTgl_pkp().toString());
            //                             }
            //                             docLegal.setFile_path(e.getPath_minio());
            //                             docLegal.setType(file.getIproc_type());
            //                             Collections.addAll(legalDoc, docLegal);
            //                         }else{
            //                             LegalDocData docLegal = new LegalDocData();
            //                             docLegal.setDocument_name(file.getNama_file());
            //                             docLegal.setFile_path(e.getPath_minio());
            //                             docLegal.setType(file.getIproc_type());
            //                             Collections.addAll(legalDoc, docLegal);
            //                         }
                                    
                                    
            //                     }
        
            //                     // System.out.println("before request separator = ");
            //                     // LOG.info("Before Request non skt begin");
            //                     Registration regis = new Registration();
            //                     regis.setEmail(p.getEmail());
                            
            //                     RegistrationData regData = new RegistrationData();
            //                     regData.setRegistration(regis);;
            //                     regData.setMain(md);
            //                     regData.setBidang_usaha(b_usaha_array);
            //                     regData.setLegal_docs(legalDoc);
            //                     regData.setBanks(bank);
            //                     regData.setOrganization_structures(organisasi);
            //                     regData.setHuman_resources(hrd);
            //                     regData.setReviews(rd);
        
            //                     String urlApiEproc = ConfigProvider.getConfig().getValue("url.registrasi.eproc", String.class);
            //                     httpRequest hr = new httpRequest();
            //                     responseConsume res = hr.postData(urlApiEproc, regData);
            //                     String request = objectMapper.writeValueAsString(regData);
            //                     String response = objectMapper.writeValueAsString(res);
                                
            //                     if(res.getData() != null){
            //                         tdrPengajuan.setIs_eproc(1);
            //                         tdrPengajuan.persist();
            //                         String resultData = objectMapper.writeValueAsString(res.getData());
            //                         JsonNode rootNode = objectMapper.readTree(resultData);
                                    
            //                         TdrPengajuanVendorNonSktEntity tdrNonSktUpdate = TdrPengajuanVendorNonSktEntity.find("id_identitas = ?1", tdrPengajuan.getId_identitas()).firstResult();
            //                         tdrNonSktUpdate.setStatus_eproc(rootNode.get("code").asText());
            //                         tdrNonSktUpdate.setMessage_is_eproc(objectMapper.writeValueAsString(res));
            //                         tdrNonSktUpdate.persist();
            
            //                         TdrLogEprocEntity tdrLogEproc = new TdrLogEprocEntity();
            //                         tdrLogEproc.setRequest(request);
            //                         tdrLogEproc.setResponse(response);
            //                         tdrLogEproc.setCreated_at(LocalDateTime.now());
            //                         tdrLogEproc.persist();
            //                     }else{
            //                         if(res.getMessage() != null){
            //                             tdrPengajuan.setIs_eproc(1);
            //                             tdrPengajuan.persist();
            //                             TdrPengajuanVendorNonSktEntity tdrNonSktUpdate = TdrPengajuanVendorNonSktEntity.find("id_identitas = ?1", tdrPengajuan.getId_identitas()).firstResult();
            //                             tdrNonSktUpdate.setStatus_eproc("REG01");
            //                             tdrNonSktUpdate.setMessage_is_eproc(objectMapper.writeValueAsString(res));
            //                             tdrNonSktUpdate.persist();
            //                         }else{
            //                             TdrPengajuanVendorNonSktEntity tdrNonSktUpdate = TdrPengajuanVendorNonSktEntity.find("id_identitas = ?1", tdrPengajuan.getId_identitas()).firstResult();
            //                             tdrNonSktUpdate.setStatus_eproc("SCH01");
            //                             tdrNonSktUpdate.setMessage_is_eproc(objectMapper.writeValueAsString(res.getValidation()));
            //                             tdrNonSktUpdate.persist();
            //                         }
                                    
                                    

            //                         TdrLogEprocEntity tdrLogEproc = new TdrLogEprocEntity();
            //                         tdrLogEproc.setRequest(request);
            //                         tdrLogEproc.setResponse(response);
            //                         tdrLogEproc.setCreated_at(LocalDateTime.now());
            //                         tdrLogEproc.persist();
            //                         // tdrNonSktUpdate
    
            //                     }
        
                                
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
