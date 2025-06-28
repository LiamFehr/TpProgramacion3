package frp.utn.edu.backend.View;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import frp.utn.edu.backend.Models.Persona;
import frp.utn.edu.backend.Models.Todo;
import frp.utn.edu.backend.Repository.PersonaRepository;
import frp.utn.edu.backend.Repository.TodoReposirtory;
import org.springframework.beans.factory.annotation.Autowired;

@Route("")
public class TodoView extends VerticalLayout {

    private final TodoReposirtory todoRepository;
    private final PersonaRepository personaRepository;

    private final Grid<Todo> grid = new Grid<>(Todo.class);
    private final TextField descripcion = new TextField("Descripción de la tarea");
    private final ComboBox<Persona> personaComboBox = new ComboBox<>("Responsable");

    @Autowired
    public TodoView(TodoReposirtory TodoRepository, PersonaRepository personaRepository) {
        this.todoRepository = TodoRepository;
        this.personaRepository = personaRepository;

        personaComboBox.setItems(personaRepository.findAll());
        personaComboBox.setItemLabelGenerator(p -> p.getNombre() + " " + p.getApellido());

        Button addButton = new Button("Agregar Tarea", e -> {
            if (descripcion.isEmpty() || personaComboBox.isEmpty()) {
                Notification.show("Complete todos los campos");
                return;
            }

            Todo todo = new Todo(descripcion.getValue(), personaComboBox.getValue());
            todoRepository.save(todo);
            actualizarGrid();
            descripcion.clear();
            personaComboBox.clear();
        });

        grid.setColumns("descripcion");
        grid.addColumn(t -> t.getResponsable().getNombre() + " " + t.getResponsable().getApellido())
             .setHeader("Responsable");

        Button irAPersonas = new Button("Agregar Persona", e ->
                getUI().ifPresent(ui -> ui.navigate("personas")));

        add(descripcion, personaComboBox, addButton, irAPersonas, grid);
        actualizarGrid();
    }

    private void actualizarGrid() {
        grid.setItems(todoRepository.findAll());
    }
}
