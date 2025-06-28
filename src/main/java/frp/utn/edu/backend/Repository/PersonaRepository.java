package frp.utn.edu.backend.Repository;

import frp.utn.edu.backend.Models.Persona;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonaRepository extends JpaRepository<Persona, String> {}