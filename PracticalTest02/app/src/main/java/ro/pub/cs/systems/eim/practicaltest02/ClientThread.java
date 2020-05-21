package ro.pub.cs.systems.eim.practicaltest02;

import android.os.AsyncTask;
import android.widget.TextView;

public class ClientThread {
    int port;
    String ore, minute, buttonType;
    TextView info;

    public ClientThread(int port, String ip, String ora, String minut, TextView info, String buttonType) {
        this.port = port;
        this.ore = ora;
        this.minute = minut;
        this.info = info;
        this.buttonType = buttonType;
    }

}
