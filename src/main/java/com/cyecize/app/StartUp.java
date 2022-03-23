package com.cyecize.app;

import com.cyecize.javache.embedded.JavacheEmbedded;
import com.cyecize.summer.DispatcherSolet;

public class StartUp extends DispatcherSolet {

    public StartUp() {

    }

    public static void main(String[] args) {
        JavacheEmbedded.startServer(8000, StartUp.class);
    }
}
