FROM oracle-jdk-8:latest

RUN groupadd -g 1000 elasticsearch && useradd elasticsearch -u 1000 -g 1000

RUN apt-key adv --keyserver hkp://pgp.mit.edu:80 --recv-keys 46095ACC8548582C1A2699A9D27D666CD88E42B4
RUN echo "deb http://packages.elastic.co/elasticsearch/2.x/debian stable main" > /etc/apt/sources.list.d/elastic-2x.list
RUN apt-get update && \
    apt-get install -y --no-install-recommends elasticsearch=2.4.5 \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /usr/share/elasticsearch

RUN set -ex && for path in data logs config config/scripts; do \
        mkdir -p "$path"; \
        chown -R elasticsearch:elasticsearch "$path"; \
    done

# Install Elasticsearch plug-ins
RUN bin/plugin install analysis-icu --batch
RUN bin/plugin install analysis-kuromoji --batch
RUN bin/plugin install analysis-smartcn --batch
RUN bin/plugin install analysis-stempel --batch

RUN bin/plugin install cloud-aws
RUN bin/plugin install mobz/elasticsearch-head
RUN bin/plugin install analysis-phonetic

## RUN /usr/share/elasticsearch/bin/plugin install io.fabric8/elasticsearch-cloud-kubernetes/2.4.5_01 --batch

COPY es-logging.yml config/logging.yml
COPY elasticsearch.yml config/elasticsearch.yml
COPY elasticsearch-entrypoint.sh /docker-entrypoint.sh

USER elasticsearch

# Set environment
ENV NAMESPACE default
ENV ES_HEAP_SIZE 512m
ENV CLUSTER_NAME elasticsearch-default
ENV NODE_MASTER true
ENV NODE_DATA true
ENV HTTP_ENABLE true
ENV NETWORK_HOST _site_
ENV HTTP_CORS_ENABLE true
ENV HTTP_CORS_ALLOW_ORIGIN *
ENV NUMBER_OF_MASTERS 1
ENV NUMBER_OF_SHARDS 1
ENV NUMBER_OF_REPLICAS 0
ENV DISCOVERY_SERVICE elasticsearch-discovery
ENV PATH=$PATH:/usr/share/elasticsearch/bin

CMD ["elasticsearch"]

EXPOSE 9200 9300
