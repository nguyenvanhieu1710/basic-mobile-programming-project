package com.example.basicmobileprogramingproject.Utils;

import java.util.Random;

public class RamdomUtils {
    public static String appendRandomNumbers(String input) {
        Random random = new Random();

        // Tạo 3 số ngẫu nhiên từ 0 đến 9
        int randomNumber1 = random.nextInt(10); // Số ngẫu nhiên thứ nhất
        int randomNumber2 = random.nextInt(10); // Số ngẫu nhiên thứ hai
        int randomNumber3 = random.nextInt(10); // Số ngẫu nhiên thứ ba

        // Ghép các số ngẫu nhiên vào chuỗi đầu vào
        String result = input + randomNumber1 + randomNumber2 + randomNumber3;

        return result;
    }
}
