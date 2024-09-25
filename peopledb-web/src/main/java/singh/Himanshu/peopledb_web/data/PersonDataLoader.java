package singh.Himanshu.peopledb_web.data;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import singh.Himanshu.peopledb_web.biz.model.Person;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDataLoader implements ApplicationRunner {

    private PersonRepository personRepository;

    public PersonDataLoader(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(personRepository.count()==0){
            List<Person> people= List.of(
//            new Person(null, "Pete", "Doe", LocalDate.of(1990, 5, 20), "",new BigDecimal("50000")),
//            new Person(null, "Jane", "Smith", LocalDate.of(1985, 8, 15), "",new BigDecimal("60000")),
//            new Person(null, "Alex", "Johnson", LocalDate.of(1995, 11, 30), "",new BigDecimal("55000")),
//            new Person(null, "Jennifer", "Davis", LocalDate.of(1988, 3, 22), "",new BigDecimal("62000")),
//            new Person(null, "Akira", "Brown", LocalDate.of(1992, 7, 10), "",new BigDecimal("58000"))
                    );
                    personRepository.saveAll(people);
        }
    }
}
