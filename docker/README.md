# 🐳 Docker PostgreSQL pour TeacherAssistant

## 🚀 Démarrage rapide

### 1. Lancer PostgreSQL
```bash
# Démarrer uniquement PostgreSQL
docker-compose -f docker-compose-dev.yml up -d postgres-dev

# Ou démarrer avec pgAdmin (interface graphique)
docker-compose -f docker-compose-dev.yml --profile admin up -d
```

### 2. Lancer l'application Spring Boot
```bash
# Profile Docker (utilise PostgreSQL sur port 5433)
./mvnw spring-boot:run -Dspring-boot.run.profiles=docker
```

### 3. Vérifier que tout fonctionne
- **API** : http://localhost:8083/swagger-ui.html
- **pgAdmin** : http://localhost:5050 (admin@teacher.local / admin123)

## 📊 Informations de connexion

### PostgreSQL
- **Host** : localhost
- **Port** : 5433 (pour éviter conflit avec PostgreSQL local)
- **Database** : teacherdb
- **User** : teacher  
- **Password** : teacherpass123

### pgAdmin (optionnel)
- **URL** : http://localhost:5050
- **Email** : admin@teacher.local
- **Password** : admin123

## 🗄️ Données de test incluses

La base est initialisée avec :
- **3 professeurs** : Martin, Claire, Jean
- **5 élèves** : Alice, Bob, Claire, David, Emma  
- **3 classes** : 5ème A, 5ème B, 6ème A
- **Matières** : Maths, Français, Histoire-Géo, Sciences, Anglais, EPS, Musique
- **Évaluations et notes** complètes pour tester les calculs

## 🛠️ Commandes utiles

```bash
# Voir les logs PostgreSQL
docker-compose -f docker-compose-dev.yml logs postgres-dev

# Se connecter à PostgreSQL en ligne de commande
docker exec -it teacherapi-postgres-dev psql -U teacher -d teacherdb

# Arrêter les services
docker-compose -f docker-compose-dev.yml down

# Arrêter et supprimer les volumes (⚠️ perte de données)
docker-compose -f docker-compose-dev.yml down -v

# Redémarrer avec données fraîches
docker-compose -f docker-compose-dev.yml down -v && docker-compose -f docker-compose-dev.yml up -d postgres-dev
```

## 🔍 Requêtes de test

Quelques requêtes SQL pour tester :

```sql
-- Voir toutes les notes d'Alice Martin
SELECT u.name, e.name as evaluation, se.points, e.max_points 
FROM student_evaluations se
JOIN users u ON se.student_id = u.id  
JOIN evaluations e ON se.evaluation_id = e.id
WHERE u.name = 'Alice Martin';

-- Calculer la moyenne de mathématiques pour Alice
SELECT 
  u.name,
  s.name as matiere,
  ROUND(
    (SUM(se.points) / SUM(e.max_points)) * s.max_grade, 2
  ) as moyenne
FROM student_evaluations se
JOIN users u ON se.student_id = u.id
JOIN evaluations e ON se.evaluation_id = e.id  
JOIN topics t ON e.topic_id = t.id
JOIN subjects s ON t.subject_id = s.id
WHERE u.name = 'Alice Martin' AND s.name = 'Mathématiques'
GROUP BY u.name, s.name, s.max_grade;

-- Voir les classes et leurs professeurs principaux
SELECT c.name, u.name as professeur_principal
FROM classes c
JOIN class_teachers ct ON c.id = ct.class_id
JOIN users u ON ct.teacher_id = u.id
WHERE ct.is_main_teacher = true;
```

## 🎯 Avantages de cette configuration

- ✅ **Même technologie** que la production (PostgreSQL)
- ✅ **Scripts SQL réels** testés  
- ✅ **Données de test** riches
- ✅ **Triggers et contraintes** fonctionnels
- ✅ **Interface graphique** avec pgAdmin
- ✅ **Performance** réelle
- ✅ **Isolation** : pas de conflit avec autres DB