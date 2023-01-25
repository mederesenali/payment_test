package com.meder.security.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AccountAudit {
    @Id
    @SequenceGenerator(
            name = "audit_sequence",
            sequenceName = "audit_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "audit_sequence"
    )
    private Integer auditId;
    @CreatedDate
    private LocalDateTime paymentDate;
    private Double currentBalance;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "account_id", referencedColumnName = "accountId")
    private Account account;
}
