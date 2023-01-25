package com.meder.security.repository;

import com.meder.security.model.AccountAudit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountAuditRepository extends JpaRepository<AccountAudit,Long> {
}
