-- phpMyAdmin SQL Dump
-- version 4.8.5
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 24-08-2021 a las 00:36:10
-- Versión del servidor: 10.1.38-MariaDB
-- Versión de PHP: 7.3.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `tiramisustore`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tabla_capital`
--

CREATE TABLE `tabla_capital` (
  `Id` int(10) NOT NULL,
  `Capital` double(10,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tabla_configuracion`
--

CREATE TABLE `tabla_configuracion` (
  `Id` int(10) NOT NULL,
  `Ingles` varchar(45) DEFAULT NULL,
  `Audio` varchar(45) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tabla_producto`
--

CREATE TABLE `tabla_producto` (
  `Id` int(10) NOT NULL,
  `Codigo` varchar(45) DEFAULT NULL,
  `Nombre` varchar(45) DEFAULT NULL,
  `Compra` double(10,2) DEFAULT NULL,
  `Venta` double(10,2) DEFAULT NULL,
  `Fecha` varchar(50) DEFAULT NULL,
  `Cantidad` int(5) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `tabla_producto`
--

INSERT INTO `tabla_producto` (`Id`, `Codigo`, `Nombre`, `Compra`, `Venta`, `Fecha`, `Cantidad`) VALUES
(21, '001', 'mirinda', 20.00, 25.00, '0000000', 3),
(22, '002', 'coca', 20.00, 22.00, '000000', 1),
(23, '003', 'mirinda', 10.00, 12.00, '000000', 1),
(24, '004', 'sprite', 5.00, 8.00, '000000', 1),
(27, '009', 'ssss', 100.00, 120.00, '101010', 1),
(30, '005', 'toraso', 20.00, 30.00, '000000', 0),
(31, '005', 'toraso', 20.00, 30.00, '000000', 0),
(32, '008', 'DSFDS', 100.00, 110.00, 'FDSF', 1),
(33, '010', 'seco', 10.00, 15.00, '0000000', 1),
(34, '009', 'mirinda', 10.00, 15.00, '0000000', 1),
(35, '011', 'mirinda', 10.00, 15.00, '0000000', 1),
(36, 'rm3', 'remera', 200.00, 210.00, '0000000', 9);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tabla_provedores`
--

CREATE TABLE `tabla_provedores` (
  `Id` int(10) NOT NULL,
  `Nombre` varchar(45) DEFAULT NULL,
  `Telefono` varchar(80) DEFAULT NULL,
  `Correo` varchar(45) DEFAULT NULL,
  `Descripcion` varchar(300) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `tabla_provedores`
--

INSERT INTO `tabla_provedores` (`Id`, `Nombre`, `Telefono`, `Correo`, `Descripcion`) VALUES
(12, 'chesna', '3816487545', 'chesna@hotmail.com', 'venden tazas dde harry potter');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `tabla_venta`
--

CREATE TABLE `tabla_venta` (
  `Id` int(10) NOT NULL,
  `Codigo` varchar(45) DEFAULT NULL,
  `Nombre` varchar(45) DEFAULT NULL,
  `Importe` double(10,2) DEFAULT NULL,
  `Ganancia` double(10,2) DEFAULT NULL,
  `Cantidad` int(50) DEFAULT NULL,
  `Fecha` varchar(50) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `tabla_venta`
--

INSERT INTO `tabla_venta` (`Id`, `Codigo`, `Nombre`, `Importe`, `Ganancia`, `Cantidad`, `Fecha`) VALUES
(1, '3432', 'DSFDS', 21321.00, 20090.00, 1, ''),
(2, '3432', 'DSFDS', 21321.00, 20090.00, 1, ''),
(3, '3432', 'DSFDS', 21321.00, 20090.00, 1, 'FDSF'),
(4, '3432', 'DSFDS', 21321.00, 20090.00, 1, 'FDSF'),
(5, '3432', 'DSFDS', 21321.00, 20090.00, 1, '15/08/2021'),
(6, '3432', 'DSFDS', 21321.00, 20090.00, 1, '15/08/2021'),
(7, '002', 'coca', 22.00, 2.00, 1, '15/08/2021'),
(8, '003', 'mirinda', 36.00, 6.00, 3, '15/08/2021'),
(9, '009', 'ssss', 120.00, 20.00, 1, '15/08/2021'),
(10, '008', 'DSFDS', 110.00, 10.00, 1, '15/08/2021'),
(11, '008', 'DSFDS', 110.00, 10.00, 1, '15/08/2021'),
(12, '003', 'mirinda', 12.00, 2.00, 1, '15/08/2021'),
(13, '003', 'mirinda', 12.00, 2.00, 1, '15/08/2021'),
(14, '001', 'mirinda', 15.00, 5.00, 1, '15/08/2021'),
(15, '009', 'ssss', 120.00, 20.00, 1, '15/08/2021'),
(16, '004', 'sprite', 8.00, 3.00, 1, '15/08/2021'),
(17, '002', 'coca', 22.00, 2.00, 1, '15/08/2021'),
(18, '001', 'mirinda', 15.00, 5.00, 1, '15/08/2021'),
(19, '002', 'coca', 22.00, 2.00, 1, '15/08/2021'),
(20, '001', 'mirinda', 15.00, 5.00, 1, '15/08/2021'),
(21, '002', 'coca', 22.00, 2.00, 1, '15/08/2021'),
(22, '001', 'mirinda', 15.00, 5.00, 1, '15/08/2021'),
(23, '003', 'mirinda', 12.00, 2.00, 1, '15/08/2021'),
(24, '004', 'sprite', 8.00, 3.00, 1, '15/08/2021'),
(25, '004', 'sprite', 8.00, 3.00, 1, '15/08/2021'),
(26, '009', 'ssss', 120.00, 20.00, 1, '15/08/2021'),
(27, '004', 'sprite', 8.00, 3.00, 1, '15/08/2021'),
(28, '009', 'ssss', 120.00, 20.00, 1, '15/08/2021'),
(29, '002', 'coca', 22.00, 2.00, 1, '15/08/2021'),
(30, '003', 'mirinda', 12.00, 2.00, 1, '15/08/2021'),
(31, '002', 'coca', 22.00, 2.00, 1, '15/08/2021'),
(32, '009', 'ssss', 120.00, 20.00, 1, '15/08/2021'),
(33, '001', 'mirinda', 15.00, 5.00, 1, '15/08/2021'),
(34, '001', 'mirinda', 15.00, 5.00, 1, '15/08/2021'),
(35, '002', 'coca', 22.00, 2.00, 1, '15/08/2021'),
(36, '009', 'ssss', 120.00, 20.00, 1, '15/08/2021'),
(37, '002', 'coca', 22.00, 2.00, 1, '15/08/2021'),
(38, '003', 'mirinda', 12.00, 2.00, 1, '15/08/2021'),
(39, '002', 'coca', 22.00, 2.00, 1, '15/08/2021'),
(40, '003', 'mirinda', 12.00, 2.00, 1, '15/08/2021'),
(41, '009', 'ssss', 120.00, 20.00, 1, '15/08/2021'),
(42, '008', 'DSFDS', 110.00, 10.00, 1, '15/08/2021'),
(43, '001', 'mirinda', 15.00, 5.00, 1, '15/08/2021'),
(44, '001', 'mirinda', 15.00, 5.00, 1, '15/08/2021'),
(45, '004', 'sprite', 8.00, 3.00, 1, '15/08/2021'),
(46, '004', 'sprite', 8.00, 3.00, 1, '15/08/2021'),
(47, '008', 'DSFDS', 110.00, 10.00, 1, '15/08/2021'),
(48, '001', 'mirinda', 15.00, 5.00, 1, '15/08/2021'),
(49, '009', 'ssss', 120.00, 20.00, 1, '16/08/2021'),
(50, '009', 'mirinda', 120.00, 20.00, 1, '16/08/2021'),
(51, '008', 'DSFDS', 110.00, 10.00, 1, '16/08/2021'),
(52, '008', 'DSFDS', 110.00, 10.00, 1, '16/08/2021'),
(53, '008', 'DSFDS', 220.00, 20.00, 2, '16/08/2021'),
(54, '008', 'DSFDS', 110.00, 10.00, 1, '16/08/2021'),
(55, '001', 'mirinda', 25.00, 5.00, 1, '20/08/2021'),
(56, '001', 'mirinda', 25.00, 5.00, 1, '20/08/2021'),
(57, '001', 'mirinda', 25.00, 5.00, 1, '20/08/2021'),
(58, '001', 'mirinda', 25.00, 5.00, 1, '20/08/2021'),
(59, '001', 'mirinda', 25.00, 5.00, 1, '20/08/2021'),
(60, '001', 'mirinda', 25.00, 5.00, 1, '20/08/2021'),
(61, '001', 'mirinda', 25.00, 5.00, 1, '20/08/2021'),
(62, 'rm3', 'remera', 210.00, 10.00, 1, '20/08/2021');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `tabla_capital`
--
ALTER TABLE `tabla_capital`
  ADD PRIMARY KEY (`Id`);

--
-- Indices de la tabla `tabla_configuracion`
--
ALTER TABLE `tabla_configuracion`
  ADD PRIMARY KEY (`Id`);

--
-- Indices de la tabla `tabla_producto`
--
ALTER TABLE `tabla_producto`
  ADD PRIMARY KEY (`Id`);

--
-- Indices de la tabla `tabla_provedores`
--
ALTER TABLE `tabla_provedores`
  ADD PRIMARY KEY (`Id`);

--
-- Indices de la tabla `tabla_venta`
--
ALTER TABLE `tabla_venta`
  ADD PRIMARY KEY (`Id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `tabla_capital`
--
ALTER TABLE `tabla_capital`
  MODIFY `Id` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `tabla_configuracion`
--
ALTER TABLE `tabla_configuracion`
  MODIFY `Id` int(10) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `tabla_producto`
--
ALTER TABLE `tabla_producto`
  MODIFY `Id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=37;

--
-- AUTO_INCREMENT de la tabla `tabla_provedores`
--
ALTER TABLE `tabla_provedores`
  MODIFY `Id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT de la tabla `tabla_venta`
--
ALTER TABLE `tabla_venta`
  MODIFY `Id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=63;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
