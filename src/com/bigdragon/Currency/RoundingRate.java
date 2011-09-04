package com.bigdragon.currency;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 @brief Класс округлание курса
 @details Класс выполняет округленние значений курса после конверитирования.
 */
public class RoundingRate {

/**
 @brief Округление курса 
 @details На вход перелается занчени типа Float
 на выходе мы получаем округленное значение типа Float
 @param Args Значение которое надо округлить 
 @return tmpFloat Округленно значение
 */
public float RoundingFloat (Float Args) {
        String ArgsString = String.valueOf(Args);
        float tmpFloat = new BigDecimal(Float.parseFloat(ArgsString)).setScale(2, RoundingMode.UP).floatValue();
        return tmpFloat;
    }
/**
 @brief Округление курса 
 @details На вход перелается занчени типа String
 на выходе мы получаем округленное значение типа Float
 @param args Значение которое надо округлить 
 @return tmpFloat Округленно значение
 */
public float Rounding (String args) {
        float tmpFloat = new BigDecimal(Float.parseFloat(args)).setScale(2, RoundingMode.UP).floatValue();
        return tmpFloat;
    }
}
