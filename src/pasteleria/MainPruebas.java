package pasteleria;

import java.lang.reflect.Array;
import java.nio.file.FileSystemException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainPruebas {
    public static void main(String[] args) {

        try {
            String fichero = "C:\\Users\\Monst\\Desktop\\UNED\\Asignaturas\\2022-2023\\Programacion y Estructuras de Datos Avanzadas\\PRACTICA 1\\PREDA_PEC1\\FICHEROS\\ficheroPruebas.txt";
            String ficheroSal = "C:\\Users\\Monst\\Desktop\\UNED\\Asignaturas\\2022-2023\\Programacion y Estructuras de Datos Avanzadas\\PRACTICA 1\\PREDA_PEC1\\FICHEROS\\salidaPrueba.txt";
            String ficheroSal2 = "C:\\Users\\Monst\\Desktop\\UNED\\Asignaturas\\2022-2023\\Programacion y Estructuras de Datos Avanzadas\\PRACTICA 1\\PREDA_PEC1\\FICHEROS\\basura.txt";

            //Pasteleria.main(new String[] {"-t",fichero,ficheroSal});

            String patata    = "1-2-3";
            Arrays.asList(patata.split("-")).forEach( (n) -> System.out.println(n)   );


        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }


    }

}
