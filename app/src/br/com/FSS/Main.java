package app.src.br.com.FSS;

import java.util.*;


import java.time.format.DateTimeFormatter;

import app.src.br.com.FSS.exception.NotMoneyEnoughException;
import app.src.br.com.FSS.exception.AccountNotFoundExcpetion;
import app.src.br.com.FSS.repository.AccountRepository;
import app.src.br.com.FSS.repository.InvestmentRepository;

public class Main {

    private static final AccountRepository ARepository = new AccountRepository();
    private static final InvestmentRepository IRepository = new InvestmentRepository();
    private static final Scanner input = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Olá! Seja bem-vindo ao Capybara Bank!");

        while (true) {
            System.out.println("\nSelecione a operação desejada:");
            System.out.println("1) Criar uma conta");
            System.out.println("2) Criar um investimento");
            System.out.println("3) Criar carteira de investimento");
            System.out.println("4) Depósito");
            System.out.println("5) Saque");
            System.out.println("6) Transferência entre contas");
            System.out.println("7) Investir");
            System.out.println("8) Resgatar investimento");
            System.out.println("9) Listar contas");
            System.out.println("10) Listar investimentos");
            System.out.println("11) Listar carteiras de investimento");
            System.out.println("12) Atualizar investimentos");
            System.out.println("13) Verificar histórico da conta");
            System.out.println("14) Encerrar sessão");

            int choose = input.nextInt();
            input.nextLine();

            switch (choose) {
                case 1 -> createAccount();
                case 2 -> createInvestment();
                case 3 -> criarCarteiraDeInvestimento();
                case 4 -> deposito();
                case 5 -> saque();
                case 6 -> transferencia();
                case 7 -> investir();
                case 8 -> resgatarInvestimento();
                case 9 -> ARepository.list().forEach(System.out::println);
                case 10 -> IRepository.list().forEach(System.out::println);
                case 11 -> IRepository.listWallets().forEach(System.out::println);
                case 12 -> { IRepository.updateAmount(); System.out.println("Investimentos atualizados."); }
                case 13 -> checkHistory();
                case 14 -> { System.out.println("Sessão encerrada."); System.exit(0); }
                default -> System.out.println("Opção inválida!");
            }
        }
    }

    private static void createAccount() {
        System.out.println("Digite as chaves PIX (separadas por ponto e vírgula ';'):");
        var pix = Arrays.asList(input.nextLine().split(";"));
        System.out.println("Informe o valor inicial do depósito:");
        long amount = input.nextLong();
        var wallet = ARepository.create(pix, amount);
        System.out.println("A conta " + wallet + " foi criada com êxito.");
    }

    private static void createInvestment() {
        System.out.println("Informe a taxa do investimento (em %):");
        long tax = input.nextLong();
        System.out.println("Informe o valor inicial do investimento:");
        long initialFunds = input.nextLong();
        IRepository.create(tax, initialFunds);
        System.out.println("Investimento criado com sucesso.");
    }

    private static void deposito() {
        System.out.println("Insira a chave PIX da conta para depósito:");
        String pix = input.next();
        System.out.println("Agora insira o valor do depósito:");
        long amount = input.nextLong();
        try {
            ARepository.deposito(pix, amount);
        } catch (AccountNotFoundExcpetion ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void saque() {
        System.out.println("Insira a chave PIX da conta para saque:");
        String pix = input.next();
        System.out.println("Agora insira o valor do saque:");
        long amount = input.nextLong();
        try {
            ARepository.saque(pix, amount);
        } catch (NotMoneyEnoughException | AccountNotFoundExcpetion ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void transferencia() {
        System.out.println("Insira a chave PIX da conta de origem:");
        String source = input.next();
        System.out.println("Insira a chave PIX da conta de destino:");
        String target = input.next();
        System.out.println("Agora insira o valor da transferência:");
        long amount = input.nextLong();
        try {
            ARepository.transferencia(source, target, amount);
        } catch (AccountNotFoundExcpetion ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void criarCarteiraDeInvestimento() {
        System.out.println("Informe a chave PIX da conta:");
        String pix = input.next();
        var account = ARepository.findByPix(pix);
        System.out.println("Digite o ID do investimento:");
        long id = input.nextLong();
        IRepository.initInvestment(account, id);
        System.out.println("Carteira de investimento criada com sucesso.");
    }

    private static void investir() {
        System.out.println("Insira a chave PIX da conta que fará o investimento:");
        String pix = input.next();
        System.out.println("Agora insira o valor a investir:");
        long amount = input.nextLong();
        try {
            IRepository.deposito(pix, amount);
        } catch (AccountNotFoundExcpetion ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void resgatarInvestimento() {
        System.out.println("Insira a chave PIX da conta para resgate:");
        String pix = input.next();
        System.out.println("Agora insira o valor do saque:");
        long amount = input.nextLong();
        try {
            IRepository.saque(pix, amount);
        } catch (NotMoneyEnoughException | AccountNotFoundExcpetion ex) {
            System.out.println(ex.getMessage());
        }
    }

    private static void checkHistory() {
        System.out.println("Insira a chave PIX da conta para ver o histórico:");
        String pix = input.next();
        try {
            var sortedHistory = ARepository.getHistory(pix);
            sortedHistory.forEach((k, v) -> {
                System.out.println(k.format(DateTimeFormatter.ISO_DATE_TIME));
                v.forEach(t -> {
                    System.out.println(t.transactionID());
                    System.out.println(t.description());
                });
                System.out.println("Total de transações: " + v.size());
            });
        } catch (AccountNotFoundExcpetion ex) {
            System.out.println(ex.getMessage());
        }
    }
}
