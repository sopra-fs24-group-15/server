package ch.uzh.ifi.hase.soprafs24.repository;

import ch.uzh.ifi.hase.soprafs24.entity.Meme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("memeRepository")
public interface MemeRepository extends JpaRepository<Meme, Long> {
    // Add custom query methods here if needed

    // Example to find memes by URL if needed
    Optional<Meme> findByMemeURL(String memeURL);

    // This is already part of JpaRepository, shown here for example purposes
    @Override
    Optional<Meme> findById(Long id);
}
