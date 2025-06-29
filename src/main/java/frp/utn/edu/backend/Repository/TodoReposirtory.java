package frp.utn.edu.backend.Repository;


import frp.utn.edu.backend.Models.Persona;
import frp.utn.edu.backend.Models.Todo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoReposirtory extends JpaRepository<Todo, Long> 
{
List<Todo> findByResponsable(Persona responsable);
}