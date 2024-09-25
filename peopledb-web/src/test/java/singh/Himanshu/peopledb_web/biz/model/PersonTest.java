package singh.Himanshu.peopledb_web.biz.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.FactoryBasedNavigableListAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class PersonTest {

    @Test
    public void canParse(){
        String csvLine="";
        Person person=Person.parse(csvLine);

    }
}