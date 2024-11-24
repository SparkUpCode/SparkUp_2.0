package com.ocheret.SparkUp.entity;

import jakarta.persistence.*;

@Entity
public class SetupDetails extends StageDetails {
    @Column(length = 5000, nullable = true)
    private String legalAndCompliance;
    
    @Column(length = 5000, nullable = true)
    private String funding;
    
    @Column(length = 5000, nullable = true)
    private String teamBuilding;
    
    @Column(length = 5000, nullable = true)
    private String productDevelopment;

    public String getLegalAndCompliance() {
        return legalAndCompliance;
    }

    public void setLegalAndCompliance(String legalAndCompliance) {
        this.legalAndCompliance = legalAndCompliance;
    }

    public String getFunding() {
        return funding;
    }

    public void setFunding(String funding) {
        this.funding = funding;
    }

    public String getTeamBuilding() {
        return teamBuilding;
    }

    public void setTeamBuilding(String teamBuilding) {
        this.teamBuilding = teamBuilding;
    }

    public String getProductDevelopment() {
        return productDevelopment;
    }

    public void setProductDevelopment(String productDevelopment) {
        this.productDevelopment = productDevelopment;
    }

    // Getters and setters
} 