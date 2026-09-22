# EduBridge
## Contributors 
|Roles                                                          |Members|
|---------------------------------------------------------------|--------------------------------|
|**Documentation & DevOps**                                     |Meira John Daniels ST10356144   |
|**Backend/REST API Implementation**                            |Khumo Machoga ST10396677        |
|**Android Data Layer & External SDK Integration**              |Arlo (Abby) Staples ST10404431  |
|**Android UI/UX**                                              |Hashmira Govender ST10437073    |

## Overview
EduBridge is a mobile school and parent portal which is an android mobile application that centralizes academic information and school communication for parents, students, guardians and schools. 

Authorized users can use this single mobile platform to access information such as:
- Academic results
- Attendance records
- Timetables
- School notices
- Messages
- Student profiles settings and preferences
- School location information
- Weather information

EduBridge is designed using a role-based architecture where the application communicates with a hosted REST API. The API is responsible for authentication, authorisation, validation, business rules and access to data.

## Purpose
The purpose of this application is to provide a centralised and user friendly school portal that reduces the need to access academic information and communication across multiple platforms.

The aim of this application includes:
1. Provide parents and students with access to academic information.
2. Centralise school communication.
3. Provide role-based access to appropriate and authorized information.
4. Support offline access.
5. Provide push notifications for school updates.
6. Provide additional functionality using external services such as maps and weather.

## Scope
The EduBridge application includes this functionality:
- Secure SSO authentication
- Role-based access
- Parent student selection
- Academic results
- Attendance
- Timetable
- School notices
- User settings
- Language preferences
- Notification preferences
- Biometric authentication
- Offline data caching
- Data synchronization
- School location using Google Maps
- Weather information using a weather API
- REST API integration
- Room database
- Automated testing
- GitHub-based version control
- GitHub Actions continuous integration

This application does not replace a school’s complete administration or learning management system.

## Architecture Diagram
<img src ="Architecture Diagram PROG.png"> \
Caption: Architecture Diagram  created using draw.io Diagrams

## Technology Stack
|Layer |Technology |Purpose |
|----------------|----------------------------|-----------------------------------|
|Mobile Platform |Android                     |Required platform                  |
|Language        |Kotlin                      |Used to develop application        |
|UI              |Jetpack Compose             |Declares Android UI                |
|Architecture    |MVVM                        |Separates the UI and business logic|
|State Management|StateFlow                   |Observes UI Sates                  |
|Async Processing|Kotlin Coroutines           |Asynchronous operations            |
|Networking      |Retrofit                    |REST API communication             |
|Local Database  |RoomDB/SQLite               |Local caching                      |
|API             |REST API                    |Backend communication              |
|Authentication  |SSO/OAuth 2.0/OpenID Connect|User authentication                |
|Maps            |Google Maps SDK             |School location                    |
|Weather         |OpenWeather API             |Weather information                |
|Version Control |Git                         |Source control                     |
|Repository      |GitHub                      |Collaboration and source management|
|CI              |GitHub Actions              |Automated build and testing        |

## External SDKs/Services
### Google Maps SDK
The SDK provides map functionality for the school location feature as its purpose is to display a map, the schools location and a visual location reference.

### Weather API
Using an external weather service, the purpose of this API is to retrieve weather information, display weather on the dashboard and demonstrate integration with an external API.

### SSO/ OAuth
This service provides authentication for the application by using application tokens when communication with protected backend endpoints.

## CI/CD Summary
### Version Control
The collaboration platform and version control used for this project is Git and GitHub.

GitHub provides centralised source code management, pulls request, code review, issue tracking, GitHub Actions automation and documentation through the README.md.

The main branch is protected to reduce the risk of unstable code being merged directly into the stable project.

Branch protection includes successful GitHub Actions build, Pull request requirement, required status checks and review before merging.

Each member can commit changes regularly and must use descriptive commit messages as these commits make it easier to identify changes, track progress, review code, locate errors and bugs, revert problematic changes and demonstrates the project’s development history.

### GitHub Actions CI/CD
GitHub Actions is used to automate testing and the project’s build.

The workflow is triggered when changes to code are pushed to the repository and is responsible for checking the repository, setting up required Java environment, building the Android project, running unit tests and reporting success or failure.

## Screenshots


## Video Link


## AI Declaration
### Meira John Daniels
- Architecture Diagram - used AI to assist with structuring and refining the diagram as well as suggest appropriate terminology for the architecture diagram.
- Concept clarification - used AI to clarify concepts required for the documentation.
- GitHub Actions Automation - used AI to assist with understanding and creating the GitHub Actions workflow for project build and testing.

### Khumo Machoga
- Unit Tests - used AI to assist with understanding and developing unit tests.
- Concept Clarification - used AI to explain Kotlin and Ktor concepts as well as package/import resolution and Android build configuration.
- Errors - used AI to help understand compiler errors and suggest possible causes and solutions.
- Code Review - used AI to review code to identify any potential bugs or problems within code and provide correction or improvement suggestions.
- Boilerplate assistance - used AI to assist with repetitive code structures.

### Arlo (Abby) Staples
- Package and build errors - used AI to assist with resolving package-related issues, Gradle errors and project configuration.
- Errors - used AI to help understand compiler errors and suggest possible causes and solutions.
- Configuration - used AI to assist with Gradle configuration.

## References
Android Developers. (2024). Android gradle plugin 8.7.0 (October 2024). [online] Available at: https://developer.android.google.cn/build/releases/agp-8-7-0-release-notes [Accessed 22 Sept. 2026]. \
Android Developers (2025) BuildFeatures. [online] Available at: https://developer.android.com/reference/tools/gradle-api/7.1/com/android/build/api/dsl/BuildFeatures.html [Accessed: 22 September 2026]. \
Android Developers (2025) Java 8+ API desugaring support. [online] Available at: https://developer.android.com/studio/write/java8-support-table [Accessed: 22 September 2026]. \
Android Developers (2025) Java 11+ APIs available through desugaring. [online] Available at: https://developer.android.google.cn/studio/write/java11-default-support-table [Accessed: 22 September 2026]. \
Dagger.dev. (2026). Conflicting `@inject`. [online] Available at: https://dagger.dev/conflicting-inject.html [Accessed 22 Sept. 2026]. \
Gradle (2024) Gradle 8.10.2 release notes. [online] Available at: https://docs.gradle.org/8.10.2/release-notes.html [Accessed: 22 September 2026]. \
Gradle (n.d.) Toolchains for JVM projects. [online] Available at: https://docs.gradle.org/current/userguide/toolchains.html [Accessed: 22 September 2026]. \
Ktor Help. (2024). Custom plugins | Ktor. [online] Available at: https://ktor.io/docs/2.3.13/server-custom-plugins.html#webpage [Accessed 22 Sept. 2026]. \
Ktor (2025) Custom server plugins. [online] Available at: https://ktor.io/docs/server-custom-plugins.html [Accessed: 22 September 2026]. \
Openjdk.org. (2024). Loading... [online] Available at: https://bugs.openjdk.org/browse/JDK-8341167 [Accessed 22 Sept. 2026]. \
W3Schools. (2026). Kotlin online compiler (editor / interpreter). [online] Available at: https://www.w3schools.com/kotlin/kotlin_compiler.php [Accessed 22 Sept. 2026].
