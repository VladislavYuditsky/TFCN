package com.yuditsky.serial_port_messenger.core;


import com.yuditsky.serial_port_messenger.service.HammingCode;
import com.yuditsky.serial_port_messenger.view.MessengerView;
import jssc.*;

import java.util.ArrayList;
import java.util.Arrays;

import static java.nio.charset.StandardCharsets.UTF_8;

public class MessengerCore {
    private SerialPort serialPort;
    public final static String[] baudValues = {"110", "300", "600", "1200", "4800",
            "9600", "14400", "19200", "38400", "57600", "115200", "128000", "256000"};

    public ArrayList<String> getPortNames() {
        return new ArrayList<>(Arrays.asList(SerialPortList.getPortNames()));
    }

    public boolean connect(String portName, String speedName) {
        serialPort = new SerialPort(portName);
        try {
            switch (speedName) {
                case "110":
                    serialPort.openPort();
                    serialPort.setParams(SerialPort.BAUDRATE_110,
                            SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);
                    break;
                case "300":
                    serialPort.openPort();
                    serialPort.setParams(SerialPort.BAUDRATE_300,
                            SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);
                    break;
                case "600":
                    serialPort.openPort();
                    serialPort.setParams(SerialPort.BAUDRATE_600,
                            SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);
                    break;
                case "1200":
                    serialPort.openPort();
                    serialPort.setParams(SerialPort.BAUDRATE_1200,
                            SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);
                    break;
                case "4800":
                    serialPort.openPort();
                    serialPort.setParams(SerialPort.BAUDRATE_4800,
                            SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);
                    break;
                case "9600":
                    serialPort.openPort();
                    serialPort.setParams(SerialPort.BAUDRATE_9600,
                            SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_EVEN);
                    break;
                case "14400":
                    serialPort.openPort();
                    serialPort.setParams(SerialPort.BAUDRATE_14400,
                            SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_EVEN);
                    break;
                case "19200":
                    serialPort.openPort();
                    serialPort.setParams(SerialPort.BAUDRATE_19200,
                            SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);
                    break;
                case "38400":
                    serialPort.openPort();
                    serialPort.setParams(SerialPort.BAUDRATE_38400,
                            SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);
                    break;
                case "57600":
                    serialPort.openPort();
                    serialPort.setParams(SerialPort.BAUDRATE_57600,
                            SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);
                    break;
                case "115200":
                    serialPort.openPort();
                    serialPort.setParams(SerialPort.BAUDRATE_115200,
                            SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);
                    break;
                case "128000":
                    serialPort.openPort();
                    serialPort.setParams(SerialPort.BAUDRATE_128000,
                            SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);
                    break;
                case "256000":
                    serialPort.openPort();
                    serialPort.setParams(SerialPort.BAUDRATE_256000,
                            SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);
                    break;
                default:
                    return false;
            }

            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT);

            serialPort.addEventListener(new PortReader(), SerialPort.MASK_RXCHAR);

        } catch (SerialPortException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean stop() {
        try {
            serialPort.closePort();
        } catch (SerialPortException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void sendMessage(String message) {
        Runnable runnable = () -> {
            try {
                serialPort.writeBytes(HammingCode.encode((message).getBytes()));
            } catch (SerialPortException e) {
                e.printStackTrace();
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();

        try {
            thread.join(2000);
            if (thread.isAlive()) {
                thread.interrupt();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private class PortReader implements SerialPortEventListener {
        @Override
        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR() && event.getEventValue() > 0) {
                try {
                    MessengerView.showMessage(new String(HammingCode.decode(serialPort.readBytes(event.getEventValue())), UTF_8));
                } catch (SerialPortException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
