package com.moh.yehia.demo.architecture.rules;

import com.moh.yehia.demo.SpringBootAgentAgnosticGuardrailsApplication;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import de.rweisleder.archunit.spring.SpringAnnotationPredicates;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

@AnalyzeClasses(packagesOf = SpringBootAgentAgnosticGuardrailsApplication.class, importOptions = ImportOption.DoNotIncludeTests.class)
public class NamingConventionTests {

    @ArchTest
    ArchRule controllerNaming = ArchRuleDefinition.classes()
            .that()
            .areAnnotatedWith(RestController.class)
            .should()
            .haveSimpleNameEndingWith("Controller");


    @ArchTest
    ArchRule serviceNaming = ArchRuleDefinition.classes()
            .that()
            .areAnnotatedWith(Service.class)
            .should()
            .haveSimpleNameEndingWith("Service");

    @ArchTest
    ArchRule repositoryNaming = ArchRuleDefinition.classes()
            .that()
            .areAnnotatedWith(Repository.class)
            .should()
            .haveSimpleNameEndingWith("Repository");

    @ArchTest
    ArchRule configurationNaming = ArchRuleDefinition.classes()
            .that()
            .areAnnotatedWith(Configuration.class)
            .should()
            .haveSimpleNameEndingWith("Config");

    @ArchTest
    ArchRule springControllerNaming = ArchRuleDefinition.classes()
            .that(SpringAnnotationPredicates.springAnnotatedWith(RestController.class))
            .should()
            .haveSimpleNameEndingWith("Controller");

}
