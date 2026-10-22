# Made in Colombia

Programa de consola en Java que coordina los tiempos de preparación de varios platos para que todos queden listos a una misma hora de servicio. El usuario ingresa el menú (hora de servicio, platos y tareas) y el programa calcula a qué hora debe empezar cada tarea.

Proyecto del equipo de la ficha 3311658 ADSO, desarrollado con Scrum.

## Requisitos

- JDK 14 o superior (probado con JDK 21).
- Una terminal (o el terminal de un IDE como IntelliJ, Eclipse o VS Code).
- No usa librerías externas.

## Estructura del proyecto

```
MadeInColombia/
├── src/
│   ├── Main.java               Menú principal y registro del menú
│   ├── LecturaConsola.java     Lectura y validación de entradas (T-03)
│   ├── Tarea.java              Modelo: una tarea (nombre, duración, activa/pasiva)
│   ├── Plato.java              Modelo: un plato con sus tareas en orden
│   ├── ConversorTiempo.java    HH:mm <-> minutos (T-04)
│   ├── Planificador.java       Cálculo inverso y aprovechamiento de esperas (T-05, T-06)
│   ├── PasoCronograma.java     Una tarea ubicada en el tiempo (inicio y fin)
│   ├── Cronograma.java         Pasos ordenados cronológicamente y filtros (T-07)
│   ├── ValidadorHorario.java   Detección de horario imposible (T-08)
│   └── ReporteCronograma.java  Tabla de texto y mensajes de salida (T-09)
├── tests/                      Casos de prueba (.md) y pruebas con main (.java)
└── README.md
```

## Compilar y ejecutar

Desde la carpeta `MadeInColombia`:

```bash
javac -d out src/*.java
java -cp out Main
```

Si cambias algún archivo, vuelve a compilar todo con el primer comando.

## Cómo usar el programa

Al iniciar aparece el menú principal:

```
1) Registrar menu (hora de servicio, platos y tareas)
2) Calcular y mostrar cronograma
3) Salir
```

**Opción 1: registrar el menú**

1. Escribe la hora de servicio en formato HH:mm de 24 horas (ej. `13:00`). Es una sola para todo el menú.
2. Escribe el nombre del plato (no se permiten nombres repetidos).
3. Registra las tareas del plato **en el orden en que se ejecutan**: nombre, duración en minutos (entero mayor que 0) y tipo (`1` = Activa, `2` = Pasiva).
4. Cuando termines un plato, elige si agregas otra tarea o terminas el plato. El menú necesita al menos 2 platos.
5. Al final se muestra un resumen. Registrar de nuevo reemplaza el menú anterior.

**Opción 2: calcular y mostrar el cronograma**

1. Escribe la hora actual o de inicio disponible (HH:mm).
2. El programa muestra si hay tiempo suficiente o una advertencia, y debajo la tabla con la hora de inicio y fin de cada tarea, ordenada por hora.

Si escribes un dato incorrecto (letras en un número, una hora como `25:70`, una opción que no existe), el programa muestra el error y vuelve a preguntar sin cerrarse.

### Ejemplo

Registro con servicio a las 13:00: **Arroz con coco** (Lavar y sofreír 10 min Activa, Cocinar arroz 30 min Pasiva) y **Pollo sudado** (Picar verduras 15 min Activa, Cocer pollo 40 min Pasiva). Con hora actual `11:30` la opción 2 muestra:

```
Tiempo suficiente: debe empezar a las 12:05 y sobran 35 minutos.

+-------------+----------+----------------+-----------------+--------+
| Hora inicio | Hora fin | Plato          | Tarea           | Tipo   |
+-------------+----------+----------------+-----------------+--------+
| 12:05       | 12:20    | Pollo sudado   | Picar verduras  | Activa |
| 12:20       | 12:30    | Arroz con coco | Lavar y sofreir | Activa |
| 12:20       | 13:00    | Pollo sudado   | Cocer pollo     | Pasiva |
| 12:30       | 13:00    | Arroz con coco | Cocinar arroz   | Pasiva |
+-------------+----------+----------------+-----------------+--------+
```

Con hora actual `12:15` aparece, antes de la tabla, la advertencia con el retraso (10 minutos). El cronograma se imprime igual.

## Reglas principales

- Todo el menú comparte una sola hora de servicio.
- Las tareas de un plato se ejecutan en el orden en que se registran.
- Hay un solo cocinero: dos tareas activas nunca se solapan. Las tareas pasivas sí pueden solaparse con cualquier otra.
- Una tarea activa de otro plato se ubica dentro de una espera pasiva solo si cabe completa.
- Hay conflicto si la hora de inicio más temprana queda antes de la hora actual. Empezar justo a la hora actual no es conflicto.
- Si una tarea debe empezar antes de la medianoche, la hora se muestra como `<00:00`.

## Fuera del alcance

Guardar datos en archivos, editar o eliminar platos ya registrados, varios cocineros, fechas distintas al día actual, recetas precargadas e interfaz gráfica.

## Pruebas

Los casos de prueba están en la carpeta `tests/` como tablas `.md` (datos de entrada, resultado esperado, resultado obtenido y estado). Los de `tests/casos_integracion.md` se ejecutan escribiendo los datos en el programa (`java -cp out Main`).

También hay pruebas automáticas de dos clases:

```bash
javac -d out src/*.java tests/*.java

```

## Equipo

| Integrante | Rol Scrum | Módulo |
|---|---|---|
| Yesid Alejandro Mejía | Product Owner y Developer | Módulo 2: motor algorítmico y planificación |
| Johana Fernández | Scrum Master y Developer | Módulo 1: captura de datos y modelos |
| Jorge Andry Pineda (Andrey) | Developer | Módulo 3: validación de conflictos y reporte |
