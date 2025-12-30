package org.acme.controllers.upload_minio;




import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
// import java.util.List;
// import java.util.Map;
// import java.util.Collection;
// import java.util.concurrent.CompletableFuture;
import java.io.File;
import java.io.FileOutputStream;
// import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.concurrent.CompletableFuture;

import org.acme.controllers.upload_minio.dto.CheckDirectoryDto;
import org.acme.controllers.upload_minio.dto.DeleteUploadDto;
// import org.acme.controllers.upload_minio.dto.DokLegalDto;
import org.acme.controllers.upload_minio.dto.FileUploadDto;
import org.acme.controllers.upload_minio.dto.GetObjectPerbankanDto;
import org.acme.controllers.upload_minio.repository.TdrIdentitasVendorSktRepository;
import org.acme.controllers.upload_minio.repository.TdrMstFileLegalitasRepository;
import org.acme.utils.handlerResponse;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.openapi.annotations.servers.Server;
import org.eclipse.microprofile.openapi.annotations.servers.Servers;
import org.jboss.resteasy.reactive.MultipartForm;
// import org.jboss.resteasy.reactive.RestResponse.Status;
// import org.jboss.resteasy.reactive.server.multipart.FileItem;
// import org.jboss.resteasy.reactive.server.multipart.FormValue;
// import org.jboss.resteasy.reactive.server.multipart.MultipartFormDataInput;

// import com.fasterxml.jackson.databind.ObjectMapper;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.GetObjectResponse;
import io.minio.MakeBucketArgs;
import io.minio.MinioAsyncClient;
import io.minio.ObjectWriteResponse;
import io.minio.RemoveObjectArgs;
import io.minio.UploadObjectArgs;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
// import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;





@Path("/upload")
public class Upload {

    @Inject MinioAsyncClient MinioClient;


    @Inject TdrIdentitasVendorSktRepository tdrIdentitas;

    @Inject TdrMstFileLegalitasRepository tdrMstLegalitasDok;

    @PersistenceContext
    EntityManager em;

    
    // private final ObjectMapper objectMapper = new ObjectMapper();
    private static final String UPLOAD_DIR = "tmp";
    private static final String pathMinioProfile = "Profile";

    

    @POST
    @RolesAllowed("tdr_online")
    @Path("upload_perbankan")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadPerbankan(@MultipartForm @Valid FileUploadDto file){
            String bucket = ConfigProvider.getConfig().getValue("bucket.minio", String.class);
            java.nio.file.Path pathDirTemp = java.nio.file.Paths.get(UPLOAD_DIR);
            String pathM = pathMinioProfile+"/"+file.getIdVendor()+"/"+file.getFileName();
            // System.out.println(Files.exists(pathDirTemp));
            try {
                boolean directory = Files.exists(pathDirTemp);
                if(!directory){
                    Files.createDirectories(pathDirTemp);
                }
                InputStream inputStream = file.getFileData();
                // System.out.println(inputStream);
                if(inputStream == null){
                    // new Exception("U");
                    return Response.ok().status(400).entity(new handlerResponse("file_upload null", "01", null)).build();
                }
                // java.nio.file.Path targetPath = Files.createTempFile("uploaded-", "-" + file.getFileName());
                
                File files = new File(UPLOAD_DIR+"/"+file.getFileName());
                // Save the uploaded file
                try (FileOutputStream out = new FileOutputStream(files)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                }

                if(files.exists()){
                    boolean a = this.checkBucket(bucket);
                    if(!a){
                        this.createBucket(bucket);
                    }
                    boolean get = this.check_object(bucket, pathM);
                    // System.out.println(get);
                    if(!get){
                        ObjectWriteResponse result_upload = this.push_upload(bucket, pathM, UPLOAD_DIR+"/"+file.getFileName());
                        if(result_upload == null){
                            return Response.ok().status(500).entity(new handlerResponse("error upload", "01", result_upload)).build();
                        }
                    }

                }
                
            } catch (Exception e) {
                // TODO: handle exception
                return Response.status(500).entity(e).build();
            }
            java.nio.file.Path deletePath = java.nio.file.Paths.get(UPLOAD_DIR+"/"+file.getFileName());
            try{
                Files.delete(deletePath);
            }catch(Exception e){
                System.out.println(e);
            }
            
            return Response.ok().status(200).entity(new handlerResponse(pathM, "00", null)).build();
    }


    @POST
    @RolesAllowed("tdr_online")
    @Path("delete_upload_perbankan")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUploadPerbankan(DeleteUploadDto delete){
        String bucket = ConfigProvider.getConfig().getValue("bucket.minio", String.class);
        try {
            remove_object(bucket, delete.getUrl_minio());
        } catch (Exception e) {
            return Response.ok().status(500).entity(e).build();
        } 
        return Response.ok().status(200).entity(new handlerResponse("", "00", null)).build();
    }


    @POST
    @Path("check_directory")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkDirectory(CheckDirectoryDto chekDto){
        java.nio.file.Path path = java.nio.file.Paths.get(chekDto.getUrlPath());
        boolean directory = Files.isDirectory(path);
        File f = new File(chekDto.getUrlPath()+"/"+chekDto.getNameFile());
        // boolean files = Files.exists()
        return Response.ok().status(200).entity(new handlerResponse("directory = "+directory+" files = "+f.exists(), "00", null)).build();
    }

    @POST
    @RolesAllowed("tdr_online")
    @Path("get-data-perbankan-minio")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getObjectPerbankan(@Valid GetObjectPerbankanDto getObjec){
        String bucket = ConfigProvider.getConfig().getValue("bucket.minio", String.class);
        // System.out.println(getObjec.getUrl_minio());
       try {
            CompletableFuture<GetObjectResponse> get = MinioClient.getObject(GetObjectArgs.builder().bucket(bucket).object(getObjec.getUrl_minio()).build());
        
            byte[] stream = get.join().readAllBytes();
            InputStream is = new ByteArrayInputStream(stream);
            // OutputStream os = new ByteArrayOutputStream(0);


            return Response.ok(is).type(MediaType.APPLICATION_OCTET_STREAM).build();
       } catch (Exception e) {
        // TODO: handle exception
            System.out.println(e.getLocalizedMessage());
            return Response.status(500).type(MediaType.APPLICATION_JSON).entity(new handlerResponse("cant get object", "01", null)).build();
       }
            // remove_object(bucket, getObjec.getUrl_minio());
            // InputStream render = MinioClient.getObject(GetObjectArgs.builder().bucket(bucket).object(getObjec.getUrl_minio()).build())
        

        
        // return Response.ok().status(200).entity(new handlerResponse("", "00", null)).build();
    }
    



    public boolean checkBucket(String Bucket){ 
        try {
            return MinioClient.bucketExists(BucketExistsArgs.builder().bucket(Bucket).build()).get();
        }catch(Exception e){
            e.printStackTrace();
            return false;
        } 
        
    }

    public void createBucket(String Bucket){
        try {
            MinioClient.makeBucket(MakeBucketArgs.builder().bucket(Bucket).build()).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObjectWriteResponse push_upload(String bucket, String object, String pathUrl){
        try {
           return MinioClient.uploadObject(UploadObjectArgs.builder().bucket(bucket).object(object).filename(pathUrl).build()).get();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
            // TODO: handle exception
        }
    }

    public boolean check_object(String bucket, String object){
        try {
            MinioClient.getObject(GetObjectArgs.builder().bucket(bucket).object(object).build()).get();
            
            return true;
        } catch (Exception f) {
            // TODO: handle exception
            System.out.println(f);
            return false;
        }
    }

    // public
    public void remove_object(String bucket, String object){
        try {
            MinioClient.removeObject(RemoveObjectArgs.builder().bucket(bucket).object(object).build());
        } catch (Exception e) {
            System.out.println(e);
            // TODO: handle exception
        }
    }

}
