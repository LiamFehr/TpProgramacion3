package frp.utn.edu.backend.Service;


import frp.utn.edu.backend.Models.Todo;
import frp.utn.edu.backend.Repository.TodoReposirtory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {
    private final TodoReposirtory repository;

    public TodoService(TodoReposirtory repository) {
        this.repository = repository;
    }

    public List<Todo> listar() {
        return repository.findAll();
    }

    public void guardar(Todo tarea) {
        repository.save(tarea);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
