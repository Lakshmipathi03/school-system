package com.example.school.repository;
import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.school.model.Fee;
import java.util.List;

public interface FeeRepository extends JpaRepository<Fee, Long> {

	@Query("SELECT SUM(f.amount) FROM Fee f WHERE f.rollNumber = :rollNumber")
	Double getTotalPaid(@Param("rollNumber") Integer rollNumber);
	List<Fee> findByPaymentDate(LocalDate paymentDate);
	Fee findByReceiptNumber(String receiptNumber);
	List<Fee> findByRollNumber(Integer rollNumber);
}