# Casos de prueba - T-05 Calculo inverso (Issue #5, HU-05)

Compilar: `javac -d out src/*.java tests/PruebasPlanificador.java`  |  Ejecutar: `java -cp out PruebasPlanificador`

Escenario de referencia: servicio 13:00. Arroz con coco (Lavar y sofreir 10 min Activa, Cocinar arroz 30 min Pasiva). Pollo sudado (Picar verduras 15 min Activa, Cocer pollo 40 min Pasiva).

| ID | Funcionalidad | Datos de entrada | Resultado esperado | Resultado obtenido | Estado |
|---|---|---|---|---|---|
| CT-P01 | Cantidad de pasos | Escenario de referencia | 4 pasos | 4 pasos | Satisfactorio |
| CT-P02 | Horas de inicio y fin | Escenario de referencia | Lavar 12:20-12:30, Cocinar arroz 12:30-13:00, Picar 12:05-12:20, Cocer pollo 12:20-13:00 | Coincide | Satisfactorio |
| CT-P03 | Convergencia | Escenario de referencia | Ultima tarea de cada plato termina a las 13:00 | Termina a las 13:00 | Satisfactorio |
| CT-P04 | Orden dentro del plato | Escenario de referencia | El fin de una tarea es el inicio de la siguiente | Coincide | Satisfactorio |
| CT-P05 | Plato de una tarea | Licuar 5 min, servicio 13:00 | 12:55-13:00 | 12:55-13:00 | Satisfactorio |
| CT-P06 | Inicio antes de medianoche | Licuar 5 min, servicio 00:03 | Inicio -2 (negativo) | -2 | Satisfactorio |
| CT-P07 | Entradas invalidas | Lista vacia, lista nula, hora 1440 | Lanza IllegalArgumentException | Lanza la excepcion | Satisfactorio |
