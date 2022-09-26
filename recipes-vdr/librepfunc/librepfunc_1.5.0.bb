PR = "r0"
MAINTAINER = "df"
SECTION = "multimedia"

DESCRIPTION = "This is librepfunc - a collection of functions, classes and so on, which i use often."

HOMEPAGE = "https://github.com/wirbel-at-vdr-portal/librepfunc"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRC_URI = "git://github.com/wirbel-at-vdr-portal/librepfunc;branch=main;protocol=https \
  file://00-makefile.patch"

SRCREV="3d0730be73237bbdadb211268b6ce79e999eb4b9"

S = "${WORKDIR}/git"

inherit pkgconfig

PACKAGES = "${PN}-dbg ${PN} ${PN}-dev ${PN}-doc"

EXTRA_OEMAKE="prefix=${prefix} srcdir=${S} DESTDIR=${D}"

FILES:${PN} =+ "${libdir}/librepfunc.so*"

INSANE_SKIP += "dev-so"

do_install () {
  oe_runmake install
}
