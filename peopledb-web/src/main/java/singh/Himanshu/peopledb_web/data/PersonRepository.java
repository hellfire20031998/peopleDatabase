package singh.Himanshu.peopledb_web.data;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import singh.Himanshu.peopledb_web.biz.model.Person;

import java.util.Set;

@Repository
public interface PersonRepository extends CrudRepository<Person,Long>, PagingAndSortingRepository<Person,Long> {

    @Query(nativeQuery = true,value = "SELECT photo_filename FROM PERSON where id in:ids")
    public Set<String> findFilenamesByIds(@Param("ids") Iterable<Long> ids);
}
