package pasteleria;

import java.util.ArrayList;

/**
 * Clase que representa la estructura de datos "Montículo de máximos". En esta nueva se utiliza arraylist en lugar de
 * array.
 * UNED PREDA 2022/2023 - PEC2 - Oracle OpenJDK version 19.
 * @author Asier Rodríguez López
 * @version 2.0
 * @since 1.0
 * @param <T> Tipo de clase que se utilizará con el Montículo. Debe de ser de tipo Comparable.
 */
public class Monticulo <T extends Comparable<T>>{

    /**
     * Variable que contendrá el montículo de tipo T.
     */
    private ArrayList<T> vector;

    /**
     * Si no se dispone de un ArrayList, se inicializa la clase y el montículo vacío.
     * @see Monticulo#crearMonticuloVacio
     */
    public Monticulo(){
        this.vector = crearMonticuloVacio();
    }

    /**
     * Transforma el arraylist de entrada en un montículo.
     * El tamaño máximo no puede ser menor al del ArrayList de entrada.
     * @see Monticulo#creaMonticuloLineal
     * @param vector vector sobre el cual se va a crear el montículo.
     * @throws IllegalArgumentException si el vector de entrada tiene un tamaño menor a cero.
     */
    public Monticulo (ArrayList<T> vector){
        if(vector == null)     throw new IllegalArgumentException("ERROR: el vector no puede ser nulo.");
        if(vector.size() < 1 ) throw new IllegalArgumentException("ERROR: el tamaño del vector no puede ser menor a 1.");
        //Crea el montículo
        creaMonticuloLineal(vector);
        this.vector = vector;
    }

    /**
     * Devuelve el montículo creado en el constructor.
     * @return montículo generado en el constructor.
     */
    public ArrayList<T> getMonticulo(){
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
    public ArrayList<T> crearMonticuloVacio(){
        return new ArrayList<T>(0);
    }

    /**
     * Crea un montículo vacío de tamaño n > 1.
     * Complejidad temporal lineal O(n). La información obtenida en internet sugiere que la complejidad temporal estaría
     * en torno a O(n), puesto que la máquina virtual tiene que reservar memoria e introducir el valor null en cada
     * posición del array. Para simplificar el cálculo de la complejidad, y al tener un tamaño n, se asumirá que el
     * coste es de la creación del montículo es lineal.
     * @param tamano tamaño del montículo a crear. Si no se indica tamaño, se creará de tamaño 1.
     * @throws IllegalArgumentException si el vector de entrada tiene un tamaño menor a cero.
     * @return una nueva instancia de un montículo de tamaño n.
     */
    @SuppressWarnings("unchecked")
    public ArrayList<T> crearMonticuloVacio(int tamano){
        if(tamano < 1 ) throw new IllegalArgumentException("ERROR: el tamaño del montículo no puede ser menor a 1.");
        return new ArrayList<T>(tamano);
    }

    /**
     * El montículo está vacío si tiene un tamaño de 0 o no tiene elementos.
     * Complejidad temporal constante O(1).
     * @param monticulo montículo que se inspeccionará para ver si está vacío.
     * @return true si montículo vacío.
     */
    public boolean elMonticuloEstaVacio(ArrayList<T> monticulo){
        return monticulo.size() == 0;
    }

    /**
     * Reubica el elemento 'i' del vector, en caso de que este sea mayor que el padre, hasta que esté correctamente
     * situado en el montículo y se haya restablecido la propiedad de montículo.
     * Se utiliza para la inserción de un elemento nuevo en el montículo.
     * Complejidad temporal logarítmico O(log(n)). Cada iteración del bucle corresponde a un nivel del montículo y cada
     * nivel tiene solo dos elementos. En cada iteración se compara el elemento del nivel de ese momento con su elemento
     * padre, por lo que el bucle, en el caso peor, será de tamaño log(n), donde 'n' es la cantidad de elementos en el montículo.
     * @see Monticulo#intercambiar
     * @param monticulo montículo sobre el que se realizará la acción flotar.
     * @param elemento posición en el montículo del elemento sobre el que se realizará la acción flotar.
     */
    public void flotar(ArrayList<T> monticulo, int elemento){
        int hijo  = elemento;
        int padre = elemento/2;
        //compareTO => negativo: menor    cero: igual    positivo: mayor
        while(hijo>0 && monticulo.get(padre).compareTo(monticulo.get(hijo))<0){
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
    public void hundir(ArrayList<T> monticulo, int elemento){
        int hijoIZQ;
        int hijoDRC;
        int padre = -1;
        int i = elemento;

        while(padre != i) {
            hijoIZQ = ( 2 * i )+1;   //Se suma +1 para corregir el offset del indice.
            hijoDRC = ( (2 * i) + 1 )+1; //Se suma +1 para corregir el offset del indice.
            padre = i;

            //compareTO => negativo: menor    cero: igual    positivo: mayor
            if ((hijoDRC < monticulo.size()) )
                if(monticulo.get(hijoDRC).compareTo(monticulo.get(i)) > 0) i = hijoDRC;

            if ((hijoIZQ < monticulo.size()))
                if(monticulo.get(hijoIZQ).compareTo(monticulo.get(i)) > 0) i = hijoIZQ;

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
    private void intercambiar(int a, int b, ArrayList<T> monticulo){
        T temp = monticulo.get(a);
        monticulo.set(a,monticulo.get(b));
        monticulo.set(b,temp);
    }

    /**
     * Inserta un elemento en el montículo y lo flota hasta restaurar la propiedad de montículo.
     * Aunque un ArrayList dispone de una capacidad adicional superior a la indicada en su creación y, por lo tanto, cada
     * vez que se inserta un item no es necesario crear un nuevo ArrayList de mayor capacidad, con el fin de simplificar
     * cálculos, se asume una complejidad temporal lineal O(n).
     * @see Monticulo#crearMonticuloVacio(int)
     * @see Monticulo#flotar
     * @param elemento elemento a insertar en montículo.
     * @param monticulo montículo sobre el que se realiza la acción insertar.
     * @throws IllegalArgumentException si el elemento a insertar es nulo o el montículo es nulo.
     */
    public void insertar(T elemento, ArrayList<T> monticulo){
        if(elemento == null) throw new IllegalArgumentException("ERROR: no se puede insertar un elemento nulo.");
        if(monticulo == null) throw new IllegalArgumentException("ERROR: el montículo es nulo.");
        monticulo.add(elemento); // O(n)
        flotar(monticulo, monticulo.size()-1); // O(n)
    }

    /**
     * Devuelve la cima del montículo sin modificarlo.
     * Complejidad temporal constante O(1).
     * @param monticulo montículo sobre el que se realizará la acción mostrarCima.
     * @return primer elemento del montículo.
     */
    public T mostrarCima(ArrayList<T> monticulo){
        return monticulo.get(0);
    }

    /**
     * Devuelve la cima del montículo, la elimina y recompone la propiedad de montículo.
     * Complejidad temporal O(log(n)).
     * @see Monticulo#mostrarCima
     * @see Monticulo#hundir
     * @param monticulo montículo sobre el que se realizará la acción obtenerCima.
     * @return cima del montículo.
     */
    public T obtenerCima(ArrayList<T> monticulo){
        T cima = mostrarCima(monticulo); //Guardamos la cima O(1)
        //Se elimina cima, se pone el último elemento en cabeza y se recompone montículo con hundir.
        monticulo.set(0,monticulo.get(monticulo.size()-1)); // O(1)
        monticulo.remove(monticulo.size()-1); // O(1) Al ser el último elemento.
        if(monticulo.size()>1) hundir(monticulo,0); // O(log(n))
        return cima;
    }

    /**
     * Crea un montículo a partir de un vector.
     * Complejidad temporal O(n).
     * @see Monticulo#hundir
     * @param vector array sobre el que se realizará la acción creaMonticuloLineal.
     */
    public void creaMonticuloLineal(ArrayList<T> vector){
        for(int i=( (vector.size()-1)/2 ); i>=0;i--)
            hundir(vector, i); // O(log(n))
    }

    /**
     * Recibe un vector y lo ordenada de mayor a menor.
     * Complejidad temporal O(n*log(n)).
     * @see Monticulo#creaMonticuloLineal
     * @see Monticulo#obtenerCima
     * @param vector vector sobre el que se realizará la acción heapShort.
     * @throws IllegalArgumentException si el vector de entrada es de tamaño menor a uno.
     */
    public void heapSort(ArrayList<T> vector){
        if(vector == null)     throw new IllegalArgumentException("ERROR: el vector no puede ser nulo.");
        if(vector.size() < 1 ) throw new IllegalArgumentException("ERROR: el tamaño del vector no puede ser menor a 1.");

        ArrayList<T> clon = (ArrayList<T>) vector.clone();
        creaMonticuloLineal(vector); // O(n)

        for(int i=0; i<vector.size(); i++) // Bucle O(n) + obtenerCima O(log(n)) = O(n*log(n))
            vector.set(i,obtenerCima(clon));
    }

}
