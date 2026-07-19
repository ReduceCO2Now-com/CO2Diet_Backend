# CO2 Diet Backend

CO2 Diet is a privacy-first, open-source nutrition application designed to help users track calories, nutrients, weight progress, and the estimated CO2 impact of their food choices.

The project aims to provide a free, ad-free, fast, and trustworthy alternative to traditional diet tracking applications, while also helping users understand the environmental impact of their meals.

## Project Purpose

The main goal of CO2 Diet is to support users in building healthier and more sustainable eating habits.

The application helps users:

* Track daily calories and macronutrients
* Calculate the estimated CO2 impact of meals
* Search foods from an offline-first food database
* Track weight and progress
* Use the app without unnecessary data collection
* Keep personal data under user control
* Contribute to improving open food and CO2 data

## Core Principles

* Free to use
* No advertisements
* Open source
* Privacy-respecting
* Offline-first
* Fast and simple daily food logging
* Scientifically grounded nutrition and CO2 estimation
* Non-judgemental and supportive user experience

## Backend Responsibility

This repository contains the Java backend side of the CO2 Diet application. It manages the food catalog, nutrition data, CO2 impact calculations, food-data synchronization, barcode lookup, sustainable alternatives, legal documents, optional authentication, and food-data contributions.

Sensitive personal data such as daily meals, weight history, and private progress should remain on the user's device whenever possible.

## Planned MVP Features

* Food search by name, brand, and barcode
* Nutrition storage and meal calculations
* CO2 impact estimates per food item and meal
* Offline-first catalog synchronization
* Legal acceptance, data export, deletion, and GDPR-oriented architecture

## Suggested Architecture

The project is a modular Spring Boot monolith:

```text
co2diet
├── shared       Shared domain primitives and errors
├── catalog      Food products, search, and barcode lookup
├── ingestion    External food-data ingestion adapters
└── app          Application configuration and database migrations
```

This structure keeps the MVP simple while allowing future migration to microservices if needed.

## Tech Stack

* Java 21
* Spring Boot
* Spring Web and Spring Data JPA
* Spring Security
* PostgreSQL
* Flyway or Liquibase
* Lombok
* OpenAPI / Swagger
* Docker

## Open Food Facts Ingestion

The `ingestion` module provides an Open Food Facts (OFF) data source. It fetches one batch of up to 100 products from OFF's v2 search API, sends a custom User-Agent, and maps valid products to normalized `FoodUpsert` records. Malformed products are logged and skipped; unmapped OFF categories are retained and logged for review.

Run the ingestion tests from the repository root:

```bash
mvn -pl ingestion test
```

## Future Improvements

* Barcode scanner integration
* AI-based nutrition extraction and CO2 estimation
* Recipe builder and meal planning
* Wearable integration
* Community food-data contribution and moderation
* Advanced analytics and weekly insights

## Project Status

The project is in its initial backend setup phase. Current development focuses on backend architecture, package structure, food catalog and nutrition modules, CO2 calculations, synchronization APIs, and legal/privacy endpoints.

## License

This project is intended to be open source. The license will be defined later.
