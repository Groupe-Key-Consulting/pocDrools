import com.kc.poc.drools.model.Applicant;
import com.kc.poc.drools.model.SuggestedRole;
import com.kc.poc.drools.service.ApplicantService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ApplicantRulesTest {

    @Test
    public void whenCriteriaMatching_ThenSuggestManagerRole(){
        Applicant applicant = new Applicant("Niko", 37, 1600000.0,11);
        SuggestedRole suggestedRole = new SuggestedRole();
        ApplicantService applicantService = new ApplicantService();

        applicantService.suggestARoleForApplicant(applicant, suggestedRole);

        assertEquals("Manager", suggestedRole.getRole());
    }
}
