package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("templateRepository")
public interface TemplateRepository extends JpaRepository<Template, Long> {

    Optional<Template> findByUrl(String url);

    @Override
    Optional<Template> findById(Long id);
}