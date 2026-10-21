# Casos de prueba - T-07 Ordenamiento temporal del cronograma (Issue #7, HU-05)

Compilar: `javac -d out src/*.java`  |  Ejecutar: `java -cp out Main`

Escenario de referencia: servicio 13:00. Arroz con coco (Lavar y sofreir 10 min Activa, Cocinar arroz 30 min Pasiva). Pollo sudado (Picar verduras 15 min Activa, Cocer pollo 40 min Pasiva). En todos los casos se usa la opcion 1 del menu para registrar y la opcion 2 para ver el cronograma.

| ID | Funcionalidad | Datos de entrada | Resultado esperado | Resultado obtenido | Estado |
|---|---|---|---|---|---|
| CT-O01 | Orden cronologico del cronograma | Escenario de referencia, registrando primero Arroz con coco; opcion 2 | Picar 12:05, Lavar 12:20, Cocer pollo 12:20, Cocinar arroz 12:30 | Coincide | Satisfactorio |
| CT-O02 | Orden independiente del orden de registro (desempate por hora de fin) | Escenario de referencia, registrando primero Pollo sudado; opcion 2 | Misma tabla que CT-O01: a las 12:20 sale primero Lavar (termina 12:30) y despues Cocer pollo (termina 13:00) | Coincide (antes de T-07 salia Cocer pollo antes que Lavar) | Satisfactorio |
| CT-O03 | Orden con tres platos | Escenario de referencia + Ensalada (Cortar 8 min Activa); opcion 2 | 5 filas ordenadas; Cortar 12:52-13:00 en la ultima fila | Coincide | Satisfactorio |
| CT-O04 | Inicio antes de medianoche | Servicio 00:03; Jugo (Licuar 5 min Activa), Te (Hervir 3 min Pasiva); opcion 2 | Primera fila Licuar con inicio `<00:00`; segunda fila Hervir 00:00-00:03 | Coincide | Satisfactorio |
| CT-O05 | Desempate por nombre de plato | Servicio 13:00; Zeta (Hervir 10 min Pasiva) y luego Alfa (Reposar 10 min Pasiva); opcion 2 | Alfa antes que Zeta, ambos 12:50-13:00 | Coincide | Satisfactorio |
| CT-O06 | Cronograma sin menu registrado | Opcion 2 al iniciar el programa | Aviso "Primero registre un menu con al menos 2 platos" sin cerrar el programa | Coincide | Satisfactorio |
| CT-O07 | Opciones no reconocidas | En el menu principal: `9` y luego `abc` | Mensaje "elija una opcion entre 1 y 3" y vuelve a pedir, sin cerrar | Coincide | Satisfactorio |
| CT-O08 | Conservacion del estado tras una opcion rechazada | Escenario de referencia; opcion 2; luego `9`; luego opcion 2 | Error por `9` y el segundo cronograma es identico al primero (4 filas, sin repetir) | Coincide | Satisfactorio |