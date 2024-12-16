package com.example.techpod;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Sort;

public interface TechpodRepository extends JpaRepository<Trouble, Long> {

    // Для поиска по ключевому слову
    @Query("SELECT t FROM Trouble t WHERE CONCAT(t.initiator, ' ', t.executor, ' ', t.dateOfRegistration, ' ', t.systemName, ' ', t.priority) LIKE %:searchTerm%")
    List<Trouble> search(@Param("searchTerm") String searchTerm);

    // Сортировка по значениям
    List<Trouble> findAll(Sort sort);
}
