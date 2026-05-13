# Casos de prueba (Gherkin)

Documentación alineada a la suite JUnit del módulo **Clases** y contexto de aplicación. Cada escenario referencia el método `@Test` y el archivo fuente.

---

## Smoke / contexto

### Feature: Arranque de la aplicación en perfil test

**Archivo:** `src/test/java/com/prog4/EjemploDesdeCero/EjemploDesdeCeroApplicationTests.java`

#### Scenario: El contexto Spring carga con perfil `test`

- **Test:** `contextLoads`
- **Given** el perfil activo es `test` (`@ActiveProfiles("test")`)
- **When** se inicia el contexto de Spring Boot
- **Then** el contexto se inicializa sin errores

---

## E2E — API Clases

**Archivo:** `src/test/java/com/prog4/EjemploDesdeCero/features/clases/controller/ClaseControllerE2EIT.java`

### Feature: Listado de clases con JWT

#### Scenario: CLIENT obtiene listado exitoso

- **Test:** `get_list_withClientToken_isOk`
- **Given** existen usuarios de prueba y un token JWT de rol CLIENT
- **When** se solicita `GET /api/clases` con cabecera `Authorization: Bearer <token>`
- **Then** la respuesta HTTP es 200 y el mensaje indica listado correcto

#### Scenario: ADMIN obtiene listado exitoso

- **Test:** `get_list_withAdminToken_isOk`
- **Given** un token JWT de rol ADMIN
- **When** se solicita `GET /api/clases` con ese token
- **Then** la respuesta HTTP es 200

#### Scenario: Petición sin token es rechazada (403)

- **Test:** `get_list_withoutToken_isForbidden`
- **Given** no se envía cabecera `Authorization`
- **When** se solicita `GET /api/clases`
- **Then** la respuesta HTTP es 403 (comportamiento actual de Spring Security con recurso protegido y usuario anónimo)

### Feature: Creación de clases y validación

#### Scenario: CLIENT no puede crear clase

- **Test:** `post_create_withClientToken_isForbidden`
- **Given** un token JWT de rol CLIENT
- **When** se envía `POST /api/clases` con cuerpo JSON válido
- **Then** la respuesta HTTP es 403

#### Scenario: ADMIN crea clase con datos válidos

- **Test:** `post_create_withAdminToken_isOk`
- **Given** un token JWT de rol ADMIN
- **When** se envía `POST /api/clases` con nombre, instructor y cupo válidos
- **Then** la respuesta HTTP es 200 y el cuerpo incluye la clase creada

#### Scenario: ADMIN recibe error de validación con nombre vacío

- **Test:** `post_create_invalidBody_isBadRequest`
- **Given** un token JWT de rol ADMIN
- **When** se envía `POST /api/clases` con `name` vacío
- **Then** la respuesta HTTP es 400

### Feature: Actualización y borrado lógico por API

#### Scenario: PATCH y DELETE sobre id inexistente devuelven 404

- **Test:** `patchAndDelete_notFound_returns404`
- **Given** un token JWT de rol ADMIN
- **When** se envía `PATCH` y luego `DELETE` sobre un id que no existe
- **Then** ambas respuestas HTTP son 404

#### Scenario: Flujo feliz de creación, parcheo y borrado

- **Test:** `patchAndDelete_happyPath_isOk`
- **Given** un token JWT de rol ADMIN
- **When** se crea una clase, se parchea instructor y cupo, y se elimina por id
- **Then** las tres operaciones responden 200 y el parche refleja los nuevos datos

---

## Unitarios — servicios

### Feature: Búsqueda de clase por id

**Archivo:** `src/test/java/com/prog4/EjemploDesdeCero/features/clases/services/impl/commons/ClaseFindByIdServiceTest.java`

#### Scenario: Clase activa encontrada

- **Test:** `execute_returnsClase_whenFoundAndNotDeleted`
- **Given** el repositorio devuelve una clase no borrada para el id
- **When** se ejecuta el servicio de búsqueda
- **Then** se devuelve esa misma instancia

#### Scenario: Clase no encontrada lanza excepción

- **Test:** `execute_throwsNotFound_whenMissing`
- **Given** el repositorio devuelve `Optional.empty()`
- **When** se ejecuta el servicio
- **Then** se lanza `NotFoundException`

#### Scenario: Id nulo tratado como no encontrado

- **Test:** `execute_treatsNullAsNotFound_whenRepositoryReturnsEmpty`
- **Given** el repositorio devuelve vacío para id nulo
- **When** se ejecuta el servicio con id nulo
- **Then** se lanza `NotFoundException`

### Feature: Creación de clase

**Archivo:** `src/test/java/com/prog4/EjemploDesdeCero/features/clases/services/impl/domain/ClaseCreateServiceTest.java`

#### Scenario: Persistencia y mapeo correctos

- **Test:** `execute_savesOnce_andReturnsDto`
- **Given** un request válido y el repositorio devuelve la entidad persistida
- **When** se ejecuta el servicio de creación
- **Then** se invoca `save` una vez y el DTO contiene los datos esperados

#### Scenario: Error del repositorio se propaga

- **Test:** `execute_propagates_whenSaveFails`
- **Given** el repositorio lanza excepción al guardar
- **When** se ejecuta el servicio
- **Then** la excepción se propaga al llamador

### Feature: Listado de clases

**Archivo:** `src/test/java/com/prog4/EjemploDesdeCero/features/clases/services/impl/domain/ClaseListServiceTest.java`

#### Scenario: Lista con elementos

- **Test:** `execute_returnsMappedList_whenNonEmpty`
- **Given** el repositorio devuelve N clases activas
- **When** se lista
- **Then** el resultado tiene N elementos mapeados

#### Scenario: Lista vacía

- **Test:** `execute_returnsEmptyList`
- **Given** el repositorio devuelve lista vacía
- **When** se lista
- **Then** el resultado está vacío

#### Scenario: Fallo del repositorio

- **Test:** `execute_propagates_whenRepositoryFails`
- **Given** el repositorio lanza `RuntimeException`
- **When** se lista
- **Then** la excepción se propaga

### Feature: Parche de clase

**Archivo:** `src/test/java/com/prog4/EjemploDesdeCero/features/clases/services/impl/domain/ClasePatchServiceTest.java`

#### Scenario: Parche aplicado y guardado

- **Test:** `execute_happyPath`
- **Given** la clase existe y el repositorio persiste los cambios
- **When** se ejecuta el parche
- **Then** el DTO refleja instructor y cupo actualizados

#### Scenario: Clase inexistente

- **Test:** `execute_notFoundFromFind`
- **Given** el servicio de búsqueda lanza `NotFoundException`
- **When** se intenta parchear
- **Then** se propaga `NotFoundException`

#### Scenario: Fallo al guardar

- **Test:** `execute_propagatesWhenSaveFails`
- **Given** la clase existe pero `save` falla
- **When** se ejecuta el parche
- **Then** se propaga la excepción de persistencia

### Feature: Borrado lógico de clase

**Archivo:** `src/test/java/com/prog4/EjemploDesdeCero/features/clases/services/impl/domain/ClaseDeleteServiceTest.java`

#### Scenario: Marca deleted y persiste

- **Test:** `execute_softDeletes_andSaves`
- **Given** una clase activa encontrada por id
- **When** se ejecuta el borrado
- **Then** la entidad pasada a `save` tiene `deleted=true`

#### Scenario: No se guarda si no existe

- **Test:** `execute_doesNotSave_whenNotFound`
- **Given** la búsqueda lanza `NotFoundException`
- **When** se ejecuta el borrado
- **Then** no se invoca `save` en el repositorio

---

## Repositorio — `IClaseRepository`

**Archivo:** `src/test/java/com/prog4/EjemploDesdeCero/features/clases/repositories/IClaseRepositoryTest.java`

### Feature: Persistencia y consultas derivadas

#### Scenario: Guardar y recuperar por id

- **Test:** `save_andFindById_roundTrip`
- **Given** una nueva clase sin id
- **When** se guarda y se limpia la sesión
- **Then** `findById` devuelve la entidad con el mismo nombre

#### Scenario: `findByDeletedFalse` excluye borradas

- **Test:** `findByDeletedFalse_excludesDeleted`
- **Given** una clase activa y otra con `deleted=true`
- **When** se consulta `findByDeletedFalse`
- **Then** solo aparece la activa

#### Scenario: `findByIdAndDeletedFalse` con entidad borrada

- **Test:** `findByIdAndDeletedFalse_emptyWhenDeleted`
- **Given** una clase persistida con `deleted=true`
- **When** se consulta `findByIdAndDeletedFalse`
- **Then** el resultado es vacío

#### Scenario: Id inexistente

- **Test:** `findByIdAndDeletedFalse_emptyWhenMissing`
- **Given** un id que no existe en base
- **When** se consulta `findByIdAndDeletedFalse`
- **Then** el resultado es vacío
