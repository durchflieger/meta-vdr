#!/bin/bash
set -e

META_VDR_DIR=$(dirname $0)
if [ ! -f "$META_VDR_DIR/config.sh" ]; then echo "could not find config.sh"; exit 1; fi
source $META_VDR_DIR/config.sh

if [ ! -f "$META_VDR_PKG_LIST" ]; then echo "package list not found!"; exit 1; fi
PACKAGES=$(sed -e 's/#.*$//' $META_VDR_PKG_LIST)

bitbake $PACKAGES $*
