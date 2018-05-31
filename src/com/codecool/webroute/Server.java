package com.codecool.webroute;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;

public class Server {

    public static void main(String[] args) throws IOException {
        Class<ConnectionHandler> handlerClass = ConnectionHandler.class;
        HttpServer server = HttpServer.create(new InetSocketAddress(8888), 0);
        for (Method method: handlerClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(WebRoute.class)) {
                Annotation annotation = method.getAnnotation(WebRoute.class);
                WebRoute webRoute = (WebRoute) annotation;
                server.createContext(webRoute.path(), new ConnectionHandler(webRoute.path()));
            }
        }
        server.setExecutor(null);
        server.start();
    }
}
