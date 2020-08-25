package com.betvictor.interviewtask.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@Entity
@Table(name = "GAME_ACTION")
@AllArgsConstructor
@NoArgsConstructor
public class GameAction implements Serializable {

    static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Integer id;
    private Integer accountId;
    private BigDecimal stake;
    private Integer gameId; // for future use
    private LocalDateTime actionTime;

    @PrePersist
    protected void onCreate() {
        if(actionTime == null)
            actionTime = LocalDateTime.now();
    }

}
