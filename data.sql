-- product(idProduct, isPrimary, prixUnitaire)
-- ingredient(idIngredient, idProduct, quantite)
-- limonade
    -- eau gazeuze
        -- eau
        -- gaz
    -- sucre
    -- arome citron

CONNECT sys as sysdba
DROP USER star CASCADE;
CREATE USER star IDENTIFIED BY star;
GRANT CONNECT, RESOURCE TO star;
GRANT CREATE VIEW, RESOURCE TO star;
CONNECT star/star

CREATE TABLE product(
    idProduct VARCHAR(7) NOT NULL PRIMARY KEY,
    productName VARCHAR(30),
    isPrimary VARCHAR(1),
    prixUnitaire NUMBER(10)
);
INSERT INTO product VALUES ('PRO0001', 'Limonade', 'n', null);
INSERT INTO product VALUES ('PRO0002', 'Fanta orange', 'n', null);
INSERT INTO product VALUES ('PRO0003', 'Bière', 'n', null);

INSERT INTO product VALUES ('PRO0004', 'Eau-gazeuze', 'n', null);
INSERT INTO product VALUES ('PRO0005', 'Eau', 'y', 2000);
INSERT INTO product VALUES ('PRO0006', 'Gaz', 'y', 2500);
INSERT INTO product VALUES ('PRO0007', 'Sucre', 'y', 100);
INSERT INTO product VALUES ('PRO0008', 'Arôme citron', 'y', 500);

CREATE TABLE ingredient(
    idIngredient VARCHAR(7) NOT NULL PRIMARY KEY,
    idProductFinished VARCHAR(7),
    idProductUsed VARCHAR(7),
    quantity NUMBER(5,2),
    FOREIGN KEY (idProductFinished) REFERENCES product(idProduct),
    FOREIGN KEY (idProductUsed) REFERENCES product(idProduct)
);

INSERT INTO ingredient VALUES ('ING0001', 'PRO0004', 'PRO0005', 1);
INSERT INTO ingredient VALUES ('ING0002', 'PRO0004', 'PRO0006', 0.4);
INSERT INTO ingredient VALUES ('ING0003', 'PRO0001', 'PRO0004', 1);
INSERT INTO ingredient VALUES ('ING0004', 'PRO0001', 'PRO0007', 30);
INSERT INTO ingredient VALUES ('ING0005', 'PRO0001', 'PRO0008', 0.7);

CREATE OR REPLACE VIEW ingredientDetailled AS
    SELECT i.idIngredient,pf.idProduct idProductFinished, pf.productName productFinishedName,pu.idProduct idProductUsed, pu.productName productUsedName, pu.isPrimary, pu.prixUnitaire,(pu.prixUnitaire*i.quantity) prix ,i.quantity
    FROM ingredient i
        JOIN product pf
        ON i.idProductFinished=pf.idProduct
        JOIN product pu
        ON i.idProductUsed=pu.idProduct
;


CREATE TABLE unity(
    idUnity VARCHAR(7) NOT NULL PRIMARY KEY,
    idProduct VARCHAR(7),
    volumicMass NUMBER(4,2),
    FOREIGN KEY (idProduct) REFERENCES product(idProduct)
);

 select* from ingredientDetailled connect by prior idproductUsed=idProductFinished start with idProductFinished='PRO0001';