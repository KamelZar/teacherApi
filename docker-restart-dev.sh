#!/bin/bash

# =============================================================================
# Script de redémarrage complet TeacherAssistant Docker Development
# =============================================================================

set -e  # Arrêt en cas d'erreur

# Couleurs pour l'affichage
RED='\033[0;31m'
GREEN='\033[0;32m'
BLUE='\033[0;34m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Configuration
COMPOSE_FILE="docker-compose-dev.yml"
POSTGRES_CONTAINER="teacherapi-postgres-dev"
PGADMIN_CONTAINER="teacherapi-pgadmin"

echo -e "${BLUE}======================================${NC}"
echo -e "${BLUE}🚀 TeacherAssistant Docker Restart${NC}"
echo -e "${BLUE}======================================${NC}"
echo

# Vérifier que le fichier docker-compose existe
if [ ! -f "$COMPOSE_FILE" ]; then
    echo -e "${RED}❌ Erreur: $COMPOSE_FILE non trouvé!${NC}"
    exit 1
fi

# Vérifier que les scripts d'init existent
if [ ! -f "src/main/resources/data.sql" ]; then
    echo -e "${RED}❌ Erreur: src/main/resources/data.sql non trouvé!${NC}"
    exit 1
fi

if [ ! -f "docker/postgres/init-dev.sql" ]; then
    echo -e "${RED}❌ Erreur: docker/postgres/init-dev.sql non trouvé!${NC}"
    exit 1
fi

echo -e "${YELLOW}🛑 Arrêt des containers existants...${NC}"
docker-compose -f $COMPOSE_FILE down -v 2>/dev/null || true
echo -e "${GREEN}✅ Containers arrêtés${NC}"
echo

echo -e "${YELLOW}🐳 Démarrage de PostgreSQL...${NC}"
docker-compose -f $COMPOSE_FILE up -d postgres-dev

echo -e "${CYAN}⏳ Attente du démarrage de PostgreSQL...${NC}"
# Attendre que PostgreSQL soit prêt
MAX_ATTEMPTS=30
ATTEMPT=1
while ! docker exec $POSTGRES_CONTAINER pg_isready -U teacher -d teacherdb >/dev/null 2>&1; do
    if [ $ATTEMPT -ge $MAX_ATTEMPTS ]; then
        echo -e "${RED}❌ Timeout: PostgreSQL n'a pas démarré après 30 secondes${NC}"
        exit 1
    fi
    echo -n "."
    sleep 1
    ((ATTEMPT++))
done
echo
echo -e "${GREEN}✅ PostgreSQL est prêt!${NC}"
echo

echo -e "${YELLOW}📊 Vérification des tables créées...${NC}"
TABLE_COUNT=$(docker exec $POSTGRES_CONTAINER psql -U teacher -d teacherdb -t -c "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema='public';" | xargs)
echo -e "${CYAN}Nombre de tables créées: ${TABLE_COUNT}${NC}"

if [ "$TABLE_COUNT" -eq "12" ]; then
    echo -e "${GREEN}✅ Toutes les 12 tables ont été créées avec succès!${NC}"
else
    echo -e "${YELLOW}⚠️  Seulement ${TABLE_COUNT} tables créées (12 attendues)${NC}"
    echo -e "${CYAN}📋 Tables présentes:${NC}"
    docker exec $POSTGRES_CONTAINER psql -U teacher -d teacherdb -c "\dt" | grep -E "^ [a-z]" | awk '{print "  - " $3}'
fi
echo

echo -e "${YELLOW}👤 Vérification des données de test...${NC}"
USER_COUNT=$(docker exec $POSTGRES_CONTAINER psql -U teacher -d teacherdb -t -c "SELECT COUNT(*) FROM users;" 2>/dev/null | xargs || echo "0")
echo -e "${CYAN}Nombre d'utilisateurs: ${USER_COUNT}${NC}"

if [ "$USER_COUNT" -gt "0" ]; then
    echo -e "${GREEN}✅ Données de test chargées avec succès!${NC}"
    echo -e "${CYAN}👨‍🏫 Professeurs:${NC}"
    docker exec $POSTGRES_CONTAINER psql -U teacher -d teacherdb -t -c "SELECT '  - ' || name FROM users WHERE role='TEACHER';" 2>/dev/null || true
    echo -e "${CYAN}👨‍🎓 Élèves:${NC}"
    docker exec $POSTGRES_CONTAINER psql -U teacher -d teacherdb -t -c "SELECT '  - ' || name FROM users WHERE role='STUDENT';" 2>/dev/null || true
else
    echo -e "${YELLOW}⚠️  Aucune donnée de test trouvée${NC}"
fi
echo

# Démarrer pgAdmin si demandé
if [ "$1" = "--with-pgadmin" ] || [ "$1" = "-p" ]; then
    echo -e "${YELLOW}🖥️  Démarrage de pgAdmin...${NC}"
    docker-compose -f $COMPOSE_FILE --profile admin up -d pgadmin
    
    echo -e "${CYAN}⏳ Attente du démarrage de pgAdmin...${NC}"
    sleep 5
    
    PGADMIN_STATUS=$(docker inspect --format='{{.State.Status}}' $PGADMIN_CONTAINER 2>/dev/null || echo "not found")
    if [ "$PGADMIN_STATUS" = "running" ]; then
        echo -e "${GREEN}✅ pgAdmin est prêt!${NC}"
    else
        echo -e "${YELLOW}⚠️  pgAdmin en cours de démarrage...${NC}"
    fi
    echo
fi

echo -e "${GREEN}🎉 Démarrage terminé avec succès!${NC}"
echo
echo -e "${BLUE}======================================${NC}"
echo -e "${BLUE}📋 INFORMATIONS DE CONNEXION${NC}"
echo -e "${BLUE}======================================${NC}"
echo
echo -e "${CYAN}🗄️  PostgreSQL:${NC}"
echo -e "  Host:     localhost"
echo -e "  Port:     5433"
echo -e "  Database: teacherdb"
echo -e "  Username: teacher"
echo -e "  Password: teacherpass123"
echo
echo -e "${CYAN}💻 Ligne de commande PostgreSQL:${NC}"
echo -e "  docker exec -it $POSTGRES_CONTAINER psql -U teacher -d teacherdb"
echo

if [ "$1" = "--with-pgadmin" ] || [ "$1" = "-p" ]; then
    echo -e "${CYAN}🖥️  pgAdmin:${NC}"
    echo -e "  URL:      http://localhost:8080"
    echo -e "  Login:    admin@example.com"
    echo -e "  Password: admin123"
    echo
    echo -e "${CYAN}🔗 Configuration serveur dans pgAdmin:${NC}"
    echo -e "  Name:     TeacherAPI Local"
    echo -e "  Host:     postgres-dev"
    echo -e "  Port:     5432"
    echo -e "  Database: teacherdb"  
    echo -e "  Username: teacher"
    echo -e "  Password: teacherpass123"
    echo
fi

echo -e "${CYAN}🚀 Spring Boot:${NC}"
echo -e "  Profil:   docker"
echo -e "  Commande: ./mvnw spring-boot:run -Dspring-boot.run.profiles=docker"
echo -e "  API URL:  http://localhost:8083/swagger-ui.html"
echo

echo -e "${BLUE}======================================${NC}"
echo -e "${BLUE}🛠️  COMMANDES UTILES${NC}"
echo -e "${BLUE}======================================${NC}"
echo
echo -e "${CYAN}Voir les logs:${NC}"
echo -e "  docker-compose -f $COMPOSE_FILE logs postgres-dev"
echo -e "  docker-compose -f $COMPOSE_FILE logs pgadmin"
echo
echo -e "${CYAN}Arrêter les services:${NC}"
echo -e "  docker-compose -f $COMPOSE_FILE down"
echo
echo -e "${CYAN}Redémarrer ce script:${NC}"
echo -e "  ./docker-restart-dev.sh                    # PostgreSQL seulement"
echo -e "  ./docker-restart-dev.sh --with-pgadmin     # PostgreSQL + pgAdmin"
echo -e "  ./docker-restart-dev.sh -p                 # PostgreSQL + pgAdmin (raccourci)"
echo
echo -e "${GREEN}✨ Prêt à développer! ✨${NC}"