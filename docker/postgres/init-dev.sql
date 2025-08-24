-- =============================================================================
-- TeacherAssistant - Données de test pour développement
-- =============================================================================

-- -----------------------------------------------------------------------------
-- 1. DONNÉES DE TEST - UTILISATEURS
-- -----------------------------------------------------------------------------

-- Professeurs de test
INSERT INTO users (google_id, email, name, picture_url, role) VALUES 
('teacher1_google_id', 'prof.martin@school.com', 'Martin Dubois', 'https://example.com/avatar1.jpg', 'TEACHER'),
('teacher2_google_id', 'prof.claire@school.com', 'Claire Rousseau', 'https://example.com/avatar2.jpg', 'TEACHER'),
('teacher3_google_id', 'prof.jean@school.com', 'Jean Leclerc', 'https://example.com/avatar3.jpg', 'TEACHER');

-- Élèves de test
INSERT INTO users (google_id, email, name, picture_url, role) VALUES 
('student1_google_id', 'alice.martin@student.com', 'Alice Martin', 'https://example.com/student1.jpg', 'STUDENT'),
('student2_google_id', 'bob.durand@student.com', 'Bob Durand', 'https://example.com/student2.jpg', 'STUDENT'),
('student3_google_id', 'claire.bernard@student.com', 'Claire Bernard', 'https://example.com/student3.jpg', 'STUDENT'),
('student4_google_id', 'david.petit@student.com', 'David Petit', 'https://example.com/student4.jpg', 'STUDENT'),
('student5_google_id', 'emma.moreau@student.com', 'Emma Moreau', 'https://example.com/student5.jpg', 'STUDENT');

-- -----------------------------------------------------------------------------
-- 2. DONNÉES DE TEST - CLASSES
-- -----------------------------------------------------------------------------

INSERT INTO classes (name, description, school_year) VALUES 
('5ème A', 'Classe de cinquième A - Section générale', 2024),
('5ème B', 'Classe de cinquième B - Section sportive', 2024),
('6ème A', 'Classe de sixième A - Section musicale', 2024);

-- -----------------------------------------------------------------------------
-- 3. DONNÉES DE TEST - RELATIONS PROFESSEURS/CLASSES
-- -----------------------------------------------------------------------------

-- Martin Dubois : professeur principal 5ème A + intervenant 5ème B
INSERT INTO class_teachers (class_id, teacher_id, is_main_teacher) VALUES 
(1, 1, true),   -- Martin = prof principal 5ème A
(2, 1, false),  -- Martin = intervenant 5ème B
(3, 2, true);   -- Claire = prof principal 6ème A

-- Jean Leclerc : intervenant dans toutes les classes
INSERT INTO class_teachers (class_id, teacher_id, is_main_teacher) VALUES 
(1, 3, false),  -- Jean = intervenant 5ème A
(2, 3, false),  -- Jean = intervenant 5ème B
(3, 3, false);  -- Jean = intervenant 6ème A

-- -----------------------------------------------------------------------------
-- 4. DONNÉES DE TEST - ÉLÈVES DANS LES CLASSES
-- -----------------------------------------------------------------------------

-- Répartition des élèves
INSERT INTO class_students (class_id, student_id) VALUES 
-- 5ème A
(1, 4), -- Alice Martin
(1, 5), -- Bob Durand
(1, 6), -- Claire Bernard
-- 5ème B  
(2, 7), -- David Petit
(2, 8), -- Emma Moreau
-- 6ème A (Alice en redoublement)
(3, 4); -- Alice Martin (redoublante)

-- -----------------------------------------------------------------------------
-- 5. DONNÉES DE TEST - MATIÈRES
-- -----------------------------------------------------------------------------

-- Matières pour 5ème A
INSERT INTO subjects (name, max_grade, description, class_id) VALUES 
('Mathématiques', 20.00, 'Algèbre et géométrie niveau 5ème', 1),
('Français', 20.00, 'Littérature et expression écrite', 1),
('Histoire-Géographie', 20.00, 'Moyen Âge et géographie européenne', 1),
('Sciences', 20.00, 'Physique-Chimie et SVT', 1),
('Anglais', 20.00, 'Langue vivante 1', 1);

-- Matières pour 5ème B  
INSERT INTO subjects (name, max_grade, description, class_id) VALUES 
('Mathématiques', 20.00, 'Algèbre et géométrie niveau 5ème', 2),
('EPS', 20.00, 'Éducation Physique et Sportive - Section sport', 2),
('Français', 20.00, 'Littérature et expression écrite', 2);

-- Matières pour 6ème A
INSERT INTO subjects (name, max_grade, description, class_id) VALUES 
('Mathématiques', 20.00, 'Arithmétique et géométrie niveau 6ème', 3),
('Français', 20.00, 'Grammaire et vocabulaire', 3),
('Musique', 20.00, 'Solfège et pratique instrumentale', 3);

-- -----------------------------------------------------------------------------
-- 6. DONNÉES DE TEST - PÉRIODES
-- -----------------------------------------------------------------------------

-- Trimestre 1 actif pour 5ème A
INSERT INTO periods (name, class_id, start_date, end_date, is_active, is_closed) VALUES 
('Trimestre 1', 1, '2024-09-01', '2024-12-20', true, false),
('Trimestre 2', 1, '2025-01-06', '2025-03-28', false, false),
('Trimestre 3', 1, '2025-04-14', '2025-07-04', false, false);

-- Trimestre 1 pour 5ème B
INSERT INTO periods (name, class_id, start_date, end_date, is_active, is_closed) VALUES 
('Trimestre 1', 2, '2024-09-01', '2024-12-20', true, false);

-- Trimestre 1 pour 6ème A  
INSERT INTO periods (name, class_id, start_date, end_date, is_active, is_closed) VALUES 
('Trimestre 1', 3, '2024-09-01', '2024-12-20', true, false);

-- -----------------------------------------------------------------------------
-- 7. DONNÉES DE TEST - SUJETS
-- -----------------------------------------------------------------------------

-- Sujets Mathématiques 5ème A
INSERT INTO topics (name, description, subject_id, period_id) VALUES 
('Fractions', 'Addition et soustraction des fractions', 1, 1),
('Géométrie plane', 'Triangles et parallélogrammes', 1, 1),
('Nombres relatifs', 'Introduction aux nombres négatifs', 1, 1);

-- Sujets Français 5ème A
INSERT INTO topics (name, description, subject_id, period_id) VALUES 
('Récits d''aventure', 'Étude de romans d''aventure', 2, 1),
('Grammaire', 'Classes grammaticales et fonctions', 2, 1);

-- -----------------------------------------------------------------------------
-- 8. DONNÉES DE TEST - ÉVALUATIONS
-- -----------------------------------------------------------------------------

-- Évaluations Mathématiques
INSERT INTO evaluations (name, max_points, description, topic_id, evaluation_date) VALUES 
('Contrôle Fractions', 15.00, 'Addition et soustraction de fractions', 1, '2024-10-15'),
('Interrogation Géométrie', 10.00, 'Propriétés des triangles', 2, '2024-11-05'),
('Devoir Nombres relatifs', 20.00, 'Calculs avec nombres relatifs', 3, '2024-11-20');

-- Évaluations Français
INSERT INTO evaluations (name, max_points, description, topic_id, evaluation_date) VALUES 
('Contrôle de lecture', 12.00, 'Questions sur le roman étudié', 4, '2024-10-22'),
('Dictée préparée', 8.00, 'Dictée sur les accords', 5, '2024-11-12');

-- -----------------------------------------------------------------------------
-- 9. DONNÉES DE TEST - NOTES DES ÉLÈVES
-- -----------------------------------------------------------------------------

-- Notes Alice Martin (student_id = 4)
INSERT INTO student_evaluations (student_id, evaluation_id, points, comments, submitted_at) VALUES 
(4, 1, 12.50, 'Bonne maîtrise des fractions simples', '2024-10-15 14:30:00'),
(4, 2, 7.00, 'Quelques erreurs sur les propriétés', '2024-11-05 15:15:00'),
(4, 3, 16.00, 'Excellent travail sur les relatifs', '2024-11-20 16:00:00'),
(4, 4, 10.00, 'Lecture attentive du roman', '2024-10-22 14:45:00'),
(4, 5, 6.50, 'Attention aux accords participe passé', '2024-11-12 15:30:00');

-- Notes Bob Durand (student_id = 5)  
INSERT INTO student_evaluations (student_id, evaluation_id, points, comments, submitted_at) VALUES 
(5, 1, 8.00, 'Difficultés avec les fractions complexes', '2024-10-15 14:30:00'),
(5, 2, 9.00, 'Bonne compréhension géométrique', '2024-11-05 15:15:00'),
(5, 3, 11.00, 'Progrès en calcul mental', '2024-11-20 16:00:00'),
(5, 4, 8.50, 'Lecture correcte mais manque d''analyse', '2024-10-22 14:45:00'),
(5, 5, 5.00, 'Revoir les règles d''orthographe', '2024-11-12 15:30:00');

-- Notes Claire Bernard (student_id = 6)
INSERT INTO student_evaluations (student_id, evaluation_id, points, comments, submitted_at) VALUES 
(6, 1, 14.00, 'Très bonne maîtrise technique', '2024-10-15 14:30:00'),
(6, 2, 8.50, 'Raisonnement géométrique correct', '2024-11-05 15:15:00'),
(6, 3, 18.50, 'Excellente logique mathématique', '2024-11-20 16:00:00'),
(6, 4, 11.50, 'Analyse fine du texte', '2024-10-22 14:45:00'),
(6, 5, 7.00, 'Orthographe à améliorer', '2024-11-12 15:30:00');

-- -----------------------------------------------------------------------------
-- 10. DONNÉES DE TEST - REMARQUES
-- -----------------------------------------------------------------------------

INSERT INTO remarks (student_id, period_id, type, content, teacher_id) VALUES 
(4, 1, 'BEHAVIOR', 'Élève très participative et attentive en cours', 1),
(4, 1, 'PROGRESS', 'Progrès notable en mathématiques ce trimestre', 1),
(5, 1, 'EFFORT', 'Doit fournir plus d''efforts dans les apprentissages', 1),
(5, 1, 'HOMEWORK', 'Devoirs parfois non rendus', 1),
(6, 1, 'PARTICIPATION', 'Excellente participation orale', 1),
(6, 1, 'PROGRESS', 'Niveau très satisfaisant dans toutes les matières', 1);

-- -----------------------------------------------------------------------------
-- 11. DONNÉES DE TEST - BULLETINS ET NOTES FINALES
-- -----------------------------------------------------------------------------

-- Bulletins trimestre 1 (non finalisés)
INSERT INTO bulletins (student_id, period_id, teacher_final_comment, is_finalized) VALUES 
(4, 1, 'Trimestre encourageant. Continue tes efforts !', false),
(5, 1, 'Des progrès à faire mais bon potentiel.', false),
(6, 1, 'Excellent trimestre, félicitations !', false);

-- Notes finales par matière (calculées automatiquement)
INSERT INTO bulletin_subject_grades (bulletin_id, subject_id, final_grade, subject_comment) VALUES 
-- Alice Martin
(1, 1, 15.17, 'Très bonne élève en mathématiques'),  -- (12.5+7+16)/3 * 20/moyenne_max
(1, 2, 16.25, 'Lecture attentive, expression à améliorer'), -- (10+6.5)/2 * 20/moyenne_max
-- Bob Durand  
(2, 1, 11.33, 'Doit consolider les bases'),
(2, 2, 13.50, 'Lecture correcte mais superficielle'),
-- Claire Bernard
(3, 1, 16.33, 'Excellente logique mathématique'), 
(3, 2, 18.50, 'Très bon niveau littéraire');