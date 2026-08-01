package org.example.repository;

import org.example.entity.TrendlyneIpo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrendlyneIpoRepository extends JpaRepository<TrendlyneIpo, Long> {
    List<TrendlyneIpo> findTop50ByOrderByFetchedAtDesc();

    Optional<TrendlyneIpo> findFirstByIsinOrderByFetchedAtDesc(String isin);
}