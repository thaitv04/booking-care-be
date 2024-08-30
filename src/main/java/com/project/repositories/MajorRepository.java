package com.project.repositories;

import com.project.models.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MajorRepository extends JpaRepository<Major, Long> {
    boolean existsByName(String name);

    @Query("SELECT m FROM Major m WHERE " +
            "(:name IS NULL OR m.name LIKE (CONCAT('%', :name, '%'))) AND " +
            "(:minDoctors IS NULL OR SIZE(m.userEntities) >= :minDoctors) AND " +
            "(:maxDoctors IS NULL OR SIZE(m.userEntities) <= :maxDoctors)")
    List<Major> getAllMajors(@Param("name") String name,
                             @Param("minDoctors") Integer minDoctors,
                             @Param("maxDoctors") Integer maxDoctors);

}
