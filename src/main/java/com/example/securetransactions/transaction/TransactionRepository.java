package com.example.securetransactions.transaction;

import com.example.securetransactions.user.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByOwner(AppUser owner);

    @Query("select t from Transaction t where t.id = :id and t.owner = :owner")
    Optional<Transaction> findByIdAndOwner(@Param("id") Long id, @Param("owner") AppUser owner);
}
