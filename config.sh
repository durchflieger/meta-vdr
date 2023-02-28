# ---
# OE build system specific options
# ---
if [ ! -e env.source ]; then echo "not in oe build directory!"; exit 1; fi
if [ -z "$MACHINE" ]; then source env.source; fi

# Root directory of opkg utils
export OPKG_UTILS_DIR=${OPKG_UTILS_DIR:-~/opkg-utils}

# OE linux version
export OE_VERSION=${OE_VERSION:-$(git branch --show-current)}

# ---
# meta build system specific options
# ---

# Path to package build list
export META_PKG_LIST=${VDR_META_PKG_LIST:-${META_DIR}/package_list}

# ---
# opkg repository deployment specific options
# ---

# Name of opkg repository
export REPO_NAME=${VDR_REPO_NAME:-"vdr-oe-${OE_VERSION}-repo"}

# Root directory of local opkg repository
export REPO_DIR=${VDR_REPO_DIR:-~/$REPO_NAME}

# Name of git repository owner
export GIT_REPO_OWNER=${VDR_GIT_REPO_OWNER:-"durchflieger"}

# URL for accessing project on github
export GIT_REPO_URL=${VDR_GIT_REPO_URL:-"git@github.com-${REPO_NAME}:${GIT_REPO_OWNER}/${REPO_NAME}.git"}

# URL of github ghpages
export REPO_URL=${VDR_REPO_URL:-"https://${GIT_REPO_OWNER}.github.io/${REPO_NAME}"}

# Set to yes if update_repository.sh should automatically commit and push changes to github
export GIT_COMMIT=${VDR_GIT_COMMIT:-"no"}

