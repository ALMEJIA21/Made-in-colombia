# Casos de prueba - T-06 Aprovechamiento de tiempos pasivos (Issue #6, HU-06)

Compilar: `javac -d out src/*.java tests/PruebasAprovechamiento.java`  |  Ejecutar: `java -cp out PruebasAprovechamiento`

Servicio a las 13:00 en todos los casos. (A) = activa, (P) = pasiva.

| ID | Funcionalidad | Datos de entrada | Resultado esperado | Resultado obtenido | Estado |
|---|---|---|---|---|---|
| CT-A01 | Escenario de referencia sin choques | Arroz: Lavar 10 (A), Cocinar 30 (P). Pollo: Picar 15 (A), Cocer 40 (P) | Lavar 12:20-12:30, Cocinar 12:30-13:00, Picar 12:05-12:20, Cocer 12:20-13:00 | Coincide | Satisfactorio |
| CT-A02 | Dos activas chocan al final | Sopa: Hervir 30 (P), Servir 5 (A). Pescado: Freir 10 (A) | Servir 12:55-13:00, Freir se desplaza a 12:45-12:55, Hervir 12:25-12:55 | Coincide | Satisfactorio |
| CT-A03 | Activas dentro de una espera pasiva | Pastel: Preparar 10 (A), Hornear 60 (P). Ensalada: Picar 20 (A), Mezclar 10 (A) | Picar y Mezclar quedan dentro de Hornear; primer inicio 11:50 | Coincide | Satisfactorio |
| CT-A04 | Activa que no cabe se desplaza | Flan: Preparar flan 10 (A), Reposar 20 (P). Guiso: Cocinar 30 (A) | Cocinar 12:30-13:00, Preparar flan 12:20-12:30, Reposar 12:40-13:00 | Coincide | Satisfactorio |
| CT-A05 | Tres platos con muchas activas | P1, P2 y P3 con 9 tareas | Ningun par de activas se solapa; 9 pasos; orden de cada plato conservado | Coincide | Satisfactorio |
| CT-A06 | Un solo plato | Solo Arroz con coco | Igual al calculo inverso del #5 | Igual | Satisfactorio |
| CT-A07 | Entradas invalidas | Lista vacia, lista nula, hora 1440 | Lanza IllegalArgumentException | Lanza la excepcion | Satisfactorio |
