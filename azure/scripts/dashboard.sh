#!/bin/bash

set -e

cd $(dirname $(readlink -f $0))

if ! nc -z 127.0.0.1 8001; then
    source kube-context.sh
    kubectl proxy &
fi

xdg-open http://127.0.0.1:8001/api/v1/namespaces/kube-system/services/http:kubernetes-dashboard:/proxy/\#\!/overview\?namespace\=default
