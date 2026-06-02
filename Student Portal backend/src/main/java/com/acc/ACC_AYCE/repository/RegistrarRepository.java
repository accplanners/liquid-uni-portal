package com.acc.ACC_AYCE.repository;

import com.acc.ACC_AYCE.Entity.Registrar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrarRepository extends JpaRepository<Registrar, Long> {
}
