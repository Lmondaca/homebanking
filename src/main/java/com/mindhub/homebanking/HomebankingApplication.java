package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class HomebankingApplication {
	@Autowired
	private PasswordEncoder passwordEnconder;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository repository, AccountRepository repository2,
									  TransactionRepository repository3, LoanRepository repository4,
									  ClientLoanRepository repository5, CardRepository repository6) {
		return (args) -> {
			// save a couple of customers
			Client client1 = new Client("Melba", "Morel", "melba@mindhub.com", "56943856103",
					"Av. NoExiste #1234", "Casada", "EmpresaCo", passwordEnconder.encode("12345"));

			Account account1 = new Account("VIN001", LocalDateTime.now(), 5000.0);
			Account account2 = new Account("VIN002", LocalDateTime.now().plusDays(1), 7500.0);

			Client client2 = new Client("Carlos", "Gaaaawar", "Pater@mindhub.com", "56990023745",
					"Pj. OtraDimension #1002", "Soltero", "EmpresaSPA", passwordEnconder.encode("12344"));

			Transaction trx1 = new Transaction(TransactionType.CREDITO, 1093.0, "DepositoC11", LocalDateTime.now());
			Transaction trx2 = new Transaction(TransactionType.DEBITO, 1562.0, "CompraC11", LocalDateTime.now());
			Transaction trx3 = new Transaction(TransactionType.CREDITO, 5381.0, "DepositoC21", LocalDateTime.now());
			Transaction trx4 = new Transaction(TransactionType.DEBITO, 1322.0, "GiroC21", LocalDateTime.now());
			Transaction trx5 = new Transaction(TransactionType.CREDITO, 2512.0, "DepositoC22", LocalDateTime.now());

			Account account3 = new Account("VIN003", LocalDateTime.now(), 5500.0);
			Transaction trx6 = new Transaction(TransactionType.CREDITO, 3241.0, "DepositoC31", LocalDateTime.now());

			Loan loan1 = new Loan("Hipotecario", 500000.0, List.of(12, 24, 36, 48, 60));
			Loan loan2 = new Loan("Personal", 100000.0, List.of(6, 12, 24));
			Loan loan3 = new Loan("Automotriz", 300000.0, List.of(6, 12, 24, 36));

			Card card1 = new Card("MELBA MOREL", CardType.DEBIT, CardColor.GOLD, "1234-5678-9012-3456", 123, LocalDateTime.now(), LocalDateTime.now().plusYears(5));
			Card card2 = new Card("MELBA MOREL", CardType.CREDIT, CardColor.TITANIUM, "9420-1912-0920-9831", 412, LocalDateTime.now(), LocalDateTime.now().plusYears(5) );
			Card card3 = new Card("CARLOS GAAAAWAR", CardType.CREDIT, CardColor.SILVER, "9420-1913-0910-6445", 521, LocalDateTime.now(), LocalDateTime.now().plusYears(5) );

			Client client3 = new Client("admin", "Barato", "hol@fad.com", "56987542622",
					"Elm Street #2215", "Viudo", "BusinessCo", passwordEnconder.encode("adm123"));

			repository6.save(card1);
			repository6.save(card2);
			repository6.save(card3);

			client1.addCards(card1);
			client1.addCards(card2);
			client2.addCards(card3);

			repository.save(client1);
			repository.save(client2);
			repository.save(client3);

			repository6.save(card1);
			repository6.save(card2);
			repository6.save(card3);

			repository4.save(loan1);
			repository4.save(loan2);
			repository4.save(loan3);

			ClientLoan clientLoan1 = new ClientLoan(400000, 60, client1, loan1);
			ClientLoan clientLoan2 = new ClientLoan(50000, 12, client1, loan2);

			ClientLoan clientLoan3 = new ClientLoan(100000, 24, client2, loan2);
			ClientLoan clientLoan4= new ClientLoan(200000, 36, client2, loan3);

			repository5.save(clientLoan1);
			repository5.save(clientLoan2);
			repository5.save(clientLoan3);
			repository5.save(clientLoan4);


			repository4.save(loan1);
			repository4.save(loan2);
			repository4.save(loan3);
			repository.save(client1);
			repository.save(client2);

			repository2.save(account1);
			repository2.save(account2);
			repository2.save(account3);

			repository3.save(trx1);
			repository3.save(trx2);
			repository3.save(trx3);
			repository3.save(trx4);
			repository3.save(trx5);
			repository3.save(trx6);

			client1.setAccounts(account1);
			client1.setAccounts(account2);
			client2.setAccounts(account3);
			repository.save(client1);
			repository.save(client2);
			repository2.save(account1);
			repository2.save(account2);
			repository2.save(account3);

			account1.setTransactions(trx1);
			account1.setTransactions(trx2);
			account2.setTransactions(trx3);
			account2.setTransactions(trx4);
			account2.setTransactions(trx5);

			account3.setTransactions(trx6);

			repository3.save(trx1);
			repository3.save(trx2);
			repository3.save(trx3);
			repository3.save(trx4);
			repository3.save(trx5);
			repository3.save(trx6);

			repository2.save(account1);
			repository2.save(account2);
			repository2.save(account3);

		};
	}


}
