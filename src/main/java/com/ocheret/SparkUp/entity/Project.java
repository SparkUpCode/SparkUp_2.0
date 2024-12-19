package com.ocheret.SparkUp.entity;

import jakarta.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 10000)
    private String title;

    @Column(length = 10000)
    private String description;

    @Column(length = 10000)
    private String stage;

    @Column(length = 10000)
    private String industry;

    @Column(length = 10000)
    private String linkToProject;

   
    @Column(name = "creator_username", length = 10000)
    private String creatorUsername;

    @Column(length = 10000)
    private String problem;

    @Column(length = 10000)
    private String solution;

    @Column(length = 10000)
    private String prototype;

    @Column(length = 10000)
    private String idealCustomer;

    @Column(length = 10000)
    private String competitors;

    @ElementCollection
    private List<String> pictures;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks;

    @ElementCollection
    @MapKeyColumn(name = "key")
    @Column(name = "value", columnDefinition = "TEXT")
    private Map<String, String> stageData;

    
    @Column(length = 10000)
    private String goals;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getLinkToProject() {
        return linkToProject;
    }

    public void setLinkToProject(String linkToProject) {
        this.linkToProject = linkToProject;
    }

    public List<String> getPictures() {
        return pictures;
    }

    public void setPictures(List<String> pictures) {
        this.pictures = pictures;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public Map<String, String> getStageData() {
        return stageData;
    }

    public void setStageData(Map<String, String> stageData) {
        this.stageData = stageData;
    }

    public String getCreatorUsername() {
        return creatorUsername;
    }

    public void setCreatorUsername(String creatorUsername) {
        this.creatorUsername = creatorUsername;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }

    public String getPrototype() {
        return prototype;
    }

    public void setPrototype(String prototype) {
        this.prototype = prototype;
    }

    public String getIdealCustomer() {
        return idealCustomer;
    }

    public void setIdealCustomer(String idealCustomer) {
        this.idealCustomer = idealCustomer;
    }

    public String getCompetitors() {
        return competitors;
    }

    public void setCompetitors(String competitors) {
        this.competitors = competitors;
    }

    public String getGoals() {
        return goals;
    }

    public void setGoals(String goals) {
        this.goals = goals;
    }
}


