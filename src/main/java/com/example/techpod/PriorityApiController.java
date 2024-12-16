package com.example.techpod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/priorities")
public class PriorityApiController {

    @Autowired
    private PriorityRepository priorityRepository;

    // Забор информации о приоритетах
    @GetMapping
    public List<Priority> getAllPriorities() {
        return priorityRepository.findAll();
    }
}
