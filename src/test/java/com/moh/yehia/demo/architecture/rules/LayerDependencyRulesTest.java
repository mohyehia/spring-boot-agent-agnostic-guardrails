package com.moh.yehia.demo.architecture.rules;

import com.moh.yehia.demo.SpringBootAgentAgnosticGuardrailsApplication;
import com.tngtech.archunit.core.domain.properties.CanBeAnnotated;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.library.Architectures;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

@AnalyzeClasses(packagesOf = SpringBootAgentAgnosticGuardrailsApplication.class, importOptions = ImportOption.DoNotIncludeTests.class)
public class LayerDependencyRulesTest {

//    @ArchTest
//    PlantUmlArchCondition plantUmlDiagramTest = PlantUmlArchCondition.adhereToPlantUmlDiagram(
//            "src/test/resources/architecture_diagram.puml",
//            PlantUmlArchCondition.Configuration.consideringOnlyDependenciesInDiagram()
//    );

    // to include tests from another class
//    @ArchTest
//    ArchTests allTests = ArchTests.in(LayerDependencyRulesTest.class);

    @ArchTest
    ArchRule layers = Architectures.layeredArchitecture()
            .consideringOnlyDependenciesInLayers()
            .layer("controller").definedBy(CanBeAnnotated.Predicates.annotatedWith(RestController.class))
            .layer("service").definedBy(CanBeAnnotated.Predicates.annotatedWith(Service.class))
            .layer("repository").definedBy(CanBeAnnotated.Predicates.annotatedWith(Repository.class))
            .whereLayer("controller").mayNotBeAccessedByAnyLayer()
            .whereLayer("service").mayOnlyBeAccessedByLayers("controller", "service")
            .whereLayer("repository").mayOnlyBeAccessedByLayers("service");


    @ArchTest
    ArchRule servicesShouldNotAccessControllers = ArchRuleDefinition.noClasses()
            .that().areAnnotatedWith(Service.class)
            .should().accessClassesThat().areAnnotatedWith(RestController.class);

    @ArchTest
    ArchRule repositoriesShouldNotAccessService = ArchRuleDefinition.noClasses()
            .that()
            .areAnnotatedWith(Repository.class)
            .should()
            .accessClassesThat().areAnnotatedWith(Service.class);

    @ArchTest
    ArchRule servicesShouldOnlyBeAccessedByControllersOrOtherServices = ArchRuleDefinition.classes()
            .that().areAnnotatedWith(Service.class)
            .should().onlyBeAccessed().byAnyPackage("..controller..", "..service..");

    @ArchTest
    ArchRule repositoriesMustResideInRepositoryPackage = ArchRuleDefinition.classes()
            .that()
            .areAnnotatedWith(Repository.class)
            .should()
            .resideInAPackage("..repository..")
            .because("Repositories should be in the repository package for better organization and clarity");

    @ArchTest
    ArchRule servicesMustResideInServicePackage = ArchRuleDefinition.classes()
            .that()
            .areAnnotatedWith(Service.class)
            .should()
            .resideInAPackage("..service..")
            .because("Services should be in the service package for better organization and clarity");

    @ArchTest
    ArchRule controllersMustResideInControllerPackage = ArchRuleDefinition.classes()
            .that()
            .areAnnotatedWith(RestController.class)
            .should()
            .resideInAPackage("..controller..")
            .because("Controllers should be in the controller package for better organization and clarity");

    @ArchTest
    ArchRule configurationsMustResideInConfigPackage = ArchRuleDefinition.classes()
            .that()
            .areAnnotatedWith(Configuration.class)
            .should()
            .resideInAPackage("..config..")
            .because("Configuration classes should be in the config package for better organization and clarity");


}
