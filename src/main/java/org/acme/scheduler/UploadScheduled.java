package org.acme.scheduler;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

import org.acme.controllers.upload_minio.dto.DokLegalDto;
import org.acme.controllers.upload_minio.dto.UploaderDto;

import org.acme.controllers.upload_minio.entity.TdrIdentitasVendorEntity;
import org.acme.controllers.upload_minio.entity.TdrIdentitasVendorMinioEntity;
import org.acme.controllers.upload_minio.entity.TdrIdentitasVendorMinioNonSktEntity;
import org.acme.controllers.upload_minio.entity.TdrIdentitasVendorNonSktEntity;
import org.acme.controllers.upload_minio.entity.TdrMstFileLegalitasEntity;
import org.acme.controllers.upload_minio.entity.TdrPengajuanVendorEntity;
import org.acme.controllers.upload_minio.entity.TdrPengajuanVendorNonSktEntity;
import org.acme.controllers.upload_minio.entity.TdrPengalamanVendorEntity;
import org.acme.controllers.upload_minio.entity.TdrPengalamanVendorMinioEntity;
import org.acme.controllers.upload_minio.entity.TdrPerbankanVendorEntity;
import org.acme.controllers.upload_minio.entity.TdrPerbankanVendorMinioEntity;
import org.acme.controllers.upload_minio.entity.TdrPerbankanVendorMinioNonSktEntity;
import org.acme.controllers.upload_minio.entity.TdrPerbankanVendorNonSktEntity;

import org.acme.controllers.upload_minio.repository.TdrIdentitasVendorMinioRepository;
import org.acme.controllers.upload_minio.repository.TdrIdentitasVendorRepository;
import org.acme.controllers.upload_minio.repository.TdrMstFileLegalitasRepository;
import org.acme.controllers.upload_minio.repository.TdrPengajuanVendorRepository;
import org.acme.controllers.upload_minio.repository.TdrPengalamanVendorMinioRepository;
import org.acme.controllers.upload_minio.repository.TdrPengalamanVendorRepository;
import org.acme.controllers.upload_minio.repository.TdrPerbankanVendorMinioRepository;
import org.acme.controllers.upload_minio.repository.TdrPerbankanVendorRepository;
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

   

    @Inject TdrIdentitasVendorRepository tdrIdentitas;

    @Inject TdrPengajuanVendorRepository tdrPengajuan;

    @Inject TdrMstFileLegalitasRepository tdrFileLegal;

    @Inject TdrIdentitasVendorMinioRepository tdrIdentitasMinio;

    @Inject TdrPengalamanVendorRepository tdrPengalamanVendor;

    @Inject TdrPerbankanVendorRepository tdrPerbankanVendor;

    @Inject TdrPerbankanVendorMinioRepository tdrPerbankanVendorMinio;

    @Inject TdrPengalamanVendorMinioRepository tdrPengalamanVendorMinio;

    @Inject MinioAsyncClient MinioClient;

    @Inject MinioClient MC;

    @Inject Vertx vertx;

    private final ObjectMapper objectMapper = new ObjectMapper();

     @PersistenceContext
    EntityManager em;

    @Inject ManagedExecutor executor;


    private static final Logger LOG = Logger.getLogger(UploadScheduled.class);

    




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
    

    @Scheduled(every = "60s")
    @Transactional
    public void upload_skt(){

        
        try {
            String bucket = ConfigProvider.getConfig().getValue("bucket.minio", String.class);
            boolean a = this.checkBucket(bucket);
            String folderRoot = ConfigProvider.getConfig().getValue("root.folder.sso", String.class);
            
            if(!a){
                this.createBucket(bucket);
            }
            LOG.info("Upload SKT Begin");
            
            List<TdrPengajuanVendorEntity> tdrPengajuanVendorList = tdrPengajuan.find("where id_pengajuan is not null AND id_vendor_eproc is not null").page(0, 50).list();
            for(TdrPengajuanVendorEntity tdrPengajuanVendor: tdrPengajuanVendorList){
                // System.out.println(i.getDok_legalitas_path());
                TdrIdentitasVendorEntity i = tdrIdentitas.find("id_identitas = ?1 AND is_minio_identitas_vendor is null OR is_minio_identitas_vendor = ?2", tdrPengajuanVendor.getId_identitas(),0).firstResult();

                if(i != null){

                    if(i.getDok_legalitas_path() != null){
                    
                       
                        // TdrPengajuanVendorEntity tdrPengajuanVendor = tdrPengajuan.find("where id_identitas = :id_identitas AND id_pengajuan is not null AND id_vendor_eproc is not null", Parameters.with("id_identitas", i.getId_identitas())).firstResult();
                        // System.out.println(tdrPengajuanVendor.getId_pengajuan());
                        if(tdrPengajuanVendor != null){
                            // Long iVM = tdrIdentitasMinio.delete("where id_pengajuan = :id", Parameters.with("id", tdrPengajuanVendor.getId_pengajuan()));
                            // System.out.println("delete = "+iVM);
                            // System.out.println("init Upload");
    
                            DokLegalDto obj = this.parseJson(i.getDok_legalitas_path());
                            try {



                                List<TdrIdentitasVendorMinioEntity> tdrList = tdrIdentitasMinio.find("id_pengajuan = ?1", tdrPengajuanVendor.getId_pengajuan()).list();
                                if(tdrList.size() > 0){
                                    tdrIdentitasMinio.delete("id_pengajuan = ?1", tdrPengajuanVendor.getId_pengajuan());
                                }

                                if(obj.getTxt_dok1() != null){
                                    LOG.info("txt dok 1 begin upload id_pengajuan = "+tdrPengajuanVendor.getId_pengajuan());
                                    TdrMstFileLegalitasEntity file = tdrFileLegal.find("where dok = :dok", Parameters.with("dok", 1)).firstResult();
                                    String nama_file = file.getNama_file();
                                    String pathMinio = tdrPengajuanVendor.getId_pengajuan()+"/"+nama_file+"/"+obj.getTxt_dok1();
                                    String urlPath = folderRoot+"/"+obj.getTxt_dok1();
                                    System.out.println(urlPath);
                                    Boolean checkFile = this.checkFile(urlPath);
                                    if(tdrPengajuanVendor.getId_pengajuan() != null){
                                        if(checkFile){
                                            boolean get = this.check_object(bucket, pathMinio);
                                            if(!get){
                                                Boolean upload = this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                if(upload){
                                                    TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                    entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                    entityTdrIdentitas.setDok(1);
                                                    entityTdrIdentitas.setIs_eproc(0);
                                                    entityTdrIdentitas.setNama_dok(nama_file);
                                                    entityTdrIdentitas.setPath_minio(pathMinio);
                                                    entityTdrIdentitas.setStatus_skt(1);
                                                    entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                    tdrIdentitasMinio.persist(entityTdrIdentitas);
            
                                                    LOG.info("data berhasil di upload");
            
                                                    
            
                                                }
                                            }else{
                                                this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                entityTdrIdentitas.setDok(1);
                                                entityTdrIdentitas.setIs_eproc(0);
                                                entityTdrIdentitas.setNama_dok(nama_file);
                                                entityTdrIdentitas.setPath_minio(pathMinio);
                                                entityTdrIdentitas.setStatus_skt(1);
                                                entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                tdrIdentitasMinio.persist(entityTdrIdentitas);
        
                                                LOG.info("data berhasil di upload");
                                            }
                                        }else{
                                            LOG.info("File Not Exist "+obj.getTxt_dok1());
                                        }
                                    }
                                    
                                }
                                if(obj.getTxt_dok2() != null){
                                    LOG.info("txt dok 2 begin upload id_pengajuan = "+tdrPengajuanVendor.getId_pengajuan());
                                    TdrMstFileLegalitasEntity file = tdrFileLegal.find("where dok = :dok", Parameters.with("dok", 2)).firstResult();
                                    String nama_file = file.getNama_file();
                                    String pathMinio = tdrPengajuanVendor.getId_pengajuan()+"/"+nama_file+"/"+obj.getTxt_dok2();
                                    String urlPath = folderRoot+"/"+obj.getTxt_dok2();
                                    // System.out.println(pathMinio);
                                    Boolean checkFile = this.checkFile(urlPath);
                                    if(tdrPengajuanVendor.getId_pengajuan() != null){
                                        if(checkFile){
                                            boolean get = this.check_object(bucket, pathMinio);
                                            if(!get){
                                                Boolean upload = this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                if(upload){
                                                    TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                    entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                    entityTdrIdentitas.setDok(2);
                                                    entityTdrIdentitas.setIs_eproc(0);
                                                    entityTdrIdentitas.setNama_dok(nama_file);
                                                    entityTdrIdentitas.setPath_minio(pathMinio);
                                                    entityTdrIdentitas.setStatus_skt(1);
                                                    entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                    tdrIdentitasMinio.persist(entityTdrIdentitas);
            
                                                    LOG.info("data berhasil di upload");
                                                }
                                            }else{
                                                this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                entityTdrIdentitas.setDok(2);
                                                entityTdrIdentitas.setIs_eproc(0);
                                                entityTdrIdentitas.setNama_dok(nama_file);
                                                entityTdrIdentitas.setPath_minio(pathMinio);
                                                entityTdrIdentitas.setStatus_skt(1);
                                                entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                tdrIdentitasMinio.persist(entityTdrIdentitas);
        
                                                LOG.info("data berhasil di upload");
                                            }
                                        }else{
                                            LOG.info("File not exist "+obj.getTxt_dok2());
                                        }
                                    }
                                }
                                if(obj.getTxt_dok3() != null){
                                    LOG.info("txt dok 3 begin upload id_pengajuan = "+tdrPengajuanVendor.getId_pengajuan());
                                    TdrMstFileLegalitasEntity file = tdrFileLegal.find("where dok = :dok", Parameters.with("dok", 3)).firstResult();
                                    String nama_file = file.getNama_file();
                                    String pathMinio = tdrPengajuanVendor.getId_pengajuan()+"/"+nama_file+"/"+obj.getTxt_dok3();
                                    String urlPath = folderRoot+"/"+obj.getTxt_dok3();
                                    // System.out.println(pathMinio);
                                    Boolean checkFile = this.checkFile(urlPath);
                                    if(tdrPengajuanVendor.getId_pengajuan() != null){
                                        if(checkFile){
                                            boolean get = this.check_object(bucket, pathMinio);
                                            if(!get){
                                                Boolean upload = this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                if(upload){
                                                    TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                    entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                    entityTdrIdentitas.setDok(3);
                                                    entityTdrIdentitas.setIs_eproc(0);
                                                    entityTdrIdentitas.setNama_dok(nama_file);
                                                    entityTdrIdentitas.setPath_minio(pathMinio);
                                                    entityTdrIdentitas.setStatus_skt(1);
                                                    entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                    tdrIdentitasMinio.persist(entityTdrIdentitas);
            
                                                    LOG.info("data berhasil di upload");
                                                }
                                            }else{
                                                this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                entityTdrIdentitas.setDok(3);
                                                entityTdrIdentitas.setIs_eproc(0);
                                                entityTdrIdentitas.setNama_dok(nama_file);
                                                entityTdrIdentitas.setPath_minio(pathMinio);
                                                entityTdrIdentitas.setStatus_skt(1);
                                                entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                tdrIdentitasMinio.persist(entityTdrIdentitas);
        
                                                LOG.info("data berhasil di upload");
                                            }
                                        }else{
                                            LOG.info("File not exist "+obj.getTxt_dok3());
                                        }
                                    }
                                }
                                if(obj.getTxt_dok4() != null){
                                    LOG.info("txt dok 4 begin upload id_pengajuan = "+tdrPengajuanVendor.getId_pengajuan());
                                    TdrMstFileLegalitasEntity file = tdrFileLegal.find("where dok = :dok", Parameters.with("dok", 4)).firstResult();
                                    String nama_file = file.getNama_file();
                                    String pathMinio = tdrPengajuanVendor.getId_pengajuan()+"/"+nama_file+"/"+obj.getTxt_dok4();
                                    String urlPath = folderRoot+"/"+obj.getTxt_dok4();
                                    // System.out.println(pathMinio);
                                    Boolean checkFile = this.checkFile(urlPath);
                                    if(tdrPengajuanVendor.getId_pengajuan() != null){
                                        if(checkFile){
                                            boolean get = this.check_object(bucket, pathMinio);
                                            if(!get){
                                                Boolean upload = this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                if(upload){
                                                    TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                    entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                    entityTdrIdentitas.setDok(4);
                                                    entityTdrIdentitas.setIs_eproc(0);
                                                    entityTdrIdentitas.setNama_dok(nama_file);
                                                    entityTdrIdentitas.setPath_minio(pathMinio);
                                                    entityTdrIdentitas.setStatus_skt(1);
                                                    entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                    tdrIdentitasMinio.persist(entityTdrIdentitas);
            
                                                    LOG.info("data berhasil di upload");
                                                }
                                            }else{
                                                this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                entityTdrIdentitas.setDok(4);
                                                entityTdrIdentitas.setIs_eproc(0);
                                                entityTdrIdentitas.setNama_dok(nama_file);
                                                entityTdrIdentitas.setPath_minio(pathMinio);
                                                entityTdrIdentitas.setStatus_skt(1);
                                                entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                tdrIdentitasMinio.persist(entityTdrIdentitas);
        
                                                LOG.info("data berhasil di upload");
                                            }
                                        }else{
                                            LOG.info("File not exist "+obj.getTxt_dok4());
                                        }
                                    }
                                }
                                if(obj.getTxt_dok5() != null){
                                    LOG.info("txt dok 5 begin upload id_pengajuan = "+tdrPengajuanVendor.getId_pengajuan());
                                    TdrMstFileLegalitasEntity file = tdrFileLegal.find("where dok = :dok", Parameters.with("dok", 5)).firstResult();
                                    String nama_file = file.getNama_file();
                                    String pathMinio = tdrPengajuanVendor.getId_pengajuan()+"/"+nama_file+"/"+obj.getTxt_dok5();
                                    String urlPath = folderRoot+"/"+obj.getTxt_dok5();
                                    // System.out.println(pathMinio);
                                    Boolean checkFile = this.checkFile(urlPath);
                                    if(tdrPengajuanVendor.getId_pengajuan() != null){
                                        if(checkFile){
                                            boolean get = this.check_object(bucket, pathMinio);
                                            if(!get){
                                                Boolean upload = this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                if(upload){
                                                    TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                    entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                    entityTdrIdentitas.setDok(5);
                                                    entityTdrIdentitas.setIs_eproc(0);
                                                    entityTdrIdentitas.setNama_dok(nama_file);
                                                    entityTdrIdentitas.setPath_minio(pathMinio);
                                                    entityTdrIdentitas.setStatus_skt(1);
                                                    entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                    tdrIdentitasMinio.persist(entityTdrIdentitas);
            
                                                    LOG.info("data berhasil di upload");
            
                                                }
                                            }else{
                                                this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                entityTdrIdentitas.setDok(5);
                                                entityTdrIdentitas.setIs_eproc(0);
                                                entityTdrIdentitas.setNama_dok(nama_file);
                                                entityTdrIdentitas.setPath_minio(pathMinio);
                                                entityTdrIdentitas.setStatus_skt(1);
                                                entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                tdrIdentitasMinio.persist(entityTdrIdentitas);
        
                                                LOG.info("data berhasil di upload");
                                            }
                                        }else{
                                            LOG.info("File not exist "+obj.getTxt_dok5());
                                        }
                                    }else{
                                        LOG.info("Id Pengajuan Not Exist ");
                                    }
                                }
                                if(obj.getTxt_dok6() != null){
                                    LOG.info("txt dok 6 begin upload id_pengajuan = "+tdrPengajuanVendor.getId_pengajuan());
                                    TdrMstFileLegalitasEntity file = tdrFileLegal.find("where dok = :dok", Parameters.with("dok", 6)).firstResult();
                                    String nama_file = file.getNama_file();
                                    String pathMinio = tdrPengajuanVendor.getId_pengajuan()+"/"+nama_file+"/"+obj.getTxt_dok6();
                                    String urlPath = folderRoot+"/"+obj.getTxt_dok6();
                                    // System.out.println(pathMinio);
                                    Boolean checkFile = this.checkFile(urlPath);
                                    if(tdrPengajuanVendor.getId_pengajuan() != null){
                                        if(checkFile){
                                            boolean get = this.check_object(bucket, pathMinio);
                                            if(!get){
                                                Boolean upload = this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                if(upload){
                                                    TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                    entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                    entityTdrIdentitas.setDok(6);
                                                    entityTdrIdentitas.setIs_eproc(0);
                                                    entityTdrIdentitas.setNama_dok(nama_file);
                                                    entityTdrIdentitas.setPath_minio(pathMinio);
                                                    entityTdrIdentitas.setStatus_skt(1);
                                                    entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                    tdrIdentitasMinio.persist(entityTdrIdentitas);
            
                                                    LOG.info("data berhasil di upload");
            
                                                }
                                            }else{
                                                this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                entityTdrIdentitas.setDok(6);
                                                entityTdrIdentitas.setIs_eproc(0);
                                                entityTdrIdentitas.setNama_dok(nama_file);
                                                entityTdrIdentitas.setPath_minio(pathMinio);
                                                entityTdrIdentitas.setStatus_skt(1);
                                                entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                tdrIdentitasMinio.persist(entityTdrIdentitas);
        
                                                LOG.info("data berhasil di upload");
                                            }
                                        }else{
                                            LOG.info("File not exist "+obj.getTxt_dok6());
                                        }
                                    }else{
                                        LOG.info("Id Pengajuan Not Exists");
                                    }
                                    // System.out.println(nama_file);
                                }
                                if(obj.getTxt_dok7() != null){
                                    LOG.info("txt dok 7 begin upload id_pengajuan = "+tdrPengajuanVendor.getId_pengajuan());
                                    TdrMstFileLegalitasEntity file = tdrFileLegal.find("where dok = :dok", Parameters.with("dok", 7)).firstResult();
                                    String nama_file = file.getNama_file();
                                    String pathMinio = tdrPengajuanVendor.getId_pengajuan()+"/"+nama_file+"/"+obj.getTxt_dok7();
                                    String urlPath = folderRoot+"/"+obj.getTxt_dok7();
                                    // System.out.println(pathMinio);
                                    Boolean checkFile = this.checkFile(urlPath);
                                    if(tdrPengajuanVendor.getId_pengajuan() != null){
                                        if(checkFile){
                                            boolean get = this.check_object(bucket, pathMinio);
                                            if(!get){
                                                Boolean upload = this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                if(upload){
                                                    TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                    entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                    entityTdrIdentitas.setDok(7);
                                                    entityTdrIdentitas.setIs_eproc(0);
                                                    entityTdrIdentitas.setNama_dok(nama_file);
                                                    entityTdrIdentitas.setPath_minio(pathMinio);
                                                    entityTdrIdentitas.setStatus_skt(1);
                                                    entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                    tdrIdentitasMinio.persist(entityTdrIdentitas);
            
                                                    LOG.info("data berhasil di upload");
            
                                                }
                                            }else{
                                                this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                entityTdrIdentitas.setDok(7);
                                                entityTdrIdentitas.setIs_eproc(0);
                                                entityTdrIdentitas.setNama_dok(nama_file);
                                                entityTdrIdentitas.setPath_minio(pathMinio);
                                                entityTdrIdentitas.setStatus_skt(1);
                                                entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                tdrIdentitasMinio.persist(entityTdrIdentitas);
        
                                                LOG.info("data berhasil di upload");
                                            }
                                        }else{
                                            LOG.info("File not exist "+obj.getTxt_dok7());
                                        }
                                    }else{
                                        LOG.info("Id Pengajuan Not Exists");
                                    }
                                }
                                if(obj.getTxt_dok8() != null){
                                    LOG.info("txt dok 8 begin upload id_pengajuan = "+tdrPengajuanVendor.getId_pengajuan());
                                    TdrMstFileLegalitasEntity file = tdrFileLegal.find("where dok = :dok", Parameters.with("dok", 8)).firstResult();
                                    String nama_file = file.getNama_file();
                                    String pathMinio = tdrPengajuanVendor.getId_pengajuan()+"/"+nama_file+"/"+obj.getTxt_dok8();
                                    String urlPath = folderRoot+"/"+obj.getTxt_dok8();
                                    // System.out.println(pathMinio);
                                    Boolean checkFile = this.checkFile(urlPath);
                                    if(tdrPengajuanVendor.getId_pengajuan() != null){
                                        if(checkFile){
                                            boolean get = this.check_object(bucket, pathMinio);
                                            if(!get){
                                                Boolean upload = this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                if(upload){
                                                    TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                    entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                    entityTdrIdentitas.setDok(8);
                                                    entityTdrIdentitas.setIs_eproc(0);
                                                    entityTdrIdentitas.setNama_dok(nama_file);
                                                    entityTdrIdentitas.setPath_minio(pathMinio);
                                                    entityTdrIdentitas.setStatus_skt(1);
                                                    entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                    tdrIdentitasMinio.persist(entityTdrIdentitas);
            
                                                    LOG.info("data berhasil di upload");
            
                                                }
                                            }else{
                                                this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                entityTdrIdentitas.setDok(8);
                                                entityTdrIdentitas.setIs_eproc(0);
                                                entityTdrIdentitas.setNama_dok(nama_file);
                                                entityTdrIdentitas.setPath_minio(pathMinio);
                                                entityTdrIdentitas.setStatus_skt(1);
                                                entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                tdrIdentitasMinio.persist(entityTdrIdentitas);
        
                                                LOG.info("data berhasil di upload");
                                            }
                                        }else{
                                            LOG.info("File not exist "+obj.getTxt_dok8());
                                        }
                                    }else{
                                        LOG.info("Id Pengajuan Not Exists");
                                    }
                                }
                                if(obj.getTxt_dok9() != null){
                                    LOG.info("txt dok 9 begin upload id_pengajuan = "+tdrPengajuanVendor.getId_pengajuan());
                                    TdrMstFileLegalitasEntity file = tdrFileLegal.find("where dok = :dok", Parameters.with("dok", 9)).firstResult();
                                    String nama_file = file.getNama_file();
                                    String pathMinio = tdrPengajuanVendor.getId_pengajuan()+"/"+nama_file+"/"+obj.getTxt_dok9();
                                    String urlPath = folderRoot+"/"+obj.getTxt_dok9();
                                    // System.out.println(pathMinio);
                                    Boolean checkFile = this.checkFile(urlPath);
                                    if(tdrPengajuanVendor.getId_pengajuan() != null){
                                        if(checkFile){
                                            boolean get = this.check_object(bucket, pathMinio);
                                            if(!get){
                                                Boolean upload = this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                if(upload){
                                                    TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                    entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                    entityTdrIdentitas.setDok(9);
                                                    entityTdrIdentitas.setIs_eproc(0);
                                                    entityTdrIdentitas.setNama_dok(nama_file);
                                                    entityTdrIdentitas.setPath_minio(pathMinio);
                                                    entityTdrIdentitas.setStatus_skt(1);
                                                    entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                    tdrIdentitasMinio.persist(entityTdrIdentitas);
            
                                                    LOG.info("data berhasil di upload");
            
                                                }
                                            }else{
                                                this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                entityTdrIdentitas.setDok(9);
                                                entityTdrIdentitas.setIs_eproc(0);
                                                entityTdrIdentitas.setNama_dok(nama_file);
                                                entityTdrIdentitas.setPath_minio(pathMinio);
                                                entityTdrIdentitas.setStatus_skt(1);
                                                entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                tdrIdentitasMinio.persist(entityTdrIdentitas);
        
                                                LOG.info("data berhasil di upload");
                                            }
                                        }else{
                                            LOG.info("File not exist "+obj.getTxt_dok9());
                                        }
                                    }else{
                                        LOG.info("Id Pengajuan Not Exists");
                                    }
                                    // System.out.println(nama_file);
                                }
                                if(obj.getTxt_dok10() != null){
                                    LOG.info("txt dok 10 begin upload id_pengajuan = "+tdrPengajuanVendor.getId_pengajuan());
                                    TdrMstFileLegalitasEntity file = tdrFileLegal.find("where dok = :dok", Parameters.with("dok", 10)).firstResult();
                                    String nama_file = file.getNama_file();
                                    String pathMinio = tdrPengajuanVendor.getId_pengajuan()+"/"+nama_file+"/"+obj.getTxt_dok10();
                                    String urlPath = folderRoot+"/"+obj.getTxt_dok10();
                                    // System.out.println(pathMinio);
                                    Boolean checkFile = this.checkFile(urlPath);
                                    if(tdrPengajuanVendor.getId_pengajuan() != null){
                                        if(checkFile){
                                            boolean get = this.check_object(bucket, pathMinio);
                                            if(!get){
                                                Boolean upload = this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                if(upload){
                                                    TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                    entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                    entityTdrIdentitas.setDok(10);
                                                    entityTdrIdentitas.setIs_eproc(0);
                                                    entityTdrIdentitas.setNama_dok(nama_file);
                                                    entityTdrIdentitas.setPath_minio(pathMinio);
                                                    entityTdrIdentitas.setStatus_skt(1);
                                                    entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                    tdrIdentitasMinio.persist(entityTdrIdentitas);
            
                                                    LOG.info("data berhasil di upload");
            
                                                }
                                            }else{
                                                this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                entityTdrIdentitas.setDok(10);
                                                entityTdrIdentitas.setIs_eproc(0);
                                                entityTdrIdentitas.setNama_dok(nama_file);
                                                entityTdrIdentitas.setPath_minio(pathMinio);
                                                entityTdrIdentitas.setStatus_skt(1);
                                                entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                tdrIdentitasMinio.persist(entityTdrIdentitas);
        
                                                LOG.info("data berhasil di upload");
                                            }
                                        }else{
                                            LOG.info("File not exist "+obj.getTxt_dok10());
                                        }
                                    }else{
                                        LOG.info("Id Pengajuan Not Exists");
                                    }
                                }
                                if(obj.getTxt_dok11() != null){
                                    LOG.info("txt dok 11 begin upload id_pengajuan = "+tdrPengajuanVendor.getId_pengajuan());
                                    TdrMstFileLegalitasEntity file = tdrFileLegal.find("where dok = :dok", Parameters.with("dok", 11)).firstResult();
                                    String nama_file = file.getNama_file();
                                    String pathMinio = tdrPengajuanVendor.getId_pengajuan()+"/"+nama_file+"/"+obj.getTxt_dok11();
                                    String urlPath = folderRoot+"/"+obj.getTxt_dok11();
                                    // System.out.println(pathMinio);
                                    Boolean checkFile = this.checkFile(urlPath);
                                    if(tdrPengajuanVendor.getId_pengajuan() != null){
                                        if(checkFile){
                                            boolean get = this.check_object(bucket, pathMinio);
                                            if(!get){
                                                Boolean upload = this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                if(upload){
                                                    TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                    entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                    entityTdrIdentitas.setDok(11);
                                                    entityTdrIdentitas.setIs_eproc(0);
                                                    entityTdrIdentitas.setNama_dok(nama_file);
                                                    entityTdrIdentitas.setPath_minio(pathMinio);
                                                    entityTdrIdentitas.setStatus_skt(1);
                                                    entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                    tdrIdentitasMinio.persist(entityTdrIdentitas);
            
                                                    LOG.info("data berhasil di upload");
            
                                                }
                                            }else{
                                                this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                entityTdrIdentitas.setDok(11);
                                                entityTdrIdentitas.setIs_eproc(0);
                                                entityTdrIdentitas.setNama_dok(nama_file);
                                                entityTdrIdentitas.setPath_minio(pathMinio);
                                                entityTdrIdentitas.setStatus_skt(1);
                                                entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                tdrIdentitasMinio.persist(entityTdrIdentitas);
        
                                                LOG.info("data berhasil di upload");
                                            }
                                        }else{
                                            LOG.info("File not exist "+obj.getTxt_dok11());
                                        }
                                    }else{
                                        LOG.info("Id Pengajuan Not Exists");
                                    }
                                }
                                if(obj.getTxt_dok12() != null){
                                    LOG.info("txt dok 12 begin upload id_pengajuan = "+tdrPengajuanVendor.getId_pengajuan());
                                    TdrMstFileLegalitasEntity file = tdrFileLegal.find("where dok = :dok", Parameters.with("dok", 12)).firstResult();
                                    String nama_file = file.getNama_file();
                                    String pathMinio = tdrPengajuanVendor.getId_pengajuan()+"/"+nama_file+"/"+obj.getTxt_dok12();
                                    String urlPath = folderRoot+"/"+obj.getTxt_dok12();
                                    // System.out.println(pathMinio);
                                    Boolean checkFile = this.checkFile(urlPath);
                                    if(tdrPengajuanVendor.getId_pengajuan() != null){
                                        if(checkFile){
                                            boolean get = this.check_object(bucket, pathMinio);
                                            if(!get){
                                                Boolean upload = this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                if(upload){
                                                    TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                    entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                    entityTdrIdentitas.setDok(12);
                                                    entityTdrIdentitas.setIs_eproc(0);
                                                    entityTdrIdentitas.setNama_dok(nama_file);
                                                    entityTdrIdentitas.setPath_minio(pathMinio);
                                                    entityTdrIdentitas.setStatus_skt(1);
                                                    entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                    tdrIdentitasMinio.persist(entityTdrIdentitas);
            
                                                    LOG.info("data berhasil di upload");
            
                                                }
                                            }else{
                                                this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                entityTdrIdentitas.setDok(12);
                                                entityTdrIdentitas.setIs_eproc(0);
                                                entityTdrIdentitas.setNama_dok(nama_file);
                                                entityTdrIdentitas.setPath_minio(pathMinio);
                                                entityTdrIdentitas.setStatus_skt(1);
                                                entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                tdrIdentitasMinio.persist(entityTdrIdentitas);
        
                                                LOG.info("data berhasil di upload");
                                            }
                                        }else{
                                            LOG.info("File not exist "+obj.getTxt_dok12());
                                        }
                                    }else{
                                        LOG.info("Id Pengajuan Not Exists");
                                    }
                                    // System.out.println(nama_file);
                                }
                                if(obj.getTxt_dok13() != null){
                                    LOG.info("txt dok 13 begin upload id_pengajuan = "+tdrPengajuanVendor.getId_pengajuan());
                                    TdrMstFileLegalitasEntity file = tdrFileLegal.find("where dok = :dok", Parameters.with("dok", 13)).firstResult();
                                    String nama_file = file.getNama_file();
                                    String pathMinio = tdrPengajuanVendor.getId_pengajuan()+"/"+nama_file+"/"+obj.getTxt_dok13();
                                    String urlPath = folderRoot+"/"+obj.getTxt_dok13();
                                    // System.out.println(pathMinio);
                                    Boolean checkFile = this.checkFile(urlPath);
                                    if(tdrPengajuanVendor.getId_pengajuan() != null){
                                        if(checkFile){
                                            boolean get = this.check_object(bucket, pathMinio);
                                            if(!get){
                                                Boolean upload = this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                if(upload){
                                                    TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                    entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                    entityTdrIdentitas.setDok(13);
                                                    entityTdrIdentitas.setIs_eproc(0);
                                                    entityTdrIdentitas.setNama_dok(nama_file);
                                                    entityTdrIdentitas.setPath_minio(pathMinio);
                                                    entityTdrIdentitas.setStatus_skt(1);
                                                    entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                    tdrIdentitasMinio.persist(entityTdrIdentitas);
            
                                                    LOG.info("data berhasil di upload");
            
                                                }
                                            }else{
                                                this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                entityTdrIdentitas.setDok(13);
                                                entityTdrIdentitas.setIs_eproc(0);
                                                entityTdrIdentitas.setNama_dok(nama_file);
                                                entityTdrIdentitas.setPath_minio(pathMinio);
                                                entityTdrIdentitas.setStatus_skt(1);
                                                entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                tdrIdentitasMinio.persist(entityTdrIdentitas);
        
                                                LOG.info("data berhasil di upload");
                                            }
                                        }else{
                                            LOG.info("File not exist "+obj.getTxt_dok13());
                                        }
                                    }else{
                                        LOG.info("Id Pengajuan Not Exists");
                                    }
                                }
                                if(obj.getTxt_dok14() != null){
                                    LOG.info("txt dok 14 begin upload id_pengajuan = "+tdrPengajuanVendor.getId_pengajuan());
                                    TdrMstFileLegalitasEntity file = tdrFileLegal.find("where dok = :dok", Parameters.with("dok", 14)).firstResult();
                                    String nama_file = file.getNama_file();
                                    String pathMinio = tdrPengajuanVendor.getId_pengajuan()+"/"+nama_file+"/"+obj.getTxt_dok14();
                                    String urlPath = folderRoot+"/"+obj.getTxt_dok14();
                                    // System.out.println(pathMinio);
                                    Boolean checkFile = this.checkFile(urlPath);
                                    if(tdrPengajuanVendor.getId_pengajuan() != null){
                                        if(checkFile){
                                            boolean get = this.check_object(bucket, pathMinio);
                                            if(!get){
                                                Boolean upload = this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                if(upload){
                                                    TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                    entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                    entityTdrIdentitas.setDok(14);
                                                    entityTdrIdentitas.setIs_eproc(0);
                                                    entityTdrIdentitas.setNama_dok(nama_file);
                                                    entityTdrIdentitas.setPath_minio(pathMinio);
                                                    entityTdrIdentitas.setStatus_skt(1);
                                                    entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                    tdrIdentitasMinio.persist(entityTdrIdentitas);
            
                                                    LOG.info("data berhasil di upload");
            
                                                }
                                            }else{
                                                this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                entityTdrIdentitas.setDok(14);
                                                entityTdrIdentitas.setIs_eproc(0);
                                                entityTdrIdentitas.setNama_dok(nama_file);
                                                entityTdrIdentitas.setPath_minio(pathMinio);
                                                entityTdrIdentitas.setStatus_skt(1);
                                                entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                tdrIdentitasMinio.persist(entityTdrIdentitas);
        
                                                LOG.info("data berhasil di upload");
                                            }
                                        }else{
                                            LOG.info("File not exist "+obj.getTxt_dok14());
                                        }
                                    }else{
                                        LOG.info("Id Pengajuan Not Exists");
                                    }
                                }
                                if(obj.getTxt_dok15() != null){
                                    LOG.info("txt dok 15 begin upload id_pengajuan = "+tdrPengajuanVendor.getId_pengajuan());
                                    TdrMstFileLegalitasEntity file = tdrFileLegal.find("where dok = :dok", Parameters.with("dok", 15)).firstResult();
                                    String nama_file = file.getNama_file();
                                    // System.out.println(nama_file);
                                    String pathMinio = tdrPengajuanVendor.getId_pengajuan()+"/"+nama_file+"/"+obj.getTxt_dok15();
                                    String urlPath = folderRoot+"/"+obj.getTxt_dok15();
                                    // System.out.println(pathMinio);
                                    Boolean checkFile = this.checkFile(urlPath);
                                    if(tdrPengajuanVendor.getId_pengajuan() != null){
                                        if(checkFile){
                                            boolean get = this.check_object(bucket, pathMinio);
                                            if(!get){
                                                Boolean upload = this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                if(upload){
                                                    TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                    entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                    entityTdrIdentitas.setDok(15);
                                                    entityTdrIdentitas.setIs_eproc(0);
                                                    entityTdrIdentitas.setNama_dok(nama_file);
                                                    entityTdrIdentitas.setPath_minio(pathMinio);
                                                    entityTdrIdentitas.setStatus_skt(1);
                                                    entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                    tdrIdentitasMinio.persist(entityTdrIdentitas);
            
                                                    LOG.info("data berhasil di upload");
            
                                                }
                                            }else{
                                                this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                entityTdrIdentitas.setDok(15);
                                                entityTdrIdentitas.setIs_eproc(0);
                                                entityTdrIdentitas.setNama_dok(nama_file);
                                                entityTdrIdentitas.setPath_minio(pathMinio);
                                                entityTdrIdentitas.setStatus_skt(1);
                                                entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                tdrIdentitasMinio.persist(entityTdrIdentitas);
        
                                                LOG.info("data berhasil di upload");
                                            }
                                        }else{
                                            LOG.info("File not exist "+obj.getTxt_dok15());
                                        }
                                    }else{
                                        LOG.info("Id Pengajuan Not Exists");
                                    }
                                }
                                if(obj.getTxt_dok16() != null){
                                    LOG.info("txt dok 16 begin upload id_pengajuan = "+tdrPengajuanVendor.getId_pengajuan());
                                    TdrMstFileLegalitasEntity file = tdrFileLegal.find("where dok = :dok", Parameters.with("dok", 16)).firstResult();
                                    String nama_file = file.getNama_file();
                                    String pathMinio = tdrPengajuanVendor.getId_pengajuan()+"/"+nama_file+"/"+obj.getTxt_dok16();
                                    String urlPath = folderRoot+"/"+obj.getTxt_dok16();
                                    // System.out.println(pathMinio);
                                    Boolean checkFile = this.checkFile(urlPath);
                                    if(tdrPengajuanVendor.getId_pengajuan() != null){
                                        if(checkFile){
                                            boolean get = this.check_object(bucket, pathMinio);
                                            if(!get){
                                                Boolean upload = this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                if(upload){
                                                    TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                    entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                    entityTdrIdentitas.setDok(16);
                                                    entityTdrIdentitas.setIs_eproc(0);
                                                    entityTdrIdentitas.setNama_dok(nama_file);
                                                    entityTdrIdentitas.setPath_minio(pathMinio);
                                                    entityTdrIdentitas.setStatus_skt(1);
                                                    entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                    tdrIdentitasMinio.persist(entityTdrIdentitas);
            
                                                    LOG.info("data berhasil di upload");
            
                                                }
                                            }else{
                                                this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                entityTdrIdentitas.setDok(16);
                                                entityTdrIdentitas.setIs_eproc(0);
                                                entityTdrIdentitas.setNama_dok(nama_file);
                                                entityTdrIdentitas.setPath_minio(pathMinio);
                                                entityTdrIdentitas.setStatus_skt(1);
                                                entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                tdrIdentitasMinio.persist(entityTdrIdentitas);
        
                                                LOG.info("data berhasil di upload");
                                            }
                                        }else{
                                            LOG.info("File not exist "+obj.getTxt_dok16());
                                        }
                                    }else{
                                        LOG.info("Id Pengajuan Not Exists");
                                    }
                                }
                                if(obj.getTxt_dok17() != null){
                                    LOG.info("txt dok 17 begin upload id_pengajuan = "+tdrPengajuanVendor.getId_pengajuan());
                                    TdrMstFileLegalitasEntity file = tdrFileLegal.find("where dok = :dok", Parameters.with("dok", 17)).firstResult();
                                    String nama_file = file.getNama_file();
                                    String pathMinio = tdrPengajuanVendor.getId_pengajuan()+"/"+nama_file+"/"+obj.getTxt_dok17();
                                    String urlPath = folderRoot+"/"+obj.getTxt_dok17();
                                    // System.out.println(pathMinio);
                                    Boolean checkFile = this.checkFile(urlPath);
                                    if(tdrPengajuanVendor.getId_pengajuan() != null){
                                        if(checkFile){
                                            boolean get = this.check_object(bucket, pathMinio);
                                            if(!get){
                                                Boolean upload = this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                if(upload){
                                                    TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                    entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                    entityTdrIdentitas.setDok(17);
                                                    entityTdrIdentitas.setIs_eproc(0);
                                                    entityTdrIdentitas.setNama_dok(nama_file);
                                                    entityTdrIdentitas.setPath_minio(pathMinio);
                                                    entityTdrIdentitas.setStatus_skt(1);
                                                    entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                    tdrIdentitasMinio.persist(entityTdrIdentitas);
            
                                                    LOG.info("data berhasil di upload");
            
                                                }
                                            }else{
                                                this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                entityTdrIdentitas.setDok(17);
                                                entityTdrIdentitas.setIs_eproc(0);
                                                entityTdrIdentitas.setNama_dok(nama_file);
                                                entityTdrIdentitas.setPath_minio(pathMinio);
                                                entityTdrIdentitas.setStatus_skt(1);
                                                entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                tdrIdentitasMinio.persist(entityTdrIdentitas);
        
                                                LOG.info("data berhasil di upload");
                                            }
                                        }else{
                                            LOG.info("File not exist "+obj.getTxt_dok17());
                                        }
                                    }else{
                                        LOG.info("Id Pengajuan Not Exists");
                                    }
                                }
                                if(obj.getTxt_dok18() != null){
                                    LOG.info("txt dok 18 begin upload id_pengajuan = "+tdrPengajuanVendor.getId_pengajuan());
                                    TdrMstFileLegalitasEntity file = tdrFileLegal.find("where dok = :dok", Parameters.with("dok", 18)).firstResult();
                                    String nama_file = file.getNama_file();
                                    String pathMinio = tdrPengajuanVendor.getId_pengajuan()+"/"+nama_file+"/"+obj.getTxt_dok18();
                                    String urlPath = folderRoot+"/"+obj.getTxt_dok18();
                                    // System.out.println(pathMinio);
                                    Boolean checkFile = this.checkFile(urlPath);
                                    if(tdrPengajuanVendor.getId_pengajuan() != null){
                                        if(checkFile){
                                            boolean get = this.check_object(bucket, pathMinio);
                                            if(!get){
                                                Boolean upload = this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                if(upload){
                                                    TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                    entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                    entityTdrIdentitas.setDok(18);
                                                    entityTdrIdentitas.setIs_eproc(0);
                                                    entityTdrIdentitas.setNama_dok(nama_file);
                                                    entityTdrIdentitas.setPath_minio(pathMinio);
                                                    entityTdrIdentitas.setStatus_skt(1);
                                                    entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                    tdrIdentitasMinio.persist(entityTdrIdentitas);
            
                                                    LOG.info("data berhasil di upload");
            
                                                }
                                            }else{
                                                this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                entityTdrIdentitas.setDok(18);
                                                entityTdrIdentitas.setIs_eproc(0);
                                                entityTdrIdentitas.setNama_dok(nama_file);
                                                entityTdrIdentitas.setPath_minio(pathMinio);
                                                entityTdrIdentitas.setStatus_skt(1);
                                                entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                tdrIdentitasMinio.persist(entityTdrIdentitas);
        
                                                LOG.info("data berhasil di upload");
                                            }
                                        }else{
                                            LOG.info("File not exist "+obj.getTxt_dok18());
                                        }
                                    }else{
                                        LOG.info("Id Pengajuan Not Exists");
                                    }
                                    // System.out.println(nama_file);
                                }
                                if(obj.getTxt_dok19() != null){
                                    LOG.info("txt dok 19 begin upload id_pengajuan = "+tdrPengajuanVendor.getId_pengajuan());
                                    TdrMstFileLegalitasEntity file = tdrFileLegal.find("where dok = :dok", Parameters.with("dok", 19)).firstResult();
                                    String nama_file = file.getNama_file();
                                    String pathMinio = tdrPengajuanVendor.getId_pengajuan()+"/"+nama_file+"/"+obj.getTxt_dok19();
                                    String urlPath = folderRoot+"/"+obj.getTxt_dok19();
                                    // System.out.println(pathMinio);
                                    Boolean checkFile = this.checkFile(urlPath);
                                    if(tdrPengajuanVendor.getId_pengajuan() != null){
                                        if(checkFile){
                                            boolean get = this.check_object(bucket, pathMinio);
                                            if(!get){
                                                Boolean upload = this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                if(upload){
                                                    TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                    entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                    entityTdrIdentitas.setDok(19);
                                                    entityTdrIdentitas.setIs_eproc(0);
                                                    entityTdrIdentitas.setNama_dok(nama_file);
                                                    entityTdrIdentitas.setPath_minio(pathMinio);
                                                    entityTdrIdentitas.setStatus_skt(1);
                                                    entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                    tdrIdentitasMinio.persist(entityTdrIdentitas);
            
                                                    LOG.info("data berhasil di upload");
            
                                                }
                                            }else{
                                                this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                entityTdrIdentitas.setDok(19);
                                                entityTdrIdentitas.setIs_eproc(0);
                                                entityTdrIdentitas.setNama_dok(nama_file);
                                                entityTdrIdentitas.setPath_minio(pathMinio);
                                                entityTdrIdentitas.setStatus_skt(1);
                                                entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                tdrIdentitasMinio.persist(entityTdrIdentitas);
        
                                                LOG.info("data berhasil di upload");
                                            }
                                        }else{
                                            LOG.info("File not exist "+obj.getTxt_dok19());
                                        }
                                    }else{
                                        LOG.info("Id Pengajuan Not Exists");
                                    }
                                }
                                if(obj.getTxt_dok20() != null){
                                    LOG.info("txt dok 20 begin upload id_pengajuan = "+tdrPengajuanVendor.getId_pengajuan());
                                    TdrMstFileLegalitasEntity file = tdrFileLegal.find("where dok = :dok", Parameters.with("dok", 20)).firstResult();
                                    String nama_file = file.getNama_file();
                                    String pathMinio = tdrPengajuanVendor.getId_pengajuan()+"/"+nama_file+"/"+obj.getTxt_dok20();
                                    String urlPath = folderRoot+"/"+obj.getTxt_dok20();
                                    // System.out.println(pathMinio);
                                    Boolean checkFile = this.checkFile(urlPath);
                                    if(tdrPengajuanVendor.getId_pengajuan() != null){
                                        if(checkFile){
                                            boolean get = this.check_object(bucket, pathMinio);
                                            if(!get){
                                                Boolean upload = this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                if(upload){
                                                    TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                    entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                    entityTdrIdentitas.setDok(20);
                                                    entityTdrIdentitas.setIs_eproc(0);
                                                    entityTdrIdentitas.setNama_dok(nama_file);
                                                    entityTdrIdentitas.setPath_minio(pathMinio);
                                                    entityTdrIdentitas.setStatus_skt(1);
                                                    entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                    tdrIdentitasMinio.persist(entityTdrIdentitas);
            
                                                    LOG.info("data berhasil di upload");
            
                                                }
                                            }else{
                                                this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                entityTdrIdentitas.setDok(20);
                                                entityTdrIdentitas.setIs_eproc(0);
                                                entityTdrIdentitas.setNama_dok(nama_file);
                                                entityTdrIdentitas.setPath_minio(pathMinio);
                                                entityTdrIdentitas.setStatus_skt(1);
                                                entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                tdrIdentitasMinio.persist(entityTdrIdentitas);
        
                                                LOG.info("data berhasil di upload");
                                            }
                                        }else{
                                            LOG.info("File not exist "+obj.getTxt_dok20());
                                        }
                                    }else{
                                        LOG.info("Id Pengajuan Not Exists");
                                    }
                                }
                                if(obj.getTxt_dok21() != null){
                                    LOG.info("txt dok 21 begin upload id_pengajuan = "+tdrPengajuanVendor.getId_pengajuan());
                                    TdrMstFileLegalitasEntity file = tdrFileLegal.find("where dok = :dok", Parameters.with("dok", 21)).firstResult();
                                    String nama_file = file.getNama_file();
                                    String pathMinio = tdrPengajuanVendor.getId_pengajuan()+"/"+nama_file+"/"+obj.getTxt_dok21();
                                    String urlPath = folderRoot+"/"+obj.getTxt_dok21();
                                    // System.out.println(pathMinio);
                                    Boolean checkFile = this.checkFile(urlPath);
                                    if(tdrPengajuanVendor.getId_pengajuan() != null){
                                        if(checkFile){
                                            boolean get = this.check_object(bucket, pathMinio);
                                            if(!get){
                                                Boolean upload = this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                if(upload){
                                                    TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                    entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                    entityTdrIdentitas.setDok(21);
                                                    entityTdrIdentitas.setIs_eproc(0);
                                                    entityTdrIdentitas.setNama_dok(nama_file);
                                                    entityTdrIdentitas.setPath_minio(pathMinio);
                                                    entityTdrIdentitas.setStatus_skt(1);
                                                    entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                    tdrIdentitasMinio.persist(entityTdrIdentitas);
            
                                                    LOG.info("data berhasil di upload");
            
                                                }
                                            }else{
                                                this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                entityTdrIdentitas.setDok(21);
                                                entityTdrIdentitas.setIs_eproc(0);
                                                entityTdrIdentitas.setNama_dok(nama_file);
                                                entityTdrIdentitas.setPath_minio(pathMinio);
                                                entityTdrIdentitas.setStatus_skt(1);
                                                entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                tdrIdentitasMinio.persist(entityTdrIdentitas);
        
                                                LOG.info("data berhasil di upload");
                                            }
                                        }else{
                                            LOG.info("File not exist "+obj.getTxt_dok21());
                                        }
                                    }else{
                                        LOG.info("Id Pengajuan Not Exists");
                                    }
                                    // System.out.println(nama_file);
                                }
                                if(obj.getTxt_dok22() != null){
                                    LOG.info("txt dok 22 begin upload id_pengajuan = "+tdrPengajuanVendor.getId_pengajuan());
                                    TdrMstFileLegalitasEntity file = tdrFileLegal.find("where dok = :dok", Parameters.with("dok", 22)).firstResult();
                                    String nama_file = file.getNama_file();
                                    String pathMinio = tdrPengajuanVendor.getId_pengajuan()+"/"+nama_file+"/"+obj.getTxt_dok22();
                                    String urlPath = folderRoot+"/"+obj.getTxt_dok22();
                                    // System.out.println(pathMinio);
                                    Boolean checkFile = this.checkFile(urlPath);
                                    if(tdrPengajuanVendor.getId_pengajuan() != null){
                                        if(checkFile){
                                            boolean get = this.check_object(bucket, pathMinio);
                                            if(!get){
                                                Boolean upload = this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                if(upload){
                                                    TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                    entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                    entityTdrIdentitas.setDok(22);
                                                    entityTdrIdentitas.setIs_eproc(0);
                                                    entityTdrIdentitas.setNama_dok(nama_file);
                                                    entityTdrIdentitas.setPath_minio(pathMinio);
                                                    entityTdrIdentitas.setStatus_skt(1);
                                                    entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                    tdrIdentitasMinio.persist(entityTdrIdentitas);
            
                                                    LOG.info("data berhasil di upload");
            
                                                }
                                            }else{
                                                this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                entityTdrIdentitas.setDok(22);
                                                entityTdrIdentitas.setIs_eproc(0);
                                                entityTdrIdentitas.setNama_dok(nama_file);
                                                entityTdrIdentitas.setPath_minio(pathMinio);
                                                entityTdrIdentitas.setStatus_skt(1);
                                                entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                tdrIdentitasMinio.persist(entityTdrIdentitas);
        
                                                LOG.info("data berhasil di upload");
                                            }
                                        }else{
                                            LOG.info("File not exist "+obj.getTxt_dok22());
                                        }
                                    }else{
                                        LOG.info("Id Pengajuan Not Exists");
                                    }
                                }
                                if(obj.getTxt_dok23() != null){
                                    LOG.info("txt dok 23 begin upload id_pengajuan = "+tdrPengajuanVendor.getId_pengajuan());
                                    TdrMstFileLegalitasEntity file = tdrFileLegal.find("where dok = :dok", Parameters.with("dok", 23)).firstResult();
                                    String nama_file = file.getNama_file();
                                    String pathMinio = tdrPengajuanVendor.getId_pengajuan()+"/"+nama_file+"/"+obj.getTxt_dok23();
                                    String urlPath = folderRoot+"/"+obj.getTxt_dok23();
                                    // System.out.println(pathMinio);
                                    Boolean checkFile = this.checkFile(urlPath);
                                    if(tdrPengajuanVendor.getId_pengajuan() != null){
                                        if(checkFile){
                                            boolean get = this.check_object(bucket, pathMinio);
                                            if(!get){
                                                Boolean upload = this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                if(upload){
                                                    TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                    entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                    entityTdrIdentitas.setDok(23);
                                                    entityTdrIdentitas.setIs_eproc(0);
                                                    entityTdrIdentitas.setNama_dok(nama_file);
                                                    entityTdrIdentitas.setPath_minio(pathMinio);
                                                    entityTdrIdentitas.setStatus_skt(1);
                                                    entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                    tdrIdentitasMinio.persist(entityTdrIdentitas);
            
                                                    LOG.info("data berhasil di upload");
            
                                                }
                                            }else{
                                                this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                entityTdrIdentitas.setDok(23);
                                                entityTdrIdentitas.setIs_eproc(0);
                                                entityTdrIdentitas.setNama_dok(nama_file);
                                                entityTdrIdentitas.setPath_minio(pathMinio);
                                                entityTdrIdentitas.setStatus_skt(1);
                                                entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                tdrIdentitasMinio.persist(entityTdrIdentitas);
        
                                                LOG.info("data berhasil di upload");
                                            }
                                        }else{
                                            LOG.info("File not exist "+obj.getTxt_dok23());
                                        }
                                    }else{
                                        LOG.info("Id Pengajuan Not Exists");
                                    }
                                }
                                if(obj.getTxt_dok24() != null){
                                    LOG.info("txt dok 24 begin upload id_pengajuan = "+tdrPengajuanVendor.getId_pengajuan());
                                    TdrMstFileLegalitasEntity file = tdrFileLegal.find("where dok = :dok", Parameters.with("dok", 24)).firstResult();
                                    String nama_file = file.getNama_file();
                                    // System.out.println(nama_file);
                                    String pathMinio = tdrPengajuanVendor.getId_pengajuan()+"/"+nama_file+"/"+obj.getTxt_dok24();
                                    String urlPath = folderRoot+"/"+obj.getTxt_dok24();
                                    // System.out.println(pathMinio);
                                    Boolean checkFile = this.checkFile(urlPath);
                                    if(tdrPengajuanVendor.getId_pengajuan() != null){
                                        if(checkFile){
                                            boolean get = this.check_object(bucket, pathMinio);
                                            if(!get){
                                                Boolean upload = this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                if(upload){
                                                    TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                    entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                    entityTdrIdentitas.setDok(24);
                                                    entityTdrIdentitas.setIs_eproc(0);
                                                    entityTdrIdentitas.setNama_dok(nama_file);
                                                    entityTdrIdentitas.setPath_minio(pathMinio);
                                                    entityTdrIdentitas.setStatus_skt(1);
                                                    entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                    tdrIdentitasMinio.persist(entityTdrIdentitas);
            
                                                    LOG.info("data berhasil di upload");
            
                                                }
                                            }else{
                                                this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                entityTdrIdentitas.setDok(24);
                                                entityTdrIdentitas.setIs_eproc(0);
                                                entityTdrIdentitas.setNama_dok(nama_file);
                                                entityTdrIdentitas.setPath_minio(pathMinio);
                                                entityTdrIdentitas.setStatus_skt(1);
                                                entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                tdrIdentitasMinio.persist(entityTdrIdentitas);
        
                                                LOG.info("data berhasil di upload");
                                            }
                                        }else{
                                            LOG.info("File not exist "+obj.getTxt_dok24());
                                        }
                                    }else{
                                        LOG.info("Id Pengajuan Not Exists");
                                    }
                                }
                                if(obj.getTxt_dok25() != null){
                                    LOG.info("txt dok 25 begin upload id_pengajuan = "+tdrPengajuanVendor.getId_pengajuan());
                                    TdrMstFileLegalitasEntity file = tdrFileLegal.find("where dok = :dok", Parameters.with("dok", 25)).firstResult();
                                    String nama_file = file.getNama_file();
                                    String pathMinio = tdrPengajuanVendor.getId_pengajuan()+"/"+nama_file+"/"+obj.getTxt_dok25();
                                    String urlPath = folderRoot+"/"+obj.getTxt_dok25();
                                    // System.out.println(pathMinio);
                                    Boolean checkFile = this.checkFile(urlPath);
                                    if(tdrPengajuanVendor.getId_pengajuan() != null){
                                        if(checkFile){
                                            boolean get = this.check_object(bucket, pathMinio);
                                            if(!get){
                                                Boolean upload = this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                if(upload){
                                                    TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                    entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                    entityTdrIdentitas.setDok(25);
                                                    entityTdrIdentitas.setIs_eproc(0);
                                                    entityTdrIdentitas.setNama_dok(nama_file);
                                                    entityTdrIdentitas.setPath_minio(pathMinio);
                                                    entityTdrIdentitas.setStatus_skt(1);
                                                    entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                    tdrIdentitasMinio.persist(entityTdrIdentitas);
            
                                                    LOG.info("data berhasil di upload");
            
                                                }
                                            }else{
                                                this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                entityTdrIdentitas.setDok(25);
                                                entityTdrIdentitas.setIs_eproc(0);
                                                entityTdrIdentitas.setNama_dok(nama_file);
                                                entityTdrIdentitas.setPath_minio(pathMinio);
                                                entityTdrIdentitas.setStatus_skt(1);
                                                entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                tdrIdentitasMinio.persist(entityTdrIdentitas);
        
                                                LOG.info("data berhasil di upload");
                                            }
                                        }else{
                                            LOG.info("File not exist "+obj.getTxt_dok25());
                                        }
                                    }else{
                                        LOG.info("Id Pengajuan Not Exists");
                                    }
                                }
                                if(obj.getTxt_dok26() != null){
                                    LOG.info("txt dok 26 begin upload id_pengajuan = "+tdrPengajuanVendor.getId_pengajuan());
                                    TdrMstFileLegalitasEntity file = tdrFileLegal.find("where dok = :dok", Parameters.with("dok", 26)).firstResult();
                                    String nama_file = file.getNama_file();
                                    // System.out.println(nama_file);
                                    String pathMinio = tdrPengajuanVendor.getId_pengajuan()+"/"+nama_file+"/"+obj.getTxt_dok26();
                                    String urlPath = folderRoot+"/"+obj.getTxt_dok26();
                                    // System.out.println(pathMinio);
                                    Boolean checkFile = this.checkFile(urlPath);
                                    if(tdrPengajuanVendor.getId_pengajuan() != null){
                                        if(checkFile){
                                            boolean get = this.check_object(bucket, pathMinio);
                                            if(!get){
                                                Boolean upload = this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                if(upload){
                                                    TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                    entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                    entityTdrIdentitas.setDok(26);
                                                    entityTdrIdentitas.setIs_eproc(0);
                                                    entityTdrIdentitas.setNama_dok(nama_file);
                                                    entityTdrIdentitas.setPath_minio(pathMinio);
                                                    entityTdrIdentitas.setStatus_skt(1);
                                                    entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                    tdrIdentitasMinio.persist(entityTdrIdentitas);
            
                                                    LOG.info("data berhasil di upload");
            
                                                }
                                            }else{
                                                this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                entityTdrIdentitas.setDok(26);
                                                entityTdrIdentitas.setIs_eproc(0);
                                                entityTdrIdentitas.setNama_dok(nama_file);
                                                entityTdrIdentitas.setPath_minio(pathMinio);
                                                entityTdrIdentitas.setStatus_skt(1);
                                                entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                tdrIdentitasMinio.persist(entityTdrIdentitas);
        
                                                LOG.info("data berhasil di upload");
                                            }
                                        }else{
                                            LOG.info("File not exist "+obj.getTxt_dok26());
                                        }
                                    }else{
                                        LOG.info("Id Pengajuan Not Exists");
                                    }
                                }
                                if(obj.getTxt_dok27() != null){
                                    LOG.info("txt dok 27 begin upload id_pengajuan = "+tdrPengajuanVendor.getId_pengajuan());
                                    TdrMstFileLegalitasEntity file = tdrFileLegal.find("where dok = :dok", Parameters.with("dok", 27)).firstResult();
                                    String nama_file = file.getNama_file();
                                    String pathMinio = tdrPengajuanVendor.getId_pengajuan()+"/"+nama_file+"/"+obj.getTxt_dok27();
                                    String urlPath = folderRoot+"/"+obj.getTxt_dok27();
                                    // System.out.println(pathMinio);
                                    Boolean checkFile = this.checkFile(urlPath);
                                    if(tdrPengajuanVendor.getId_pengajuan() != null){
                                        if(checkFile){
                                            boolean get = this.check_object(bucket, pathMinio);
                                            if(!get){
                                                Boolean upload = this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                if(upload){
                                                    TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                    entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                    entityTdrIdentitas.setDok(27);
                                                    entityTdrIdentitas.setIs_eproc(0);
                                                    entityTdrIdentitas.setNama_dok(nama_file);
                                                    entityTdrIdentitas.setPath_minio(pathMinio);
                                                    entityTdrIdentitas.setStatus_skt(1);
                                                    entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                    tdrIdentitasMinio.persist(entityTdrIdentitas);
            
                                                    LOG.info("data berhasil di upload");
            
                                                }
                                            }else{
                                                this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                entityTdrIdentitas.setDok(27);
                                                entityTdrIdentitas.setIs_eproc(0);
                                                entityTdrIdentitas.setNama_dok(nama_file);
                                                entityTdrIdentitas.setPath_minio(pathMinio);
                                                entityTdrIdentitas.setStatus_skt(1);
                                                entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                tdrIdentitasMinio.persist(entityTdrIdentitas);
        
                                                LOG.info("data berhasil di upload");
                                            }
                                        }else{
                                            LOG.info("File not exist "+obj.getTxt_dok27());
                                        }
                                    }else{
                                        LOG.info("Id Pengajuan Not Exists");
                                    }
                                }
                                if(obj.getTxt_dok28() != null){
                                    LOG.info("txt dok 28 begin upload id_pengajuan = "+tdrPengajuanVendor.getId_pengajuan());
                                    TdrMstFileLegalitasEntity file = tdrFileLegal.find("where dok = :dok", Parameters.with("dok", 28)).firstResult();
                                    String nama_file = file.getNama_file();
                                    String pathMinio = tdrPengajuanVendor.getId_pengajuan()+"/"+nama_file+"/"+obj.getTxt_dok28();
                                    String urlPath = folderRoot+"/"+obj.getTxt_dok28();
                                    // System.out.println(pathMinio);
                                    Boolean checkFile = this.checkFile(urlPath);
                                    if(tdrPengajuanVendor.getId_pengajuan() != null){
                                        if(checkFile){
                                            boolean get = this.check_object(bucket, pathMinio);
                                            if(!get){
                                                Boolean upload = this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                if(upload){
                                                    TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                    entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                    entityTdrIdentitas.setDok(28);
                                                    entityTdrIdentitas.setIs_eproc(0);
                                                    entityTdrIdentitas.setNama_dok(nama_file);
                                                    entityTdrIdentitas.setPath_minio(pathMinio);
                                                    entityTdrIdentitas.setStatus_skt(1);
                                                    entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                    tdrIdentitasMinio.persist(entityTdrIdentitas);
            
                                                    LOG.info("data berhasil di upload");
            
                                                }
                                            }else{
                                                    this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                    TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                    entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                    entityTdrIdentitas.setDok(28);
                                                    entityTdrIdentitas.setIs_eproc(0);
                                                    entityTdrIdentitas.setNama_dok(nama_file);
                                                    entityTdrIdentitas.setPath_minio(pathMinio);
                                                    entityTdrIdentitas.setStatus_skt(1);
                                                    entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                    tdrIdentitasMinio.persist(entityTdrIdentitas);
            
                                                    LOG.info("data berhasil di upload");
                                            }
                                        }else{
                                            LOG.info("File not exist "+obj.getTxt_dok28());
                                        }
                                    }else{
                                        LOG.info("Id Pengajuan Not Exists");
                                    }
                                }
                                if(obj.getTxt_dok29() != null){
                                    LOG.info("txt dok 29 begin upload id_pengajuan = "+tdrPengajuanVendor.getId_pengajuan());
                                    TdrMstFileLegalitasEntity file = tdrFileLegal.find("where dok = :dok", Parameters.with("dok", 29)).firstResult();
                                    String nama_file = file.getNama_file();
                                    String pathMinio = tdrPengajuanVendor.getId_pengajuan()+"/"+nama_file+"/"+obj.getTxt_dok29();
                                    String urlPath = folderRoot+"/"+obj.getTxt_dok29();
                                    // System.out.println(pathMinio);
                                    Boolean checkFile = this.checkFile(urlPath);
                                    if(tdrPengajuanVendor.getId_pengajuan() != null){
                                        if(checkFile){
                                            boolean get = this.check_object(bucket, pathMinio);
                                            if(!get){
                                                Boolean upload = this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                if(upload){
                                                    TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                    entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                    entityTdrIdentitas.setDok(29);
                                                    entityTdrIdentitas.setIs_eproc(0);
                                                    entityTdrIdentitas.setNama_dok(nama_file);
                                                    entityTdrIdentitas.setPath_minio(pathMinio);
                                                    entityTdrIdentitas.setStatus_skt(1);
                                                    entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                    tdrIdentitasMinio.persist(entityTdrIdentitas);
            
                                                    LOG.info("data berhasil di upload");
            
                                                }
                                            }else{
                                                this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                entityTdrIdentitas.setDok(29);
                                                entityTdrIdentitas.setIs_eproc(0);
                                                entityTdrIdentitas.setNama_dok(nama_file);
                                                entityTdrIdentitas.setPath_minio(pathMinio);
                                                entityTdrIdentitas.setStatus_skt(1);
                                                entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                tdrIdentitasMinio.persist(entityTdrIdentitas);
        
                                                LOG.info("data berhasil di upload");
                                            }
                                        }else{
                                            LOG.info("File not exist "+obj.getTxt_dok29());
                                        }
                                    }else{
                                        LOG.info("Id Pengajuan Not Exists");
                                    }
                                    // System.out.println(nama_file);
                                }
                                if(obj.getTxt_dok30() != null){
                                    TdrMstFileLegalitasEntity file = tdrFileLegal.find("where dok = :dok", Parameters.with("dok", 30)).firstResult();
                                    String nama_file = file.getNama_file();
                                    String pathMinio = tdrPengajuanVendor.getId_pengajuan()+"/"+nama_file+"/"+obj.getTxt_dok30();
                                    String urlPath = folderRoot+"/"+obj.getTxt_dok30();
                                    // System.out.println(pathMinio);
                                    Boolean checkFile = this.checkFile(urlPath);
                                    if(tdrPengajuanVendor.getId_pengajuan() != null){
                                        if(checkFile){
                                            boolean get = this.check_object(bucket, pathMinio);
                                            if(!get){
                                                Boolean upload = this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                if(upload){
                                                    TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                    entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                    entityTdrIdentitas.setDok(30);
                                                    entityTdrIdentitas.setIs_eproc(0);
                                                    entityTdrIdentitas.setNama_dok(nama_file);
                                                    entityTdrIdentitas.setPath_minio(pathMinio);
                                                    entityTdrIdentitas.setStatus_skt(1);
                                                    entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                    tdrIdentitasMinio.persist(entityTdrIdentitas);
            
                                                    LOG.info("data berhasil di upload");
            
                                                }
                                            }else{
                                                this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                TdrIdentitasVendorMinioEntity entityTdrIdentitas = new TdrIdentitasVendorMinioEntity();
                                                entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                entityTdrIdentitas.setDok(30);
                                                entityTdrIdentitas.setIs_eproc(0);
                                                entityTdrIdentitas.setNama_dok(nama_file);
                                                entityTdrIdentitas.setPath_minio(pathMinio);
                                                entityTdrIdentitas.setStatus_skt(1);
                                                entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                tdrIdentitasMinio.persist(entityTdrIdentitas);
                                                LOG.info("data berhasil di upload");
                                            }
                                        }else{
                                            LOG.info("File not exist "+obj.getTxt_dok30());
                                        }
                                    }else{
                                        LOG.info("Id Pengajuan Not Exists");
                                    }
                                }
        
        
        
                                
        
                               
        
        
        
                            } catch (Exception e) {
                                try {
                                    Thread.sleep(1000);
                                } catch (Exception f) {
                                    LOG.error(f);
                                    // TODO: handle exception
                                }
                                
                                LOG.info("error = "+e);
                            }
    
    
                            try {
                                List<TdrPengalamanVendorEntity> getPengalaman = tdrPengalamanVendor.find("where id_identitas = :id_identitas", Parameters.with("id_identitas", i.getId_identitas())).list();
                                List<TdrPengalamanVendorMinioEntity> dPengalamanList = tdrPengalamanVendorMinio.find("where id_identitas = ?1", i.getId_identitas()).list();
                                if(dPengalamanList.size() > 0){
                                    Long dPengalaman = tdrPengalamanVendorMinio.delete("where id_identitas = :id", Parameters.with("id", i.getId_identitas()));
                                }
                                // System.out.println("delete pengalaman = "+dPengalaman);
                                for (TdrPengalamanVendorEntity f : getPengalaman) {
                                    String nama_file = f.getSpk_path();
                                    String urlPath = folderRoot+"/"+nama_file;
                                    String pathMinio = tdrPengajuanVendor.getId_pengajuan()+"/Pengalaman/"+nama_file;
                                    Boolean checkFile = this.checkFile(urlPath);
                
                                    if(tdrPengajuanVendor.getId_pengajuan() != null){
                                        if(checkFile){
                                            boolean get = this.check_object(bucket, pathMinio);
                                            if(!get){
                                                Boolean upload = this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                if(upload){
                                                    
                                                    try {
                                                        TdrPengalamanVendorMinioEntity tdrPengalamanSave = new TdrPengalamanVendorMinioEntity();
                                                        tdrPengalamanSave.setId_identitas(f.getId_identitas());
                                                        tdrPengalamanSave.setNama_pekerjaan(f.getNama_pekerjaan());
                                                        tdrPengalamanSave.setNama_pemilik_pekerja(f.getNama_pemilik_pekerja());
                                                        tdrPengalamanSave.setBidang_usaha(f.getBidang_usaha());
                                                        tdrPengalamanSave.setSub_bidang_usaha(f.getSub_bidang_usaha());
                                                        tdrPengalamanSave.setPemberi_tugas(f.getPemberi_tugas());
                                                        tdrPengalamanSave.setNo_spk(f.getNo_spk());
                                                        tdrPengalamanSave.setTgl_mulai_spk(f.getTgl_mulai_spk());
                                                        tdrPengalamanSave.setTgl_selesai_spk(f.getTgl_selesai_spk());
                                                        tdrPengalamanSave.setNilai_kontrak(f.getNilai_kontrak());
                                                        tdrPengalamanSave.setBa_pekerjaan(f.getBa_pekerjaan());
                                                        tdrPengalamanSave.setSpk_path(f.getSpk_path());
                                                        tdrPengalamanSave.setMinio_path(pathMinio);
                                                        tdrPengalamanSave.setUpload_minio(LocalDateTime.now());
                                                        tdrPengalamanVendorMinio.persist(tdrPengalamanSave);
                                                        LOG.info("data Pengalaman berhasil di upload");
                                                    } catch (Exception e) {
                                                        // System.err.println(e);
                                                        LOG.error("Pengalaman Save Minio "+e);
                                                        // TODO: handle exception
                                                    }
                
                
                                                    
                
                                                }
                                            }else{
                                                try {
                                                    this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                    TdrPengalamanVendorMinioEntity tdrPengalamanSave = new TdrPengalamanVendorMinioEntity();
                                                    tdrPengalamanSave.setId_identitas(f.getId_identitas());
                                                    tdrPengalamanSave.setNama_pekerjaan(f.getNama_pekerjaan());
                                                    tdrPengalamanSave.setNama_pemilik_pekerja(f.getNama_pemilik_pekerja());
                                                    tdrPengalamanSave.setBidang_usaha(f.getBidang_usaha());
                                                    tdrPengalamanSave.setSub_bidang_usaha(f.getSub_bidang_usaha());
                                                    tdrPengalamanSave.setPemberi_tugas(f.getPemberi_tugas());
                                                    tdrPengalamanSave.setNo_spk(f.getNo_spk());
                                                    tdrPengalamanSave.setTgl_mulai_spk(f.getTgl_mulai_spk());
                                                    tdrPengalamanSave.setTgl_selesai_spk(f.getTgl_selesai_spk());
                                                    tdrPengalamanSave.setNilai_kontrak(f.getNilai_kontrak());
                                                    tdrPengalamanSave.setBa_pekerjaan(f.getBa_pekerjaan());
                                                    tdrPengalamanSave.setSpk_path(f.getSpk_path());
                                                    tdrPengalamanSave.setMinio_path(pathMinio);
                                                    tdrPengalamanSave.setUpload_minio(LocalDateTime.now());
                                                    tdrPengalamanVendorMinio.persist(tdrPengalamanSave);
                                                    LOG.info("data Pengalaman berhasil di upload");
                                                } catch (Exception e) {
                                                    // System.err.println(e);
                                                    LOG.error("Pengalaman Save Minio "+e);
                                                    // TODO: handle exception
                                                }
                                            }
                                        }else{
                                            LOG.info("File not exist "+nama_file);
                                        }
                                    }else{
                                        LOG.info("Id Pengajuan Not Exists");
                                    }
                
                
                                }
                            } catch (Exception g) {
                                try {
                                    Thread.sleep(1000);
                                } catch (Exception k) {
                                    LOG.error("pengalaman k = "+k);
                                    // TODO: handle exception
                                }
                                LOG.error("pengalaman g = "+g);
                                // TODO: handle exception
                            }
            
            
                            try {
                                List<TdrPerbankanVendorEntity> tdrPerbankanEntity = tdrPerbankanVendor.find("where id_identitas = :id_identitas", Parameters.with("id_identitas", i.getId_identitas())).list();
                                List<TdrPerbankanVendorMinioEntity> tdrPerbankanList = tdrPerbankanVendorMinio.find("where id_identitas = ?1", i.getId_identitas()).list();
                                if(tdrPerbankanList.size() > 0){
                                    Long dPerbankan = tdrPerbankanVendorMinio.delete("where id_identitas = :id", Parameters.with("id", i.getId_identitas()));
                                }
                                // Long dPerbankan = tdrPerbankanVendorMinio.delete("where id_identitas = :id", Parameters.with("id", i.getId_identitas()));
                                // System.out.println("delete perbankan = "+dPerbankan);
                                for (TdrPerbankanVendorEntity p : tdrPerbankanEntity) {
                                    String nama_file = p.getKoran_path();
                                    String urlPath = folderRoot+"/"+nama_file;
                                    String pathMinio = tdrPengajuanVendor.getId_pengajuan()+"/Perbankan/"+nama_file;
                                    Boolean checkFile = this.checkFile(urlPath);
                
                                    if(tdrPengajuanVendor.getId_pengajuan() != null){
                                        if(p.getMinio_status() == 1){
                                            TdrPerbankanVendorMinioEntity TPBM = new TdrPerbankanVendorMinioEntity();
                                            TPBM.setId_identitas(p.getId_identitas());
                                            TPBM.setHave_account_bri(p.getHave_account_bri());
                                            TPBM.setLokasi_bank(p.getLokasi_bank());
                                            TPBM.setKode_bank(p.getKode_bank());
                                            TPBM.setNama_bank(p.getNama_bank());
                                            TPBM.setCity(p.getCity());
                                            TPBM.setBank_key(p.getBank_key());
                                            TPBM.setNegara(p.getNegara());
                                            TPBM.setKode_negara(p.getKode_negara());
                                            TPBM.setNo_rek(p.getNo_rek());
                                            TPBM.setNama_rek(p.getNama_rek());
                                            TPBM.setSwift_kode(p.getSwift_kode());
                                            TPBM.setCabang_bank(p.getCabang_bank());
                                            TPBM.setKoran_path(p.getKoran_path());
                                            TPBM.setMinio_path(p.getKoran_path());
                                            TPBM.setUpload_minio(LocalDateTime.now());
                                            tdrPerbankanVendorMinio.persist(TPBM);
                                        }else{

                                            if(checkFile){
                                                boolean get = this.check_object(bucket, pathMinio);
                                                if(!get){
                                                    Boolean upload = this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                    if(upload){
                                                        
                                                        try {
                                                            TdrPerbankanVendorMinioEntity TPBM = new TdrPerbankanVendorMinioEntity();
                                                            TPBM.setId_identitas(p.getId_identitas());
                                                            TPBM.setHave_account_bri(p.getHave_account_bri());
                                                            TPBM.setLokasi_bank(p.getLokasi_bank());
                                                            TPBM.setKode_bank(p.getKode_bank());
                                                            TPBM.setNama_bank(p.getNama_bank());
                                                            TPBM.setCity(p.getCity());
                                                            TPBM.setBank_key(p.getBank_key());
                                                            TPBM.setNegara(p.getNegara());
                                                            TPBM.setKode_negara(p.getKode_negara());
                                                            TPBM.setNo_rek(p.getNo_rek());
                                                            TPBM.setNama_rek(p.getNama_rek());
                                                            TPBM.setSwift_kode(p.getSwift_kode());
                                                            TPBM.setCabang_bank(p.getCabang_bank());
                                                            TPBM.setKoran_path(p.getKoran_path());
                                                            TPBM.setMinio_path(pathMinio);
                                                            TPBM.setUpload_minio(LocalDateTime.now());
                                                            tdrPerbankanVendorMinio.persist(TPBM);
                                                            LOG.info("data Perbankan berhasil di upload");
                                                        } catch (Exception e) {
                                                            // System.err.println(e);
                                                            LOG.error("Perbankan data save minio "+e);
                                                            // TODO: handle exception
                                                        }
                    
                    
                                                        
                    
                                                    }
                                                }else{
                                                    try {
                                                        this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                        TdrPerbankanVendorMinioEntity TPBM = new TdrPerbankanVendorMinioEntity();
                                                        TPBM.setId_identitas(p.getId_identitas());
                                                        TPBM.setHave_account_bri(p.getHave_account_bri());
                                                        TPBM.setLokasi_bank(p.getLokasi_bank());
                                                        TPBM.setKode_bank(p.getKode_bank());
                                                        TPBM.setNama_bank(p.getNama_bank());
                                                        TPBM.setCity(p.getCity());
                                                        TPBM.setBank_key(p.getBank_key());
                                                        TPBM.setNegara(p.getNegara());
                                                        TPBM.setKode_negara(p.getKode_negara());
                                                        TPBM.setNo_rek(p.getNo_rek());
                                                        TPBM.setNama_rek(p.getNama_rek());
                                                        TPBM.setSwift_kode(p.getSwift_kode());
                                                        TPBM.setCabang_bank(p.getCabang_bank());
                                                        TPBM.setKoran_path(p.getKoran_path());
                                                        TPBM.setMinio_path(pathMinio);
                                                        TPBM.setUpload_minio(LocalDateTime.now());
                                                        tdrPerbankanVendorMinio.persist(TPBM);
                                                        LOG.info("data Perbankan berhasil di upload");
                                                    } catch (Exception e) {
                                                        // System.err.println(e);
                                                        LOG.error("Perbankan data save minio "+e);
                                                        // TODO: handle exception
                                                    }
                                                }
                                            }else{
                                                LOG.info("File not exist "+nama_file);
                                            }


                                        }
                                        
                                    }else{
                                        LOG.info("Id Pengajuan Not Exists");
                                    }
                                }
            
                            } catch (Exception e) {
            
                                try {
                                    Thread.sleep(1000);
                                } catch (Exception k) {
                                    LOG.error("perbankan k = "+k);
                                    // TODO: handle exception
                                }
                                LOG.error("perbankan g = "+e);
                                // TODO: handle exception
                            }
                            
                            try {
                                List<TdrIdentitasVendorMinioEntity> tdrListB = tdrIdentitasMinio.find("id_pengajuan = ?1", tdrPengajuanVendor.getId_pengajuan()).list();
                                List<TdrPengalamanVendorMinioEntity> dPengalamanListA = tdrPengalamanVendorMinio.find("where id_identitas = ?1", i.getId_identitas()).list();
                                List<TdrPerbankanVendorMinioEntity> tdrPerbankanListC = tdrPerbankanVendorMinio.find("where id_identitas = ?1", i.getId_identitas()).list();
                                
                                if(tdrListB.size() > 0 && dPengalamanListA.size() > 0 && tdrPerbankanListC.size() > 0){
                                    TdrIdentitasVendorEntity entityIdentitasVendor = tdrIdentitas.find("where id_identitas = :id_identitas", Parameters.with("id_identitas", i.getId_identitas())).firstResult();
                                    entityIdentitasVendor.setIs_minio_identitas_vendor(1);
                                    tdrIdentitas.persist(entityIdentitasVendor);
                                    LOG.info("Upload SKT done ="+i.getId_identitas());
                                }
                                
                                
                            } catch (Exception e) {
                                LOG.error("error = "+e.getMessage()+" id_pengajuan = "+tdrPengajuanVendor.getId_pengajuan());
                                // TODO: handle exception
                            }
    
    
                        
                            
                            
                        }else{
                            LOG.info("TDR Pengajuan NULL id_identitas = "+i.getId_identitas());
                            // if(tdrPengajuanVendor == null){
                            //     LOG.info("TDR Pengajuan Kosong");
                            // }else{
                            //     if(tdrPengajuanVendor.getId_pengajuan() == null){
                            //         LOG.info("ID Pengajuan Kosong");
                            //     }else{
                            //         if(tdrPengajuanVendor.getId_vendor_eproc() == null){
                            //             LOG.info("Id Vendor Eproc kosong");
                            //         }
                            //     }   
                            // }
                        }
                        // System.out.println(tdrPengajuanVendor.getId_pengajuan());
    
                        
                    }else{
                        LOG.info("Dokumen Legalitas Not Exist "+i.getId_identitas());
                    }

                    
                }else{
                    LOG.info("tdr identitas vendor null ");
                }
                
    
    
                
            }



        } catch (Exception e) {
            // TODO: handle exception
            LOG.error("error upload skt", e);
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

    @Scheduled(every = "60s")
    @Transactional
    public void upload_non_skt(){
        LOG.info("Upload Non SKT begin");
        try {
            String bucket = ConfigProvider.getConfig().getValue("bucket.minio", String.class);
            boolean a = this.checkBucket(bucket);
            String folderRoot = ConfigProvider.getConfig().getValue("root.folder.sso", String.class);
           
            if(!a){
                this.createBucket(bucket);
            }
           
            
            List<TdrIdentitasVendorNonSktEntity> identitas = TdrIdentitasVendorNonSktEntity.find("is_minio_identitas_vendor is null OR is_minio_identitas_vendor = :id ORDER BY id_identitas DESC", Parameters.with("id", 0)).page(0, 50).list();
            
            if(identitas.size() > 0){

                for (TdrIdentitasVendorNonSktEntity vendorNonSkt : identitas) {
                    // System.out.println(vendorNonSkt.getId_identitas());
                    try {
                        
                        if(vendorNonSkt != null && vendorNonSkt.getDok_legalitas_path() != null && vendorNonSkt.getId_identitas() != null){
                            
                            TdrPengajuanVendorNonSktEntity tdrPengajuanVendor = TdrPengajuanVendorNonSktEntity.find("id_identitas = :id_identitas", Parameters.with("id_identitas", vendorNonSkt.getId_identitas())).firstResult();
                            try {   
                            if(tdrPengajuanVendor != null && tdrPengajuanVendor.getId_pengajuan() != null && vendorNonSkt.getDok_legalitas_path() != null){
                                DokLegalDto obj = this.parseJson(vendorNonSkt.getDok_legalitas_path());
                                List<TdrIdentitasVendorMinioNonSktEntity> tdrIdentitasVendorMinioNonSktList = TdrIdentitasVendorMinioNonSktEntity.find("id_pengajuan = :id_pengajuan", Parameters.with("id_pengajuan", tdrPengajuanVendor.getId_pengajuan())).list();
                                if(tdrIdentitasVendorMinioNonSktList.size() > 0){
                                    TdrIdentitasVendorMinioNonSktEntity.delete("id_pengajuan = :id_pengajuan", Parameters.with("id_pengajuan", tdrPengajuanVendor.getId_pengajuan()));
                                }
        
                                if(!obj.getTxt_dok8().isEmpty()){
                                    LOG.info("Dok 8 Upload Begin "+tdrPengajuanVendor.getId_pengajuan());
                                    
                                    TdrMstFileLegalitasEntity file = tdrFileLegal.find("where dok = :dok", Parameters.with("dok", 8)).firstResult();
                                    String nama_file = file.getNama_file();
                                    String pathMinio = tdrPengajuanVendor.getId_pengajuan()+"/"+nama_file+"/"+obj.getTxt_dok8();
                                    String urlPath = folderRoot+"/"+obj.getTxt_dok8();
                                    // System.out.println(pathMinio);
                                    Boolean checkFile = this.checkFile(urlPath);
                                    if(tdrPengajuanVendor.getId_pengajuan() != null){
                                        if(checkFile){
                                            boolean get = this.check_object(bucket, pathMinio);
                                            if(!get){
                                                Boolean upload = this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                if(upload){
                                                    TdrIdentitasVendorMinioNonSktEntity entityTdrIdentitas = new TdrIdentitasVendorMinioNonSktEntity();
                                                    entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                    entityTdrIdentitas.setDok(8);
                                                    entityTdrIdentitas.setIs_eproc(0);
                                                    entityTdrIdentitas.setNama_dok(nama_file);
                                                    entityTdrIdentitas.setPath_minio(pathMinio);
                                                    entityTdrIdentitas.setStatus_skt(0);
                                                    entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                    TdrIdentitasVendorMinioNonSktEntity.persist(entityTdrIdentitas);
                                                }else{
                                                    LOG.info("upload Gagal");
                                                }
                                            }else{
                                                this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                TdrIdentitasVendorMinioNonSktEntity entityTdrIdentitas = new TdrIdentitasVendorMinioNonSktEntity();
                                                entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                entityTdrIdentitas.setDok(8);
                                                entityTdrIdentitas.setIs_eproc(0);
                                                entityTdrIdentitas.setNama_dok(nama_file);
                                                entityTdrIdentitas.setPath_minio(pathMinio);
                                                entityTdrIdentitas.setStatus_skt(0);
                                                entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                TdrIdentitasVendorMinioNonSktEntity.persist(entityTdrIdentitas);
                                            }
                                        }else{
                                            LOG.info("File not exist "+obj.getTxt_dok8());
                                        }
                                    }else{
                                        LOG.info("Id Pengajuan Not Exists");
                                    }
                                }
        
                                if(!obj.getTxt_dok9().isEmpty()){
                                    LOG.info("Dok 9 Upload Begin "+tdrPengajuanVendor.getId_pengajuan());
                                    TdrMstFileLegalitasEntity file = tdrFileLegal.find("where dok = :dok", Parameters.with("dok", 9)).firstResult();
                                    String nama_file = file.getNama_file();
                                    String pathMinio = tdrPengajuanVendor.getId_pengajuan()+"/"+nama_file+"/"+obj.getTxt_dok9();
                                    String urlPath = folderRoot+"/"+obj.getTxt_dok9();
                                    // System.out.println(pathMinio);
                                    Boolean checkFile = this.checkFile(urlPath);
                                    if(tdrPengajuanVendor.getId_pengajuan() != null){
                                        if(checkFile){
                                            boolean get = this.check_object(bucket, pathMinio);
                                            if(!get){
                                                Boolean upload = this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                if(upload){
                                                    TdrIdentitasVendorMinioNonSktEntity entityTdrIdentitas = new TdrIdentitasVendorMinioNonSktEntity();
                                                    entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                    entityTdrIdentitas.setDok(9);
                                                    entityTdrIdentitas.setIs_eproc(0);
                                                    entityTdrIdentitas.setNama_dok(nama_file);
                                                    entityTdrIdentitas.setPath_minio(pathMinio);
                                                    entityTdrIdentitas.setStatus_skt(0);
                                                    entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                    TdrIdentitasVendorMinioNonSktEntity.persist(entityTdrIdentitas);
            
                                                    LOG.info("data berhasil di upload");
            
                                                }
                                            }else{
                                                this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                TdrIdentitasVendorMinioNonSktEntity entityTdrIdentitas = new TdrIdentitasVendorMinioNonSktEntity();
                                                entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                entityTdrIdentitas.setDok(9);
                                                entityTdrIdentitas.setIs_eproc(0);
                                                entityTdrIdentitas.setNama_dok(nama_file);
                                                entityTdrIdentitas.setPath_minio(pathMinio);
                                                entityTdrIdentitas.setStatus_skt(0);
                                                entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                TdrIdentitasVendorMinioNonSktEntity.persist(entityTdrIdentitas);
        
                                                LOG.info("data berhasil di upload");
                                            }
                                        }else{
                                            LOG.info("File not exist "+obj.getTxt_dok9());
                                        }
                                    }else{
                                        LOG.info("Id Pengajuan Not Exists");
                                    }
                                }
        
        
                                if(!obj.getTxt_dok3().isEmpty()){
                                    LOG.info("Dok 3 Upload Begin "+tdrPengajuanVendor.getId_pengajuan());
                                    TdrMstFileLegalitasEntity file = tdrFileLegal.find("where dok = :dok", Parameters.with("dok", 3)).firstResult();
                                    String nama_file = file.getNama_file();
                                    String pathMinio = tdrPengajuanVendor.getId_pengajuan()+"/"+nama_file+"/"+obj.getTxt_dok3();
                                    String urlPath = folderRoot+"/"+obj.getTxt_dok3();
                                    // System.out.println(pathMinio);
                                    Boolean checkFile = this.checkFile(urlPath);
                                    if(tdrPengajuanVendor.getId_pengajuan() != null){
                                        if(checkFile){
                                            boolean get = this.check_object(bucket, pathMinio);
                                            if(!get){
                                                Boolean upload = this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                if(upload){
                                                    TdrIdentitasVendorMinioNonSktEntity entityTdrIdentitas = new TdrIdentitasVendorMinioNonSktEntity();
                                                    entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                    entityTdrIdentitas.setDok(3);
                                                    entityTdrIdentitas.setIs_eproc(0);
                                                    entityTdrIdentitas.setNama_dok(nama_file);
                                                    entityTdrIdentitas.setPath_minio(pathMinio);
                                                    entityTdrIdentitas.setStatus_skt(0);
                                                    entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                    TdrIdentitasVendorMinioNonSktEntity.persist(entityTdrIdentitas);
            
                                                    LOG.info("data berhasil di upload");
            
                                                }
                                            }else{
                                                this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                TdrIdentitasVendorMinioNonSktEntity entityTdrIdentitas = new TdrIdentitasVendorMinioNonSktEntity();
                                                entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                entityTdrIdentitas.setDok(3);
                                                entityTdrIdentitas.setIs_eproc(0);
                                                entityTdrIdentitas.setNama_dok(nama_file);
                                                entityTdrIdentitas.setPath_minio(pathMinio);
                                                entityTdrIdentitas.setStatus_skt(0);
                                                entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                TdrIdentitasVendorMinioNonSktEntity.persist(entityTdrIdentitas);
        
                                                LOG.info("data berhasil di upload");
                                            }
                                        }else{
                                            LOG.info("File not exist "+obj.getTxt_dok3());
                                        }
                                    }else{
                                        LOG.info("Id Pengajuan Not Exists");
                                    }
                                }
        
        
                                if(!obj.getTxt_dok12().isEmpty()){
                                    LOG.info("Dok 12 Upload Begin "+tdrPengajuanVendor.getId_pengajuan());
                                    TdrMstFileLegalitasEntity file = tdrFileLegal.find("where dok = :dok", Parameters.with("dok", 12)).firstResult();
                                    String nama_file = file.getNama_file();
                                    String pathMinio = tdrPengajuanVendor.getId_pengajuan()+"/"+nama_file+"/"+obj.getTxt_dok12();
                                    String urlPath = folderRoot+"/"+obj.getTxt_dok12();
                                    // System.out.println(pathMinio);
                                    Boolean checkFile = this.checkFile(urlPath);
                                    if(tdrPengajuanVendor.getId_pengajuan() != null){
                                        if(checkFile){
                                            boolean get = this.check_object(bucket, pathMinio);
                                            if(!get){
                                                Boolean upload = this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                if(upload){
                                                    TdrIdentitasVendorMinioNonSktEntity entityTdrIdentitas = new TdrIdentitasVendorMinioNonSktEntity();
                                                    entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                    entityTdrIdentitas.setDok(12);
                                                    entityTdrIdentitas.setIs_eproc(0);
                                                    entityTdrIdentitas.setNama_dok(nama_file);
                                                    entityTdrIdentitas.setPath_minio(pathMinio);
                                                    entityTdrIdentitas.setStatus_skt(0);
                                                    entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                    TdrIdentitasVendorMinioNonSktEntity.persist(entityTdrIdentitas);
            
                                                    LOG.info("data berhasil di upload");
            
                                                }
                                            }else{
                                                this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                TdrIdentitasVendorMinioNonSktEntity entityTdrIdentitas = new TdrIdentitasVendorMinioNonSktEntity();
                                                entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                entityTdrIdentitas.setDok(12);
                                                entityTdrIdentitas.setIs_eproc(0);
                                                entityTdrIdentitas.setNama_dok(nama_file);
                                                entityTdrIdentitas.setPath_minio(pathMinio);
                                                entityTdrIdentitas.setStatus_skt(0);
                                                entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                TdrIdentitasVendorMinioNonSktEntity.persist(entityTdrIdentitas);
        
                                                LOG.info("data berhasil di upload");
                                            }
                                        }else{
                                            LOG.info("File not exist "+obj.getTxt_dok12());
                                        }
                                    }else{
                                        LOG.info("Id Pengajuan Not Exists");
                                    }
                                }
        
        
                                if(!obj.getTxt_dok26().isEmpty()){
                                    LOG.info("Dok 26 Upload Begin "+tdrPengajuanVendor.getId_pengajuan());
                                    TdrMstFileLegalitasEntity file = tdrFileLegal.find("where dok = :dok", Parameters.with("dok", 26)).firstResult();
                                    String nama_file = file.getNama_file();
                                    String pathMinio = tdrPengajuanVendor.getId_pengajuan()+"/"+nama_file+"/"+obj.getTxt_dok26();
                                    String urlPath = folderRoot+"/"+obj.getTxt_dok26();
                                    // System.out.println(pathMinio);
                                    Boolean checkFile = this.checkFile(urlPath);
                                    if(tdrPengajuanVendor.getId_pengajuan() != null){
                                        if(checkFile){
                                            boolean get = this.check_object(bucket, pathMinio);
                                            if(!get){
                                                Boolean upload = this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                if(upload){
                                                    TdrIdentitasVendorMinioNonSktEntity entityTdrIdentitas = new TdrIdentitasVendorMinioNonSktEntity();
                                                    entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                    entityTdrIdentitas.setDok(26);
                                                    entityTdrIdentitas.setIs_eproc(0);
                                                    entityTdrIdentitas.setNama_dok(nama_file);
                                                    entityTdrIdentitas.setPath_minio(pathMinio);
                                                    entityTdrIdentitas.setStatus_skt(0);
                                                    entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                    TdrIdentitasVendorMinioNonSktEntity.persist(entityTdrIdentitas);
            
                                                    LOG.info("data berhasil di upload");
            
                                                }
                                            }else{
                                                this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                TdrIdentitasVendorMinioNonSktEntity entityTdrIdentitas = new TdrIdentitasVendorMinioNonSktEntity();
                                                entityTdrIdentitas.setId_pengajuan(tdrPengajuanVendor.getId_pengajuan());
                                                entityTdrIdentitas.setDok(26);
                                                entityTdrIdentitas.setIs_eproc(0);
                                                entityTdrIdentitas.setNama_dok(nama_file);
                                                entityTdrIdentitas.setPath_minio(pathMinio);
                                                entityTdrIdentitas.setStatus_skt(0);
                                                entityTdrIdentitas.setDate_upload(LocalDateTime.now());
                                                TdrIdentitasVendorMinioNonSktEntity.persist(entityTdrIdentitas);
        
                                                LOG.info("data berhasil di upload");
                                            }
                                        }else{
                                            LOG.info("File not exist "+obj.getTxt_dok26());
                                        }
                                    }else{
                                        LOG.info("Id Pengajuan Not Exists");
                                    }
                                }
        
                                try {
                                    List<TdrPerbankanVendorNonSktEntity> tdrPerbankanEntity = TdrPerbankanVendorNonSktEntity.find("where id_identitas = :id_identitas", Parameters.with("id_identitas", vendorNonSkt.getId_identitas())).list();
                                    List<TdrPerbankanVendorMinioNonSktEntity> tdrPerbankanNonSktList = TdrPerbankanVendorMinioNonSktEntity.find("id_identitas = ?1", vendorNonSkt.getId_identitas()).list();
                                    System.out.println("minio non skt = "+tdrPerbankanNonSktList.size());
                                    if(tdrPerbankanNonSktList.size() > 0){
                                        Long dPerbankan = TdrPerbankanVendorMinioNonSktEntity.delete("where id_identitas = :id", Parameters.with("id", vendorNonSkt.getId_identitas()));
                                    }
                                    
                                    // System.out.println("delete perbankan = "+dPerbankan);
                                    for (TdrPerbankanVendorNonSktEntity p : tdrPerbankanEntity) {
                                        String nama_file = p.getKoran_path();
                                        String urlPath = folderRoot+"/"+nama_file;
                                        String pathMinio = tdrPengajuanVendor.getId_pengajuan()+"/Perbankan/"+nama_file;
                                        Boolean checkFile = this.checkFile(urlPath);
                                        
                                        if(tdrPengajuanVendor.getId_pengajuan() != null){
                                            if(checkFile){
                                                boolean get = this.check_object(bucket, pathMinio);
                                                if(!get){
                                                    Boolean upload = this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                    if(upload){
                                                        
                                                        try {
                                                            TdrPerbankanVendorMinioNonSktEntity TPBM = new TdrPerbankanVendorMinioNonSktEntity();
                                                            TPBM.setId_identitas(p.getId_identitas());
                                                            TPBM.setHave_account_bri(p.getHave_account_bri());
                                                            TPBM.setLokasi_bank(p.getLokasi_bank());
                                                            TPBM.setKode_bank(p.getKode_bank());
                                                            TPBM.setNama_bank(p.getNama_bank());
                                                            TPBM.setCity(p.getCity());
                                                            TPBM.setBank_key(p.getBank_key());
                                                            TPBM.setNegara(p.getNegara());
                                                            TPBM.setKode_negara(p.getKode_negara());
                                                            TPBM.setNo_rek(p.getNo_rek());
                                                            TPBM.setNama_rek(p.getNama_rek());
                                                            TPBM.setSwift_kode(p.getSwift_kode());
                                                            TPBM.setCabang_bank(p.getCabang_bank());
                                                            TPBM.setKoran_path(p.getKoran_path());
                                                            TPBM.setMinio_path(pathMinio);
                                                            TPBM.setUpload_minio(LocalDateTime.now());
                                                            TdrPerbankanVendorMinioNonSktEntity.persist(TPBM);
                                                            LOG.info("data Perbankan berhasil di upload");
                                                        } catch (Exception e) {
                                                            // System.err.println(e);
                                                            LOG.error("Perbankan data save minio "+e);
                                                            // TODO: handle exception
                                                        }
                    
                                                    }
                                                }else{
                                                    try {
                                                        this.uploadLegalDoc(bucket, pathMinio, urlPath);
                                                        TdrPerbankanVendorMinioNonSktEntity TPBM = new TdrPerbankanVendorMinioNonSktEntity();
                                                        TPBM.setId_identitas(p.getId_identitas());
                                                        TPBM.setHave_account_bri(p.getHave_account_bri());
                                                        TPBM.setLokasi_bank(p.getLokasi_bank());
                                                        TPBM.setKode_bank(p.getKode_bank());
                                                        TPBM.setNama_bank(p.getNama_bank());
                                                        TPBM.setCity(p.getCity());
                                                        TPBM.setBank_key(p.getBank_key());
                                                        TPBM.setNegara(p.getNegara());
                                                        TPBM.setKode_negara(p.getKode_negara());
                                                        TPBM.setNo_rek(p.getNo_rek());
                                                        TPBM.setNama_rek(p.getNama_rek());
                                                        TPBM.setSwift_kode(p.getSwift_kode());
                                                        TPBM.setCabang_bank(p.getCabang_bank());
                                                        TPBM.setKoran_path(p.getKoran_path());
                                                        TPBM.setMinio_path(pathMinio);
                                                        TPBM.setUpload_minio(LocalDateTime.now());
                                                        TdrPerbankanVendorMinioNonSktEntity.persist(TPBM);
                                                        LOG.info("data Perbankan berhasil di upload");
                                                    } catch (Exception e) {
                                                        // System.err.println(e);
                                                        LOG.error("Perbankan data save minio "+e);
                                                        // TODO: handle exception
                                                    }
                                                }
                                            }
                                        }
                                    }
                
                                } catch (Exception e) {
                
                                    // try {
                                    //     Thread.sleep(1000);
                                    // } catch (Exception k) {
                                    //     LOG.error("perbankan k = "+k);
                                    //     // TODO: handle exception
                                    // }
                                    LOG.error("perbankan non skt = "+e.getMessage());
                                    // TODO: handle exception
                                }
        
        
                            }
                            
                            if(tdrPengajuanVendor != null && tdrPengajuanVendor.getId_pengajuan() != null){
                                try {
                                    List<TdrPerbankanVendorNonSktEntity> tdrPerbankanNonSktListA = TdrPerbankanVendorNonSktEntity.find("id_identitas = ?1", vendorNonSkt.getId_identitas()).list();
                                    List<TdrIdentitasVendorMinioNonSktEntity> tdrIdentitasVendorMinioNonSktListB = TdrIdentitasVendorMinioNonSktEntity.find("id_pengajuan = :id_pengajuan", Parameters.with("id_pengajuan", tdrPengajuanVendor.getId_pengajuan())).list();
                                    if(tdrPerbankanNonSktListA.size() > 0 && tdrIdentitasVendorMinioNonSktListB.size() > 0){
                                        TdrIdentitasVendorNonSktEntity tdrVendorNonSktUpdate = TdrIdentitasVendorNonSktEntity.find("id_identitas = ?1", vendorNonSkt.getId_identitas()).firstResult();
                                        tdrVendorNonSktUpdate.setIs_minio_identitas_vendor(1);
                                        tdrVendorNonSktUpdate.persist();
                                        LOG.info("Upload File Non SKT "+vendorNonSkt.getId_identitas()+" Done");
                                    }
                                    
                                } catch (Exception e) {
                                    LOG.info("id identitas "+vendorNonSkt.getId_identitas()+" message ="+e.getMessage());
                                }
                            }

                            
        
                            } catch (Exception e) {
                                LOG.info(e);
                                // TODO: handle exception
                            }
                            
        
                        }else{
                            LOG.info("Dokumen Legalitas NULL");
                        }
    
    
                    } catch (Exception e) {
                        LOG.info("id_identitas = "+vendorNonSkt.getId_identitas()+" error = "+e);
                        // TODO: handle exception
                    }
    
                    
                   
                }




            }

            

        } catch (Exception e) {
            // TODO: handle exception
            LOG.error("Identitas Vendor Non SKT update "+e.getMessage());
        }
       

        LOG.info("Upload Non SKT finished");

        // return "";
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
        // try {
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
        // } catch (Exception e) {
        //     LOG.info(e);
        //     return false;
        //     // TODO: handle exception
        // }
        

        
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

    

   


    

}
