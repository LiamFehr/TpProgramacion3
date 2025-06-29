package frp.utn.edu.backend.View;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import frp.utn.edu.backend.Models.Persona;
import frp.utn.edu.backend.Models.Todo;
import frp.utn.edu.backend.Repository.PersonaRepository;
import frp.utn.edu.backend.Repository.TodoReposirtory;

@Route("")
public class TodoView extends VerticalLayout {

    private final TodoReposirtory todoRepository;
    private final PersonaRepository personaRepository; 
    private final ComboBox<Persona> personaCombo = new ComboBox<>("Responsable");
    private final TextField taskField = new TextField("Tarea");
    private final FlexLayout tareasLayout = new FlexLayout();
    private final List<Todo> currentTareas = new ArrayList<>();
    private final List<Checkbox> checkboxes = new ArrayList<>();

    @Autowired
    public TodoView(TodoReposirtory todoRepository, PersonaRepository personaRepository) {
        this.todoRepository = todoRepository;
        this.personaRepository = personaRepository;

        // Titulazo
        H2 titulo = new H2("Todo");
        //Boton oscuro
        Button toggleDarkMode = new Button("👁️"); // Cambio crucial para trabajar de noche

        toggleDarkMode.addClickListener(e -> {
            getUI().ifPresent(ui -> {
                ui.getPage().executeJs("""
                    const html = document.documentElement;
                    if (html.hasAttribute('theme')) {
                        html.removeAttribute('theme');
                        $0.textContent = '👁️'; // Cambia a modo oscuro donde descansan los ojos
                    } else {
                        html.setAttribute('theme', 'dark');
                        $0.textContent = '🔥'; // Cambia a modo claro donde queman los ojos
                    }
                """, toggleDarkMode.getElement());
            });
        });

        // ComboBox de Personas ordenado por DNI
        personaCombo.setItems(personaRepository.findAll());
        personaCombo.setItemLabelGenerator(p -> p.getNombre() + " " + p.getApellido());
        personaCombo.addValueChangeListener(ev -> actualizarLista(ev.getValue()));

        // Botón Agregar Task
        Button addButton = new Button("Agregar Tarea", click -> {
            if (taskField.isEmpty() || personaCombo.isEmpty()) {
                Notification.show("Complete la tarea y seleccione un responsable.");
                return;
            }
            Todo todo = new Todo(taskField.getValue(), personaCombo.getValue());
            todoRepository.save(todo);
            taskField.clear();
            actualizarLista(personaCombo.getValue());
        });

     // Botón Eliminar Task seleccionados
        Button deleteSelected = new Button("Eliminar tarea", click -> {
            for (int i = 0; i < checkboxes.size(); i++) {
                if (checkboxes.get(i).getValue()) {
                    todoRepository.delete(currentTareas.get(i));
                }
            }
            actualizarLista(personaCombo.getValue());
        });

        // Botón Ir a Personas Giordano (modal)
        Button personasButton = new Button("Ir a Personas", click -> {
            Dialog dialog = new Dialog();

            // Crear la vista PersonaView manualmente (inyectando el repo)
            PersonaView personaView = new PersonaView(personaRepository);

            // Callback para refrescar el combo cuando se agregue/modifique persona
            personaView.setOnPersonaGuardada(() -> {
                personaCombo.setItems(personaRepository.findAll());
            });

            dialog.add(personaView);

            Button closeBtn = new Button("Cerrar", e -> dialog.close());
            dialog.getFooter().add(closeBtn);

            dialog.setWidth("800px");
            dialog.setHeight("700px");

            dialog.open();
        });
        
        // Layout horizontal de inputs y botones
        HorizontalLayout tituloLayout = new HorizontalLayout(titulo, toggleDarkMode);
        tituloLayout.setAlignItems(Alignment.CENTER);
        tituloLayout.setSpacing(true);
        
        HorizontalLayout inputLayout = new HorizontalLayout(taskField, personaCombo);
        inputLayout.setDefaultVerticalComponentAlignment(Alignment.END);
        inputLayout.setSpacing(true);
        
        HorizontalLayout inputLayout2 = new HorizontalLayout(addButton, deleteSelected, personasButton);
        inputLayout2.setDefaultVerticalComponentAlignment(Alignment.END);
        inputLayout2.setSpacing(true);
        
        // Configuración del FlexLayout para las Task
        tareasLayout.setWidth("80%");
        tareasLayout.getStyle().set("flex-wrap", "wrap");  // Permite que los checkbox se distribuyan en varias filas
        tareasLayout.setAlignItems(Alignment.START);
        tareasLayout.getStyle().set("gap", "10px"); // Espacio de objetos!
        
        
        //Layout de lista de task
        tareasLayout.setAlignItems(Alignment.CENTER);
        tareasLayout.setWidth("80%");
        
        
        //Div para encerrar, importante hacer antes del contenedor centrado
        Div card = new Div(tareasLayout);
        card.getStyle()
            .set("background", "var(--lumo-base-color)")
            .set("border-radius", "12px")
            .set("box-shadow", "0 2px 8px rgba(0, 0, 0, 0.1)")
            .set("padding", "20px")
            .set("margin-top", "20px");

        card.setWidth("100%"); // que ocupe todo el ancho posible dentro del contenedor padre
        card.getStyle().set("max-width", "375px"); // pero que no supere un límite máximo
        card.getStyle().set("margin-left", "auto").set("margin-right", "auto"); // y quede centrado
        //card.setWidth("100%");
        //card.getStyle().set("margin-left", "auto").set("margin-right", "auto");

     // Contenedor centrado
        VerticalLayout contenidoLayout = new VerticalLayout();
        contenidoLayout.setPadding(true);
        contenidoLayout.setSpacing(true);
        contenidoLayout.setAlignItems(Alignment.CENTER);
        contenidoLayout.add(tituloLayout, inputLayout, inputLayout2, card);

        // Centrado
        add(contenidoLayout);
        //setSizeFull();		// si esta expandido no funciona el centrado vertical
        getStyle().set("display", "flex");
        getStyle().set("justify-content", "center");	//centra horizontal!
        //getStyle().set("align-items", "start");		//centra vertical!! - no funcionaba porque estaba el SizeFull()
        contenidoLayout.getStyle().set("margin-top", "40px");
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
                cb.getStyle().set("min-width", "135px");	//permite que esten parejos los objetos!
                cb.getStyle().set("margin", "5px");
                checkboxes.add(cb);
                tareasLayout.add(cb);
            }
        }
    }
}
