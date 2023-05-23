# Ecommerce (Backend) using Spring Boot
## **Run**:
- Create a ".env" file in the same directory as "docker-compose.yml" with the following environment variables:
    - **HOST_SPRING_PORT**: specifies the host's spring port to be mapped to the container's port
    - **HOST_MAILDEV_SMTP_PORT**: specifies the host's maildev smtp port to be mapped to the container's port
    - **HOST_MAILDEV_WEB_PORT**: specifies the host's maildev web port to be mapped to the container's port (You can view the received emails on this port) 
    - **HOST_REDIS_PORT**: specifies the host's redis port to be mapped to the container's port
    - **HOST_MYSQL_PORT**: specifies the host's mysql port to be mapped to the container's port

- Create a "ecommerce.env" file in the same directory as "docker-compose.yml" with the following environment variables:
    - **DB_URL**: specifies the url to your database.
    - **DB_USER**: specifies your database's username.
    - **DB_PASSWORD**: specifies your database's password.
    - **stripeAPIKey**: specifies your secret key (Stripe)
    - **stripeEndpointSecret**: specifies your stripe endpoint key (Use Stripe CLI to find it)
    - **REDIS_HOST**: specifies the host of redis.
    - **REDIS_PORT**: specifies the password of redis.
    - **MAIL_HOST**: specifies the host that would be used to send emails 
        - example: smtp.gmail.com.
        - example: maildev
    - **MAIL_PORT**: specifies the mail's port.
    - **MAIL_USERNAME**: specifies the mail's username.
    - **MAIL_PASSWORD**: specifies the mail's password.

- Create a "mysql.env" file in the same directory as "docker-compose.yml" with the following environment variables:
    - **MYSQL_ROOT_PASSWORD**: specifies MySQL's password

- Run the following command in the terminal:
    ```
    docker-compose up --build
    ```
   
   
##  ***Some*** of the available Features:
   - User registration and login.
   - Email verification
   - Shopping cart
   - Wishlist
   - Ordering system
   - Payment using Stripe
   - Product addition/deletion/modification
   - Uploading product images
   - Reviewing products
   
   **This project is for educational purposes only.**
