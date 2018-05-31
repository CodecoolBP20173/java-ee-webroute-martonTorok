package com.codecool.webroute;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ConnectionHandler implements HttpHandler {
    private String path;

    public ConnectionHandler() {
    }

    ConnectionHandler(String path) {
        this.path = path;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        Class<ConnectionHandler> handlerClass = ConnectionHandler.class;
        for (Method method: handlerClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(WebRoute.class)) {
                Annotation annotation = method.getAnnotation(WebRoute.class);
                WebRoute webRoute = (WebRoute) annotation;
                if (webRoute.path().equals(path)) {
                    try {
                        method.invoke(handlerClass.newInstance(), httpExchange);
                    } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @WebRoute(path = "/index")
    private void onIndex(HttpExchange httpExchange) throws IOException {
        String response = "This is America (uhm..index)";
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    @WebRoute
    private void onSlash(HttpExchange httpExchange) throws IOException {
        String response = "This is the main page";
        httpExchange.sendResponseHeaders(200, response.length());
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
