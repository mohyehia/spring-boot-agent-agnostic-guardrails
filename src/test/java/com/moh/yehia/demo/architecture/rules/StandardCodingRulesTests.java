package com.moh.yehia.demo.architecture.rules;

import com.moh.yehia.demo.SpringBootAgentAgnosticGuardrailsApplication;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.DependencyRules;
import com.tngtech.archunit.library.GeneralCodingRules;

@AnalyzeClasses(packagesOf = SpringBootAgentAgnosticGuardrailsApplication.class, importOptions = ImportOption.DoNotIncludeTests.class)
public class StandardCodingRulesTests {
    @ArchTest
    ArchRule useLoggerInsteadOfSystemOut = GeneralCodingRules.NO_CLASSES_SHOULD_ACCESS_STANDARD_STREAMS;

    @ArchTest
    ArchRule noFieldInjection = GeneralCodingRules.NO_CLASSES_SHOULD_USE_FIELD_INJECTION;

    @ArchTest
    ArchRule testClassesShouldBeInSamePackageAsImplementation = GeneralCodingRules.testClassesShouldResideInTheSamePackageAsImplementation();

    @ArchTest
    ArchRule noGenericExceptions = GeneralCodingRules.NO_CLASSES_SHOULD_THROW_GENERIC_EXCEPTIONS;

    @ArchTest
    ArchRule deprecatedFunctionShouldNotBeUsed = GeneralCodingRules.DEPRECATED_API_SHOULD_NOT_BE_USED;

    @ArchTest
    ArchRule test = DependencyRules.NO_CLASSES_SHOULD_DEPEND_UPPER_PACKAGES;
}
