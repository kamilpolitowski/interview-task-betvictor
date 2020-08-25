package com.betvictor.interviewtask.repository;

import com.betvictor.interviewtask.model.GameAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface GameActionRepository extends JpaRepository<GameAction, Integer> {

    List<GameAction> findByAccountId(Integer accountId);

    @Query("SELECT SUM(stake) FROM GameAction WHERE accountId = ?1 and actionTime >= ?2 GROUP BY accountId")
    Optional<BigDecimal> sumStakeForThresholdTime(Integer accountId, LocalDateTime thresholdTime);
}
