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

This repository contains the Java backend side of the CO2 Diet application.

The backend is responsible for:

* Food catalog management
* Nutrition data management
* CO2 impact calculation
* Food database synchronization with the mobile app
* Barcode/product lookup support
* Sustainable food alternatives
* Legal document and version handling
* Optional authentication support
* User feedback and food data contribution handling

By default, sensitive personal data such as daily meals, weight history, and private progress data should remain on the user's device whenever possible.

## Planned MVP Features

### Food Database

* Search food by name
* Search food by brand
* Search food by barcode
* Store calories and macronutrient data
* Support offline synchronization with the mobile application

### Nutrition Calculation

* Calculate calories based on portion size
* Calculate protein, carbohydrates, fat, sugar, fiber, and sodium
* Provide nutrition values per meal and per serving

### CO2 Impact Calculation

* Estimate CO2 impact per food item
* Calculate CO2 impact per meal
* Support daily and weekly CO2 tracking
* Include confidence level and data source where possible

### Synchronization

* Provide food catalog versioning
* Allow the mobile app to download updated food and CO2 data
* Support offline-first usage

### Legal and Privacy

* Terms of Use acceptance
* Privacy Policy acceptance
* Health disclaimer acceptance
* Data export support
* Data deletion support
* GDPR-oriented architecture

## Suggested Architecture

The project starts as a modular monolith using Spring Boot.

```text
com.reduceco2now.co2diet
├── common
├── config
├── security
├── auth
├── legal
├── food
├── nutrition
├── co2
├── sync
└── contribution
```

This structure keeps the MVP simple while allowing future migration to microservices if needed.

## Tech Stack

* Java 21
* Spring Boot
* Spring Web
* Spring Data JPA
* Spring Security
* PostgreSQL
* Flyway or Liquibase
* MapStruct
* Lombok
* OpenAPI / Swagger
* Docker

## Future Improvements

* Barcode scanner integration
* AI-based nutrition extraction
* AI-based CO2 estimation
* Recipe builder
* Meal planning
* Wearable integration
* Community food database contribution
* Admin moderation panel
* Advanced analytics and weekly insights

## Project Status

This project is currently in the initial backend setup phase.

The first development focus is:

1. Define backend architecture
2. Create package structure
3. Implement food catalog module
4. Implement nutrition calculation module
5. Implement CO2 calculation module
6. Add synchronization APIs
7. Add legal and privacy-related endpoints

## License

This project is intended to be open source.

The license will be defined later.
