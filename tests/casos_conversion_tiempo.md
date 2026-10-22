# Casos de prueba - T-04 Conversion Min/Hora (Issue #4, HU-04)

Compilar: `javac -d out src/*.java   Ejecutar: `java -cp out Main`

| ID | Funcionalidad | Datos de entrada | Resultado esperado | Resultado obtenido | Estado |
|---|---|---|---|---|---|
| CT-C01 | horaAMinutos | `13:00`, `00:00`, `23:59`, `9:05` | 780, 0, 1439, 545 | 780, 0, 1439, 545 | Satisfactorio |
| CT-C02 | minutosAHora | 780, 0, 545 | `13:00`, `00:00`, `09:05` | `13:00`, `00:00`, `09:05` | Satisfactorio |
| CT-C03 | Ida y vuelta | Cada minuto de 0 a 1439 | Se conserva el valor original | Se conserva | Satisfactorio |
| CT-C04 | esHoraValida | `25:70`, `24:00`, `12:60`, `abc`, vacio, `12`, `12:5`, `1:2:3`, `-1:30`, `12.30`, null | `false` en todos | `false` en todos | Satisfactorio |
| CT-C05 | horaAMinutos invalida | `25:70`, null | Lanza IllegalArgumentException | Lanza la excepcion | Satisfactorio |
| CT-C06 | minutosAHora fuera del dia | -1, 1440 | Lanza IllegalArgumentException | Lanza la excepcion | Satisfactorio |
