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
public class PrimeServer {
    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket(8888)) {
            System.out.println("Server is running...");

            byte[] receiveBuffer = new byte[1024];
            byte[] sendBuffer;

            while (true) {
                // Nhận dữ liệu từ client
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                socket.receive(receivePacket);

                String input = new String(receivePacket.getData(), 0, receivePacket.getLength());
                int n;

                try {
                    n = Integer.parseInt(input.trim());
                } catch (NumberFormatException e) {
                    n = -1; // Giá trị không hợp lệ
                }

                String response;
                if (n <= 0) {
                    response = "K hop le. Nhap so be hon 0";
                } else {
                    response = "Server nhận n = " + n + ". Processing...";
                }

                // Gửi phản hồi lại cho client
                sendBuffer = response.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(
                        sendBuffer,
                        sendBuffer.length,
                        receivePacket.getAddress(),
                        receivePacket.getPort()
                );
                socket.send(sendPacket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
