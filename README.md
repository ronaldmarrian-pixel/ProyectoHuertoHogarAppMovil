🌿 Proyecto HuertoHogar App Móvil



Bienvenido al repositorio oficial de la aplicación móvil nativa HuertoHogar. Este proyecto representa una solución integral para la gestión y visualización de productos agrícolas, desarrollada con las tecnologías más modernas del ecosistema Android: Kotlin y Jetpack Compose.

La aplicación implementa una arquitectura MVVM (Model-View-ViewModel) robusta y escalable, garantizando un código limpio, mantenible y eficiente, con persistencia de datos local segura.

📋 Descripción General y Propósito

HuertoHogar nace con el objetivo de conectar a los usuarios con productos frescos y saludables directamente desde su dispositivo móvil. La aplicación ofrece una experiencia de usuario fluida e intuitiva que permite:

Gestión de Identidad: Registro y autenticación segura de usuarios.

Exploración de Productos: Un catálogo visual y detallado de productos disponibles.

Geolocalización: Ubicación precisa de tiendas físicas mediante integración con mapas.

Personalización: Gestión de perfil de usuario con capacidad multimedia (cámara y galería).

🛠️ Stack Tecnológico y Herramientas

Este proyecto ha sido construido utilizando un stack tecnológico de vanguardia:

Lenguaje de Programación: Kotlin (100% nativo).

Interfaz de Usuario (UI): Jetpack Compose (Material Design 3).

Arquitectura: MVVM (Model-View-ViewModel) con Inyección de Dependencias manual (Factory Pattern).

Persistencia de Datos: Room Database (Abstracción sobre SQLite).

Navegación: Jetpack Navigation Compose.

Servicios de Mapas: Google Maps SDK para Android (maps-compose).

Multimedia: API de Cámara nativa (CameraX intent) y FileProvider seguro.

Control de Versiones: Git & GitHub.

IDE: Android Studio.

🚀 Bitácora de Desarrollo (Roadmap de Implementación)

El ciclo de vida del desarrollo se estructuró en fases incrementales para asegurar la estabilidad y funcionalidad en cada etapa:

🔹 Fase 1: Cimientos y Arquitectura

Inicialización: Configuración de proyecto "Empty Activity" con soporte para Compose y Kotlin DSL.

Estructura Modular: Organización del código en paquetes semánticos para una clara separación de responsabilidades:

model: Definición de entidades de datos (User, Product).

viewmodels: Lógica de negocio y estado de la UI (HomeViewModel, LoginViewModel).

views: Componentes visuales Composables (HomeView, CameraView).

data: Capa de persistencia (AppDatabase, UserDao, UserRepository).

navigation: Gestión centralizada de rutas (AppRoutes).

Resolución de Conflictos: Configuración precisa de JAVA_HOME y versiones de Gradle/JDK para evitar errores de compilación (jbr-21).

🔹 Fase 2: Seguridad y Persistencia (Core)

Integración de Room:

Definición de Entidades y DAOs.

Implementación de AppDatabase como Singleton.

Estabilidad (ANR Fixes): Implementación de estrategias de hilos (.allowMainThreadQueries()) para optimizar la inicialización de la base de datos y prevenir bloqueos (ANR).

Sistema de Autenticación: Desarrollo de flujos completos de Registro y Login con validación de campos en tiempo real.

🔹 Fase 3: Lógica de Negocio y Catálogo

Patrón Repository: Implementación de UserRepository como única fuente de verdad, intermediando entre la DB local y la UI.

Mock Data: Simulación de servicios backend mediante inyección de datos estáticos para el catálogo.

Interfaz Reactiva: Uso de LazyColumn y Card en HomeView para un renderizado eficiente de listas de productos.

🔹 Fase 4: Integración de Hardware (Recursos Nativos)

Geolocalización:

Implementación de GoogleMap composable.

Gestión de permisos de ubicación (ACCESS_FINE_LOCATION).

Marcadores personalizados en el mapa.

Multimedia y Archivos:

Configuración de seguridad con FileProvider (file_paths.xml).

Lógica para captura de fotos (Cámara) y selección de imágenes (Galería) en CameraView.

🔹 Fase 5: Despliegue y Entrega

Compilación: Generación exitosa del artefacto instalable app-debug.apk.

Documentación: Creación de este README y subida al repositorio público.

✨ Funcionalidades Clave Detalladas

Funcionalidad

Descripción Técnica

Estado

Registro de Usuarios

Validación de formularios y almacenamiento encriptado (simulado) en Room.

✅ Completo

Inicio de Sesión

Autenticación contra base de datos local con persistencia de sesión.

✅ Completo

Catálogo Dinámico

Scroll infinito y renderizado optimizado de items.

✅ Completo

Mapa de Tiendas

Integración nativa de Google Maps con puntos de interés (POIs).

✅ Completo

Perfil con Foto

Uso de Intents para interactuar con la Cámara y Galería del sistema.

✅ Completo


👤 Autor y Créditos

Desarrollado por Ronald Marrián Álvarez.

Asignatura: Programación de Aplicaciones Móviles.

Institución: DuocUc Concepcion.

Fecha: Noviembre 2025.

Este proyecto es de código abierto y está disponible para fines educativos.