package com.example.lib;

class Test {


    public static void main(String[] args) {
        System.out.println("Hello, lets start testing!");

        ThreadedMainServer mainServ = new ThreadedMainServer();
        ThreadedAreaComputer areacomp = new ThreadedAreaComputer();

        Sensor sens1 =new Sensor();
        Sensor sens2 =new Sensor();
        Sensor sens3 =new Sensor();
        Sensor sens4 =new Sensor();

        Driver driv1 = new Driver();
        Driver driv2 = new Driver();
        Driver driv3 = new Driver();
        Driver driv4 = new Driver();

        mainServ.start();
        areacomp.start();

        sens1.start();
        sens2.start();
        sens3.start();
        sens4.start();

        driv1.start();
        driv2.start();
        driv3.start();
        driv4.start();


    }
}