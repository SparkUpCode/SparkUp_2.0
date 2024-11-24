package com.ocheret.SparkUp.entity;

import jakarta.persistence.*;

@Entity
public class LaunchDetails extends StageDetails {
    @Column(length = 5000, nullable = true)
    private String goToMarketStrategy;
    
    @Column(length = 5000, nullable = true)
    private String customerAcquisition;
    
    @Column(length = 5000, nullable = true)
    private String metricsAndPerformance;

    public String getGoToMarketStrategy() {
        return goToMarketStrategy;
    }

    public void setGoToMarketStrategy(String goToMarketStrategy) {
        this.goToMarketStrategy = goToMarketStrategy;
    }

    public String getCustomerAcquisition() {
        return customerAcquisition;
    }

    public void setCustomerAcquisition(String customerAcquisition) {
        this.customerAcquisition = customerAcquisition;
    }

    public String getMetricsAndPerformance() {
        return metricsAndPerformance;
    }

    public void setMetricsAndPerformance(String metricsAndPerformance) {
        this.metricsAndPerformance = metricsAndPerformance;
    }

    // Getters and setters
} 