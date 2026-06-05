-- ============================================================
-- Fichier d'initialisation de la base de données
-- Portail Etudiant - SupNum
-- ============================================================

-- ============================================================
-- Table: etudiants
-- ============================================================
-- L'admin et l'étudiant démo sont créés automatiquement
-- par le CommandLineRunner dans EtudiantApplication.java
-- (admin@supnum.mr / admin123) et (etudiant@supnum.mr / 1234567890)

-- ============================================================
-- Données de démonstration: Étudiants supplémentaires
-- ============================================================
-- Note: Les mots de passe sont le numéro national encodé en BCrypt
-- Ces insertions ne seront exécutées que si les emails n'existent pas déjà

INSERT INTO etudiants (nom, prenom, email, password, matricule, numero_national, telephone, specialite, niveau, role, date_naissance, lieu_naissance, pays, date_inscription)
SELECT 'Ba', 'Oumar', 'oumar.ba@supnum.mr',
       '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
       'SN2026002', '2345678901', '+222 36 12 34 56',
       'Développement Web et Mobile', 'L3', 'USER',
       '2002-05-15', 'Nouakchott', 'Mauritanie', '2023-10-01'
WHERE NOT EXISTS (SELECT 1 FROM etudiants WHERE email = 'oumar.ba@supnum.mr');

INSERT INTO etudiants (nom, prenom, email, password, matricule, numero_national, telephone, specialite, niveau, role, date_naissance, lieu_naissance, pays, date_inscription)
SELECT 'Mint Ahmed', 'Fatimetou', 'fatimetou.ma@supnum.mr',
       '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
       'SN2026003', '3456789012', '+222 46 78 90 12',
       'Réseaux, Systèmes et Sécurité', 'L3', 'USER',
       '2003-01-20', 'Nouadhibou', 'Mauritanie', '2023-10-01'
WHERE NOT EXISTS (SELECT 1 FROM etudiants WHERE email = 'fatimetou.ma@supnum.mr');

INSERT INTO etudiants (nom, prenom, email, password, matricule, numero_national, telephone, specialite, niveau, role, date_naissance, lieu_naissance, pays, date_inscription)
SELECT 'Sy', 'Abdoulaye', 'abdoulaye.sy@supnum.mr',
       '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
       'SN2026004', '4567890123', '+222 22 33 44 55',
       'Développement Web et Mobile', 'L2', 'USER',
       '2004-03-10', 'Kaédi', 'Mauritanie', '2024-10-01'
WHERE NOT EXISTS (SELECT 1 FROM etudiants WHERE email = 'abdoulaye.sy@supnum.mr');

INSERT INTO etudiants (nom, prenom, email, password, matricule, numero_national, telephone, specialite, niveau, role, date_naissance, lieu_naissance, pays, date_inscription)
SELECT 'Ould Mohamed', 'Ahmed', 'ahmed.om@supnum.mr',
       '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy',
       'SN2026005', '5678901234', '+222 44 55 66 77',
       'Réseaux, Systèmes et Sécurité', 'L2', 'USER',
       '2003-08-25', 'Atar', 'Mauritanie', '2024-10-01'
WHERE NOT EXISTS (SELECT 1 FROM etudiants WHERE email = 'ahmed.om@supnum.mr');

-- ============================================================
-- Données de démonstration: Notes pour les étudiants
-- ============================================================

-- Notes pour Aminata Diallo (etudiant@supnum.mr) - Semestre 3
INSERT INTO notes (code, module, matiere, credit, valeur, note_devoir, note_rattrapage, semestre, etudiant_id)
SELECT 'PAV310', 'Programmation Avancée', 'Programmation Orientée Objets Java', 2, 12.5, 14.0, NULL, '3', id
FROM etudiants WHERE email = 'etudiant@supnum.mr'
AND NOT EXISTS (
    SELECT 1 FROM notes n JOIN etudiants e ON n.etudiant_id = e.id 
    WHERE e.email = 'etudiant@supnum.mr' AND n.code = 'PAV310'
);

INSERT INTO notes (code, module, matiere, credit, valeur, note_devoir, note_rattrapage, semestre, etudiant_id)
SELECT 'PAV311', 'Programmation Avancée', 'Structure de données et Complexité algo.', 2, 10.0, 11.5, NULL, '3', id
FROM etudiants WHERE email = 'etudiant@supnum.mr'
AND NOT EXISTS (
    SELECT 1 FROM notes n JOIN etudiants e ON n.etudiant_id = e.id 
    WHERE e.email = 'etudiant@supnum.mr' AND n.code = 'PAV311'
);

INSERT INTO notes (code, module, matiere, credit, valeur, note_devoir, note_rattrapage, semestre, etudiant_id)
SELECT 'RSS321', 'Sécurité et Base de données', 'Bases de données et conception des SI', 2, 14.0, 15.0, NULL, '3', id
FROM etudiants WHERE email = 'etudiant@supnum.mr'
AND NOT EXISTS (
    SELECT 1 FROM notes n JOIN etudiants e ON n.etudiant_id = e.id 
    WHERE e.email = 'etudiant@supnum.mr' AND n.code = 'RSS321'
);

-- Notes pour Oumar Ba (oumar.ba@supnum.mr) - Semestre 3
INSERT INTO notes (code, module, matiere, credit, valeur, note_devoir, note_rattrapage, semestre, etudiant_id)
SELECT 'PAV310', 'Programmation Avancée', 'Programmation Orientée Objets Java', 2, 8.0, 10.0, 12.0, '3', id
FROM etudiants WHERE email = 'oumar.ba@supnum.mr'
AND NOT EXISTS (
    SELECT 1 FROM notes n JOIN etudiants e ON n.etudiant_id = e.id 
    WHERE e.email = 'oumar.ba@supnum.mr' AND n.code = 'PAV310'
);

INSERT INTO notes (code, module, matiere, credit, valeur, note_devoir, note_rattrapage, semestre, etudiant_id)
SELECT 'RSS321', 'Sécurité et Base de données', 'Bases de données et conception des SI', 2, 11.0, 13.0, NULL, '3', id
FROM etudiants WHERE email = 'oumar.ba@supnum.mr'
AND NOT EXISTS (
    SELECT 1 FROM notes n JOIN etudiants e ON n.etudiant_id = e.id 
    WHERE e.email = 'oumar.ba@supnum.mr' AND n.code = 'RSS321'
);
