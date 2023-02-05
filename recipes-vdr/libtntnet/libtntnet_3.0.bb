PR = "r0"
MAINTAINER = "df"
SECTION = "extra"

SUMMARY ="Tntnet is a web application server for web applications written in C++."

DESCRIPTION = "You can write a Web-page with HTML and with special tags you embed \
C++-code into the page for active contents. These pages, called components are \
compiled into C++-classes with the ecpp-compilier "ecppc", then compiled into \
objectcode and linked into a process or shared library."

HOMEPAGE = "http://www.tntnet.org"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=91a0db8168d8a6e334686ea704ca2dd1"

SRC_URI = "git://github.com/maekitalo/tntnet.git;branch=master;protocol=https"

#SRCREV = "V${PV}"
SRCREV = "725bc4dffea94525e6722c625bb6720e4bb470a0"

S = "${WORKDIR}/git"

inherit pkgconfig binconfig autotools gettext
BBCLASSEXTEND = "native"

DEPENDS = "cxxtools libtool zlib"

EXTRA_OECONF:class-native = "--disable-unittest --disable-server"

EXTRA_OECONF:class-target = "--disable-unittest --disable-server --disable-sdk"

FILES:${PN}-dev =+ "${datadir}/tntnet"

INSANE_SKIP += "file-rdeps"
