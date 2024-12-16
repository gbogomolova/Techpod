package com.example.techpod;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.Generated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;


@Entity
public class Trouble {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Заявитель обязателен")
    private String initiator;

    @NotBlank(message = "Исполнитель обязателен")
    private String executor;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Дата регистрации обязательна")
    private LocalDate dateOfRegistration;

    @NotBlank(message = "Название системы обязательно")
    private String systemName;

    @NotNull(message = "Указание приоритета обязательно")
    @ManyToOne
    @JoinColumn(name = "priority_id", nullable = false) // Внешний ключ
    private Priority priority;

    protected Trouble() {
    }

    public String toString() {
        Long var10000 = this.id;
        return "Trouble [id=" + var10000 + ", initiator=" + this.initiator + ", executor=" + this.executor + ", dateOfRegistration=" + String.valueOf(this.dateOfRegistration) + ", systemName=" + this.systemName + ", priority=" + this.priority + "]";
    }

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public String getInitiator() {
        return this.initiator;
    }

    @Generated
    public String getExecutor() {
        return this.executor;
    }

    @Generated
    public LocalDate getDateOfRegistration() {
        return this.dateOfRegistration;
    }

    @Generated
    public String getSystemName() {
        return this.systemName;
    }

    @Generated
    public Priority getPriority() {
        return priority;
    }

    @Generated
    public void setId(final Long id) {
        this.id = id;
    }

    @Generated
    public void setInitiator(final String initiator) {
        this.initiator = initiator;
    }

    @Generated
    public void setExecutor(final String executor) {
        this.executor = executor;
    }

    @Generated
    public void setDateOfRegistration(final LocalDate dateOfRegistration) {
        this.dateOfRegistration = dateOfRegistration;
    }

    @Generated
    public void setSystemName(final String systemName) {
        this.systemName = systemName;
    }

    @Generated
    public void setPriority(Priority priority) {
        this.priority = priority;
    }
    }