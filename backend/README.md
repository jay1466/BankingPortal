# Banking Portal Rest API Using Spring Boot & Spring Security

***

The Banking Portal API provides a set of endpoints for managing user accounts, fund transfers, and transactions. This project aims to facilitate secure and efficient banking operations for users.

## Features

- User Registration: Users can register by providing their details, such as name, email, address, and phone number.
- PIN Management: Users can create and update their PINs for added security.
- Cash Deposit and Withdrawal: Users can deposit and withdraw cash from their accounts.
- Fund Transfer: Users can transfer funds to other accounts within the system.
- Transaction History: Users can view their transaction history.

## Technologies Used

- Spring Boot
- Spring Security
- Spring Data JPA
- MySQL
- JWT
- Redis / Caffeine

## TODO

- UI Fix for Dashboard Charts
- Pagination in table
- Save JWT Token in db and remove on logout
- Email trigger on account login
- Send Bank Statement on Email

## Installation and Setup

1. Clone the repository: `git clone https://github.com/yourusername/banking-portal-api.git`
2. Navigate to the project folder: `cd banking-portal-api`
3. Configure MySQL: Set up a MySQL database, create a copy of `application.properties.sample`, rename it `application.properties`, and update the properties as needed.
4. Build and run the project: `mvn spring-boot:run`

## Error Handling

The API implements global exception handling for common error scenarios, such as account not found, unauthorized access, and insufficient balance.
