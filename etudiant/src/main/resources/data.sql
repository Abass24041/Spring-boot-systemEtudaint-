-- ============================================================
-- database.sql
-- Portail Etudiant - SupNum
-- ============================================================

CREATE DATABASE IF NOT EXISTS portail_etudiant
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE portail_etudiant;

-- ============================================================
-- TABLE: etudiants
-- ============================================================
CREATE TABLE IF NOT EXISTS etudiants (
    id                BIGINT       NOT NULL AUTO_INCREMENT,
    nom               VARCHAR(100) NOT NULL,
    prenom            VARCHAR(100) NOT NULL,
    email             VARCHAR(150) NOT NULL,
    password          VARCHAR(255) NOT NULL,
    matricule         VARCHAR(50),
    numero_national   VARCHAR(50),
    telephone         VARCHAR(30),
    specialite        VARCHAR(150),
    niveau            VARCHAR(20),
    role              VARCHAR(20)  NOT NULL DEFAULT 'USER',
    date_naissance    VARCHAR(20),
    lieu_naissance    VARCHAR(100),
    pays              VARCHAR(100),
    date_inscription  VARCHAR(20),

    PRIMARY KEY (id),
    UNIQUE KEY uk_email           (email),
    UNIQUE KEY uk_matricule       (matricule),
    UNIQUE KEY uk_numero_national (numero_national)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- TABLE: notes
-- ============================================================
CREATE TABLE IF NOT EXISTS notes (
    id               BIGINT       NOT NULL AUTO_INCREMENT,
    code             VARCHAR(20)  NOT NULL,
    module           VARCHAR(200) NOT NULL,
    matiere          VARCHAR(200) NOT NULL,
    credit           INT,
    valeur           DOUBLE,
    note_devoir      DOUBLE,
    note_rattrapage  DOUBLE,
    semestre         VARCHAR(10)  NOT NULL,
    etudiant_id      BIGINT       NOT NULL,

    PRIMARY KEY (id),
    CONSTRAINT fk_notes_etudiant
        FOREIGN KEY (etudiant_id) REFERENCES etudiants(id)
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- DONNEES : Admin + Etudiant demo
-- admin@supnum.mr    / admin123
-- etudiant@supnum.mr / 1234567890
-- ============================================================
INSERT INTO etudiants (nom, prenom, email, password, matricule, numero_national,
                       telephone, specialite, niveau, role,
                       date_naissance, lieu_naissance, pays, date_inscription)
SELECT 'Admin', 'SupNum', 'admin@supnum.mr',
       '$2a$10$7EqJtq98hPqEX7fNZaFWoOe3d1MCo9oR7lA1BzCTlZHZ3NfW8eXSi',
       'SN2026000', '0000000000', '+222 00 00 00 00',
       NULL, NULL, 'ADMIN',
       '1990-01-01', 'Nouakchott', 'Mauritanie', '2024-01-01'
WHERE NOT EXISTS (SELECT 1 FROM etudiants WHERE email = 'admin@supnum.mr');

INSERT INTO etudiants (nom, prenom, email, password, matricule, numero_national,
                       telephone, specialite, niveau, role,
                       date_naissance, lieu_naissance, pays, date_inscription)
SELECT 'Diallo', 'Aminata', 'etudiant@supnum.mr',
       '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
       'SN2026001', '1234567890', '+222 20 00 00 01',
       'Développement Web et Mobile', 'L3', 'USER',
       '2002-07-04', 'Nouakchott', 'Mauritanie', '2023-10-01'
WHERE NOT EXISTS (SELECT 1 FROM etudiants WHERE email = 'etudiant@supnum.mr');

-- ============================================================
-- DONNEES : Etudiants supplementaires
-- ============================================================
INSERT INTO etudiants (nom, prenom, email, password, matricule, numero_national,
                       telephone, specialite, niveau, role,
                       date_naissance, lieu_naissance, pays, date_inscription)
SELECT 'Ba', 'Oumar', 'oumar.ba@supnum.mr',
       '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
       'SN2026002', '2345678901', '+222 36 12 34 56',
       'Développement Web et Mobile', 'L3', 'USER',
       '2002-05-15', 'Nouakchott', 'Mauritanie', '2023-10-01'
WHERE NOT EXISTS (SELECT 1 FROM etudiants WHERE email = 'oumar.ba@supnum.mr');

INSERT INTO etudiants (nom, prenom, email, password, matricule, numero_national,
                       telephone, specialite, niveau, role,
                       date_naissance, lieu_naissance, pays, date_inscription)
SELECT 'Mint Ahmed', 'Fatimetou', 'fatimetou.ma@supnum.mr',
       '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
       'SN2026003', '3456789012', '+222 46 78 90 12',
       'Réseaux, Systèmes et Sécurité', 'L3', 'USER',
       '2003-01-20', 'Nouadhibou', 'Mauritanie', '2023-10-01'
WHERE NOT EXISTS (SELECT 1 FROM etudiants WHERE email = 'fatimetou.ma@supnum.mr');

INSERT INTO etudiants (nom, prenom, email, password, matricule, numero_national,
                       telephone, specialite, niveau, role,
                       date_naissance, lieu_naissance, pays, date_inscription)
SELECT 'Sy', 'Abdoulaye', 'abdoulaye.sy@supnum.mr',
       '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
       'SN2026004', '4567890123', '+222 22 33 44 55',
       'Développement Web et Mobile', 'L2', 'USER',
       '2004-03-10', 'Kaédi', 'Mauritanie', '2024-10-01'
WHERE NOT EXISTS (SELECT 1 FROM etudiants WHERE email = 'abdoulaye.sy@supnum.mr');

INSERT INTO etudiants (nom, prenom, email, password, matricule, numero_national,
                       telephone, specialite, niveau, role,
                       date_naissance, lieu_naissance, pays, date_inscription)
SELECT 'Ould Mohamed', 'Ahmed', 'ahmed.om@supnum.mr',
       '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
       'SN2026005', '5678901234', '+222 44 55 66 77',
       'Réseaux, Systèmes et Sécurité', 'L2', 'USER',
       '2003-08-25', 'Atar', 'Mauritanie', '2024-10-01'
WHERE NOT EXISTS (SELECT 1 FROM etudiants WHERE email = 'ahmed.om@supnum.mr');

-- ============================================================
-- DONNEES : Notes
-- Formule : (examen * 0.6) + (devoir * 0.4)
-- Rattrapage : max(finale_normale, (rattrapage * 0.6) + (devoir * 0.4))
-- ============================================================

-- Aminata Diallo — Semestre 3
INSERT INTO notes (code, module, matiere, credit, valeur, note_devoir, note_rattrapage, semestre, etudiant_id)
SELECT 'PAV310', 'Programmation Avancée', 'Programmation Orientée Objets Java', 2, 12.5, 14.0, NULL, '3', id
FROM etudiants WHERE email = 'etudiant@supnum.mr'
AND NOT EXISTS (SELECT 1 FROM notes n JOIN etudiants e ON n.etudiant_id = e.id
                WHERE e.email = 'etudiant@supnum.mr' AND n.code = 'PAV310');

INSERT INTO notes (code, module, matiere, credit, valeur, note_devoir, note_rattrapage, semestre, etudiant_id)
SELECT 'PAV311', 'Programmation Avancée', 'Structure de données et Complexité algo.', 2, 10.0, 11.5, NULL, '3', id
FROM etudiants WHERE email = 'etudiant@supnum.mr'
AND NOT EXISTS (SELECT 1 FROM notes n JOIN etudiants e ON n.etudiant_id = e.id
                WHERE e.email = 'etudiant@supnum.mr' AND n.code = 'PAV311');

INSERT INTO notes (code, module, matiere, credit, valeur, note_devoir, note_rattrapage, semestre, etudiant_id)
SELECT 'RSS321', 'Sécurité et Base de données', 'Bases de données et conception des SI', 2, 14.0, 15.0, NULL, '3', id
FROM etudiants WHERE email = 'etudiant@supnum.mr'
AND NOT EXISTS (SELECT 1 FROM notes n JOIN etudiants e ON n.etudiant_id = e.id
                WHERE e.email = 'etudiant@supnum.mr' AND n.code = 'RSS321');

-- Oumar Ba — Semestre 3
INSERT INTO notes (code, module, matiere, credit, valeur, note_devoir, note_rattrapage, semestre, etudiant_id)
SELECT 'PAV310', 'Programmation Avancée', 'Programmation Orientée Objets Java', 2, 8.0, 10.0, 12.0, '3', id
FROM etudiants WHERE email = 'oumar.ba@supnum.mr'
AND NOT EXISTS (SELECT 1 FROM notes n JOIN etudiants e ON n.etudiant_id = e.id
                WHERE e.email = 'oumar.ba@supnum.mr' AND n.code = 'PAV310');

INSERT INTO notes (code, module, matiere, credit, valeur, note_devoir, note_rattrapage, semestre, etudiant_id)
SELECT 'RSS321', 'Sécurité et Base de données', 'Bases de données et conception des SI', 2, 11.0, 13.0, NULL, '3', id
FROM etudiants WHERE email = 'oumar.ba@supnum.mr'
AND NOT EXISTS (SELECT 1 FROM notes n JOIN etudiants e ON n.etudiant_id = e.id
                WHERE e.email = 'oumar.ba@supnum.mr' AND n.code = 'RSS321');