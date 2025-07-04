
package frp.utn.edu.backend.View;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import frp.utn.edu.backend.Models.Persona;
import frp.utn.edu.backend.Repository.PersonaRepository;

@Route("personas")
public class PersonaView extends VerticalLayout {

    private final PersonaRepository personaRepository;
    private final Grid<Persona> grid = new Grid<>(Persona.class, false);

    private final TextField dni       = new TextField("DNI");
    private final TextField nombre    = new TextField("Nombre");
    private final TextField apellido  = new TextField("Apellido");

    private Persona seleccionada = null;
  //Refresheo
    private Runnable onPersonaGuardada;
    public void setOnPersonaGuardada(Runnable callback) {
        this.onPersonaGuardada = callback;
    }

    @Autowired
    public PersonaView(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;

        
        H2 titulo = new H2("Gestion de Personas");

        
        grid.addColumn(Persona::getDni).setHeader("DNI");
        grid.addColumn(Persona::getNombre).setHeader("Nombre");
        grid.addColumn(Persona::getApellido).setHeader("Apellido");
        grid.setWidthFull();
        grid.setHeight("300px");
        grid.asSingleSelect().addValueChangeListener(e -> cargarEnFormulario(e.getValue()));

      
        Button guardar   = new Button("Guardar", e -> guardarPersona());
        Button modificar = new Button("Modificar", e -> modificarPersona());
        Button eliminar  = new Button("Eliminar", e -> eliminarPersona());
        Button buscar    = new Button("Buscar por Nombre", e -> buscarPorNombre());


        
        HorizontalLayout formLayout = new HorizontalLayout(dni, nombre, apellido);
        formLayout.setDefaultVerticalComponentAlignment(Alignment.CENTER);
        formLayout.setSpacing(true);
        dni.setWidth("150px");
        nombre.setWidth("200px");
        apellido.setWidth("200px");

        
        HorizontalLayout buttonsLayout = new HorizontalLayout(guardar, modificar, eliminar, buscar);
        buttonsLayout.setDefaultVerticalComponentAlignment(Alignment.END);
        buttonsLayout.setSpacing(true);

        setPadding(true);
        setSpacing(true);
        add(titulo, formLayout, buttonsLayout, grid);

        setSizeFull();
        refreshGrid();
    }

    private void cargarEnFormulario(Persona p) {
        seleccionada = p;
        if (p != null) {
            dni.setValue(p.getDni());
            nombre.setValue(p.getNombre());
            apellido.setValue(p.getApellido());
            dni.setReadOnly(true);
        }
    }

    private void guardarPersona() {
        if (dni.isEmpty() || nombre.isEmpty() || apellido.isEmpty()) {
            Notification.show("Complete todos los campos");
            return;
        }
        Persona p = new Persona(dni.getValue(), nombre.getValue(), apellido.getValue());
        personaRepository.save(p);
        Notification.show("Persona guardada");
        clearForm();
        refreshGrid();
        if (onPersonaGuardada != null) {
            onPersonaGuardada.run();
        }
    }
    
    
   

    private void modificarPersona() {
        if (seleccionada == null) {
            Notification.show("Seleccione una persona para modificar");
            return;
        }
        seleccionada.setNombre(nombre.getValue());
        seleccionada.setApellido(apellido.getValue());
        personaRepository.save(seleccionada);
        Notification.show("Persona modificada");
        refreshGrid();
    }

    private void eliminarPersona() {
        if (seleccionada == null) {
            Notification.show("Seleccione una persona para eliminar");
            return;
        }
        personaRepository.delete(seleccionada);
        Notification.show("Persona eliminada");
        clearForm();
        refreshGrid();
        
        if (onPersonaGuardada != null) {
            onPersonaGuardada.run();}
    }

    private void buscarPorNombre() {
        if (nombre.isEmpty()) {
            refreshGrid();
        } else {
            grid.setItems(personaRepository.findByNombreContainingIgnoreCase(nombre.getValue()));
        }
    }

    private void clearForm() {
        dni.clear();
        nombre.clear();
        apellido.clear();
        dni.setReadOnly(false);
        seleccionada = null;
    }

    private void refreshGrid() {
        grid.setItems(personaRepository.findAll());
    }
  
}

