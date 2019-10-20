package com.yuditsky.serial_port_messenger;

import com.yuditsky.serial_port_messenger.core.MessengerCore;
import com.yuditsky.serial_port_messenger.view.MessengerView;

import java.util.Scanner;

public class Main {
    public static MessengerCore messengerCore = new MessengerCore();
    public static MessengerView view = new MessengerView();

    private static void openMessenger() {
        Scanner in = new Scanner(System.in);

        while (view.messengerMenu() != 0) {
            messengerCore.sendMessage(in.nextLine());
        }
        messengerCore.stop();
    }

    public static void main(String[] args) {
        String baudValue;
        String portName;
        int choice;

        if (view.checkPorts(messengerCore.getPortNames())) {
            portName = messengerCore.getPortNames().get(0);
            baudValue = MessengerCore.baudValues[0];

            while ((choice = view.menu(portName, baudValue)) != 0) {
                switch (choice) {
                    case 1:
                        if (messengerCore.connect(portName, baudValue)) {
                            openMessenger();
                        }
                        break;
                    case 2:
                        portName = view.setPortMenu(messengerCore.getPortNames());
                        break;
                    case 3: {
                        baudValue = view.setBaudMenu();
                        break;
                    }
                    default:
                        break;
                }
            }
        }
    }

}