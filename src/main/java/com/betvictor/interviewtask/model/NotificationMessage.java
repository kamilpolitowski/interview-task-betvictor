package com.betvictor.interviewtask.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Builder
@Entity
@Table(name = "NOTIFICATION_MESSAGE")
@AllArgsConstructor
@NoArgsConstructor
public class NotificationMessage implements Serializable {

    static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Integer id;
    private Integer accountId;
    private String message;

}
