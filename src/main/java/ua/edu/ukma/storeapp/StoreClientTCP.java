package ua.edu.ukma.storeapp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.InvalidKeyException;
import java.util.Arrays;
import java.util.List;

public class StoreClientTCP {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private String IP;
    private int Port;

    public static void main(String[] args) throws InterruptedException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException {
        StoreClientTCP client = new StoreClientTCP();
        client.startConnection("127.0.0.1", 5454);
        JSONObject json = new JSONObject();
        json.put("key", "value");
        System.out.println(Arrays.toString(json.toString().getBytes()));
        byte[] arr=new Packet((byte) 1, 3, new byte[0], 10).toByteArray();
        System.out.println(Arrays.toString(arr));
        System.out.println(arr.length);
        System.out.println();
        System.out.println(client.sendByteArray(arr));
    }

    public boolean startConnection(String ip, int port) throws InterruptedException {
        IP = ip;
        Port = port;
        boolean trying_to_connect = true;
        //int tries = 1;
        while (trying_to_connect) {
            try {
                clientSocket = new Socket(ip, port);
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                trying_to_connect = false;
            } catch (IOException e) {
                System.out.println("Could not connected to " + ip + ":" + port);
                System.out.println("Retrying...");
                Thread.sleep(3000);
            }
            //++tries;
            //if (tries >= 5) break;
        }
//        if (tries >= 5) {
//            System.out.println("Too many failed connection attempts. Terminating connection.");
//            try {
//                stopConnection();
//            } catch (IOException e) {
//                System.out.println("Could not stop connection.");
//            }
//            return false;
//        }
        //heartbeat check
        new Thread(() -> {
            while(true){
                if(!ping()){
                    try {
                        System.out.println("OOPs!");
                        stopConnection();
                        startConnection(IP, port);
                    } catch (InterruptedException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Tried!");
            }
        }).start();
        return true;
    }

    public int listen() throws IOException {
        return in.read();
    }

    public boolean ping() {
        out.println("ping");
        String input;
        try {
            input = in.readLine();
        }
        catch (IOException e) {
            return false;
        }
        return input.equals("pong");
    }

    public List<goods> getAllProductsList() throws Exception {
        JSONObject json = new JSONObject();
        json.put("key", "value");
        json.put("table", "goods");
        byte[] arr = new Packet((byte) 1, 3, json.toString().getBytes(), 10).toByteArray();
        String result = sendByteArray(arr);
        Packet resPacket = new Packet(stringToArray(result), Key.KEY);

        ObjectMapper mapper = new ObjectMapper();
        //System.out.println(Arrays.toString(resPacket.message().getByteMessage()));
        //ObservableList<goods> lst = FXCollections.<goods> observableArrayList(good1, good2);
        //System.out.println(newList);
        return mapper.readValue(resPacket.message().getByteMessage(), new TypeReference<List<goods>>() {});
    }

    public List<String> getHash(String login) throws Exception {
        JSONObject innerJson = new JSONObject();
        innerJson.put("name", login);

        JSONObject json = new JSONObject();
        json.put("info", innerJson);
        json.put("table", "hash");

        byte[] arr = new Packet((byte) 1, 0, json.toString().getBytes(), 10).toByteArray();
        String result = sendByteArray(arr);
        Packet resPacket = new Packet(stringToArray(result), Key.KEY);

        ObjectMapper mapper = new ObjectMapper();
        //System.out.println(Arrays.toString(resPacket.message().getByteMessage()));
        //ObservableList<goods> lst = FXCollections.<goods> observableArrayList(good1, good2);
        //System.out.println(newList);
        if (new String(resPacket.message().getByteMessage()).equals("Ok")) throw new Exception();

        return mapper.readValue(resPacket.message().getByteMessage(), new TypeReference<>() {});
    }

    public String updateGood(int id, String name, String desc, String manuf, int amount, double price, int group) throws Exception {
        JSONObject innerJson = new JSONObject();
        innerJson.put("name", name);
        innerJson.put("description", desc);
        innerJson.put("manufacturer", manuf);
        innerJson.put("amount", amount);
        innerJson.put("price", price);
        innerJson.put("id_group", group);
        innerJson.put("id", id);

        JSONObject json = new JSONObject();
        json.put("table", "goods");
        json.put("info", innerJson);

        byte[] arr = new Packet((byte) 1, 2, json.toString().getBytes(), 10).toByteArray();
        String result = sendByteArray(arr);

        Packet resPacket = new Packet(stringToArray(result), Key.KEY);
        return new String(resPacket.message().getByteMessage());
    }

    public String createGood(String name, String desc, String manuf, int amount, double price, int group) throws Exception {
        JSONObject innerJson = new JSONObject();
        innerJson.put("name", name);
        innerJson.put("description", desc);
        innerJson.put("manufacturer", manuf);
        innerJson.put("amount", amount);
        innerJson.put("price", price);
        innerJson.put("id_group", group);

        JSONObject json = new JSONObject();
        json.put("table", "goods");
        json.put("info", innerJson);
        byte[] arr = new Packet((byte) 1, 0, json.toString().getBytes(), 10).toByteArray();
        String result = sendByteArray(arr);
        Packet resPacket = new Packet(stringToArray(result), Key.KEY);
        return new String(resPacket.message().getByteMessage());
    }


    public void deleteGood(int id) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException, InterruptedException {
        JSONObject innerJson = new JSONObject();
        innerJson.put("id", id);

        JSONObject json = new JSONObject();
        json.put("table", "goods");
        json.put("info", innerJson);
        byte[] arr = new Packet((byte) 1, 1, json.toString().getBytes(), 10).toByteArray();
        String result = sendByteArray(arr);
    }

    public List<group> getAllGroupsList() throws Exception {
        JSONObject json = new JSONObject();
        json.put("table", "group");
        byte[] arr = new Packet((byte) 1, 3, json.toString().getBytes(), 10).toByteArray();
        String result = sendByteArray(arr);
        Packet resPacket = new Packet(stringToArray(result), Key.KEY);

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(resPacket.message().getByteMessage(), new TypeReference<List<group>>() {});
    }

    public String createGroup(String name, String desc) throws Exception {
        JSONObject innerJson = new JSONObject();
        innerJson.put("name", name);
        innerJson.put("description", desc);
        JSONObject json = new JSONObject();
        json.put("table", "group");
        json.put("info", innerJson);
        byte[] arr = new Packet((byte) 1, 0, json.toString().getBytes(), 10).toByteArray();
        String result = sendByteArray(arr);
        Packet resPacket = new Packet(stringToArray(result), Key.KEY);
        return new String(resPacket.message().getByteMessage());
    }

    public String updateGroup(int id, String name, String desc) throws Exception {
        JSONObject innerJson = new JSONObject();
        innerJson.put("name", name);
        innerJson.put("description", desc);
        innerJson.put("id", id);

        JSONObject json = new JSONObject();
        json.put("table", "group");
        json.put("info", innerJson);

        byte[] arr = new Packet((byte) 1, 2, json.toString().getBytes(), 10).toByteArray();
        String result = sendByteArray(arr);
        Packet resPacket = new Packet(stringToArray(result), Key.KEY);
        return new String(resPacket.message().getByteMessage());
    }

    public void deleteGroup(int id) throws IllegalBlockSizeException, BadPaddingException, InvalidKeyException, IOException, InterruptedException {
        JSONObject innerJson = new JSONObject();
        innerJson.put("id", id);

        JSONObject json = new JSONObject();
        json.put("table", "group");
        json.put("info", innerJson);
        byte[] arr = new Packet((byte) 1, 1, json.toString().getBytes(), 10).toByteArray();
        String result = sendByteArray(arr);
    }

    public String sendByteArray(byte[] btarr) throws InterruptedException, IOException {
        //        for (byte b : btarr) {
//            //System.out.println("Sent");
//            while(!ping()) {
//                System.out.println("Client not connected. Trying to reconnect.");
//                //stopConnection();
//                Thread.sleep(1000);
//                if (startConnection(IP, Port)) {
//                    sendByteArray(btarr);
//                }
//                return null;
//            }
//            sendByte(b);
//        }
        System.out.println("Sent!");
        out.println(Arrays.toString(btarr));
        String input = in.readLine();
        System.out.println("Received!");
        return input;
    }

    public void sendByte(byte bt){
        out.println(bt);
    }

    public void sendMessage(String msg) throws IOException, InterruptedException {
        sendByteArray(msg.getBytes());
    }

    public void stopConnection() throws IOException {
        if (in != null) in.close();
        if (out != null) out.close();
        if (clientSocket != null) clientSocket.close();
    }

    byte[] stringToArray(String string) {
        String newString = string.substring(1, string.length()-1);
        String[] array = newString.split(", ");
        byte[] result = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = Byte.parseByte(array[i]);
        }
        return result;
    }
}
