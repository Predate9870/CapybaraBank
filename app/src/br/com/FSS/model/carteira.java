package app.src.br.com.FSS.model;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public abstract class carteira {

    protected final Bankservice service;
    protected final List<Money> money;

    public carteira(final Bankservice service) {
        this.service = service;
        this.money = new ArrayList<>();
    }

    protected List<Money> generateMoney(final long amount, final String description) {
        var history = new MoneyAudit(UUID.randomUUID(), service, description, OffsetDateTime.now());
        return Stream.generate(() -> new Money(history)).limit(amount).toList();
    }

    public long getFunds() {
        return money.size();
    }

    public void addMoney(final List<Money> money, final Bankservice service, final String description) {
        var history = new MoneyAudit(UUID.randomUUID(), service, description, OffsetDateTime.now());
        money.forEach(m -> m.addHistory(history));
        this.money.addAll(money);
    }

    public Bankservice getService() {
        return this.service;
    }

    public List<Money> reduceMoney(final long amount) {
        List<Money> toRemove = new ArrayList<>();

        for (int i = 0; i < amount; i++) {
            toRemove.add(this.money.remove(0));
        }
        return toRemove;
    }

    public List<MoneyAudit> getFinancialTransaction() {
        return money.stream().flatMap(m -> m.getHistory().stream()).toList();
    }

    @Override
    public String toString() {
        return "Carteira{" +
               "Serviço: " + service +
               ", Saldo: R$" + (money.size() / 100) +
               "}";
    }
}
