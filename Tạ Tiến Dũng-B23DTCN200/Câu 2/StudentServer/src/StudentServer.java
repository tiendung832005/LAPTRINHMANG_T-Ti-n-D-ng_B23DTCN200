/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author hi
 */
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class StudentServer {
    public static boolean isPrime(int number) {
        if (number < 2) return false;
        for (int i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) return false;
        }
        return true;
    }

    public static String processStudentCode(String studentCode) {
        int evenSum = 0;
        StringBuilder primeNumbers = new StringBuilder();

        for (char c : studentCode.toCharArray()) {
            if (Character.isDigit(c)) {
                int digit = c - '0';
                if (digit % 2 == 0) {
                    evenSum += digit;
                }
                if (isPrime(digit)) {
                    primeNumbers.append(digit).append(" ");
                }
            }
        }

        return "Tổng các số chẵn: " + evenSum +
               "\nSố nguyên tố: " + primeNumbers.toString().trim();
    }

    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket(8888)) {
            System.out.println("Server đang chạy...");

            ExecutorService executorService = Executors.newFixedThreadPool(5);

            while (true) {
                byte[] receiveBuffer = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                socket.receive(receivePacket);

                
                executorService.execute(() -> {
                    try {
                        String studentCode = new String(receivePacket.getData(), 0, receivePacket.getLength()).trim();
                        String response;

                        if (studentCode.isEmpty() || !studentCode.matches("\\d+")) {
                            response = "Mã sinh viên không hợp lệ. Vui lòng chỉ nhập giá trị số.";
                        } else {
                            response = processStudentCode(studentCode);
                        }

                        byte[] sendBuffer = response.getBytes();
                        DatagramPacket sendPacket = new DatagramPacket(
                                sendBuffer,
                                sendBuffer.length,
                                receivePacket.getAddress(),
                                receivePacket.getPort()
                        );
                        socket.send(sendPacket);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
