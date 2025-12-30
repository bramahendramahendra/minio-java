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

import org.acme.controllers.upload_minio.dto.DokLegalDto;
import org.acme.controllers.upload_minio.dto.UploaderDto;

import org.acme.controllers.upload_minio.entity.TdrPengajuanVendorSktEntity;
import org.acme.controllers.upload_minio.entity.TdrPengajuanVendorNonSktEntity;
import org.acme.controllers.upload_minio.entity.TdrIdentitasVendorSktEntity;
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
    private void saveUploadLog(String idPengajuan, Long idIdentitas, String dokType, Integer dokNumber, String fileName, String pathMinio, String status, String errorMessage, Integer statusSkt) {
        try {
            TdrUploadMinioLogEntity entityTdrUpload = new TdrUploadMinioLogEntity();
            entityTdrUpload.setIdPengajuan(idPengajuan);
            entityTdrUpload.setIdIdentitas(idIdentitas);
            entityTdrUpload.setDokType(dokType);
            entityTdrUpload.setDokNumber(dokNumber);
            entityTdrUpload.setFileName(fileName);
            entityTdrUpload.setPathMinio(pathMinio);
            entityTdrUpload.setStatus(status);
            entityTdrUpload.setErrorMessage(errorMessage);
            entityTdrUpload.setUploadDate(LocalDateTime.now());
            entityTdrUpload.setStatusSkt(statusSkt);
            tdrUploadMinioLogRepository.persist(entityTdrUpload);
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
            
            TdrMstFileLegalitasEntity file = tdrFileLegal.find("where dok = :dok", Parameters.with("dok", dokNumber)).firstResult();
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
                            saveIdentitasVendorSktMinio(eTdrPengajuanVendorSkt, dokNumber, nama_file, pathMinio);
                            saveUploadLog(eTdrPengajuanVendorSkt.getId_pengajuan(), eTdrIdentitasVendorSkt.getId_identitas(), "identitas", dokNumber, fileName, pathMinio, "SUCCESS", null, 1);
                            LOG.info("data berhasil di upload");
                        } else {
                            saveUploadLog(eTdrPengajuanVendorSkt.getId_pengajuan(), eTdrIdentitasVendorSkt.getId_identitas(), "identitas", dokNumber, fileName, pathMinio, "FAILED", "Upload failed", 1);
                        }
                    } else {
                        this.uploadLegalDoc(bucket, pathMinio, urlPath);
                        saveIdentitasVendorSktMinio(eTdrPengajuanVendorSkt, dokNumber, nama_file, pathMinio);
                        saveUploadLog(eTdrPengajuanVendorSkt.getId_pengajuan(), eTdrIdentitasVendorSkt.getId_identitas(), "identitas", dokNumber, fileName, pathMinio, "SUCCESS", "Re-uploaded", 1);
                        LOG.info("data berhasil di upload");
                    }
                } else {
                    saveUploadLog(eTdrPengajuanVendorSkt.getId_pengajuan(), eTdrIdentitasVendorSkt.getId_identitas(), "identitas", dokNumber, fileName, pathMinio, "SKIPPED", "File not exist", 1);
                    LOG.info("File Not Exist " + fileName);
                }
            } else {
                LOG.info("Id Pengajuan Not Exists");
            }
        } catch (Exception e) {
            saveUploadLog(eTdrPengajuanVendorSkt.getId_pengajuan(), eTdrIdentitasVendorSkt.getId_identitas(), "identitas", dokNumber, fileName, null, "FAILED", e.getMessage(), 1);
            LOG.error("Error uploading dok " + dokNumber + ": " + e.getMessage());
        }
    }

    /**
     * Helper method untuk save identitas vendor minio
     */
    private void saveIdentitasVendorSktMinio(TdrPengajuanVendorSktEntity eTdrPengajuanVendorSkt, int dokNumber, String namaFile, String pathMinio) {
        try {
            TdrIdentitasVendorMinioSktEntity entityTdrIdentitasSkt = new TdrIdentitasVendorMinioSktEntity();
            entityTdrIdentitasSkt.setId_pengajuan(eTdrPengajuanVendorSkt.getId_pengajuan());
            entityTdrIdentitasSkt.setDok(dokNumber);
            entityTdrIdentitasSkt.setIs_eproc(0);
            entityTdrIdentitasSkt.setNama_dok(namaFile);
            entityTdrIdentitasSkt.setPath_minio(pathMinio);
            entityTdrIdentitasSkt.setStatus_skt(1);
            entityTdrIdentitasSkt.setDate_upload(LocalDateTime.now());
            tdrIdentitasVendorMinioSkt.persist(entityTdrIdentitasSkt);
        } catch (Exception e) {
            LOG.error("Failed to save identitas vendor log: " + e.getMessage());
        }
    }

    /**
     * Helper method untuk upload pengalaman vendor
     */
    private void uploadPengalamanSkt(String bucket, String folderRoot, TdrPengajuanVendorSktEntity eTdrPengajuanVendorSkt, TdrIdentitasVendorSktEntity eTdrIdentitasVendorSkt) {
        try {
            List<TdrPengalamanVendorSktEntity> getPengalaman = tdrPengalamanVendorSkt.find("where id_identitas = :id_identitas", Parameters.with("id_identitas", eTdrIdentitasVendorSkt.getId_identitas())).list();
            if (getPengalaman == null) {
                LOG.warn("[NOT FOUND] Pengalaman Vendor tidak ditemukan untuk id_identitas: " + eTdrPengajuanVendorSkt.getId_identitas());
                LOG.info("[INFO] No pending vendors to process.");
                LOG.info("Send Data Eproc SKT Finished");
                return;
            }
            
            List<TdrPengalamanVendorMinioSktEntity> tdrPengalamanVendorSktList = tdrPengalamanVendorMinioSkt.find("where id_identitas = ?1", eTdrIdentitasVendorSkt.getId_identitas()).list();
            
            if(tdrPengalamanVendorSktList.size() > 0){
                tdrPengalamanVendorMinioSkt.delete("where id_identitas = :id", Parameters.with("id", eTdrIdentitasVendorSkt.getId_identitas()));
            }

            for (TdrPengalamanVendorSktEntity f : getPengalaman) {
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
                                    savePengalamanVendorSktMinio(f, pathMinio);
                                    saveUploadLog(eTdrPengajuanVendorSkt.getId_pengajuan(), eTdrIdentitasVendorSkt.getId_identitas(), "pengalaman", null, nama_file, pathMinio, "SUCCESS", null, 1);
                                    LOG.info("data Pengalaman berhasil di upload");
                                } catch (Exception e) {
                                    saveUploadLog(eTdrPengajuanVendorSkt.getId_pengajuan(), eTdrIdentitasVendorSkt.getId_identitas(), "pengalaman", null, nama_file, pathMinio, "FAILED", e.getMessage(), 1);
                                    LOG.error("Pengalaman Save Minio " + e);
                                }
                            }
                        } else {
                            try {
                                this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                savePengalamanVendorSktMinio(f, pathMinio);
                                saveUploadLog(eTdrPengajuanVendorSkt.getId_pengajuan(), eTdrIdentitasVendorSkt.getId_identitas(), "pengalaman", null, nama_file, pathMinio, "SUCCESS", "Re-uploaded", 1);
                                LOG.info("data Pengalaman berhasil di upload");
                            } catch (Exception e) {
                                saveUploadLog(eTdrPengajuanVendorSkt.getId_pengajuan(), eTdrIdentitasVendorSkt.getId_identitas(), "pengalaman", null, nama_file, pathMinio, "FAILED", e.getMessage(), 1);
                                LOG.error("Pengalaman Save Minio " + e);
                            }
                        }
                    } else {
                        saveUploadLog(eTdrPengajuanVendorSkt.getId_pengajuan(), eTdrIdentitasVendorSkt.getId_identitas(), "pengalaman", null, nama_file, pathMinio, "SKIPPED", "File not exist", 1);
                        LOG.info("File not exist " + nama_file);
                    }
                } else {
                    LOG.info("Id Pengajuan Not Exists");
                }
            }
        } catch (Exception g) {
            saveUploadLog(eTdrPengajuanVendorSkt.getId_pengajuan(), eTdrIdentitasVendorSkt.getId_identitas(), "pengalaman", null, null, null, "FAILED", g.getMessage(), 1);
            try {
                Thread.sleep(1000);
            } catch (Exception k) {
                LOG.error("pengalaman k = " + k);
            }
            LOG.error("pengalaman g = " + g);
        }
    }

    /**
     * Helper method untuk save pengalaman vendor minio
     */
    private void savePengalamanVendorSktMinio(TdrPengalamanVendorSktEntity eTdrPengalamanVendorSkt, String pathMinio) {
        TdrPengalamanVendorMinioSktEntity entityTdrPengalaman = new TdrPengalamanVendorMinioSktEntity();
        entityTdrPengalaman.setId_identitas(eTdrPengalamanVendorSkt.getId_identitas());
        entityTdrPengalaman.setNama_pekerjaan(eTdrPengalamanVendorSkt.getNama_pekerjaan());
        entityTdrPengalaman.setNama_pemilik_pekerja(eTdrPengalamanVendorSkt.getNama_pemilik_pekerja());
        entityTdrPengalaman.setBidang_usaha(eTdrPengalamanVendorSkt.getBidang_usaha());
        entityTdrPengalaman.setSub_bidang_usaha(eTdrPengalamanVendorSkt.getSub_bidang_usaha());
        entityTdrPengalaman.setPemberi_tugas(eTdrPengalamanVendorSkt.getPemberi_tugas());
        entityTdrPengalaman.setNo_spk(eTdrPengalamanVendorSkt.getNo_spk());
        entityTdrPengalaman.setTgl_mulai_spk(eTdrPengalamanVendorSkt.getTgl_mulai_spk());
        entityTdrPengalaman.setTgl_selesai_spk(eTdrPengalamanVendorSkt.getTgl_selesai_spk());
        entityTdrPengalaman.setNilai_kontrak(eTdrPengalamanVendorSkt.getNilai_kontrak());
        entityTdrPengalaman.setBa_pekerjaan(eTdrPengalamanVendorSkt.getBa_pekerjaan());
        entityTdrPengalaman.setSpk_path(eTdrPengalamanVendorSkt.getSpk_path());
        entityTdrPengalaman.setMinio_path(pathMinio);
        entityTdrPengalaman.setUpload_minio(LocalDateTime.now());
        tdrPengalamanVendorMinioSkt.persist(entityTdrPengalaman);
    }

    /**
     * Helper method untuk upload perbankan vendor
     */
    private void uploadPerbankanSkt(String bucket, String folderRoot, TdrPengajuanVendorSktEntity eTdrPengajuanVendorSkt, TdrIdentitasVendorSktEntity eTdrIdentitasVendorSkt) {
        try {
            List<TdrPerbankanVendorSktEntity> tdrPerbankanVendorSktEntity = tdrPerbankanVendorSkt.find("where id_identitas = :id_identitas", Parameters.with("id_identitas", eTdrIdentitasVendorSkt.getId_identitas())).list();
            List<TdrPerbankanVendorMinioSktEntity> tdrPerbankanVendorSktMinioList = tdrPerbankanVendorMinioSkt.find("where id_identitas = ?1", eTdrIdentitasVendorSkt.getId_identitas()).list();

            if(tdrPerbankanVendorSktMinioList.size() > 0){
                tdrPerbankanVendorMinioSkt.delete("where id_identitas = :id", Parameters.with("id", eTdrIdentitasVendorSkt.getId_identitas()));
            }

            for (TdrPerbankanVendorSktEntity p : tdrPerbankanVendorSktEntity) {
                String nama_file = p.getKoran_path();
                String urlPath = folderRoot + "/" + nama_file;
                String pathMinio = eTdrPengajuanVendorSkt.getId_pengajuan() + "/Perbankan/" + nama_file;
                Boolean checkFile = this.checkFile(urlPath);

                if(eTdrPengajuanVendorSkt.getId_pengajuan() != null){
                    if(p.getMinio_status() == 1){
                        // Jika minio_status = 1, langsung save tanpa upload
                        savePerbankanVendorSktMinio(p, p.getKoran_path());
                        saveUploadLog(eTdrPengajuanVendorSkt.getId_pengajuan(), eTdrIdentitasVendorSkt.getId_identitas(), "perbankan", null, nama_file, p.getKoran_path(), "SKIPPED", "Already in minio", 1);
                    } else {
                        if(checkFile){
                            boolean get = this.check_object(bucket, pathMinio);
                            if(!get){
                                Boolean upload = this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                if(upload){
                                    try {
                                        savePerbankanVendorSktMinio(p, pathMinio);
                                        saveUploadLog(eTdrPengajuanVendorSkt.getId_pengajuan(), eTdrIdentitasVendorSkt.getId_identitas(), "perbankan", null, nama_file, pathMinio, "SUCCESS", null, 1);
                                        LOG.info("data Perbankan berhasil di upload");
                                    } catch (Exception e) {
                                        saveUploadLog(eTdrPengajuanVendorSkt.getId_pengajuan(), eTdrIdentitasVendorSkt.getId_identitas(), "perbankan", null, nama_file, pathMinio, "FAILED", e.getMessage(), 1);
                                        LOG.error("Perbankan data save minio " + e);
                                    }
                                }
                            } else {
                                try {
                                    this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                    savePerbankanVendorSktMinio(p, pathMinio);
                                    saveUploadLog(eTdrPengajuanVendorSkt.getId_pengajuan(), eTdrIdentitasVendorSkt.getId_identitas(), "perbankan", null, nama_file, pathMinio, "SUCCESS", "Re-uploaded", 1);
                                    LOG.info("data Perbankan berhasil di upload");
                                } catch (Exception e) {
                                    saveUploadLog(eTdrPengajuanVendorSkt.getId_pengajuan(), eTdrIdentitasVendorSkt.getId_identitas(), "perbankan", null, nama_file, pathMinio, "FAILED", e.getMessage(), 1);
                                    LOG.error("Perbankan data save minio " + e);
                                }
                            }
                        } else {
                            saveUploadLog(eTdrPengajuanVendorSkt.getId_pengajuan(), eTdrIdentitasVendorSkt.getId_identitas(), "perbankan", null, nama_file, pathMinio, "SKIPPED", "File not exist", 1);
                            LOG.info("File not exist " + nama_file);
                        }
                    }
                } else {
                    LOG.info("Id Pengajuan Not Exists");
                }
            }
        } catch (Exception e) {
            saveUploadLog(eTdrPengajuanVendorSkt.getId_pengajuan(), eTdrIdentitasVendorSkt.getId_identitas(), "perbankan", null, null, null, "FAILED", e.getMessage(), 1);
            try {
                Thread.sleep(1000);
            } catch (Exception k) {
                LOG.error("perbankan k = " + k);
            }
            LOG.error("perbankan g = " + e);
        }
    }

    /**
     * Helper method untuk save perbankan vendor minio
     */
    private void savePerbankanVendorSktMinio(TdrPerbankanVendorSktEntity eTdrPerbankanVendorSkt, String pathMinio) {
        TdrPerbankanVendorMinioSktEntity entityTdrPerbankan = new TdrPerbankanVendorMinioSktEntity();
        entityTdrPerbankan.setId_identitas(eTdrPerbankanVendorSkt.getId_identitas());
        entityTdrPerbankan.setHave_account_bri(eTdrPerbankanVendorSkt.getHave_account_bri());
        entityTdrPerbankan.setLokasi_bank(eTdrPerbankanVendorSkt.getLokasi_bank());
        entityTdrPerbankan.setKode_bank(eTdrPerbankanVendorSkt.getKode_bank());
        entityTdrPerbankan.setNama_bank(eTdrPerbankanVendorSkt.getNama_bank());
        entityTdrPerbankan.setCity(eTdrPerbankanVendorSkt.getCity());
        entityTdrPerbankan.setBank_key(eTdrPerbankanVendorSkt.getBank_key());
        entityTdrPerbankan.setNegara(eTdrPerbankanVendorSkt.getNegara());
        entityTdrPerbankan.setKode_negara(eTdrPerbankanVendorSkt.getKode_negara());
        entityTdrPerbankan.setNo_rek(eTdrPerbankanVendorSkt.getNo_rek());
        entityTdrPerbankan.setNama_rek(eTdrPerbankanVendorSkt.getNama_rek());
        entityTdrPerbankan.setSwift_kode(eTdrPerbankanVendorSkt.getSwift_kode());
        entityTdrPerbankan.setCabang_bank(eTdrPerbankanVendorSkt.getCabang_bank());
        entityTdrPerbankan.setKoran_path(eTdrPerbankanVendorSkt.getKoran_path());
        entityTdrPerbankan.setMinio_path(pathMinio);
        entityTdrPerbankan.setUpload_minio(LocalDateTime.now());
        tdrPerbankanVendorMinioSkt.persist(entityTdrPerbankan);
    }

    /**
     * Helper method untuk update status minio
     */
    private void updateStatusMinioSkt(TdrPengajuanVendorSktEntity eTdrPengajuanVendorSkt, TdrIdentitasVendorSktEntity eTdrIdentitasVendorSkt) {
        try {
            List<TdrIdentitasVendorMinioSktEntity> tdrIdentitasVendorSktMinioEntityList = tdrIdentitasVendorMinioSkt.find("id_pengajuan = ?1", eTdrPengajuanVendorSkt.getId_pengajuan()).list();
            List<TdrPengalamanVendorMinioSktEntity> tdrPengalamanVendorSktMinioEntityList = tdrPengalamanVendorMinioSkt.find("where id_identitas = ?1", eTdrIdentitasVendorSkt.getId_identitas()).list();
            List<TdrPerbankanVendorMinioSktEntity> tdrPerbankanVendorSktMinioEntityList = tdrPerbankanVendorMinioSkt.find("where id_identitas = ?1", eTdrIdentitasVendorSkt.getId_identitas()).list();
            
            if(tdrIdentitasVendorSktMinioEntityList.size() > 0 && tdrPengalamanVendorSktMinioEntityList.size() > 0 && tdrPerbankanVendorSktMinioEntityList.size() > 0){
                TdrIdentitasVendorSktEntity entityIdentitasVendor = tdrIdentitasVendorSkt.find("where id_identitas = :id_identitas", Parameters.with("id_identitas", eTdrIdentitasVendorSkt.getId_identitas())).firstResult();
                entityIdentitasVendor.setIs_minio_identitas_vendor(1);
                tdrIdentitasVendorSkt.persist(entityIdentitasVendor);
                LOG.info("Upload SKT done = " + eTdrIdentitasVendorSkt.getId_identitas());
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
    @Transactional
    public void upload_skt(){
        upload_skt_process(null);
    }

    /**
     * Method untuk upload berdasarkan id_pengajuan tertentu
     */
    @Transactional
    public void upload_skt_by_id_pengajuan(String idPengajuan){
        upload_skt_process(idPengajuan);
    }
    
    /**
     * Core method untuk proses upload SKT
     */
    @Transactional
    public void upload_skt_process(String specificIdPengajuan) {
        LOG.info("Upload SKT begin");
        try {
            String bucket = ConfigProvider.getConfig().getValue("bucket.minio", String.class);
            boolean a = this.checkBucket(bucket);
            String folderRoot = ConfigProvider.getConfig().getValue("root.folder.sso", String.class);
            
            if(!a){
                this.createBucket(bucket);
            }
            LOG.info("Upload SKT Begin" + (specificIdPengajuan != null ? " for ID: " + specificIdPengajuan : ""));
            
            List<TdrPengajuanVendorSktEntity> eTdrPengajuanVendorSktList;
            if(specificIdPengajuan != null && !specificIdPengajuan.isEmpty()) {
                // Upload berdasarkan id_pengajuan tertentu
                eTdrPengajuanVendorSktList = tdrPengajuanVendorSkt.find("id_pengajuan = ?1 AND id_vendor_eproc is not null", specificIdPengajuan).list();
            } else {
                // Upload scheduled (semua data)
                eTdrPengajuanVendorSktList = tdrPengajuanVendorSkt.find("where id_pengajuan is not null AND id_vendor_eproc is not null").page(0, 50).list();
            }
            LOG.info("[DATA] Total Pengajuan vendors SKT found: " + eTdrPengajuanVendorSktList.size());
            if (eTdrPengajuanVendorSktList.isEmpty()) {
                LOG.info("[INFO] No pending vendors to process.");
                LOG.info("Send Upload Data Eproc SKT Finished");
                return;
            }

            int dumpLimit = Math.min(5, eTdrPengajuanVendorSktList.size());
            for (int i = 0; i < dumpLimit; i++) {
                varDump("Vendor SKT [" + i + "]", eTdrPengajuanVendorSktList.get(i));
            }
            
            for(TdrPengajuanVendorSktEntity elTdrPengajuanVendorSkt: eTdrPengajuanVendorSktList){ 
                if (elTdrPengajuanVendorSkt == null || elTdrPengajuanVendorSkt.getId_identitas() == null || elTdrPengajuanVendorSkt.getId_identitas() == 0) {
                    LOG.info("[SKIP] Id Identitas Not Exists for pengajuan: " + (elTdrPengajuanVendorSkt != null ? elTdrPengajuanVendorSkt.getId_pengajuan() : "NULL"));
                    continue;
                }

                if (elTdrPengajuanVendorSkt == null || elTdrPengajuanVendorSkt.getId_pengajuan() == null || elTdrPengajuanVendorSkt.getId_pengajuan() == "") {
                    LOG.info("[SKIP] Id Pengajuan Not Exists for pengajuan: " + (elTdrPengajuanVendorSkt != null ? elTdrPengajuanVendorSkt.getId_pengajuan() : "NULL"));
                    continue;
                }

                try {
                    TdrIdentitasVendorSktEntity eTdrIdentitasVendorSkt = tdrIdentitasVendorSkt.find("id_identitas = ?1 AND is_minio_identitas_vendor is null OR is_minio_identitas_vendor = ?2", elTdrPengajuanVendorSkt.getId_identitas(),0).firstResult();
                     if (eTdrIdentitasVendorSkt == null) {
                        LOG.warn("[NOT FOUND] Identitas tidak ditemukan untuk id_identitas: " + elTdrPengajuanVendorSkt.getId_identitas());
                        LOG.info("[INFO] No pending vendors to process.");
                        LOG.info("Send Data Eproc SKT Finished");
                        return;
                    }

                    if(eTdrIdentitasVendorSkt.getDok_legalitas_path() == null){
                        LOG.info("[NOT FOUND] Dokumen Legalitas Not Exist: "+eTdrIdentitasVendorSkt.getId_identitas());
                        return;
                    }

                    // varDump("Tdr Pengajuan", tdrPengajuanVendor);
                    // varDump("Tdr Identitas", i);

                    DokLegalDto DokLegalObject = this.parseJson(eTdrIdentitasVendorSkt.getDok_legalitas_path());
                    try {
                        List<TdrIdentitasVendorMinioSktEntity> tdrIdentitasVendorSktMinioList = tdrIdentitasVendorMinioSkt.find("id_pengajuan = ?1", elTdrPengajuanVendorSkt.getId_pengajuan()).list();
                        if(tdrIdentitasVendorSktMinioList.size() > 0){
                            tdrIdentitasVendorMinioSkt.delete("id_pengajuan = ?1", elTdrPengajuanVendorSkt.getId_pengajuan());
                        }

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

                        if(DokLegalObject.getTxt_dok14() != null){
                            uploadDokumenIdentitasSkt(bucket, folderRoot, elTdrPengajuanVendorSkt, eTdrIdentitasVendorSkt, DokLegalObject.getTxt_dok14(), 14);
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
                    } catch (Exception e) {
                       saveUploadLog(elTdrPengajuanVendorSkt.getId_pengajuan(), eTdrIdentitasVendorSkt.getId_identitas(), "identitas", null, null, null, "FAILED", "Error processing documents: " + e.getMessage(), 1);
                        try {
                            Thread.sleep(1000);
                        } catch (Exception f) {
                            LOG.error(f);
                        }
                        LOG.info("error = " + e);
                    }

                    // ===== UPLOAD PENGALAMAN =====
                    uploadPengalamanSkt(bucket, folderRoot, elTdrPengajuanVendorSkt, eTdrIdentitasVendorSkt);

                    // ===== UPLOAD PERBANKAN =====
                    uploadPerbankanSkt(bucket, folderRoot, elTdrPengajuanVendorSkt, eTdrIdentitasVendorSkt);

                    // ===== UPDATE STATUS MINIO =====
                    updateStatusMinioSkt(elTdrPengajuanVendorSkt, eTdrIdentitasVendorSkt);
    
                } catch (Exception e) {
                    LOG.error("[ERROR] Failed to process identitas for id_identitas: " + elTdrPengajuanVendorSkt.getId_identitas() + " - Error: " + e.getMessage(), e);
                }    
            }
        } catch (Exception e) {
            // TODO: handle exception
            LOG.error("error upload SKT", e);
            LOG.error("Identitas Vendor SKT update "+e.getMessage());
        }
    }

    /**
     * Helper method untuk upload dokumen identitas non skt (mengganti kode repetitif)
     */
    private void uploadDokumenIdentitasNonSkt(String bucket, String folderRoot, TdrPengajuanVendorNonSktEntity eTdrPengajuanVendorNonSkt, TdrIdentitasVendorNonSktEntity eTdrIdentitasVendorNonSkt, String fileName, int dokNumber) {
        try {
            LOG.info("txt dok " + dokNumber + " begin upload id_pengajuan = " + eTdrPengajuanVendorNonSkt.getId_pengajuan());
            
            TdrMstFileLegalitasEntity file = tdrFileLegal.find("where dok = :dok", Parameters.with("dok", dokNumber)).firstResult();
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
                            saveIdentitasVendorNonSktMinio(eTdrPengajuanVendorNonSkt, dokNumber, nama_file, pathMinio);
                            saveUploadLog(eTdrPengajuanVendorNonSkt.getId_pengajuan(), eTdrIdentitasVendorNonSkt.getId_identitas(), "identitas", dokNumber, fileName, pathMinio, "SUCCESS", null, 1);
                            LOG.info("data berhasil di upload");
                        } else {
                            saveUploadLog(eTdrPengajuanVendorNonSkt.getId_pengajuan(), eTdrIdentitasVendorNonSkt.getId_identitas(), "identitas", dokNumber, fileName, pathMinio, "FAILED", "Upload failed", 1);
                        }
                    } else {
                        this.uploadLegalDoc(bucket, pathMinio, urlPath);
                        saveIdentitasVendorNonSktMinio(eTdrPengajuanVendorNonSkt, dokNumber, nama_file, pathMinio);
                        saveUploadLog(eTdrPengajuanVendorNonSkt.getId_pengajuan(), eTdrIdentitasVendorNonSkt.getId_identitas(), "identitas", dokNumber, fileName, pathMinio, "SUCCESS", "Re-uploaded", 1);
                        LOG.info("data berhasil di upload");
                    }
                } else {
                    saveUploadLog(eTdrPengajuanVendorNonSkt.getId_pengajuan(), eTdrIdentitasVendorNonSkt.getId_identitas(), "identitas", dokNumber, fileName, pathMinio, "SKIPPED", "File not exist", 1);
                    LOG.info("File Not Exist " + fileName);
                }
            } else {
                LOG.info("Id Pengajuan Not Exists");
            }
        } catch (Exception e) {
            saveUploadLog(eTdrPengajuanVendorNonSkt.getId_pengajuan(), eTdrIdentitasVendorNonSkt.getId_identitas(), "identitas", dokNumber, fileName, null, "FAILED", e.getMessage(), 1);
            LOG.error("Error uploading dok " + dokNumber + ": " + e.getMessage());
        }
    }

    /**
     * Helper method untuk save identitas vendor NON SKT minio
     */
    private void saveIdentitasVendorNonSktMinio(TdrPengajuanVendorNonSktEntity eTdrPengajuanVendorNonSkt, int dokNumber, String namaFile, String pathMinio) {
        try {
            TdrIdentitasVendorMinioNonSktEntity entityTdrIdentitasNonSkt = new TdrIdentitasVendorMinioNonSktEntity();
            entityTdrIdentitasNonSkt.setId_pengajuan(eTdrPengajuanVendorNonSkt.getId_pengajuan());
            entityTdrIdentitasNonSkt.setDok(dokNumber);
            entityTdrIdentitasNonSkt.setIs_eproc(0);
            entityTdrIdentitasNonSkt.setNama_dok(namaFile);
            entityTdrIdentitasNonSkt.setPath_minio(pathMinio);
            entityTdrIdentitasNonSkt.setStatus_skt(1);
            entityTdrIdentitasNonSkt.setDate_upload(LocalDateTime.now());
            tdrIdentitasVendorMinioNonSkt.persist(entityTdrIdentitasNonSkt);
        } catch (Exception e) {
            LOG.error("Failed to save identitas vendor log: " + e.getMessage());
        }
    }

    /**
     * Helper method untuk upload perbankan vendor NON SKT
     */
    private void uploadPerbankanNonSkt(String bucket, String folderRoot, TdrPengajuanVendorNonSktEntity eTdrPengajuanVendorNonSkt, TdrIdentitasVendorNonSktEntity eTdrIdentitasVendorNonSkt) {
        try {
            List<TdrPerbankanVendorNonSktEntity> tdrPerbankanVendorNonSktEntity = tdrPerbankanVendorNonSkt.find("where id_identitas = :id_identitas", Parameters.with("id_identitas", eTdrIdentitasVendorNonSkt.getId_identitas())).list();
            List<TdrPerbankanVendorMinioNonSktEntity> tdrPerbankanVendorNonSktMinioList = tdrPerbankanVendorMinioNonSkt.find("where id_identitas = ?1", eTdrIdentitasVendorNonSkt.getId_identitas()).list();
            
            if(tdrPerbankanVendorNonSktMinioList.size() > 0){
                tdrPerbankanVendorMinioNonSkt.delete("where id_identitas = :id", Parameters.with("id", eTdrIdentitasVendorNonSkt.getId_identitas()));
            }

            for (TdrPerbankanVendorNonSktEntity p : tdrPerbankanVendorNonSktEntity) {
                String nama_file = p.getKoran_path();
                String urlPath = folderRoot + "/" + nama_file;
                String pathMinio = eTdrPengajuanVendorNonSkt.getId_pengajuan() + "/Perbankan/" + nama_file;
                Boolean checkFile = this.checkFile(urlPath);

                if(eTdrPengajuanVendorNonSkt.getId_pengajuan() != null){
                    if(p.getMinio_status() == 1){
                        // Jika minio_status = 1, langsung save tanpa upload
                        savePerbankanVendorNonSktMinio(p, p.getKoran_path());
                        saveUploadLog(eTdrPengajuanVendorNonSkt.getId_pengajuan(), eTdrIdentitasVendorNonSkt.getId_identitas(), "perbankan", null, nama_file, p.getKoran_path(), "SKIPPED", "Already in minio", 1);
                    } else {
                        if(checkFile){
                            boolean get = this.check_object(bucket, pathMinio);
                            if(!get){
                                Boolean upload = this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                if(upload){
                                    try {
                                        savePerbankanVendorNonSktMinio(p, pathMinio);
                                        saveUploadLog(eTdrPengajuanVendorNonSkt.getId_pengajuan(), eTdrIdentitasVendorNonSkt.getId_identitas(), "perbankan", null, nama_file, pathMinio, "SUCCESS", null, 1);
                                        LOG.info("data Perbankan berhasil di upload");
                                    } catch (Exception e) {
                                        saveUploadLog(eTdrPengajuanVendorNonSkt.getId_pengajuan(), eTdrIdentitasVendorNonSkt.getId_identitas(), "perbankan", null, nama_file, pathMinio, "FAILED", e.getMessage(), 1);
                                        LOG.error("Perbankan data save minio " + e);
                                    }
                                }
                            } else {
                                try {
                                    this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                    savePerbankanVendorNonSktMinio(p, pathMinio);
                                    saveUploadLog(eTdrPengajuanVendorNonSkt.getId_pengajuan(), eTdrIdentitasVendorNonSkt.getId_identitas(), "perbankan", null, nama_file, pathMinio, "SUCCESS", "Re-uploaded", 1);
                                    LOG.info("data Perbankan berhasil di upload");
                                } catch (Exception e) {
                                    saveUploadLog(eTdrPengajuanVendorNonSkt.getId_pengajuan(), eTdrIdentitasVendorNonSkt.getId_identitas(), "perbankan", null, nama_file, pathMinio, "FAILED", e.getMessage(), 1);
                                    LOG.error("Perbankan data save minio " + e);
                                }
                            }
                        } else {
                            saveUploadLog(eTdrPengajuanVendorNonSkt.getId_pengajuan(), eTdrIdentitasVendorNonSkt.getId_identitas(), "perbankan", null, nama_file, pathMinio, "SKIPPED", "File not exist", 1);
                            LOG.info("File not exist " + nama_file);
                        }
                    }
                } else {
                    LOG.info("Id Pengajuan Not Exists");
                }
            }
        } catch (Exception e) {
            saveUploadLog(eTdrPengajuanVendorNonSkt.getId_pengajuan(), eTdrIdentitasVendorNonSkt.getId_identitas(), "perbankan", null, null, null, "FAILED", e.getMessage(), 1);
            try {
                Thread.sleep(1000);
            } catch (Exception k) {
                LOG.error("perbankan k = " + k);
            }
            LOG.error("perbankan g = " + e);
        }
    }

    /**
     * Helper method untuk save perbankan vendor NON SKT minio
     */
    private void savePerbankanVendorNonSktMinio(TdrPerbankanVendorNonSktEntity eTdrPerbankanVendorNonSkt, String pathMinio) {
        TdrPerbankanVendorMinioNonSktEntity entityTdrPerbankan = new TdrPerbankanVendorMinioNonSktEntity();
        entityTdrPerbankan.setId_identitas(eTdrPerbankanVendorNonSkt.getId_identitas());
        entityTdrPerbankan.setHave_account_bri(eTdrPerbankanVendorNonSkt.getHave_account_bri());
        entityTdrPerbankan.setLokasi_bank(eTdrPerbankanVendorNonSkt.getLokasi_bank());
        entityTdrPerbankan.setKode_bank(eTdrPerbankanVendorNonSkt.getKode_bank());
        entityTdrPerbankan.setNama_bank(eTdrPerbankanVendorNonSkt.getNama_bank());
        entityTdrPerbankan.setCity(eTdrPerbankanVendorNonSkt.getCity());
        entityTdrPerbankan.setBank_key(eTdrPerbankanVendorNonSkt.getBank_key());
        entityTdrPerbankan.setNegara(eTdrPerbankanVendorNonSkt.getNegara());
        entityTdrPerbankan.setKode_negara(eTdrPerbankanVendorNonSkt.getKode_negara());
        entityTdrPerbankan.setNo_rek(eTdrPerbankanVendorNonSkt.getNo_rek());
        entityTdrPerbankan.setNama_rek(eTdrPerbankanVendorNonSkt.getNama_rek());
        entityTdrPerbankan.setSwift_kode(eTdrPerbankanVendorNonSkt.getSwift_kode());
        entityTdrPerbankan.setCabang_bank(eTdrPerbankanVendorNonSkt.getCabang_bank());
        entityTdrPerbankan.setKoran_path(eTdrPerbankanVendorNonSkt.getKoran_path());
        entityTdrPerbankan.setMinio_path(pathMinio);
        entityTdrPerbankan.setUpload_minio(LocalDateTime.now());
        tdrPerbankanVendorMinioNonSkt.persist(entityTdrPerbankan);
    }

    /**
     * Helper method untuk update status minio NON SKT
     */
    private void updateStatusMinioNonSkt(TdrPengajuanVendorNonSktEntity eTdrPengajuanVendorNonSkt, TdrIdentitasVendorNonSktEntity eTdrIdentitasVendorNonSkt) {
        try {
            List<TdrIdentitasVendorMinioNonSktEntity> tdrIdentitasVendorNonSktMinioEntityList = tdrIdentitasVendorMinioNonSkt.find("id_pengajuan = ?1", eTdrPengajuanVendorNonSkt.getId_pengajuan()).list();
            List<TdrPerbankanVendorMinioNonSktEntity> tdrPerbankanVendorNonSktMinioEntityList = tdrPerbankanVendorMinioNonSkt.find("where id_identitas = ?1", eTdrIdentitasVendorNonSkt.getId_identitas()).list();

            if(tdrIdentitasVendorNonSktMinioEntityList.size() > 0 && tdrPerbankanVendorNonSktMinioEntityList.size() > 0){
                TdrIdentitasVendorNonSktEntity entityIdentitasVendor = tdrIdentitasVendorNonSkt.find("where id_identitas = :id_identitas", Parameters.with("id_identitas", eTdrIdentitasVendorNonSkt.getId_identitas())).firstResult();
                entityIdentitasVendor.setIs_minio_identitas_vendor(1);
                tdrIdentitasVendorNonSkt.persist(entityIdentitasVendor);
                LOG.info("Upload Non SKT done = " + eTdrIdentitasVendorNonSkt.getId_identitas());
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
    @Transactional
    public void upload_non_skt(){
        upload_non_skt_process(null);
    }

    /**
     * Method untuk upload berdasarkan id_pengajuan tertentu
     */
    @Transactional
    public void upload_non_skt_by_id_pengajuan(String idPengajuan){
        upload_non_skt_process(idPengajuan);
    }

    /**
     * Core method untuk proses upload NON SKT
     */
    @Transactional
    public void upload_non_skt_process(String specificIdPengajuan){
        LOG.info("Upload Non SKT begin");
        try {
            String bucket = ConfigProvider.getConfig().getValue("bucket.minio", String.class);
            boolean a = this.checkBucket(bucket);
            String folderRoot = ConfigProvider.getConfig().getValue("root.folder.sso", String.class);
           
            if(!a){
                this.createBucket(bucket);
            }
            LOG.info("Upload Non SKT Begin" + (specificIdPengajuan != null ? " for ID: " + specificIdPengajuan : ""));
            
            List<TdrPengajuanVendorNonSktEntity> eTdrPengajuanVendorNonSktList;
            if(specificIdPengajuan != null && !specificIdPengajuan.isEmpty()) {
                // Upload berdasarkan id_pengajuan tertentu
                eTdrPengajuanVendorNonSktList = tdrPengajuanVendorNonSkt.find("id_pengajuan = ?1 AND id_vendor_eproc is not null", specificIdPengajuan).list();
            } else {
                // Upload scheduled (semua data)
                eTdrPengajuanVendorNonSktList = tdrPengajuanVendorNonSkt.find("where id_pengajuan is not null AND id_vendor_eproc is not null").page(0, 50).list();
            }

            LOG.info("[DATA] Total Pengajuan vendors Non SKT found: " + eTdrPengajuanVendorNonSktList.size());
            if (eTdrPengajuanVendorNonSktList.isEmpty()) {
                LOG.info("[INFO] No pending vendors to process.");
                LOG.info("Send Upload Data Eproc Non SKT Finished");
                return;
            }

            int dumpLimit = Math.min(5, eTdrPengajuanVendorNonSktList.size());
            for (int i = 0; i < dumpLimit; i++) {
                varDump("Vendor Non SKT [" + i + "]", eTdrPengajuanVendorNonSktList.get(i));
            }

            for(TdrPengajuanVendorNonSktEntity elTdrPengajuanVendorNonSkt: eTdrPengajuanVendorNonSktList){ 
                 if (elTdrPengajuanVendorNonSkt == null || elTdrPengajuanVendorNonSkt.getId_identitas() == null || elTdrPengajuanVendorNonSkt.getId_identitas() == 0) {
                    LOG.info("[SKIP] Id Identitas Not Exists for pengajuan: " + (elTdrPengajuanVendorNonSkt != null ? elTdrPengajuanVendorNonSkt.getId_pengajuan() : "NULL"));
                    continue;
                }

                if (elTdrPengajuanVendorNonSkt == null || elTdrPengajuanVendorNonSkt.getId_pengajuan() == null || elTdrPengajuanVendorNonSkt.getId_pengajuan() == "") {
                    LOG.info("[SKIP] Id Pengajuan Not Exists for pengajuan: " + (elTdrPengajuanVendorNonSkt != null ? elTdrPengajuanVendorNonSkt.getId_pengajuan() : "NULL"));
                    continue;
                }

                try {
                    TdrIdentitasVendorNonSktEntity eTdrIdentitasVendorNonSkt = tdrIdentitasVendorNonSkt.find("id_identitas = ?1 AND is_minio_identitas_vendor is null OR is_minio_identitas_vendor = ?2", elTdrPengajuanVendorNonSkt.getId_identitas(),0).firstResult();
                    if (eTdrIdentitasVendorNonSkt == null) {
                        LOG.warn("[NOT FOUND] Identitas tidak ditemukan untuk id_identitas: " + elTdrPengajuanVendorNonSkt.getId_identitas());
                        LOG.info("[INFO] No pending vendors to process.");
                        LOG.info("Send Data Eproc NON SKT Finished");
                        return;
                    }
             
                    if(eTdrIdentitasVendorNonSkt.getDok_legalitas_path() == null){
                        LOG.info("[NOT FOUND] Dokumen Legalitas Not Exist: "+eTdrIdentitasVendorNonSkt.getId_identitas());
                        return;
                    }
                
                    // varDump("Tdr Pengajuan", tdrPengajuanVendorNonSkt);
                    // varDump("Tdr Identitas", eTdrIdentitasVendorNonSkt);

                    DokLegalDto DokLegalObject = this.parseJson(eTdrIdentitasVendorNonSkt.getDok_legalitas_path());
                    try {
                        List<TdrIdentitasVendorMinioNonSktEntity> tdrIdentitasVendorNonSktMinioList = tdrIdentitasVendorMinioNonSkt.find("id_pengajuan = ?1", elTdrPengajuanVendorNonSkt.getId_pengajuan()).list();
                        if(tdrIdentitasVendorNonSktMinioList.size() > 0){
                            tdrIdentitasVendorMinioNonSkt.delete("id_pengajuan = ?1", elTdrPengajuanVendorNonSkt.getId_pengajuan());
                        }

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
                     } catch (Exception e) {
                       saveUploadLog(elTdrPengajuanVendorNonSkt.getId_pengajuan(), eTdrIdentitasVendorNonSkt.getId_identitas(), "identitas", null, null, null, "FAILED", "Error processing documents: " + e.getMessage(), 1);
                        try {
                            Thread.sleep(1000);
                        } catch (Exception f) {
                            LOG.error(f);
                        }
                        LOG.info("error = " + e);
                    }

                    // ===== UPLOAD PERBANKAN =====
                    uploadPerbankanNonSkt(bucket, folderRoot, elTdrPengajuanVendorNonSkt, eTdrIdentitasVendorNonSkt);

                    // ===== UPDATE STATUS MINIO =====
                    updateStatusMinioNonSkt(elTdrPengajuanVendorNonSkt, eTdrIdentitasVendorNonSkt);
                
                } catch (Exception e) {
                    LOG.error("[ERROR] Failed to process identitas for id_identitas: " + elTdrPengajuanVendorNonSkt.getId_identitas() + " - Error: " + e.getMessage(), e);
                }
            }      
        } catch (Exception e) {
            // TODO: handle exception
            LOG.error("error upload NON SKT", e);
            LOG.error("Identitas Vendor NON SKT update "+e.getMessage());
        }
                   
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
