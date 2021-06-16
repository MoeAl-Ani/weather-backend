SHELL := /bin/bash
deploy:
	docker-compose down || docker-compose -f docker-compose-base.yaml down || exit 1
	mvn clean install -T 1C -DskipTests || exit 1
	docker-compose up -d --build
	docker-compose logs -f
deploy_base:
	docker-compose down || docker-compose -f docker-compose-base.yaml down || exit 1
	docker-compose -f docker-compose-base.yaml up -d --build
