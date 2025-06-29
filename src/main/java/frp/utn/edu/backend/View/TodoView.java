package frp.utn.edu.backend.View;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import frp.utn.edu.backend.Models.Persona;
import frp.utn.edu.backend.Models.Todo;
import frp.utn.edu.backend.Repository.PersonaRepository;
import frp.utn.edu.backend.Repository.TodoReposirtory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Route("")
public class TodoView extends VerticalLayout {

    private final TodoReposirtory todoRepository;
    private final PersonaRepository personaRepository; 
    private final ComboBox<Persona> personaCombo = new ComboBox<>("Responsable");
    private final TextField taskField = new TextField("Tarea");
    private final VerticalLayout tareasLayout = new VerticalLayout();
    private final List<Todo> currentTareas = new ArrayList<>();
    private final List<Checkbox> checkboxes = new ArrayList<>();

    @Autowired
    public TodoView(TodoReposirtory todoRepository, PersonaRepository personaRepository) {
        this.todoRepository = todoRepository;
        this.personaRepository = personaRepository;

        
        // Titulazo
        H2 titulo = new H2("Todo");

        // ComboBox de Personas
        personaCombo.setItems(personaRepository.findAll());
        personaCombo.setItemLabelGenerator(p -> p.getNombre() + " " + p.getApellido());
        personaCombo.addValueChangeListener(ev -> actualizarLista(ev.getValue()));

        // Botón Agregar
        Button addButton = new Button("Agregar", click -> {
            if (taskField.isEmpty() || personaCombo.isEmpty()) {
                Notification.show("Complete la tarea y seleccione un responsable.");
                return;
            }
            Todo todo = new Todo(taskField.getValue(), personaCombo.getValue());
            todoRepository.save(todo);
            taskField.clear();
            actualizarLista(personaCombo.getValue());
        });

        // Botón Ir a Personas
        Button personasButton = new Button("Ir a Personas", click ->
            getUI().ifPresent(ui -> ui.navigate("personas"))
        );

        // Botón Eliminar seleccionados
        Button deleteSelected = new Button("Eliminar tarea ", click -> {
            for (int i = 0; i < checkboxes.size(); i++) {
                if (checkboxes.get(i).getValue()) {
                    todoRepository.delete(currentTareas.get(i));
                }
            }
            actualizarLista(personaCombo.getValue());
        });

       
        // Layout horizontal de inputs y botones
        HorizontalLayout inputLayout = new HorizontalLayout(
            taskField, addButton, deleteSelected, personasButton );
        HorizontalLayout inputLayout2 = new HorizontalLayout( personaCombo );
        inputLayout.setDefaultVerticalComponentAlignment(Alignment.END);
        inputLayout.setSpacing(true);
        inputLayout2.setDefaultVerticalComponentAlignment(Alignment.END);
        inputLayout2.setSpacing(true);

      
        //vista 
        setPadding(true);
        setSpacing(true);
        add(
            titulo,inputLayout,inputLayout2, tareasLayout );
    }

    private void actualizarLista(Persona responsable) {
        tareasLayout.removeAll();
        currentTareas.clear();
        checkboxes.clear();

        if (responsable != null) {
            List<Todo> tareas = todoRepository.findByResponsable(responsable);
            currentTareas.addAll(tareas);
            for (Todo tarea : tareas) {
                Checkbox cb = new Checkbox(tarea.getDescripcion());
                checkboxes.add(cb);
                tareasLayout.add(cb);
            }
        }
    }
}

