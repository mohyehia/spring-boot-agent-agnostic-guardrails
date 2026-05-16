package com.moh.yehia.demo.architecture.rules;

import com.moh.yehia.demo.SpringBootAgentAgnosticGuardrailsApplication;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.*;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.library.freeze.FreezingArchRule;
import org.springframework.web.bind.annotation.GetMapping;

@AnalyzeClasses(packagesOf = SpringBootAgentAgnosticGuardrailsApplication.class, importOptions = ImportOption.DoNotIncludeTests.class)
public class LoggingConventionTests {

    @ArchTest
    ArchRule loggingConvention = FreezingArchRule.freeze(ArchRuleDefinition.methods()
            .that()
            .areAnnotatedWith(GetMapping.class)
            .should(log()));

    private ArchCondition<? super JavaMethod> log() {
        return new ArchCondition<>("log") {
            @Override
            public void check(JavaMethod method, ConditionEvents events) {
                boolean logs = method.getMethodCallsFromSelf()
                        .stream()
                        .anyMatch(methodCall -> methodCall.getTargetOwner().isEquivalentTo(org.slf4j.Logger.class));
                if (!logs) {
                    String message = String.format("Method %s should log using SLF4J Logger", method.getFullName());
                    events.add(SimpleConditionEvent.violated(method, ConditionEvent.createMessage(method, message)));
                }
            }
        };
    }
}
