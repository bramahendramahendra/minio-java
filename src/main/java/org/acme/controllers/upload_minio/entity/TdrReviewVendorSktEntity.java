package org.acme.controllers.upload_minio.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tdr_review_vendor")
public class TdrReviewVendorSktEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_review")
    private Long id_review;

    @Column(name = "id_identitas")
    private Long id_identitas;

    @Column(name = "total_project")
    private String total_project;

    @Column(name = "total_revenue")
    private String total_revenue;

    @Column(name = "ontime_rate")
    private String ontime_rate;

    @Column(name = "average_rating")
    private String average_rating;

    public Long getId_review() {
        return id_review;
    }

    public void setId_review(Long id_review) {
        this.id_review = id_review;
    }

    public Long getId_identitas() {
        return id_identitas;
    }

    public void setId_identitas(Long id_identitas) {
        this.id_identitas = id_identitas;
    }

    public String getTotal_project() {
        return total_project;
    }

    public void setTotal_project(String total_project) {
        this.total_project = total_project;
    }

    public String getTotal_revenue() {
        return total_revenue;
    }

    public void setTotal_revenue(String total_revenue) {
        this.total_revenue = total_revenue;
    }

    public String getOntime_rate() {
        return ontime_rate;
    }

    public void setOntime_rate(String ontime_rate) {
        this.ontime_rate = ontime_rate;
    }

    public String getAverage_rating() {
        return average_rating;
    }

    public void setAverage_rating(String average_rating) {
        this.average_rating = average_rating;
    }

    
}
