package ch.polgrabia.springshowcaseweb.services;

import ch.polgrabia.springshowcaseweb.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserDao extends JpaRepository<User, Long> {
}
