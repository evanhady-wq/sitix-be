package com.sitix.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "m_transaction")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private LocalDateTime transactionDate;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TransactionDetail> transactionDetails;

    private Status status;

    private LocalDate paidAt;

    public enum Status {
        PAID,
        UNPAID,
        CANCELLED
    }

    @PrePersist
    protected void onCreate() {
        if (status == null) {
            status = Status.UNPAID;
            transactionDate = LocalDateTime.now();        }
    }

    @PreUpdate
    protected void onUpdate() {
        if (status == Status.PAID) {
            paidAt = LocalDate.now();
        }
    }


}
