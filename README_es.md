<!-- Language Navigation -->
<div align="right">
  <a href="./README.md">English</a> | <a href="./README_fr.md">Français</a> | <b><a href="./README_es.md">Español</a></b>
</div>

# Aplicación Java de Gestión de Cine

[![License: MIT](https://img.shields.io/badge/Licencia-MIT-blue.svg)](https://opensource.org/licenses/MIT)
![Language](https://img.shields.io/badge/Lenguaje-Java_21-orange)
![UI](https://img.shields.io/badge/UI-Java_Swing-blue)
![Build](https://img.shields.io/badge/Build-Maven-red)

Una completa aplicación de escritorio para la gestión de cines, desarrollada como un proyecto universitario. Esta aplicación, construida íntegramente en **Java** con el framework **Swing** para la interfaz de usuario, ofrece una solución completa tanto para clientes como para administradores de cine. Está estructurada siguiendo una robusta **arquitectura de 3 capas** y utiliza **Maven** para la gestión del proyecto.

![Vista de Cliente - Detalles de Película](img/client-view-film.png)

## Tabla de Contenidos
- [Sobre el Proyecto](#sobre-el-proyecto)
- [Características Principales](#características-principales)
- [Arquitectura y Diseño](#arquitectura-y-diseño)
- [Stack Tecnológico](#stack-tecnológico)
- [Cómo Empezar](#cómo-empezar)
- [Agradecimientos](#agradecimientos)
- [Contacto](#contacto)
- [Licencia](#licencia)

## Sobre el Proyecto
Este proyecto simula un sistema completo de gestión de cines. Cuenta con dos interfaces distintas:
1.  Una **aplicación para clientes** donde los usuarios pueden buscar películas, ver horarios, reservar asientos y gestionar sus cuentas.
2.  Un **panel de administración** que proporciona un control total sobre el catálogo del cine (películas, sesiones, salas), las ventas y la gestión del personal.

El objetivo principal fue aplicar principios fundamentales de ingeniería de software, incluyendo arquitectura en capas, patrones de diseño (DAO) y gestión manual de la integridad de los datos sin un sistema de base de datos relacional. La persistencia de datos se logra mediante la serialización de objetos Java en archivos `.dat`.

## Características Principales

### 🎬 Funcionalidades para el Cliente
- **Gestión de Cuentas:** Los usuarios pueden registrarse, iniciar sesión, actualizar su información y eliminar sus cuentas.
- **Consultar Cartelera:** Ver una lista de películas y sus horarios, con opciones de filtrado (por fecha, título, género).
- **Selección Interactiva de Asientos:** Una interfaz gráfica para elegir asientos en la sala, con actualización de precios en tiempo real.
- **Proceso de Reserva:** Un flujo completo de reserva, incluyendo la adición de snacks y un proceso de pago simulado.
- **Historial de Reservas:** Los usuarios pueden ver sus reservas pasadas y futuras, y cancelarlas si es necesario.
- **Reseñas de Películas:** Posibilidad de dejar y modificar valoraciones y comentarios para las películas.

### ⚙️ Funcionalidades para el Administrador
- **Operaciones CRUD Completas:** Los administradores pueden añadir, modificar y eliminar películas, géneros, sesiones, salas y tarifas.
- **Gestión de Snacks e Inventario:** Administrar el catálogo de snacks y sus niveles de stock.
- **Punto de Venta (TPV):** Una interfaz dedicada para que los empleados gestionen las ventas en persona.
- **Gestión de Personal y Horarios:** Administrar las cuentas de los empleados, sus roles y sus horarios de trabajo.
- **Informes de Ventas:** Ver informes detallados de reservas y ventas para seguir la actividad comercial.

## Arquitectura y Diseño
La aplicación se basa en una **Arquitectura de 3 Capas** clásica para garantizar una clara separación de responsabilidades, lo que hace que el código sea modular, mantenible y escalable.

1.  **Capa de Presentación (Vista):**
    *   Construida con **Java Swing**, utilizando el editor gráfico de NetBeans.
    *   Maneja todas las interacciones del usuario a través de varios componentes `JPanel` gestionados por `CardLayout` para la navegación del cliente y `JTabbedPane` para la administración.
    *   Delega todas las acciones del usuario a la capa de servicio y no contiene lógica de negocio.

2.  **Capa de Servicio (Servicio):**
    *   El núcleo funcional de la aplicación. Orquesta las operaciones de negocio y aplica todas las reglas de negocio (p. ej., verificar la disponibilidad de un asiento, validar una contraseña, evitar conflictos de horarios).
    *   Actúa como el único punto de entrada para la capa de vista y coordina las llamadas a la capa de acceso a datos.

3.  **Capa de Acceso a Datos (DAO):**
    *   Responsable de la persistencia de los datos. En lugar de una base de datos SQL, los datos se persisten mediante la **serialización y deserialización de objetos Java** en archivos `.dat`.
    *   Esta capa implementa el patrón de diseño **Data Access Object (DAO)**, abstrayendo la fuente de datos del resto de la aplicación. Esta modularidad permite que el método de persistencia pueda ser sustituido (por ejemplo, por una base de datos SQL) con cambios mínimos en las otras capas.

## Stack Tecnológico
- **Lenguaje:** Java 21 (LTS)
- **Framework de UI:** Java Swing
- **Gestión de Proyecto:** Apache Maven
- **IDE:** Desarrollado en NetBeans

## Cómo Empezar
Para ejecutar este proyecto, necesitarás un JDK (Java Development Kit) y Maven instalados.

1.  **Prerrequisitos:**
    *   JDK 21 o superior.
    *   Apache Maven.
    *   Un IDE de Java que soporte proyectos Maven (p. ej., NetBeans, IntelliJ IDEA, Eclipse).

2.  **Clonar el repositorio:**
    ```bash
    git clone https://github.com/Alespfer/cinema-management-app.git
    ```

3.  **Abrir el proyecto en tu IDE:**
    *   Abre tu IDE y selecciona "Abrir Proyecto".
    *   Navega al directorio clonado y selecciónalo. El IDE debería reconocerlo automáticamente como un proyecto Maven y resolver las dependencias.

4.  **Ejecutar la aplicación:**
    *   El proyecto contiene una clase `DataInitializer.java` que puebla automáticamente el sistema con datos de ejemplo (películas, usuarios, etc.) si el directorio `/data` no existe en la primera ejecución.
    *   Encuentra la clase principal de la aplicación del cliente (p. ej., `FenetrePrincipaleClient.java`) y ejecútala desde tu IDE.

## Agradecimientos
- Este proyecto fue desarrollado por Alberto ESPERON y Axelle MORICE.
- Parte del programa de Máster "Projets Informatiques et Stratégie d'Entreprise (PISE)" en la Université Paris Cité.

## Contacto

Alberto Esperon - [LinkedIn](https://www.linkedin.com/in/alberto-espfer) - [Perfil de GitHub](https://github.com/Alespfer)

## Licencia

Distribuido bajo la Licencia MIT. Consulta el archivo `LICENSE` para más información.
