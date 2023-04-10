package it.polimi.ingsw.view;

import java.io.OutputStream;
import java.io.PrintStream;

public class MyPrintStream extends PrintStream {
    public MyPrintStream() {
        super(System.out);
    }

    @Override
    public void println(String x) {
        super.println(x);
        System.out.flush();
    }
}
