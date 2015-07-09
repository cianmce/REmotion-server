package com.ciancode;

public class Main {

    public static void main(String[] args) {

        System.out.println("Starting...");

        HTTPServer httpServer = new HTTPServer();
        try {
            httpServer.start();
        } catch (Exception e) {
            System.out.print("Error starting server... :(");
        }


        /*
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();
        for(GraphicsDevice curGs : gs)
        {
            DisplayMode dm = curGs.getDisplayMode();
            System.out.println(dm.getWidth() + " x " + dm.getHeight());
        }

        System.out.println("Starting...");
        Action action = new Action();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        byte[] input = new byte[3];
        input[0] = 0b01000000;
        input[1] = 54;
        input[2] = 1;
        //input[2] = 'j';
        action.set(input);

        action.print_info();
        action.execute();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        input[0] = 0b01000000;
        //action.set(input);
        action.execute();
        action.print_info();
        */

    }
}
