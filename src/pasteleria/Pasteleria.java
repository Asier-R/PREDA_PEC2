package pasteleria;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Clase principal del programa, donde se solicitan los datos de entrada al usuario y se ejecuta el algoritmo de
 * ramificación y poda que resuelve el problema de la pastelería.
 * UNED PREDA 2022/2023 - PEC2 - Oracle OpenJDK version 19.
 * @author Asier Rodríguez López
 * @version 1.0
 * @since 1.0
 */
public class Pasteleria {
    /**
     * True si se ha indicado fichero de entrada en los argumentos de inicio del programa.
     */
    static boolean existeFicheroEntrada = false;

    /**
     * Ruta del fichero de entrada indicada en los argumentos de inicio del programa.
     */
    static String ficheroEntrada = "";

    /**
     * True si se ha indicado fichero de salida en los argumentos de inicio del programa.
     */
    static boolean existeFicheroSalida = false;

    /**
     * Ruta del fichero de salida indicada en los argumentos de inicio del programa.
     */
    static String ficheroSalida = "";

    /**
     * Indica si se han activado las trazas.
     */
    static boolean trazasActivas = false;

    /**
     * Tabla de costes de elaboración de cada pastel por cada pastelero.
     */
    static float[][] tablaDeCostes;

    /**
     * Lista de pedidos.
     */
    static int[] pedidos;

    /**
     * Solución producida por el algoritmo de ramificación y poda.
     */
    static int[] pasteleros_sol;

    /**
     * Coste total de la solución producida por el algoritmo de ramificación y poda.
     */
    static float costeT_sol;

    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);

        try {
            //Validaciones de argumentos de entrada
            if (args.length > 4) {
                mostrarAyuda();
                throw new IllegalArgumentException("ERROR: ha introducido " + (args.length - 4) + " argumentos más de los permitidos.");
            }
            if (args.length > 0) {
                if (!sonArgumentosValidos(args)) throw new IllegalArgumentException("ERROR: argumentos de entrada no válidos.");
            }

            if (!existeFicheroEntrada)
                trazar("SYSTEM: No se ha especificado fichero de entrada...se solicitarán los datos por entrada de teclado.",false);
            if (!existeFicheroSalida)
                trazar("SYSTEM: No se ha especificado fichero de salida...el resultado se mostrará por la consola.",false);

            //Validaciones de datos de pastelería
            if (existeFicheroEntrada) {
                //Lectura y validación de la entrada
                sonValidosDatosFichero(leerFichero(ficheroEntrada));
            } else {
                //Se solicita al usuario que introduzca la entrada por teclado
                esEntradaPorTecladoValida();
            }

            pasteleros_sol = new int[0];
            costeT_sol  = 0f;

            asignaPasteleros(tablaDeCostes,pedidos);

            String salida = "";
            for (int i=0; i<pasteleros_sol.length; i++) {
                salida += (pasteleros_sol[i]+1)+(i==pasteleros_sol.length-1?"\n":"-");
            }
            salida += costeT_sol;

            if(!existeFicheroSalida) System.out.println("\nSYSTEM: resultado\n"+salida);
            else escribirFichero(salida);

        } catch (Exception iae) {
            gestionarMensajeError(iae);
        }
    }

    /**
     * Método encargado de mostrar trazas si estas han sido activadas previamente.
     * @param traza mensaje de texto a trazar.
     * @param esError indica si es un mensaje de error.
     */
    static void trazar(String traza, Boolean esError){
        if(trazasActivas || esError) System.out.println(traza);
    }

    /**
     * Método encargado de gestionar el mensaje de error de las excepciones.
     * @param e excepción a gestionar.
     * @param <Excep> clase de tipo excepción a gestionar.
     */
    private static <Excep extends Exception> void gestionarMensajeError(Excep e){
        trazar(e.getMessage().startsWith("ERROR: ")?e.getMessage()+"\n":"ERROR: error inesperado => "+e.getMessage()+"\n",true);
    }

    /**
     * Muestra el mensaje de trazas activadas.
     */
    private static void trazasActivadas(){
        trazasActivas = true;
        trazar("SYSTEM: se han activado las trazas.",false);
    }

    /**
     * Muestra el mensaje de ayuda.
     */
    private static void mostrarAyuda(){
        String h = """    
                        \nSINTAXIS: pasteleria [-t] [-h] [fichero_entrada] [fichero_salida]
                                -t: traza cada paso de manera que se describa la aplicación del algoritmo utilizado.
                                -h: muestra una ayuda y la sintaxis del comando.
                   fichero_entrada: es el nombre del fichero del que se leen los datos de entrada.
                    fichero_salida: es el nombre del fichero que se creará para almacenar la salida.\n
                    """;
        System.out.println(h);
    }

    /**
     * Recibe los argumentos de inicio de programa y devuelve true si están correctamente introducidos. También se
     * encarga de activar las trazas, mostrar el mensaje de ayuda y de gestionar los ficheros de entrada y salida.
     * @param args argumentos de inicio de programa.
     * @return devuelve true si los argumentos son correctos.
     */
    private static boolean sonArgumentosValidos(String[] args){
        //Se utilizan 4 argumentos
        if(args.length == 4){
            //Primer argumento
            if(!esValidoArgumentoFichero(args[0], !existeFicheroEntrada)) return false;
            //Segundo argumento
            if(!esValidoArgumentoFichero(args[1], !existeFicheroEntrada)) return false;
            //Tercer argumento
            if(!esValidoArgumentoFichero(args[2], !existeFicheroEntrada)) return false;
            //Cuarto argumento
            return esValidoArgumentoFichero(args[3], !existeFicheroEntrada);
        }
        //Se utilizan 3 argumentos
        else if(args.length == 3){
            //Primer argumento
            if(!esValidoArgumento(args[0])) return false;
            //Segundo argumento
            switch (args[1]){
                case "-t":
                    trazasActivadas();
                    break;
                case "-h":
                    mostrarAyuda();
                    break;
                default:
                    if(!esValidoArgumentoFichero(args[1], !existeFicheroEntrada)) return false;
                    break;
            }
            //Tercer argumento
            return esValidoArgumentoFichero(args[2], !existeFicheroEntrada);
        }
        //Se utilizan 2 argumentos
        else if(args.length == 2){
            //Primer argumento
            switch (args[0]){
                case "-t":
                    trazasActivadas();
                    break;
                case "-h":
                    mostrarAyuda();
                    break;
                default:
                    if(!esValidoArgumentoFichero(args[0], true)) return false;
                    break;
            }
            //Segundo argumento
            if(args[1].equals("-t")) trazasActivadas();
            else if(args[1].equals("-h")) mostrarAyuda();
            else return esValidoArgumentoFichero(args[1], !existeFicheroEntrada);

            return true;
        }
        //Se utilizan 1 argumento
        else if(args.length == 1){
            switch (args[0]){
                case "-t":
                    trazasActivadas();
                    break;
                case "-h":
                    mostrarAyuda();
                    break;
                default:
                    return validarFichero(args[0], true);
            }
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Evalúa el argumento de inicio y devuelve true si es correcto. Si el argumento es un fichero, validará este. Será
     * eliminado en la próxima versión.
     * @see pasteleria.Pasteleria#trazasActivadas
     * @see pasteleria.Pasteleria#mostrarAyuda
     * @see pasteleria.Pasteleria#esValidoArgumentoFichero
     * @param arg argumento a validar.
     * @return true si el argumento es válido.
     */
    private static boolean esValidoArgumento(String arg){
        switch (arg){
            case "-t":
                trazasActivadas();
                return true;
            case "-h":
                mostrarAyuda();
                return true;
            default:
                return esValidoArgumentoFichero(arg, true);
        }
    }

    /**
     * Evalúa el argumento de inicio y devuelve true si es correcto. Si el argumento es un fichero, validará este.
     * @see pasteleria.Pasteleria#trazasActivadas
     * @see pasteleria.Pasteleria#mostrarAyuda
     * @see pasteleria.Pasteleria#validarFichero
     * @param arg arg argumento a validar.
     * @param esEntrada indica si el argumento es un fichero de entrada.
     * @return true si el argumento es válido.
     */
    private static boolean esValidoArgumentoFichero(String arg, Boolean esEntrada){
        if(arg.equals("-t")){
            trazasActivadas();
        }
        else if(arg.equals("-h")){
            mostrarAyuda();
        }
        else{
            return validarFichero(arg, esEntrada);
        }
        return true;
    }

    /**
     * Verifica que el fichero indicado en los argumentos de inicio de programa es válido. Se comprueba que existe, que
     * es realmente un fichero, que se puede escribir o leer (en función de esEntrada) e informa las variables
     * necesarias.
     * @param nombre_fichero ruta completa con nombre del fichero a validar.
     * @param esEntrada indica si el fichero es es de entrada.
     * @return true si el fichero es válido.
     */
    private static boolean validarFichero(String nombre_fichero, Boolean esEntrada){
        trazar("SYSTEM: se comprueba fichero "+(esEntrada?"de entrada":"de salida")+" "+nombre_fichero+".",false);
        File fichero = new File(nombre_fichero);
        //¿Existe fichero?
        if(!fichero.exists()) {
            trazar("ERROR: el fichero no existe.",true);
            return false;
        }
        //¿Es fichero válido?
        if(!fichero.isFile()) {
            trazar("ERROR: no es un fichero válido.",true);
            return false;
        }
        //¿Se puede leer?
        if(esEntrada && !fichero.canRead()) {
            trazar("ERROR: el fichero no se puede leer.",true);
            return false;
        }
        //¿Se puede escribir?
        if(!esEntrada && !fichero.canWrite()) {
            trazar("ERROR: no se puede escribir en el fichero.",true);
            return false;
        }
        if(esEntrada) {
            existeFicheroEntrada = true;
            ficheroEntrada = nombre_fichero;

        }
        else{
            existeFicheroSalida = true;
            ficheroSalida = nombre_fichero;
        }
        //Fichero válido
        trazar("SYSTEM: el fichero "+(esEntrada?"de entrada":"de salida")+" se puede procesar.\n",false);
        return true;
    }

    /**
     * Verifica que los datos del fichero se ajustan a las especificaciones del programa.
     * @param datos datos del fichero.
     * @throws FileSystemException cuando los datos no tienen el formato correcto.
     */
    private static void sonValidosDatosFichero(String datos) throws FileSystemException {
        trazar("SYSTEM: inicio de la validación de los datos.",false);

        int numPasteleros;
        int tiposDePasteles;
        float[][] tablaCostes;

        //Estructura del fichero
        Pattern pattern = Pattern.compile("^(?:[0-9]+)\\n(?:[0-9]+)\\n(?:[0-9]+(?:-[0-9]+)*)\\n(?:(?:[0-9]+(?:\\.[0-9]+)?)+(?: (?:[0-9]+(?:\\.[0-9]+)?)+)*\\n?)+$", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(datos);

        if(matcher.find())
            trazar("SYSTEM: el fichero está correctamente estructurado.",false);
        else
            throw new FileSystemException("""
                    ERROR: el fichero no está correctamente estructurado.
                    La primera linea del fichero corresponde al número de pasteleros de la pastelería.
                    La segunda linea del fichero corresponde al número de tipos de pasteles.
                    La tercera linea del fichero corresponde al los pedidos realizados, siendo de un máximo igual al 
                    número de pasteleros indicados en la primera linea, y los tipos un número igual o menor al número de 
                    tipos de pasteles indicados en la segunda linea.
                    """);

        String[] arrayDatos = datos.split("\n");

        //Número de pasteleros
        pattern = Pattern.compile("^[0-9]+$");
        matcher = pattern.matcher(arrayDatos[0]);

        if(matcher.find())
            trazar("SYSTEM: el número de pasteleros es "+arrayDatos[0]+".",false);
        else
            throw new FileSystemException("ERROR: no se ha introducido correctamente el número de pasteleros => "+arrayDatos[0]);

        numPasteleros = Integer.parseInt(arrayDatos[0]);

        //Número de tipos de pasteles
        pattern = Pattern.compile("^[0-9]+$");
        matcher = pattern.matcher(arrayDatos[1]);

        if(matcher.find())
            trazar("SYSTEM: el número de tipos de pasteles es "+arrayDatos[1]+".",false);
        else
            throw new FileSystemException("ERROR: no se ha introducido correctamente el número de tipos de pasteles => "+arrayDatos[1]);

        tiposDePasteles = Integer.parseInt(arrayDatos[1]);

        //Datos de los pasteleros
        if(arrayDatos.length-3 != numPasteleros)
            throw new FileSystemException("ERROR: no se ha introducido correctamente los datos de los pasteleros, debería haber "+numPasteleros+" lineas y hay "+(arrayDatos.length-3));

        pattern = Pattern.compile("^(?:[0-9]+(?:\\.[0-9]+)?)+(?: (?:[0-9]+(?:\\.[0-9]+)?)+)*$");
        tablaCostes = new float[numPasteleros][tiposDePasteles];

        for (int i=3; i<arrayDatos.length; i++) {
            matcher = pattern.matcher(arrayDatos[i]);

            if(!matcher.find())
                throw new FileSystemException("ERROR: la estructura de los datos de costes del pastelero "+(i-2)+" son erróneos => "+arrayDatos[i]);

            String[] costes = arrayDatos[i].split(" ");

            if(costes.length != tiposDePasteles)
                throw new FileSystemException("ERROR: los costes por pastel del pastelero "+(i-2)+" contiene "+( (costes.length > tiposDePasteles)?"mas":"menos" )+" datos que tipos de pasteles => "+arrayDatos[i]);

            for (int k=0; k<costes.length; k++)
                tablaCostes[i-3][k] = Float.parseFloat(costes[k]);
        }

        tablaDeCostes = tablaCostes;

        trazar("SYSTEM: los datos de los pasteleros son correctos.",false);

        //Pedidos
        pattern = Pattern.compile("^[0-9]+(?:-[0-9]+)*$");
        matcher = pattern.matcher(arrayDatos[2]);

        if(matcher.find())
            trazar("SYSTEM: el pedido es "+arrayDatos[2]+".",false);
        else
            throw new FileSystemException("ERROR: el pedido no está correctamente estructurado => "+arrayDatos[2]);

        String[] strPedidos = arrayDatos[2].split("-");
        pedidos = new int[strPedidos.length];

        if(pedidos.length > numPasteleros)
            throw new FileSystemException("ERROR: el número de pedidos ("+ pedidos.length+") es superior al número de pasteleros ("+numPasteleros+"");

        int i=0;
        int pedido;

        for (String tipoPast: strPedidos) {
            pedido = Integer.parseInt(tipoPast);

            if (pedido > tiposDePasteles)
                throw new FileSystemException("ERROR: se ha incluido entre los pedidos un tipo de pastel (" + tipoPast + ") no existente.");

            pedidos[i] = pedido;
            i++;
        }

        trazar("SYSTEM: datos correctos. Fin de validación de los datos.\n",false);
    }

    /**
     * Lee el fichero indicado en el parámetro del método y devuelve sus datos.
     * @param path ruta completa con nombre del fichero a leer.
     * @return datos del fichero leído.
     * @throws FileNotFoundException si el fichero no existe.
     */
    private static String leerFichero(String path) throws FileNotFoundException{
        //Obtenemos el fichero
        File fichero = new File(path);

        Scanner lector = new Scanner(fichero);
        StringBuilder datos = new StringBuilder();

        while(lector.hasNext())
            datos.append(lector.nextLine()).append("\n");

        trazar("SYSTEM: lectura de fichero de entrada => \n"+datos+"\n",false);

        return datos.toString();
    }

    /**
     * Escribe en el fichero indicado en los argumentos de inicio de programa. Si no existe el fichero se creará y se
     * escribirá en un nuevo fichero de salida llamado "salida_pasteleria.txt".
     * @param salida datos a escribir en el fichero de salida.
     * @throws IOException cuando no es posible escribir en el fichero.
     */
    private static void escribirFichero(String salida) throws IOException {
        String path;
        FileOutputStream fos;

        if(existeFicheroSalida)
            path = ficheroSalida;
        else
            path = System.getProperty("user.dir")+"\\"+"salida_pasteleria.txt";

        fos = new FileOutputStream(path,true);

        String datos = "";
        datos += ("\n.................... ");
        datos += (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
        datos += (" ....................\n");
        datos += ("Salida producto de la ejecución de pasteleria:\n");
        datos += (salida);

        fos.write(datos.getBytes());
        fos.close();
    }

    /**
     * Verifica que la entrada por teclado es válida.
     * @throws IOException cuando se da un error en la entrada de datos.
     */
    private static void esEntradaPorTecladoValida() throws IOException{

        Scanner entrada = new Scanner(System.in);

        System.out.println("\nSYSTEM: inicio entrada por teclado...");

        boolean entradaErronea = true;
        int numPasteleros    = 0;
        int tiposDePasteles  = 0;
        ArrayList<Integer> peds = new ArrayList<>();
        float[][] tablaCostes;

        //Número de pasteleros
        while(entradaErronea) {
            try {
                System.out.println("SYSTEM: introduzca el número de pasteleros que trabajan en la pastelería:");
                numPasteleros = entrada.nextInt();
                if(numPasteleros <= 0) throw new Exception("ERROR: no ha introducido un número entero mayor a cero.");
                entradaErronea = false;
                System.out.println("SYSTEM: número de pasteleros => "+numPasteleros);
                entrada.nextLine();
            } catch (Exception e) {
                decidirSiFinalizarEjecucion(entrada, e);
            }
        }

        entradaErronea = true;

        //Tipos de pasteles
        while(entradaErronea) {
            try {
                System.out.println("SYSTEM: introduzca el número de tipos de pasteles:");
                tiposDePasteles = entrada.nextInt();
                if(tiposDePasteles <= 0) throw new Exception("ERROR: no ha introducido un número entero mayor a cero.");
                entradaErronea = false;
                System.out.println("SYSTEM: número de tipos de pasteles => "+tiposDePasteles);
                entrada.nextLine();
            } catch (Exception e) {
                decidirSiFinalizarEjecucion(entrada, e);
            }
        }

        //Pedidos
        entradaErronea = true;
        String temp;

        while(entradaErronea) {
            try {
                System.out.println("SYSTEM: introduzca un pedido:");
                peds.add(entrada.nextInt());
                if(tiposDePasteles <= 0) throw new Exception("ERROR: no ha introducido un número entero mayor a cero.");
                System.out.println("SYSTEM: número de tipos de pasteles => "+tiposDePasteles);
                entrada.nextLine();
                System.out.println("SYSTEM: ¿desea introducir otro pedido? Pulse S");
                temp = entrada.nextLine();
                if(peds.size() == numPasteleros || !temp.equalsIgnoreCase("S")){
                    entradaErronea = false;
                    System.out.println("SYSTEM: ha alcanzado el máximo de pedido o no desea introducir mas pedidos.");
                }
            } catch (Exception e) {
                decidirSiFinalizarEjecucion(entrada, e);
            }
        }

        pedidos = new int[peds.size()];
        for(int i=0; i<pedidos.length; i++)
            pedidos[i] = peds.get(i);

        //Datos de los pasteleros
        int i = 0;
        int k = 0;
        tablaCostes = new float[numPasteleros][tiposDePasteles];

        while(i < numPasteleros){
            try {
                while(k < tiposDePasteles) {
                    System.out.println("SYSTEM: introduzca el coste de pastel "+(k+1)+" para el pastelero "+(i+1));
                    float coste = entrada.nextFloat();
                    if (coste <= 0) throw new Exception("ERROR: coste menor o igual a cero.");
                    tablaCostes[i][k] = coste;
                    System.out.println("SYSTEM: se ha introducido el coste "+coste+" para el pastel "+(k+1)+" del pastelero "+(i+1));
                    k++;
                    entrada.nextLine();
                }
                k=0;
                i++;
            } catch (Exception e) {
                decidirSiFinalizarEjecucion(entrada, e);
            }
        }

        tablaDeCostes = tablaCostes;

        trazar("SYSTEM: fin de entrada por teclado.\n",false);
    }

    /**
     * Método encargado de gestionar los errores y la finalización de la entrada por teclado.
     * @param entrada instancia de la clase Scanner que se está utilizando para obtener la entrada.
     * @param e la excepción a gestionar.
     * @throws IOException cuando se decide interrumpir la entrada por teclado.
     */
    private static void decidirSiFinalizarEjecucion(Scanner entrada, Exception e) throws IOException {
        entrada.nextLine();
        System.out.println((e.getMessage() == null || e.getMessage().contains("null"))?"ERROR: no ha introducido un número. Para introducir un decimal use el punto.":e.getMessage()+"\n");
        System.out.println("SYSTEM: si desea finalizar el programa escriba SI.");
        String opcion = entrada.nextLine();
        if(opcion.equalsIgnoreCase("SI")){
            entrada.close();
            throw new IOException("ERROR: se ha interrumpido la entrada de datos, se finaliza la ejecución del programa.\n");
        }
        entrada.nextLine();
    }

    /**
     * Crea un literal a partir del contenido de un array.
     * @param nodo array a partir del cual crear un literal.
     */
    private static String instantanea(Nodo nodo){
        if(!trazasActivas) return "No se traza";
        String datos = "pasteleros:{";
        for(int i=0; i<nodo.pasteleros.length; i++)
            datos += nodo.pasteleros[i]+(i==nodo.pasteleros.length-1?"":",");
        datos += "}  ";

        datos += "booAsignados:{";
        for(int i=0; i<nodo.booAsignados.length; i++)
            datos += nodo.booAsignados[i]+(i==nodo.booAsignados.length-1?"":",");
        datos += "}  ";

        datos += "costeTotal:"+nodo.costeTotal+"   estOpt:"+nodo.estOpt+"   numNodo:"+nodo.numNodo;

        return datos;
    }

    /**
     * Cálculo de la estimación optimista partiendo del coste de los pedidos ya asignados.
     * @param tabla_costes tabla de costes de la elaboración de cada pastel por cada pastelero.
     * @param pedidos lista de los pedidos.
     * @param num_nodo número del nodo a partir del cual calcular la estimación.
     * @param coste coste de los pedidos ya asignados.
     * @return estimación optimista en función de los parámetros de entrada.
     */
    private static float estimacionOpt(float[][] tabla_costes, int[] pedidos, int num_nodo, float coste){

        float estimacion = coste, menorCoste;

        for(int i=num_nodo+1; i<pedidos.length; i++){
            menorCoste = tabla_costes[0][pedidos[i]-1];

            for(int k=1; k<pedidos.length; k++)
                if (menorCoste > tabla_costes[k][pedidos[i] - 1])
                    menorCoste = tabla_costes[k][pedidos[i] - 1];

            estimacion += menorCoste;
        }

        return estimacion;
    }

    /**
     * Cálculo de la estimación pesimista partiendo del coste de los pedidos ya asignados.
     * @param tabla_costes tabla de costes de la elaboración de cada pastel por cada pastelero.
     * @param pedidos lista de los pedidos.
     * @param num_nodo número del nodo a partir del cual calcular la estimación.
     * @param coste coste de los pedidos ya asignados.
     * @return estimación pesimista en función de los parámetros de entrada.
     */
    private static float estimacionPes(float[][] tabla_costes, int[] pedidos, int num_nodo, float coste){

        float estimacion = coste, mayorCoste;

        for(int i=num_nodo+1; i<pedidos.length; i++){
            mayorCoste = tabla_costes[0][pedidos[i]-1];

            for(int k=1; k<pedidos.length; k++)
                if(mayorCoste < tabla_costes[k][pedidos[i]-1])
                    mayorCoste = tabla_costes[k][pedidos[i]-1];

            estimacion += mayorCoste;
        }

        return estimacion;
    }

    /**
     * Algoritmo de ramificación y poda para la asignación de pasteleros a cada pedido con coste mínimo.
     * @param tabla_costes tabla de costes de elaboración de cada pastel por cada pastelero.
     * @param pedidos lista de pedidos.
     * @return array solución del algoritmo de ramificación y poda, donde en la posición j del pedido está el pastelero i que lo elabora.
     */
    public static void asignaPasteleros(float[][] tabla_costes, int[] pedidos){
        trazar("SYSTEM: inicio de algoritmo de asignación de pasteleros a pedidos.",false);
        trazar("SYSTEM: se inician variables y el primer nodo.",false);
        Monticulo<Nodo> montC = new Monticulo<>();
        ArrayList<Nodo> monticulo = montC.getMonticulo();

        Nodo nodo = new Nodo(pedidos.length);
        Nodo hijo;
        float cota, estPes;

        montC.insertar(nodo,monticulo);
        nodo.numNodo = -1;
        trazar("SYSTEM: se ha insertado el primer nodo en el montículo",false);

        nodo.estOpt = estimacionOpt(tabla_costes,pedidos,nodo.numNodo,nodo.costeTotal);
        trazar("SYSTEM: la estimación optimista del primer nodo es: "+nodo.estOpt,false);

        cota = estimacionPes(tabla_costes,pedidos,nodo.numNodo,nodo.costeTotal);
        trazar("SYSTEM: la cota es: "+cota,false);

        while( (!montC.elMonticuloEstaVacio(monticulo))
                &&
                (montC.mostrarCima(monticulo).estOpt <= cota) )
        {

            trazar("\n\nSYSTEM: se generan los nodos para cada pastelero no asignado.",false);
            nodo = montC.obtenerCima(monticulo);
            trazar("SYSTEM: instantánea del primer nodo del montículo => "+instantanea(nodo),false);

            ArrayList<Nodo> nodos = new ArrayList<>();
            for(int i=0; i<nodo.booAsignados.length;i++){
                if(nodo.booAsignados[i]==false){
                    hijo              = new Nodo();
                    hijo.numNodo      = nodo.numNodo+1;
                    hijo.pasteleros   = nodo.pasteleros.clone();
                    hijo.booAsignados = nodo.booAsignados.clone();
                    nodos.add(hijo);
                }
            }

            int i=0;
            for(int n=0; n<nodos.size(); n++){
                hijo = nodos.get(n);
                trazar("\nSYSTEM: se examina el hijo ("+n+") => "+instantanea(hijo),false);

                while( i<hijo.booAsignados.length && true){
                    if(hijo.booAsignados[i]==false) break;
                    else i++;
                }

                trazar("SYSTEM: el pastelero "+i+" está disponible.",false);

                if(!hijo.booAsignados[i] ){
                    trazar("SYSTEM: se asigna el pastelero "+i+" al pedido "+hijo.numNodo,false);
                    hijo.pasteleros[hijo.numNodo] = i;
                    hijo.booAsignados[i]          = true;
                    hijo.costeTotal               = nodo.costeTotal + tabla_costes[i][pedidos[hijo.numNodo]-1];

                    if( hijo.numNodo == pedidos.length-1 ){
                        if( cota >= hijo.costeTotal ){
                            trazar("SYSTEM: cota:"+cota+" es "+( (cota> hijo.costeTotal)?"mayor que":"igual al" )+" costeTotal:"+hijo.costeTotal,false);
                            trazar("SYSTEM: se actualiza la solución, el coste y la cota.",false);
                            pasteleros_sol = hijo.pasteleros;
                            costeT_sol     = hijo.costeTotal;
                            cota           = costeT_sol;
                        }
                    }
                    else //Solución no completa
                    {
                        trazar("SYSTEM: solución no completa.",false);
                        hijo.estOpt = estimacionOpt(tabla_costes,pedidos,hijo.numNodo,hijo.costeTotal);
                        if(hijo.estOpt <= cota)montC.insertar(hijo, monticulo);
                        trazar("SYSTEM: la estimación optimista es " + hijo.estOpt + " y la cota es "+cota+". "+( (hijo.estOpt < cota)?"Se":"No se" )+" inserta el nodo en el montículo", false);
                        estPes = estimacionPes(tabla_costes, pedidos, hijo.numNodo, hijo.costeTotal);
                        trazar("SYSTEM: cota:" + cota + " " + ((cota > estPes) ? "" : "no") + " es mayor que la estPes:" + estPes, false);
                        if (cota > estPes)
                            cota = estPes;
                    }
                    trazar("SYSTEM: instantánea del nodo hijo => "+instantanea(hijo),false);
                }
                i++;
            }
        }
    }

}

/**
 * Clase creada exprofeso para ser un nodo en el árbol implícito del algoritmo de ramificación y poda que resuelve el
 * problema de la pastelería.
 * UNED PREDA 2022/2023 - PEC2 - Oracle OpenJDK version 19.
 * @author Asier Rodríguez López
 * @version 1.0
 * @since 2.0
 */
class Nodo implements Comparable<Nodo>{

    /**
     * Pasteleros asignados a cada pedido.
     */
    int[] pasteleros;

    /**
     * Pasteleros disponibles=F u ocupados=T
     */
    boolean[] booAsignados;

    /**
     * Número de nodo.
     */
    int numNodo;

    /**
     * Coste total en este nodo.
     */
    float costeTotal;

    /**
     * Estimación optimista en este nodo.
     */
    float estOpt;

    /**
     * Constructor vacío.
     */
    public Nodo(){
        this.numNodo      = 0;
        this.costeTotal   = 0f;
        this.estOpt       = 0f;
    }

    /**
     * Constructor que inicializa a ceros las variables de clase.
     * @param tamano_pedidos cantidad de pedidos realizados.
     */
    public Nodo(int tamano_pedidos){
        this.pasteleros   = new int[tamano_pedidos];
        this.booAsignados = new boolean[tamano_pedidos];
        this.numNodo      = 0;
        this.costeTotal   = 0f;
        this.estOpt       = 0f;
        for(int i=0; i<tamano_pedidos; i++){
            this.pasteleros[i]   = -1;
            this.booAsignados[i] = false;
        }
    }

    /**
     * Constructor encargado de informar los datos del nodo.
     * @param pasteleros pasteleros asignados a cada pedido.
     * @param booAsignados pasteleros disponibles=0 u ocupados=1.
     * @param numNodo número de nodo.
     * @param costeTotal coste total en este nodo.
     * @param estOpt estimación optimista en este nodo.
     */
    public Nodo(int[] pasteleros, boolean[] booAsignados, int numNodo, float costeTotal, float estOpt){
        this.pasteleros   = pasteleros;
        this.booAsignados = booAsignados;
        this.numNodo      = numNodo;
        this.costeTotal   = numNodo;
        this.estOpt       = estOpt;
    }

    /**
     * Clona el nodo.
     * @return clon del nodo.
     */
    public Nodo clonarNodo(){
        Nodo clon         = new Nodo();
        clon.pasteleros   = this.pasteleros.clone();
        clon.booAsignados = this.booAsignados.clone();
        clon.numNodo      = this.numNodo;
        clon.costeTotal   = this.costeTotal;
        clon.estOpt       = this.estOpt;
        return clon;
    }

    /**
     * Método encargado de comparar dos objetos distintos.
     * Se compara el parámetro de entrada contra el nodo que llama a la función para obtener un orden inverso en el
     * montículo (de máximos).
     * @param nodo objeto a comparar.
     * @return número entero positivo si el objeto es menor al comparado, cero si son iguales y positivo si es menor.
     */
    @Override
    public int compareTo(Nodo nodo) {
        return Float.compare(nodo.estOpt,this.estOpt);
    }
}