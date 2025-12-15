package org.acme.controllers.upload_minio.repository;

import org.acme.controllers.upload_minio.entity.TdrPengalamanVendorMinioEntity;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TdrPengalamanVendorMinioRepository implements PanacheRepository<TdrPengalamanVendorMinioEntity> {
    
}
