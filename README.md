# Visa Requirement – Config Driven Rule Engine (Java)

##  Overview

This project is a **core Java rule evaluation system** that determines whether a traveler requires a visa for a given destination country.

The system is **config-driven**, meaning all visa rules are loaded from an external configuration file (JSON / YAML), not hardcoded in Java logic.

This assignment focuses on **clean OOP design, enums, collections, immutability, and defensive coding**, without using any frameworks or databases.

---

##  Objective

Build a rule engine that evaluates visa requirements based on:
- Destination country
- Passport country
- Travel purpose
- Intended stay duration

The engine returns a structured decision describing visa requirements, documents, and warnings.

---

##  Inputs

- `Country` (enum) – Destination country
- `Country` (enum) – Passport country
- `TravelPurpose` (enum)
- `int stayDuration` – number of days

---

##  Output

A `VisaDecision` object containing:

- `boolean visaRequired`
- `VisaType visaType`
- `List<DocumentType> requiredDocuments`
- `int estimatedProcessingDays`
- `List<String> warnings`

---

##  Configuration (Rules)

Visa rules are **loaded from an external config file**:

- Supported formats: **JSON or YAML**
- Example: `rules.json`

Rules must define:
- Applicable countries
- Passport nationality
- Travel purpose
- Maximum allowed stay
- Visa requirement
- Visa type
- Required documents
- Processing duration
- Warnings (if any)

 **No hardcoded country logic is allowed in Java code.**

---

##  Technical Constraints

###  Must Use
- Java 8+
- Enums (`Country`, `VisaType`, `DocumentType`, `TravelPurpose`)
- Immutable DTOs
- Java Collections
- `Optional` (where appropriate)

###  Must NOT Use
- Spring / Spring Boot
- Hibernate / JPA
- Any database
- Hardcoded rules per country
- Reflection hacks

---

##  Core Design Components

### `RuleLoader`
- Reads rule definitions from JSON / YAML
- Performs basic validation
- Converts config data into Java objects

### `RuleRepository`
- Stores all loaded rules in memory
- Provides rule lookup utilities

### `VisaRuleEvaluator`
- Core business logic
- Matches input against rules
- Handles conflicts and edge cases

### `VisaDecision`
- Immutable output DTO
- Represents final visa evaluation result

---

##  Required Test Scenarios

Interns must implement tests for:

1. Valid rule match
2. No rule found
3. Multiple rule conflict
4. Invalid input handling
5. Missing or malformed config fields

---

##  Project Structure (Example)

