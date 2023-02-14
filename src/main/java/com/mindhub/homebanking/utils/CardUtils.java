package com.mindhub.homebanking.utils;

import java.util.Random;

final public class CardUtils {
    private CardUtils() {
    }

    public static int getCVV(Random random) {
        int numeroCvv = random.nextInt(999);
        return numeroCvv;
    }

    public static String getCardNumber(Random random) {
        StringBuilder numeroCard = new StringBuilder();
        int seccionCard= 0;
        for (int i = 0; i<4; i++){
            seccionCard = random.nextInt(9999);
            if(i<3){
                numeroCard.append(String.format("%04d", seccionCard));
                numeroCard.append("-");
            } else {
                numeroCard.append(String.format("%04d", seccionCard));
            }
        }
        String numeroCardStr = numeroCard.toString();
        return numeroCardStr;
    }
}
