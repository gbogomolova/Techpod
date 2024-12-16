package com.example.techpod;

import jakarta.persistence.*;

// Сущность для модели данных "родитель-дочка"
@Entity
@Table(name = "priorities")
public class Priority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING) // Используем Enum для удобства
    private PriorityName name;

    public enum PriorityName {
        Низкий, Средний, Высокий, Критический
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PriorityName getName() {
        return name;
    }

    public void setName(PriorityName name) {
        this.name = name;
    }
}
