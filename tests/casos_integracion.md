# Casos de prueba - T-10 Pruebas de integracion end-to-end (Issue #10, HU-01 a HU-08)

Compilar: `javac -d out src/*.java`  |  Ejecutar: `java -cp out Main`

Escenario de referencia: servicio 13:00. Arroz con coco (Lavar y sofreir 10 min Activa, Cocinar arroz 30 min Pasiva). Pollo sudado (Picar verduras 15 min Activa, Cocer pollo 40 min Pasiva). Inicio mas temprano: 12:05. En los casos con cronograma se usa la opcion 1 para registrar y la opcion 2 para calcular; la opcion 2 pide la hora actual.

| ID | Funcionalidad | Datos de entrada | Resultado esperado | Resultado obtenido | Estado |

| CI-01 | Flujo principal sin conflicto (HU-01 a HU-08) | Escenario de referencia; opcion 2 con hora actual 11:30 | "Tiempo suficiente: debe empezar a las 12:05 y sobran 35 minutos" y tabla con 12:05, 12:20, 12:20, 12:30 | Coincide | Satisfactorio |
| CI-02 | Conflicto de horario (HU-07) | Escenario de referencia; hora actual 12:15 | Advertencia con servicio 13:00, hora actual 12:15, inicio necesario 12:05 y retraso de 10 minutos; la tabla se imprime igual | Coincide | Satisfactorio |
| CI-03 | Limite: empezar justo a la hora actual | Escenario de referencia; hora actual 12:05 | Sin conflicto: "Tiempo justo: debe empezar a las 12:05, que es la hora actual" | Coincide | Satisfactorio |
| CI-04 | Hora actual posterior al servicio | Escenario de referencia; hora actual 13:30 | Advertencia con retraso de 85 minutos y la linea "La hora actual es posterior a la hora de servicio" | Coincide | Satisfactorio |
| CI-05 | Hora actual invalida (HU-04) | Opcion 2 con `25:70`, luego `abc`, luego `11:00` | Dos mensajes de formato invalido, vuelve a pedir la hora y al final calcula sin cerrarse | Coincide | Satisfactorio |
| CI-06 | Validaciones de captura (HU-04) | Hora de servicio `25:70` y `abc`; plato vacio; duracion `abc`, `0` y `-5`; tipo `3`; plato repetido `arroz`; opcion de menu `9` | Un error claro en cada caso, vuelve a pedir el dato y el programa no se cierra | Coincide | Satisfactorio |
| CI-07 | Dos activas al final (HU-06) | Servicio 13:00; Sopa (Hervir 30 P, Servir 5 A) y Pescado (Freir 10 A); hora actual 12:00 | Servir 12:55-13:00, Freir se desplaza a 12:45-12:55, Hervir 12:25-12:55; sobran 25 minutos | Coincide | Satisfactorio |
| CI-08 | Tres platos ordenados (HU-05, T-07) | Escenario de referencia + Ensalada (Cortar 8 min Activa); hora actual 12:00 | 5 filas ordenadas; Cortar 12:52-13:00 en la ultima; sobran 5 minutos | Coincide | Satisfactorio |
| CI-09 | Inicio antes de medianoche | Servicio 00:03; Jugo (Licuar 5 A), Te (Hervir 3 P); hora actual 00:00 | Advertencia con inicio `<00:00 (2 minutos antes de la medianoche)` y retraso de 2 minutos; Licuar `<00:00` en la primera fila | Coincide | Satisfactorio |
| CI-10 | Sin menu y opcion no reconocida | Opcion 2 al iniciar; luego `9` | Aviso "Primero registre un menu..." sin pedir hora actual; error de opcion y el programa sigue | Coincide | Satisfactorio |
| CI-11 | Conservacion del estado tras opcion rechazada | Escenario de referencia; opcion 2 (11:30); luego `9`; luego opcion 2 (12:15) | Error por `9`; el segundo calculo muestra la misma tabla y ahora la advertencia de 10 minutos | Coincide | Satisfactorio |
| CI-12 | Entrada cerrada (Ctrl+D) | Registrar hora `13:00` y cerrar la entrada | Mensaje "Entrada finalizada. Hasta pronto." sin excepcion | Coincide | Satisfactorio |
| CI-13 | Aprovechamiento de pasivas, caso CT-A03 de T-06 | Servicio 13:00; Pastel (Preparar 10 A, Hornear 60 P) y Ensalada (Picar 20 A, Mezclar 10 A); hora actual 10:00 | Picar 12:30-12:50 y Mezclar 12:50-13:00 dentro de Hornear; primer inicio 11:50 | Coincide | Satisfactorio |
| CI-14 | Activa que no cabe, caso CT-A04 de T-06 | Servicio 13:00; Flan (Preparar flan 10 A, Reposar 20 P) y Guiso (Cocinar 30 A); hora actual 10:00 | Cocinar 12:30-13:00, Preparar flan 12:20-12:30, Reposar 12:40-13:00 | Coincide | Satisfactorio |

**Regresion:** `PruebasPlanificador` 12 de 12 y `PruebasConversorTiempo` 24 de 24 despues de los cambios de T-03, T-08 y T-09.