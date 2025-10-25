package app.src.br.com.FSS.repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import app.src.br.com.FSS.exception.NotMoneyEnoughException;
import app.src.br.com.FSS.model.Money;
import app.src.br.com.FSS.model.MoneyAudit;
import app.src.br.com.FSS.model.Bankservice;
import app.src.br.com.FSS.model.carteira;

public class CommonsRepository {
    public static void checkFundsTransaction(final carteira source, final long amount) {
        if (source.getFunds() < amount) {
            throw new NotMoneyEnoughException("Você não possui saldo suficiente para realizar essa transação");
        }
    }

    public static List<Money> generateMoney(final UUID transactionID, final long funds, final String description) {
        var history = new MoneyAudit(transactionID, Bankservice.ACCOUNT, description, OffsetDateTime.now());
        return Stream.generate(() -> new Money(history)).limit(funds).toList();
    }
}
