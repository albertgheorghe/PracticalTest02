package ro.pub.cs.systems.eim.practicaltest02;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class ServerThread extends Thread {
    private int port = 0;
    private ServerSocket serverSocket = null;

    public ServerThread(int port) {
        this.port = port;
        try {
            this.serverSocket = new ServerSocket(Constants.PORT);
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
    }

    HashMap<String, String> map = new HashMap<>();

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    @Override
    public void run() {
        try {

            while (!Thread.currentThread().isInterrupted()) {
                Log.i(Constants.TAG, "[SERVER THREAD] Waiting for a client invocation...");
                Socket socket = serverSocket.accept();
                Log.i(Constants.TAG, "[SERVER THREAD] A connection request was received from " + socket.getInetAddress() + ":" + socket.getLocalPort());
                BufferedReader reader = Utilities.getReader(socket);
                PrintWriter writer = Utilities.getWriter(socket);

                String type = reader.readLine();
                if (type.compareTo(Constants.SET) == 0) {
                    String ore = reader.readLine();
                    String minute = reader.readLine();
                    if (map.containsKey(socket.getRemoteSocketAddress().toString().split(":")[0].substring(1))) {
                        map.remove(socket.getRemoteSocketAddress().toString().split(":")[0].substring(1));
                    }
                    map.put(socket.getRemoteSocketAddress().toString().split(":")[0].substring(1), ore + " " + minute);
                    writer.println("Set");
                } else if (type.compareTo(Constants.RESET) == 0) {
                    if (map.containsKey(socket.getRemoteSocketAddress().toString().split(":")[0].substring(1))) {
                        map.remove(socket.getRemoteSocketAddress().toString().split(":")[0].substring(1));
                    }
                    writer.println("Reset");
                } else if (type.compareTo(Constants.POLL) == 0) {
                    Log.v(Constants.TAG,"[SERVER THREAD] POLL");
                    Socket serviceSocket = new Socket(Constants.WEB_SERVICE_ADDRESS, Constants.PORT);
                    BufferedReader serviceReader = Utilities.getReader(serviceSocket);
                    String time = serviceReader.readLine();
                    while (time == null || time.isEmpty()) {
                        time = serviceReader.readLine();
                    }
                    serviceSocket.close();
                    Date date = new SimpleDateFormat("yy-MM-dd HH:mm:ss").parse(time.substring(5, 22));

                    if (!map.containsKey(socket.getRemoteSocketAddress().toString().split(":")[0].substring(1))) {
                        writer.println("none");
                        Log.v(Constants.TAG,"[SERVER THREAD] None");
                    } else {
                        String timee = map.get(socket.getRemoteSocketAddress().toString().split(":")[0].substring(1));

                        if (date.getHours() > Integer.parseInt(timee.split(" ")[0]) || (date.getHours() == Integer.parseInt(timee.split(" ")[0])
                                && date.getMinutes() > Integer.parseInt(timee.split(" ")[1]))) {
                            writer.println("active");
                            Log.v(Constants.TAG,"[SERVER THREAD] Activa");

                        } else {
                            writer.println("inactive");
                            Log.v(Constants.TAG,"[SERVER THREAD] Inactiva");

                        }
                    }

                }
            }
        } catch (IOException | ParseException ioException) {
            Log.e(Constants.TAG, "[SERVER THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
    }

    public void stopThread() {
        interrupt();
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException ioException) {
                Log.e(Constants.TAG, "[SERVER THREAD] An exception has occurred: " + ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            }
        }
    }
}
