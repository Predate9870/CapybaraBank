package app.src.br.com.FSS.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public record MoneyAudit(
    UUID id,
    Bankservice service,
    String description,
    OffsetDateTime createdAt
) {

    public char[] transactionID() {
        throw new UnsupportedOperationException("Unimplemented method 'transactionID'");
    } }
