package singh.Himanshu.peopledb_web.biz.service;


import org.aspectj.weaver.patterns.PerObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import singh.Himanshu.peopledb_web.biz.model.Person;
import singh.Himanshu.peopledb_web.data.FileStorageRepository;
import singh.Himanshu.peopledb_web.data.PersonRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import java.util.zip.ZipInputStream;

@Service
public class PersonService {
    private final PersonRepository personRepository;
    private final FileStorageRepository storageRepository;

    public PersonService(PersonRepository personRepository, FileStorageRepository storageRepository) {
        this.personRepository = personRepository;
        this.storageRepository = storageRepository;
    }

    @Transactional
    public Person save(Person person, InputStream photoStream) {
        Person savedPerson= personRepository.save(person);
        storageRepository.save(person.getPhotoFilename(),photoStream);
        return savedPerson;
    }

    public Optional<Person> findById(Long aLong) {
        return personRepository.findById(aLong);
    }

    public Iterable<Person> findAll() {
        return personRepository.findAll();
    }

    public void deleteAllById(Iterable<Long> ids) {
//        Iterable<Person> peopleToDelete=personRepository.findAllById(ids);
//        Stream<Person> peopleStream= StreamSupport.stream(peopleToDelete.spliterator(), false);
//        Set<String> filenames= peopleStream
//                .map(Person::getPhotoFilename)
//                        .collect(Collectors.toSet());

        Set<String> filenames=  personRepository.findFilenamesByIds(ids);
        personRepository.deleteAllById(ids);
        storageRepository.deleteAllByName(filenames);
    }

    public Page<Person> findAll(Pageable pageable) {
        return personRepository.findAll(pageable);
    }

    public void importCSV(InputStream csvFileStream) {
        try {
            ZipInputStream zipInputStream = new ZipInputStream(csvFileStream);
            zipInputStream.getNextEntry();
            InputStreamReader inputStreamReader = new InputStreamReader(zipInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            bufferedReader.lines()
                    .skip(1)
                    .limit(20)
                    .map(Person::parse)
                    .forEach(personRepository::save);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
