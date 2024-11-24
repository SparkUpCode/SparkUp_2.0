package com.ocheret.SparkUp.controller;

import com.ocheret.SparkUp.entity.*;
import com.ocheret.SparkUp.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects/{projectId}/stage-details")
public class StageDetailsController {
    
    @Autowired
    private ProjectService projectService;
    
    @GetMapping
    public ResponseEntity<StageDetails> getStageDetails(@PathVariable Long projectId) {
        return ResponseEntity.ok(projectService.getStageDetails(projectId));
    }
    
    @PutMapping
    public ResponseEntity<StageDetails> updateStageDetails(
            @PathVariable Long projectId,
            @RequestBody StageDetails details) {
        return ResponseEntity.ok(projectService.updateStageDetails(projectId, details));
    }
} 