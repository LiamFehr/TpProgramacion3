package frp.utn.edu.backend.Service;


import frp.utn.edu.backend.Models.Persona;
import frp.utn.edu.backend.Repository.PersonaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonaService {
    private final PersonaRepository repository;

    public PersonaService(PersonaRepository repository) {
        this.repository = repository;
    }

    public List<Persona> listar() {
        return repository.findAll();
    }

    public void guardar(Persona persona) {
        repository.save(persona);
    }

    public void eliminar(String dni) {
        repository.deleteById(dni);
    }
}
