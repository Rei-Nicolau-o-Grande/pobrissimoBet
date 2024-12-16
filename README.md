
# Pobrissimo Bet
Um Cassino Online de renda passiva. 1000% de retorno garantido. 💸💸💸

## Baixando o cassino e preprarando o ambiente
```
git clone https://github.com/Rei-Nicolau-o-Grande/pobrissimoBet.git
```

Entre em resources

```bash
  cd src/main/resources 
```

Crie o application.properties e copiando o application.properties.example
```bash
cp application.properties.example application.properties
```

Criando Schemas e Roles
```bash
cp data.sql.example data.sql
```


# Crie a chaves privada e publica

Entre em resources

```bash
  cd src/main/resources 
```

Crie a chave privada
```bash
openssl genrsa -out private.pem 2048
```
ou
```bash
openssl genrsa > private.pem
```
Crie a chave publica
```bash
openssl rsa -in private.pem -pubout -out public.pem
```




## Variáveis de Ambiente

Para rodar esse projeto, você vai precisar adicionar as seguintes variáveis de ambiente no seu .env

```
APP_NAME=Pobrissimo-Bet

# Database
SQL_INIT_MODE=always
DEFER_DATASOURCE_INITIALIZATION=true

DB_HOST=SEU_HOST
DB_PORT=5432
DB_NAME=pobrissimo_bet
DB_URL=jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}

DB_USERNAME=USERNAME_DO_BANCO_DE_DADOS
DB_PASSWORD=SENHA_DO_BANCO_DE_DADOS
DB_DRIVER=org.postgresql.Driver
DB_DIALECT=
SHOW_SQL=true
FORMAT_SQL=true
DDL_AUTO=update

# Redis
REDIS_HOST=SEU_HOST
REDIS_PORT=PORTA_REDIS

# JWT
JWT_PUBLIC_KEY=classpath:public.pem
JWT_PRIVATE_KEY=classpath:private.pem

# Swagger
SWAGGER_HOST=
SWAGGER_PROTOCOL=
SWAGGER_PATH_PATTERN=
SWAGGER_TITLE=
SWAGGER_DESCRIPTION=
SWAGGER_UI_PATH=/docs

SERVER_PORT_SPRING_BOOT=8080

# Create Admin User
ADMIN_EMAIL=EMAIL_ADMIN
ADMIN_USERNAME=USERNAME_ADMIN
ADMIN_PASSWORD=SENHA_ADMIN

# cors
CORS_ALLOWED_ORIGINS_PROD=
CORS_ALLOWED_ORIGINS_TEST=
CORS_ALLOWED_ORIGINS_DEV=
CORS_ALLOWED_MAPPING=/api/**
CORS_ALLOWED_METHODS=GET,POST,PATCH,PUT,DELETE,OPTIONS
CORS_ALLOWED_HEADERS=*
```


## Rodando localmente com docker compose

Mude o host do postgres e redis
```bash
DB_HOST=db # postgres
REDIS_HOST=redis # redis
```
Criando o .jar
```bash
mvn clean package -DskipTests
```

Rodando Docker Compose
```bash
docker compose up --build -d 
```
#### Acesse o Swagger em http://localhost:8080/docs

Parando Docker Compose
```bash
docker compose down -v
```
## Stack utilizada

**Front-end:** React, TailwindCSS, Flowbite React

**Back-end:** Java, Spring Boot, PostgreSQL, Redis


## Usado por

Esse projeto é usado pelas seguintes empresas:

- Front-end em React  
https://github.com/Rei-Nicolau-o-Grande/React-Pobrissimo-Bet-Client/tree/flowbite-react


