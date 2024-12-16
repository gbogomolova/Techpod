package com.example.techpod;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

@Service
public class TechpodService {

    private final TechpodRepository techpodRepository;

    @Autowired
    public TechpodService(TechpodRepository techpodRepository) {
        this.techpodRepository = techpodRepository;
    }

    // Забор данных и возможность поиска по ключевому слову
    public List<Trouble> listAll(String keyword) {
        System.out.println("Поиск с параметром: " + keyword);
        if (keyword != null && !keyword.isEmpty()) {
            return techpodRepository.search(keyword);
        }
        return techpodRepository.findAll();
    }

    // Сохранение проблемы
    public void save(Trouble trouble) {
        this.techpodRepository.save(trouble);
    }

    // Получение проблемы по ID
    public Trouble get(Long id) {
        return techpodRepository.findById(id).orElse(null);
    }

    // Удаление проблемы по ID
    public void delete(Long id) {
        techpodRepository.deleteById(id);
    }

    // Сортировка проблем по указанной колонке и направлению для гистограммы
    public List<Trouble> sortByColumn(String column, String direction) {
        Sort sort = direction.equalsIgnoreCase("asc")
                ? Sort.by(Sort.Direction.ASC, column)
                : Sort.by(Sort.Direction.DESC, column);

        return techpodRepository.findAll(sort);
    }

    // Получение статистики по датам регистрации
    public Map<String, Integer> getStatistics() {
        List<Trouble> troubles = techpodRepository.findAll();
        Map<String, Integer> statistics = new HashMap<>();
        for (Trouble trouble : troubles) {
            String date = trouble.getDateOfRegistration().toString();
            statistics.put(date, statistics.getOrDefault(date, 0) + 1);
        }
        return statistics;
    }
}
