package com.moh.yehia.demo.architecture.rules;

import com.moh.yehia.demo.SpringBootAgentAgnosticGuardrailsApplication;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.ProxyRules;
import com.tngtech.archunit.library.freeze.FreezingArchRule;
import de.rweisleder.archunit.spring.framework.SpringAsyncRules;
import de.rweisleder.archunit.spring.framework.SpringCacheRules;
import de.rweisleder.archunit.spring.framework.SpringComponentRules;
import org.springframework.scheduling.annotation.Async;

@AnalyzeClasses(packagesOf = SpringBootAgentAgnosticGuardrailsApplication.class, importOptions = ImportOption.DoNotIncludeTests.class)

public class SpringProxyRulesTest {
    @ArchTest
    ArchRule caching = FreezingArchRule.freeze(SpringCacheRules.CacheableMethodsNotCalledFromSameClass);

    @ArchTest
    ArchRule noBypassOfProxyLogic = ProxyRules.no_classes_should_directly_call_other_methods_declared_in_the_same_class_that_are_annotated_with(Async.class);

    @ArchTest
    ArchRule asyncMethodsNotCalledFromSameClass = SpringAsyncRules.AsyncMethodsNotCalledFromSameClass;

    @ArchTest
    ArchRule dependenciesOfServices = SpringComponentRules.DependenciesOfServices;

    @ArchTest
    ArchRule dependenciesOfRepositories = SpringComponentRules.DependenciesOfRepositories;

}
