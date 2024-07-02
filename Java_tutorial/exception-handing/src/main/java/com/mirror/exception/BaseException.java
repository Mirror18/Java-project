package com.mirror.exception;

/**
 * @author mirror
 */
public class BaseException extends RuntimeException{
    public BaseException(){
        super();
    }
    public BaseException(String message,Throwable cause){
        super(message,cause);
    }

    public BaseException(String message){
        super(message);
    }
    public BaseException(Throwable cause){
        super(cause);
    }

    /*
    应该要提供多个构造方法
     */
}
/*
Exception
│
├─ RuntimeException
│  │
│  ├─ NullPointerException
│  │
│  ├─ IndexOutOfBoundsException
│  │
│  ├─ SecurityException
│  │
│  └─ IllegalArgumentException
│     │
│     └─ NumberFormatException
│
├─ IOException
│  │
│  ├─ UnsupportedCharsetException
│  │
│  ├─ FileNotFoundException
│  │
│  └─ SocketException
│
├─ ParseException
│
├─ GeneralSecurityException
│
├─ SQLException
│
└─ TimeoutException
 */
