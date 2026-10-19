# Casos de prueba - Sprint 1 (T-01 y T-02)

Compilar: `javac -d out src/*.java`  |  Ejecutar: `java -cp out Main`

| # | Entrada | Resultado esperado |
|---|---|---|
| 1 | Hora de servicio `25:70` | Error de formato y vuelve a pedir la hora |
| 2 | Hora de servicio `abc` o vacio | Error de formato y vuelve a pedir la hora |
| 3 | Hora de servicio `13:00` | Se acepta |
| 4 | Nombre de plato vacio | Error y vuelve a pedir el nombre |
| 5 | Duracion `abc`, `0` o `-5` | Error y vuelve a pedir la duracion |
| 6 | Tipo `3` o `x` | Error y vuelve a pedir el tipo |
| 7 | Registrar un solo plato | No permite terminar: pide un segundo plato |
| 8 | Plato con 2 tareas (10 min Activa, 30 min Pasiva) | Total del plato 40 min; tareas en el orden ingresado |
| 9 | Opcion 2 del menu sin menu registrado | Aviso de registrar el menu primero, sin cerrarse |
| 10 | Cerrar la entrada (Ctrl+D) | El programa termina sin excepcion |
| 11 | Segundo plato con el mismo nombre que el primero (ej. `Arroz` y `arroz`) | Error de plato repetido y vuelve a pedir el nombre |