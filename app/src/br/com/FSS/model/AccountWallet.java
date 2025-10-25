package app.src.br.com.FSS.model;

import java.util.List;

public class AccountWallet extends carteira {

    private final List<String> pix;

    public AccountWallet(final List<String> pix) {
        super(Bankservice.ACCOUNT);
        this.pix = pix;
    }

    public AccountWallet(final long amount, final List<String> pix) {
        super(Bankservice.ACCOUNT);
        this.pix = pix;
        addMoney(generateMoney(amount, "conta criada"), Bankservice.ACCOUNT, "depósito inicial");
    }

    public void addMoney(final long amount, final String description){
        var money = generateMoney(amount, description);
        this.money.addAll(money);
    }

    @Override
    public String toString() {
        return super.toString() + " Carteira{"  +
               "pix=" + pix +
               "}";
    }
    public List<String> getPix() {
        return pix;
    }
}
