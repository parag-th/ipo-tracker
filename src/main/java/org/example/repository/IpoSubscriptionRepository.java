package org.example.repository;

import org.example.entity.IpoSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IpoSubscriptionRepository extends JpaRepository<IpoSubscription, Long> {
    List<IpoSubscription> findBySlugOrderByFetchedAtDesc(String slug);
    List<IpoSubscription> findTop50ByOrderByFetchedAtDesc();
}