package dev.demo.basic.file;

/**
 * File Demo Exception
 *
 */
public class FileDemoException extends Throwable {
    String msg;

    public FileDemoException(String msg) {
        this.msg = msg;
    }

}
