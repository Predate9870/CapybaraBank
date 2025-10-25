package app.src.br.com.FSS.repository;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import app.src.br.com.FSS.exception.AccountNotFoundExcpetion;
import app.src.br.com.FSS.model.AccountWallet;
import app.src.br.com.FSS.model.MoneyAudit;

public class AccountRepository {

    private final List<AccountWallet> accounts = new ArrayList<>();

    public AccountWallet create(final List<String> pix, final long amount) {
        var wallet = new AccountWallet(amount, pix);
        accounts.add(wallet);
        return wallet;
    }

    public void deposito(final String pix, final long amount) {
        var wallet = findWalletByPix(pix);
        wallet.addMoney(amount, "depósitado");
    }

    public void saque(final String pix, final long amount) {
        var wallet = findWalletByPix(pix);
        CommonsRepository.checkFundsTransaction(wallet, amount);
        wallet.reduceMoney(amount); 
    }

    public void transferencia(final String sourcePix, final String targetPix, final long amount) {
        var source = findWalletByPix(sourcePix);
        var target = findWalletByPix(targetPix);
        CommonsRepository.checkFundsTransaction(source, amount);
        var removed = source.reduceMoney(amount);
        target.addMoney(removed, target.getService(), "transferência recebida");
    }

    public AccountWallet findByPix(final String pix) {
        return findWalletByPix(pix);
    }

    private AccountWallet findWalletByPix(final String pix) {
        return accounts.stream()
                .filter(w -> w.getPix().contains(pix))
                .findFirst()
                .orElseThrow(() -> new AccountNotFoundExcpetion("Conta com PIX " + pix + " não encontrada."));
    }

    public Map<OffsetDateTime, List<MoneyAudit>> getHistory(final String pix) {
        var wallet = findWalletByPix(pix);
        var audits = wallet.getFinancialTransaction();
        return audits.stream().collect(Collectors.groupingBy(MoneyAudit::createdAt));
    }

    public List<AccountWallet> list() {
        return this.accounts;
    }
}
