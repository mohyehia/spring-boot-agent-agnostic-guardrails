package com.moh.yehia.demo.architecture.rules;

import com.moh.yehia.demo.SpringBootAgentAgnosticGuardrailsApplication;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.springframework.web.bind.annotation.RestController;

@AnalyzeClasses(packagesOf = SpringBootAgentAgnosticGuardrailsApplication.class, importOptions = ImportOption.DoNotIncludeTests.class)
public class MethodRulesTests {
    @ArchTest
    ArchRule methodsInControllerShouldNotBeFinalOrStatic = ArchRuleDefinition.methods()
            .that()
            .areDeclaredInClassesThat()
            .areAnnotatedWith(RestController.class)
            .should()
            .notBeFinal()
            .andShould()
            .notBeStatic()
            .because("Controller methods should be non-final and non-static to allow for proper proxying and testing");

    /*
     * This test is commented out because we don't have the spring-security dependency,
     *  but in a real application, you would want to enforce that service methods are not secured with @Secured or @PreAuthorize,
     *  as security should be applied at the controller layer.
     * */
//    @ArchTest
//    ArchRule methodsInServiceShouldNotBeSecured = ArchRuleDefinition.methods()
//            .that()
//            .areDeclaredInClassesThat()
//            .areAnnotatedWith(Service.class)
//            .should()
//            .notBeAnnotatedWith(Secured.class)
//            .andShould()
//            .notBeAnnotatedWith(PreAuthorize.class)
//            .because("Service methods should not be directly secured with @Secured, security should be applied at the controller layer");
}
