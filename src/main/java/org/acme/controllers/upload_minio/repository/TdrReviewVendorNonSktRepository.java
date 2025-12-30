package org.acme.controllers.upload_minio.repository;

import org.acme.controllers.upload_minio.entity.TdrReviewVendorNonSktEntity;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TdrReviewVendorNonSktRepository implements PanacheRepository<TdrReviewVendorNonSktEntity> {
    
}
