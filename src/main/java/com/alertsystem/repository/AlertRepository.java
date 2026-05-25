package com.alertsystem.repository;

import com.alertsystem.model.Alert;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertRepository extends MongoRepository<Alert, String> {
	List<Alert> findByAccountId(String accountId);

	List<Alert> findBySeverity(String severity);

	List<Alert> findByResolvedFalse();
}
