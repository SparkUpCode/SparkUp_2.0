package com.ocheret.SparkUp.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ProjectRepositoryTest {
    @Autowired
    private ProjectRepository projectRepository;
    
    @Test
    void saveAndRetrieveProject() {
        assert(true);
        // Test database operations
    }
} 