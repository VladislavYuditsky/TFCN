package com.yuditsky.serial_port_messenger.view;

import com.yuditsky.serial_port_messenger.core.MessengerCore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MessengerView {

    public boolean checkPorts(ArrayList<String> portNames) {
        return !portNames.isEmpty();
    }

    public int menu(String portName, String baudValue) {
        int choice;
        Scanner in = new Scanner(System.in);

        System.out.println("Enter:");
        System.out.println("1.Connect");
        System.out.println("2.Set port");
        System.out.println("3.Set baud");
        System.out.println("0.Exit");
        System.out.println("(Current port " + portName + ", baud value " + baudValue + ")");

        do {
            try {
                choice = in.nextInt();
            }catch(InputMismatchException e){
                choice = -1;
                in.next();
            }
        } while (choice < 0 || choice > 3);

        return choice;
    }

    public String setPortMenu(ArrayList<String> portNames) {
        Scanner in = new Scanner(System.in);
        String portName;

        System.out.println("Available ports: " + portNames);
        System.out.println("Enter a name of port: ");

        portName = in.next();

        return portNames.contains(portName) ? portName : portNames.get(0);
    }

    public String setBaudMenu() {
        Scanner in = new Scanner(System.in);
        String baudValue;

        System.out.println("Available bauds: " + Arrays.toString(MessengerCore.baudValues));
        System.out.println("Enter a baud: ");

        baudValue = in.next();

        return Arrays.binarySearch(MessengerCore.baudValues, baudValue) != -1 ? baudValue : MessengerCore.baudValues[0];
    }

    public int messengerMenu() {
        Scanner in = new Scanner(System.in);
        int choice;

        System.out.println("Enter:");
        System.out.println("1.Write message");
        System.out.println("0.Close port and exit");

        do {
            try {
                choice = in.nextInt();
            }catch(InputMismatchException e){
                choice = -1;
                in.next();
            }
        } while (choice < 0 || choice > 1);

        return choice;
    }

    public static void showMessage(String message){
        System.out.println(message);
    }
}
