package com.cyecize.app;

import com.cyecize.javache.embedded.JavacheEmbedded;
import com.cyecize.summer.DispatcherSolet;
import com.cyecize.summer.SummerAppRunner;

public class StartUp extends DispatcherSolet {

    public StartUp() {
        SummerAppRunner.run(StartUp.class);
    }

    public static void main(String[] args) {
        JavacheEmbedded.startServer(8000, StartUp.class);
    }
}
