-- -----------------------------------------------------
-- Create schema "educational center".                    --
-- -----------------------------------------------------
CREATE SCHEMA educational_center;

-- -----------------------------------------------------
-- Table person.
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS educational_center.person (
  DNI INT NOT NULL,
  name VARCHAR(45) NULL,
  last_name VARCHAR(45) NULL,
  address VARCHAR(45) NULL,
  CONSTRAINT DNI_pos CHECK (DNI>0),
  PRIMARY KEY (DNI));

-- -----------------------------------------------------
-- Table enterprise
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS educational_center.company (
  CUIT INT NOT NULL,
  name VARCHAR(45) NULL,
  address VARCHAR(45) NULL,
  CONSTRAINT CUIT_pos CHECK (CUIT>0),
  PRIMARY KEY (CUIT));

-- -----------------------------------------------------
-- Table company_phone
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS educational_center.company_phone (
  CUIT INT NOT NULL,
  phone INT NOT NULL,
  PRIMARY KEY (CUIT, phone),
  CONSTRAINT CUIT_CF_company
    FOREIGN KEY (CUIT) REFERENCES educational_center.company (CUIT));

-- -----------------------------------------------------
-- Table student
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS educational_center.student (
  DNI INT NOT NULL,
  age INT NULL,
  phone INT NULL,
  CUIT INT NULL,
  PRIMARY KEY (DNI),
  CONSTRAINT DNI_pos CHECK (DNI>0),
  CONSTRAINT DNI_CF_person
    FOREIGN KEY (DNI) REFERENCES educational_center.person (DNI),
  CONSTRAINT CUIT_CF_company
    FOREIGN KEY (CUIT) REFERENCES educational_center.company (CUIT));

-- -----------------------------------------------------
-- Table professor
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS educational_center.professor (
  DNI INT NOT NULL,
  CONSTRAINT DNI_pos CHECK (DNI>0),
  PRIMARY KEY (DNI),
  CONSTRAINT DNI_CF_person
    FOREIGN KEY (DNI) REFERENCES educational_center.person (DNI));

-- -----------------------------------------------------
-- Table Course_Type
-- -----------------------------------------------------
CREATE DOMAIN thousand AS INT 
  DEFAULT 0
  CHECK ((value>0)AND(value<=1000));

CREATE TABLE IF NOT EXISTS educational_center.Tipo_course (
  code_type thousand NOT NULL,
  duration VARCHAR(45) NULL,
  program VARCHAR(100) NULL,
  title VARCHAR(45) NULL,
  PRIMARY KEY (code_type));

-- -----------------------------------------------------
-- Table course
-- -----------------------------------------------------
CREATE TYPE Torientation AS ENUM ('Matematica', 'Lengua', 'Naturales', 'Sociales');

CREATE TABLE IF NOT EXISTS educational_center.course (
  course_code INT NOT NULL,
  name VARCHAR(45) NULL,
  orientation Torientation NULL,
  date_start DATE NULL,
  date_end DATE NULL,
  code_type INT NULL,
  DNI INT NULL,
  PRIMARY KEY (course_code),
  CONSTRAINT codigoTipo_CF_Tipocourse
    FOREIGN KEY (code_type) REFERENCES educational_center.Tipo_course (code_type),
  CONSTRAINT DNI_CF_professor
    FOREIGN KEY (DNI) REFERENCES educational_center.professor (DNI));

-- -----------------------------------------------------
-- Table Asiste
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS educational_center.Asiste (
  DNI INT NOT NULL,
  course_code INT NOT NULL,
  PRIMARY KEY (DNI, course_code),
  CONSTRAINT DNI_CF_student
    FOREIGN KEY (DNI) REFERENCES educational_center.student (DNI),
  CONSTRAINT codigocourse_CF_course
    FOREIGN KEY (course_code) REFERENCES educational_center.course (course_code));

-- -----------------------------------------------------
-- Table Auditoria_course
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS educational_center.Auditoria_course (
  audit_code SERIAL,
  course_code INT,
  date_start_new DATE,
  date_start_old DATE,
  date_end_new DATE,
  date_end_old DATE,
  date_update DATE,
  PRIMARY KEY (audit_code));

CREATE OR REPLACE FUNCTION update_date_course() RETURNS TRIGGER AS $trigger_audit_course$
DECLARE
BEGIN
   INSERT INTO educational_center.Auditoria_course VALUES (
      OLD.course_code, NEW.date_start, OLD.date_start, NEW.date_end, OLD.date_end, NOW());
   RETURN NULL;
END;
$trigger_audit_course$ LANGUAGE plpgsql;

-- -----------------------------------------------------
-- Trigger auditoria cursos
-- -----------------------------------------------------
CREATE TRIGGER trigger_audit_course
  AFTER UPDATE ON educational_center.course
  FOR EACH ROW EXECUTE PROCEDURE update_date_course();
