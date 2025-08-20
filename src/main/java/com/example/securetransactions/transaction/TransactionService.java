package com.example.securetransactions.transaction;

import com.example.securetransactions.security.CurrentUser;
import com.example.securetransactions.user.AppUser;
import com.example.securetransactions.user.Role;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TransactionService {

    private final TransactionRepository repo;
    private final CurrentUser currentUser;

    public TransactionService(TransactionRepository repo, CurrentUser currentUser) {
        this.repo = repo;
        this.currentUser = currentUser;
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Transaction create(Transaction tx) {
        return repo.save(tx);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<Transaction> myTransactions() {
        AppUser me = currentUser.get();
        return repo.findByOwner(me);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<Transaction> allTransactions() {
        return repo.findAll();
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public Transaction updateOwn(Long id, Transaction update) {
        AppUser me = currentUser.get();
        Transaction existing = repo.findByIdAndOwner(id, me)
                .orElseThrow(() -> new RuntimeException("Not found or not yours"));
        existing.setAmount(update.getAmount());
        existing.setDescription(update.getDescription());
        return repo.save(existing);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public void deleteOwn(Long id) {
        AppUser me = currentUser.get();
        Transaction existing = repo.findByIdAndOwner(id, me)
                .orElseThrow(() -> new RuntimeException("Not found or not yours"));
        repo.delete(existing);
    }
}
