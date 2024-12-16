package com.example.techpod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/troubles")
public class TechpodApiController {

    @Autowired
    private TechpodService service;

    // Выгрузка всех проблем + поиск по параметру
    @GetMapping
    public ResponseEntity<List<Trouble>> getAllTroubles(@RequestParam(value = "search", required = false) String search) {
        System.out.println("Контроллер получил параметр search: " + search); // Лог для проверки
        List<Trouble> troubles = service.listAll(search);
        return ResponseEntity.ok(troubles);
    }

    // Поиск проблемы по ID
    @GetMapping("/{id}")
    public ResponseEntity<Trouble> getTroubleById(@PathVariable Long id) {
        Trouble trouble = service.get(id);
        if (trouble == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(trouble);
        }
    }

    // Создание новой проблемы
    @PostMapping
    public ResponseEntity<Trouble> createTrouble(@RequestBody Trouble trouble) {
        service.save(trouble);
        return ResponseEntity.status(HttpStatus.CREATED).body(trouble);
    }

    // Редактирование проблемы
    @PutMapping("/{id}")
    public ResponseEntity<Trouble> updateTrouble(@PathVariable Long id, @RequestBody Trouble troubleDetails) {
        Trouble trouble = service.get(id);
        if (trouble == null) {
            return ResponseEntity.notFound().build();
        }
        trouble.setInitiator(troubleDetails.getInitiator());
        trouble.setExecutor(troubleDetails.getExecutor());
        trouble.setDateOfRegistration(troubleDetails.getDateOfRegistration());
        trouble.setSystemName(troubleDetails.getSystemName());
        trouble.setPriority(troubleDetails.getPriority());
        service.save(trouble);
        return ResponseEntity.ok(trouble);
    }

    // Удаление проблемы
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrouble(@PathVariable Long id) {
        Trouble trouble = service.get(id);
        if (trouble == null) {
            return ResponseEntity.notFound().build();
        }
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Сортировка
    @GetMapping("/sort")
    public ResponseEntity<List<Trouble>> getSortedTroubles(
            @RequestParam(value = "sort") String sortColumn,
            @RequestParam(value = "direction") String sortDirection) {

        System.out.println("Сортировка: column=" + sortColumn + ", direction=" + sortDirection);

        List<Trouble> troubles = service.sortByColumn(sortColumn, sortDirection);
        return ResponseEntity.ok(troubles);
    }

    // Гистограмма
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Integer>> getStatistics() {

        Map<String, Integer> statistics = service.getStatistics();
        return ResponseEntity.ok(statistics);
    }


}
