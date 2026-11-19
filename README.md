# Chuleta Práctica 2: Gestión de Reservas (JavaFX + Scene Builder)

## 1. Estructura del Proyecto Maven (`src/main/java` y `src/main/resources`)

```text
ApellidoNombre_PracticaUD2/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/
    │   │   └── paquete/
    │   │       ├── Main.java
    │   │       ├── modelo/
    │   │       │   └── Reserva.java
    │   │       └── controlador/
    │   │           └── ReservaController.java
    │   └── resources/
    │       └── paquete/
    │           └── vista_reserva.fxml
```

* En el `.fxml`, el nodo raíz debe enlazar el controlador:
  `fx:controller="paquete.controlador.ReservaController"`
* Cada control que leas o modifiques desde Java debe tener su `fx:id` en Scene Builder (ej. `txtNombre`, `dpFechaNacimiento`, `cmbInstalacion`, `btnGuardar`).
* El evento del botón en Scene Builder se asocia a un método del controller (ej. `onAction="#guardarReserva"`).

---

## 2. Comandos Maven Útiles

Ejecutar siempre en la carpeta raíz (donde está `pom.xml`):

* `mvn clean javafx:run` $\rightarrow$ Limpia, compila y arranca la aplicación JavaFX con FXML.
* `mvn clean package` $\rightarrow$ Limpia, compila y genera el `.jar` en `target/`.
* `mvn clean` $\rightarrow$ Borra `target/` si hay cambios de paquetes o caché antigua.

---

## 3. Modelo: `Reserva.java` (Tipos de Datos Adecuados)

No guardes fechas como `String`; usa `LocalDate` (y `LocalTime` o `int` según corresponda):

```java
package paquete.modelo;

import java.time.LocalDate;

public class Reserva {
    private String nombreCliente;
    private String apellidos;
    private String dni;
    private LocalDate fechaNacimiento;
    private String telefono;
    private String correo;
    private String instalacion;
    private String actividad;
    private LocalDate fechaReserva;
    private String hora;
    private int duracionMinutos;
    private String tipoUsuario;
    private String observaciones;

    // Constructor completo, getters y setters
}
```

---

## 4. Lectura de Controles en el `Controller`

```java
@FXML private TextField txtNombre;
@FXML private TextField txtApellidos;
@FXML private TextField txtDni;
@FXML private DatePicker dpFechaNacimiento;
@FXML private TextField txtTelefono;
@FXML private TextField txtCorreo;
@FXML private ComboBox<String> cmbInstalacion;
@FXML private ComboBox<String> cmbActividad;
@FXML private DatePicker dpFechaReserva;
@FXML private TextField txtHora;
@FXML private TextField txtDuracion;
@FXML private ComboBox<String> cmbTipoUsuario;
@FXML private TextArea txtObservaciones;
```

* Texto sin espacios de relleno: `txtNombre.getText() == null ? "" : txtNombre.getText().trim()`
* Fecha de `DatePicker`: `LocalDate fecha = dpFechaNacimiento.getValue();` (puede ser `null` si no se ha elegido).
* Opción de `ComboBox`: `String opcion = cmbInstalacion.getValue();` (puede ser `null` o estar en blanco si no hay selección válida).

---

## 5. Validaciones por Métodos Separados

Organiza cada comprobación en un método privado del `Controller` para no meter todo en un único bloque:

```java
private boolean esTextoValido(String valor) {
    return valor != null && !valor.trim().isEmpty();
}

private boolean esDniValido(String dni) {
    if (dni == null) return false;
    String limpio = dni.trim().toUpperCase();
    return limpio.matches("^[0-9]{8}[A-Z]$");
}

private boolean esTelefonoValido(String tel) {
    if (tel == null) return false;
    return tel.trim().matches("^[0-9]{9}$");
}

private boolean esCorreoValido(String email) {
    if (email == null) return false;
    return email.trim().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
}

private int parsearDuracionValida(String textoDuracion) {
    if (!esTextoValido(textoDuracion)) return -1;
    try {
        int minutos = Integer.parseInt(textoDuracion.trim());
        return minutos > 0 ? minutos : -1;
    } catch (NumberFormatException e) {
        return -1;
    }
}

private boolean sonFechasCoherentes(LocalDate nacimiento, LocalDate reserva) {
    if (nacimiento == null || reserva == null) return false;
    LocalDate hoy = LocalDate.now();
    boolean nacimientoOk = nacimiento.isBefore(hoy);
    boolean reservaOk = !reserva.isBefore(hoy);
    return nacimientoOk && reservaOk;
}
```

---

## 6. Gestión Unificada de Errores (`Alert`) y Creación de `Reserva`

Acumula **todos** los errores en una lista o `StringBuilder` y muéstralos en **un único `Alert`**:

```java
@FXML
private void guardarReserva() {
    List<String> errores = new ArrayList<>();

    String nombre = txtNombre.getText();
    String apellidos = txtApellidos.getText();
    String dni = txtDni.getText();
    LocalDate fechaNac = dpFechaNacimiento.getValue();
    String telefono = txtTelefono.getText();
    String correo = txtCorreo.getText();
    String instalacion = cmbInstalacion.getValue();
    String actividad = cmbActividad.getValue();
    LocalDate fechaRes = dpFechaReserva.getValue();
    String hora = txtHora.getText();
    String duracionTexto = txtDuracion.getText();
    String tipoUsuario = cmbTipoUsuario.getValue();
    String observaciones = txtObservaciones.getText();

    if (!esTextoValido(nombre)) errores.add("El nombre es obligatorio.");
    if (!esTextoValido(apellidos)) errores.add("Los apellidos son obligatorios.");
    if (!esDniValido(dni)) errores.add("El DNI no es válido.");
    if (fechaNac == null) errores.add("Debes indicar la fecha de nacimiento.");
    if (!esTelefonoValido(telefono)) errores.add("El teléfono no es válido.");
    if (!esCorreoValido(correo)) errores.add("El correo electrónico no es válido.");
    if (!esTextoValido(instalacion)) errores.add("Debes seleccionar una instalación.");
    if (!esTextoValido(actividad)) errores.add("Debes seleccionar una actividad.");
    if (fechaRes == null) errores.add("Debes indicar una fecha de reserva.");
    if (!esTextoValido(hora)) errores.add("Debes indicar la hora.");
    int duracion = parsearDuracionValida(duracionTexto);
    if (duracion <= 0) errores.add("La duración debe ser un número mayor que 0.");
    if (!esTextoValido(tipoUsuario)) errores.add("Debes seleccionar un tipo de usuario.");
    if (fechaNac != null && fechaRes != null && !sonFechasCoherentes(fechaNac, fechaRes)) {
        errores.add("Las fechas introducidas no son coherentes.");
    }

    if (!errores.isEmpty()) {
        String mensaje = "No se puede guardar la reserva. Corrige los siguientes errores:\n"
                + String.join("\n", errores);
        mostrarAlerta(Alert.AlertType.ERROR, "Errores de validación", mensaje);
        return; // No crea el objeto Reserva
    }

    Reserva reserva = new Reserva(
            nombre.trim(), apellidos.trim(), dni.trim().toUpperCase(),
            fechaNac, telefono.trim(), correo.trim(),
            instalacion, actividad, fechaRes, hora.trim(),
            duracion, tipoUsuario, observaciones == null ? "" : observaciones.trim()
    );

    mostrarAlerta(Alert.AlertType.INFORMATION, "Reserva registrada",
            "La reserva se ha registrado correctamente.");
}

private void mostrarAlerta(Alert.AlertType tipo, String titulo, String contenido) {
    Alert alert = new Alert(tipo);
    alert.setTitle(titulo);
    alert.setHeaderText(null);
    alert.setContentText(contenido);
    alert.showAndWait();
}
```

---

## 7. Checklist Antes de Comprimir (`ApellidoNombre_PracticaUD2.zip`)

* Interfaz hecha en Scene Builder con los 13 campos y el botón **GUARDAR RESERVA**.
* `fx:id` y `onAction` revisados en el `.fxml` y conectados con el `Controller`.
* Clase `Reserva` con `LocalDate` y tipos apropiados (sin guardar fechas como `String`).
* Validaciones separadas en métodos y un solo `Alert` agrupando todos los fallos; no se instancia `Reserva` si hay errores.
* Captura de pantalla de Scene Builder incluida junto al proyecto (`src/main/java`, `src/main/resources` y `pom.xml`).
