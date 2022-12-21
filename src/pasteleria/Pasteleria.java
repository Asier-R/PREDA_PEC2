package pasteleria;

import java.io.File;
import java.nio.file.FileSystemException;
import java.util.Locale;
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

    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);

        try {
            //Validaciones de argumentos de entrada
            if (args.length > 4) {
                mostrarAyuda();
                throw new IllegalArgumentException("ERROR: ha introducido " + (args.length - 4) + " argumentos más de los permitidos.");
            }
            if (args.length > 0) {
                //if (!sonArgumentosValidos(args)) throw new IllegalArgumentException("ERROR: argumentos de entrada no válidos.");
            }

            if (!existeFicheroEntrada)
                trazar("SYSTEM: No se ha especificado fichero de entrada...se solicitarán los datos por entrada de teclado.",false);
            if (!existeFicheroSalida)
                trazar("SYSTEM: No se ha especificado fichero de salida...el resultado se mostrará por la consola.",false);

            //Validaciones de datos de mochila
            if (existeFicheroEntrada) {
                //Lectura y validación de la entrada
                //sonValidosDatosFichero(leerFichero(ficheroEntrada));
            } else {
                //Se solicita al usuario que introduzca la entrada por teclado
                //esEntradaPorTecladoValida();
            }

            //Se resuelve el problema de la mochila con objetos fraccionable
            //Mochila.ResultadoMochila[] resultado = mochila.mochilaObjetosFraccionables(mochila);

            //Salida de datos
            StringBuilder salida = new StringBuilder();
            //for (Mochila.ResultadoMochila res : resultado)
            //    if(res.peso != 0) salida.append(res).append("\n");

            //salida.append(mochila.getBeneficioObtenido());

            if(!existeFicheroSalida) System.out.println("\nSYSTEM: resultado\n"+salida);
            //else escribirFichero(salida.toString());

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

        //Estructura del fichero
        Pattern pattern = Pattern.compile("^(?:[1-9]+)\\s(?:[1-9]+)\\s(?:[1-9]+(?:-[1-9]+)*)\\s(?:(?:[0-9]+(?:\\.[0-9]+)?)+(?: (?:[0-9]+(?:\\.[0-9]+)?)+)*\\s?)+$", Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(datos);

        if(matcher.find())
            trazar("SYSTEM: el fichero está correctamente estructurado.",false);
        else
            throw new FileSystemException("""
                    ERROR: el fichero no está correctamente estructurado.
                    La primera linea del fichero corresponde al número de cocineros de la pastelería.
                    La segunda linea del fichero corresponde al número de tipos de pasteles.
                    La tercera linea del fichero corresponde al los pedidos realizados, siendo de un máximo igual al 
                    número de cocineros indicados en la primera linea, y los tipos un número igual o menor al número de 
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

        //Número de tipos de pasteles
        pattern = Pattern.compile("^[1-9]+$");
        matcher = pattern.matcher(arrayDatos[1]);

        if(matcher.find())
            trazar("SYSTEM: el número de tipos de pasteles es "+arrayDatos[1]+".",false);
        else
            throw new FileSystemException("ERROR: no se ha introducido correctamente el número de tipos de pasteles => "+arrayDatos[1]);

        //Datos de los pasteleros
        if(arrayDatos.length-3 != Integer.parseInt(arrayDatos[0]))
            throw new FileSystemException("ERROR: no se ha introducido correctamente el número de pasteleros => "+arrayDatos[0]);

        pattern = Pattern.compile("^(?:[0-9]+(?:\\.[0-9]+)?)+(?: (?:[0-9]+(?:\\.[0-9]+)?)+)*$");

        for (int i=3; i<arrayDatos.length; i++) {
            matcher = pattern.matcher(arrayDatos[i]);

            if(!matcher.find())
                throw new FileSystemException("ERROR: la estructura de los datos del pastelero "+(i-2)+" son erróneos => "+arrayDatos[i]);

            String[] pastelero = arrayDatos[i].split(" ");

            if(pastelero.length > Integer.parseInt(arrayDatos[1]))
                throw new FileSystemException("ERROR: la estructura de los datos del pastelero "+(i-2)+" contiene mas datos que tipos de pasteles => "+arrayDatos[i]);
        }

        //Pedido
        pattern = Pattern.compile("^[1-9]+(?:-[1-9]+)*$");
        matcher = pattern.matcher(arrayDatos[2]);

        if(matcher.find())
            trazar("SYSTEM: el pedido es "+arrayDatos[2]+".",false);
        else
            throw new FileSystemException("ERROR: el pedido no está correctamente estructurado => "+arrayDatos[2]);

        String[] pedidos = arrayDatos[2].split("-");

        if(pedidos.length > Integer.parseInt(arrayDatos[0]))
            throw new FileSystemException("ERROR: el número de pedidos ("+ pedidos.length+") es superior al número de pasteleros ("+arrayDatos[0]+"");

        for (String pedido: pedidos)
            if(Integer.parseInt(pedido) > Integer.parseInt(arrayDatos[1]))
                throw new FileSystemException("ERROR: se ha incluido entre los pedidos un tipo de pastel ("+pedido+") no existente.");


        /*
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
        */
        trazar("SYSTEM: datos correctos. Fin de validación de los datos.\n",false);
    }

}
