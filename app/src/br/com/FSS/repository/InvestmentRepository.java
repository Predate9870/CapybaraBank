package app.src.br.com.FSS.repository;

import java.util.ArrayList;
import java.util.List;

import app.src.br.com.FSS.exception.InvestmentNotFoundException;
import app.src.br.com.FSS.exception.WalletNotFoundException;
import app.src.br.com.FSS.exception.AccountWithInvestiment;
import app.src.br.com.FSS.model.AccountWallet;
import app.src.br.com.FSS.model.Investimento;
import app.src.br.com.FSS.model.InvestmentWallet;

public class InvestmentRepository {

    private final List<Investimento> investments = new ArrayList<>();
    private final List<InvestmentWallet> wallets = new ArrayList<>();
    private long nextID = 0;

    public Investimento create(final long tax, final long initialFunds) {
        this.nextID++;
        var investimento = new Investimento(this.nextID, tax, initialFunds);
        investments.add(investimento);
        return investimento;
    }

    public InvestmentWallet initInvestment(final AccountWallet account, final long id) {
        var contasEmUso = wallets.stream()
                .map(InvestmentWallet::getConta)
                .toList();

        if (contasEmUso.contains(account)) {
            throw new AccountWithInvestiment("A conta " + account + " já possui um investimento.");
        }

        var investimento = findByID(id);
    var wallet = new InvestmentWallet(investimento, account, investimento.initialFunds());
        wallets.add(wallet);
        return wallet;
    }

    public InvestmentWallet deposito(final String pix, final long funds) {
        var wallet = findWalletByAccount(pix);
        wallet.addMoney(wallet.getConta().reduceMoney(funds), wallet.getService(), "investimento");
        return wallet;
    }

    public InvestmentWallet saque(final String pix, final long funds) {
        var wallet = findWalletByAccount(pix);
        wallet.getConta().addMoney(wallet.reduceMoney(funds), wallet.getService(), "Saque de investimentos");

        if (wallet.getFunds() == 0) {
            wallets.remove(wallet);
        }

        return wallet;
    }

    public void updateAmount() {
        wallets.forEach(w -> w.updateMoney(w.getInvestimento().tax()));
    }

    public Investimento findByID(final long id) {
        return investments.stream()
                .filter(a -> a.id() == id)
                .findFirst()
                .orElseThrow(() -> new InvestmentNotFoundException("O investimento " + id + " não foi encontrado."));
    }

    public InvestmentWallet findWalletByAccount(final String pix) {
        return wallets.stream()
                .filter(w -> w.getConta().getPix().contains(pix))
                .findFirst()
                .orElseThrow(() -> new WalletNotFoundException("A carteira não foi encontrada."));
    }

    public List<InvestmentWallet> listWallets() {
        return this.wallets;
    }

    public List<Investimento> list() {
        return this.investments;
    }

}
