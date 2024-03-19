package org.umbrella.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "transactions", indexes = {
        @Index(name = "from_account_index", columnList = "fromAccount"),
        @Index(name = "date_index", columnList = "date")
})
public class TransactionsEntity {
    @Id
    @Column(nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String fromAccount;
    private String toAccount;
    private Date date;
    private BigDecimal amount;
    private String status;

    @ManyToOne
    @JoinColumn(name = "entrepreneur_id")
    private EntrepreneurEntity entrepreneur;

    //TODO: Add more fields
}
