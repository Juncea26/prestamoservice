# PrestaControl — `prestamoservice`

Servicio backend en Spring Boot 3 (Java 17) para la gestión de préstamos personales con autenticación JWT y persistencia en PostgreSQL.

## Variables de entorno

Copia `.env.example` a `.env` o define las siguientes variables antes de ejecutar el servicio:

| Variable | Obligatoria | Valor por defecto | Descripción |
| --- | :---: | --- | --- |
| `PORT` | No | `8080` | Puerto HTTP del servidor. |
| `DB_URL` | Sí | — | URL JDBC de PostgreSQL (ej. `jdbc:postgresql://localhost:5432/prestamoservice`). |
| `DB_USERNAME` | Sí | — | Usuario de la base de datos PostgreSQL. |
| `DB_PASSWORD` | Sí | — | Contraseña de la base de datos PostgreSQL. |
| `JWT_SECRET` | Sí | — | Clave secreta para firmar JWT con HMAC-SHA256 (mínimo 32 bytes en UTF-8). |
| `JWT_EXPIRATION_MS` | No | `86400000` | Tiempo de expiración del JWT en milisegundos (24 horas por defecto). |

## Compilación y ejecución

```bash
mvn clean package -DskipTests
java -jar target/prestamoservice-0.0.1-SNAPSHOT.jar
```

También incluye un `Dockerfile` multi-stage listo para contenedores.

## Colección de Postman

La colección lista para importar está en:

- `postman/PrestaControl.postman_collection.json` (formato Postman Collection v2.1.0)

### Variables de colección

- `baseUrl`: `http://localhost:8080` (ajústala si pruebas contra otro entorno o puerto).
- `token`: se completa automáticamente al ejecutar **Registrar Prestamista** o **Iniciar Sesión**.
- `prestamoId`: se completa automáticamente al ejecutar **Registrar Préstamo**.

### Flujo de pruebas secuencial

Importa el archivo en Postman y ejecuta las peticiones en orden (manualmente o con el Collection Runner):

1. **POST Registrar Prestamista** (`/auth/register`) — Crea la cuenta y guarda `token`.
2. **POST Iniciar Sesión** (`/auth/login`) — Autentica y actualiza `token`.
3. **GET Préstamos SIN Token (Prueba 401)** (`/prestamos`) — Verifica el rechazo `401 Unauthorized` sin cabecera `Authorization`.
4. **POST Registrar Préstamo** (`/prestamos`) — Registra un préstamo y guarda `prestamoId`.
5. **GET Listar Préstamos** (`/prestamos`) — Lista los préstamos del usuario autenticado.
6. **GET Resumen de Cartera** (`/prestamos/resumen`) — Consulta capital prestado, total por cobrar, ganancia esperada y conteos.
7. **GET Obtener Préstamo por ID** (`/prestamos/{{prestamoId}}`) — Obtiene el detalle del préstamo creado.
8. **PUT Actualizar / Marcar Pagado** (`/prestamos/{{prestamoId}}`) — Actualiza el préstamo y lo marca con `pagado: true`.
9. **DELETE Eliminar Préstamo** (`/prestamos/{{prestamoId}}`) — Elimina el préstamo (`204 No Content`).
