package org.umbrella.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "transactions")
public class TransactionsEntity {
    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String fromAccount;
    private LocalDate date;
    private BigDecimal amount;
    private String status;

    @ManyToOne
    @JoinColumn(name = "entrepreneur_id")
    private EntrepreneurEntity entrepreneur;

    //TODO: Add more fields
}
