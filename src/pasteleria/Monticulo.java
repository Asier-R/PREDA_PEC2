package pasteleria;

import java.lang.reflect.Array;

/**
 * Clase que representa la estructura de datos "Montículo de máximos".
 * UNED PREDA 2022/2023 - PEC2 - Oracle OpenJDK version 19.
 * @author Asier Rodríguez López
 * @version 1.0
 * @since 1.0
 * @param <T> Tipo de clase que se utilizará con el Montículo. Debe de ser de tipo Comparable.
 */
public class Monticulo <T extends Comparable<T>>{

    /**
     * Variable auxiliar utilizada para poder instanciar un array utilizando genéricos.
     */
    private final T clase;

    /**
     * Variable array que contendrá el montículo de tipo T.
     */
    private T[] vector;

    /**
     * Si no se dispone de un array se inicializa la clase y el montículo vacío.
     * @see Monticulo#crearMonticuloVacio
     * @param clase instancia de la clase Comparable que se va a utilizar para crear el montículo.
     */
    public Monticulo(T clase){
        this.clase = clase;
        this.vector = crearMonticuloVacio();
    }

    /**
     * Transforma el array de entrada en un montículo.
     * El tamaño máximo no puede ser menor al del array de entrada.
     * @see Monticulo#creaMonticuloLineal
     * @param clase instancia de la clase Comparable que se va a utilizar para crear el montículo.
     * @param vector vector sobre el cual se va a crear el montículo.
     * @throws IllegalArgumentException si el vector de entrada tiene un tamaño menor a cero.
     */
    public Monticulo (T clase, T[] vector){
        if(vector.length < 1 ) throw new IllegalArgumentException("ERROR: el tamaño del montículo no puede ser menor a 1.");
        this.clase = clase;
        //Crea el montículo
        creaMonticuloLineal(vector);
    }

    /**
     * Devuelve el montículo creado en el constructor de un solo parámetro.
     * @return montículo generado en el constructor.
     */
    public T[] getMonticulo(){
        return this.vector;
    }

    /**
     * Crea un montículo vacío de tamaño 1 (mínimo posible).
     * Complejidad temporal lineal O(1). La información obtenida en internet sugiere que la complejidad temporal estaría
     * en torno a O(n), puesto que la máquina virtual tiene que reservar memoria e introducir el valor null en cada
     * posición del array. Para simplificar el cálculo de la complejidad, y al tener un tamaño de 1, se asumirá que el
     * coste es de la creación del montículo es constante.
     * @return una nueva instancia de un montículo vacío.
     */
    @SuppressWarnings("unchecked")
    public T[] crearMonticuloVacio(){
        return (T[]) Array.newInstance(clase.getClass(),1);
    }

    /**
     * Crea un montículo vacío de tamaño n > 1.
     * Complejidad temporal lienal O(n). La información obtenida en internet sugiere que la complejidad temporal estaría
     * en torno a O(n), puesto que la máquina virtual tiene que reservar memoria e introducir el valor null en cada
     * posición del array. Para simplificar el cálculo de la complejidad, y al tener un tamaño n, se asumirá que el
     * coste es de la creación del montículo es lienal.
     * @param tamano tamaño del montículo a crear. Si no se indica tamaño, se creará de tamaño 1.
     * @throws IllegalArgumentException si el vector de entrada tiene un tamaño menor a cero.
     * @return una nueva instancia de un montículo de tamaño n.
     */
    @SuppressWarnings("unchecked")
    public T[] crearMonticuloVacio(int tamano){
        if(tamano < 1 ) throw new IllegalArgumentException("ERROR: el tamaño del montículo no puede ser menor a 1.");
        return (T[]) Array.newInstance(clase.getClass(),tamano);
    }

    /**
     * El montículo está vacío si tiene un tamaño de 0 o no tiene elementos.
     * Complejidad temporal lineal O(n) en el caso peor.
     * @param monticulo montículo que se inspeccionará para ver si está vacío.
     * @return true si montículo vacío.
     */
    public boolean elMonticuloEstaVacio(T[] monticulo){
        if(monticulo.length == 0) return true;
        for (T t : monticulo) if (t != null) return false;
        return true;
    }

    /**
     * Reubica el elemento 'i' del vector, en caso de que este sea mayor que el padre, hasta que esté correctamente
     * situado en el montículo y se haya restablecido la propiedad de montículo.
     * Se utiliza para la inserción de un elemento nuevo en el montículo.
     * Complejidad temporal lineal O(n/2). Cada iteración del bucle corresponde a un nivel del montículo y cada nivel
     * tiene solo dos elementos. Ambos elementos se exploran en la misma iteración, por lo que el bucle será de tamaño
     * n/2, donde 'n' es la cantidad de elementos en el montículo.
     * @see Monticulo#intercambiar
     * @param monticulo montículo sobre el que se realizará la acción flotar.
     * @param elemento posición en el montículo del elemento sobre el que se realizará la acción flotar.
     */
    public void flotar(T[] monticulo, int elemento){
        int hijo  = elemento;
        int padre = elemento/2;
        //compareTO => negativo: menor    cero: igual    positivo: mayor
        while(hijo>0 && monticulo[padre].compareTo(monticulo[hijo])<0){
            intercambiar(padre, hijo, monticulo);
            hijo = padre;
            padre = hijo/2;
        }
    }

    /**
     * Reubica el elemento i (intercambia su valor por el del mayor de sus hijos) del vector en caso de que este sea
     * menor que alguno de sus hijos.
     * Complejidad temporal logarítmico O(log(n)). El caso peor es aquel en el que el primer elemento acaba en el último
     * nivel del montículo. Cada iteración del bucle corresponde a un nivel del montículo y cada nivel tiene solo dos
     * elementos. Ambos elementos se exploran en la misma iteración, por lo que el bucle será de tamaño log2(2^n),
     * donde 'n' es la cantidad de elementos en el montículo.
     * @see Monticulo#intercambiar
     * @param monticulo montículo sobre el que se realizará la acción hundir.
     * @param elemento posición en el montículo del elemento sobre el que se realizará la acción hundir.
     */
    public void hundir(T[] monticulo, int elemento){
        int hijoIZQ;
        int hijoDRC;
        int padre = -1;
        int i = elemento;

        while(padre != i) {
            hijoIZQ = ( 2 * i )+1;   //Se suma +1 para corregir el offset del indice.
            hijoDRC = ( (2 * i) + 1 )+1; //Se suma +1 para corregir el offset del indice.
            padre = i;

            //compareTO => negativo: menor    cero: igual    positivo: mayor
            if ((hijoDRC < monticulo.length) )
                if(monticulo[hijoDRC] != null)
                    if((monticulo[i] == null) || (monticulo[hijoDRC].compareTo(monticulo[i]) > 0)) i = hijoDRC;

            if ((hijoIZQ < monticulo.length))
                if(monticulo[hijoIZQ] != null)
                    if((monticulo[i] == null) || (monticulo[hijoIZQ].compareTo(monticulo[i]) > 0)) i = hijoIZQ;

            intercambiar(padre, i, monticulo);
        }
    }

    /**
     * Intercambia de posición a dos elementos de un montículo.
     * Complejidad temporal constante O(3). O(3) = O(1)+O(1)+O(1) Se realizan tres acciones de complejidad constante.
     * @param a posición en el montículo del primer elemento.
     * @param b posición en el montículo del segundo elemento.
     * @param monticulo montículo sobre el que se realiza el intercambio.
     */
    private void intercambiar(int a, int b, T[] monticulo){
        T temp = monticulo[a];
        monticulo[a] = monticulo[b];
        monticulo[b] = temp;
    }

    /**
     * Inserta un elemento en el montículo y lo flota hasta restaurar la propiedad de montículo.
     * Complejidad temporal lineal O(n). No se añaden espacios (nulos) adicionales al montículo para simplificar el
     * algoritmo de flotar y hundir. Aunque esto implica que siempre que se inserta un elemento se hace en un tiempo O(n).
     * @see Monticulo#crearMonticuloVacio(int) 
     * @see Monticulo#flotar
     * @param elemento elemento a insertar en montículo.
     * @param monticulo montículo sobre el que se realiza la acción insertar.
     * @throws IllegalArgumentException si el elemento a insertar es nulo.
     * @return montículo de tamaño n+1 con el elemento nuevo.
     */
    public T[] insertar(T elemento, T[] monticulo){
        if(elemento == null) throw new IllegalArgumentException("ERROR: no se puede insertar un elemento nulo.");
        T[] vTemp = crearMonticuloVacio(monticulo.length+1);
        System.arraycopy(monticulo,0,vTemp,0,monticulo.length); //Complejidad temporal: O(n)
        vTemp[vTemp.length-1] = elemento;
        flotar(vTemp, vTemp.length-1);
        monticulo = vTemp;
        return monticulo;
    }

    /**
     * Devuelve la cima del montículo sin modificarlo.
     * Complejidad temporal constante O(1).
     * @param monticulo montículo sobre el que se realizará la acción mostrarCima.
     * @return primer elemento del montículo.
     */
    public T mostrarCima(T[] monticulo){
        return monticulo[0];
    }

    /**
     * Devuelve la cima del montículo, la elimina y recompone la propiedad de montículo.
     * Complejidad temporal O(log(n)).
     * @see Monticulo#mostrarCima
     * @see Monticulo#hundir
     * @param monticulo montículo sobre el que se realizará la acción obtenerCima.
     * @return cima del montículo.
     */
    public T obtenerCima(T[] monticulo){
        T cima = mostrarCima(monticulo); //Guardamos la cima O(1)
        //Se elimina cima, se pone el último elemento en cabeza y se recompone montículo con hundir.
        monticulo[0] = monticulo[monticulo.length-1];
        monticulo[monticulo.length-1] = null;
        hundir(monticulo,0); // O(log(n))
        return cima;
    }

    /**
     * Crea un montículo a partir de un vector.
     * Complejidad temporal O(n).
     * @see Monticulo#hundir
     * @param vector array sobre el que se realizará la acción creaMonticuloLineal.
     */
    public void creaMonticuloLineal(T[] vector){
        for(int i=( (vector.length-1)/2 ); i>=0;i--)
            hundir(vector, i);
    }

    /**
     * Recibe un vector y lo ordenada de mayor a menor.
     * Complejidad temporal O(n*log(n)).
     * @see Monticulo#creaMonticuloLineal
     * @see Monticulo#obtenerCima
     * @param vector vector sobre el que se realizará la acción heapShort.
     * @throws IllegalArgumentException si el vector de entrada es de tamaño menor a uno.
     */
    public void heapSort(T[] vector){
        if(vector.length < 1 ) throw new IllegalArgumentException("ERROR: el tamaño del vector no puede ser menor a 1.");
        T cima;
        T[] clon = vector.clone(); // O(1)
        creaMonticuloLineal(clon); // O(n)

        for(int i=0; i<vector.length; i++){ // Bucle O(n) + obtenerCima O(log(n)) = O(n*log(n))
            cima = obtenerCima(clon); // O(log(n))
            vector[i] = cima;
        }
    }

}
