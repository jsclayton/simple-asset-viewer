package com.cheezburger.simple;

/**
 * Created by IntelliJ IDEA.
 * User: john
 * Date: 3/21/12
 * Time: 9:15 AM
 * To change this template use File | Settings | File Templates.
 */
public class CheezburgerException extends Exception {
    public CheezburgerException(String message, Throwable e)  {
        super(message, e);
    }
}
