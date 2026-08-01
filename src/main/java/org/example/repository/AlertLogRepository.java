package org.example.repository;

import org.example.entity.AlertLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertLogRepository extends JpaRepository<AlertLog, Long> {
    boolean existsBySlug(String slug);
}