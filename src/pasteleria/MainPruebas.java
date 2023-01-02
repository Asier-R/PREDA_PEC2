package pasteleria;


import java.util.ArrayList;

public class MainPruebas {
    public static void main(String[] args) {

        try {
            String fichero = "C:\\Users\\Monst\\Desktop\\UNED\\Asignaturas\\2022-2023\\Programacion y Estructuras de Datos Avanzadas\\PRACTICA 2\\PREDA_PEC2\\FICHEROS\\ficheroPruebas.txt";
            String ficheroSal = "C:\\Users\\Monst\\Desktop\\UNED\\Asignaturas\\2022-2023\\Programacion y Estructuras de Datos Avanzadas\\PRACTICA 2\\PREDA_PEC2\\FICHEROS\\salidaPrueba.txt";

            Pasteleria.main(new String[] {"-t","-h",fichero,ficheroSal});

        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }


    }

}
