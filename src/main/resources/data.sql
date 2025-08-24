-- =============================================================================
-- TeacherAssistant - Script d'initialisation de la base de données
-- =============================================================================

-- -----------------------------------------------------------------------------
-- 1. FONCTION DE MISE À JOUR AUTOMATIQUE DES TIMESTAMPS
-- -----------------------------------------------------------------------------

-- Fonction pour mettre à jour automatiquement updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- -----------------------------------------------------------------------------
-- 2. CRÉATION DES TABLES AVEC CONTRAINTES ET TYPES PRÉCIS
-- -----------------------------------------------------------------------------

-- Table users
CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    google_id VARCHAR(255) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    picture_url TEXT,
    role VARCHAR(20) NOT NULL CHECK (role IN ('TEACHER', 'STUDENT')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Table classes
CREATE TABLE IF NOT EXISTS classes (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    school_year INTEGER NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(name, school_year)
);

-- Table class_teachers (relation N-N)
CREATE TABLE IF NOT EXISTS class_teachers (
    id BIGSERIAL PRIMARY KEY,
    class_id BIGINT NOT NULL REFERENCES classes(id) ON DELETE CASCADE,
    teacher_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    is_main_teacher BOOLEAN NOT NULL DEFAULT false,
    assigned_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(class_id, teacher_id)
);

-- Table class_students (relation N-N)
CREATE TABLE IF NOT EXISTS class_students (
    id BIGSERIAL PRIMARY KEY,
    class_id BIGINT NOT NULL REFERENCES classes(id) ON DELETE CASCADE,
    student_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    enrolled_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(class_id, student_id)
);

-- Table subjects
CREATE TABLE IF NOT EXISTS subjects (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    max_grade DECIMAL(5,2) NOT NULL DEFAULT 20.00,
    description TEXT,
    class_id BIGINT NOT NULL REFERENCES classes(id) ON DELETE CASCADE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Table periods
CREATE TABLE IF NOT EXISTS periods (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    class_id BIGINT NOT NULL REFERENCES classes(id) ON DELETE CASCADE,
    start_date DATE,
    end_date DATE,
    is_active BOOLEAN NOT NULL DEFAULT false,
    is_closed BOOLEAN NOT NULL DEFAULT false,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Table topics
CREATE TABLE IF NOT EXISTS topics (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    subject_id BIGINT NOT NULL REFERENCES subjects(id) ON DELETE CASCADE,
    period_id BIGINT NOT NULL REFERENCES periods(id) ON DELETE CASCADE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Table evaluations
CREATE TABLE IF NOT EXISTS evaluations (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    max_points DECIMAL(5,2) NOT NULL,
    description TEXT,
    topic_id BIGINT NOT NULL REFERENCES topics(id) ON DELETE CASCADE,
    evaluation_date DATE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Table student_evaluations
CREATE TABLE IF NOT EXISTS student_evaluations (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    evaluation_id BIGINT NOT NULL REFERENCES evaluations(id) ON DELETE CASCADE,
    points DECIMAL(5,2) NOT NULL,
    photo_path TEXT,
    comments TEXT,
    submitted_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(student_id, evaluation_id)
);

-- Table remarks
CREATE TABLE IF NOT EXISTS remarks (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    period_id BIGINT NOT NULL REFERENCES periods(id) ON DELETE CASCADE,
    type VARCHAR(20) NOT NULL CHECK (type IN ('BEHAVIOR', 'PROGRESS', 'EFFORT', 'PARTICIPATION', 'HOMEWORK', 'ATTENDANCE', 'GENERAL')),
    content TEXT NOT NULL,
    teacher_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Table bulletins
CREATE TABLE IF NOT EXISTS bulletins (
    id BIGSERIAL PRIMARY KEY,
    student_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    period_id BIGINT NOT NULL REFERENCES periods(id) ON DELETE CASCADE,
    teacher_final_comment TEXT,
    year_end_comment TEXT,
    is_finalized BOOLEAN NOT NULL DEFAULT false,
    finalized_at TIMESTAMP,
    finalized_by_teacher_id BIGINT REFERENCES users(id),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(student_id, period_id)
);

-- Table bulletin_subject_grades
CREATE TABLE IF NOT EXISTS bulletin_subject_grades (
    id BIGSERIAL PRIMARY KEY,
    bulletin_id BIGINT NOT NULL REFERENCES bulletins(id) ON DELETE CASCADE,
    subject_id BIGINT NOT NULL REFERENCES subjects(id) ON DELETE CASCADE,
    final_grade DECIMAL(5,2),
    subject_comment TEXT,
    calculated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(bulletin_id, subject_id)
);

-- -----------------------------------------------------------------------------
-- 3. TRIGGERS POUR MISE À JOUR AUTOMATIQUE DES TIMESTAMPS
-- -----------------------------------------------------------------------------

-- Trigger pour users
DROP TRIGGER IF EXISTS update_users_updated_at ON users;
CREATE TRIGGER update_users_updated_at 
    BEFORE UPDATE ON users 
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column();

-- Trigger pour classes
DROP TRIGGER IF EXISTS update_classes_updated_at ON classes;
CREATE TRIGGER update_classes_updated_at 
    BEFORE UPDATE ON classes 
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column();

-- Trigger pour subjects
DROP TRIGGER IF EXISTS update_subjects_updated_at ON subjects;
CREATE TRIGGER update_subjects_updated_at 
    BEFORE UPDATE ON subjects 
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column();

-- Trigger pour periods
DROP TRIGGER IF EXISTS update_periods_updated_at ON periods;
CREATE TRIGGER update_periods_updated_at 
    BEFORE UPDATE ON periods 
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column();

-- Trigger pour topics
DROP TRIGGER IF EXISTS update_topics_updated_at ON topics;
CREATE TRIGGER update_topics_updated_at 
    BEFORE UPDATE ON topics 
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column();

-- Trigger pour evaluations
DROP TRIGGER IF EXISTS update_evaluations_updated_at ON evaluations;
CREATE TRIGGER update_evaluations_updated_at 
    BEFORE UPDATE ON evaluations 
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column();

-- Trigger pour student_evaluations
DROP TRIGGER IF EXISTS update_student_evaluations_updated_at ON student_evaluations;
CREATE TRIGGER update_student_evaluations_updated_at 
    BEFORE UPDATE ON student_evaluations 
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column();

-- -----------------------------------------------------------------------------
-- 4. INDEX POUR PERFORMANCE
-- -----------------------------------------------------------------------------

-- Index sur les clés étrangères fréquemment utilisées
CREATE INDEX IF NOT EXISTS idx_class_teachers_class_id ON class_teachers(class_id);
CREATE INDEX IF NOT EXISTS idx_class_teachers_teacher_id ON class_teachers(teacher_id);
CREATE INDEX IF NOT EXISTS idx_class_students_class_id ON class_students(class_id);
CREATE INDEX IF NOT EXISTS idx_class_students_student_id ON class_students(student_id);
CREATE INDEX IF NOT EXISTS idx_subjects_class_id ON subjects(class_id);
CREATE INDEX IF NOT EXISTS idx_periods_class_id ON periods(class_id);
CREATE INDEX IF NOT EXISTS idx_topics_subject_id ON topics(subject_id);
CREATE INDEX IF NOT EXISTS idx_topics_period_id ON topics(period_id);
CREATE INDEX IF NOT EXISTS idx_evaluations_topic_id ON evaluations(topic_id);
CREATE INDEX IF NOT EXISTS idx_student_evaluations_student_id ON student_evaluations(student_id);
CREATE INDEX IF NOT EXISTS idx_student_evaluations_evaluation_id ON student_evaluations(evaluation_id);
CREATE INDEX IF NOT EXISTS idx_remarks_student_id ON remarks(student_id);
CREATE INDEX IF NOT EXISTS idx_remarks_period_id ON remarks(period_id);
CREATE INDEX IF NOT EXISTS idx_bulletins_student_id ON bulletins(student_id);
CREATE INDEX IF NOT EXISTS idx_bulletins_period_id ON bulletins(period_id);

-- Index pour les requêtes de calcul de moyennes
CREATE INDEX IF NOT EXISTS idx_student_evaluations_student_period ON student_evaluations(student_id, evaluation_id);

-- -----------------------------------------------------------------------------
-- 6. CONTRAINTES SPÉCIALES
-- -----------------------------------------------------------------------------

-- Contrainte : une seule période active par classe
CREATE UNIQUE INDEX IF NOT EXISTS idx_periods_unique_active 
ON periods(class_id) WHERE is_active = true;

-- -----------------------------------------------------------------------------
-- 7. COMMENTAIRES SUR LES TABLES (DOCUMENTATION)
-- -----------------------------------------------------------------------------

COMMENT ON TABLE users IS 'Utilisateurs du système (professeurs et élèves)';
COMMENT ON TABLE classes IS 'Classes scolaires';
COMMENT ON TABLE class_teachers IS 'Relation N-N entre classes et professeurs';
COMMENT ON TABLE class_students IS 'Relation N-N entre classes et élèves';
COMMENT ON TABLE subjects IS 'Matières enseignées dans chaque classe';
COMMENT ON TABLE periods IS 'Périodes scolaires (trimestres, semestres)';
COMMENT ON TABLE topics IS 'Sujets/chapitres par matière et période';
COMMENT ON TABLE evaluations IS 'Évaluations/contrôles';
COMMENT ON TABLE student_evaluations IS 'Notes et résultats des élèves';
COMMENT ON TABLE remarks IS 'Remarques sur les élèves';
COMMENT ON TABLE bulletins IS 'Bulletins scolaires';
COMMENT ON TABLE bulletin_subject_grades IS 'Notes finales par matière dans les bulletins';

COMMENT ON COLUMN users.google_id IS 'ID unique Google pour authentification OAuth';
COMMENT ON COLUMN users.role IS 'Rôle: TEACHER ou STUDENT';
COMMENT ON COLUMN periods.is_active IS 'Une seule période peut être active par classe';
COMMENT ON COLUMN periods.is_closed IS 'Période fermée = plus de modifications possibles';
COMMENT ON COLUMN student_evaluations.points IS 'Note obtenue par l''élève';
COMMENT ON COLUMN student_evaluations.photo_path IS 'Chemin vers la photo de la copie (1 max par évaluation/élève)';
COMMENT ON COLUMN bulletins.is_finalized IS 'Bulletin finalisé = irréversible';