============
CompareTwoDB
============

En este archivo se puede encontrar informacion general acerca del proyecto:

Integrantes:
	-Gasparrini, Jonathan Leonel.   DNI: 36643145.
	-Torletti, Franco Damian.	DNI: 34590692.

Herramientas utilizadas:
	- Java compiler version 1.6
	- PostgreSQL version 9.1.10
	- API JDBC version postgresql-9.2-1003.jdbc4

	Podremos observar que en la ubicación en donde está este archivo README
hay una serie de carpetas, en donde en cada una de ellas podremos encontrar:

  * "db-tests" 2 Tests completos que se utilizan para testear nuestro programa CompareTwoDB.

  * "doc" En donde podremos encontrar el enunciado de este proyecto.

  * "executable" Aqui tenemos el archivo ejecutable (*jar) que permite lanzar la aplicacion con una simple vista.

  * "lib" En esta carpeta pusimos las librerias utilizadas para nuestro comparador.

  * "src" El codigo fuente del proyecto escrito en JAVA.

      (Como podremos observar mas abajo la arquitectura utilizada para este proyecto 
      es el Modelo-Vista-Controlador)

      Dentro de la carpeta "src" tenemos:
        * "controller" Aca tenemos a "Controller.java". Un controlador encargado de la interaccion
          entre la vista y el comparador de las bases de datos.

        * "model" Tenemos "DBConecction.java". Permite conectarnos a una base de datos postgreSQL.
          Tambien tenemos "Comparator.java". Que nos permite realizar comparaciones 
          con el objetivo de obtener las diferencias entre las mismas.

        * "utils" Se encuentra "Queries.java". Se encarga de realizar cada una de las consultas a la base de datos.
        Tambien disponemos de "TuplesOfStrings.java" cumpliendo con el objetivo de ser una n-upla de Strings.

        * "view" Será "View.java" la encargada de mostrar una sencilla interfaz al usuario.
	Para que pueda comparar las dos base de datos de manera simple y obtener los resultados inmediatamente en la misma vista.

        * Por último tenemos "Main.java" que es la encargada de lanzar la aplicacion principal.
