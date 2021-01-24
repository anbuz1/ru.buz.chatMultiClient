package sample;

import java.io.PrintWriter;

public class Writer extends Thread{


    private final PrintWriter wr;

    public Writer(PrintWriter wr) {

        this.wr = wr;
    }
}
