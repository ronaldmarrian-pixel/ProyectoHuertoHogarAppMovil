# 🌿 Proyecto HuertoHogar App Móvil


Bienvenido al repositorio oficial de la aplicación móvil nativa **HuertoHogar**. Este proyecto representa una solución integral para la gestión y visualización de productos agrícolas, desarrollada con las tecnologías más modernas del ecosistema Android: **Kotlin** y **Jetpack Compose**.

La aplicación implementa una arquitectura **MVVM (Model-View-ViewModel)** robusta y escalable, garantizando un código limpio, mantenible y eficiente, con persistencia de datos local segura mediante **Room**.

---

## 📋 Descripción General y Propósito

**HuertoHogar** nace con el objetivo de conectar a los usuarios con productos frescos y saludables directamente desde su dispositivo móvil. La aplicación ofrece una experiencia de usuario fluida e intuitiva que permite:

* **Gestión de Identidad:** Registro y autenticación segura de usuarios con validación.
* **Exploración de Productos:** Un catálogo visual y detallado de productos disponibles.
* **Carrito de Compras:** Sistema para agregar productos y calcular totales.
* **Geolocalización:** Ubicación precisa de tiendas físicas mediante integración con mapas.
* **Perfil Completo:** Gestión de datos personales (nombre, teléfono, dirección) y foto de perfil personalizada (Cámara/Galería).

---

## 🛠️ Stack Tecnológico y Herramientas

Este proyecto ha sido construido utilizando un stack tecnológico de vanguardia:

* **Lenguaje de Programación:** [Kotlin](https://kotlinlang.org/) (100% nativo).
* **Interfaz de Usuario (UI):** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material Design 3).
* **Arquitectura:** MVVM (Model-View-ViewModel) con Inyección de Dependencias manual (Factory Pattern).
* **Persistencia de Datos:** [Room Database](https://developer.android.com/training/data-storage/room) (Abstracción sobre SQLite).
* **Navegación:** Jetpack Navigation Compose.
* **Servicios de Mapas:** Google Maps SDK para Android (`maps-compose`).
* **Multimedia:** API de Cámara nativa (CameraX intent) y FileProvider seguro.
* **Control de Versiones:** Git & GitHub.
* **IDE:** Android Studio.

---

## 🚀 Bitácora de Desarrollo (Roadmap de Implementación)

El ciclo de vida del desarrollo se estructuró en fases incrementales para asegurar la estabilidad y funcionalidad en cada etapa:

### 🔹 Fase 1: Cimientos y Arquitectura
1.  **Inicialización:** Configuración de proyecto "Empty Activity" con soporte para Compose y Kotlin DSL.
2.  **Estructura Modular:** Organización del código en paquetes semánticos (`model`, `viewmodels`, `views`, `data`, `navigation`).
3.  **Resolución de Conflictos:** Configuración precisa de `JAVA_HOME` y versiones de Gradle/JDK para evitar errores de compilación (`jbr-21`).

### 🔹 Fase 2: Seguridad y Persistencia (Core)
4.  **Integración de Room:** Definición de Entidades (`User`) y DAOs. Implementación de `AppDatabase` como Singleton.
5.  **Estabilidad (ANR Fixes):** Implementación de `.allowMainThreadQueries()` para optimizar la inicialización de la base de datos y prevenir bloqueos.
6.  **Sistema de Autenticación:** Desarrollo de flujos completos de Registro y Login con validación de campos.

### 🔹 Fase 3: Lógica de Negocio y Catálogo
7.  **Patrón Repository:** Implementación de `UserRepository` y `CartRepository` (Singleton) para manejar datos.
8.  **Catálogo Dinámico:** Uso de `LazyColumn` y `Card` en `HomeView` para un renderizado eficiente de productos con animaciones de entrada.
9.  **Carrito de Compras:** Implementación de lógica para agregar productos y calcular el total en tiempo real.

### 🔹 Fase 4: Integración de Hardware (Recursos Nativos)
10. **Geolocalización:** Implementación de `GoogleMap` composable con marcadores de tiendas en Santiago.
11. **Multimedia y Perfil:**
    * Configuración de seguridad con `FileProvider`.
    * Lógica para captura de fotos (Cámara) y selección de galería en `CameraView`.
    * Actualización de datos de usuario (Dirección y Teléfono) en la base de datos local.

### 🔹 Fase 5: Despliegue y Entrega
12. **Compilación:** Generación exitosa del artefacto instalable `app-debug.apk`.
13. **Documentación:** Creación de este README y subida al repositorio público.

---

## ✨ Funcionalidades Clave Detalladas

| Funcionalidad | Descripción Técnica | Estado |
| :--- | :--- | :--- |
| **Registro de Usuarios** | Validación de formularios y almacenamiento encriptado (simulado) en Room. | ✅ Completo |
| **Inicio de Sesión** | Autenticación contra base de datos local con persistencia de sesión. | ✅ Completo |
| **Catálogo Dinámico** | Scroll infinito, animaciones y renderizado optimizado de items. | ✅ Completo |
| **Carrito de Compras** | Gestión de estado global para agregar/quitar productos y ver totales. | ✅ Completo |
| **Mapa de Tiendas** | Integración nativa de Google Maps con puntos de interés (POIs). | ✅ Completo |
| **Perfil Completo** | Edición de datos personales y foto de perfil usando Cámara/Galería. | ✅ Completo |

---

## 👤 Autor y Créditos

Desarrollado por Ronald Marrián Álvarez.

* **Asignatura:** Programación de Aplicaciones Móviles.
* **Institución:** Duoc UC.
* **Fecha:** Noviembre 2025.

---
*Este proyecto es de código abierto y está disponible para fines educativos.*