package pasteleria;


import java.util.ArrayList;

public class MainPruebas {
    public static void main(String[] args) {

        try {
            String fichero = "C:\\Users\\Monst\\Desktop\\UNED\\Asignaturas\\2022-2023\\Programacion y Estructuras de Datos Avanzadas\\PRACTICA 2\\PREDA_PEC2\\FICHEROS\\ficheroPruebas.txt";
            String ficheroSal = "C:\\Users\\Monst\\Desktop\\UNED\\Asignaturas\\2022-2023\\Programacion y Estructuras de Datos Avanzadas\\PRACTICA 2\\PREDA_PEC2\\FICHEROS\\salidaPrueba.txt";

            Pasteleria.main(new String[] {"-t","-h",fichero,ficheroSal});

            /*
            ArrayList<Integer> lista = new ArrayList<>();
            lista.add(1);
            lista.add(2);
            lista.add(3);
            lista.add(5);
            lista.add(6);
            lista.add(4);

            String datos = "";
            System.out.println("INICIAL: ");
            System.out.println(lista);

            Monticulo<Integer> mont = new Monticulo<Integer>(lista);
            mont.heapSort(lista);

            System.out.println("FINAL: ");
            System.out.println(lista);

            mont.insertar(22,lista);
            System.out.println("FINAL+1: ");
            System.out.println(lista);

            mont.insertar(0,lista);
            System.out.println("FINAL+2: ");
            System.out.println(lista);

            mont.heapSort(lista);
            System.out.println("ORDEN: ");
            System.out.println(lista);
            */

        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }


    }

}
