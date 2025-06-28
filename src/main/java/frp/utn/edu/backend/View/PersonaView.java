package frp.utn.edu.backend.View;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import frp.utn.edu.backend.Models.Persona;
import frp.utn.edu.backend.Repository.PersonaRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Route("personas")
public class PersonaView extends VerticalLayout {

    private final PersonaRepository personaRepository;
    private final Grid<Persona> grid = new Grid<>(Persona.class);
    private final TextField nombre = new TextField("Nombre");
    private final TextField apellido = new TextField("Apellido");
    private final TextField dni = new TextField("DNI");

    @Autowired
    public PersonaView(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;

        grid.setColumns("nombre", "apellido", "dni");
        grid.setItems(personaRepository.findAll());

        Button addButton = new Button("Agregar", e -> {
            if (!nombre.isEmpty() && !apellido.isEmpty() && !dni.isEmpty()) {
                Persona p = new Persona(nombre.getValue(), apellido.getValue(), dni.getValue());
                personaRepository.save(p);
                actualizarGrid();
                nombre.clear();
                apellido.clear();
                dni.clear();
            }
        });

        add(nombre, apellido, dni, addButton, grid);
    }

    private void actualizarGrid() {
        grid.setItems(personaRepository.findAll());
    }
}
