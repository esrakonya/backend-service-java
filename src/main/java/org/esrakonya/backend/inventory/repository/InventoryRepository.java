package org.esrakonya.backend.inventory.repository;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;
import org.esrakonya.backend.inventory.domain.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<InventoryEntity, Long> {
    boolean existsByProductId(Long productId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT i FROM InventoryEntity i WHERE i.productId = :productId")
    Optional<InventoryEntity> findByProductIdWithLock(@Param("productId") Long productId);

    Optional<InventoryEntity> findByProductId(Long productId);
}
