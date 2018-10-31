# Liferay Portal Docker Build

### Prerequisites

ECS docker registry configured
ECS CLI installed locally
Docker version 17.09 or newer
Docker-compose/Docker-machine installed

### Download Binary dependencies

```
gradle downloadDeps
```

### Build Oracle JDK base docker image (for changes to base image)

```
docker image build --force-rm -t oracle-jdk-8 -f oracle-jdk-8.Dockerfile .
```

### Build Elasticsearch docker image (for changes to base image)

```
docker image build --force-rm -t elasticsearch-2.4 -f elasticsearch-2.4.Dockerfile .
```

### Build Jenkins docker image (for changes to base image)

```
docker image build --force-rm -t jenkins-lts -f jenkins-lts.Dockerfile .
```

### Build Liferay Portal image

For local developers:
1. Run ```./build.sh dev```
2. Start Docker services (if needed)
3. Run ```docker-compose up``` in the ```docker``` folder

For code deployment (note, this requires ECS CLI access):
1. Run ```./build.sh {ENV}``` in the root project, where {ENV} is your build environment (dev,prod,etc)
1. Check the new liferay base image: ``` docker images ```
1. Tag the new image according to the docker registry and build version  ```docker tag liferay-portal {ECS_REPOSITORY_URL}/liferay-portal:{TAG}``` where {ECS_REPOSITORY_URL} is the url to the ECS Container Repo you have configured and {TAG} is the version tag of the build
1. Run the ECS login(Find this in the ECS Repository Push Instructions).  For example, ```aws ecr --profile liferay-portal get-login --no-include-email --region us-west-2```.  What is returned is what you run to login.
1. Push it to ECS Docker Registry: ```docker push {ECS_REPOSITORY_URL}/liferay/liferay-portal:{TAG}```
1. Repeat Steps 3 and 4 for the elasticsearch image, tagging it as ```/base/elasticsearch-2.4```

To test the image locally, edit the env_variables file that define database and elasticsearch access, then: ```docker run --env-file env_variables liferay-portal```
