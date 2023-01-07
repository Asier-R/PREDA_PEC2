package pasteleria;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class MainPruebas {
    public static void main(String[] args) {

        try {
            String entrada = "C:\\Users\\Monst\\Desktop\\UNED\\Asignaturas\\2022-2023\\Programacion y Estructuras de Datos Avanzadas\\PRACTICA 2\\PREDA_PEC2\\FICHEROS\\ficheroPruebas.txt";
            String salida = "C:\\Users\\Monst\\Desktop\\UNED\\Asignaturas\\2022-2023\\Programacion y Estructuras de Datos Avanzadas\\PRACTICA 2\\PREDA_PEC2\\FICHEROS\\salidaPrueba.txt";

            ArrayList<String[]> casosPrueba = new ArrayList<>(100);

            //casosPrueba.add(new String[] {"-t",entrada,salida});
            casosPrueba.add(new String[] {entrada,salida});

            casosPrueba.forEach(caso -> {
                //System.out.println("CASO: "+ Arrays.stream(caso).toList().toString());
                System.out.println("TIEMPO-INI: "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new java.util.Date()));
                pasteleria.Pasteleria.main(caso);
                System.out.println("TIEMPO-FIN: "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new java.util.Date()));
                pasteleria.Pasteleria.existeFicheroEntrada = false;
                pasteleria.Pasteleria.existeFicheroSalida = false;
                pasteleria.Pasteleria.trazasActivas = false;
                System.out.println("-----------------------------------------------------------------------------------");
            });

        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }


    }

}

/*
ArrayList<Integer[]> pruebas = new ArrayList(120);
            pruebas.add(new Integer[]{1,2,3,4,5});
            pruebas.add(new Integer[]{2,1,3,4,5});
            pruebas.add(new Integer[]{3,1,2,4,5});
            pruebas.add(new Integer[]{1,3,2,4,5});
            pruebas.add(new Integer[]{2,3,1,4,5});
            pruebas.add(new Integer[]{3,2,1,4,5});
            pruebas.add(new Integer[]{3,2,4,1,5});
            pruebas.add(new Integer[]{2,3,4,1,5});
            pruebas.add(new Integer[]{4,3,2,1,5});
            pruebas.add(new Integer[]{3,4,2,1,5});
            pruebas.add(new Integer[]{2,4,3,1,5});
            pruebas.add(new Integer[]{4,2,3,1,5});
            pruebas.add(new Integer[]{4,1,3,2,5});
            pruebas.add(new Integer[]{1,4,3,2,5});
            pruebas.add(new Integer[]{3,4,1,2,5});
            pruebas.add(new Integer[]{4,3,1,2,5});
            pruebas.add(new Integer[]{1,3,4,2,5});
            pruebas.add(new Integer[]{3,1,4,2,5});
            pruebas.add(new Integer[]{2,1,4,3,5});
            pruebas.add(new Integer[]{1,2,4,3,5});
            pruebas.add(new Integer[]{4,2,1,3,5});
            pruebas.add(new Integer[]{2,4,1,3,5});
            pruebas.add(new Integer[]{1,4,2,3,5});
            pruebas.add(new Integer[]{4,1,2,3,5});
            pruebas.add(new Integer[]{5,1,2,3,4});
            pruebas.add(new Integer[]{1,5,2,3,4});
            pruebas.add(new Integer[]{2,5,1,3,4});
            pruebas.add(new Integer[]{5,2,1,3,4});
            pruebas.add(new Integer[]{1,2,5,3,4});
            pruebas.add(new Integer[]{2,1,5,3,4});
            pruebas.add(new Integer[]{2,1,3,5,4});
            pruebas.add(new Integer[]{1,2,3,5,4});
            pruebas.add(new Integer[]{3,2,1,5,4});
            pruebas.add(new Integer[]{2,3,1,5,4});
            pruebas.add(new Integer[]{1,3,2,5,4});
            pruebas.add(new Integer[]{3,1,2,5,4});
            pruebas.add(new Integer[]{3,5,2,1,4});
            pruebas.add(new Integer[]{5,3,2,1,4});
            pruebas.add(new Integer[]{2,3,5,1,4});
            pruebas.add(new Integer[]{3,2,5,1,4});
            pruebas.add(new Integer[]{5,2,3,1,4});
            pruebas.add(new Integer[]{2,5,3,1,4});
            pruebas.add(new Integer[]{1,5,3,2,4});
            pruebas.add(new Integer[]{5,1,3,2,4});
            pruebas.add(new Integer[]{3,1,5,2,4});
            pruebas.add(new Integer[]{1,3,5,2,4});
            pruebas.add(new Integer[]{5,3,1,2,4});
            pruebas.add(new Integer[]{3,5,1,2,4});
            pruebas.add(new Integer[]{4,5,1,2,3});
            pruebas.add(new Integer[]{5,4,1,2,3});
            pruebas.add(new Integer[]{1,4,5,2,3});
            pruebas.add(new Integer[]{4,1,5,2,3});
            pruebas.add(new Integer[]{5,1,4,2,3});
            pruebas.add(new Integer[]{1,5,4,2,3});
            pruebas.add(new Integer[]{1,5,2,4,3});
            pruebas.add(new Integer[]{5,1,2,4,3});
            pruebas.add(new Integer[]{2,1,5,4,3});
            pruebas.add(new Integer[]{1,2,5,4,3});
            pruebas.add(new Integer[]{5,2,1,4,3});
            pruebas.add(new Integer[]{2,5,1,4,3});
            pruebas.add(new Integer[]{2,4,1,5,3});
            pruebas.add(new Integer[]{4,2,1,5,3});
            pruebas.add(new Integer[]{1,2,4,5,3});
            pruebas.add(new Integer[]{2,1,4,5,3});
            pruebas.add(new Integer[]{4,1,2,5,3});
            pruebas.add(new Integer[]{1,4,2,5,3});
            pruebas.add(new Integer[]{5,4,2,1,3});
            pruebas.add(new Integer[]{4,5,2,1,3});
            pruebas.add(new Integer[]{2,5,4,1,3});
            pruebas.add(new Integer[]{5,2,4,1,3});
            pruebas.add(new Integer[]{4,2,5,1,3});
            pruebas.add(new Integer[]{2,4,5,1,3});
            pruebas.add(new Integer[]{3,4,5,1,2});
            pruebas.add(new Integer[]{4,3,5,1,2});
            pruebas.add(new Integer[]{5,3,4,1,2});
            pruebas.add(new Integer[]{3,5,4,1,2});
            pruebas.add(new Integer[]{4,5,3,1,2});
            pruebas.add(new Integer[]{5,4,3,1,2});
            pruebas.add(new Integer[]{5,4,1,3,2});
            pruebas.add(new Integer[]{4,5,1,3,2});
            pruebas.add(new Integer[]{1,5,4,3,2});
            pruebas.add(new Integer[]{5,1,4,3,2});
            pruebas.add(new Integer[]{4,1,5,3,2});
            pruebas.add(new Integer[]{1,4,5,3,2});
            pruebas.add(new Integer[]{1,3,5,4,2});
            pruebas.add(new Integer[]{3,1,5,4,2});
            pruebas.add(new Integer[]{5,1,3,4,2});
            pruebas.add(new Integer[]{1,5,3,4,2});
            pruebas.add(new Integer[]{3,5,1,4,2});
            pruebas.add(new Integer[]{5,3,1,4,2});
            pruebas.add(new Integer[]{4,3,1,5,2});
            pruebas.add(new Integer[]{3,4,1,5,2});
            pruebas.add(new Integer[]{1,4,3,5,2});
            pruebas.add(new Integer[]{4,1,3,5,2});
            pruebas.add(new Integer[]{3,1,4,5,2});
            pruebas.add(new Integer[]{1,3,4,5,2});
            pruebas.add(new Integer[]{2,3,4,5,1});
            pruebas.add(new Integer[]{3,2,4,5,1});
            pruebas.add(new Integer[]{4,2,3,5,1});
            pruebas.add(new Integer[]{2,4,3,5,1});
            pruebas.add(new Integer[]{3,4,2,5,1});
            pruebas.add(new Integer[]{4,3,2,5,1});
            pruebas.add(new Integer[]{4,3,5,2,1});
            pruebas.add(new Integer[]{3,4,5,2,1});
            pruebas.add(new Integer[]{5,4,3,2,1});
            pruebas.add(new Integer[]{4,5,3,2,1});
            pruebas.add(new Integer[]{3,5,4,2,1});
            pruebas.add(new Integer[]{5,3,4,2,1});
            pruebas.add(new Integer[]{5,2,4,3,1});
            pruebas.add(new Integer[]{2,5,4,3,1});
            pruebas.add(new Integer[]{4,5,2,3,1});
            pruebas.add(new Integer[]{5,4,2,3,1});
            pruebas.add(new Integer[]{2,4,5,3,1});
            pruebas.add(new Integer[]{4,2,5,3,1});
            pruebas.add(new Integer[]{3,2,5,4,1});
            pruebas.add(new Integer[]{2,3,5,4,1});
            pruebas.add(new Integer[]{5,3,2,4,1});
            pruebas.add(new Integer[]{3,5,2,4,1});
            pruebas.add(new Integer[]{2,5,3,4,1});
            pruebas.add(new Integer[]{5,2,3,4,1});
            float coste = 0f;
            float min = 25f;
            int[] pedido = new int[]{1,1,3,2,1};
            for (Integer[] prueba: pruebas) {
                for (int i=0; i<pedido.length;i++){
                    coste += tablaDeCostes[prueba[i]-1][pedido[i]-1];
                }
                //if(coste<=min) {
                  //  min = coste;
                    System.out.println(""+Arrays.toString(prueba)+"    coste="+coste);
                //}
                coste=0;
            }

            System.out.println("\n\n-----------------------");
*/