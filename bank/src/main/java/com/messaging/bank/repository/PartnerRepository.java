package com.messaging.bank.repository;

import com.messaging.bank.entities.PartnerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartnerRepository extends JpaRepository<PartnerEntity, Long> {
    boolean existsByAlias(String alias);

}
