package com.sitix.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "m_event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;

    @ManyToOne
    @JoinColumn(name="event_category_id")
    private EventCategory category;

    private String description;
    private String city;
    private String address;
    private String linkMaps;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm", timezone = "Asia/Jakarta")
    private Date date;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TicketCategory> ticketCategories;

    @OneToOne
    private Image poster;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private Creator eventCreator;
    private Boolean isDone;

    @PrePersist
    protected void onCreate() {
        isDone = Boolean.FALSE;
    }

}
