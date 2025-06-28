package frp.utn.edu.backend.Repository;


import frp.utn.edu.backend.Models.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoReposirtory extends JpaRepository<Todo, Long> {}
