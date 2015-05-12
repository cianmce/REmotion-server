package com.ciancode;

import java.awt.*;

public class Main {

    public static void main(String[] args) {

        Monitors monitors = new Monitors();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();
        int num_screens = gs.length;

        for( int i=0; i<num_screens; i++ ){
            GraphicsDevice gd = gs[i];
            Rectangle screen = gd.getDefaultConfiguration().getBounds();

            String name = gd.getIDstring();
            System.out.println(name + ": " + screen.getWidth() + " * " + screen.getHeight() + " @ x = " + screen.getX());
            System.out.println();
        }

        System.out.println("Starting...");
        Action action = new Action();
        /*
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
        //*/

    }
}
