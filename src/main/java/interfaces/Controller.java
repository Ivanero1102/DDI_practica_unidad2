package interfaces;

import java.util.ArrayList;
import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class Controller {
    @FXML private TextField textFieldNombre;
    @FXML private TextField textFieldApellidos;
    @FXML private TextField textFieldDNI;
    @FXML private TextField textFieldTelefono;
    @FXML private TextField textFieldCorreoElectronico;
    @FXML private TextField textFieldObservaciones;

    @FXML private DatePicker datePickerFechaNacimiento;
    @FXML private DatePicker datePickerFechaReserva;

    @FXML private ChoiceBox<String> choiceBoxInstalaciones;
    @FXML private ChoiceBox<String> choiceBoxActividad;
    @FXML private ChoiceBox<String> choiceBoxHora;
    @FXML private ChoiceBox<String> choiceBoxDuracion;
    @FXML private ChoiceBox<String> choiceBoxTipoUsuario;

    @FXML private Button Button;

    @FXML
    public void initialize() {
        choiceBoxInstalaciones.getItems().addAll("Pádel", "Tenis", "Piscina", "Gimnasio");
        choiceBoxActividad.getItems().addAll("Libre", "Dirigida", "Entrenamiento");
        choiceBoxHora.getItems().addAll("09:00", "10:30", "12:00", "16:00", "18:30");
        choiceBoxDuracion.getItems().addAll("30 minutos", "60 minutos", "90 minutos");
        choiceBoxTipoUsuario.getItems().addAll("Socio", "No Socio", "Bono");
    }

    @FXML
    private void handleGuardarReserva() {
        List<String> errores = new ArrayList<>();

        validarTexto(textFieldNombre, "El campo 'Nombre' es obligatorio.", errores);
        validarTexto(textFieldApellidos, "El campo 'Apellidos' es obligatorio.", errores);

        String dni = textFieldDNI.getText();
        if (dni == null || !dni.trim().matches("^[0-9]{8}[A-Za-z]$")) {
            errores.add("El DNI no es válido (ej. 12345678A).");
        }

        String tlf = textFieldTelefono.getText();
        if (tlf == null || !tlf.trim().matches("^[0-9]{9}$")) {
            errores.add("El teléfono debe contener 9 dígitos.");
        }

        String correo = textFieldCorreoElectronico.getText();
        if (correo == null || !correo.trim().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            errores.add("El correo electrónico no es válido.");
        }

        if (choiceBoxInstalaciones.getValue() == null) errores.add("Debes seleccionar una instalación.");
        if (choiceBoxActividad.getValue() == null) errores.add("Debes seleccionar una actividad.");
        if (choiceBoxHora.getValue() == null) errores.add("Debes seleccionar una hora.");
        if (choiceBoxDuracion.getValue() == null) errores.add("Debes seleccionar una duración.");
        if (choiceBoxTipoUsuario.getValue() == null) errores.add("Debes seleccionar un tipo de usuario.");

        if (datePickerFechaNacimiento.getValue() == null) {
            errores.add("Debes indicar la fecha de nacimiento.");
        }
        if (datePickerFechaReserva.getValue() == null) {
            errores.add("Debes indicar la fecha de reserva.");
        }

        if (!errores.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error de validación");
            alert.setHeaderText("No se puede guardar la reserva. Corrige los siguientes errores:");
            alert.setContentText(String.join("\n• ", errores));
            alert.showAndWait();
        } else {
            Reserva nuevaReserva = new Reserva(
                textFieldNombre.getText().trim(),
                textFieldApellidos.getText().trim(),
                textFieldDNI.getText().trim(),
                datePickerFechaNacimiento.getValue(),
                textFieldTelefono.getText().trim(),
                textFieldCorreoElectronico.getText().trim(),
                choiceBoxInstalaciones.getValue(),
                choiceBoxActividad.getValue(),
                datePickerFechaReserva.getValue(),
                choiceBoxHora.getValue(),
                choiceBoxDuracion.getValue(),
                choiceBoxTipoUsuario.getValue(),
                textFieldObservaciones.getText() != null ? textFieldObservaciones.getText().trim() : ""
            );

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Éxito");
            alert.setHeaderText(null);
            alert.setContentText("¡La reserva se ha registrado correctamente!");
            alert.showAndWait();
        }
    }

    private void validarTexto(TextField campo, String mensaje, List<String> errores) {
        if (campo.getText() == null || campo.getText().trim().isEmpty()) {
            errores.add(mensaje);
        }
    }
}
