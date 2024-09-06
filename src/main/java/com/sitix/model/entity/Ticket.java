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
@Table(name = "m_ticket")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name="event_id")
    private Event event;

    @ManyToOne
    @JoinColumn(name="ticket_category_id")
    private TicketCategory ticketCategory;

    @ManyToOne
    @JoinColumn(name="transaction_id")
    private Transaction transaction;

    private Boolean isUsed;

    @PrePersist
    protected void onCreate() {
        isUsed = false;
    }

}

