FROM buildpack-deps:jessie-scm

RUN apt-get update && apt-get install -y --no-install-recommends \
		bzip2 \
		unzip \
		xz-utils \
	&& rm -rf /var/lib/apt/lists/*

RUN echo 'deb http://deb.debian.org/debian jessie-backports main' > /etc/apt/sources.list.d/jessie-backports.list

# Default to UTF-8 file.encoding
ENV LANG C.UTF-8

ENV VERSION 8u152
ENV MAJ_VERSION 8
ENV MIN_VERSION 0
ENV PATCH_VERSION 152

ADD build/jdk-${VERSION}-linux-x64.tar.gz /usr/lib/jvm/

ENV JAVA_HOME /usr/lib/jvm/jdk1.${MAJ_VERSION}.${MIN_VERSION}_${PATCH_VERSION}

ENV PATH ${PATH}:${JAVA_HOME}/bin
