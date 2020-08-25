package com.betvictor.interviewtask.repository;

import com.betvictor.interviewtask.model.NotificationMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationMessageRepository extends JpaRepository<NotificationMessage, Integer> {

}
