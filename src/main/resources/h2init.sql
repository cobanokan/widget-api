DROP TABLE IF EXISTS widget;
 
CREATE TABLE widget (
  id uuid NOT NULL PRIMARY KEY,
  x int NOT NULL,
  y int NOT NULL,
  z int NOT NULL,
  width int NOT NULL,
  height int NOT NULL
  last_modification DATE NOT NULL
);
 
