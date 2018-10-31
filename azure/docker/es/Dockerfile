FROM docker.elastic.co/elasticsearch/elasticsearch-oss:6.1.4

RUN /usr/share/elasticsearch/bin/elasticsearch-plugin install analysis-icu --batch
RUN /usr/share/elasticsearch/bin/elasticsearch-plugin install analysis-kuromoji --batch
RUN /usr/share/elasticsearch/bin/elasticsearch-plugin install analysis-smartcn --batch
RUN /usr/share/elasticsearch/bin/elasticsearch-plugin install analysis-stempel --batch
