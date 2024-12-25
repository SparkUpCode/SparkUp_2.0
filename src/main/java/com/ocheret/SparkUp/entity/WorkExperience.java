package com.ocheret.SparkUp.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "work_experience")
public class WorkExperience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String jobTitle;
    private String company;
    
    @Column(name = "date_start")
    private LocalDate dateEmploymentStart;
    
    @Column(name = "date_finish")
    private LocalDate dateEmploymentFinish;
    
    private boolean presentEmployment;
    
    @Column(length = 1000)
    private String jobSummary;
    
    @ElementCollection
    @CollectionTable(
        name = "key_achievements",
        joinColumns = @JoinColumn(name = "work_experience_id")
    )
    @Column(name = "achievement", length = 1000)
    private List<String> keyAchievements = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "work_experience_id")
    private List<Recommendation> recommendations = new ArrayList<>();

    @Column(name = "visible")
    private Boolean visible = true;

    // Calculate employment length
    public String getEmploymentLength() {
        LocalDate endDate = presentEmployment ? LocalDate.now() : dateEmploymentFinish;
        if (endDate == null || dateEmploymentStart == null) {
            return "N/A";
        }
        
        Period period = Period.between(dateEmploymentStart, endDate);
        return String.format("%d years, %d months", period.getYears(), period.getMonths());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public LocalDate getDateEmploymentStart() {
        return dateEmploymentStart;
    }

    public void setDateEmploymentStart(LocalDate dateEmploymentStart) {
        this.dateEmploymentStart = dateEmploymentStart;
    }

    public LocalDate getDateEmploymentFinish() {
        return dateEmploymentFinish;
    }

    public void setDateEmploymentFinish(LocalDate dateEmploymentFinish) {
        this.dateEmploymentFinish = dateEmploymentFinish;
    }

    public boolean isPresentEmployment() {
        return presentEmployment;
    }

    public void setPresentEmployment(boolean presentEmployment) {
        this.presentEmployment = presentEmployment;
    }

    public String getJobSummary() {
        return jobSummary;
    }

    public void setJobSummary(String jobSummary) {
        this.jobSummary = jobSummary;
    }

    public List<String> getKeyAchievements() {
        return keyAchievements;
    }

    public void setKeyAchievements(List<String> keyAchievements) {
        this.keyAchievements = keyAchievements;
    }

    public List<Recommendation> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<Recommendation> recommendations) {
        this.recommendations = recommendations;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    // Getters and setters
    // ... standard getters and setters for all fields
} 