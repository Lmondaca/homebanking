package com.mindhub.homebanking;

import com.mindhub.homebanking.utils.CardUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
public class CardUtilsTests {
    @Test
    public void cardNumberIsCreated(){

        String cardNumber = CardUtils.getCardNumber(new Random());

        assertThat(cardNumber,is(not(emptyOrNullString())));

    }
    @Test
    public void cardNumberLeng(){
        String cardNumber = CardUtils.getCardNumber(new Random());

        assertThat(cardNumber.length(), equalTo(19));
    }
    @Test
    public void numberCVVLess1000(){
        int cvvNumber = CardUtils.getCVV(new Random());

        assertThat(cvvNumber, is(lessThan(1000)));
    }
    @Test
    public void numberCVVIsInt(){
        int cvvNumber = CardUtils.getCVV((new Random()));

        assertThat(cvvNumber, isA(Integer.class));
    }
}
