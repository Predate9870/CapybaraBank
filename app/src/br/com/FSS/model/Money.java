package app.src.br.com.FSS.model;

import java.util.ArrayList;
import java.util.List;

public class Money {

    private final List<MoneyAudit> history = new ArrayList<>();

    public Money(final MoneyAudit initial) {
        this.history.add(initial);
    }

    public void addHistory(final MoneyAudit entry) {
        this.history.add(entry);
    }

    public List<MoneyAudit> getHistory() {
        return this.history;
    }
}
