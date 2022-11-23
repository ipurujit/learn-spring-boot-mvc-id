package chapter.one.LearnSpringBoot.repositories;

import chapter.one.LearnSpringBoot.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public Optional<User> findByEmail(String email);

    public int countByEmail(final String email);
}
