package interfaces;

import java.time.LocalDate;

public class Reserva {
    private String nombre;
    private String apellidos;
    private String dni;
    private LocalDate fechaNacimiento;
    private String telefono;
    private String correo;
    private String instalacion;
    private String actividad;
    private LocalDate fechaReserva;
    private String hora;
    private String duracion;
    private String tipoUsuario;
    private String observaciones;

    public Reserva(String nombre, String apellidos, String dni, LocalDate fechaNacimiento, 
                   String telefono, String correo, String instalacion, String actividad, 
                   LocalDate fechaReserva, String hora, String duracion, 
                   String tipoUsuario, String observaciones) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.correo = correo;
        this.instalacion = instalacion;
        this.actividad = actividad;
        this.fechaReserva = fechaReserva;
        this.hora = hora;
        this.duracion = duracion;
        this.tipoUsuario = tipoUsuario;
        this.observaciones = observaciones;
    }

    public String getNombre() { return nombre; }
    public String getApellidos() { return apellidos; }
    public String getDni() { return dni; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public String getTelefono() { return telefono; }
    public String getCorreo() { return correo; }
    public String getInstalacion() { return instalacion; }
    public String getActividad() { return actividad; }
    public LocalDate getFechaReserva() { return fechaReserva; }
    public String getHora() { return hora; }
    public String getDuracion() { return duracion; }
    public String getTipoUsuario() { return tipoUsuario; }
    public String getObservaciones() { return observaciones; }
}
