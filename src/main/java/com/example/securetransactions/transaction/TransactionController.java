package com.example.securetransactions.transaction;

import com.example.securetransactions.security.CurrentUser;
import com.example.securetransactions.user.AppUser;
import com.example.securetransactions.util.HtmlSanitizer;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService service;
    private final CurrentUser currentUser;

    public TransactionController(TransactionService service, CurrentUser currentUser) {
        this.service = service;
        this.currentUser = currentUser;
    }

    @PostMapping
    public ResponseEntity<TransactionResponse> create(@Valid @RequestBody TransactionCreateRequest req) {
        AppUser me = currentUser.get();
        Transaction tx = new Transaction(me, req.getAmount(), HtmlSanitizer.sanitize(req.getDescription()));
        Transaction saved = service.create(tx);

        TransactionResponse response = toResponse(saved);
        return ResponseEntity
                .created(URI.create("/api/transactions/" + saved.getId()))
                .body(response);
    }

    @GetMapping("/mine")
    public List<TransactionResponse> mine() {
        return service.myTransactions()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/all")
    public List<TransactionResponse> all() {
        return service.allTransactions()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @PutMapping("/{id}")
    public TransactionResponse update(@PathVariable("id") Long id,
                                      @Valid @RequestBody TransactionUpdateRequest req) {
        Transaction update = new Transaction();
        update.setAmount(req.getAmount());
        update.setDescription(HtmlSanitizer.sanitize(req.getDescription()));
        Transaction saved = service.updateOwn(id, update);

        return toResponse(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        service.deleteOwn(id);
        return ResponseEntity.noContent().build();
    }

    // --- Mapper method ---
    private TransactionResponse toResponse(Transaction tx) {
        return new TransactionResponse(
                tx.getId(),
                tx.getAmount(),
                tx.getDescription()
        );
    }
}