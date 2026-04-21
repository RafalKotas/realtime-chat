# SonarCloud & Test Coverage

## SonarCloud configuration

- `sonar.projectKey` – unique identifier of the project in SonarCloud
- `sonar.organization` – links project to SonarCloud organization

## JaCoCo

JaCoCo is used to collect test coverage data.

### Executions

- `prepare-agent`
  - attaches JaCoCo agent to JVM during test phase
  - enables runtime collection of coverage data

- `report`
  - generates XML report after tests
  - used by SonarCloud to calculate coverage metrics