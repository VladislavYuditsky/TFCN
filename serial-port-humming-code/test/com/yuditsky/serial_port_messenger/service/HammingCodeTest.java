package com.yuditsky.serial_port_messenger.service;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;

import static com.yuditsky.serial_port_messenger.service.HammingCode.*;
import static org.junit.Assert.*;

public class HammingCodeTest {

    @Test
    public void encodeArrayEqualsTest() {
        byte[] bytes = {1, 0, 1, 0, 1};
        assertArrayEquals(HammingCode.encode(bytes), encode(bytes));
    }

    @Test
    public void decodeArrayEqualsTest() {
        byte[] bytes = {1, 1, 1, 0, 0, 0};
        assertArrayEquals(HammingCode.decode(bytes), decode(bytes));
    }

    @Test
    public void isErrorTrueTest(){
        ArrayList<Character> incomingBits = new ArrayList<>();
        ArrayList<Character> bits = new ArrayList<>();

        incomingBits.add('1');
        bits.add('1');

        incomingBits.add('1');
        bits.add('1');

        incomingBits.add('0');
        bits.add('0');

        incomingBits.add('0');
        bits.add('1');

        assertTrue(isError(incomingBits, bits));
    }

    @Test
    public void isErrorFalseTest(){
        ArrayList<Character> incomingBits = new ArrayList<>();
        ArrayList<Character> bits = new ArrayList<>();

        incomingBits.add('1');
        bits.add('1');

        incomingBits.add('1');
        bits.add('1');

        incomingBits.add('0');
        bits.add('0');

        assertFalse(isError(incomingBits, bits));
    }

    @Test
    public void takeErrorPositionTest(){
        ArrayList<Character> incomingBits = new ArrayList<>();
        ArrayList<Character> bits = new ArrayList<>();

        incomingBits.add('1');
        bits.add('1');

        incomingBits.add('1');
        bits.add('1');

        incomingBits.add('0');
        bits.add('0');

        incomingBits.add('0');
        bits.add('1');

        assertEquals(takeErrorPosition(incomingBits, bits), 3);
    }

    @Test
    public void errorFixTest(){
        String message = "Hello";

        for(int errorPosition = 0; errorPosition < message.length(); errorPosition++) {

            byte[] encodeBytes = encode(message.getBytes());

            String binaryEncodeString = new BigInteger(encodeBytes).toString(2);
            ArrayList<Character> encodeBits = new ArrayList<>(Arrays.asList(ArrayUtils.toObject(binaryEncodeString.toCharArray())));

            invert(encodeBits, errorPosition);

            byte[] decodeBytes = decode(encodeBytes);

            String incomingMessage = new String(decodeBytes);

            assertEquals(message, incomingMessage);
        }
    }

    @Test
    public void invertTest(){
        String message = "Hello";

        for(int errorPosition = 0; errorPosition < message.length(); errorPosition++) {

            byte[] encodeBytes = encode(message.getBytes());

            String binaryEncodeString = new BigInteger(encodeBytes).toString(2);
            ArrayList<Character> encodeBits = new ArrayList<>(Arrays.asList(ArrayUtils.toObject(binaryEncodeString.toCharArray())));

            invert(encodeBits, errorPosition);

            String distortedMessage = new String(encodeBytes);

            assertNotEquals(message, distortedMessage);
        }
    }
}