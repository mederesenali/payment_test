package com.meder.security.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

import static jakarta.persistence.GenerationType.SEQUENCE;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
    @Id
    @SequenceGenerator(
            name = "account_sequence",
            sequenceName = "account_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "account_sequence"
    )
    private Long accountId;
    @Column(scale = 2)
    private Double accountBalance;
    @OneToOne(mappedBy = "account")
    private User user;
    @OneToMany(mappedBy = "account")
    private List<AccountAudit> audit;


}
