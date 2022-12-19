# PREDA_PEC2
Práctica 2 de Programación y estructuras de datos avanzadas:
https://asier-r.github.io/preda_JavaDoc/

UNED - GRADO EN INGENIERÍA EN INFORMÁTICA Y EN TECNOLOGÍAS DE LA INFORMACIÓN

PROGRAMACIÓN Y ESTRUCTURAS DE DATOS AVANZADAS

1.- ENUNCIADO DE LA PRÁCTICA
Una pastelería tiene n pasteleros capaces de realizar los m tipos de pasteles diferentes que ofrece
la pastelería, aunque cada uno de ellos presenta distinta destreza en la realización de los
diferentes pasteles. El objetivo es asignar los n próximos pedidos recibidos en la pastelería, uno a
cada uno de los n pasteleros, de forma que se minimice el tiempo total de preparación de los
pedidos. Se conoce el tiempo que cada pastelero emplea en preparar cada tipo de pastel.
El enunciado del problema, la descripción de la solución, junto con un ejemplo explicativo se puede
encontrar en el apartado 7.2 del texto base de la asignatura.



2.- REALIZACIÓN DE LA PRÁCTICA

2.1.- Diseño del algoritmo
La práctica constará de una memoria y de un programa en java original que resuelva el problema
aplicando el esquema de ramificación y poda.


2.2.- Argumentos y parámetros
La práctica se invoca usando la siguiente sintaxis:

java pasteleria [-t][-h] [fichero entrada] [fichero salida]
o
java –jar pasteleria.jar [-t][-h] [fichero entrada] [fichero salida]

Los argumentos son los siguientes:
	• -t: traza cada paso de manera que se describa la aplicación del algoritmo utilizado.
	• -h: muestra una ayuda y la sintaxis del comando.
	• fichero_entrada: es el nombre del fichero del que se leen los datos, en este caso, la
	  información sobre los pedidos y los tiempos de realización de los distintos pasteles por parte
	  de los pasteleros.
	• fichero_salida: es el nombre del fichero que se creará para almacenar la salida. Si el
      fichero ya existe, el comando dará un error. Si falta este argumento, el programa muestra
	  el resultado por pantalla.
	  
Por ejemplo:
$ java pasteleria -h <ENTER>
SINTAXIS: pasteleria [-t][-h] [fichero entrada]
-t Traza el algoritmo
-h Muestra esta ayuda
[fichero entrada] Nombre del fichero de entrada
[fichero salida] Nombre del fichero de salida


2.3- Datos de entrada
El fichero de entrada consta de:
	• Una primera línea que indica el valor de n (número de pasteleros de la pastelería)
	• Una segunda línea que indica el valor de m (número de tipos de pasteles distintos que
	  ofrece la pastelería)
	• Una tercera línea, con n valores separados por guión (-), donde cada valor indica el tipo de
	  pastel correspondiente a cada pedido.
	• n líneas más con la matriz de costes C[1..n,1..m], en la que el valor cij corresponde al
	 coste de que el pastelero i realice el pastel j, y cuyos valores se presentan separados por
	 un espacio.
	 
Por ejemplo, para una pastelería con 5 pasteleros y una carta de 3 tipos de pasteles diferentes,
que reciben 5 pedidos de tipos [11321], y cuya tabla de costes es la del ejemplo del texto base de
la asignatura (página 197, apartado 7.2), el fichero de entrada tendría el siguiente aspecto:
5
3
1-1-3-2-1
2 5 3
5 3 2
6 4 9
6 3 8
7 5 8

En caso de que el fichero de entrada no exista, se leerán los datos por la entrada estándar.


2.4- Datos de salida
La salida constará de dos líneas:
	• Una línea con la asignación resultante. Consistirá en n valores separados por guión (-),
	  donde el primer valor representa al pastelero asignado al primer pedido, el segundo valor
	  representa al pastelero encargado del segundo pedido, y así sucesivamente.
	• El coste total de la asignación.
	
Por ejemplo, la salida para el fichero de entrada descrito en el apartado anterior tendría
el siguiente aspecto:
1-3-2-4-5
20

Expresando que el pastelero 1 elabora el pedido 1, el pastelero 3 elabora el pedido 2, el pastelero
2 elabora el pedido 3, el pastelero 4 elabora el pedido 4 y el pastelero 5 elabora el pedido 5. El
coste de esta asignación de pasteleros a pedidos es 20.

En caso de que el fichero de salida no se indique en la llamada al programa, se escribirá el
resultado por la salida estándar.


2.5.- Implementación del algoritmo
El programa se desarrollará en Java siguiendo un diseño orientado a objetos. Los detalles del
entorno recomendado se encuentran en la guía de la asignatura. Se valorará el diseño OO y la
eficiencia del desarrollo.



3.- CUESTIONES TEÓRICAS DE LA PRÁCTICA

1) Indica y razona sobre el coste temporal y espacial del algoritmo.
2) Explica qué otros esquemas pueden resolver el problema y razona sobre su idoneidad.



4.- ENTREGA DE LA PRÁCTICA

4.1 Material que hay que entregar al Tutor
Se confeccionará una memoria, en PDF con el siguiente índice:
	1. Portada de la memoria con nombre, apellidos, dni y dirección de correo.
	2. Respuesta a las cuestiones teóricas planteadas en este enunciado.
	3. Un ejemplo de ejecución para distintos tamaños del problema.
	4. Un listado del código fuente completo.
	

4.2 Normativa de las prácticas en relación con el Centro Asociado:
	1. La asistencia a las sesiones de prácticas es obligatoria.
	2. El calendario y procedimiento para asistir a las sesiones de prácticas está publicado en
	   su Centro Asociado o bien aparece en el foro correspondiente a su centro en el curso
	   virtual.
	3. El plazo de entrega de la documentación y de la práctica lo establece el Tutor de prácticas
	   de cada Centro Asociado o Campus.
	4. El Tutor califica la práctica, informa al alumno y en su caso la revisa de acuerdo con los
	   horarios y procedimiento que establezca el Centro Asociado.
	5. Todos los alumnos deberán registrarse a través del Curso Virtual en el grupo del Tutor/a
	   con el que hayan asistido a las sesiones presenciales obligatorias a fin de que su práctica
	   pueda ser calificada.
	6. La práctica se debe aprobar en la misma o anterior convocatoria para que se pueda calificar
	   la asignatura. En caso contrario la calificación será de suspenso.
	7. La práctica se entregará tanto en el entorno virtual como al Tutor. La falta de cualquiera de
	   ellas será motivo suficiente para quedar excluida de la convocatoria.

El alumno debe asegurarse de que no se da ninguna de las siguientes circunstancias, ya que
implican automáticamente una calificación de suspenso:
	• Código: el código no compila, no está desarrollado en Java, no se corresponde con el
	  pseudocódigo recogido en la documentación, no es original, está copiado de la red,
	  academia, compañero, etc., o no sigue un diseño OO encapsulado o modular.
	• Ejecutable: el ejecutable no termina, se queda sin memoria con ejemplares pequeños o
	  aborta sin justificación. El ejecutable no lee los ficheros previstos en el formato adecuado.
	  No trata los argumentos o no se ajusta a las especificaciones.
	• Documentación: No se presenta en el soporte indicado por el tutor o está incompleta.
	• Soporte: No se puede leer, o contiene un virus de cualquier tipo. A este respecto, las
	  prácticas en las que se detecte cualquier tipo de virus estarán suspensas.
	  
Los alumnos estudiando en el EXTRANJERO se deberán poner en contacto con el profesor tutor
que se indicará en los foros.