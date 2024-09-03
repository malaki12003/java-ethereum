package org.eth.common;

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CommonDebug {

    public static void report(Object... extra) {
        PrintStream err = System.err;
        err.println("You've encountered a sought after, hard to reproduce bug. Please report this to the developers <3 https://github.com/ethereum/go-ethereum/issues");
        err.println(extra);

        StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[1];
        err.printf("%s:%d\n", stackTraceElement.getFileName(), stackTraceElement.getLineNumber());

        try {
            Method printStackMethod = Thread.class.getDeclaredMethod("dumpStack");
            printStackMethod.setAccessible(true);
            printStackMethod.invoke(Thread.currentThread(), err);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace(err);
        }

        err.println("#### BUG! PLEASE REPORT ####");
    }

    public static void printDeprecationWarning(String str) {
        int lineLength = str.length() + 4;
        String line = String.format("%0" + lineLength + "d", 0).replace('0', '#');
        String emptyLine = String.format("%0" + lineLength + "d", 0).replace('0', ' ');
        System.out.printf(
                "\n%s\n# %s #\n# %s #\n# %s #\n%s\n",
                line, emptyLine, str, emptyLine, line);
    }
}