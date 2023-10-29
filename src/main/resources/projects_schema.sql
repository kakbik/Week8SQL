DROP TABLE IF EXISTS project_category;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS step;
DROP TABLE IF EXISTS material;
DROP TABLE IF EXISTS project;

CREATE TABLE project (
  project_id INT AUTO_INCREMENT NOT NULL,
  project_name VARCHAR(128) NOT NULL,
  estimated_hours decimal(7,2),
  actual_hours decimal (7,2),
  deifficulty INT,
  notes TEXT,
  PRIMARY KEY (project_id)
);

CREATE TABLE material (
  material_id INT AUTO_INCREMENT not null,
  project_id INT not null,
  material_name VARCHAR(128) NOT NULL,
  num_required INT,
  cost decimal(7,2),
  PRIMARY KEY (material_id)
);

CREATE TABLE step (
  step_id INT AUTO_INCREMENT NOT NULL,
  project_id INT not null,
  step_text TEXT not null,
  step_order INT not null,
  PRIMARY KEY (step_id),
  foreign key (project_id) references project (project_id) on delete CASCADE
);

CREATE TABLE category (
  category_id INT auto_increment not null,
  category_name varchar(128) not null,
  primary key (category_id)
);

CREATE TABLE project_category (
  project_id INT NOT NULL,
  category_id INT NOT NULL,
  FOREIGN KEY (project_id) REFERENCES project (project_id) ON DELETE CASCADE,
  FOREIGN KEY (category_id) REFERENCES category (category_id) ON DELETE CASCADE,
  UNIQUE KEY (project_id, category_id)
);

