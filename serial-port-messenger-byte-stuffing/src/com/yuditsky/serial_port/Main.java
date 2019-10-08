package com.yuditsky.serial_port;

import com.yuditsky.serial_port.core.MessengerCore;
import com.yuditsky.serial_port.ui.MessengerInterface;

import java.util.Scanner;

public class Main {
    public static MessengerCore messengerCore = new MessengerCore();
    public static MessengerInterface messengerInterface = new MessengerInterface();

    private static void openMessenger() {
        Scanner in = new Scanner(System.in);

        while (messengerInterface.messengerMenu() != 0) {
            messengerCore.sendMessage(in.next());
        }
        messengerCore.stop();
    }

    public static void main(String[] args) {
        String baudValue;
        String portName;
        int choice;

        if (messengerInterface.checkPorts(messengerCore.getPortNames())) {
            portName = messengerCore.getPortNames().get(0);
            baudValue = MessengerCore.baudValues[0];

            while ((choice = messengerInterface.menu(portName, baudValue)) != 0) {
                switch (choice) {
                    case 1:
                        if (messengerCore.connect(portName, baudValue)) {
                            openMessenger();
                        }
                        break;
                    case 2:
                        portName = messengerInterface.setPortMenu(messengerCore.getPortNames());
                        break;
                    case 3: {
                        baudValue = messengerInterface.setBaudMenu();
                        break;
                    }
                    default:
                        break;
                }
            }
        }
    }

}