package org.example.repository;

import org.example.entity.TrendlyneIpo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrendlyneIpoRepository extends JpaRepository<TrendlyneIpo, Long> {
    List<TrendlyneIpo> findTop50ByOrderByFetchedAtDesc();
}