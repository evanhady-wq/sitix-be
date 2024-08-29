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
@Table(name = "m_ticket_category")
public class TicketCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name="event_id")
    private Event event;

    private String name;
    private Integer quota;
    private Double price;

    public boolean hasAvailableTickets(int quantity) {
        return quota >= quantity;
    }

    public void reduceQuota(int quantity) {
        if (quota >= quantity) {
            this.quota -= quantity;
        } else {
            throw new IllegalArgumentException("Not enough tickets available");
        }
    }

    public void increaseQuota(int quantity) {
        this.quota += quantity;
    }
}
