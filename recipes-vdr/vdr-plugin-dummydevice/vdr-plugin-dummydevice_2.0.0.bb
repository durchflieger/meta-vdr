PR = "r1"
MAINTAINER = "df"
SECTION = "multimedia"

PLUGIN_BASE_NAME = "${@d.getVar('PN').replace('vdr-plugin-', '')}"
PLUGIN_CONF_FILE = "50-${PLUGIN_BASE_NAME}.conf"

SUMMARY = "VDR plugin that emulates a dvb output device"

DESCRIPTION = "This plugin emulates an output device, that does nothing. \
All data is silently discarded. That is useful if you just \
run vdr as recording server or for streaming and would \
like to overcome some limitions regarding the handling of a \
primary device."

HOMEPAGE = "http://www.vdr-wiki.de/wiki/index.php/Dummydevice-plugin"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=892f569a555ba9c07a568a7c0c4fa63a"

SRC_URI = "http://phivdr.dyndns.org/vdr/vdr-${PLUGIN_BASE_NAME}/vdr-${PLUGIN_BASE_NAME}-${PV}.tgz \
  file://00-devicename.patch \
"

SRC_URI[sha256sum] = "5c0049824415bd463d3abc728a3136ee064b60a37b5d3a1986cf282b0d757085"

S = "${WORKDIR}/${PLUGIN_BASE_NAME}-${PV}"

inherit gettext pkgconfig

DEPENDS = "vdr"

PACKAGES = "${PN}-dbg ${PN} ${PN}-doc"

RDEPENDS:${PN} =+ "vdr"

FILES:${PN} =+ "${libdir}/vdr/plugins"
FILES:${PN} =+ "${datadir}/locale"

CONFFILES:${PN} =+ "${sysconfdir}/vdr/conf.d/${PLUGIN_CONF_FILE}"

do_install () {
  oe_runmake install DESTDIR=${D}

  install -D -m 0644 -t ${D}${docdir}/${PN} ${S}/README

  echo "[${PLUGIN_BASE_NAME}]" > ${WORKDIR}/${PLUGIN_CONF_FILE}
  install -D -m 0644 -t ${D}${sysconfdir}/vdr/conf.d ${WORKDIR}/${PLUGIN_CONF_FILE}
}
