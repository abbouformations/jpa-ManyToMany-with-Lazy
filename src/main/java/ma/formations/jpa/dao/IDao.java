package ma.formations.jpa.dao;

import ma.formations.jpa.dtos.PersonneDto;
import java.util.List;

public interface IDao {
    <T> void save(T t);
    List<PersonneDto> getPersonnesByActivite(String description);
    void remove(String firstname,String lastname);
}
