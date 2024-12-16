-- MySQL Script generated by MySQL Workbench
-- Fri Nov 29 19:38:47 2024
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE DATABASE IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;
USE `mydb` ;


-- -----------------------------------------------------
-- Table `mydb`.`Funcionário`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Funcionário` (
  `CodFuncionário` INT NOT NULL AUTO_INCREMENT,
  `Nome` VARCHAR(45) NOT NULL,
  `CPF` CHAR(11) NOT NULL,
  `Telefone` VARCHAR(45) NOT NULL,
  `DataContratação` DATE NOT NULL,
  `TipodeFuncionário` VARCHAR(45) NULL,
  PRIMARY KEY (`CodFuncionário`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Gerente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Gerente` (
  `Departamento` VARCHAR(45) NOT NULL,
  `Nível de Gestão` VARCHAR(45) NOT NULL,
  `CodGerente` INT NOT NULL,
  PRIMARY KEY (`CodGerente`),
  CONSTRAINT `pk_Gerente_funcionário`
    FOREIGN KEY (`CodGerente`)
    REFERENCES `mydb`.`Funcionário` (`CodFuncionário`)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Evento`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Evento` (
  `codEvento` INT NOT NULL,
  `Data` DATE NOT NULL,
  `Descrição` VARCHAR(45) NOT NULL,
  `N° de Participantes` INT NOT NULL,
  `codOrganizador` INT NOT NULL,
  PRIMARY KEY (`codEvento`),
  INDEX `pk_Gerente_Evento_idx` (`codOrganizador` ASC) VISIBLE,
  CONSTRAINT `pk_Gerente_Evento`
    FOREIGN KEY (`codOrganizador`)
    REFERENCES `mydb`.`Gerente` (`CodGerente`)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

ALTER TABLE `mydb`.`Evento`
  MODIFY COLUMN `codEvento` INT NOT NULL AUTO_INCREMENT,
  AUTO_INCREMENT = 20000;

-- -----------------------------------------------------
-- Table `mydb`.`Prato`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Prato` (
  `CodPrato` INT NOT NULL,
  `Tempo de Preparo` TIME NOT NULL,
  `Preço` DECIMAL(5,2) NOT NULL,
  `Descrição` VARCHAR(100) NOT NULL,
  `Nome` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`CodPrato`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Cliente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Cliente` (
  `CodCliente` INT NOT NULL,
  `Nome` VARCHAR(45) NOT NULL,
  `Telefone` VARCHAR(45) NOT NULL,
  `Número de Fidelidade` INT NOT NULL,
  PRIMARY KEY (`CodCliente`))
ENGINE = INNODB;

ALTER TABLE `mydb`.`Cliente`
  MODIFY COLUMN `CodCliente` INT NOT NULL AUTO_INCREMENT,
  AUTO_INCREMENT = 8000;


-- -----------------------------------------------------
-- Table `mydb`.`Mesa`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Mesa` (
  `Número` INT NOT NULL,
  `Capacidade` INT NOT NULL,
  `Status` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`Número`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Fornecedor`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Fornecedor` (
  `CodFornecedor` INT NOT NULL AUTO_INCREMENT,
  `Nome` VARCHAR(45) NOT NULL,
  `Telefone` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`CodFornecedor`))
ENGINE = INNODB;

ALTER TABLE `mydb`.`Fornecedor` AUTO_INCREMENT = 100;


-- -----------------------------------------------------
-- Table `mydb`.`Atendente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Atendente` (
  `Turno de Trabalho` VARCHAR(45) NULL,
  `IdAtendente` INT NOT NULL,
  PRIMARY KEY (`IdAtendente`),
  CONSTRAINT `funAtendenteCod`
    FOREIGN KEY (`IdAtendente`)
    REFERENCES `mydb`.`Funcionário` (`CodFuncionário`)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Pedido`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Pedido` (
  `CodPedido` INT NOT NULL AUTO_INCREMENT,
  `Data/Hora` VARCHAR(45) NOT NULL,
  `Status` VARCHAR(45) NOT NULL,
  `n°Mesa` INT NOT NULL,
  `CodCliente` INT NOT NULL,
  `CodAtendente` INT NOT NULL,
  PRIMARY KEY (`CodPedido`),
  INDEX `fk_Pedido_Mesa_idx` (`n°Mesa` ASC) VISIBLE,
  INDEX `fk_Pedido_Cliente_idx` (`CodCliente` ASC) VISIBLE,
  INDEX `fk_Pedido_Atendente_idx` (`CodAtendente` ASC) VISIBLE,
  CONSTRAINT `fk_Pedido_Mesa`
    FOREIGN KEY (`n°Mesa`)
    REFERENCES `mydb`.`Mesa` (`Número`)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Pedido_Cliente`
    FOREIGN KEY (`CodCliente`)
    REFERENCES `mydb`.`Cliente` (`CodCliente`)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Pedido_Atendente`
    FOREIGN KEY (`CodAtendente`)
    REFERENCES `mydb`.`Atendente` (`IdAtendente`)
    ON DELETE NO ACTION
    ON UPDATE RESTRICT)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Ingrediente`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Ingrediente` (
  `CodIngrediente` INT NOT NULL,
  `Quantidade` INT NOT NULL,
  `Unidade de Medida` VARCHAR(45) NOT NULL,
  `Nome` VARCHAR(45) NOT NULL,
  `Data de Validade` DATE NOT NULL,
  PRIMARY KEY (`CodIngrediente`))
ENGINE = INNODB;


ALTER TABLE `mydb`.`Ingrediente`
  MODIFY COLUMN `CodIngrediente` INT NOT NULL AUTO_INCREMENT,
  AUTO_INCREMENT = 10000;


-- -----------------------------------------------------
-- Table `mydb`.`Cozinheiro`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Cozinheiro` (
  `Especialização` VARCHAR(45) NOT NULL,
  `IdCozinheiro` INT NOT NULL,
  `CodChef` INT NULL,
  PRIMARY KEY (`IdCozinheiro`),
  INDEX `pk_Cozinheiro_Cozinheiro_idx` (`CodChef` ASC) VISIBLE,
  CONSTRAINT `pk_Cozinheiro.funcionário`
    FOREIGN KEY (`IdCozinheiro`)
    REFERENCES `mydb`.`Funcionário` (`CodFuncionário`)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION,
  CONSTRAINT `pk_Cozinheiro_Cozinheiro`
    FOREIGN KEY (`CodChef`)
    REFERENCES `mydb`.`Cozinheiro` (`IdCozinheiro`)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Preparo`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Preparo` (
  `CodIngrediente` INT NOT NULL,
  `CodPrato` INT NOT NULL,
  PRIMARY KEY (`CodIngrediente`, `CodPrato`),
  INDEX `pk_Preparo_Prato_idx` (`CodPrato` ASC) VISIBLE,
  CONSTRAINT `pk_Preparo_Prato`
    FOREIGN KEY (`CodPrato`)
    REFERENCES `mydb`.`Prato` (`CodPrato`)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION,
  CONSTRAINT `pk_Preparo_Ingrediente`
    FOREIGN KEY (`CodIngrediente`)
    REFERENCES `mydb`.`Ingrediente` (`CodIngrediente`)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Fornecimento`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Fornecimento` (
  `CodFornecedor` INT NOT NULL,
  `CodIngrediente` INT NOT NULL,
  `Quantidade` INT NOT NULL,
  PRIMARY KEY (`CodFornecedor`, `CodIngrediente`),
  INDEX `pk_Fornecimento_Ingrediente_idx` (`CodIngrediente` ASC) VISIBLE,
  CONSTRAINT `pk_Fornecimento_Fornecedor`
    FOREIGN KEY (`CodFornecedor`)
    REFERENCES `mydb`.`Fornecedor` (`CodFornecedor`)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION,
  CONSTRAINT `pk_Fornecimento_Ingrediente`
    FOREIGN KEY (`CodIngrediente`)
    REFERENCES `mydb`.`Ingrediente` (`CodIngrediente`)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Item`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Item` (
  `CodPedido` INT NOT NULL,
  `CodPrato` INT NOT NULL,
  PRIMARY KEY (`CodPedido`, `CodPrato`),
  INDEX `pk_Item_Prato_idx` (`CodPrato` ASC) VISIBLE,
  CONSTRAINT `pk_Item_Pedido`
    FOREIGN KEY (`CodPedido`)
    REFERENCES `mydb`.`Pedido` (`CodPedido`)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION,
  CONSTRAINT `pk_Item_Prato`
    FOREIGN KEY (`CodPrato`)
    REFERENCES `mydb`.`Prato` (`CodPrato`)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`Funcionário/Evento`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`Funcionário/Evento` (
  `CodFuncionário` INT NOT NULL,
  `CodEvento` INT NOT NULL,
  PRIMARY KEY (`CodFuncionário`, `CodEvento`),
  INDEX `pk_Evento_Trabalho_idx` (`CodEvento` ASC) VISIBLE,
  CONSTRAINT `pk_funcionário_Trabalho`
    FOREIGN KEY (`CodFuncionário`)
    REFERENCES `mydb`.`Funcionário` (`CodFuncionário`)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION,
  CONSTRAINT `pk_Evento_Trabalho`
    FOREIGN KEY (`CodEvento`)
    REFERENCES `mydb`.`Evento` (`codEvento`)
    ON DELETE RESTRICT
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

CREATE VIEW pedidos AS
SELECT 
                    p.CodPedido,
                    p.`Data/Hora` AS DataHora,
                    p.Status,
                    c.Nome AS Cliente,
                    f.Nome AS Atendente,
                    p.`n°Mesa` AS Mesa,
                    GROUP_CONCAT(pr.Nome SEPARATOR ', ') AS Pratos
                FROM Pedido p
                INNER JOIN Cliente c ON p.CodCliente = c.CodCliente
                INNER JOIN Atendente a ON p.CodAtendente = a.IdAtendente
                INNER JOIN Funcionário f ON a.IdAtendente = f.CodFuncionário
                INNER JOIN Item i ON p.CodPedido = i.CodPedido
                INNER JOIN Prato pr ON i.CodPrato = pr.CodPrato
                GROUP BY p.CodPedido, p.`Data/Hora`, p.Status, c.Nome, f.Nome, p.`n°Mesa`
                ORDER BY p.CodPedido;
                
CREATE OR REPLACE VIEW fornecimentos AS
SELECT 
    f.Nome AS Fornecedor,
    i.Nome AS Ingrediente,
    fo.Quantidade AS Quantidade,  -- Agora a quantidade vem da tabela Fornecimento
    i.`Unidade de Medida`,
    i.`Data de Validade`
FROM Fornecimento fo
INNER JOIN Fornecedor f ON fo.CodFornecedor = f.CodFornecedor
INNER JOIN Ingrediente i ON fo.CodIngrediente = i.CodIngrediente
ORDER BY f.Nome, i.Nome;


CREATE VIEW funcionario_evento AS
SELECT 
    fe.CodFuncionário,
    f.Nome AS NomeFuncionario,
	 fe.CodEvento,
	 e.Descrição AS DescricaoEvento
FROM `Funcionário/Evento` fe
JOIN Funcionário f ON fe.CodFuncionário = f.CodFuncionário
JOIN Evento e ON fe.CodEvento = e.codEvento
ORDER BY f.Nome, e.Data;



INSERT INTO Funcionário (Nome, CPF, Telefone, DataContratação, TipodeFuncionário) VALUES
('João Silva', '12345678900', '21999999999', '2022-05-01', 'Atendente'),
('Maria Oliveira', '98765432100', '21988888888', '2021-11-15', 'Cozinheiro'),
('Carlos Souza', '11122233344', '21977777777', '2020-01-20', 'Gerente'),
('Fernanda Costa', '33344455566', '21966666666', '2023-02-10', 'Atendente'),
('Ricardo Lima', '55566677788', '21955555555', '2021-07-05', 'Cozinheiro'),
('Cláudia Mendes', '88899900011', '21944444444', '2019-09-12', 'Gerente'),
('Luís Rocha', '22233344455', '21933333333', '2020-12-08', 'Atendente'),
('Rafaela Braga', '44455566677', '21922222222', '2023-06-18', 'Cozinheiro'),
('Bruno Ferreira', '66677788899', '21911111111', '2021-04-03', 'Gerente'),
('Camila Silva', '77788899900', '21900000000', '2022-11-25', 'Atendente');



INSERT INTO Atendente (IdAtendente, `Turno de Trabalho`) VALUES
(1, 'Manhã'),
(4, 'Tarde'),
(7, 'Noite'),
(10, 'Manhã');


INSERT INTO Cozinheiro (IdCozinheiro, Especialização, CodChef) VALUES 
(2, 'Massas', NULL),
(5, 'Carnes', 2), 
(8, 'Sobremesas', 5);

INSERT INTO Gerente (CodGerente, Departamento, `Nível de Gestão`) VALUES
(3, 'Operacional', 'Pleno'),
(6, 'Financeiro', 'Sênior'),
(9, 'Recursos Humanos', 'Júnior');

INSERT INTO Cliente (Nome, Telefone, `Número de Fidelidade`) VALUES
('Ana Paula', '21966666666', '123456'),
('Pedro Lima', '21955555555', '789101'),
('Lucas Almeida', '21944444444', '456789'),
('Mariana Torres', '21933333333', '987654'),
('Felipe Martins', '21922222222', '654321'),
('Juliana Gonçalves', '21911111111', '112233'),
('Bruno Carvalho', '21900000000', '223344'),
('Isabela Souza', '21999998888', '334455'),
('Gabriel Castro', '21988887777', '445566'),
('Letícia Pereira', '21977776666', '556677');

INSERT INTO Fornecedor (CodFornecedor, Nome, Telefone) VALUES
(1, 'Distribuidora Alimentos Frescos', '21999999999'),
(2, 'Casa dos Laticínios', '21988888888'),
(3, 'Pescados do Atlântico', '21977777777'),
(4, 'Hortifruti Qualidade', '21966666666'),
(5, 'Grãos & Cereais', '21955555555'),
(6, 'Especiarias Orientais', '21944444444'),
(7, 'Carne e Cia', '21933333333'),
(8, 'Molhos e Conservas', '21922222222'),
(9, 'Padaria Suprema', '21911111111'),
(10, 'Adega Gourmet', '21900000000');




INSERT INTO Ingrediente (Nome, `Data de Validade`, `Unidade de Medida`, Quantidade) VALUES
('Cebola', '2024-12-29', 'kg', 15.0),
('Alho', '2025-01-15', 'kg', 8.0),
('Pimenta do Reino', '2025-02-10', 'g', 500),
('Orégano', '2025-03-01', 'g', 300),
('Azeite de Oliva', '2025-06-15', 'litros', 10.0),
('Arroz Arbóreo', '2025-05-30', 'kg', 18.0),
('Camarão', '2024-12-25', 'kg', 12.0),
('Filé Mignon', '2024-12-20', 'kg', 20.0),
('Linguiça', '2024-12-18', 'kg', 10.0),
('Couve', '2024-12-22', 'unidades', 30),
('Batata', '2024-12-28', 'kg', 25.0),
('Molho de Tomate', '2025-01-10', 'litros', 15.0),
('Farinha de Mandioca', '2025-03-01', 'kg', 10.0),
('Queijo Cheddar', '2024-12-26', 'kg', 7.0),
('Pancetta', '2024-12-24', 'kg', 5.0),
('Cenoura', '2024-12-20', 'kg', 12.0),
('Molho de Soja', '2025-01-15', 'litros', 5.0),
('Coco Ralado', '2024-12-31', 'kg', 4.0),
('Pimentão Vermelho', '2024-12-21', 'unidades', 25),
('Leite de Coco', '2025-01-05', 'litros', 6.0),
('Nozes', '2025-02-15', 'kg', 3.0),
('Peixe Branco', '2024-12-27', 'kg', 10.0),
('Carne Suína', '2024-12-22', 'kg', 8.0),
('Ervilhas', '2025-01-30', 'kg', 10.0),
('Champignon', '2024-12-26', 'kg', 5.0),
('Abóbora', '2024-12-24', 'unidades', 10),
('Vinho Branco', '2025-03-01', 'litros', 8.0),
('Cebolinha', '2024-12-15', 'maços', 20),
('Salsinha', '2024-12-15', 'maços', 20),
('Requeijão', '2024-12-31', 'kg', 6.0);



INSERT INTO Mesa (Número, Capacidade, Status) VALUES
(1, 4, 'Disponível'),
(2, 6, 'Ocupada'),
(3, 2, 'Reservada'),
(4, 8, 'Disponível'),
(5, 4, 'Ocupada'),
(6, 6, 'Reservada'),
(7, 2, 'Disponível'),
(8, 8, 'Ocupada'),
(9, 4, 'Disponível'),
(10, 6, 'Reservada');

-- Entradas
INSERT INTO `mydb`.`Prato` (CodPrato, `Tempo de Preparo`, Preço, Descrição, Nome) VALUES
(1, '00:15:00', 18.00, 'Tomate, muçarela de búfala, manjericão e azeite balsâmico.', 'Salada Caprese'),
(2, '00:20:00', 32.00, 'Seleção de queijos, embutidos e pães artesanais.', 'Tábua de Frios'),
(3, '00:10:00', 25.00, 'Tradicional bolinho de bacalhau servido com limão.', 'Bolinho de Bacalhau (6 un.)'),
(4, '00:25:00', 20.00, 'Sopa francesa gratinada com queijo parmesão.', 'Sopa de Cebola Gratinada'),

-- Pratos Principais
(5, '00:30:00', 48.00, 'Arroz arbóreo com camarão, vinho branco e especiarias.', 'Risoto de Camarão'),
(6, '01:00:00', 55.00, 'Costela bovina assada lentamente, acompanhada de arroz e farofa.', 'Costela no Bafo'),
(7, '00:40:00', 40.00, 'Tilápia servida com purê de batata e legumes salteados.', 'Tilápia Grelhada'),
(8, '00:25:00', 35.00, 'Massa ao molho de gemas, queijo pecorino e bacon crocante.', 'Espaguete à Carbonara'),
(9, '00:50:00', 38.00, 'Feijão preto com carnes, acompanhado de arroz, couve e farofa.', 'Feijoada Completa (Porção Individual)'),
(10, '00:30:00', 30.00, 'Mix de legumes grelhados com quinoa e molho especial.', 'Vegetariano do Chef'),

-- Bebidas
(11, '00:00:00', 6.00, 'Coca-Cola, Guaraná, Sprite ou Pepsi.', 'Refrigerante (lata)'),
(12, '00:00:00', 10.00, 'Sabores: laranja, abacaxi, manga ou limão.', 'Suco Natural'),
(13, '00:00:00', 8.00, 'Água com Gás.', 'Chá Gelado'),
(14, '00:00:00', 4.00, 'Água com gás.', 'Água com Gás'),
(15, '00:00:00', 3.50, 'Água sem gás.', 'Água Sem Gás'),
(16, '00:00:00', 18.00, 'Cerveja Artesanal.', 'Cerveja Artesanal'),
(17, '00:00:00', 25.00, 'Vinho Tinto (Taça).', 'Vinho Tinto (Taça)'),

-- Sobremesas
(18, '00:15:00', 22.00, 'Cheesecake clássico com calda de frutas vermelhas.', 'Cheesecake de Frutas Vermelhas'),
(19, '00:10:00', 25.00, 'Bolo quente de chocolate com sorvete de baunilha.', 'Petit Gateau'),
(20, '00:20:00', 20.00, 'Massa crocante com recheio de limão e merengue.', 'Torta de Limão'),
(21, '00:15:00', 18.00, 'Sobremesa cremosa com calda de maracujá.', 'Mousse de Maracujá'),
(22, '00:10:00', 15.00, 'Sorvete com chantilly e calda de chocolate.', 'Taça de Sorvete Especial');

