package singh.Himanshu.peopledb_web.web.controller;



import jakarta.validation.Valid;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import singh.Himanshu.peopledb_web.biz.model.Person;
import singh.Himanshu.peopledb_web.biz.service.PersonService;
import singh.Himanshu.peopledb_web.data.FileStorageRepository;
import singh.Himanshu.peopledb_web.data.PersonRepository;
import singh.Himanshu.peopledb_web.exception.StorageException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
//import singh.Himanshu.peopledb_web.data.PersonDataLoader;

@Controller
@RequestMapping("/people")
@Log4j2
public class PeopleController {

    public static final String DISPO = """
             attachment; filename="%s"
            """;
    private PersonRepository personRepository;
    private FileStorageRepository fileStorageRepository;
    private PersonService personService;


    public PeopleController(PersonService personService, PersonRepository personRepository, FileStorageRepository fileStorageRepository) {
        this.personRepository = personRepository;
        this.fileStorageRepository = fileStorageRepository;
        this.personService=personService;
    }

//    public PeopleController(PersonRepository personRepository) {
//        this.personRepository = personRepository;
//    }

    @ModelAttribute("people")
    public Page<Person> getPeople(@PageableDefault(size = 5) Pageable page){
        return personService.findAll(page);
    }

    @ModelAttribute
    public Person getPerson(){
        return  new Person();
    }

    @GetMapping
    public  String showPeoplePage(Model model){
        return "people";
    }

    @GetMapping("/images/{resource}")
    public ResponseEntity<Resource> getResource(@PathVariable String resource){
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, format(DISPO,resource))
                .body(fileStorageRepository.findByName(resource));

    }

    @PostMapping
    public String savePerson(Model model, @Valid Person person, Errors errors, @RequestParam("photoFilename") MultipartFile photoFile) throws IOException {
        log.info(person);
        log.info("File Name "+photoFile.getOriginalFilename());
        log.info("File Size "+photoFile.getSize());
        log.info("Errors "+errors);
        if(!errors.hasErrors()){
            try {
                personService.save(person,photoFile.getInputStream());
                return "redirect:people";
            } catch (StorageException e) {
                model.addAttribute("errorMsg","System is currently unable to accept photos");
                return "people";
            }
        }else{
            return "people";
        }
    }

    @PostMapping(params = "action=delete=true")
    public String deletePeople(@RequestParam Optional<List<Long>> Select){
        log.info(Select);
        if (Select.isPresent()) {
//             personRepository.deleteAllById(Select.get());
             personService.deleteAllById(Select.get());
        }
        return "redirect:people";
    }

    @PostMapping(params = "action=edit=true")
    public String editPerson(@RequestParam Optional<List<Long>> Select,Model model){
        log.info(Select);

        if (Select.isPresent()) {
           Optional<Person> person= personRepository.findById(Select.get().get(0));
           model.addAttribute("person",person);
        }
        return "people";
    }

    @PostMapping(params = "actions=true")
    public String importCSV(@RequestParam MultipartFile csvFile){
        log.info("File name: "+csvFile.getOriginalFilename());
        log.info("File size: "+csvFile.getSize());
        try {
            personService.importCSV(csvFile.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return  "redirect:people";
    }
}
