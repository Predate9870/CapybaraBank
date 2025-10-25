package app.src.br.com.FSS.model;

import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.stream.Stream;

public class InvestmentWallet extends carteira {

    private final Investimento investimento;
    private final AccountWallet conta;

    public InvestmentWallet(final Investimento investimento, final AccountWallet conta, final long valor) {
        super(Bankservice.INVESTMENT);
        this.investimento = investimento;
        this.conta = conta;

        addMoney(conta.reduceMoney(valor), getService(), "investimento");
    }

    public void updateMoney(final long percent) {
        long rendimento = getFunds() * percent / 100;
        var history = new MoneyAudit(UUID.randomUUID(), getService(), "rendimentos", OffsetDateTime.now());
        var money = Stream.generate(() -> new Money(history)).limit(rendimento).toList();
        this.money.addAll(money);
    }

    public Investimento getInvestimento() {
        return investimento;
    }

    public AccountWallet getConta() {
        return conta;
    }

    @Override
    public String toString() {
        return "Carteira de investimento {" +
                "investimento=" + investimento +
                ", conta=" + conta +
                ", saldo=" + getFunds() +
                '}';
    }
}
