package org.acme.controllers.upload_minio.repository;

import org.acme.controllers.upload_minio.entity.TdrPerbankanVendorMinioSktEntity;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TdrPerbankanVendorMinioSktRepository implements PanacheRepository<TdrPerbankanVendorMinioSktEntity> {
     
}
