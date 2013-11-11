-- -----------------------------------------------------
-- Creo esquema centro_educativo
-- -----------------------------------------------------
CREATE SCHEMA centro_educativo;

-- -----------------------------------------------------
-- Tabla Persona
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS centro_educativo.Persona (
  DNI INT NOT NULL,
  nombre VARCHAR(45) NULL,
  apellido VARCHAR(45) NULL,
  direccion VARCHAR(45) NULL,
  CONSTRAINT DNI_pos CHECK (DNI>0),
  PRIMARY KEY (DNI),
  UNIQUE (apellido));

-- -----------------------------------------------------
-- Tabla Empresa
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS centro_educativo.Empresa (
  CUIT INT NOT NULL,
  nombre VARCHAR(45) NULL,
  direccion VARCHAR(45) NULL,
  CONSTRAINT CUIT_pos CHECK (CUIT>0),
  PRIMARY KEY (CUIT));

-- -----------------------------------------------------
-- Tabla Empresa_Telefono
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS centro_educativo.Empresa_Telefono (
  CUIT INT NOT NULL,
  Telefono INT NOT NULL,
  PRIMARY KEY (CUIT, Telefono),
  CONSTRAINT CUIT_CF_Empresa
    FOREIGN KEY (CUIT) REFERENCES centro_educativo.Empresa (CUIT));

-- -----------------------------------------------------
-- Tabla Alumno
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS centro_educativo.Alumno (
  DNI INT NOT NULL,
  edad INT NULL,
  telefono INT NULL,
  CUIT INT NULL,
  PRIMARY KEY (DNI),
  CONSTRAINT DNI_pos CHECK (DNI>0),
  CONSTRAINT DNI_CF_Persona
    FOREIGN KEY (DNI) REFERENCES centro_educativo.Persona (DNI),
  CONSTRAINT CUIT_CF_Empresa
    FOREIGN KEY (CUIT) REFERENCES centro_educativo.Empresa (CUIT));

-- -----------------------------------------------------
-- Tabla Profesor
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS centro_educativo.Profesor (
  DNI INT NOT NULL,
  CONSTRAINT DNI_pos CHECK (DNI>0),
  PRIMARY KEY (DNI),
  CONSTRAINT DNI_CF_Persona
    FOREIGN KEY (DNI) REFERENCES centro_educativo.Persona (DNI));

-- -----------------------------------------------------
-- Tabla Tipo_Curso
-- -----------------------------------------------------
--CREATE DOMAIN EnteroMilAS INT 
--DEFAULT 0
--CHECK ((value>0)AND(value<=1000));

CREATE TABLE IF NOT EXISTS centro_educativo.Tipo_Curso (
  codigo_tipo EnteroMil NOT NULL,
  duracion VARCHAR(45) NULL,
  programa VARCHAR(100) NULL,
  titulo VARCHAR(45) NULL,
  PRIMARY KEY (codigo_tipo));

-- -----------------------------------------------------
-- Tabla Curso
-- -----------------------------------------------------
CREATE TYPE TOrientacion AS ENUM ('Matematica', 'Lengua', 'Naturales', 'Sociales');

CREATE TABLE IF NOT EXISTS centro_educativo.curso (
  codigo_curso INT NOT NULL,
  nombre VARCHAR(45) UNIQUE NOT NULL,
  orientacion TOrientacion NULL,
  fecha_inicio DATE NULL,
  fecha_fin DATE NULL,
  codigo_tipo INT NULL,
  DNI INT NULL,
  PRIMARY KEY (codigo_curso),
  CONSTRAINT codigoTipo_CF_TipoCurso
    FOREIGN KEY (codigo_tipo) REFERENCES centro_educativo.Tipo_Curso (codigo_tipo),
  CONSTRAINT DNI_CF_Profesor
    FOREIGN KEY (DNI) REFERENCES centro_educativo.Profesor (DNI));

CREATE INDEX i_curso ON centro_educativo.curso(codigo_curso);

-- -----------------------------------------------------
-- Tabla Asiste
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS centro_educativo.asiste (
  DNI INT NOT NULL,
  codigo_curso INT NOT NULL,
  PRIMARY KEY (DNI, codigo_curso),
  CONSTRAINT DNI_CF_Alumno
    FOREIGN KEY (DNI) REFERENCES centro_educativo.alumno (DNI),
  CONSTRAINT codigoCurso_CF_Curso
    FOREIGN KEY (codigo_curso) REFERENCES centro_educativo.curso (codigo_curso) On delete cascade);

CREATE INDEX i_asiste ON centro_educativo.asiste(codigo_curso);

-- -----------------------------------------------------
-- Tabla Auditoria_Curso
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS centro_educativo.Auditoria_Curso (
  codigo_auditoria SERIAL,
  codigo_curso INT,
  fecha_inicio_nueva DATE,
  fecha_inicio_vieja DATE,
  fecha_fin_nueva DATE,
  fecha_fin_vieja DATE,
  fecha_actualizacion DATE,
  PRIMARY KEY (codigo_auditoria));

CREATE INDEX i_auditoria ON centro_educativo.auditoria_curso(codigo_auditoria);

CREATE OR REPLACE FUNCTION actualizacion_fecha_curso() RETURNS TRIGGER AS $trigger_auditoria_cursos$
DECLARE
BEGIN
   INSERT INTO centro_educativo.Auditoria_Curso VALUES (
      OLD.codigo_curso, NEW.fecha_inicio, OLD.fecha_inicio, NEW.fecha_fin, OLD.fecha_fin, NOW());
   RETURN NULL;
END;
$trigger_auditoria_cursos$ LANGUAGE plpgsql;

-- -----------------------------------------------------
-- Trigger auditoria cursos
-- -----------------------------------------------------
CREATE TRIGGER trigger_auditoria_cursos
  AFTER UPDATE ON centro_educativo.Curso
  FOR EACH ROW EXECUTE PROCEDURE actualizacion_fecha_curso();

CREATE OR REPLACE FUNCTION sp_get_by_orientacion(TOrientacion) RETURNS text AS $$
DECLARE
   result TEXT;
BEGIN
   SELECT nombre INTO result FROM centro_educativo.curso WHERE orientacion=$1;
   return result;
END
$$ LANGUAGE plpgsql STABLE;
