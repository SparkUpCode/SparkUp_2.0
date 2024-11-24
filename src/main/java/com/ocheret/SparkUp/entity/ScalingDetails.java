package com.ocheret.SparkUp.entity;

import jakarta.persistence.*;

@Entity
public class ScalingDetails extends StageDetails {
    @Column(length = 5000, nullable = true)
    private String scalabilityAndGrowth;
    
    @Column(length = 5000, nullable = true)
    private String networkingAndPartnerships;

    public String getScalabilityAndGrowth() {
        return scalabilityAndGrowth;
    }

    public void setScalabilityAndGrowth(String scalabilityAndGrowth) {
        this.scalabilityAndGrowth = scalabilityAndGrowth;
    }

    public String getNetworkingAndPartnerships() {
        return networkingAndPartnerships;
    }

    public void setNetworkingAndPartnerships(String networkingAndPartnerships) {
        this.networkingAndPartnerships = networkingAndPartnerships;
    }

    // Getters and setters
} 