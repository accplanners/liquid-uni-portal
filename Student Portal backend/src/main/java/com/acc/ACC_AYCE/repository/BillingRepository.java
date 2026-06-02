package com.acc.ACC_AYCE.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.acc.ACC_AYCE.Entity.Billing;
public interface BillingRepository extends JpaRepository<Billing, Long> {
}
