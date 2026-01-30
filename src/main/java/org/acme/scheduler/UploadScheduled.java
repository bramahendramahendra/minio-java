package org.acme.scheduler;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

import org.acme.controllers.upload_minio.repository.TdrPengajuanVendorSktRepository;
import org.acme.controllers.upload_minio.repository.TdrPengajuanVendorNonSktRepository;
import org.acme.controllers.upload_minio.repository.TdrIdentitasVendorSktRepository;
import org.acme.controllers.upload_minio.repository.TdrIdentitasVendorNonSktRepository;
import org.acme.controllers.upload_minio.repository.TdrPengalamanVendorSktRepository;
import org.acme.controllers.upload_minio.repository.TdrPerbankanVendorSktRepository;
import org.acme.controllers.upload_minio.repository.TdrPerbankanVendorNonSktRepository;

import org.acme.controllers.upload_minio.repository.TdrIdentitasVendorMinioSktRepository;
import org.acme.controllers.upload_minio.repository.TdrIdentitasVendorMinioNonSktRepository;
import org.acme.controllers.upload_minio.repository.TdrPengalamanVendorMinioSktRepository;
import org.acme.controllers.upload_minio.repository.TdrPerbankanVendorMinioSktRepository;
import org.acme.controllers.upload_minio.repository.TdrPerbankanVendorMinioNonSktRepository;

import org.acme.controllers.upload_minio.repository.TdrMstFileLegalitasRepository;
import org.acme.controllers.upload_minio.repository.TdrUploadMinioLogRepository;
import org.acme.controllers.upload_minio.repository.TdrLogEprocRepository;

import org.acme.controllers.upload_minio.dto.DokLegalDto;
import org.acme.controllers.upload_minio.dto.UploaderDto;

import org.acme.controllers.upload_minio.entity.TdrPengajuanVendorSktEntity;
import org.acme.controllers.upload_minio.entity.TdrPengajuanVendorNonSktEntity;
import org.acme.controllers.upload_minio.entity.TdrIdentitasVendorSktEntity;
import org.acme.controllers.upload_minio.entity.TdrLogEprocEntity;
import org.acme.controllers.upload_minio.entity.TdrIdentitasVendorNonSktEntity;
import org.acme.controllers.upload_minio.entity.TdrPengalamanVendorSktEntity;
import org.acme.controllers.upload_minio.entity.TdrPerbankanVendorSktEntity;
import org.acme.controllers.upload_minio.entity.TdrPerbankanVendorNonSktEntity;

import org.acme.controllers.upload_minio.entity.TdrIdentitasVendorMinioSktEntity;
import org.acme.controllers.upload_minio.entity.TdrIdentitasVendorMinioNonSktEntity;
import org.acme.controllers.upload_minio.entity.TdrPengalamanVendorMinioSktEntity;
import org.acme.controllers.upload_minio.entity.TdrPerbankanVendorMinioSktEntity;
import org.acme.controllers.upload_minio.entity.TdrPerbankanVendorMinioNonSktEntity;

import org.acme.controllers.upload_minio.entity.TdrMstFileLegalitasEntity;
import org.acme.controllers.upload_minio.entity.TdrUploadMinioLogEntity;

import org.acme.utils.handlerResponse;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.jboss.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MakeBucketArgs;
import io.minio.MinioAsyncClient;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import io.minio.UploadObjectArgs;
import io.minio.errors.MinioException;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import io.quarkus.panache.common.Parameters;
import io.quarkus.scheduler.Scheduled;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.common.annotation.NonBlocking;
import io.vertx.core.Vertx;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.control.ActivateRequestContext;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import net.bytebuddy.utility.RandomString;


@ApplicationScoped
public class UploadScheduled {
    @Inject TdrPengajuanVendorSktRepository tdrPengajuanVendorSkt;
    @Inject TdrPengajuanVendorNonSktRepository tdrPengajuanVendorNonSkt;

    @Inject TdrIdentitasVendorSktRepository tdrIdentitasVendorSkt;
    @Inject TdrIdentitasVendorNonSktRepository tdrIdentitasVendorNonSkt;
    
    @Inject TdrPengalamanVendorSktRepository tdrPengalamanVendorSkt;
    
    @Inject TdrPerbankanVendorSktRepository tdrPerbankanVendorSkt;
    @Inject TdrPerbankanVendorNonSktRepository tdrPerbankanVendorNonSkt;

    @Inject TdrIdentitasVendorMinioSktRepository tdrIdentitasVendorMinioSkt;
    @Inject TdrIdentitasVendorMinioNonSktRepository tdrIdentitasVendorMinioNonSkt;
    
    @Inject TdrPengalamanVendorMinioSktRepository tdrPengalamanVendorMinioSkt;
    
    @Inject TdrPerbankanVendorMinioSktRepository tdrPerbankanVendorMinioSkt;
    @Inject TdrPerbankanVendorMinioNonSktRepository tdrPerbankanVendorMinioNonSkt;
    
    @Inject TdrUploadMinioLogRepository tdrUploadMinioLogRepository;
    @Inject TdrLogEprocRepository tdrLogEprocRepository;
    @Inject TdrMstFileLegalitasRepository tdrFileLegal;

    @Inject MinioAsyncClient MinioClient;

    @Inject MinioClient MC;

    @Inject Vertx vertx;

    // private final ObjectMapper objectMapper = new ObjectMapper();
    private final ObjectMapper objectMapper;

    @PersistenceContext
    EntityManager em;

    @Inject ManagedExecutor executor;


    private static final Logger LOG = Logger.getLogger(UploadScheduled.class);

    // Constructor untuk init ObjectMapper dengan JavaTimeModule
    public UploadScheduled() {
        this.objectMapper = new ObjectMapper();
        // Register module untuk Java 8 Date/Time
        this.objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        // Disable write dates as timestamps (agar lebih readable)
        this.objectMapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /**
     * Helper method untuk menyimpan log upload
     */
    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    public void saveLogEproc(Long id_log_eproc, String status) {
         if (id_log_eproc == null) {
            LOG.warn("[LOG] Cannot save log - id_log_eproc is null");
            return;
        }
        
        try {
            TdrLogEprocEntity entityLogEproc = tdrLogEprocRepository.findById(id_log_eproc);
            if (entityLogEproc != null) {
                entityLogEproc.setStatus(status);
                entityLogEproc.setUpdated_at(LocalDateTime.now());
                tdrLogEprocRepository.persist(entityLogEproc);
            }
        } catch (Exception e) {
            LOG.error("Failed to save upload log: " + e.getMessage());
        }
    }

    /**
     * Helper method untuk upload dokumen identitas skt (mengganti kode repetitif )
     */
    private void uploadDokumenIdentitasSkt(String bucket, String folderRoot, TdrPengajuanVendorSktEntity eTdrPengajuanVendorSkt, TdrIdentitasVendorSktEntity eTdrIdentitasVendorSkt, String fileName, int dokNumber) {
        try {
            LOG.info("txt dok " + dokNumber + " begin upload id_pengajuan = " + eTdrPengajuanVendorSkt.getId_pengajuan());
            
            TdrMstFileLegalitasEntity file = tdrFileLegal.find("where status_upload_minio = ?1 AND status_skt = ?2 AND dok = ?3", 1, 1, dokNumber).firstResult();
            String nama_file = file.getNama_file();
            String pathMinio = eTdrPengajuanVendorSkt.getId_pengajuan() + "/" + nama_file + "/" + fileName;
            String urlPath = folderRoot + "/" + fileName;
            Boolean checkFile = this.checkFile(urlPath);

            if(eTdrPengajuanVendorSkt.getId_pengajuan() != null){
                if(checkFile){
                    boolean get = this.check_object(bucket, pathMinio);
                    if(!get){
                        Boolean upload = this.uploadLegalDoc(bucket, pathMinio, urlPath);
                        if(upload){
                            try {
                                saveIdentitasVendorSktMinio(eTdrPengajuanVendorSkt, dokNumber, fileName, nama_file, pathMinio, "SUCCESS", null);
                                LOG.info("data berhasil di upload");
                            } catch (Exception e) {
                                saveIdentitasVendorSktMinio(eTdrPengajuanVendorSkt, dokNumber, fileName, nama_file, pathMinio, "FAILED", e.getMessage());
                                LOG.error("Identitas Save Minio " + e);
                            }
                        } else {
                            saveIdentitasVendorSktMinio(eTdrPengajuanVendorSkt, dokNumber, fileName, nama_file, pathMinio, "FAILED", "Upload failed");
                        }
                    } else {
                        try {
                            this.uploadLegalDoc(bucket, pathMinio, urlPath);
                            saveIdentitasVendorSktMinio(eTdrPengajuanVendorSkt, dokNumber, fileName, nama_file, pathMinio, "SUCCESS", "Re-uploaded");
                            LOG.info("data berhasil di upload ulang");
                        } catch (Exception e) {
                            saveIdentitasVendorSktMinio(eTdrPengajuanVendorSkt, dokNumber, fileName, nama_file, pathMinio, "FAILED", e.getMessage());
                            LOG.error("Pengalaman Save Minio " + e);
                        }
                    }
                } else {
                    saveIdentitasVendorSktMinio(eTdrPengajuanVendorSkt, dokNumber, fileName, nama_file, pathMinio, "SKIPPED", "File not exist");
                    LOG.info("File Not Exist " + fileName);
                }
            } else {
                LOG.info("Id Pengajuan Not Exists");
            }
        } catch (Exception e) {
            saveIdentitasVendorSktMinio(eTdrPengajuanVendorSkt, dokNumber, fileName, null, null, "FAILED", e.getMessage());
            LOG.error("Error uploading dok " + dokNumber + ": " + e.getMessage());
        }
    }

    /**
     * Helper method untuk upload pengalaman vendor
     */
    private void uploadPengalamanSkt(String bucket, String folderRoot, TdrPengajuanVendorSktEntity eTdrPengajuanVendorSkt, List<TdrPengalamanVendorSktEntity> eTdrPengalamanVendorSkt) {
        for (TdrPengalamanVendorSktEntity f : eTdrPengalamanVendorSkt) {
            try {
                String nama_file = f.getSpk_path();
                String urlPath = folderRoot + "/" + nama_file;
                String pathMinio = eTdrPengajuanVendorSkt.getId_pengajuan() + "/Pengalaman/" + nama_file;
                Boolean checkFile = this.checkFile(urlPath);

                if(eTdrPengajuanVendorSkt.getId_pengajuan() != null){
                    if(checkFile){
                        boolean get = this.check_object(bucket, pathMinio);
                        if(!get){
                            Boolean upload = this.uploadLegalDoc(bucket, pathMinio, urlPath);
                            if(upload){
                                try {
                                    // savePengalamanVendorSktMinio(f, pathMinio);
                                    savePengalamanVendorSktMinio(f, pathMinio, "SUCCESS", null);
                                    LOG.info("data Pengalaman berhasil di upload");
                                } catch (Exception e) {
                                    savePengalamanVendorSktMinio(f, pathMinio, "FAILED", e.getMessage());
                                    LOG.error("Pengalaman Save Minio " + e);
                                }
                            } else {
                                savePengalamanVendorSktMinio(f, pathMinio, "FAILED", "Upload failed");
                            }
                        } else {
                            try {
                                this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                // savePengalamanVendorSktMinio(f, pathMinio);
                                savePengalamanVendorSktMinio(f, pathMinio, "SUCCESS", "Re-uploaded");
                                LOG.info("data Pengalaman berhasil di upload ulang");
                            } catch (Exception e) {
                                savePengalamanVendorSktMinio(f, pathMinio, "FAILED", e.getMessage());
                                LOG.error("Pengalaman Save Minio " + e);
                            }
                        }
                    } else {
                        savePengalamanVendorSktMinio(f, pathMinio, "SKIPPED", "File not exist");
                        LOG.info("File not exist " + nama_file);
                    }
                } else {
                    LOG.info("Id Pengajuan Not Exists");
                }
            } catch (Exception e) {
                savePengalamanVendorSktMinio(f, null, "FAILED", e.getMessage());
                LOG.error("Error uploading file : " + e.getMessage());
            }
        }
    }

    /**
     * Helper method untuk upload perbankan vendor
     */
    private void uploadPerbankanSkt(String bucket, String folderRoot, TdrPengajuanVendorSktEntity eTdrPengajuanVendorSkt, List<TdrPerbankanVendorSktEntity> eTdrPerbankanVendorSkt) {
        for (TdrPerbankanVendorSktEntity p : eTdrPerbankanVendorSkt) {
            try {
                String nama_file = p.getKoran_path();
                String urlPath = folderRoot + "/" + nama_file;
                String pathMinio = eTdrPengajuanVendorSkt.getId_pengajuan() + "/Perbankan/" + nama_file;
                Boolean checkFile = this.checkFile(urlPath);

                if(eTdrPengajuanVendorSkt.getId_pengajuan() != null){
                    if(checkFile){
                        boolean get = this.check_object(bucket, pathMinio);
                        if(!get){
                            Boolean upload = this.uploadLegalDoc(bucket, pathMinio, urlPath);
                            if(upload){
                                try {
                                    // savePerbankanVendorSktMinio(p, pathMinio);
                                    savePerbankanVendorSktMinio(p, pathMinio, "SUCCESS", null);
                                    LOG.info("data Perbankan berhasil di upload");
                                } catch (Exception e) {
                                    savePerbankanVendorSktMinio(p, pathMinio, "FAILED", e.getMessage());
                                    LOG.error("Perbankan data save minio " + e);
                                }
                            } else {
                                savePerbankanVendorSktMinio(p, pathMinio, "FAILED", "Upload failed");
                            }
                        } else {
                            try {
                                this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                // savePerbankanVendorSktMinio(p, pathMinio);
                                savePerbankanVendorSktMinio(p, pathMinio, "SUCCESS", "Re-uploaded");
                                LOG.info("data Perbankan berhasil di upload");
                            } catch (Exception e) {
                                savePerbankanVendorSktMinio(p, pathMinio, "FAILED", e.getMessage());
                                LOG.error("Perbankan data save minio " + e);
                            }
                        }
                    } else {
                        savePerbankanVendorSktMinio(p, pathMinio, "SKIPPED", "File not exist");
                        LOG.info("File not exist " + nama_file);
                    }
                } else {
                    LOG.info("Id Pengajuan Not Exists");
                }
            } catch (Exception e) {
                savePerbankanVendorSktMinio(p, null, "FAILED", e.getMessage());
                LOG.error("Error uploading file : " + e.getMessage());
            }
        }
    }
    
    /**
     * Helper method untuk save identitas vendor minio
     */
    private void saveIdentitasVendorSktMinio(TdrPengajuanVendorSktEntity eTdrPengajuanVendorSkt, int dokNumber, String fileName, String namaFile, String pathMinio, String status, String errorMessage) {
        try {
            TdrIdentitasVendorMinioSktEntity entityTdrIdentitasSkt = new TdrIdentitasVendorMinioSktEntity();
            entityTdrIdentitasSkt.setId_pengajuan(eTdrPengajuanVendorSkt.getId_pengajuan());
            entityTdrIdentitasSkt.setIdIdentitas(eTdrPengajuanVendorSkt.getId_identitas());
            entityTdrIdentitasSkt.setDok(dokNumber);
            entityTdrIdentitasSkt.setDokLegalitasPath(fileName);
            // entityTdrIdentitasSkt.setIs_eproc(0);
            entityTdrIdentitasSkt.setNama_dok(namaFile);
            entityTdrIdentitasSkt.setPath_minio(pathMinio);
            entityTdrIdentitasSkt.setStatus(status);
            entityTdrIdentitasSkt.setErrorMessage(errorMessage);
            // entityTdrIdentitasSkt.setStatus_skt(1);
            entityTdrIdentitasSkt.setDate_upload(LocalDateTime.now());
            tdrIdentitasVendorMinioSkt.persist(entityTdrIdentitasSkt);
        } catch (Exception e) {
            LOG.error("Failed to save identitas vendor log: " + e.getMessage());
        }
    }

    /**
     * Helper method untuk save pengalaman vendor minio skt
     */
    private void savePengalamanVendorSktMinio(TdrPengalamanVendorSktEntity eTdrPengalamanVendorSkt, String pathMinio, String status, String errorMessage) {
        try {
            TdrPengalamanVendorMinioSktEntity entityTdrPengalaman = new TdrPengalamanVendorMinioSktEntity();
            entityTdrPengalaman.setId_pengalaman(eTdrPengalamanVendorSkt.getId_pengalaman());
            entityTdrPengalaman.setId_identitas(eTdrPengalamanVendorSkt.getId_identitas());
            entityTdrPengalaman.setNama_pekerjaan(eTdrPengalamanVendorSkt.getNama_pekerjaan());
            entityTdrPengalaman.setSpk_path(eTdrPengalamanVendorSkt.getSpk_path());
            entityTdrPengalaman.setMinio_path(pathMinio);
            entityTdrPengalaman.setStatus(status);
            entityTdrPengalaman.setErrorMessage(errorMessage);
            entityTdrPengalaman.setUpload_date(LocalDateTime.now());
            tdrPengalamanVendorMinioSkt.persist(entityTdrPengalaman);
        } catch (Exception e) {
            LOG.error("Failed to save identitas vendor log: " + e.getMessage());
        }
    }

    /**
     * Helper method untuk save perbankan vendor minio skt
     */
    private void savePerbankanVendorSktMinio(TdrPerbankanVendorSktEntity eTdrPerbankanVendorSkt, String pathMinio, String status, String errorMessage) {
        try {
            TdrPerbankanVendorMinioSktEntity entityTdrPerbankan = new TdrPerbankanVendorMinioSktEntity();
            entityTdrPerbankan.setId_perbankan(eTdrPerbankanVendorSkt.getId_perbankan());
            entityTdrPerbankan.setId_identitas(eTdrPerbankanVendorSkt.getId_identitas());
            entityTdrPerbankan.setNama_bank(eTdrPerbankanVendorSkt.getNama_bank());
            entityTdrPerbankan.setNama_rek(eTdrPerbankanVendorSkt.getNama_rek());
            entityTdrPerbankan.setKoran_path(eTdrPerbankanVendorSkt.getKoran_path());
            entityTdrPerbankan.setMinio_path(pathMinio);
            entityTdrPerbankan.setStatus(status);
            entityTdrPerbankan.setErrorMessage(errorMessage);
            entityTdrPerbankan.setUpload_date(LocalDateTime.now());
            tdrPerbankanVendorMinioSkt.persist(entityTdrPerbankan);
        } catch (Exception e) {
            LOG.error("Failed to save identitas vendor log: " + e.getMessage());
        }
    }

    /**
     * Helper method untuk update status minio
     */
    private void updateStatusMinioIdentitasSkt(TdrPengajuanVendorSktEntity eTdrPengajuanVendorSkt, TdrIdentitasVendorSktEntity eTdrIdentitasVendorSkt) {
        try {
            List<TdrIdentitasVendorMinioSktEntity> tdrIdentitasVendorSktMinioEntityList = tdrIdentitasVendorMinioSkt.find("id_pengajuan = ?1", eTdrPengajuanVendorSkt.getId_pengajuan()).list();
            // List<TdrPengalamanVendorMinioSktEntity> tdrPengalamanVendorSktMinioEntityList = tdrPengalamanVendorMinioSkt.find("where id_identitas = ?1", eTdrIdentitasVendorSkt.getId_identitas()).list();
            // List<TdrPerbankanVendorMinioSktEntity> tdrPerbankanVendorSktMinioEntityList = tdrPerbankanVendorMinioSkt.find("where id_identitas = ?1", eTdrIdentitasVendorSkt.getId_identitas()).list();
            
            // if(tdrIdentitasVendorSktMinioEntityList.size() > 0 && tdrPengalamanVendorSktMinioEntityList.size() > 0 && tdrPerbankanVendorSktMinioEntityList.size() > 0){
            if(tdrIdentitasVendorSktMinioEntityList.size() > 0){
                TdrIdentitasVendorSktEntity entityIdentitasVendor = tdrIdentitasVendorSkt.find("where id_identitas = :id_identitas", Parameters.with("id_identitas", eTdrIdentitasVendorSkt.getId_identitas())).firstResult();
                entityIdentitasVendor.setIs_minio_identitas_vendor(1);
                tdrIdentitasVendorSkt.persist(entityIdentitasVendor);
                LOG.info("Upload SKT done = " + eTdrIdentitasVendorSkt.getId_identitas());
            }
        } catch (Exception e) {
            LOG.error("error = " + e.getMessage() + " id_pengajuan = " + eTdrPengajuanVendorSkt.getId_pengajuan());
        }
    }

    /**
     * Helper method untuk update status minio
     */
    private void updateStatusMinioPengalamanSkt(TdrPengajuanVendorSktEntity eTdrPengajuanVendorSkt, List<TdrPengalamanVendorSktEntity> eTdrPengalamanVendorSkt) {
        try {
            // List<TdrIdentitasVendorMinioSktEntity> tdrIdentitasVendorSktMinioEntityList = tdrIdentitasVendorMinioSkt.find("id_pengajuan = ?1", eTdrPengajuanVendorSkt.getId_pengajuan()).list();
            List<TdrPengalamanVendorMinioSktEntity> tdrPengalamanVendorSktMinioEntityList = tdrPengalamanVendorMinioSkt.find("where id_identitas = ?1", eTdrPengajuanVendorSkt.getId_identitas()).list();
            // List<TdrPerbankanVendorMinioSktEntity> tdrPerbankanVendorSktMinioEntityList = tdrPerbankanVendorMinioSkt.find("where id_identitas = ?1", eTdrIdentitasVendorSkt.getId_identitas()).list();
            
            // if(tdrIdentitasVendorSktMinioEntityList.size() > 0 && tdrPengalamanVendorSktMinioEntityList.size() > 0 && tdrPerbankanVendorSktMinioEntityList.size() > 0){
            if(tdrPengalamanVendorSktMinioEntityList.size() > 0){
                for (TdrPengalamanVendorMinioSktEntity value : tdrPengalamanVendorSktMinioEntityList) {
                    TdrPengalamanVendorSktEntity entityPengalamanVendor = tdrPengalamanVendorSkt.find("where id_pengalaman = :id_pengalaman", Parameters.with("id_pengalaman", value.getId_pengalaman())).firstResult();
                    entityPengalamanVendor.setIs_minio(1);
                    tdrPengalamanVendorSkt.persist(entityPengalamanVendor);
                    LOG.info("Upload Pengalaman FIle done = " + value.getId_pengalaman());
                }
            }
        } catch (Exception e) {
            LOG.error("error = " + e.getMessage() + " id_pengajuan = " + eTdrPengajuanVendorSkt.getId_pengajuan());
        }
    }

     /**
     * Helper method untuk update status minio
     */
    private void updateStatusMinioPerbankanSkt(TdrPengajuanVendorSktEntity eTdrPengajuanVendorSkt, List<TdrPerbankanVendorSktEntity> eTdrPerbankanVendorSkt) {
        try {
            // List<TdrIdentitasVendorMinioSktEntity> tdrIdentitasVendorSktMinioEntityList = tdrIdentitasVendorMinioSkt.find("id_pengajuan = ?1", eTdrPengajuanVendorSkt.getId_pengajuan()).list();
            // List<TdrPengalamanVendorMinioSktEntity> tdrPengalamanVendorSktMinioEntityList = tdrPengalamanVendorMinioSkt.find("where id_identitas = ?1", eTdrIdentitasVendorSkt.getId_identitas()).list();
            List<TdrPerbankanVendorMinioSktEntity> tdrPerbankanVendorSktMinioEntityList = tdrPerbankanVendorMinioSkt.find("where id_identitas = ?1", eTdrPengajuanVendorSkt.getId_identitas()).list();
            
            // if(tdrIdentitasVendorSktMinioEntityList.size() > 0 && tdrPengalamanVendorSktMinioEntityList.size() > 0 && tdrPerbankanVendorSktMinioEntityList.size() > 0){
            if(tdrPerbankanVendorSktMinioEntityList.size() > 0){
                for (TdrPerbankanVendorMinioSktEntity value : tdrPerbankanVendorSktMinioEntityList) {
                    TdrPerbankanVendorSktEntity entityPerbankanVendor = tdrPerbankanVendorSkt.find("where id_perbankan = :id_perbankan", Parameters.with("id_perbankan", value.getId_perbankan())).firstResult();
                    entityPerbankanVendor.setIs_minio(1);
                    tdrPerbankanVendorSkt.persist(entityPerbankanVendor);
                    LOG.info("Upload Koran Path done = " + value.getId_perbankan());
                }
            }
        } catch (Exception e) {
            LOG.error("error = " + e.getMessage() + " id_pengajuan = " + eTdrPengajuanVendorSkt.getId_pengajuan());
        }
    }
    
    // @Scheduled(every = "60s")
    // @Transactional

    // @Scheduled(every = "60s")
    // public void scheduled_upload_skt(){
    //     CompletableFuture.supplyAsync(() -> {
    //         // Simulate some long-running task
    //         try {
    //             Thread.sleep(1000); // Simulating a delay
    //             // System.out.println("Task completed!");
    //             this.upload_skt();
    //             LOG.info("Task upload Skt Completed");
    //         } catch (InterruptedException e) {
    //             Thread.currentThread().interrupt();
    //             // System.out.println("Task interrupted!");
    //             LOG.info("task Interupted");
    //         }
    //         return "Task Upload Skt Berhasil";
    //     }, executor).thenAccept(result -> {
    //         // Process the result if needed
    //         // System.out.println("Result: " + result);
    //         LOG.info(result);
    //     });
    // }

    // ============================================
    // MAIN UPLOAD METHODS SKT
    // ============================================
    
    /**
     * Scheduler untuk upload otomatis setiap 60 detik
     */

    // @Scheduled(every = "60s")
    // @Transactional
    public void upload_skt(){
        upload_skt_process(null);
    }

    /**
     * Method untuk upload berdasarkan id_pengajuan tertentu
     */
    // @Transactional
    public void upload_skt_by_id_pengajuan(String idPengajuan){
        upload_skt_process(idPengajuan);
    }
    
    /**
     * Core method untuk proses upload SKT
     */
    // @Transactional
    public void upload_skt_process(String specificIdPengajuan) {
        LOG.info("==============================================");
        LOG.info("============= Upload SKT begin ===============");
        LOG.info("==============================================");
        try {
            String bucket = ConfigProvider.getConfig().getValue("bucket.minio", String.class);
            boolean a = this.checkBucket(bucket);
            String folderRoot = ConfigProvider.getConfig().getValue("root.folder.sso", String.class);
            
            if(!a){
                this.createBucket(bucket);
            }
            LOG.info("Upload SKT Begin" + (specificIdPengajuan != null ? " for ID Pengajuan: " + specificIdPengajuan : ""));
            
            List<TdrPengajuanVendorSktEntity> eTdrPengajuanVendorSktList;
            if(specificIdPengajuan != null && !specificIdPengajuan.isEmpty()) {
                // Upload berdasarkan id_pengajuan tertentu
                eTdrPengajuanVendorSktList = tdrPengajuanVendorSkt.find("id_pengajuan = ?1 AND id_vendor_eproc is not null", specificIdPengajuan).list();
            } else {
                // Upload scheduled (semua data)
                eTdrPengajuanVendorSktList = tdrPengajuanVendorSkt.find("where id_pengajuan is not null AND id_vendor_eproc is not null").page(0, 50).list();
            }
            LOG.info("[DATA] Total Pengajuan vendor SKT found: " + eTdrPengajuanVendorSktList.size());
            if (eTdrPengajuanVendorSktList.isEmpty()) {
                LOG.info("[INFO] No pending vendor skt to process.");
                LOG.info("Send Upload Data Eproc SKT Finished");
                return;
            }
            
            for(TdrPengajuanVendorSktEntity elTdrPengajuanVendorSkt: eTdrPengajuanVendorSktList){ 
                try {
                    // Panggil method dengan @Transactional per vendor
                    processOneVendor(elTdrPengajuanVendorSkt, bucket, folderRoot);
                } catch (Exception e) {
                    LOG.error("[ERROR] Failed to process vendor: " + elTdrPengajuanVendorSkt.getId_pengajuan() + " - Error: " + e.getMessage(), e);
                    // Continue ke vendor berikutnya meskipun ada error
                }
            }

            LOG.info("==============================================");
            LOG.info("=========== Upload SKT Finished ==============");
            LOG.info("==============================================");
        } catch (Exception e) {
            // TODO: handle exception
            LOG.error("error upload SKT", e);
            LOG.error("Upload SKT Process error: " + e.getMessage());
        }
    }

    /**
     * Process satu vendor dalam satu transaction
     * Method ini menggunakan @Transactional sehingga setiap vendor punya transaction sendiri
     */
    @Transactional
    public void processOneVendor(TdrPengajuanVendorSktEntity elTdrPengajuanVendorSkt, String bucket, String folderRoot) {
        LOG.info("[START] Processing vendor SKT: " + elTdrPengajuanVendorSkt.getId_pengajuan());
        
        // Validasi data vendor
        if (elTdrPengajuanVendorSkt.getId_identitas() == null || elTdrPengajuanVendorSkt.getId_identitas() == 0) {
            LOG.info("[SKIP] Id Identitas Not Exists for pengajuan: " + (elTdrPengajuanVendorSkt != null ? elTdrPengajuanVendorSkt.getId_pengajuan() : "NULL"));
            return;
        }

        if (elTdrPengajuanVendorSkt == null || elTdrPengajuanVendorSkt.getId_pengajuan() == null || elTdrPengajuanVendorSkt.getId_pengajuan().isEmpty()) {
            LOG.info("[SKIP] Id Pengajuan Not Exists for pengajuan: " + (elTdrPengajuanVendorSkt != null ? elTdrPengajuanVendorSkt.getId_pengajuan() : "NULL"));
            return;
        }

        // Process Identitas Minio 
        try {
            processIdentitasSkt(elTdrPengajuanVendorSkt, bucket, folderRoot);
        } catch (Exception e) {
            saveLogEproc(elTdrPengajuanVendorSkt.getId_log_eproc(), "SCU00");
            LOG.error("[ERROR] Failed to process identitas for id_identitas: " + elTdrPengajuanVendorSkt.getId_identitas() + " - Error: " + e.getMessage(), e);
        }   

        // Pengalaman Minio 
        try {
           processPengalamanSkt(elTdrPengajuanVendorSkt, bucket, folderRoot);
        } catch (Exception e) {
            saveLogEproc(elTdrPengajuanVendorSkt.getId_log_eproc(), "SCU10");   
            LOG.error("[ERROR] Failed to process pengalaman for id_identitas: " + elTdrPengajuanVendorSkt.getId_identitas() + " - Error: " + e.getMessage(), e);
        }
        
        // Perbankan Minio
        try {
            processPerbankanSkt(elTdrPengajuanVendorSkt, bucket, folderRoot);
        } catch (Exception e) {
            saveLogEproc(elTdrPengajuanVendorSkt.getId_log_eproc(), "SCU20");
            LOG.error("[ERROR] Failed to process perbanakan for id_identitas: " + elTdrPengajuanVendorSkt.getId_identitas() + " - Error: " + e.getMessage(), e);
        }
    }

    /**
     * Process identitas vendor dalam transaction yang sama dengan processOneVendor
     */
    private void processIdentitasSkt(TdrPengajuanVendorSktEntity elTdrPengajuanVendorSkt, String bucket, String folderRoot) throws Exception {
        LOG.info("[IDENTITAS] Start processing for id_identitas: " + elTdrPengajuanVendorSkt.getId_identitas());
        
        TdrIdentitasVendorSktEntity eTdrIdentitasVendorSkt = tdrIdentitasVendorSkt.find("id_identitas = ?1 AND (is_minio_identitas_vendor is null OR is_minio_identitas_vendor = ?2)", elTdrPengajuanVendorSkt.getId_identitas(),0).firstResult();
        if (eTdrIdentitasVendorSkt == null) {
            LOG.warn("[NOT FOUND] Identitas tidak ditemukan untuk id_identitas: " + elTdrPengajuanVendorSkt.getId_identitas());
            return;
        }

        if(eTdrIdentitasVendorSkt.getDok_legalitas_path() == null){
            LOG.info("[NOT FOUND] Dokumen Legalitas Not Exist: "+eTdrIdentitasVendorSkt.getId_identitas());
            return;
        }

        // varDump("Tdr Pengajuan", tdrPengajuanVendor);
        // varDump("Tdr Identitas", i);

        DokLegalDto DokLegalObject = this.parseJson(eTdrIdentitasVendorSkt.getDok_legalitas_path());

        // Delete existing minio records
        List<TdrIdentitasVendorMinioSktEntity> tdrIdentitasVendorSktMinioList = tdrIdentitasVendorMinioSkt.find("id_pengajuan = ?1", elTdrPengajuanVendorSkt.getId_pengajuan()).list();
        if(tdrIdentitasVendorSktMinioList.size() > 0){
            tdrIdentitasVendorMinioSkt.delete("id_pengajuan = ?1", elTdrPengajuanVendorSkt.getId_pengajuan());
        }

        // Upload semua dokumen
        if(DokLegalObject.getTxt_dok1() != null){
            uploadDokumenIdentitasSkt(bucket, folderRoot, elTdrPengajuanVendorSkt, eTdrIdentitasVendorSkt, DokLegalObject.getTxt_dok1(), 1);
        }

            if(DokLegalObject.getTxt_dok2() != null){
            uploadDokumenIdentitasSkt(bucket, folderRoot, elTdrPengajuanVendorSkt, eTdrIdentitasVendorSkt, DokLegalObject.getTxt_dok2(), 2);
        }

        if(DokLegalObject.getTxt_dok3() != null){
            uploadDokumenIdentitasSkt(bucket, folderRoot, elTdrPengajuanVendorSkt, eTdrIdentitasVendorSkt, DokLegalObject.getTxt_dok3(), 3);
        }
        
        if(DokLegalObject.getTxt_dok4() != null){
            uploadDokumenIdentitasSkt(bucket, folderRoot, elTdrPengajuanVendorSkt, eTdrIdentitasVendorSkt, DokLegalObject.getTxt_dok4(), 4);
        }

        if(DokLegalObject.getTxt_dok5() != null){
            uploadDokumenIdentitasSkt(bucket, folderRoot, elTdrPengajuanVendorSkt, eTdrIdentitasVendorSkt, DokLegalObject.getTxt_dok5(), 5);
        }
        
        if(DokLegalObject.getTxt_dok6() != null){
            uploadDokumenIdentitasSkt(bucket, folderRoot, elTdrPengajuanVendorSkt, eTdrIdentitasVendorSkt, DokLegalObject.getTxt_dok6(), 6);
        }
        
        if(DokLegalObject.getTxt_dok7() != null){
            uploadDokumenIdentitasSkt(bucket, folderRoot, elTdrPengajuanVendorSkt, eTdrIdentitasVendorSkt, DokLegalObject.getTxt_dok7(), 7);
        }
        
        if(DokLegalObject.getTxt_dok8() != null){
            uploadDokumenIdentitasSkt(bucket, folderRoot, elTdrPengajuanVendorSkt, eTdrIdentitasVendorSkt, DokLegalObject.getTxt_dok8(), 8);
        }

        if(DokLegalObject.getTxt_dok9() != null){
            uploadDokumenIdentitasSkt(bucket, folderRoot, elTdrPengajuanVendorSkt, eTdrIdentitasVendorSkt, DokLegalObject.getTxt_dok9(), 9);
        }
        
        if(DokLegalObject.getTxt_dok10() != null){
            uploadDokumenIdentitasSkt(bucket, folderRoot, elTdrPengajuanVendorSkt, eTdrIdentitasVendorSkt, DokLegalObject.getTxt_dok10(), 10);
        }
        
        if(DokLegalObject.getTxt_dok11() != null){
            uploadDokumenIdentitasSkt(bucket, folderRoot, elTdrPengajuanVendorSkt, eTdrIdentitasVendorSkt, DokLegalObject.getTxt_dok11(), 11);
        }

        if(DokLegalObject.getTxt_dok12() != null){
            uploadDokumenIdentitasSkt(bucket, folderRoot, elTdrPengajuanVendorSkt, eTdrIdentitasVendorSkt, DokLegalObject.getTxt_dok12(), 12);
        }
        
        if(DokLegalObject.getTxt_dok13() != null){
            uploadDokumenIdentitasSkt(bucket, folderRoot, elTdrPengajuanVendorSkt, eTdrIdentitasVendorSkt, DokLegalObject.getTxt_dok13(), 13);
        }

        if(DokLegalObject.getTxt_dok15() != null){
            uploadDokumenIdentitasSkt(bucket, folderRoot, elTdrPengajuanVendorSkt, eTdrIdentitasVendorSkt, DokLegalObject.getTxt_dok15(), 15);
        }
        
        if(DokLegalObject.getTxt_dok16() != null){
            uploadDokumenIdentitasSkt(bucket, folderRoot, elTdrPengajuanVendorSkt, eTdrIdentitasVendorSkt, DokLegalObject.getTxt_dok16(), 16);
        }
        
        if(DokLegalObject.getTxt_dok17() != null){
            uploadDokumenIdentitasSkt(bucket, folderRoot, elTdrPengajuanVendorSkt, eTdrIdentitasVendorSkt, DokLegalObject.getTxt_dok17(), 17);
        }

        if(DokLegalObject.getTxt_dok18() != null){
            uploadDokumenIdentitasSkt(bucket, folderRoot, elTdrPengajuanVendorSkt, eTdrIdentitasVendorSkt, DokLegalObject.getTxt_dok18(), 18);
        }
        
        if(DokLegalObject.getTxt_dok19() != null){
            uploadDokumenIdentitasSkt(bucket, folderRoot, elTdrPengajuanVendorSkt, eTdrIdentitasVendorSkt, DokLegalObject.getTxt_dok19(), 19);
        }

        if(DokLegalObject.getTxt_dok20() != null){
            uploadDokumenIdentitasSkt(bucket, folderRoot, elTdrPengajuanVendorSkt, eTdrIdentitasVendorSkt, DokLegalObject.getTxt_dok20(), 20);
        }
        
        if(DokLegalObject.getTxt_dok21() != null){
            uploadDokumenIdentitasSkt(bucket, folderRoot, elTdrPengajuanVendorSkt, eTdrIdentitasVendorSkt, DokLegalObject.getTxt_dok21(), 21);
        }

        if(DokLegalObject.getTxt_dok22() != null){
            uploadDokumenIdentitasSkt(bucket, folderRoot, elTdrPengajuanVendorSkt, eTdrIdentitasVendorSkt, DokLegalObject.getTxt_dok22(), 22);
        }

        if(DokLegalObject.getTxt_dok23() != null){
            uploadDokumenIdentitasSkt(bucket, folderRoot, elTdrPengajuanVendorSkt, eTdrIdentitasVendorSkt, DokLegalObject.getTxt_dok23(), 23);
        }

        if(DokLegalObject.getTxt_dok24() != null){
            uploadDokumenIdentitasSkt(bucket, folderRoot, elTdrPengajuanVendorSkt, eTdrIdentitasVendorSkt, DokLegalObject.getTxt_dok24(), 24);
        }

        if(DokLegalObject.getTxt_dok25() != null){
            uploadDokumenIdentitasSkt(bucket, folderRoot, elTdrPengajuanVendorSkt, eTdrIdentitasVendorSkt, DokLegalObject.getTxt_dok25(), 25);
        }

        if(DokLegalObject.getTxt_dok26() != null){
            uploadDokumenIdentitasSkt(bucket, folderRoot, elTdrPengajuanVendorSkt, eTdrIdentitasVendorSkt, DokLegalObject.getTxt_dok26(), 26);
        }

        if(DokLegalObject.getTxt_dok27() != null){
            uploadDokumenIdentitasSkt(bucket, folderRoot, elTdrPengajuanVendorSkt, eTdrIdentitasVendorSkt, DokLegalObject.getTxt_dok27(), 27);
        }
        
        if(DokLegalObject.getTxt_dok28() != null){
            uploadDokumenIdentitasSkt(bucket, folderRoot, elTdrPengajuanVendorSkt, eTdrIdentitasVendorSkt, DokLegalObject.getTxt_dok28(), 28);
        }

        if(DokLegalObject.getTxt_dok29() != null){
            uploadDokumenIdentitasSkt(bucket, folderRoot, elTdrPengajuanVendorSkt, eTdrIdentitasVendorSkt, DokLegalObject.getTxt_dok29(), 29);
        }

        if(DokLegalObject.getTxt_dok30() != null){
            uploadDokumenIdentitasSkt(bucket, folderRoot, elTdrPengajuanVendorSkt, eTdrIdentitasVendorSkt, DokLegalObject.getTxt_dok30(), 30);
        }
            
        // } catch (Exception e) {
        //     saveIdentitasVendorSktMinio(elTdrPengajuanVendorSkt, 0, null, null, null, "FAILED", "Error processing documents: " + e.getMessage());
        //     try {
        //         Thread.sleep(1000);
        //     } catch (Exception f) {
        //         LOG.error(f);
        //     }
        //     LOG.info("error = " + e);
        // }

        // ===== UPDATE STATUS MINIO =====
        updateStatusMinioIdentitasSkt(elTdrPengajuanVendorSkt, eTdrIdentitasVendorSkt);
        saveLogEproc(elTdrPengajuanVendorSkt.getId_log_eproc(), "SCU11");

        LOG.info("[IDENTITAS] Finish processing for id_identitas: " + elTdrPengajuanVendorSkt.getId_identitas());
    }

    /**
     * Process pengalaman vendor dalam transaction yang sama dengan processOneVendor
     */
    private void processPengalamanSkt(TdrPengajuanVendorSktEntity elTdrPengajuanVendorSkt, String bucket, String folderRoot) throws Exception {
        LOG.info("[PENGALAMAN] Start processing for id_identitas: " + elTdrPengajuanVendorSkt.getId_identitas());

         List<TdrPengalamanVendorSktEntity> eTdrPengalamanVendorSkt = tdrPengalamanVendorSkt.find("where id_identitas = ?1 AND (is_minio is null OR is_minio = ?2)", elTdrPengajuanVendorSkt.getId_identitas(), 0).list();
        if (eTdrPengalamanVendorSkt == null) {
            LOG.warn("[NOT FOUND] Pengalaman tidak ditemukan untuk id_identitas: " + elTdrPengajuanVendorSkt.getId_identitas());
            return;
        }

        // Delete existing minio records
        List<TdrPengalamanVendorMinioSktEntity> tdrPengalamanVendorSktList = tdrPengalamanVendorMinioSkt.find("where id_identitas = ?1", elTdrPengajuanVendorSkt.getId_identitas()).list();
        if(tdrPengalamanVendorSktList.size() > 0){
            tdrPengalamanVendorMinioSkt.delete("where id_identitas = :id", Parameters.with("id", elTdrPengajuanVendorSkt.getId_identitas()));
        }
            
        // ===== UPLOAD PENGALAMAN =====
        uploadPengalamanSkt(bucket, folderRoot, elTdrPengajuanVendorSkt, eTdrPengalamanVendorSkt);

        // ===== UPDATE STATUS MINIO =====
        updateStatusMinioPengalamanSkt(elTdrPengajuanVendorSkt, eTdrPengalamanVendorSkt);
        saveLogEproc(elTdrPengajuanVendorSkt.getId_log_eproc(), "SCU21");

        LOG.info("[PENGALAMAN] Finish processing for id_identitas: " + elTdrPengajuanVendorSkt.getId_identitas());
    }

    /**
     * Process perbankan vendor dalam transaction yang sama dengan processOneVendor
     */
    private void processPerbankanSkt(TdrPengajuanVendorSktEntity elTdrPengajuanVendorSkt, String bucket, String folderRoot) throws Exception {
        LOG.info("[PERBANKAN] Start processing for id_identitas: " + elTdrPengajuanVendorSkt.getId_identitas());
        
        List<TdrPerbankanVendorSktEntity> eTdrPerbankanVendorSkt = tdrPerbankanVendorSkt.find("where id_identitas = ?1 AND (is_minio is null OR is_minio = ?2)", elTdrPengajuanVendorSkt.getId_identitas(), 0).list();
        if (eTdrPerbankanVendorSkt == null) {
            LOG.warn("[NOT FOUND] Perbankan tidak ditemukan untuk id_identitas: " + elTdrPengajuanVendorSkt.getId_identitas());
            return;
        }

        // Delete existing minio records
        List<TdrPerbankanVendorMinioSktEntity> tdrPerbankanVendorSktList = tdrPerbankanVendorMinioSkt.find("where id_identitas = ?1", elTdrPengajuanVendorSkt.getId_identitas()).list();
        if(tdrPerbankanVendorSktList.size() > 0){
            tdrPerbankanVendorMinioSkt.delete("where id_identitas = :id", Parameters.with("id", elTdrPengajuanVendorSkt.getId_identitas()));
        }
            
        // ===== UPLOAD PERBANKAN =====
        uploadPerbankanSkt(bucket, folderRoot, elTdrPengajuanVendorSkt, eTdrPerbankanVendorSkt);

        // ===== UPDATE STATUS MINIO =====
        updateStatusMinioPerbankanSkt(elTdrPengajuanVendorSkt, eTdrPerbankanVendorSkt);
        saveLogEproc(elTdrPengajuanVendorSkt.getId_log_eproc(), "SCR01");

        LOG.info("[PERBANKAN] Finish processing for id_identitas: " + elTdrPengajuanVendorSkt.getId_identitas());
    }

    // NON SKT 
    /**
     * Helper method untuk upload dokumen identitas non skt (mengganti kode repetitif)
     */
    private void uploadDokumenIdentitasNonSkt(String bucket, String folderRoot, TdrPengajuanVendorNonSktEntity eTdrPengajuanVendorNonSkt, TdrIdentitasVendorNonSktEntity eTdrIdentitasVendorNonSkt, String fileName, int dokNumber) {
        try {
            LOG.info("txt dok " + dokNumber + " begin upload id_pengajuan = " + eTdrPengajuanVendorNonSkt.getId_pengajuan());
            
            TdrMstFileLegalitasEntity file = tdrFileLegal.find("where status_upload_minio = ?1 AND status_skt = ?2 AND dok = ?3", 1, 1, dokNumber).firstResult();
            String nama_file = file.getNama_file();
            String pathMinio = eTdrPengajuanVendorNonSkt.getId_pengajuan() + "/" + nama_file + "/" + fileName;
            String urlPath = folderRoot + "/" + fileName;
            Boolean checkFile = this.checkFile(urlPath);
            
            if(eTdrPengajuanVendorNonSkt.getId_pengajuan() != null){
                if(checkFile){
                    boolean get = this.check_object(bucket, pathMinio);
                    if(!get){
                        Boolean upload = this.uploadLegalDoc(bucket, pathMinio, urlPath);
                        if(upload){
                            try {
                                saveIdentitasVendorNonSktMinio(eTdrPengajuanVendorNonSkt, dokNumber, fileName, nama_file, pathMinio, "SUCCESS", null);
                                LOG.info("data berhasil di upload");
                            } catch (Exception e) {
                                saveIdentitasVendorNonSktMinio(eTdrPengajuanVendorNonSkt, dokNumber, fileName, nama_file, pathMinio, "FAILED", e.getMessage());
                                LOG.error("Identitas Save Minio " + e);
                            }
                        } else {
                            saveIdentitasVendorNonSktMinio(eTdrPengajuanVendorNonSkt, dokNumber, fileName, nama_file, pathMinio, "FAILED", "Upload failed");
                        }
                    } else {
                        try {
                            this.uploadLegalDoc(bucket, pathMinio, urlPath);
                            saveIdentitasVendorNonSktMinio(eTdrPengajuanVendorNonSkt, dokNumber, fileName, nama_file, pathMinio, "SUCCESS", "Re-uploaded");
                            // saveIdentitasVendorNonSktMinio(eTdrPengajuanVendorNonSkt, dokNumber, nama_file, pathMinio);
                            // saveUploadLog(eTdrPengajuanVendorNonSkt.getId_pengajuan(), eTdrIdentitasVendorNonSkt.getId_identitas(), "identitas", dokNumber, fileName, pathMinio, "SUCCESS", "Re-uploaded", 1);
                            LOG.info("data berhasil di upload");
                        } catch (Exception e) {
                            saveIdentitasVendorNonSktMinio(eTdrPengajuanVendorNonSkt, dokNumber, fileName, nama_file, pathMinio, "FAILED", e.getMessage());
                            LOG.error("Pengalaman Save Minio " + e);
                        }
                    }
                } else {
                    saveIdentitasVendorNonSktMinio(eTdrPengajuanVendorNonSkt, dokNumber, fileName, nama_file, pathMinio, "SKIPPED", "File not exist");
                    // saveUploadLog(eTdrPengajuanVendorNonSkt.getId_pengajuan(), eTdrIdentitasVendorNonSkt.getId_identitas(), "identitas", dokNumber, fileName, pathMinio, "SKIPPED", "File not exist", 1);
                    LOG.info("File Not Exist " + fileName);
                }
            } else {
                LOG.info("Id Pengajuan Not Exists");
            }
        } catch (Exception e) {
            saveIdentitasVendorNonSktMinio(eTdrPengajuanVendorNonSkt, dokNumber, fileName, null, null, "FAILED", e.getMessage());
            // saveUploadLog(eTdrPengajuanVendorNonSkt.getId_pengajuan(), eTdrIdentitasVendorNonSkt.getId_identitas(), "identitas", dokNumber, fileName, null, "FAILED", e.getMessage(), 1);
            LOG.error("Error uploading dok " + dokNumber + ": " + e.getMessage());
        }
    }

    /**
     * Helper method untuk upload perbankan vendor NON SKT
     */
    private void uploadPerbankanNonSkt(String bucket, String folderRoot, TdrPengajuanVendorNonSktEntity eTdrPengajuanVendorNonSkt, List<TdrPerbankanVendorNonSktEntity> eTdrPerbankanVendorNonSkt) {
        // List<TdrPerbankanVendorNonSktEntity> tdrPerbankanVendorNonSktEntity = tdrPerbankanVendorNonSkt.find("where id_identitas = :id_identitas", Parameters.with("id_identitas", eTdrIdentitasVendorNonSkt.getId_identitas())).list();
        // List<TdrPerbankanVendorMinioNonSktEntity> tdrPerbankanVendorNonSktMinioList = tdrPerbankanVendorMinioNonSkt.find("where id_identitas = ?1", eTdrIdentitasVendorNonSkt.getId_identitas()).list();
        
        // if(tdrPerbankanVendorNonSktMinioList.size() > 0){
        //     tdrPerbankanVendorMinioNonSkt.delete("where id_identitas = :id", Parameters.with("id", eTdrIdentitasVendorNonSkt.getId_identitas()));
        // }
        
        for (TdrPerbankanVendorNonSktEntity p : eTdrPerbankanVendorNonSkt) {
            try {
                String nama_file = p.getKoran_path();
                String urlPath = folderRoot + "/" + nama_file;
                String pathMinio = eTdrPengajuanVendorNonSkt.getId_pengajuan() + "/Perbankan/" + nama_file;
                Boolean checkFile = this.checkFile(urlPath);

                if(eTdrPengajuanVendorNonSkt.getId_pengajuan() != null){
                    // if(p.getMinio_status() == 1){
                    //     // Jika minio_status = 1, langsung save tanpa upload
                    //     savePerbankanVendorNonSktMinio(p, p.getKoran_path());
                    //     saveUploadLog(eTdrPengajuanVendorNonSkt.getId_pengajuan(), eTdrIdentitasVendorNonSkt.getId_identitas(), "perbankan", null, nama_file, p.getKoran_path(), "SKIPPED", "Already in minio", 1);
                    // } else {
                    if(checkFile){
                        boolean get = this.check_object(bucket, pathMinio);
                        if(!get){
                            Boolean upload = this.uploadLegalDoc(bucket, pathMinio, urlPath);
                            if(upload){
                                try {
                                    savePerbankanVendorNonSktMinio(p, pathMinio, "SUCCESS", null);
                                    // savePerbankanVendorNonSktMinio(p, pathMinio);
                                    // saveUploadLog(eTdrPengajuanVendorNonSkt.getId_pengajuan(), eTdrIdentitasVendorNonSkt.getId_identitas(), "perbankan", null, nama_file, pathMinio, "SUCCESS", null, 1);
                                    LOG.info("data Perbankan berhasil di upload");
                                } catch (Exception e) {
                                    savePerbankanVendorNonSktMinio(p, pathMinio, "FAILED", e.getMessage());
                                    // saveUploadLog(eTdrPengajuanVendorNonSkt.getId_pengajuan(), eTdrIdentitasVendorNonSkt.getId_identitas(), "perbankan", null, nama_file, pathMinio, "FAILED", e.getMessage(), 1);
                                    LOG.error("Perbankan data save minio " + e);
                                }
                            } else {
                                savePerbankanVendorNonSktMinio(p, pathMinio, "FAILED", "Upload failed");
                            }
                        } else {
                            try {
                                this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                // savePerbankanVendorNonSktMinio(p, pathMinio);
                                savePerbankanVendorNonSktMinio(p, pathMinio, "SUCCESS", "Re-uploaded");
                                // saveUploadLog(eTdrPengajuanVendorNonSkt.getId_pengajuan(), eTdrIdentitasVendorNonSkt.getId_identitas(), "perbankan", null, nama_file, pathMinio, "SUCCESS", "Re-uploaded", 1);
                                LOG.info("data Perbankan berhasil di upload");
                            } catch (Exception e) {
                                savePerbankanVendorNonSktMinio(p, pathMinio, "FAILED", e.getMessage());
                                // saveUploadLog(eTdrPengajuanVendorNonSkt.getId_pengajuan(), eTdrIdentitasVendorNonSkt.getId_identitas(), "perbankan", null, nama_file, pathMinio, "FAILED", e.getMessage(), 1);
                                LOG.error("Perbankan data save minio " + e);
                            }
                        }
                    } else {
                        savePerbankanVendorNonSktMinio(p, pathMinio, "SKIPPED", "File not exist");
                        // saveUploadLog(eTdrPengajuanVendorNonSkt.getId_pengajuan(), eTdrIdentitasVendorNonSkt.getId_identitas(), "perbankan", null, nama_file, pathMinio, "SKIPPED", "File not exist", 1);
                        LOG.info("File not exist " + nama_file);
                    }
                // }
                } else {
                    LOG.info("Id Pengajuan Not Exists");
                }
            // }
            } catch (Exception e) {
                savePerbankanVendorNonSktMinio(p, null, "FAILED", e.getMessage());
                LOG.error("Error uploading file : " + e.getMessage());

                // saveUploadLog(eTdrPengajuanVendorNonSkt.getId_pengajuan(), eTdrIdentitasVendorNonSkt.getId_identitas(), "perbankan", null, null, null, "FAILED", e.getMessage(), 1);
                // try {
                //     Thread.sleep(1000);
                // } catch (Exception k) {
                //     LOG.error("perbankan k = " + k);
                // }
                // LOG.error("perbankan g = " + e);
            }
        }
    }

    /**
     * Helper method untuk save identitas vendor minio non skt
     */
    private void saveIdentitasVendorNonSktMinio(TdrPengajuanVendorNonSktEntity eTdrPengajuanVendorNonSkt, int dokNumber, String fileName, String namaFile, String pathMinio, String status, String errorMessage) {
        try {
            TdrIdentitasVendorMinioNonSktEntity entityTdrIdentitasNonSkt = new TdrIdentitasVendorMinioNonSktEntity();
            entityTdrIdentitasNonSkt.setId_pengajuan(eTdrPengajuanVendorNonSkt.getId_pengajuan());
            entityTdrIdentitasNonSkt.setIdIdentitas(eTdrPengajuanVendorNonSkt.getId_identitas());
            entityTdrIdentitasNonSkt.setDok(dokNumber);
            entityTdrIdentitasNonSkt.setDokLegalitasPath(fileName);
            // entityTdrIdentitasNonSkt.setIs_eproc(0);
            entityTdrIdentitasNonSkt.setNama_dok(namaFile);
            entityTdrIdentitasNonSkt.setPath_minio(pathMinio);
            entityTdrIdentitasNonSkt.setStatus(status);
            entityTdrIdentitasNonSkt.setErrorMessage(errorMessage);
            entityTdrIdentitasNonSkt.setDate_upload(LocalDateTime.now());
            tdrIdentitasVendorMinioNonSkt.persist(entityTdrIdentitasNonSkt);
        } catch (Exception e) {
            LOG.error("Failed to save identitas vendor log: " + e.getMessage());
        }
    }

    /**
     * Helper method untuk save perbankan vendor minio non skt
     */
    private void savePerbankanVendorNonSktMinio(TdrPerbankanVendorNonSktEntity eTdrPerbankanVendorNonSkt, String pathMinio, String status, String errorMessage) {
        try {
            TdrPerbankanVendorMinioNonSktEntity entityTdrPerbankan = new TdrPerbankanVendorMinioNonSktEntity();
            entityTdrPerbankan.setId_perbankan(eTdrPerbankanVendorNonSkt.getId_perbankan());
            entityTdrPerbankan.setId_identitas(eTdrPerbankanVendorNonSkt.getId_identitas());
            entityTdrPerbankan.setNama_bank(eTdrPerbankanVendorNonSkt.getNama_bank());
            entityTdrPerbankan.setNama_rek(eTdrPerbankanVendorNonSkt.getNama_rek());
            entityTdrPerbankan.setKoran_path(eTdrPerbankanVendorNonSkt.getKoran_path());
            entityTdrPerbankan.setMinio_path(pathMinio);
            entityTdrPerbankan.setStatus(status);
            entityTdrPerbankan.setErrorMessage(errorMessage);
            entityTdrPerbankan.setUpload_date(LocalDateTime.now());
            tdrPerbankanVendorMinioNonSkt.persist(entityTdrPerbankan);
        } catch (Exception e) {
            LOG.error("Failed to save identitas vendor log: " + e.getMessage());
        }
    }

    /**
     * Helper method untuk update status minio NON SKT
     */
    private void updateStatusMinioIdentitasNonSkt(TdrPengajuanVendorNonSktEntity eTdrPengajuanVendorNonSkt, TdrIdentitasVendorNonSktEntity eTdrIdentitasVendorNonSkt) {
        try {
            List<TdrIdentitasVendorMinioNonSktEntity> tdrIdentitasVendorNonSktMinioEntityList = tdrIdentitasVendorMinioNonSkt.find("id_pengajuan = ?1", eTdrPengajuanVendorNonSkt.getId_pengajuan()).list();
            // List<TdrPerbankanVendorMinioNonSktEntity> tdrPerbankanVendorNonSktMinioEntityList = tdrPerbankanVendorMinioNonSkt.find("where id_identitas = ?1", eTdrIdentitasVendorNonSkt.getId_identitas()).list();

            if(tdrIdentitasVendorNonSktMinioEntityList.size() > 0){
                TdrIdentitasVendorNonSktEntity entityIdentitasVendor = tdrIdentitasVendorNonSkt.find("where id_identitas = :id_identitas", Parameters.with("id_identitas", eTdrIdentitasVendorNonSkt.getId_identitas())).firstResult();
                entityIdentitasVendor.setIs_minio_identitas_vendor(1);
                tdrIdentitasVendorNonSkt.persist(entityIdentitasVendor);
                LOG.info("Upload Non SKT done = " + eTdrIdentitasVendorNonSkt.getId_identitas());
            }
        } catch (Exception e) {
            LOG.error("error = " + e.getMessage() + " id_pengajuan = " + eTdrPengajuanVendorNonSkt.getId_pengajuan());
        }
    }

    /**
     * Helper method untuk update status minio NON SKT
     */
    private void updateStatusMinioPerbankanNonSkt(TdrPengajuanVendorNonSktEntity eTdrPengajuanVendorNonSkt, List<TdrPerbankanVendorNonSktEntity> eTdrPerbankanVendorNonSkt) {
        try {
            // List<TdrIdentitasVendorMinioNonSktEntity> tdrIdentitasVendorNonSktMinioEntityList = tdrIdentitasVendorMinioNonSkt.find("id_pengajuan = ?1", eTdrPengajuanVendorNonSkt.getId_pengajuan()).list();
            List<TdrPerbankanVendorMinioNonSktEntity> tdrPerbankanVendorNonSktMinioEntityList = tdrPerbankanVendorMinioNonSkt.find("where id_identitas = ?1", eTdrPengajuanVendorNonSkt.getId_identitas()).list();

            if(tdrPerbankanVendorNonSktMinioEntityList.size() > 0){
                for (TdrPerbankanVendorMinioNonSktEntity value : tdrPerbankanVendorNonSktMinioEntityList) {
                    TdrPerbankanVendorNonSktEntity entityPerbankanVendor = tdrPerbankanVendorNonSkt.find("where id_perbankan = :id_perbankan", Parameters.with("id_perbankan", value.getId_perbankan())).firstResult();
                    entityPerbankanVendor.setIs_minio(1);
                    tdrPerbankanVendorNonSkt.persist(entityPerbankanVendor);
                    LOG.info("Upload Non SKT done = " + value.getId_perbankan());
                }
            }
        } catch (Exception e) {
            LOG.error("error = " + e.getMessage() + " id_pengajuan = " + eTdrPengajuanVendorNonSkt.getId_pengajuan());
        }
    }


    // @Scheduled(every = "60s")
    // @Transactional

    // @Scheduled(every = "60s")
    // public void scheduled_upload_non_skt(){
    //     CompletableFuture.supplyAsync(() -> {
    //         // Simulate some long-running task
    //         try {
    //             Thread.sleep(1000); // Simulating a delay
    //             // System.out.println("Task completed!");
    //             this.upload_non_skt();
    //             LOG.info("task Completed");
    //         } catch (InterruptedException e) {
    //             Thread.currentThread().interrupt();
    //             // System.out.println("Task interrupted!");
    //             LOG.info("task Interupted");
    //         }
    //         return "Task Berhasil";
    //     }, executor).thenAccept(result -> {
    //         // Process the result if needed
    //         System.out.println("Result: " + result);
    //     });
    // }


    // ============================================
    // MAIN UPLOAD METHODS NON SKT
    // ============================================
    
    /**
     * Scheduler untuk upload otomatis setiap 60 detik
     */

    // @Scheduled(every = "60s")
    // @Transactional
    public void upload_non_skt(){
        upload_non_skt_process(null);
    }

    /**
     * Method untuk upload berdasarkan id_pengajuan tertentu
     */
    // @Transactional
    public void upload_non_skt_by_id_pengajuan(String idPengajuan){
        upload_non_skt_process(idPengajuan);
    }

    /**
     * Core method untuk proses upload NON SKT
     */
    // @Transactional
    public void upload_non_skt_process(String specificIdPengajuan){
        LOG.info("==================================================");
        LOG.info("============= Upload Non SKT begin ===============");
        LOG.info("==================================================");
        try {
            String bucket = ConfigProvider.getConfig().getValue("bucket.minio", String.class);
            boolean a = this.checkBucket(bucket);
            String folderRoot = ConfigProvider.getConfig().getValue("root.folder.sso", String.class);
           
            if(!a){
                this.createBucket(bucket);
            }
            LOG.info("Upload Non SKT Begin" + (specificIdPengajuan != null ? " for ID Pengajuan: " + specificIdPengajuan : ""));
            
            List<TdrPengajuanVendorNonSktEntity> eTdrPengajuanVendorNonSktList;
            if(specificIdPengajuan != null && !specificIdPengajuan.isEmpty()) {
                // Upload berdasarkan id_pengajuan tertentu
                eTdrPengajuanVendorNonSktList = tdrPengajuanVendorNonSkt.find("id_pengajuan = ?1 AND id_vendor_eproc is not null", specificIdPengajuan).list();
            } else {
                // Upload scheduled (semua data)
                eTdrPengajuanVendorNonSktList = tdrPengajuanVendorNonSkt.find("where id_pengajuan is not null AND id_vendor_eproc is not null").page(0, 50).list();
            }

            LOG.info("[DATA] Total Pengajuan vendor Non SKT found: " + eTdrPengajuanVendorNonSktList.size());
            if (eTdrPengajuanVendorNonSktList.isEmpty()) {
                LOG.info("[INFO] No pending vendor non skt to process.");
                LOG.info("Send Upload Data Eproc Non SKT Finished");
                return;
            }

            for(TdrPengajuanVendorNonSktEntity elTdrPengajuanVendorNonSkt: eTdrPengajuanVendorNonSktList){ 
                try {
                    // Panggil method dengan @Transactional per vendor
                    processOneVendorNonSkt(elTdrPengajuanVendorNonSkt, bucket, folderRoot);
                } catch (Exception e) {
                    LOG.error("[ERROR] Failed to process vendor: " + elTdrPengajuanVendorNonSkt.getId_pengajuan() + " - Error: " + e.getMessage(), e);
                    // Continue ke vendor berikutnya meskipun ada error
                }
            }
            
            
            LOG.info("==============================================");
            LOG.info("=========== Upload Non SKT Finished ==============");
            LOG.info("==============================================");
        } catch (Exception e) {
            // TODO: handle exception
            LOG.error("error upload NON Non SKT", e);
            LOG.error("Identitas Vendor NON SKT update "+e.getMessage());
        }              
    }

    /**
     * Process satu vendor dalam satu transaction
     * Method ini menggunakan @Transactional sehingga setiap vendor punya transaction sendiri
     */
    @Transactional
    public void processOneVendorNonSkt(TdrPengajuanVendorNonSktEntity elTdrPengajuanVendorNonSkt, String bucket, String folderRoot) {
        LOG.info("[START] Processing vendor SKT: " + elTdrPengajuanVendorNonSkt.getId_pengajuan());
        
        // Validasi data vendor
        if (elTdrPengajuanVendorNonSkt == null || elTdrPengajuanVendorNonSkt.getId_identitas() == null || elTdrPengajuanVendorNonSkt.getId_identitas() == 0) {
            LOG.info("[SKIP] Id Identitas Not Exists for pengajuan: " + (elTdrPengajuanVendorNonSkt != null ? elTdrPengajuanVendorNonSkt.getId_pengajuan() : "NULL"));
            return;
        }

        if (elTdrPengajuanVendorNonSkt == null || elTdrPengajuanVendorNonSkt.getId_pengajuan() == null || elTdrPengajuanVendorNonSkt.getId_pengajuan().isEmpty()) {
            LOG.info("[SKIP] Id Pengajuan Not Exists for pengajuan: " + (elTdrPengajuanVendorNonSkt != null ? elTdrPengajuanVendorNonSkt.getId_pengajuan() : "NULL"));
            return;
        }

        // Process Identitas Minio 
        try {
            processIdentitasNonSkt(elTdrPengajuanVendorNonSkt, bucket, folderRoot);
        } catch (Exception e) {
            saveLogEproc(elTdrPengajuanVendorNonSkt.getId_log_eproc(), "SCU00");
            LOG.error("[ERROR] Failed to process identitas for id_identitas: " + elTdrPengajuanVendorNonSkt.getId_identitas() + " - Error: " + e.getMessage(), e);
        }

        // Perbankan Minio
        try {
            processPerbankanNonSkt(elTdrPengajuanVendorNonSkt, bucket, folderRoot);
        } catch (Exception e) {
            saveLogEproc(elTdrPengajuanVendorNonSkt.getId_log_eproc(), "SCU20");
            LOG.error("[ERROR] Failed to process perbanakan for id_identitas: " + elTdrPengajuanVendorNonSkt.getId_identitas() + " - Error: " + e.getMessage(), e);
        }
    }

    /**
     * Process identitas vendor dalam transaction yang sama dengan processOneVendor
     */
    private void processIdentitasNonSkt(TdrPengajuanVendorNonSktEntity elTdrPengajuanVendorNonSkt, String bucket, String folderRoot) throws Exception {
        LOG.info("[IDENTITAS] Start processing for id_identitas: " + elTdrPengajuanVendorNonSkt.getId_identitas());
        
        TdrIdentitasVendorNonSktEntity eTdrIdentitasVendorNonSkt = tdrIdentitasVendorNonSkt.find("id_identitas = ?1 AND (is_minio_identitas_vendor is null OR is_minio_identitas_vendor = ?2)", elTdrPengajuanVendorNonSkt.getId_identitas(),0).firstResult();
        if (eTdrIdentitasVendorNonSkt == null) {
            LOG.warn("[NOT FOUND] Identitas tidak ditemukan untuk id_identitas: " + elTdrPengajuanVendorNonSkt.getId_identitas());
            return;
        }
    
        if(eTdrIdentitasVendorNonSkt.getDok_legalitas_path() == null){
            LOG.info("[NOT FOUND] Dokumen Legalitas Not Exist: "+eTdrIdentitasVendorNonSkt.getId_identitas());
            return;
        }
    
        // varDump("Tdr Pengajuan", tdrPengajuanVendorNonSkt);
        // varDump("Tdr Identitas", eTdrIdentitasVendorNonSkt);

        DokLegalDto DokLegalObject = this.parseJson(eTdrIdentitasVendorNonSkt.getDok_legalitas_path());
        
        // try {
        List<TdrIdentitasVendorMinioNonSktEntity> tdrIdentitasVendorNonSktMinioList = tdrIdentitasVendorMinioNonSkt.find("id_pengajuan = ?1", elTdrPengajuanVendorNonSkt.getId_pengajuan()).list();
        if(tdrIdentitasVendorNonSktMinioList.size() > 0){
            tdrIdentitasVendorMinioNonSkt.delete("id_pengajuan = ?1", elTdrPengajuanVendorNonSkt.getId_pengajuan());
        }

        // Upload semua dokumen
        if(DokLegalObject.getTxt_dok3() != null){
            uploadDokumenIdentitasNonSkt(bucket, folderRoot, elTdrPengajuanVendorNonSkt, eTdrIdentitasVendorNonSkt, DokLegalObject.getTxt_dok3(), 3);
        }

        if(DokLegalObject.getTxt_dok8() != null){
            uploadDokumenIdentitasNonSkt(bucket, folderRoot, elTdrPengajuanVendorNonSkt, eTdrIdentitasVendorNonSkt, DokLegalObject.getTxt_dok8(), 8);
        }

        if(DokLegalObject.getTxt_dok9() != null){
            uploadDokumenIdentitasNonSkt(bucket, folderRoot, elTdrPengajuanVendorNonSkt, eTdrIdentitasVendorNonSkt, DokLegalObject.getTxt_dok9(), 9);
        }

        if(DokLegalObject.getTxt_dok12() != null){
            uploadDokumenIdentitasNonSkt(bucket, folderRoot, elTdrPengajuanVendorNonSkt, eTdrIdentitasVendorNonSkt, DokLegalObject.getTxt_dok12(), 12);
        }

        if(DokLegalObject.getTxt_dok26() != null){
            uploadDokumenIdentitasNonSkt(bucket, folderRoot, elTdrPengajuanVendorNonSkt, eTdrIdentitasVendorNonSkt, DokLegalObject.getTxt_dok26(), 26);
        }

        // ===== UPDATE STATUS MINIO =====
        updateStatusMinioIdentitasNonSkt(elTdrPengajuanVendorNonSkt, eTdrIdentitasVendorNonSkt);
        saveLogEproc(elTdrPengajuanVendorNonSkt.getId_log_eproc(), "SCU11");

        LOG.info("[IDENTITAS] Finish processing for id_identitas: " + elTdrPengajuanVendorNonSkt.getId_identitas());

        //     } catch (Exception e) {
        //     saveUploadLog(elTdrPengajuanVendorNonSkt.getId_pengajuan(), eTdrIdentitasVendorNonSkt.getId_identitas(), "identitas", null, null, null, "FAILED", "Error processing documents: " + e.getMessage(), 1);
        //     try {
        //         Thread.sleep(1000);
        //     } catch (Exception f) {
        //         LOG.error(f);
        //     }
        //     LOG.info("error = " + e);
        // }
    }

    /**
     * Process perbankan vendor dalam transaction yang sama dengan processOneVendor
     */
    private void processPerbankanNonSkt(TdrPengajuanVendorNonSktEntity elTdrPengajuanVendorNonSkt, String bucket, String folderRoot) throws Exception {
        LOG.info("[PERBANKAN] Start processing for id_identitas: " + elTdrPengajuanVendorNonSkt.getId_identitas());
        
        List<TdrPerbankanVendorNonSktEntity> eTdrPerbankanVendorNonSkt = tdrPerbankanVendorNonSkt.find("where id_identitas = ?1 AND (is_minio is null OR is_minio = ?2)", elTdrPengajuanVendorNonSkt.getId_identitas(), 0).list();
        if (eTdrPerbankanVendorNonSkt == null) {
            LOG.warn("[NOT FOUND] Perbankan tidak ditemukan untuk id_identitas: " + elTdrPengajuanVendorNonSkt.getId_identitas());
            return;
        }

        // Delete existing minio records
        List<TdrPerbankanVendorMinioSktEntity> tdrPerbankanVendorSktList = tdrPerbankanVendorMinioSkt.find("where id_identitas = ?1", elTdrPengajuanVendorNonSkt.getId_identitas()).list();
        if(tdrPerbankanVendorSktList.size() > 0){
            tdrPerbankanVendorMinioSkt.delete("where id_identitas = :id", Parameters.with("id", elTdrPengajuanVendorNonSkt.getId_identitas()));
        }
            
        // ===== UPLOAD PERBANKAN =====
        uploadPerbankanNonSkt(bucket, folderRoot, elTdrPengajuanVendorNonSkt, eTdrPerbankanVendorNonSkt);

        // ===== UPDATE STATUS MINIO =====
        updateStatusMinioPerbankanNonSkt(elTdrPengajuanVendorNonSkt, eTdrPerbankanVendorNonSkt);
        saveLogEproc(elTdrPengajuanVendorNonSkt.getId_log_eproc(), "SCR01");

        LOG.info("[PERBANKAN] Finish processing for id_identitas: " + elTdrPengajuanVendorNonSkt.getId_identitas());
    }


    public Boolean uploadLegalDoc(String bucket, String pathMinio, String pathUrl){
        try {
            this.push_upload(bucket, pathMinio, pathUrl);
            return true;
        } catch (Exception e) {
            // TODO: handle exception
            LOG.error(e);
            return false;
        }
    }

    public Boolean checkFile(String urlFile){
        File file = new File(urlFile);
        if(file.exists()){
            if(file.isFile()){
                return true;
            }else{
                return false;
            }
            
        }else{
            return false;
        }
    }
    
    public boolean checkBucket(String Bucket){ 
        try {
            return MinioClient.bucketExists(BucketExistsArgs.builder().bucket(Bucket).build()).get();
        } catch (Exception e) {
            LOG.info("check Bucket "+e);
            return false;
            // TODO: handle exception
        }
    }

    public void createBucket(String Bucket){
        try {
            MinioClient.makeBucket(MakeBucketArgs.builder().bucket(Bucket).build()).get();
            // createBucketAsync.isDone(); 
            // LOG.info();   
        } catch (Exception e) {
            LOG.error(e);
            // TODO: handle exception
        }
    }
    
    public Boolean push_upload(String bucket, String object, String pathUrl){
        try {
            MinioClient.uploadObject(UploadObjectArgs.builder().bucket(bucket).object(object).filename(pathUrl).build()).get();
            return true;
        } catch (Exception e) {
            LOG.info(e);
            return false;
            // TODO: handle exception
        }
    }

    public boolean check_object(String bucket, String object){
        try {
            MinioClient.getObject(GetObjectArgs.builder().bucket(bucket).object(object).build()).get();
            return true;
        } catch (Exception f) {
            // TODO: handle exception
            // System.out.println(f);
            LOG.info(f);
            return false;
        }
        // return checkObjectAsync.is;
    }

    public DokLegalDto parseJson(String jsonString) {
        try {
            return objectMapper.readValue(jsonString, DokLegalDto.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON", e);
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
