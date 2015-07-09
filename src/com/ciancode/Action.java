package com.ciancode;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by cian on 11/05/15.
 */
public class Action {
    private int     type = -1;
    private boolean is_hold;
    private boolean key_down;
    private int     strength;
    private int     code;

    // Codes
    static final int SPECIAL    = 0;
    static final int MOUSE      = 1;
    static final int ASCII      = 2;

    // Mouse Codes
    static final int MOUSE_UP    = 1;
    static final int MOUSE_LEFT  = 2;
    static final int MOUSE_RIGHT = 3;
    static final int MOUSE_DOWN  = 4;

    static final int MOUSE_MOVE_AMOUNT = 3;

    // Robot
    private static Robot robot;

    // Screen Dimensions
    private int screen_height;
    private int screen_width;

    public Action(){
        // Initialise everything else
        init_robot();

        init_screen_size();
    }

    public void set(byte[] input){
        // Check there is input or raise error
        if( input.length!=3 ) {
            throw new IllegalArgumentException("Invalid input size ["+input.length+"]");
        }

        // Get packets
        int[] packets = get_packets(input);

        // Get type
        type = get_type(packets);
        if( type<0 || type>2 ) {
            throw new IllegalArgumentException("Invalid input type ["+type+"]");
        }

        // Check if hold
        is_hold = get_hold(packets);

        // Check if key down
        if(is_hold){
            key_down = get_key_down(packets);
        }else{
            // Just so it has a value
            key_down = true;
        }

        // Gets strength
        strength = get_strength(packets);

        //get code
        code = get_code(packets);
    }

    private void init_robot(){
        if( robot!=null ){
            // Already initialised
            return;
        }
        try {
            robot = new Robot();
            robot.setAutoDelay(30);

        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    private void init_screen_size(){
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        screen_width  = gd.getDisplayMode().getWidth();
        screen_height = gd.getDisplayMode().getHeight();
    }

    private void charDown(char c) {
        if (Character.isUpperCase(c)) {
            robot.keyPress(KeyEvent.VK_SHIFT);
        }
        robot.keyPress(Character.toUpperCase(c));
    }

    private void charUp(char c) {
        robot.keyRelease(Character.toUpperCase(c));

        if (Character.isUpperCase(c)) {
            robot.keyRelease(KeyEvent.VK_SHIFT);
        }
    }

    public void press_ascii(char c){
        charDown(c);
        charUp(c);
    }
    public void press_ascii(int i){
        char c = (char)i;
        charDown(c);
        charUp(c);
    }
    public boolean press_special_key(String key){
        int code = get_special_keycode(key);
        if(code<0){
            return false;
        }
        robot.keyPress(code);
        robot.keyRelease(code);
        return true;
    }
    public int get_special_keycode(String key){
        try {
            return (int)KeyEvent.class.getDeclaredField("VK_"+key.toUpperCase()).get(null);
        } catch (IllegalAccessException e) {
            return -1;
        } catch (NoSuchFieldException e) {
            return -1;
        }
    }

    public void execute(){
        if( type==SPECIAL ) {
            System.out.println("Type:       " + "SPECIAL");
        }
        else if( type==MOUSE ) {
            execute_mouse();
        }
        else if( type==ASCII ) {
            execute_ascii();
        }
        else {
            System.out.println("Type:       " + "UNKNOWN");
        }
    }

    private void execute_mouse(){
        // Get current mouse pos
        Point currentLocation = MouseInfo.getPointerInfo().getLocation();
        System.out.println("\nCurrentLoc: "+currentLocation.x+", "+currentLocation.y);
        switch ( code ){
            case MOUSE_UP:
                currentLocation.y -= MOUSE_MOVE_AMOUNT;
                robot.mouseMove(currentLocation.x, currentLocation.y);
                robot.mouseMove(screen_width/2, screen_height/2);
                break;
            case MOUSE_LEFT:
                currentLocation.x -= MOUSE_MOVE_AMOUNT;
                robot.mouseMove(currentLocation.x, currentLocation.y);
                break;
            case MOUSE_RIGHT:
                currentLocation.x += MOUSE_MOVE_AMOUNT;
                robot.mouseMove(currentLocation.x, currentLocation.y);
                break;
            case MOUSE_DOWN:
                currentLocation.y += MOUSE_MOVE_AMOUNT;
                robot.mouseMove(currentLocation.x, currentLocation.y);
                break;
            default:
                System.out.println("Invalid Mouse Movement ["+code+"]");
        }
    }



    private void execute_ascii(){
        char character = (char) code;

        if(!is_hold) {
            charDown(character);
            charUp(character);
        }else if(key_down){
            charDown(character);
        }else{
            charUp(character);
        }
    }

    public void print_info(){
        System.out.println();
        System.out.println("Screen:     "+screen_width+"x"+screen_height);
        if( type==-1 ){
            System.out.println("Not initialised");
        }
        if( type==SPECIAL ) {
            System.out.println("Type:       " + "SPECIAL");
        }
        else if( type==MOUSE ) {
            System.out.println("Type:       " + "MOUSE");
        }
        else if( type==ASCII ) {
            System.out.println("Type:       " + "ASCII");
        }
        else {
            System.out.println("Type:       " + "UNKNOWN");
        }
        System.out.println("Is Hold:    "+is_hold);
        System.out.println("Key Down:   "+key_down);
        System.out.println("Strength:   "+strength);
        System.out.println("Code:       "+code);
    }

    private int get_code(int[] packets){
        return packets[2];
    }

    private int get_strength(int[] packets){
        return packets[1];
    }

    private boolean get_key_down(int[] packets){
        return (packets[0] & 0b00010000) != 0 ;
    }

    private boolean get_hold(int[] packets){
        return (packets[0] & 0b00100000) != 0 ;
    }

    private int get_type(int[] packets){
        // Check if special
        if( (packets[0] & 0b10000000) != 0 ){
            return SPECIAL;
        }
        // Check if Mouse
        if( (packets[0] & 0b01000000) != 0 ){
            return MOUSE;
        }
        return ASCII;
    }

    private int[] get_packets(byte[] input){
        int length = input.length;
        int[] packets = new int[length];

        for(int i=0; i<length; i++){
            packets[i] = (int) input[i];
        }
        return packets;
    }
}
