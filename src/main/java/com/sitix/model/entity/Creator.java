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
@Table(name = "m_creator")
public class Creator {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String name;
    private String introduction;
    private String phone;

    @OneToOne
    @JoinColumn(name = "image_id", unique = true)
    private Image profilePicture;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "user_id")
    private User user;

    private Boolean isDeleted;

    @PrePersist
    protected void onCreate() {
        isDeleted = Boolean.FALSE;
    }
}
