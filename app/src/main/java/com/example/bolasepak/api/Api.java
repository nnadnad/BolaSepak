package com.example.bolasepak.api;

import java.net.MalformedURLException;
import java.net.URL;

public class Api {
    public String request(String urlReq) throws MalformedURLException {
        URL url = new URL(urlReq);
        return url.toString();
    }
}
