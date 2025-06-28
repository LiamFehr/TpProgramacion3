package frp.utn.edu.backend.Models;


import jakarta.persistence.*;
import java.util.List;

@Entity
public class Persona {

    @Id
    private String dni;

    private String nombre;
    private String apellido;
    public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public int getEdad() {
		return edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}

	public List<Todo> getTareas() {
		return tareas;
	}

	public void setTareas(List<Todo> tareas) {
		this.tareas = tareas;
	}

	private int edad;

    @OneToMany(mappedBy = "responsable", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Todo> tareas;
    
    public Persona() {}
    
    public Persona(String nombre, String apellido, String dni) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.dni = dni;
    }

   
  
}
