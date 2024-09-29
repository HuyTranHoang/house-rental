# Introduction!

This is a Spring Boot-based API project for a house rental search website. The API provides endpoints to interact with a database containing information about houses for rent, including:
-   **House search:** Allows users to search for houses based on criteria such as location, price, number of bedrooms, area, etc.
-   **House details:** Provides detailed information about a specific house.
-   **House management:** (If applicable) Allows users to post rental listings, edit, or delete listings.
-   **Payment and Membership**: Users can pay and use features for VIP members. 

# Installation

To run the project, you need:

-   **Java Development Kit (JDK):** Ensure you have installed a compatible JDK version (17).
-   **A package manager:** Maven or Gradle.
-   **A tool to run Spring Boot applications:** You can use an IDE like IntelliJ IDEA, Eclipse, or run directly from the command line.

Or
- **Install Docker and using Docker Compose**
	```
	docker compose up
	```

## Installation steps:

 1. **Clone the repository:**
	```
	git clone https://github.com/HuyTranHoang/house-rental.git
	```
2.  **Open the project in your IDE or run from the command line:**
	```
	cd house-rental 
	mvn spring-boot:run
	```

## Configuration

-   **Database:** Configure the database connection information in the  `application.yml` file.
-   **Other configurations:** In order for the program to run full function, you need to create an .env file with the following form
	```
	## Cloudinary Api
	CLOUDINARY_CLOUD_NAME=""
	CLOUDINARY_API_KEY=""
	CLOUDINARY_API_SECRET=""

	## Gmail SMTP
	USERNAME_EMAIL=""
	PASSWORD_EMAIL=""

	## PostgresSQL
	DB_URL=""
	DB_USERNAME=""
	DB_PASSWORD=""

	## Jwt secret seed
	JWT_SECRET=""

	## VNPAY demo env
	VNPAY_TMN_CODE=""
	VNPAY_HASH_SECRET=""
	```


## Technologies used

-   **Spring Boot:** A Java framework for quickly developing web applications.
-   **Spring Data JPA:** A data access framework that simplifies interaction with relational databases.
-   **Spring Security:** A comprehensive security framework that provides authentication, authorization, and protection against common security vulnerabilities.

## Live demo

- https://mogu.id.vn/
- https://admin.mogu.id.vn/
