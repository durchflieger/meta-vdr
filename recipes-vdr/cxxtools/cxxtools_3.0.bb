PR = "r0"
MAINTAINER = "df"
SECTION = "extra"

SUMMARY ="Cxxtools is a collection of general-purpose C++ classes."

DESCRIPTION = "cxxtools contains an argument-parser, a base-64 encoder/decoder, a \
C++ interface to iconv, md5-stream for easy MD5 calculation, \
threading classes, socket classes, a dynamic exception-safe buffer, a \
wrapper for dlopen/dlsym, a pool template (e.g., for a connection \
pool in a multi-threaded application), query_params, and a class for \
easy parsing of CGI parameters (GET and POST) in a CGI program."

HOMEPAGE = "http://www.tntnet.org/cxxtools.html"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=1702a92c723f09e3fab3583b165a8d90"

SRC_URI = "git://github.com/maekitalo/cxxtools.git;branch=master;protocol=https"

#SRCREV = "V${PV}"
SRCREV = "31a212fe400b36cc5b9ea0dd76d9b5facfde914d"

S = "${WORKDIR}/git"

inherit pkgconfig binconfig autotools gettext
BBCLASSEXTEND = "native"

DEPENDS = "pkgconfig openssl"

EXTRA_OECONF:class-target = "--disable-demos --disable-unittest --without-ssl --with-iconvstream=yes"
EXTRA_OECONF:class-native = "--disable-demos --disable-unittest --without-ssl --with-iconvstream=yes"

