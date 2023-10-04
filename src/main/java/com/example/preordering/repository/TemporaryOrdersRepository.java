package com.example.preordering.repository;

import com.example.preordering.entity.TemporaryOrdersTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TemporaryOrdersRepository extends JpaRepository<TemporaryOrdersTime, Long> {
}
