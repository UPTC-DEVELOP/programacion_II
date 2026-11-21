CREATE DATABASE  IF NOT EXISTS `tienda_virtual` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `tienda_virtual`;
-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: tienda_virtual
-- ------------------------------------------------------
-- Server version	8.0.46

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `carrito_items`
--

DROP TABLE IF EXISTS `carrito_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carrito_items` (
  `cliente_id` varchar(30) NOT NULL,
  `libro_isbn` varchar(30) NOT NULL,
  `cantidad` int NOT NULL,
  PRIMARY KEY (`cliente_id`,`libro_isbn`),
  KEY `libro_isbn` (`libro_isbn`),
  CONSTRAINT `carrito_items_ibfk_1` FOREIGN KEY (`cliente_id`) REFERENCES `usuarios` (`id`) ON DELETE CASCADE,
  CONSTRAINT `carrito_items_ibfk_2` FOREIGN KEY (`libro_isbn`) REFERENCES `libros` (`isbn`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carrito_items`
--

LOCK TABLES `carrito_items` WRITE;
/*!40000 ALTER TABLE `carrito_items` DISABLE KEYS */;
INSERT INTO `carrito_items` VALUES ('1002','978-0134685991',3),('246899','978-0132350884',2);
/*!40000 ALTER TABLE `carrito_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `libros`
--

DROP TABLE IF EXISTS `libros`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `libros` (
  `isbn` varchar(30) NOT NULL,
  `titulo` varchar(150) NOT NULL,
  `autor` varchar(120) NOT NULL,
  `anio` int NOT NULL,
  `categoria` varchar(80) NOT NULL,
  `editorial` varchar(100) NOT NULL,
  `paginas` int NOT NULL,
  `precio` double NOT NULL,
  `stock` int NOT NULL,
  `formato` varchar(30) NOT NULL,
  `porcentaje_iva` double NOT NULL,
  PRIMARY KEY (`isbn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `libros`
--

LOCK TABLES `libros` WRITE;
/*!40000 ALTER TABLE `libros` DISABLE KEYS */;
INSERT INTO `libros` VALUES ('978-0132350884','Clean Code','Robert C. Martin',2008,'Programación','Pearson Education',464,180000,2,'Físico',19),('978-0134685991','Effective Java','Joshua Bloch',2018,'Programación','Addison-Wesley',416,120000,12,'Físico',19);
/*!40000 ALTER TABLE `libros` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id` varchar(30) NOT NULL,
  `nombre_completo` varchar(150) NOT NULL,
  `correo` varchar(120) NOT NULL,
  `direccion_envio` varchar(200) DEFAULT NULL,
  `departamento` varchar(80) DEFAULT NULL,
  `ciudad` varchar(80) DEFAULT NULL,
  `telefono` varchar(30) DEFAULT NULL,
  `tipo_cliente` varchar(30) NOT NULL,
  `porcentaje_descuento` double NOT NULL,
  `contrasena_hash` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `correo` (`correo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES ('1001','Ana Pérez','ana@uptc.edu.co','Calle 10 # 5-20','Boyacá','Tunja','3119998877','ESTANDAR',0,'8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92'),('1002','maria','maria@gmail.com','cra 43-6','Cundinamarca','Bogotá','3008769865','ESTANDAR',0,'c010cab4e46b823eb75438baf7eb105ac497f00d9c6689b6be6b2aab72522741'),('246899','valentina','valen@gmail.com','cra 9-10','Valle del Cauca','Tuluá','3213278643','ESTANDAR',0,'3d985753fe20fcfbe16c28b406a66b320081f8b1639fd751a27b8d065f878f0f'),('334455','isabel','isa23@gmail.com','calle 6-2','Cundinamarca','Bogotá','3213452133','ESTANDAR',0,'4132ba2d3c430e7f67e9ae591704a9009b80f10145e69021aeb5f5de4904dcbf');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-11-12 17:25:33
