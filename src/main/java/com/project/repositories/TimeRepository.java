package com.project.repositories;

import com.project.models.Time;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimeRepository extends JpaRepository<Time, Long> {
    List<Time> findAllByIdNotIn(List<Long> ids);
}
