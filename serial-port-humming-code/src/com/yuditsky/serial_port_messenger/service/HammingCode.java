package com.yuditsky.serial_port_messenger.service;

import org.apache.commons.lang3.ArrayUtils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

public class HammingCode {

    private static void addParityBits(ArrayList<Character> bits) {
        int i = 0;
        int j;
        while ((j = (int) Math.pow(2, i)) < bits.size()) {
            bits.add(j - 1, '0');
            i++;
        }
    }

    private static void calcParityBits(ArrayList<Character> bits) {
        int i, j;
        int e = 0;
        while ((i = (int) Math.pow(2, e)) < bits.size()) {
            j = i - 1;
            int num = 0;
            while (j < bits.size()) {
                for (int k = 0; k < i & j < bits.size(); k++) {
                    if (bits.get(j) == '1') {
                        num++;
                    }
                    j++;
                }
                j += i;
            }

            if (num % 2 != 0) {
                bits.set(i - 1, '1');
            }

            e++;
        }
    }

    private static void addStartBit(ArrayList<Character> bits){
        bits.add(0,'1');
    }

    public static byte[] encode(byte[] msg) {

        String binaryString = new BigInteger(msg).toString(2);
        ArrayList<Character> bits = new ArrayList<>(Arrays.asList(ArrayUtils.toObject(binaryString.toCharArray())));

        //System.out.println("Binary message: " + bits);

        addParityBits(bits);

        calcParityBits(bits);

        //System.out.println("Hamming code:" + bits);

        addStartBit(bits);

        Character[] bitsAsChar = new Character[bits.size()];
        bits.toArray(bitsAsChar);

        return new BigInteger(new String(ArrayUtils.toPrimitive(bitsAsChar)), 2).toByteArray();
    }

    private static void removeParityBits(ArrayList<Character> bits) {
        int i = 0;
        while ((int) Math.pow(2, i) <= bits.size()) {
            i++;
        }
        i--;

        int j;
        while ((j = (int) Math.pow(2, i)) >= 1) {
            bits.remove(j - 1);
            i--;
        }
    }

    private static void removeStartBit(ArrayList<Character> bits){
        bits.remove(0);
    }

    public static int takeErrorPosition(ArrayList<Character> incomingBits, ArrayList<Character> bits){
        int errorPosition = 0;
        int i, e = 0;

        while((i = (int)Math.pow(2,e)) <= bits.size()){
            if(incomingBits.get(i - 1) != bits.get(i - 1)){
                errorPosition += i - 1;
            }
            e++;
        }

        return errorPosition;
    }

    public static void invert(ArrayList<Character> bits, int position){
        if(bits.get(position).equals('0')){
            bits.set(position, '1');
        } else{
            bits.set(position, '0');
        }
    }

    public static boolean isError(ArrayList<Character> incomingBits, ArrayList<Character> bits){
        int i, e = 0;

        while((i = (int)Math.pow(2,e)) <= bits.size()){
            if(incomingBits.get(i - 1) != bits.get(i - 1)){
                return true;
            }
            e++;
        }

        return  false;
    }

    public static byte[] decode(byte[] msg) {

        String binaryString = new BigInteger(msg).toString(2);
        ArrayList<Character> incomingBits = new ArrayList<>(Arrays.asList(ArrayUtils.toObject(binaryString.toCharArray())));

        removeStartBit(incomingBits);

        ArrayList<Character> bits = new ArrayList<>(incomingBits);

        //System.out.println("Incoming message(Hamming code): " + incomingBits);

        calcParityBits(bits);

        //System.out.println("Humming code: " + bits);

        if(isError(incomingBits, bits)){
           invert(bits,takeErrorPosition(incomingBits, bits));
        }

        removeParityBits(bits);

        //System.out.println("Binary message: " + bits);

        Character[] bitsAsChar = new Character[bits.size()];
        bits.toArray(bitsAsChar);

        return new BigInteger(new String(ArrayUtils.toPrimitive(bitsAsChar)), 2).toByteArray();
    }
}
