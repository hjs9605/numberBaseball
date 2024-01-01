package com.example.numberbaseball.vo;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static org.springframework.util.ObjectUtils.isEmpty;

public class BaseBallNumber {

    private final String number;


    public BaseBallNumber(String number) {
        if (isEmpty(number)) {
            throw new IllegalArgumentException("숫자야구게임의 숫자는 필수입니다");
        }
        if (number.length() != 3) {
            throw new IllegalArgumentException("숫자 야구 게임 숫자는 3자리 입니다.");
        }
        if (!isNumber(number)) {
            throw new IllegalArgumentException("숫자야구 게임은 숫자로만 구성되어야 합니다");
        }
        if (isDuplicate(number)) {
            throw new IllegalArgumentException("숫자는 겹칠 수 없습니다.");
        }

        this.number = number;
    }

    private boolean isDuplicate(String number) {

        char[] chars = number.toCharArray();
        Set<Character> set = new HashSet<>();

        for(char ch : chars) {
            if(!set.add(ch)){
                return true;
            }
        }
        return false;
    }

    public String getNumber() {
        return this.number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BaseBallNumber)) {
            return false;
        }
        BaseBallNumber number = (BaseBallNumber) o;
        return Objects.equals(number, number.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

    public char[] toArray() {
        return number.toCharArray();
    }

    private boolean isNumber(String number) {
        for (char digit : number.toCharArray()) {
            if (!Character.isDigit(digit)) {
                return false;
            }
        }
        return true;
    }
}
