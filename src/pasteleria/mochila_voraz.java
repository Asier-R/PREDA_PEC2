package pasteleria;

import java.io.*;
import java.nio.file.FileSystemException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Clase principal del programa, donde se solicitan los datos de entrada al usuario y se ejecuta el algoritmo voraz que
 * resuelve el problema de la mochila para objetos fraccionables.
 * UNED PREDA 2022/2023 - PEC1 - Oracle OpenJDK version 19.
 * @author Asier Rodríguez López
 * @version 1.0
 * @since 1.0
 */
public class mochila_voraz {
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
     * Clase que se utilizará de soporte para los datos de entrada.
     */
    static Mochila mochila;

    /**
     * Método principal del programa y que recibe los argumentos de inicio de programa.
     * @see mochila_voraz#mostrarAyuda
     * @see mochila_voraz#sonArgumentosValidos
     * @see mochila_voraz#sonValidosDatosFichero
     * @see mochila_voraz#esEntradaPorTecladoValida
     * @see mochila_voraz#leerFichero
     * @see mochila_voraz#escribirFichero
     * @see mochila_voraz#gestionarMensajeError
     * @see Mochila
     * @see Mochila.ResultadoMochila
     * @see Mochila#mochilaObjetosFraccionables
     * @param args argumentos de inicio de programa.
     */
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

            //Validaciones de datos de mochila
            if (existeFicheroEntrada) {
                //Lectura y validación de la entrada
                sonValidosDatosFichero(leerFichero(ficheroEntrada));
            } else {
                //Se solicita al usuario que introduzca la entrada por teclado
                esEntradaPorTecladoValida();
            }

            //Se resuelve el problema de la mochila con objetos fraccionable
            Mochila.ResultadoMochila[] resultado = mochila.mochilaObjetosFraccionables(mochila);

            //Salida de datos
            StringBuilder salida = new StringBuilder();
            for (Mochila.ResultadoMochila res : resultado)
                if(res.peso != 0) salida.append(res).append("\n");

            salida.append(mochila.getBeneficioObtenido());

            if(!existeFicheroSalida) System.out.println("\nSYSTEM: resultado\n"+salida);
            else escribirFichero(salida.toString());

        } catch (Exception iae) {
            gestionarMensajeError(iae);
        }

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
     * Recibe los argumentos de inicio de programa y devuelve true si están correctamente introducidos. También se
     * encarga de activar las trazas, mostrar el mensaje de ayuda y de gestionar los ficheros de entrada y salida.
     * @see mochilaVoraz.mochila_voraz#trazasActivadas
     * @see mochilaVoraz.mochila_voraz#mostrarAyuda
     * @see mochilaVoraz.mochila_voraz#esValidoArgumento
     * @see mochilaVoraz.mochila_voraz#esValidoArgumentoFichero
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
     * Evalúa el argumento de inicio y devuelve true si es correcto. Si el argumento es un fichero, validará este. Será
     * eliminado en la próxima versión.
     * @see mochilaVoraz.mochila_voraz#trazasActivadas
     * @see mochilaVoraz.mochila_voraz#mostrarAyuda
     * @see mochilaVoraz.mochila_voraz#esValidoArgumentoFichero
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
     * @see mochilaVoraz.mochila_voraz#trazasActivadas
     * @see mochilaVoraz.mochila_voraz#mostrarAyuda
     * @see mochilaVoraz.mochila_voraz#validarFichero
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
     * @see Mochila
     * @param datos datos del fichero.
     * @throws FileSystemException cuando los datos no tienen el formato correcto.
     */
    private static void sonValidosDatosFichero(String datos) throws FileSystemException {
        trazar("SYSTEM: inicio de la validación de los datos.",false);

        //Estructura del fichero
        Pattern pattern = Pattern.compile("^[0-9]+\\s+(?:.+)[0-9]+(?:\\.[0-9]+)?$", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(datos);

        if(matcher.find())
            trazar("SYSTEM: el fichero está correctamente estructurado.",false);
        else
            throw new FileSystemException("""
                    ERROR: el fichero no está correctamente estructurado.
                    El número de objetos del conjunto debe estar en la primera linea del fichero y ser un número entero.
                    La capacidad de la mochila debe estar en la última linea del fichero.
                    Los objetos deben indicarse a partir de la primer linea del fichero. Cada linea tendrá el peso, un espacio y el beneficio.
                    El separador decimal debe ser un punto.
                    """);

        String[] arrayDatos = datos.split("\n");

        //Número de objetos
        pattern = Pattern.compile("^[0-9]+$", Pattern.MULTILINE);
        matcher = pattern.matcher(arrayDatos[0]);

        if(matcher.find())
            trazar("SYSTEM: el número de objetos es "+arrayDatos[0]+".",false);
        else
            throw new FileSystemException("ERROR: no se ha introducido correctamente el número de objetos => "+arrayDatos[0]);

        //Capacidad de la mochila
        pattern = Pattern.compile("^[0-9]+(?:\\.[0-9]+)?$", Pattern.MULTILINE);
        matcher = pattern.matcher(arrayDatos[arrayDatos.length-1]);

        if(matcher.find())
            trazar("SYSTEM: la capacidad de la mochila es "+arrayDatos[arrayDatos.length-1]+".",false);
        else
            throw new FileSystemException("ERROR: no se ha introducido correctamente la capacidad de la mochila => "+arrayDatos[arrayDatos.length-1]);

        //El número de objetos coincide con lo indicado en la primera línea
        if(arrayDatos.length-2 == Integer.parseInt(arrayDatos[0]))
            trazar("SYSTEM: el número de objetos es coherente con lo indicado.",false);
        else
            throw new FileSystemException("ERROR: el número de objetos no cuadra con lo indicado => indicado:"+arrayDatos[0]+" <> recuento:"+(arrayDatos.length-2));

        //Datos mochila
        int cantidadObjetos    = arrayDatos.length-2;
        float capacidadMochila = Float.parseFloat(arrayDatos[arrayDatos.length-1]);
        float[] pesos          = new float[cantidadObjetos];
        float[] beneficios     = new float[cantidadObjetos];

        //Comprobar validez de objetos
        pattern = Pattern.compile("^([0-9]+(?:\\.[0-9]+)?) ([0-9]+(?:\\.[0-9]+)?)$", Pattern.MULTILINE);

        for(int i=1; i<=cantidadObjetos; i++){
            matcher = pattern.matcher(arrayDatos[i]);
            if(!matcher.find()) {
                throw new FileSystemException("ERROR: el objeto "+(arrayDatos[i])+" no tiene el formato correcto.");
            }
            else {
                String[] grupo = matcher.group().split(" ");
                pesos[i-1]      = Float.parseFloat(grupo[0]);
                beneficios[i-1] = Float.parseFloat(grupo[1]);
            }
        }

        //Inicializar mochila
        mochila = new Mochila(cantidadObjetos, pesos, beneficios, capacidadMochila);

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
     * escribirá en un nuevo fichero de salida llamado "salida_mochila_voraz.txt".
     * @param salida datos a escribir en el fichero de salida.
     * @throws IOException cuando no es posible escribir en el fichero.
     */
    private static void escribirFichero(String salida) throws IOException {
        String path;
        FileOutputStream fos;

        if(existeFicheroSalida)
            path = ficheroSalida;
        else
            path = System.getProperty("user.dir")+"\\"+"salida_mochila_voraz.txt";

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
     * @see mochila_voraz#decidirSiFinalizarEjecucion
     * @see Mochila
     * @see Mochila#getCantidadObjetos
     * @see Mochila#getPesosBeneficios
     * @see Mochila#getCapacidad
     * @throws IOException cuando se da un error en la entrada de datos.
     */
    private static void esEntradaPorTecladoValida() throws IOException{

        Scanner entrada = new Scanner(System.in);

        System.out.println("\nSYSTEM: inicio entrada por teclado...");

        boolean entradaErronea = true;
        int cantidad    = 0;
        float capacidad = 0;
        float[] pesos;
        float[] beneficios;

        //Cantidad de tipo de objetos
        while(entradaErronea) {
            try {
                System.out.println("SYSTEM: introduzca la cantidad de objetos.");
                cantidad = entrada.nextInt();
                if(cantidad <= 0) throw new Exception("ERROR: no ha introducido un número entero mayor a cero.");
                entradaErronea = false;
                System.out.println("SYSTEM: cantidad de objetos => "+cantidad);
                entrada.nextLine();
            } catch (Exception e) {
                decidirSiFinalizarEjecucion(entrada, e);
            }
        }

        pesos      = new float[cantidad];
        beneficios = new float[cantidad];

        int i = 0;

        //Objetos posibles
        while(i < cantidad){
            try {
                System.out.println(("SYSTEM: introduzca el peso del objeto ("+(i+1)+"/"+cantidad+")"));
                float peso = entrada.nextFloat();
                if(peso <= 0) throw new Exception("ERROR: peso menor o igual a cero.");
                pesos[i] = peso;
                System.out.println(("SYSTEM: introduzca el beneficio del objeto ("+(i+1)+"/"+cantidad+")"));
                float beneficio = entrada.nextFloat();
                if(beneficio < 0) throw new Exception("ERROR: beneficio menor a cero.");
                beneficios[i] = beneficio;
                System.out.println("SYSTEM: se ha introducido un objeto con peso "+peso+" y beneficio "+beneficio+" ("+(i+1)+"/"+cantidad+")");
                i++;
                entrada.nextLine();
            } catch (Exception e) {
                decidirSiFinalizarEjecucion(entrada, e);
            }
        }

        entradaErronea = true;

        //Capacidad mochila
        while(entradaErronea){
            try {
                System.out.println("SYSTEM: introduce la capacidad de la mochila.");
                float cap = entrada.nextFloat();
                if(cap <= 0) throw new Exception("ERROR: no ha introducido un número entero mayor a cero.");
                capacidad= cap;
                entradaErronea = false;
                System.out.println("SYSTEM: la capacidad de la mochila es => "+capacidad);
            } catch (Exception e) {
                decidirSiFinalizarEjecucion(entrada, e);
            }
        }

        //Inicializamos la mochila
        mochila = new Mochila(cantidad, pesos, beneficios, capacidad);

        trazar("SYSTEM: los datos de la mochila son => ",false);
        trazar("objetos: "+mochila.getCantidadObjetos(),false);
        for (int e=0; e<mochila.getCantidadObjetos(); e++)
            trazar("peso: "+mochila.getPesosBeneficios()[e].peso+" beneficio: "+mochila.getPesosBeneficios()[e].beneficio,false);
        trazar("capacidad: "+mochila.getCapacidad(),false);

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
     * Método encargado de mostrar trazas si estas han sido activadas previamente.
     * @param traza mensaje de texto a trazar.
     * @param esError indica si es un mensaje de error.
     */
    static void trazar(String traza, Boolean esError){
        if(trazasActivas || esError) System.out.println(traza);
    }

}

/**
 * Clase creada exprofeso para ser el soporte de los datos de entrada.
 * UNED PREDA 2022/2023 - PEC1 - Oracle OpenJDK version 19.
 * @author Asier Rodríguez López
 * @version 1.0
 * @since 1.0
 */
class Mochila {
    /**
     * Cantidad de objetos existentes.
     */
    private final int cantidadObjetos;

    /**
     * Soporte de los datos de los objetos.
     */
    private final PesoBeneficio[] pesosBeneficios;

    /**
     * Capacidad de la mochila.
     */
    private final float capacidad;

    /**
     * Beneficio obtenido después de aplicar el algoritmo de selección de objetos.
     */
    private float beneficioObtenido = 0f;

    /**
     * Constructor encargado de informar las variables con los datos obtenidos en la entrada.
     * @see Mochila.PesoBeneficio
     * @param cantidad_de_objetos cantidad de objetos disponibles.
     * @param pesos_de_objetos pesos de cada objeto, donde el objeto de peso pesos_de_objetos[i] tiene el beneficio
     *                         beneficios_de_objetos[i].
     * @param beneficios_de_objetos beneficio de cada objeto, donde el objeto de beneficio beneficios_de_objetos[i]
     *                              tiene el pesos_de_objetos[i].
     * @param capacidad_de_mochila capacidad de la mochila.
     */
    public Mochila(int cantidad_de_objetos, float[] pesos_de_objetos, float[] beneficios_de_objetos, float capacidad_de_mochila) {
        this.cantidadObjetos = cantidad_de_objetos;
        this.capacidad       = capacidad_de_mochila;

        this.pesosBeneficios = new PesoBeneficio[cantidad_de_objetos];

        for(int i=0; i<pesos_de_objetos.length; i++)
            this.pesosBeneficios[i] = new PesoBeneficio(pesos_de_objetos[i], beneficios_de_objetos[i]);
    }

    /**
     * Algoritmo voraz que selecciona los objetos y su fracción a partir de los datos de la mochila, obteniendo el
     * máximo beneficio por unidad de peso.
     * @see mochila_voraz
     * @see mochila_voraz#trazar
     * @see Mochila.ResultadoMochila
     * @see Mochila.PesoBeneficio
     * @see Monticulo
     * @see Monticulo#heapSort
     * @param mochila mochila con los datos entre los que se hará la selección.
     * @return resultado final del algoritmo.
     */
    public ResultadoMochila[] mochilaObjetosFraccionables(Mochila mochila){

            mochila_voraz.trazar("SYSTEM: inicio del algoritmo.",false);

            PesoBeneficio[] mchPB = mochila.getPesosBeneficios();
            PesoBeneficio pb;
            int contador         = 0;
            float peso           = 0;
            float capacidad      = mochila.getCapacidad();
            float beneficioTotal = 0;

            ResultadoMochila[] res = new ResultadoMochila[this.cantidadObjetos];

            // Se inicia montículo con una referencia al tipo de datos.
            mochila_voraz.trazar("SYSTEM: se inicia montículo.",false);
            Monticulo<Mochila.PesoBeneficio> mont = new Monticulo<>(mchPB[0]);

            // Se ordenan los objetos en orden decreciente
            mochila_voraz.trazar("SYSTEM: se ordenan los objetos introducidos por "+(mochila_voraz.existeFicheroSalida?"fichero.":"teclado."),false);
            mont.heapSort(mchPB);

            // Se inicia a ceros el resultado
            mochila_voraz.trazar("SYSTEM: se inicia a ceros el resultado.",false);
            for(int i=0; i<res.length; i++)
                res[i] = new ResultadoMochila(0,0,0);

            // Bucle principal
            mochila_voraz.trazar("SYSTEM: inicio de bucle principal.",false);
            while(peso < capacidad){
                pb = mont.obtenerCima(mchPB);
                if(pb == null) break;
                mochila_voraz.trazar("SYSTEM: iteración "+contador+" -> se obtiene de la cima del montículo el objeto de peso => "+pb.peso,false);
                if( (peso + pb.peso) < capacidad ){
                    mochila_voraz.trazar("SYSTEM: máximo de capacidad no alcanzado, objeto no fraccionado.",false);
                    res[contador] = new ResultadoMochila(pb.peso, 1, pb.beneficio);
                    peso += pb.peso;
                    beneficioTotal += pb.beneficio;
                }
                else{
                    mochila_voraz.trazar("SYSTEM: máximo de capacidad alcanzado, objeto fraccionado.",false);
                    res[contador] = new ResultadoMochila(pb.peso, ((capacidad-peso)/pb.peso), (((capacidad-peso)/pb.peso))*pb.beneficio);
                    peso = capacidad;
                    beneficioTotal += res[contador].beneficio;
                }

                mochila_voraz.trazar("SYSTEM: se incorpora a la solución el objeto => peso:"+res[contador].peso+"  fracción:"+res[contador].fraccion+"  beneficio:"+res[contador].beneficio,false);
                mochila_voraz.trazar("SYSTEM: peso actualizado a => "+peso+"    beneficio total => "+beneficioTotal,false);

                contador++;
            }

            mochila.beneficioObtenido = beneficioTotal;
            return res;
    }

    /**
     * Sub-Clase creada exprofeso para ser el soporte del resultado de la selección de un objeto por el algoritmo aplicado
     * a la mochila.
     * @author Asier Rodríguez López
     * @version 1.0
     * @since 1.0
     */
    class ResultadoMochila{
        /**
         * Objeto seleccionado por el algoritmo.
         */
        float peso;

        /**
         * Fracción del objeto seleccionado.
         */
        float fraccion;

        /**
         * Beneficio resultante de la fracción del objeto seleccionado.
         */
        float beneficio;

        /**
         * Constructor encargado de asignar los valores al resultado asociado a un objeto.
         * @param peso Objeto seleccionado. Los objetos se identifican por su peso.
         * @param fraccion la fracción del objeto que se almacena en la mochila.
         * @param beneficio el beneficio obtenido en función de la fracción del objeto.
         */
        public ResultadoMochila(float peso, float fraccion, float beneficio){
            this.peso      = peso;
            this.fraccion  = fraccion;
            this.beneficio = beneficio;
        }

        /**
         * Método que devuelve un literal con el peso, la fracción y el beneficio, del resultado, concatenados.
         * @return literal con el peso, la fracción y el beneficio separados por un espacio.
         */
        @Override
        public String toString(){
            return this.peso+" "+this.fraccion+" "+this.beneficio;
        }

    }

    /**
     * Sub-Clase utilizada como soporte de los datos y atributos de los objetos disponibles para introducir en la mochila.
     * @author Asier Rodríguez López
     * @version 1.0
     * @since 1.0
     */
    class PesoBeneficio implements Comparable<PesoBeneficio>{
        /**
         * Peso del objeto.
         */
        float peso;

        /**
         * Beneficio del objeto.
         */
        float beneficio;

        /**
         * Beneficio por unidad de peso.
         */
        float ratio;

        /**
         * Constructor encargado de asignar los valores a un objeto.
         * @param peso peso del objeto.
         * @param beneficio beneficio del objeto.
         */
        public PesoBeneficio(float peso, float beneficio){
            this.peso      = peso;
            this.beneficio = beneficio;
            this.ratio     = beneficio/peso;
        }

        /**
         * Método encargado de comparar dos objetos distintos.
         * @param pb objeto a comparar.
         * @return número entero positivo si el objeto es mayor al comparado, cero si son iguales y negativo si es
         * menor.
         */
        @Override
        public int compareTo(PesoBeneficio pb) {
            float ratio = pb.beneficio/pb.peso;
            return Float.compare(this.ratio, ratio);
        }

        /**
         * Método que devuelve un literal con el peso y el beneficio, del objeto, concatenados.
         * @return literal con el peso y el beneficio separados por un espacio.
         */
        @Override
        public String toString(){
            return this.peso+" "+this.beneficio;
        }
    }

    /**
     * Devuelve todos los objetos introducidos.
     * @see Mochila.PesoBeneficio
     * @return objetos introducidos.
     */
    public PesoBeneficio[] getPesosBeneficios(){
        return this.pesosBeneficios;
    }

    /**
     * Devuelve la capacidad de la mochila.
     * @return capacidad de la mochila.
     */
    public float getCapacidad(){
        return this.capacidad;
    }

    /**
     * Devuelve la cantidad de objetos disponibles.
     * @return cantidad de objetos disponibles.
     */
    public int getCantidadObjetos(){
        return this.cantidadObjetos;
    }

    /**
     * Devuelve el beneficio obtenido después de aplicar el algoritmo de selección.
     * @return beneficio obtenido.
     */
    public float getBeneficioObtenido(){
        return this.beneficioObtenido;
    }

}