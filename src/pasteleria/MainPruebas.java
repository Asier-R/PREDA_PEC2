package pasteleria;

public class MainPruebas {
    public static void main(String[] args) {
        /*System.out.println(System.getProperty("user.home"));
        String archivo = System.getProperty("user.dir")+"\\FICHEROS\\"+"ficheroPruebas.txt";
        File fichero = new File(archivo);

        pruebas_de_ficheros(fichero);
        validarDatos(leerFichero(fichero));*/

        /*String fichero = "C:\\Users\\Monst\\Desktop\\UNED\\Asignaturas\\2022-2023\\Programacion y Estructuras de Datos Avanzadas\\PRACTICA 1\\PREDA_PEC1\\FICHEROS\\ficheroPruebas.txt";

        ArrayList<String[]> casosPrueba = new ArrayList<>(100);

        casosPrueba.add(new String[] {fichero,fichero,"-t","-t"});
        casosPrueba.add(new String[] {fichero,fichero,fichero,"-t"});
        casosPrueba.add(new String[] {fichero,fichero,"-h",fichero});
        casosPrueba.add(new String[] {fichero,fichero,fichero,fichero});

        casosPrueba.add(new String[] {"-t",fichero,"-t","-t"});
        casosPrueba.add(new String[] {fichero,"-h","-t","-t"});
        casosPrueba.add(new String[] {"-t","-h","-t","-t"});

        casosPrueba.forEach(caso -> {
            System.out.println("CASO: "+ Arrays.stream(caso).toList().toString());
            mochilaVoraz.mochila_voraz.main(caso);
            mochilaVoraz.mochila_voraz.existeFicheroEntrada = false;
            mochilaVoraz.mochila_voraz.existeFicheroSalida = false;
            mochilaVoraz.mochila_voraz.trazasActivas = false;
            mochilaVoraz.mochila_voraz.FINDEPROGAMA = false;
            System.out.println("-----------------------------------------------------------------------------------");
        });*/

        //mochilaVoraz.mochila_voraz.entradaPorTeclado();

        //String[] sp = ("10\n10 43\n2 2.3\n20").split("\n");
        //for(int i=0; i<sp.length; i++)
        //    System.out.println(sp[i]);

        //float p = Float.parseFloat("8.2");
        //System.out.println(p);

        try {
            String fichero = "C:\\Users\\Monst\\Desktop\\UNED\\Asignaturas\\2022-2023\\Programacion y Estructuras de Datos Avanzadas\\PRACTICA 1\\PREDA_PEC1\\FICHEROS\\ficheroPruebas.txt";
            String ficheroSal = "C:\\Users\\Monst\\Desktop\\UNED\\Asignaturas\\2022-2023\\Programacion y Estructuras de Datos Avanzadas\\PRACTICA 1\\PREDA_PEC1\\FICHEROS\\salidaPrueba.txt";
            String ficheroSal2 = "C:\\Users\\Monst\\Desktop\\UNED\\Asignaturas\\2022-2023\\Programacion y Estructuras de Datos Avanzadas\\PRACTICA 1\\PREDA_PEC1\\FICHEROS\\basura.txt";
            //mochila_voraz.main(new String[] {"-t","-h",fichero,ficheroSal});
            mochila_voraz.main(new String[] {"-t",fichero,ficheroSal});
            //mochila_voraz.main(new String[] {"-h",fichero});
        }catch(Exception e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }


        /*float[] pesos      = new float[]{2,2,4,5,1};
        float[] beneficios = new float[]{2,2,4,5,1};
        int cantidad       = 5;
        float capacidad    = 20;

        mochilaVoraz.Mochila mochi = new mochilaVoraz.Mochila(cantidad, pesos, beneficios, capacidad);
        mochilaVoraz.Monticulo<mochilaVoraz.Mochila.PesoBeneficio> monte = new mochilaVoraz.Monticulo(mochi.getPesosBeneficios()[0]);
        System.out.println(Arrays.toString(mochi.getPesosBeneficios()));
        monte.heapShort(mochi.getPesosBeneficios());
        System.out.println(Arrays.toString(mochi.getPesosBeneficios()));*/



    }

}
