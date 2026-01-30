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
import org.acme.controllers.upload_minio.repository.TdrLogEprocRepository;

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

import io.quarkus.arc.Lock;
import io.quarkus.logging.Log;
import io.quarkus.panache.common.Parameters;
import io.quarkus.runtime.Quarkus;
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

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

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
    @Inject TdrLogEprocRepository tdrLogEprocRepository;

    @Inject UploadScheduled uploadScheduled;

    // private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger LOG = Logger.getLogger(IprocScheduled.class);
    private final ObjectMapper objectMapper;

    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private final AtomicLong lastStartTime = new AtomicLong(0);
    private static final long TIMEOUT_MS = 10 * 60 * 1000; // 10 menit timeout
    
    
    // Constructor untuk init ObjectMapper dengan JavaTimeModule
    public IprocScheduled() {
        this.objectMapper = new ObjectMapper();
        // Register module untuk Java 8 Date/Time
        this.objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        // Disable write dates as timestamps (agar lebih readable)
        this.objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    // @Scheduled(every = "60s")
    // @Transactional
    // public void send_tdr_skt(){
    //     // send_tdr_iproc_skt(null);
    //     String idPengajuan = "TDR20251200005";
    //     send_tdr_iproc_skt(idPengajuan);
    // }

    
    // Log Eproc 
    /**
     * Create log eproc dalam transaction terpisah
     * Ini memastikan log langsung ter-commit ke database
     */
    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    public Long createLogEproc(String idPengajuan) {
        try {
            TdrLogEprocEntity tdrLogEproc = new TdrLogEprocEntity();
            tdrLogEproc.setId_pengajuan(idPengajuan);
            tdrLogEproc.setStatus("SCU01");
            tdrLogEproc.setCreated_at(LocalDateTime.now());
            tdrLogEproc.setUpdated_at(LocalDateTime.now());
            tdrLogEproc.persist();
            
            Long idLogEproc = tdrLogEproc.getId_log_eproc();
            LOG.info("[LOG] Created log with id: " + idLogEproc);
            
            return idLogEproc;
        } catch (Exception e) {
            LOG.error("[ERROR] Failed to create log: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Helper method untuk menyimpan log upload
     */
    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    public void saveLogEproc(Long id_log_eproc, String status, String request, String response) {
        if (id_log_eproc == null) {
            LOG.warn("[LOG] Cannot save log - id_log_eproc is null");
            return;
        }
        
        try {
            TdrLogEprocEntity entityLogEproc = tdrLogEprocRepository.findById(id_log_eproc);
            if (entityLogEproc != null) {
                if (request != null) {
                    entityLogEproc.setRequest(request);
                }
                if (response != null) {
                    entityLogEproc.setResponse(response);
                }
                entityLogEproc.setStatus(status);
                entityLogEproc.setUpdated_at(LocalDateTime.now());
                tdrLogEprocRepository.persist(entityLogEproc);
                LOG.info("[LOG] Saved log: " + status);
            } else {
                LOG.warn("[LOG] Log entity not found for id: " + id_log_eproc);
            }
        } catch (Exception e) {
            LOG.error("Failed to save upload log: " + e.getMessage());
        }
    }
    
    // SKT 
    @Scheduled(every = "60s")
    public void send_tdr_skt(){
        long currentTime = System.currentTimeMillis();
        
        // Cek apakah scheduler sedang berjalan
        if (isRunning.get()) {
            long runningTime = currentTime - lastStartTime.get();
            
            // Jika sudah lebih dari timeout, force reset
            if (runningTime > TIMEOUT_MS) {
                LOG.warn("[SCHEDULER] Previous execution timeout after " + (runningTime/1000) + "s, forcing reset");
                isRunning.set(false);
            } else {
                LOG.info("[SCHEDULER] Previous execution still running (" + (runningTime/1000) + "s), skipping");
                return;
            }
        }
        
        // Set flag running
        if (!isRunning.compareAndSet(false, true)) {
            LOG.info("[SCHEDULER] Race condition detected, skipping");
            return;
        }
        
        lastStartTime.set(currentTime);
        
        try {
            LOG.info("[SCHEDULER] Starting execution");
            // String idPengajuan = "TDR20251200005";
            // send_tdr_iproc_skt(idPengajuan);
            send_tdr_iproc_skt(null);
        } finally {
            isRunning.set(false);
            long duration = System.currentTimeMillis() - currentTime;
            LOG.info("[SCHEDULER] Execution completed in " + (duration/1000) + "s");
        }
    }

    /**
     * Method untuk upload berdasarkan id_pengajuan tertentu
     */
    // @Transactional
    public void send_tdr_skt_by_id_pengajuan(String idPengajuan){
        send_tdr_iproc_skt(idPengajuan);
    }
   
    // @Scheduled(every = "60s")
    // @Transactional
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
            // defaul cek is_eproc is null dan status_eproc
             if(specificIdPengajuan != null && !specificIdPengajuan.isEmpty()) {
                // Upload berdasarkan id_pengajuan tertentu
                tdrPengajuanVendorSktList = tdrPengajuanVendorSkt.find("where (is_eproc is null OR is_eproc = 0) AND status_eproc = ?1 AND id_pengajuan = ?2 ORDER BY id_pengajuan DESC", "SCH00", specificIdPengajuan).list();
            } else {
                // Upload scheduled (semua data)
                tdrPengajuanVendorSktList = tdrPengajuanVendorSkt.find("where (is_eproc is null OR is_eproc = 0) AND status_eproc = ?1 ORDER BY id_pengajuan DESC", "SCH00").page(0, 5).list();
            }

            // List<TdrPengajuanVendorEntity> tdrPengajuanList = TdrPengajuanVendorEntity.find("where is_eproc is null AND status_eproc = ?1 ORDER BY id_pengajuan DESC", "SCH00").page(0, 5).list();
            LOG.info("[DATA] Total Pengajuan vendors SKT found: " + tdrPengajuanVendorSktList.size());
            if (tdrPengajuanVendorSktList.isEmpty()) {
                LOG.info("[INFO] No pending vendors to process.");
                LOG.info("Send Data Eproc SKT Finished");
                return;
            }

            // int dumpLimit = Math.min(5, tdrPengajuanVendorSktList.size());
            // for (int i = 0; i < dumpLimit; i++) {
            //     varDump("Vendor SKT [" + i + "]", tdrPengajuanVendorSktList.get(i));
            // }

            // ============================================================
            // LOCK SEMUA DATA SEBELUM DIPROSES
            // ============================================================
            List<String> idPengajuanList = new ArrayList<>();
            for (TdrPengajuanVendorSktEntity vendor : tdrPengajuanVendorSktList) {
                idPengajuanList.add(vendor.getId_pengajuan());
            }

            // =====================================================
            // STEP 1: SET FLAG is_eproc = 2 (PROCESSING)
            // =====================================================
            lockVendorsForProcessing(idPengajuanList);
            
            LOG.info("[LOCK] Locked " + idPengajuanList.size() + " vendors for processing");


            for (TdrPengajuanVendorSktEntity vTdrPengajuanVendorSkt : tdrPengajuanVendorSktList) {
                try {
                    // Panggil method dengan @Transactional per vendor
                    // vTdrPengajuanVendorSkt.setIs_eproc(2); // 2 = PROCESSING
                    // vTdrPengajuanVendorSkt.persist();
                    processOneVendorSkt(vTdrPengajuanVendorSkt);
                } catch (Exception e) {
                    LOG.error("[ERROR] Failed to process vendor: " + vTdrPengajuanVendorSkt.getId_pengajuan() + " - Error: " + e.getMessage(), e);
                    
                    // PENTING: Jika error, reset flag is_eproc kembali ke 0 agar bisa diproses lagi
                    try {
                        resetVendorStatus(vTdrPengajuanVendorSkt.getId_pengajuan(), "SCH02", "Error: " + e.getMessage());
                    } catch (Exception resetError) {
                        LOG.error("[ERROR] Failed to reset vendor status: " + resetError.getMessage());
                    }
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

    /**
     * Lock vendors untuk processing dalam transaction terpisah
     * Ini akan LANGSUNG commit ke database
     */
    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    public void lockVendorsForProcessing(List<String> idPengajuanList) {
        if (idPengajuanList == null || idPengajuanList.isEmpty()) {
            return;
        }
        
        try {
            List<TdrPengajuanVendorSktEntity> vendors = tdrPengajuanVendorSkt.list(
                "id_pengajuan IN ?1 AND (is_eproc IS NULL OR is_eproc = 0)",
                idPengajuanList
            );

            if (vendors.isEmpty()) {
                LOG.warn("[LOCK] No vendors found to lock");
                return;
            }

            for (TdrPengajuanVendorSktEntity vendor : vendors) {
                vendor.setIs_eproc(2); // PROCESSING
            }
            
            LOG.info("[LOCK] Successfully locked " + vendors.size() + " vendors (set is_eproc=2)");
          
        } catch (Exception e) {
            LOG.error("[ERROR] Failed to lock vendors: " + e.getMessage(), e);
            throw new RuntimeException("Failed to lock vendors", e);
        }
    }
       
    /**
     * Process satu vendor SKT dalam satu transaction
     * Method ini menggunakan @Transactional sehingga setiap vendor punya transaction sendiri
     */
    @Transactional
    public void processOneVendorSkt(TdrPengajuanVendorSktEntity vTdrPengajuanVendorSkt) {
        LOG.info("[START] Processing vendor SKT: " + vTdrPengajuanVendorSkt.getId_pengajuan());
        
        // Validasi vendor
        if (vTdrPengajuanVendorSkt == null || vTdrPengajuanVendorSkt.getId_identitas() == null || vTdrPengajuanVendorSkt.getId_identitas() == 0) {
            LOG.info("[SKIP] Id Identitas Not Exists for pengajuan: " + (vTdrPengajuanVendorSkt != null ? vTdrPengajuanVendorSkt.getId_pengajuan() : "NULL"));
            return;
        }

        Long idLogEproc = null;
        String idPengajuan = vTdrPengajuanVendorSkt.getId_pengajuan();

        try {
            // =====================================================
            // STEP 2: BUAT LOG EPROC
            // =====================================================
            // TdrLogEprocEntity tdrLogEproc = new TdrLogEprocEntity();
            // tdrLogEproc.setId_pengajuan(vTdrPengajuanVendorSkt.getId_pengajuan());
            // tdrLogEproc.setStatus("SCU01");
            // tdrLogEproc.setCreated_at(LocalDateTime.now());
            // tdrLogEproc.setUpdated_at(LocalDateTime.now());
            // tdrLogEproc.persist();

            idLogEproc = createLogEproc(vTdrPengajuanVendorSkt.getId_pengajuan());
            if (idLogEproc == null) {
                LOG.error("[ERROR] Failed to create log");
                return;
            }
        
            // Update is_eproc
            // idLogEproc = tdrLogEproc.getId_log_eproc();
            // vTdrPengajuanVendorSkt.setId_log_eproc(idLogEproc);
            // vTdrPengajuanVendorSkt.persist();
            savePengajuanVendor(idPengajuan, null, null, null, idLogEproc);

            // =====================================================
            // STEP 3: GET IDENTITAS
            // =====================================================
            // TdrIdentitasVendorEntity tdrIdentitas = TdrIdentitasVendorEntity.find("where is_minio_identitas_vendor = ?1 AND id_identitas = ?2", 1, tdrPengajuan.getId_identitas()).firstResult();
            TdrIdentitasVendorSktEntity vTdrIdentitasVendorSkt = tdrIdentitasVendorSkt.find("where id_identitas = ?1", vTdrPengajuanVendorSkt.getId_identitas()).firstResult();
            // getIs_minio_identitas_vendor
            if (vTdrIdentitasVendorSkt == null || vTdrIdentitasVendorSkt.getId_identitas() == null) {
                LOG.warn("[NOT FOUND] Identitas tidak ditemukan untuk id_identitas: " + vTdrPengajuanVendorSkt.getId_identitas());
                
                // Set status error
                // vTdrPengajuanVendorSkt.setIs_eproc(0); // Reset ke 0 agar bisa diproses lagi
                // vTdrPengajuanVendorSkt.setStatus_eproc("SCH01"); // Status: Error - Identitas Not Found
                // vTdrPengajuanVendorSkt.setMessage_is_eproc("[NOT FOUND] Identitas tidak ditemukan"); // Status: Error - Identitas Not Found
                // vTdrPengajuanVendorSkt.persist();
                resetVendorStatus(vTdrPengajuanVendorSkt.getId_pengajuan(), "SCH01", "[NOT FOUND] Identitas tidak ditemukan");


                // Log error
                saveLogEproc(idLogEproc, "SCU00", null, "Identitas tidak ditemukan untuk id_identitas: " + vTdrPengajuanVendorSkt.getId_identitas());
                return;
            }

            // varDump("Tdr Pengajuan", vTdrPengajuanVendorSkt);
            // varDump("Tdr Identitas", vTdrIdentitasVendorSkt);

            // =====================================================
            // STEP 4: UPLOAD KE MINIO (JIKA BELUM)
            // =====================================================
            if (vTdrIdentitasVendorSkt.getIs_minio_identitas_vendor() != 1) {
                LOG.info("[UPLOAD] Triggering upload for id_pengajuan: " + vTdrPengajuanVendorSkt.getId_pengajuan());
                
                // Panggil upload
                uploadScheduled.upload_skt_by_id_pengajuan(vTdrPengajuanVendorSkt.getId_pengajuan());

                LOG.info("[UPLOAD] Upload completed for id_pengajuan: " + vTdrPengajuanVendorSkt.getId_pengajuan());
            } else {
                LOG.info("[SKIP] Files already uploaded to MinIO for id_pengajuan: " + vTdrPengajuanVendorSkt.getId_pengajuan());
            }

            // =====================================================
            // STEP 5: Identitas Vendor
            // =====================================================
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

            saveLogEproc(idLogEproc, "SCR02", null, "Main Data built successfully");

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

                    saveLogEproc(idLogEproc, "SCR11", null, "Bidang Usaha built successfully. Total: " + b_usaha_array.size());
                }catch(Exception e){
                    // LOG.error("Bidang Usaha "+objectMapper.writeValueAsString(b_usaha_array));
                    LOG.error("Bidang Usaha error: " + e.getMessage(), e);
                    saveLogEproc(idLogEproc, "SCR00", null, "Bidang Usaha build error: " + e.getMessage());
                    throw e;
                }
            } else{
                LOG.info("bidang usaha, sub bidang usaha, material, kualifikasi not exist");
                saveLogEproc(idLogEproc, "SCR00", null, "Bidang usaha data incomplete or not exist");
            }

            // =====================================================
            // STEP 6: Pengalaman Vendor
            // =====================================================
            List<ExperienceData> pengalamanArr = new ArrayList<>();
            try {
                String queryPengalaman = "SELECT p FROM TdrPengalamanVendorSktEntity p " +
                "LEFT JOIN FETCH p.pengalamanMinioEntity " +
                "LEFT JOIN FETCH p.bidangUsahaEntity  " +
                "LEFT JOIN FETCH p.subBidangUsahaEntity  " +
                "WHERE p.id_identitas = :id_identitas";

                List<TdrPengalamanVendorSktEntity> tdrPengalamanVendoSktList = tdrPengalamanVendorSkt.find(queryPengalaman, Parameters.with("id_identitas", vTdrIdentitasVendorSkt.getId_identitas())).list();
                for (TdrPengalamanVendorSktEntity elementPengalaman : tdrPengalamanVendoSktList) {
                    // TdrPengalamanVendorMinioSktEntity tdrPengalamanVendorMinioSktFirst = tdrPengalamanVendorMinioSkt.find("where id_pengalaman = :id_pengalaman", Parameters.with("id_pengalaman", elementPengalaman.getId_pengalaman())).firstResult();
                    ExperienceData experienceObject = new ExperienceData();
                    // TdrMstBidangUsahaEntity tdrBidangUsahaEntity = tdrBidangUsaha.find("where kode = :kode",Parameters.with("kode", elementPengalaman.getBidang_usaha())).firstResult();
                    // TdrSubBidangUsahaEntity tdrSubBidangUsahaEntity = tdrSubBidangUsaha.find("where kode = :kode", Parameters.with("kode", elementPengalaman.getSub_bidang_usaha())).firstResult();
                    experienceObject.setJob_name(elementPengalaman.getNama_pekerjaan());
                    experienceObject.setJob_owner_name(elementPengalaman.getNama_pemilik_pekerja());
                    experienceObject.setBidang_usaha_kode(elementPengalaman.getBidang_usaha());
                    // experienceObject.setBidang_usaha(tdrBidangUsahaEntity.getDeskripsi());
                    // experienceObject.setSub_bidang_usaha(tdrSubBidangUsahaEntity.getDeskripsi());
                    // experienceObject.setBidang_usaha(elementPengalaman.getBidang_usaha());
                    // experienceObject.setSub_bidang_usaha(elementPengalaman.getSub_bidang_usaha());
                    experienceObject.setSub_bidang_usaha_kode(elementPengalaman.getSub_bidang_usaha());
                    experienceObject.setAssignor(elementPengalaman.getPemberi_tugas());
                    experienceObject.setSpk_number(elementPengalaman.getNo_spk());
                    experienceObject.setSpk_issue_date(elementPengalaman.getTgl_mulai_spk().toString());
                    experienceObject.setSpk_end_date(elementPengalaman.getTgl_selesai_spk().toString());
                    experienceObject.setContract_value(elementPengalaman.getNilai_kontrak().longValue());
                    // experienceObject.setFile_path(tdrPengalamanVendorMinioSktFirst.getMinio_path());
                    // Akses data dari relasi yang sudah di-fetch
                    if (elementPengalaman.getBidangUsahaEntity() != null) {
                        experienceObject.setBidang_usaha(elementPengalaman.getBidangUsahaEntity().getDeskripsi());
                    }
                    
                    if (elementPengalaman.getSubBidangUsahaEntity() != null) {
                        experienceObject.setSub_bidang_usaha(elementPengalaman.getSubBidangUsahaEntity().getDeskripsi());
                    }

                    if (elementPengalaman.getPengalamanMinioEntity() != null) {
                        experienceObject.setFile_path(elementPengalaman.getPengalamanMinioEntity().getMinio_path());
                    }
                    Collections.addAll(pengalamanArr, experienceObject);
                }
                
                saveLogEproc(idLogEproc, "SCR21", null, "Pengalaman Vendor built successfully. Total: " + pengalamanArr.size());
            } catch (Exception e) {
                LOG.error("Pengalaman Vendor error: " + e.getMessage(), e);
                saveLogEproc(idLogEproc, "SCR10", null, "Pengalaman Vendor build error: " + e.getMessage());
                throw e;
            }

            // =====================================================
            // STEP 7: Perbankan Vendor
            // =====================================================
            List<BanksData> bankArr = new ArrayList<>();
            try {
                String queryBank = "SELECT p FROM TdrPerbankanVendorSktEntity p " +
                "LEFT JOIN FETCH p.perbankanMinioEntity " +
                "WHERE p.id_identitas = :id_identitas";
                List<TdrPerbankanVendorSktEntity> tdrPerbankanVendoSktList = tdrPerbankanVendorSkt.find(queryBank, Parameters.with("id_identitas", vTdrIdentitasVendorSkt.getId_identitas())).list();

                // List<TdrPerbankanVendorMinioSktEntity> tdrPerbankanVendorMinioSktEntity = tdrPerbankanVendorMinioSkt.find("where id_identitas = :id_identitas", Parameters.with("id_identitas", vTdrIdentitasVendorSkt.getId_identitas())).list();
                for (TdrPerbankanVendorSktEntity elementPerbankan : tdrPerbankanVendoSktList) {
                    // LOG.info("elementPerbankan: {}", elementPerbankan);
                    // javaLOG.info("=== elementPerbankan DEBUG ===");
                    // LOG.info("ID: " + elementPerbankan.getId());
                    // LOG.info("Nama Bank: " + elementPerbankan.getNama_bank());
                    // LOG.info("No Rek: " + elementPerbankan.getNo_rek());
                    // LOG.info("Nama Rek: " + elementPerbankan.getNama_rek());
                    // LOG.info("Have BRI Account: " + elementPerbankan.getHave_account_bri());
                    // LOG.info("Lokasi Bank: " + elementPerbankan.getLokasi_bank());
                    // LOG.info("City: " + elementPerbankan.getCity());
                    // LOG.info("Negara: " + elementPerbankan.getNegara());
                    // LOG.info("Swift Code: " + elementPerbankan.getSwift_kode());
                    // LOG.info("Minio Entity: " + elementPerbankan.getPerbankanMinioEntity());
                    // if (elementPerbankan.getPerbankanMinioEntity() != null) {
                    //     LOG.info("Minio Path: " + elementPerbankan.getPerbankanMinioEntity().getMinio_path());
                    // }
                    // LOG.info("==============================");

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
                    // bankObject.setFile_path(elementPerbankan.getMinio_path());
                    if (elementPerbankan.getPerbankanMinioEntity() != null) {
                        bankObject.setFile_path(elementPerbankan.getPerbankanMinioEntity().getMinio_path());
                    }
                    Collections.addAll(bankArr, bankObject);
                }

                saveLogEproc(idLogEproc, "SCR31", null, "Perbankan Vendor built successfully. Total: " + bankArr.size());
            } catch (Exception e) {
                LOG.error("Perbankan Vendor error: " + e.getMessage(), e);
                saveLogEproc(idLogEproc, "SCR20", null, "Perbankan Vendor build error: " + e.getMessage());
                throw e;
            }

            // =====================================================
            // STEP 8: Organisasi Vendor
            // =====================================================
            List<OrganizationData> organisasiArr = new ArrayList<>();
            try {
                List<TdrOrganisasiVendorSktEntity> tdrOrganisasiVendorSktEntity = tdrOrganisasiVendorSkt.find("where id_identitas = :id_identitas", Parameters.with("id_identitas", vTdrIdentitasVendorSkt.getId_identitas())).list();
                for (TdrOrganisasiVendorSktEntity elementOrganisasi : tdrOrganisasiVendorSktEntity) {
                    OrganizationData organisasiObject = new OrganizationData();
                    organisasiObject.setPosition_id(elementOrganisasi.getKode_posisi());
                    organisasiObject.setName(elementOrganisasi.getNama());
                    organisasiObject.setPosition(elementOrganisasi.getPosisi());
                    organisasiObject.setJob_title(elementOrganisasi.getJabatan());
                    organisasiObject.setGender(elementOrganisasi.getJenis_kelamin());
                    organisasiObject.setNationality(elementOrganisasi.getKewarganegaraan());
                    organisasiObject.setNik(elementOrganisasi.getNik());
                    organisasiObject.setNpwp(elementOrganisasi.getNpwp());
                    organisasiObject.setKtp(elementOrganisasi.getKtp_kitas());
                    organisasiObject.setDate_of_birth(elementOrganisasi.getTgl_lahir().toString());
                    Collections.addAll(organisasiArr, organisasiObject);
                }
                saveLogEproc(idLogEproc, "SCR41", null, "Organisasi Vendor built successfully. Total: " + organisasiArr.size());
            } catch (Exception e) {
                LOG.error("Organisasi Vendor error: " + e.getMessage(), e);
                saveLogEproc(idLogEproc, "SCR30", null, "Organisasi Vendor build error: " + e.getMessage());
                throw e;
            }

            // =====================================================
            // STEP 9: Pemegang Saham Vendor
            // =====================================================
            List<ShareHoldersData> pemegangSahamArr = new ArrayList<>();
            try {
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
                saveLogEproc(idLogEproc, "SCR51", null, "Pemegang Saham Vendor built successfully. Total: " + pemegangSahamArr.size());
            } catch (Exception e) {
                LOG.error("Pemegang Saham Vendor error: " + e.getMessage(), e);
                saveLogEproc(idLogEproc, "SCR40", null, "Pemegang Saham Vendor build error: " + e.getMessage());
                throw e;
            }

            // =====================================================
            // STEP 10: Jumlah SDM Vendor
            // =====================================================
            HumanResourcesData HumanResourcesObject = new HumanResourcesData();
            try {
                TdrJumlahSdmVendorSktEntity tdrJumlahSdmVendorSktEntity = tdrJumlahSdmVendorSkt.find("where id_identitas = :id_identitas", Parameters.with("id_identitas", vTdrIdentitasVendorSkt.getId_identitas())).firstResult();
                if(tdrJumlahSdmVendorSktEntity != null){
                    HumanResourcesObject.setManager(tdrJumlahSdmVendorSktEntity.getManajer());
                    HumanResourcesObject.setWorker(tdrJumlahSdmVendorSktEntity.getPekerja());
                    HumanResourcesObject.setExpert(tdrJumlahSdmVendorSktEntity.getTenaga_ahli());
                    saveLogEproc(idLogEproc, "SCR61", null, "Jumlah SDM Vendor built successfully");
                } else {
                    saveLogEproc(idLogEproc, "SCR50", null, "Jumlah SDM Vendor not found");
                }
            } catch (Exception e) {
                LOG.error("Jumlah SDM Vendor error: " + e.getMessage(), e);
                saveLogEproc(idLogEproc, "SCR50", null, "Jumlah SDM Vendor build error: " + e.getMessage());
                throw e;
            }
            
            // =====================================================
            // STEP 11: Review Vendor
            // =====================================================
            ReviewsData ReviewsObject = new ReviewsData();
            try {
                TdrReviewVendorSktEntity tdrReviewsVendorSktEntity = tdrReviewVendorSkt.find("where id_identitas = :id_identitas", Parameters.with("id_identitas", vTdrIdentitasVendorSkt.getId_identitas())).firstResult();
                if(tdrReviewsVendorSktEntity != null){
                    ReviewsObject.setTotal_project(Integer.parseInt(tdrReviewsVendorSktEntity.getTotal_project()));
                    ReviewsObject.setAverage_rating(Long.parseLong(tdrReviewsVendorSktEntity.getAverage_rating()));
                    ReviewsObject.setOntime_rate(Long.parseLong(tdrReviewsVendorSktEntity.getOntime_rate()));
                    ReviewsObject.setTotal_revenue(Long.parseLong(tdrReviewsVendorSktEntity.getTotal_revenue()));
                   saveLogEproc(idLogEproc, "SCR71", null, "Review Vendor built successfully");
                } else {
                    saveLogEproc(idLogEproc, "SCR60", null, "Review Vendor not found");
                }
            } catch (Exception e) {
                LOG.error("Review Vendor error: " + e.getMessage(), e);
                saveLogEproc(idLogEproc, "SCR60", null, "Review Vendor build error: " + e.getMessage());
                throw e;
            }

            // =====================================================
            // STEP 12: Legalitas Vendor
            // =====================================================
            List<LegalDocData> legalDoc = new ArrayList<>();
            try {
                List<TdrIdentitasVendorMinioSktEntity> vTdrIdentitasVendorMinioSkt = tdrIdentitasVendorMinioSkt.find("where id_pengajuan = :id ", Parameters.with("id", vTdrPengajuanVendorSkt.getId_pengajuan())).list();
                for (TdrIdentitasVendorMinioSktEntity e : vTdrIdentitasVendorMinioSkt) {
                    TdrMstFileLegalitasEntity file = tdrMstFile.find("where dok = :dok", Parameters.with("dok", e.getDok())).firstResult();
                    
                    if(e.getDok() == 3 || e.getDok() == 12 || e.getDok() == 13 || e.getDok() == 4 || e.getDok() == 6 || e.getDok() == 5 || e.getDok() == 15 || e.getDok() == 8 || e.getDok() == 9 || e.getDok() == 29){
                        LegalDocData docLegal = new LegalDocData();
                        docLegal.setDocument_name(file.getNama_file());
                        if(e.getDok() == 3){
                            docLegal.setDocument_number(vTdrIdentitasVendorSkt.getNomor_akta_pendirian());
                            docLegal.setDocument_valid_from(vTdrIdentitasVendorSkt.getTgl_terbit_akta_pendirian() != null ? vTdrIdentitasVendorSkt.getTgl_terbit_akta_pendirian().toString() : null);
                            docLegal.setDocument_valid_until(vTdrIdentitasVendorSkt.getTgl_akta_pendirian() != null ? vTdrIdentitasVendorSkt.getTgl_akta_pendirian().toString() : null);
                        }
        
                        if(e.getDok() == 12){
                            docLegal.setDocument_number(vTdrIdentitasVendorSkt.getNomor_akta_pengurus());
                            docLegal.setDocument_valid_from(vTdrIdentitasVendorSkt.getTgl_terbit_akta_pengurus() != null ? vTdrIdentitasVendorSkt.getTgl_terbit_akta_pengurus().toString() : null);
                            docLegal.setDocument_valid_until(vTdrIdentitasVendorSkt.getTgl_akta_pengurus() != null ? vTdrIdentitasVendorSkt.getTgl_akta_pengurus().toString() : null);
                        }
                        
                        if(e.getDok() == 13){
                            docLegal.setDocument_number(vTdrIdentitasVendorSkt.getNomor_akta_pemilik());
                            docLegal.setDocument_valid_from(vTdrIdentitasVendorSkt.getTgl_terbit_akta_pemilik() != null ? vTdrIdentitasVendorSkt.getTgl_terbit_akta_pemilik().toString() : null);
                            docLegal.setDocument_valid_until(vTdrIdentitasVendorSkt.getTgl_akta_pemilik() != null ? vTdrIdentitasVendorSkt.getTgl_akta_pemilik().toString() : null);
                        }
        
                        if(e.getDok() == 4){
                            docLegal.setDocument_number(vTdrIdentitasVendorSkt.getNomor_tanda_daftar());
                            docLegal.setDocument_valid_from(vTdrIdentitasVendorSkt.getTgl_terbit_tanda_daftar() != null ? vTdrIdentitasVendorSkt.getTgl_terbit_tanda_daftar().toString() : null);
                            docLegal.setDocument_valid_until(vTdrIdentitasVendorSkt.getTgl_tanda_daftar() != null ? vTdrIdentitasVendorSkt.getTgl_tanda_daftar().toString() : null);
                        }

                        if(e.getDok() == 6){
                            docLegal.setDocument_number(vTdrIdentitasVendorSkt.getNomor_siup());
                            docLegal.setDocument_valid_from(vTdrIdentitasVendorSkt.getTgl_terbit_siup() != null ? vTdrIdentitasVendorSkt.getTgl_terbit_siup().toString() : null);
                            docLegal.setDocument_valid_until(vTdrIdentitasVendorSkt.getTgl_siup() != null ? vTdrIdentitasVendorSkt.getTgl_siup().toString() : null);
                        }
        
                        if(e.getDok() == 5){
                            docLegal.setDocument_number(vTdrIdentitasVendorSkt.getNomor_skdp());
                            docLegal.setDocument_valid_from(vTdrIdentitasVendorSkt.getTgl_terbit_skdp() != null ? vTdrIdentitasVendorSkt.getTgl_terbit_skdp().toString() : null);
                            docLegal.setDocument_valid_until(vTdrIdentitasVendorSkt.getTgl_skdp() != null ? vTdrIdentitasVendorSkt.getTgl_skdp().toString() : null);
                        }
                        if(e.getDok() == 15){
                            docLegal.setDocument_number(vTdrIdentitasVendorSkt.getNomor_ijin());
                            docLegal.setDocument_valid_from(vTdrIdentitasVendorSkt.getTgl_terbit_ijin() != null ? vTdrIdentitasVendorSkt.getTgl_terbit_ijin().toString() : null);
                            docLegal.setDocument_valid_until(vTdrIdentitasVendorSkt.getTgl_ijin() != null ? vTdrIdentitasVendorSkt.getTgl_ijin().toString() : null);
                        }
        
                        if(e.getDok() == 8){
                            docLegal.setDocument_number(vTdrIdentitasVendorSkt.getNomor_npwp());
                            docLegal.setDocument_valid_from(vTdrIdentitasVendorSkt.getTgl_terbit_npwp() != null ? vTdrIdentitasVendorSkt.getTgl_terbit_npwp().toString() : null);
                            docLegal.setDocument_valid_until(vTdrIdentitasVendorSkt.getTgl_npwp() != null ? vTdrIdentitasVendorSkt.getTgl_npwp().toString() : null);
                        }
        
                        if(e.getDok() == 9){
                            docLegal.setDocument_number(vTdrIdentitasVendorSkt.getNomor_pkp());
                            docLegal.setDocument_valid_from(vTdrIdentitasVendorSkt.getTgl_terbit_pkp() != null ? vTdrIdentitasVendorSkt.getTgl_terbit_pkp().toString() : null);
                            docLegal.setDocument_valid_until(vTdrIdentitasVendorSkt.getTgl_pkp() != null ? vTdrIdentitasVendorSkt.getTgl_pkp().toString() : null);
                        }
        
                        if(e.getDok() == 29){
                            docLegal.setDocument_number(vTdrIdentitasVendorSkt.getNomor_pengalaman());
                            docLegal.setDocument_valid_from(vTdrIdentitasVendorSkt.getTgl_terbit_pengalaman() != null ? vTdrIdentitasVendorSkt.getTgl_terbit_pengalaman().toString() : null);
                            docLegal.setDocument_valid_until(vTdrIdentitasVendorSkt.getTgl_pengalaman() != null ? vTdrIdentitasVendorSkt.getTgl_pengalaman().toString() : null);
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
                saveLogEproc(idLogEproc, "SCR81", null, "Legalitas Vendor built successfully. Total: " + legalDoc.size());
            } catch (Exception e) {
                LOG.error("Legalitas Vendor error: " + e.getMessage(), e);
                saveLogEproc(idLogEproc, "SCR70", null, "Legalitas Vendor build error: " + e.getMessage());
                throw e;
            }


            // =====================================================
            // STEP 13: Neraca Keuangan Vendor
            // =====================================================
            if(vTdrIdentitasVendorSkt.getLap_keu() == null) {
                LOG.warn("[SKIP] Lapkeu Not Exists for id_pengajuan: " + vTdrPengajuanVendorSkt.getId_pengajuan());
                
                saveLogEproc(idLogEproc, "SCR80", null, "Lapkeu not exists - skipping financial data");
                
                // Set status error karena lapkeu mandatory
                vTdrPengajuanVendorSkt.setIs_eproc(0); // Reset ke 0 agar bisa diproses lagi
                vTdrPengajuanVendorSkt.setStatus_eproc("SCH01"); // Status: Error - Identitas Not Found
                vTdrPengajuanVendorSkt.setMessage_is_eproc("Laporan keuangan tidak tersedia"); // Status: Error - Identitas Not Found
                // vTdrPengajuanVendorSkt.persist();
                
                return;
            }
        
            List<CurrentAssetData> aktiva_lancar = new ArrayList<>();
            List<FixedAssetData> aktiva_tetap = new ArrayList<>();
            List<CurrentLiabilityData> kewajiban_lancar = new ArrayList<>();
            List<NoCurrentLiabilityData> kewajiban_tidak_lancar = new ArrayList<>();
            List<EquityData> ekuitas = new ArrayList<>();
            List<LiabilitiesAndEquityData> kewajiban_equitas = new ArrayList<>();
            List<ProfitData> laba = new ArrayList<>();
        
            try{
                LapkeuDto lapkeu = this.parseJsonLapkeu(vTdrIdentitasVendorSkt.getLap_keu());
                // Registration regismail = new Registration();
                if(lapkeu == null){
                    LOG.error("[ERROR] Failed to parse Lapkeu JSON");
                    saveLogEproc(idLogEproc, "SCR80", null, "Failed to parse Lapkeu JSON");
                    throw new RuntimeException("Failed to parse Lapkeu JSON");
                }

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
                
                saveLogEproc(idLogEproc, "SCR91", null, "Neraca Keuangan built successfully for " + thn_lapkeu_rur.length + " years");
            } catch (Exception e) {
                LOG.error("Neraca Keuangan error: " + e.getMessage(), e);
                saveLogEproc(idLogEproc, "SCR80", null, "Neraca Keuangan build error: " + e.getMessage());
                throw e;
            }

            // =====================================================
            // STEP 14: BUILD REGISTRATION OBJECT & SEND TO EPROC
            // =====================================================
            try {
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

                String request = objectMapper.writeValueAsString(regData);
                saveLogEproc(idLogEproc, "SCR92", request, "Sending data to E-Proc API");
            
                LOG.info("[API] Sending to E-Proc API");
                String urlApiEproc = ConfigProvider.getConfig().getValue("url.registrasi.eproc", String.class);
                httpRequest hr = new httpRequest();
                responseConsume res = hr.postData(urlApiEproc, regData);
                String response = objectMapper.writeValueAsString(res);

                // =====================================================
                // STEP 15: HANDLE RESPONSE FROM EPROC
                // =====================================================
                if(res.getData() != null){
                    String resultData = objectMapper.writeValueAsString(res.getData());
                    JsonNode rootNode = objectMapper.readTree(resultData);

                    // vTdrPengajuanVendorSkt.setIs_eproc(1);
                    // vTdrPengajuanVendorSkt.setStatus_eproc(rootNode.get("code").asText());
                    // vTdrPengajuanVendorSkt.setMessage_is_eproc(response);
                    // tdrPengajuanVendorSkt.persist(vTdrPengajuanVendorSkt);
                    savePengajuanVendor(idPengajuan, 1, rootNode.get("code").asText(), response, null);


                    // TdrIdentitasVendorEntity tdrIdentitasUpdate = tdrIdentitasVendor.find("id_identitas = ?1", p.getId_identitas()).firstResult();
                    // vTdrIdentitasVendorSkt.setIs_minio_identitas_vendor(2);
                    // tdrIdentitasVendorSkt.persist(vTdrIdentitasVendorSkt);
                    // vTdrIdentitasVendorSkt.persist();

                    // TdrLogEprocEntity tdrLogEproc = new TdrLogEprocEntity();
                    // tdrLogEproc.setRequest(request);
                    // tdrLogEproc.setResponse(response);
                    // tdrLogEproc.setCreated_at(LocalDateTime.now());
                    // tdrLogEproc.persist();

                    saveLogEproc(idLogEproc, "SCR93", request, response);
                    LOG.info("[SUCCESS] Data sent to E-Proc successfully for id_pengajuan: " + vTdrPengajuanVendorSkt.getId_pengajuan());
                } else{
                    if(res.getMessage() != null){
                        // vTdrPengajuanVendorSkt.setIs_eproc(1);
                        // TdrPengajuanVendorSktEntity tdrPengajuanUpdate = tdrPengajuanVendorSkt.find("id_pengajuan = ?1", vTdrPengajuanVendorSkt.getId_pengajuan()).firstResult();
                        // vTdrPengajuanVendorSkt.setStatus_eproc("REG01");
                        // vTdrPengajuanVendorSkt.setMessage_is_eproc(response);
                        // tdrPengajuanVendorSkt.persist(vTdrPengajuanVendorSkt);

                        // tdrPengajuanVendor.persist(tdrPengajuanUpdate); 

                        // vTdrPengajuanVendorSkt.persist();

                        savePengajuanVendor(idPengajuan, 1, "REG01", response, null);

                        saveLogEproc(idLogEproc, "SCR94", request, response);
                        LOG.warn("[WARNING] E-Proc returned message: " + res.getMessage());
                        
                    }else{
                        // vTdrPengajuanVendorSkt.setIs_eproc(0);
                        // // TdrPengajuanVendorSktEntity tdrPengajuanUpdate = tdrPengajuanVendorSkt.find("id_pengajuan = ?1", vTdrPengajuanVendorSkt.getId_pengajuan()).firstResult();
                        // vTdrPengajuanVendorSkt.setStatus_eproc("SCH01");
                        // vTdrPengajuanVendorSkt.setMessage_is_eproc(objectMapper.writeValueAsString(res.getValidation()));
                        // tdrPengajuanVendorSkt.persist(vTdrPengajuanVendorSkt);
                        // tdrPengajuanVendor.persist(tdrPengajuanUpdate);

                        savePengajuanVendor(idPengajuan, 0, "SCH01", objectMapper.writeValueAsString(res.getValidation()), null);

                        // vTdrPengajuanVendorSkt.persist();

                        saveLogEproc(idLogEproc, "SCR90", request, response);
                        LOG.error("[ERROR] E-Proc validation failed");
                    }
                    // saveLogEproc(vTdrPengajuanVendorSkt.getId_log_eproc(), "SCE71", request, response);
                }

            } catch (Exception e) {
                LOG.error("Failed to send to E-Proc: " + e.getMessage(), e);
                saveLogEproc(idLogEproc, "SCR90", null, "Failed to send to E-Proc: " + e.getMessage());
                throw e;
            }
                // LOG.info("request SKT = "+request);
                // LOG.info("response SKT = "+response);
            // }else{
            //     LOG.info("Lapkeu Not Exists");
            // }
        // } catch (Exception e) {
        //     LOG.error("[ERROR] Failed to process identitas for id_identitas: " + vTdrPengajuanVendorSkt.getId_identitas() + " - Error: " + e.getMessage(), e);
        //     // Save error log
        //     if(idLogEproc != null) {
        //         saveLogEproc(idLogEproc, "SCE999", null, "Processing failed: " + e.getMessage());
        //     }

        //     throw e;
        // } 
        } catch (Exception e) {
            LOG.error("[ERROR] Failed to process vendor: " + vTdrPengajuanVendorSkt.getId_pengajuan() + " - Error: " + e.getMessage(), e);
            
            // Save error log
            if(idLogEproc != null) {
                saveLogEproc(idLogEproc, "SCR90", null, "Processing failed: " + e.getMessage());
            }

            // Update status sebelum throw
            try {
                // vTdrPengajuanVendorSkt.setIs_eproc(0);
                // vTdrPengajuanVendorSkt.setStatus_eproc("SCH01");
                // vTdrPengajuanVendorSkt.setMessage_is_eproc("Processing error: " + e.getMessage());
                // vTdrPengajuanVendorSkt.persist();
                savePengajuanVendor(idPengajuan, 0, "SCH01", "Processing error: " + e.getMessage(), null);

            } catch (Exception updateEx) {
                LOG.error("[ERROR] Failed to update vendor status: " + updateEx.getMessage());
            }
            
            // Wrap dalam RuntimeException (unchecked exception)
            throw new RuntimeException("Failed to process vendor: " + vTdrPengajuanVendorSkt.getId_pengajuan(), e);
        }
    }


    
    /**
     * Helper method untuk menyimpan log upload
     */
    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    public void savePengajuanVendor(String id_pengajuan, Integer is_eproc, String status_eproc, String message_is_eproc, Long id_log_eproc) {
        if (id_pengajuan == "") {
            LOG.warn("[PENGAJUAN] Cannot save pengajuan SKT - id pengajuan is null");
            return;
        }
        
        try {
            TdrPengajuanVendorSktEntity entityPengajuanSKt = tdrPengajuanVendorSkt.find("where id_pengajuan = ?1",id_pengajuan).firstResult();
            if (entityPengajuanSKt != null) {
                if (is_eproc != null) {
                    entityPengajuanSKt.setIs_eproc(is_eproc);
                }
                if (status_eproc != null) {
                    entityPengajuanSKt.setStatus_eproc(status_eproc);
                }
                if (message_is_eproc != null) {
                    entityPengajuanSKt.setMessage_is_eproc(message_is_eproc);
                }
                if (id_log_eproc != null) {
                    entityPengajuanSKt.setId_log_eproc(id_log_eproc);
                }
                tdrPengajuanVendorSkt.persist(entityPengajuanSKt);
                LOG.info("[LOG] Saved pengajuan SKT: " + status_eproc);
            } else {
                LOG.warn("[LOG] Pengajuan SKT entity not found for id pengajuan: " + id_pengajuan);
            }
        } catch (Exception e) {
            LOG.error("Failed to save upload pengajuan SKT: " + e.getMessage());
        }
    }

    /**
     * Method untuk reset status vendor jika terjadi error di luar transaction
     * Ini dipanggil dari catch block di send_tdr_iproc_skt
     */
    @Transactional
    public void resetVendorStatus(String idPengajuan, String statusEproc, String errorMessage) {
        try {
            TdrPengajuanVendorSktEntity vendor = tdrPengajuanVendorSkt.find(
                "id_pengajuan = ?1", 
                idPengajuan
            ).firstResult();
            
            if (vendor != null) {
                vendor.setIs_eproc(0); // Reset ke 0 agar bisa retry
                vendor.setStatus_eproc(statusEproc);
                vendor.setMessage_is_eproc(errorMessage);
                vendor.persist();
                
                LOG.info("[RESET] Reset is_eproc = 0 for id_pengajuan: " + idPengajuan + " - Error: " + errorMessage);
            }
        } catch (Exception e) {
            LOG.error("[ERROR] Failed to reset vendor status: " + e.getMessage());
        }
    }

    // Non SKT 
    @Scheduled(every = "60s")
    public void send_tdr_nonskt(){
        long currentTime = System.currentTimeMillis();
        
        // Cek apakah scheduler sedang berjalan
        if (isRunning.get()) {
            long runningTime = currentTime - lastStartTime.get();
            
            // Jika sudah lebih dari timeout, force reset
            if (runningTime > TIMEOUT_MS) {
                LOG.warn("[SCHEDULER] Previous execution timeout after " + (runningTime/1000) + "s, forcing reset");
                isRunning.set(false);
            } else {
                LOG.info("[SCHEDULER] Previous execution still running (" + (runningTime/1000) + "s), skipping");
                return;
            }
        }
        
        // Set flag running
        if (!isRunning.compareAndSet(false, true)) {
            LOG.info("[SCHEDULER] Race condition detected, skipping");
            return;
        }
        
        lastStartTime.set(currentTime);
        
        try {
            LOG.info("[SCHEDULER] Starting execution");
            // String idPengajuan = "TDR20251200005";
            // send_tdr_iproc_skt(idPengajuan);
            send_tdr_iproc_nonskt(null);
        } finally {
            isRunning.set(false);
            long duration = System.currentTimeMillis() - currentTime;
            LOG.info("[SCHEDULER] Execution completed in " + (duration/1000) + "s");
        }
    }

    /**
     * Method untuk upload berdasarkan id_pengajuan tertentu
     */
    // @Transactional
    public void send_tdr_nonskt_by_id_pengajuan(String idPengajuan){
        send_tdr_iproc_skt(idPengajuan);
    }

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
                tdrPengajuanVendorNonSktList = tdrPengajuanVendorNonSkt.find("where (is_eproc is null OR is_eproc = 0) AND status_eproc = ?1 AND id_pengajuan = ?2 ORDER BY id_pengajuan DESC", "SCH00", specificIdPengajuan).list();
            } else {
                // Upload scheduled (semua data)
                tdrPengajuanVendorNonSktList = tdrPengajuanVendorNonSkt.find("where (is_eproc is null OR is_eproc = 0) AND status_eproc = ?1 ORDER BY id_pengajuan DESC", "SCH00").page(0, 5).list();
            }

            LOG.info("[DATA] Total Pengajuan vendors NON SKT found: " + tdrPengajuanVendorNonSktList.size());
            if (tdrPengajuanVendorNonSktList.isEmpty()) {
                LOG.info("[INFO] No pending vendors to process.");
                LOG.info("Send Data Eproc Non SKT Finished");
                return;
            }

            // int dumpLimit = Math.min(5, tdrPengajuanVendorNonSktList.size());
            // for (int i = 0; i < dumpLimit; i++) {
            //     varDump("Vendor NON SKT[" + i + "]", tdrPengajuanVendorNonSktList.get(i));
            // }

            // ============================================================
            // LOCK SEMUA DATA SEBELUM DIPROSES
            // ============================================================
            List<String> idPengajuanList = new ArrayList<>();
            for (TdrPengajuanVendorNonSktEntity vendor : tdrPengajuanVendorNonSktList) {
                idPengajuanList.add(vendor.getId_pengajuan());
            }

            // =====================================================
            // STEP 1: SET FLAG is_eproc = 2 (PROCESSING)
            // =====================================================
            lockVendorsNonSktForProcessing(idPengajuanList);
            
            LOG.info("[LOCK] Locked " + idPengajuanList.size() + " vendors for processing");

            for (TdrPengajuanVendorNonSktEntity vTdrPengajuanVendorNonSkt : tdrPengajuanVendorNonSktList) {
                try {
                    processOneVendorNonSkt(vTdrPengajuanVendorNonSkt);
                } catch (Exception e) {
                    LOG.error("[ERROR] Failed to process vendor: " + vTdrPengajuanVendorNonSkt.getId_pengajuan() + " - Error: " + e.getMessage(), e);
                    
                    // PENTING: Jika error, reset flag is_eproc kembali ke 0 agar bisa diproses lagi
                    try {
                        resetVendorNonSktStatus(vTdrPengajuanVendorNonSkt.getId_pengajuan(), "SCH02", "Error: " + e.getMessage());
                    } catch (Exception resetError) {
                        LOG.error("[ERROR] Failed to reset vendor status: " + resetError.getMessage());
                    }
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

    /**
     * Lock vendors untuk processing dalam transaction terpisah
     * Ini akan LANGSUNG commit ke database
     */
    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    public void lockVendorsNonSktForProcessing(List<String> idPengajuanList) {
        if (idPengajuanList == null || idPengajuanList.isEmpty()) {
            return;
        }
        
        try {
            List<TdrPengajuanVendorNonSktEntity> vendors = tdrPengajuanVendorNonSkt.list(
                "id_pengajuan IN ?1 AND (is_eproc IS NULL OR is_eproc = 0)",
                idPengajuanList
            );

            if (vendors.isEmpty()) {
                LOG.warn("[LOCK] No vendors found to lock");
                return;
            }

            for (TdrPengajuanVendorNonSktEntity vendor : vendors) {
                vendor.setIs_eproc(2); // PROCESSING
            }
            
            LOG.info("[LOCK] Successfully locked " + vendors.size() + " vendors (set is_eproc=2)");
          
        } catch (Exception e) {
            LOG.error("[ERROR] Failed to lock vendors: " + e.getMessage(), e);
            throw new RuntimeException("Failed to lock vendors", e);
        }
    }

    /**
     * Process satu vendor Non SKT dalam satu transaction
     * Method ini menggunakan @Transactional sehingga setiap vendor punya transaction sendiri
     */
    @Transactional
    public void processOneVendorNonSkt(TdrPengajuanVendorNonSktEntity vTdrPengajuanVendorNonSkt) {
        LOG.info("[START] Processing vendor Non SKT: " + vTdrPengajuanVendorNonSkt.getId_pengajuan());
    
        if (vTdrPengajuanVendorNonSkt == null || vTdrPengajuanVendorNonSkt.getId_identitas() == null || vTdrPengajuanVendorNonSkt.getId_identitas() == 0) {
            LOG.info("[SKIP] Id Identitas Not Exists for pengajuan: " + (vTdrPengajuanVendorNonSkt != null ? vTdrPengajuanVendorNonSkt.getId_pengajuan() : "NULL"));
            return;
        } 

        Long idLogEproc = null;
        String idPengajuan = vTdrPengajuanVendorNonSkt.getId_pengajuan();

        try {
            // =====================================================
            // STEP 2: BUAT LOG EPROC
            // =====================================================
             idLogEproc = createLogEproc(vTdrPengajuanVendorNonSkt.getId_pengajuan());
            if (idLogEproc == null) {
                LOG.error("[ERROR] Failed to create log");
                return;
            }

            // Update is_eproc
            savePengajuanVendorNonSkt(idPengajuan, null, null, null, idLogEproc);

            // TdrIdentitasVendorNonSktEntity tdrIdentitas = TdrIdentitasVendorNonSktEntity.find("where is_minio_identitas_vendor = ?1 AND id_identitas = ?2", 1, tdrPengajuan.getId_identitas()).firstResult();
            TdrIdentitasVendorNonSktEntity vTdrIdentitasVendorNonSkt = tdrIdentitasVendorNonSkt.find("where id_identitas = ?1", vTdrPengajuanVendorNonSkt.getId_identitas()).firstResult();
            // getIs_minio_identitas_vendor
            if (vTdrIdentitasVendorNonSkt == null || vTdrIdentitasVendorNonSkt.getId_identitas() == null) {
                LOG.warn("[NOT FOUND] Identitas tidak ditemukan untuk id_identitas: " + vTdrPengajuanVendorNonSkt.getId_identitas());
                
                resetVendorNonSktStatus(vTdrPengajuanVendorNonSkt.getId_pengajuan(), "SCH01", "[NOT FOUND] Identitas tidak ditemukan");

                 // Log error
                saveLogEproc(idLogEproc, "SCU00", null, "Identitas tidak ditemukan untuk id_identitas: " + vTdrPengajuanVendorNonSkt.getId_identitas());
                return;
            }

            // varDump("Tdr Pengajuan", vTdrPengajuanVendorNonSkt);
            // varDump("Tdr Identitas", vTdrIdentitasVendorNonSkt);

            // =====================================================
            // STEP 4: UPLOAD KE MINIO (JIKA BELUM)
            // =====================================================
            if (vTdrIdentitasVendorNonSkt.getIs_minio_identitas_vendor() != 1) {
                LOG.info("[UPLOAD] Triggering upload for id_pengajuan: " + vTdrPengajuanVendorNonSkt.getId_pengajuan());

                // Panggil upload
                uploadScheduled.upload_non_skt_by_id_pengajuan(vTdrPengajuanVendorNonSkt.getId_pengajuan());
                LOG.info("[UPLOAD] Upload completed for id_pengajuan: " + vTdrPengajuanVendorNonSkt.getId_pengajuan());
            } else {
                LOG.info("[SKIP] Files already uploaded to Minio for id_pengajuan: " + vTdrPengajuanVendorNonSkt.getId_pengajuan());
            }
            
            // =====================================================
            // STEP 5: Identitas Vendor
            // =====================================================
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

            saveLogEproc(idLogEproc, "SCR02", null, "Main Data built successfully");

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

                    saveLogEproc(idLogEproc, "SCR11", null, "Bidang Usaha built successfully. Total: " + b_usaha_array.size());
                }catch(Exception e){
                    LOG.error("Bidang Usaha error: " + e.getMessage(), e);
                    saveLogEproc(idLogEproc, "SCR00", null, "Bidang Usaha build error: " + e.getMessage());
                    throw e;
                }
            } else{
                LOG.info("bidang usaha, sub bidang usaha, material, kualifikasi not exist");
                saveLogEproc(idLogEproc, "SCR00", null, "Bidang usaha data incomplete or not exist");
            }

            // =====================================================
            // STEP 7: Perbankan Vendor
            // =====================================================
            List<BanksData> bankArr = new ArrayList<>();
            try {
                 String queryBank = "SELECT p FROM TdrPerbankanVendorNonSktEntity p " +
                "LEFT JOIN FETCH p.perbankanMinioNonSktEntity " +
                "WHERE p.id_identitas = :id_identitas";
                List<TdrPerbankanVendorNonSktEntity> tdrPerbankanVendorNonSktList = tdrPerbankanVendorNonSkt.find(queryBank, Parameters.with("id_identitas", vTdrIdentitasVendorNonSkt.getId_identitas())).list();

                // List<TdrPerbankanVendorMinioNonSktEntity> tdrPerbankanVendorMinioNonSktEntity = tdrPerbankanVendorMinioNonSkt.find("where id_identitas = :id_identitas", Parameters.with("id_identitas", vTdrIdentitasVendorNonSkt.getId_identitas())).list();
             
                for (TdrPerbankanVendorNonSktEntity elementPerbankan : tdrPerbankanVendorNonSktList) {
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
                    // bankObject.setFile_path(elementPerbankan.getMinio_path());
                    if (elementPerbankan.getPerbankanMinioNonSktEntity() != null) {
                        bankObject.setFile_path(elementPerbankan.getPerbankanMinioNonSktEntity().getMinio_path());
                    }
                    Collections.addAll(bankArr, bankObject);
                }

                saveLogEproc(idLogEproc, "SCR31", null, "Perbankan Vendor built successfully. Total: " + bankArr.size());
            } catch (Exception e) {
                LOG.error("Perbankan Vendor error: " + e.getMessage(), e);
                saveLogEproc(idLogEproc, "SCR20", null, "Perbankan Vendor build error: " + e.getMessage());
                throw e;
            }

            // =====================================================
            // STEP 8: Organisasi Vendor
            // =====================================================
            List<OrganizationData> organisasiArr = new ArrayList<>();
            try {
                List<TdrOrganisasiVendorNonSktEntity> tdrOrganisasiVendorNonSktEntity = tdrOrganisasiVendorNonSkt.find("where id_identitas = :id_identitas", Parameters.with("id_identitas", vTdrIdentitasVendorNonSkt.getId_identitas())).list();
                for (TdrOrganisasiVendorNonSktEntity elementOrganisasi : tdrOrganisasiVendorNonSktEntity) {
                    OrganizationData organisasiObject = new OrganizationData();
                    organisasiObject.setPosition_id(elementOrganisasi.getKode_posisi());
                    organisasiObject.setName(elementOrganisasi.getNama());
                    organisasiObject.setPosition(elementOrganisasi.getPosisi());
                    organisasiObject.setJob_title(elementOrganisasi.getJabatan());
                    organisasiObject.setGender(elementOrganisasi.getJenis_kelamin());
                    organisasiObject.setNationality(elementOrganisasi.getKewarganegaraan());
                    organisasiObject.setNpp(elementOrganisasi.getNpp());
                    organisasiObject.setNpwp(elementOrganisasi.getNpwp());
                    organisasiObject.setKtp(elementOrganisasi.getKtp_kitas());
                    organisasiObject.setDate_of_birth((elementOrganisasi.getTgl_lahir()!=null)?elementOrganisasi.getTgl_lahir().toString():null);
                    Collections.addAll(organisasiArr, organisasiObject);
                }
                saveLogEproc(idLogEproc, "SCR41", null, "Organisasi Vendor built successfully. Total: " + organisasiArr.size());
            } catch (Exception e) {
                LOG.error("Organisasi Vendor error: " + e.getMessage(), e);
                saveLogEproc(idLogEproc, "SCR30", null, "Organisasi Vendor build error: " + e.getMessage());
                throw e;
            }

            // =====================================================
            // STEP 10: Jumlah SDM Vendor
            // =====================================================
            HumanResourcesData HumanResourcesObject = new HumanResourcesData();
            try {
                TdrJumlahSdmVendorNonSktEntity tdrJumlahSdmVendorNonSktEntity = tdrJumlahSdmVendorNonSkt.find("where id_identitas = :id_identitas", Parameters.with("id_identitas", vTdrIdentitasVendorNonSkt.getId_identitas())).firstResult();
                if(tdrJumlahSdmVendorNonSktEntity != null){
                    HumanResourcesObject.setManager(tdrJumlahSdmVendorNonSktEntity.getManajer());
                    HumanResourcesObject.setWorker(tdrJumlahSdmVendorNonSktEntity.getPekerja());
                    HumanResourcesObject.setExpert(tdrJumlahSdmVendorNonSktEntity.getTenaga_ahli());
                    saveLogEproc(idLogEproc, "SCR61", null, "Jumlah SDM Vendor built successfully");
                } else {
                    saveLogEproc(idLogEproc, "SCR50", null, "Jumlah SDM Vendor not found");
                }
            } catch (Exception e) {
                LOG.error("Jumlah SDM Vendor error: " + e.getMessage(), e);
                saveLogEproc(idLogEproc, "SCR50", null, "Jumlah SDM Vendor build error: " + e.getMessage());
                throw e;
            }
            
            // =====================================================
            // STEP 11: Review Vendor
            // =====================================================
            ReviewsData ReviewsObject = new ReviewsData();
            try {
                TdrReviewVendorNonSktEntity tdrReviewsVendorNonSktEntity = tdrReviewVendorNonSkt.find("where id_identitas = :id_identitas", Parameters.with("id_identitas", vTdrIdentitasVendorNonSkt.getId_identitas())).firstResult();
                if(tdrReviewsVendorNonSktEntity != null){
                    ReviewsObject.setTotal_project(Integer.parseInt(tdrReviewsVendorNonSktEntity.getTotal_project()));
                    ReviewsObject.setAverage_rating(Long.parseLong(tdrReviewsVendorNonSktEntity.getAverage_rating()));
                    ReviewsObject.setOntime_rate(Long.parseLong(tdrReviewsVendorNonSktEntity.getOntime_rate()));
                    ReviewsObject.setTotal_revenue(Long.parseLong(tdrReviewsVendorNonSktEntity.getTotal_revenue()));
                   saveLogEproc(idLogEproc, "SCR71", null, "Review Vendor built successfully");
                } else {
                    saveLogEproc(idLogEproc, "SCR60", null, "Review Vendor not found");
                }
            } catch (Exception e) {
                LOG.error("Review Vendor error: " + e.getMessage(), e);
                saveLogEproc(idLogEproc, "SCR60", null, "Review Vendor build error: " + e.getMessage());
                throw e;
            }
            
            // =====================================================
            // STEP 12: Legalitas Vendor
            // =====================================================
            List<LegalDocData> legalDoc = new ArrayList<>();
            try {
                List<TdrIdentitasVendorMinioNonSktEntity> vTdrIdentitasVendorMinioNonSkt = tdrIdentitasVendorMinioNonSkt.find("where id_pengajuan = :id ", Parameters.with("id", vTdrPengajuanVendorNonSkt.getId_pengajuan())).list();
                for (TdrIdentitasVendorMinioNonSktEntity e : vTdrIdentitasVendorMinioNonSkt) {
                    TdrMstFileLegalitasEntity file = tdrMstFile.find("where dok = :dok", Parameters.with("dok", e.getDok())).firstResult();
                    
                    if(e.getDok() == 3 || e.getDok() == 12 || e.getDok() == 13 || e.getDok() == 4 || e.getDok() == 6 || e.getDok() == 5 || e.getDok() == 15 || e.getDok() == 8 || e.getDok() == 9 || e.getDok() == 29){
                        LegalDocData docLegal = new LegalDocData();
                        docLegal.setDocument_name(file.getNama_file());
                        if(e.getDok() == 3){
                            docLegal.setDocument_number(vTdrIdentitasVendorNonSkt.getNomor_akta_pendirian());
                            docLegal.setDocument_valid_from(vTdrIdentitasVendorNonSkt.getTgl_terbit_akta_pendirian() != null ? vTdrIdentitasVendorNonSkt.getTgl_terbit_akta_pendirian().toString() : null);
                            docLegal.setDocument_valid_until(vTdrIdentitasVendorNonSkt.getTgl_akta_pendirian() != null ? vTdrIdentitasVendorNonSkt.getTgl_akta_pendirian().toString() : null);
                        }
        
                        if(e.getDok() == 12){
                            docLegal.setDocument_number(vTdrIdentitasVendorNonSkt.getNomor_akta_pengurus());
                            docLegal.setDocument_valid_from(vTdrIdentitasVendorNonSkt.getTgl_terbit_akta_pengurus() != null ? vTdrIdentitasVendorNonSkt.getTgl_terbit_akta_pengurus().toString() : null);
                            docLegal.setDocument_valid_until(vTdrIdentitasVendorNonSkt.getTgl_akta_pengurus() != null ? vTdrIdentitasVendorNonSkt.getTgl_akta_pengurus().toString() : null);
                        }

                        if(e.getDok() == 8){
                            docLegal.setDocument_number(vTdrIdentitasVendorNonSkt.getNomor_npwp());
                            docLegal.setDocument_valid_from(vTdrIdentitasVendorNonSkt.getTgl_terbit_npwp() != null ? vTdrIdentitasVendorNonSkt.getTgl_terbit_npwp().toString() : null);
                        }
        
                        if(e.getDok() == 9){
                            docLegal.setDocument_number(vTdrIdentitasVendorNonSkt.getNomor_pkp());
                            docLegal.setDocument_valid_from(vTdrIdentitasVendorNonSkt.getTgl_terbit_pkp() != null ? vTdrIdentitasVendorNonSkt.getTgl_terbit_pkp().toString() : null);
                            docLegal.setDocument_valid_until(vTdrIdentitasVendorNonSkt.getTgl_pkp() != null ? vTdrIdentitasVendorNonSkt.getTgl_pkp().toString() : null);
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
                saveLogEproc(idLogEproc, "SCR81", null, "Legalitas Vendor built successfully. Total: " + legalDoc.size());
            } catch (Exception e) {
                LOG.error("Legalitas Vendor error: " + e.getMessage(), e);
                saveLogEproc(idLogEproc, "SCR70", null, "Legalitas Vendor build error: " + e.getMessage());
                throw e;
            }

            // System.out.println("before request separator = ");
            // LOG.info("Before Request non skt begin");
            // =====================================================
            // STEP 14: BUILD REGISTRATION OBJECT & SEND TO EPROC
            // =====================================================
            try {
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

                String request = objectMapper.writeValueAsString(regData);
                saveLogEproc(idLogEproc, "SCR92", request, "Sending data to E-Proc API");
                
                LOG.info("[API] Sending to E-Proc API");
                String urlApiEproc = ConfigProvider.getConfig().getValue("url.registrasi.eproc", String.class);
                httpRequest hr = new httpRequest();
                responseConsume res = hr.postData(urlApiEproc, regData);
                String response = objectMapper.writeValueAsString(res);
                
                // =====================================================
                // STEP 15: HANDLE RESPONSE FROM EPROC
                // =====================================================
                if(res.getData() != null){
                    String resultData = objectMapper.writeValueAsString(res.getData());
                    JsonNode rootNode = objectMapper.readTree(resultData);
                
                    savePengajuanVendorNonSkt(idPengajuan, 1, rootNode.get("code").asText(), response, null);

                    // TdrPengajuanVendorNonSktEntity tdrNonSktUpdate = TdrPengajuanVendorNonSktEntity.find("id_identitas = ?1", tdrPengajuan.getId_identitas()).firstResult();
                    // vTdrPengajuanVendorNonSkt.setIs_eproc(1);
                    // vTdrPengajuanVendorNonSkt.setStatus_eproc(rootNode.get("code").asText());
                    // vTdrPengajuanVendorNonSkt.setMessage_is_eproc(objectMapper.writeValueAsString(res));
                    // tdrPengajuanVendorNonSkt.persist(vTdrPengajuanVendorNonSkt);

                    // vTdrIdentitasVendorNonSkt.setIs_minio_identitas_vendor(2);
                    // tdrIdentitasVendorNonSkt.persist(vTdrIdentitasVendorNonSkt);


                    // TdrLogEprocEntity tdrLogEproc = new TdrLogEprocEntity();
                    // tdrLogEproc.setRequest(request);
                    // tdrLogEproc.setResponse(response);
                    // tdrLogEproc.setCreated_at(LocalDateTime.now());
                    // tdrLogEproc.persist();

                    saveLogEproc(idLogEproc, "SCR93", request, response);
                    LOG.info("[SUCCESS] Data sent to E-Proc successfully for id_pengajuan: " + vTdrPengajuanVendorNonSkt.getId_pengajuan());
                }else{
                    if(res.getMessage() != null){
                        // vTdrPengajuanVendorNonSkt.setIs_eproc(1);
                        
                        // // TdrPengajuanVendorNonSktEntity tdrNonSktUpdate = TdrPengajuanVendorNonSktEntity.find("id_identitas = ?1", tdrPengajuan.getId_identitas()).firstResult();
                        // vTdrPengajuanVendorNonSkt.setStatus_eproc("REG01");
                        // vTdrPengajuanVendorNonSkt.setMessage_is_eproc(objectMapper.writeValueAsString(res));
                        // tdrPengajuanVendorNonSkt.persist(vTdrPengajuanVendorNonSkt);

                        // // tdrNonSktUpdate.persist();

                        savePengajuanVendorNonSkt(idPengajuan, 1, "REG01", response, null);

                        saveLogEproc(idLogEproc, "SCR94", request, response);
                        LOG.warn("[WARNING] E-Proc returned message: " + res.getMessage());
                    }else{
                        // vTdrPengajuanVendorNonSkt.setIs_eproc(0);

                        // // TdrPengajuanVendorNonSktEntity tdrNonSktUpdate = TdrPengajuanVendorNonSktEntity.find("id_identitas = ?1", tdrPengajuan.getId_identitas()).firstResult();
                        // vTdrPengajuanVendorNonSkt.setStatus_eproc("SCH01");
                        // vTdrPengajuanVendorNonSkt.setMessage_is_eproc(objectMapper.writeValueAsString(res.getValidation()));
                        // tdrPengajuanVendorNonSkt.persist(vTdrPengajuanVendorNonSkt);

                        savePengajuanVendorNonSkt(idPengajuan, 0, "SCH01", objectMapper.writeValueAsString(res.getValidation()), null);

                        saveLogEproc(idLogEproc, "SCR90", request, response);
                        LOG.error("[ERROR] E-Proc validation failed");
                    }
                    
                    // TdrLogEprocEntity tdrLogEproc = new TdrLogEprocEntity();
                    // tdrLogEproc.setRequest(request);
                    // tdrLogEproc.setResponse(response);
                    // tdrLogEproc.setCreated_at(LocalDateTime.now());
                    // tdrLogEproc.persist();
                    // tdrNonSktUpdate
                }

            } catch (Exception e) {
                // LOG.error("[ERROR] Failed to process identitas for id_identitas: " + vTdrPengajuanVendorNonSkt.getId_identitas() + " - Error: " + e.getMessage(), e);
                LOG.error("Failed to send to E-Proc: " + e.getMessage(), e);
                saveLogEproc(idLogEproc, "SCR90", null, "Failed to send to E-Proc: " + e.getMessage());
                throw e;
            }
        } catch (Exception e) {
            LOG.error("[ERROR] Failed to process vendor: " + vTdrPengajuanVendorNonSkt.getId_pengajuan() + " - Error: " + e.getMessage(), e);
            
            // Save error log
            if(idLogEproc != null) {
                saveLogEproc(idLogEproc, "SCR90", null, "Processing failed: " + e.getMessage());
            }

            // Update status sebelum throw
            try {
                // vTdrPengajuanVendorSkt.setIs_eproc(0);
                // vTdrPengajuanVendorSkt.setStatus_eproc("SCH01");
                // vTdrPengajuanVendorSkt.setMessage_is_eproc("Processing error: " + e.getMessage());
                // vTdrPengajuanVendorSkt.persist();
                savePengajuanVendor(idPengajuan, 0, "SCH01", "Processing error: " + e.getMessage(), null);

            } catch (Exception updateEx) {
                LOG.error("[ERROR] Failed to update vendor status: " + updateEx.getMessage());
            }
            
            // Wrap dalam RuntimeException (unchecked exception)
            throw new RuntimeException("Failed to process vendor: " + vTdrPengajuanVendorNonSkt.getId_pengajuan(), e);
        }
    }

    /**
     * Helper method untuk menyimpan log upload
     */
    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    public void savePengajuanVendorNonSkt(String id_pengajuan, Integer is_eproc, String status_eproc, String message_is_eproc, Long id_log_eproc) {
        if (id_pengajuan == "") {
            LOG.warn("[PENGAJUAN] Cannot save pengajuan Non SKT - id pengajuan is null");
            return;
        }
        
        try {
            TdrPengajuanVendorNonSktEntity entityPengajuanNonSKt = tdrPengajuanVendorNonSkt.find("where id_pengajuan = ?1",id_pengajuan).firstResult();
            if (entityPengajuanNonSKt != null) {
                if (is_eproc != null) {
                    entityPengajuanNonSKt.setIs_eproc(is_eproc);
                }
                if (status_eproc != null) {
                    entityPengajuanNonSKt.setStatus_eproc(status_eproc);
                }
                if (message_is_eproc != null) {
                    entityPengajuanNonSKt.setMessage_is_eproc(message_is_eproc);
                }
                if (id_log_eproc != null) {
                    entityPengajuanNonSKt.setId_log_eproc(id_log_eproc);
                }
                tdrPengajuanVendorNonSkt.persist(entityPengajuanNonSKt);
                LOG.info("[LOG] Saved pengajuan Non SKT: " + status_eproc);
            } else {
                LOG.warn("[LOG] Pengajuan Non SKT entity not found for id pengajuan: " + id_pengajuan);
            }
        } catch (Exception e) {
            LOG.error("Failed to save upload pengajuan Non SKT: " + e.getMessage());
        }
    }

    /**
     * Method untuk reset status vendor jika terjadi error di luar transaction
     * Ini dipanggil dari catch block di send_tdr_iproc_skt
     */
    @Transactional
    public void resetVendorNonSktStatus(String idPengajuan, String statusEproc, String errorMessage) {
        try {
            TdrPengajuanVendorNonSktEntity vendor = tdrPengajuanVendorNonSkt.find(
                "id_pengajuan = ?1", 
                idPengajuan
            ).firstResult();
            
            if (vendor != null) {
                vendor.setIs_eproc(0); // Reset ke 0 agar bisa retry
                vendor.setStatus_eproc(statusEproc);
                vendor.setMessage_is_eproc(errorMessage);
                vendor.persist();
                
                LOG.info("[RESET] Reset is_eproc = 0 for id_pengajuan: " + idPengajuan + " - Error: " + errorMessage);
            }
        } catch (Exception e) {
            LOG.error("[ERROR] Failed to reset vendor status: " + e.getMessage());
        }
    }
    
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
