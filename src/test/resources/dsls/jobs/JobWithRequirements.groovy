package dsls.jobs

import ch.mibex.bamboo.plandsl.dsl.jobs.Requirement

project(key: 'SIMPLEPROJECT', name: 'simple project') {
    plan(key: 'SIMPLEPLAN', name: 'Renamed plan') {
        description "this was a simple plan"
        enabled false

        stage(name: 'simple stage') {
            description "this is simple stage"
            manual false

            job(key: 'SIMPLEJOB', name: 'simple job') {
                description "This was a simple job"
                enabled false

                requirements {
                    requirement(capabilityKey: 'system.builder.gradle.Gradle 2.2',
                                matchType: new Requirement.Equals("2.2")) {
                    }
                    requirement(capabilityKey: 'system.builder.ant.Ant',
                                matchType: new Requirement.Exists()) {
                    }
                    requirement(capabilityKey: 'system.builder.mvn3.maven323',
                                matchType: new Requirement.Matches("[A-Z0-9]*")) {
                    }
                }
            }
        }
    }
}