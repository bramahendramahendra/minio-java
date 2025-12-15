package org.acme.controllers.upload_minio.repository;

import org.acme.controllers.upload_minio.entity.TdrPemegangSahamVendorEntity;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TdrPemegangSahamVendorRepository implements PanacheRepository<TdrPemegangSahamVendorEntity> {
    
}
