#!/bin/bash
set -e

META_DIR=$(dirname $0)
if [ ! -f "$META_DIR/config.sh" ]; then echo "could not find config.sh"; exit 1; fi
source $META_DIR/config.sh

if [ ! -f "$META_PKG_LIST" ]; then echo "package list not found!"; exit 1; fi
PACKAGES=$(sed -e 's/#.*$//' $META_PKG_LIST)

bitbake -k $PACKAGES $*
