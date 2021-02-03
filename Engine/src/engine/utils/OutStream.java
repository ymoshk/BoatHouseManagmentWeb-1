package engine.utils;

import java.io.PrintStream;

public class OutStream {

    private static OutStream instance = null;
    private PrintStream stream;

    private OutStream() {
        this.stream = null;
    }

    private static OutStream getInstance() {
        if (instance == null) {
            instance = new OutStream();
        }
        return instance;
    }

    public static void log(String msg) {
        OutStream instance = getInstance();
        if (instance.stream != null) {
            instance.stream.print(msg);
        }
    }

    public static void setStream(PrintStream stream) {
        OutStream instance = getInstance();
        instance.stream = stream;
    }
}
