package com.ocheret.SparkUp.entity;

import com.ocheret.enums.DevelopmentStage;
import com.ocheret.enums.Industry;


import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 1000)
    private String title;
    @Column(length = 10000)
    private String description;
    @ElementCollection
    private List<String> pictures;
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Task> tasks = new ArrayList<>();
    @Column(length = 200)
    private String linkToProject;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    @JsonIgnoreProperties({"projects", "password", "roles"})
    private User owner;
    @Enumerated(EnumType.STRING)
    private Industry industry = Industry.UNDEFINED;
    @Enumerated(EnumType.STRING)
    private DevelopmentStage developmentStage = DevelopmentStage.IDEATION_AND_PLANNING;
    @OneToOne(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private IdeationDetails ideationDetails;
    @OneToOne(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private SetupDetails setupDetails;
    @OneToOne(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private LaunchDetails launchDetails;
    @OneToOne(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private ScalingDetails scalingDetails;

    public Project() {
        this.tasks = new ArrayList<>();
    }

    public String getLinkToProject() {
        return linkToProject;
    }

    public void setLinkToProject(String linkToProject) {
        this.linkToProject = linkToProject;
    }

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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void addTask(Task task) {
        tasks.add(task);
        task.setProject(this);
    }

    public Industry getIndustry() {
        return industry;
    }

    public void setIndustry(Industry industry) {
        this.industry = industry;
    }

    public DevelopmentStage getDevelopmentStage() {
        return developmentStage;
    }

    public void setDevelopmentStage(DevelopmentStage developmentStage) {
        this.developmentStage = developmentStage;
    }

    public StageDetails getCurrentStageDetails() {
        StageDetails details = switch (developmentStage) {
            case IDEATION_AND_PLANNING -> ideationDetails != null ? ideationDetails : new IdeationDetails();
            case SETUP_AND_DEVELOPMENT -> setupDetails != null ? setupDetails : new SetupDetails();
            case LAUNCH_AND_EARLY_GROWTH -> launchDetails != null ? launchDetails : new LaunchDetails();
            case SCALING_AND_EXPANSION -> scalingDetails != null ? scalingDetails : new ScalingDetails();
        };
        if (details.getProject() == null) {
            details.setProject(this);
        }
        return details;
    }

    public void updateStageDetails(StageDetails details) {
        if (details instanceof IdeationDetails) {
            this.ideationDetails = (IdeationDetails) details;
        } else if (details instanceof SetupDetails) {
            this.setupDetails = (SetupDetails) details;
        } else if (details instanceof LaunchDetails) {
            this.launchDetails = (LaunchDetails) details;
        } else if (details instanceof ScalingDetails) {
            this.scalingDetails = (ScalingDetails) details;
        }
        details.setProject(this);
    }
}


