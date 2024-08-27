package com.sitix.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "m_event_category")
public class EventCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String categoryName;
}
