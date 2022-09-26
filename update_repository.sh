#!/bin/bash
set -e
shopt -s extglob nullglob

META_VDR_DIR=$(dirname $0)
if [ ! -f "$META_VDR_DIR/config.sh" ]; then echo "could not find config.sh"; exit 1; fi
source $META_VDR_DIR/config.sh

if [ ! -f "$META_VDR_PKG_LIST" ]; then echo "package list not found!"; exit 1; fi
PACKAGES=$(sed -e 's/#.*$//' $META_VDR_PKG_LIST)

if [ ! -x "$OPKG_UTILS_DIR/opkg-make-index" ]; then echo "opkg-make-index not found!"; exit 1; fi

found_git_repo=
if [ -n "$GIT_REPO_URL" ]; then
	if [ -d "$REPO_DIR" ]; then
		if [ -d "$REPO_DIR/.git" ]; then found_git_repo=1; fi
		#if [ -n "$found_git_repo" ]; then git -C $REPO_DIR pull origin; git -C $REPO_DIR reset --hard; fi
	else
		if git -C $(dirname $REPO_DIR) clone "$GIT_REPO_URL" $REPO_DIR; then found_git_repo=1; fi
	fi
fi

PKG_DIR="$(pwd)/tmp/deploy/ipk"
for d in $REPO_DIR $PKG_DIR; do
	if [ ! -d "$d" ]; then echo "directory does not exists: ${d}"; exit 1; fi
done

if [ -n "$found_git_repo" ]; then
	GIT_MSG="$REPO_DIR/commit_msg"
	rm -f $GIT_MSG
	touch $GIT_MSG
fi

# copy new packages from build to repository
has_changed=""
for p in $PACKAGES; do
	echo "Package ${p}:"
	has_new=""
	for pkg in ${PKG_DIR}/*/${p}+(*([0-9])|-dev|-dbg|-doc|-client|-server)_*.ipk ; do
		ipk=$(basename $pkg)
		arch=$(basename $(dirname $pkg))
		echo -n "- $ipk"
		if [ ! -e $REPO_DIR/$arch/$ipk ]; then
			has_new="y"
			has_changed="y"
			install -m 0644 -D -t $REPO_DIR/$arch $pkg
			if [ -n "$found_git_repo" ]; then git -C $REPO_DIR add $arch/$ipk; fi
			echo " -> new!"
		else
			echo " -> exists"
		fi
	done
	if [ -n "$has_new" -a -n "$found_git_repo" ]; then echo "new/updated $p" >> $GIT_MSG; fi
done

if [ -n "$has_changed" ]; then
	(cd $REPO_DIR && $OPKG_UTILS_DIR/opkg-make-index . > Packages && gzip -fk Packages)
	if [ -n "$found_git_repo" ]; then git -C $REPO_DIR add Packages.gz Packages.stamps; fi
fi

if [ -n "$found_git_repo" ]; then
	# Create opkg feed file
	export FEED_FILE="${REPO_NAME}-${GIT_REPO_OWNER}-feed.conf"
	f="$REPO_DIR/$FEED_FILE"
	t="${f}.tmp"
  echo "src/gz ${REPO_NAME}-${GIT_REPO_OWNER} ${REPO_URL}" > $t
	if [ ! -f "$f" ] || if diff -q $f $t; then false; else true; fi; then
		mv $t $f
		git -C $REPO_DIR add $FEED_FILE
		echo "new/updated $FEED_FILE" >> $GIT_MSG
		has_changed="y"
	fi

	# create README
	if [ -f "$META_VDR_DIR/repository/README.in" ]; then
		f="$REPO_DIR/README.md"
		t="${f}.tmp"
		envsubst < $META_VDR_DIR/repository/README.in > $t
		if [ ! -f "$f" ] || if diff -q $f $t; then false; else true; fi; then
			mv $t $f
			git -C $REPO_DIR add README.md
			echo "new/updated README.md" >> $GIT_MSG
			has_changed="y"
		fi
	fi
fi

if [ -n "$found_git_repo" ]; then
	if [ -n "$has_changed" ]; then
		echo "commit message:"
		cat $GIT_MSG
		if [ "$GIT_COMMIT" = "yes" ]; then
			git -C $REPO_DIR commit -F $GIT_MSG && git -C $REPO_DIR push origin && echo "Changes commited and pushed"
		else
			echo "cd to $REPO_DIR and commit and push changes with:"
			echo "git commit -F ./commit_msg && git push origin"
		fi
	else
		echo "Nothing to commit"
	fi
fi
