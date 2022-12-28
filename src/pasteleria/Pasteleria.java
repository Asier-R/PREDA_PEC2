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
     * Tabla de costes pastel-pastelero
     */
    static float[][] tablaDeCostes;

    /**
     * Lista de pedidos
     */
    static int[] pedidos;

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

            //Se resuelve el problema de la mochila con objetos fraccionable
            //Mochila.ResultadoMochila[] resultado = mochila.mochilaObjetosFraccionables(mochila);

            //Salida de datos
            //StringBuilder salida = new StringBuilder();
            //for (Mochila.ResultadoMochila res : resultado)
            //    if(res.peso != 0) salida.append(res).append("\n");

            //salida.append(mochila.getBeneficioObtenido());

            //if(!existeFicheroSalida) System.out.println("\nSYSTEM: resultado\n"+salida);
            //else escribirFichero(salida.toString());
            System.out.println("FIIIIIIIIIIIIIN");
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
                        \nSINTAXIS: mochila_voraz [-t] [-h] [fichero_entrada] [fichero_salida]
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
        Pattern pattern = Pattern.compile("^(?:[1-9]+)\\n(?:[1-9]+)\\n(?:[1-9]+(?:-[1-9]+)*)\\n(?:(?:[0-9]+(?:\\.[0-9]+)?)+(?: (?:[0-9]+(?:\\.[0-9]+)?)+)*\\n?)+$", Pattern.MULTILINE);
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
        pattern = Pattern.compile("^[1-9]+$");
        matcher = pattern.matcher(arrayDatos[0]);

        if(matcher.find())
            trazar("SYSTEM: el número de pasteleros es "+arrayDatos[0]+".",false);
        else
            throw new FileSystemException("ERROR: no se ha introducido correctamente el número de pasteleros => "+arrayDatos[0]);

        numPasteleros = Integer.parseInt(arrayDatos[0]);

        //Número de tipos de pasteles
        pattern = Pattern.compile("^[1-9]+$");
        matcher = pattern.matcher(arrayDatos[1]);

        if(matcher.find())
            trazar("SYSTEM: el número de tipos de pasteles es "+arrayDatos[1]+".",false);
        else
            throw new FileSystemException("ERROR: no se ha introducido correctamente el número de tipos de pasteles => "+arrayDatos[1]);

        tiposDePasteles = Integer.parseInt(arrayDatos[1]);

        //Datos de los pasteleros
        if(arrayDatos.length-3 != numPasteleros)
            throw new FileSystemException("ERROR: no se ha introducido correctamente los datos de los pasteleros, debería haber solo "+numPasteleros+" lineas => "+arrayDatos[0]);

        pattern = Pattern.compile("^(?:[0-9]+(?:\\.[0-9]+)?)+(?: (?:[0-9]+(?:\\.[0-9]+)?)+)*$");
        tablaCostes = new float[numPasteleros][tiposDePasteles];

        for (int i=3; i<arrayDatos.length; i++) {
            matcher = pattern.matcher(arrayDatos[i]);

            if(!matcher.find())
                throw new FileSystemException("ERROR: la estructura de los datos de costes del pastelero "+(i-2)+" son erróneos => "+arrayDatos[i]);

            String[] costes = arrayDatos[i].split(" ");

            if(costes.length > tiposDePasteles)
                throw new FileSystemException("ERROR: los costes por pastel del pastelero "+(i-2)+" contiene mas datos que tipos de pasteles => "+arrayDatos[i]);

            for (int k=0; k<costes.length; k++)
                tablaCostes[i-3][k] = Float.parseFloat(costes[k]);
        }

        tablaDeCostes = tablaCostes;

        trazar("SYSTEM: los datos de los pasteleros son correctos.",false);

        //Pedidos
        pattern = Pattern.compile("^[1-9]+(?:-[1-9]+)*$");
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
        datos += ("Salida producto de la ejecución de mochila_voraz:\n");
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
                    System.out.println("SYSTEM: se ha introducido el coste "+coste+" para el paste "+(k+1)+" del pastelero "+(i+1));
                    k++;
                    entrada.nextLine();
                }
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
     * Cálculo de la estimación optimista partiendo del coste de los pedidos ya asignados.
     * @param tabla_costes tabla de costes de la elaboración de cada pastel por cada pastelero.
     * @param pedidos lista de los pedidos.
     * @param num_nodo número del nodo a partir del cual calcular la estimación.
     * @param coste coste de los pedidos ya asignados.
     * @return
     */
    private static float estimacionOpt(float[][] tabla_costes, int[] pedidos, int num_nodo, float coste){

        float estimacion = coste, menorCoste;

        for(int i=num_nodo+1; i<pedidos.length; i++){
            menorCoste = tabla_costes[0][pedidos[i]];

            for(int j=2; i<pedidos.length; i++)
                if(menorCoste > tabla_costes[j][pedidos[i]])
                    menorCoste = tabla_costes[j][pedidos[i]];

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
     * @return
     */
    private static float estimacionPes(float[][] tabla_costes, int[] pedidos, int num_nodo, float coste){

        float estimacion = coste, mayorCoste;

        for(int i=num_nodo+1; i<pedidos.length; i++){
            mayorCoste = tabla_costes[0][pedidos[i]];

            for(int j=2; i<pedidos.length; i++)
                if(mayorCoste < tabla_costes[j][pedidos[i]])
                    mayorCoste = tabla_costes[j][pedidos[i]];

            estimacion += mayorCoste;
        }

        return estimacion;
    }

}

/**
 * Clase creada exprofeso para ser un nodo en el árbol implícito del algoritmo de ramificación y poda que resuelve el
 * problema de la pastelería.
 * UNED PREDA 2022/2023 - PEC2 - Oracle OpenJDK version 19.
 * @author Asier Rodríguez López
 * @version 1.0
 * @since 1.0
 */
class Nodo {

}