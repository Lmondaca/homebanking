package com.mindhub.homebanking;


import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RepositoriesTest {
 /*   @Autowired
    LoanRepository loanRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CardRepository cardRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    TransactionRepository transactionRepository;



    @Test
    public void existLoans(){

        List<Loan> loans = loanRepository.findAll();

        assertThat(loans,is(not(empty())));

    }
    @Test
    public void existPersonalLoan(){

        List<Loan> loans = loanRepository.findAll();

        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));

    }
    @Test
    public  void existAhorroAccount(){
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts, hasItem(hasProperty("accountType", is(AccountType.AHORRO))));

    }
    @Test
    public void existAccount(){
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts, is(not(empty())));

    }
    @Test
    public void existCreditCard(){
        List<Card> cards = cardRepository.findAll();
        assertThat(cards, hasItem(hasProperty("type", is(CardType.CREDIT))));

    }
    @Test
    public void cvvGreaterThanZeroCard(){
        List<Integer> cards = cardRepository.findAll().stream().map((card) -> {return card.getCvv();}).collect(Collectors.toList());
        assertThat(cards, everyItem(greaterThan(1)));

    }
    @Test
    public void containArrobaInEmailClient(){
        List<String> clients = clientRepository.findAll().stream().map((client) -> {return client.geteMail();}).collect(Collectors.toList());
        assertThat(clients, everyItem(containsString("@")));
    }
    @Test
    public void existClient(){
        List<Client> clients = clientRepository.findAll();
        assertThat(clients, is(not(empty())));
    }

    @Test
    public void existTransaction(){
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions,is(not(empty())));
    }
    @Test
    public void existTypeTransaction(){
        List<Transaction> transactions = transactionRepository.findAll();
        assertThat(transactions,hasItem(hasProperty("type", is(TransactionType.CREDITO))));
    }
*/
}
