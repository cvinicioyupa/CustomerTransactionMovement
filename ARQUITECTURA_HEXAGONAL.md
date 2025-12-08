# Arquitectura Hexagonal - CustomerTransactionMovement

## Estructura del Proyecto

El proyecto ha sido migrado exitosamente a **Arquitectura Hexagonal** con las siguientes 3 capas principales:

### 📁 **domain** - Capa del Dominio (Core Business)
Contiene la lógica de negocio pura, sin dependencias externas.

- **domain/model/**
  - `Account.java` - Modelo de dominio para cuentas
  - `Movement.java` - Modelo de dominio para movimientos
  
- **domain/ports/in/** - Casos de uso (interfaces)
  - `AccountUseCase.java`
  - `MovementUseCase.java`
  - `ReportUseCase.java`
  
- **domain/ports/out/** - Puertos de salida (interfaces)
  - `AccountRepositoryPort.java`
  - `MovementRepositoryPort.java`

### 📁 **application** - Capa de Aplicación
Implementa los casos de uso definidos en el dominio.

- **application/usecases/**
  - `AccountUseCaseImpl.java` - Implementación de casos de uso de cuentas
  - `MovementUseCaseImpl.java` - Implementación de casos de uso de movimientos
  - `ReportUseCaseImpl.java` - Implementación de casos de uso de reportes

### 📁 **infrastructure** - Capa de Infraestructura
Contiene las implementaciones técnicas y adaptadores.

#### **infrastructure/persistence/** - Adaptadores de Persistencia
- **entity/**
  - `AccountEntity.java` - Entidad JPA para cuentas
  - `MovementEntity.java` - Entidad JPA para movimientos
  
- **repository/**
  - `AccountJpaRepository.java` - Repositorio JPA
  - `MovementJpaRepository.java` - Repositorio JPA
  
- **adapter/**
  - `AccountPersistenceAdapter.java` - Implementa `AccountRepositoryPort`
  - `MovementPersistenceAdapter.java` - Implementa `MovementRepositoryPort`
  
- **mapper/**
  - `AccountMapper.java` - Convierte entre entidades JPA y modelos de dominio
  - `MovementMapper.java` - Convierte entre entidades JPA y modelos de dominio

#### **infrastructure/web/** - Adaptadores Web (REST API)
- **controller/**
  - `AccountController.java` - Endpoints REST para cuentas
  - `MovementController.java` - Endpoints REST para movimientos
  - `ReportController.java` - Endpoints REST para reportes
  
- **dto/**
  - `AccountDto.java` - DTO para requests/responses de cuentas
  - `MovementDto.java` - DTO para requests/responses de movimientos
  
- **mapper/**
  - `AccountDtoMapper.java` - Convierte entre DTOs y modelos de dominio
  - `MovementDtoMapper.java` - Convierte entre DTOs y modelos de dominio

## 🔄 Flujo de Datos

```
HTTP Request → Controller → UseCase → RepositoryPort → Adapter → JpaRepository → Database
                   ↓            ↓            ↓              ↓
                  DTOs      Domain       Domain         Entity
                           Models       Models         (JPA)
```

## ✨ Características Implementadas

1. **Lombok** - Versión 1.18.30
   - `@Data`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`
   - `@RequiredArgsConstructor` para inyección de dependencias

2. **Separación de Responsabilidades**
   - Domain: Lógica de negocio pura
   - Application: Orquestación de casos de uso
   - Infrastructure: Detalles técnicos (BD, REST, etc.)

3. **Inversión de Dependencias**
   - El dominio NO depende de la infraestructura
   - Los adaptadores implementan los puertos del dominio

4. **Reactive Programming**
   - Uso de `Mono` y `Flux` de Project Reactor

## 🗑️ Código Eliminado

Se eliminaron las siguientes carpetas antiguas:
- `model/` (reemplazado por `domain/model/`)
- `service/` (reemplazado por `application/usecases/`)
- `repository/` (reemplazado por `infrastructure/persistence/`)
- `controller/` (reemplazado por `infrastructure/web/controller/`)

## ✅ Tests Actualizados

El archivo `MovementServiceTest.java` fue actualizado para trabajar con la nueva arquitectura hexagonal.

## 🚀 Endpoints REST (sin cambios)

- **Accounts**: `/api/v1/accounts`
- **Movements**: `/api/v1/movements`
- **Reports**: `/api/v1/reports`
