package com.ocheret.SparkUp.entity;

import jakarta.persistence.*;

@Entity
public class IdeationDetails extends StageDetails {
    @Column(length = 5000, nullable = true)
    private String ideaAndProblem;
    
    @Column(length = 5000, nullable = true)
    private String marketResearch;
    
    @Column(length = 5000, nullable = true)
    private String businessModel;

    public String getIdeaAndProblem() {
        return ideaAndProblem;
    }

    public void setIdeaAndProblem(String ideaAndProblem) {
        this.ideaAndProblem = ideaAndProblem;
    }

    public String getMarketResearch() {
        return marketResearch;
    }

    public void setMarketResearch(String marketResearch) {
        this.marketResearch = marketResearch;
    }

    public String getBusinessModel() {
        return businessModel;
    }

    public void setBusinessModel(String businessModel) {
        this.businessModel = businessModel;
    }

    // Getters and setters
} 