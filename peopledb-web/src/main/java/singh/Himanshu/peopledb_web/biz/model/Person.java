package singh.Himanshu.peopledb_web.biz.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.bridge.IMessage;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Person {
    @Id
    @GeneratedValue
    private Long id;


    @NotEmpty(message = "First name can not be empty")
    private String firstName;

    @NotEmpty(message = "last name can not be empty")
    private String lastName;

    @Past(message = "DOB must be in past")
    @NotNull(message = "DOB must be specified")
    private LocalDate dob;

    @Email(message = "Email must be valid")
    @NotEmpty(message="Email must not be empty")
    private String email;

    @DecimalMin(value = "1000",message = "salary must be at least 1000")
    @NotNull(message = "salary must not be empty")
    private BigDecimal salary;



    private String photoFilename;


    public static Person parse(String csvLine) {
        String[] fields = csvLine.split(",\\s*");
        LocalDate dob = LocalDate.parse(fields[10], DateTimeFormatter.ofPattern("M/d/yyyy"));
        return new Person(null,fields[2],fields[4],dob,fields[6],new BigDecimal(fields[25]),null);

    }
}
