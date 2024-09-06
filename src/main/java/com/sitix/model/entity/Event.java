package com.sitix.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.annotation.Nullable;
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

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name="event_category_id")
    private EventCategory category;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String linkMaps;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm", timezone = "Asia/Jakarta")
    @Column(nullable = false)
    private Date date;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    @Column(nullable = false)
    private List<TicketCategory> ticketCategories;

    @OneToOne
    private Image poster;

    @ManyToOne
    @JoinColumn(name = "creator_id")
    private Creator eventCreator;
    private Boolean isDone;

    public void checkAndSetIsDone() {
        if (this.date != null && new Date().after(this.date)) {
            this.isDone = Boolean.TRUE;
        }
    }

    @PrePersist
    @PreUpdate
    protected void onSaveOrUpdate() {
        if (isDone == null) {
            isDone = Boolean.FALSE;
        }
        checkAndSetIsDone();
    }
}

