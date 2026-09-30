# Design

This document describes the architecture of Java Error Reporting.

The user perspective, features, requirements, and acceptance scenarios are defined in [User and System Requirements](../requirements/requirements.md). This design focuses on the public Java API, the rendering pipeline, and the packaging structure inferred from the source and tests.

## Structure

### Introduction and Goals

The library realizes error-message construction, placeholder substitution, quoting, mitigation formatting, and Java-module integration through a small in-memory builder and rendering pipeline.

### Architecture Constraints

See [Architecture Constraints](constraints.md).

### Context and Scope

See [Context and Scope](context_and_scope.md).

### Solution Strategy

See [Solution Strategy](solution_strategy.md).

### Building Block View

See [Building Block View](building_block_view.md).

### Runtime View

See [Runtime View](runtime_view.md).

### Deployment View

See [Deployment View](deployment_view.md).

### Crosscutting Concepts

See [Crosscutting Concepts](crosscutting_concepts.md).

### Architecture Decisions

See [Architecture Decisions](architecture_decisions.md).

### Quality Requirements

See [Quality Requirements](quality_requirements.md).

### Risks and Technical Debt

See [Risks and Technical Debt](risks_and_technical_debt.md).

### Glossary

See [Glossary](glossary.md).

### Open Issues

See [Open Issues](open_issues.md).
