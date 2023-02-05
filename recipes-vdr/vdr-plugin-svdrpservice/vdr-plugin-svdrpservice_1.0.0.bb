PR = "r0"
MAINTAINER = "df"
SECTION = "multimedia"

PLUGIN_BASE_NAME = "${@d.getVar('PN').replace('vdr-plugin-', '')}"
PLUGIN_CONF_FILE = "40-${PLUGIN_BASE_NAME}.conf"

SUMMARY = "VDR plugin that provides a SVDRP service for other plugins"

DESCRIPTION = "This VDR plugin offers SVDRP connections as a service to other plugins. \
Connecting to streamdev's VTP server port is possible too."

HOMEPAGE = "https://github.com/vdr-projects/vdr-plugin-svdrpservice"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=393a5ca445f6965873eca0259a17f833"

SRC_URI = "git://github.com/vdr-projects/vdr-plugin-svdrpservice.git;branch=master;protocol=https"

SRCREV = "7f10bcd5db1f6f4ac8b71bedbff3ddef4f77ec14"

S = "${WORKDIR}/git"

inherit gettext pkgconfig

DEPENDS = "vdr"

PACKAGES = "${PN}-dbg ${PN} ${PN}-doc ${PN}-dev"

RDEPENDS:${PN} =+ "vdr"

FILES:${PN} =+ "${libdir}/vdr/plugins"
FILES:${PN} =+ "${datadir}/locale"

CONFFILES:${PN} =+ "${sysconfdir}/vdr/conf.d/${PLUGIN_CONF_FILE}"

do_install () {
  oe_runmake install DESTDIR=${D}

  install -D -m 0644 -t ${D}${includedir} ${S}/svdrpservice.h

  install -D -m 0644 -t ${D}${docdir}/${PN} ${S}/README

  echo "[${PLUGIN_BASE_NAME}]" > ${WORKDIR}/${PLUGIN_CONF_FILE}
  install -D -m 0644 -t ${D}${sysconfdir}/vdr/conf.d ${WORKDIR}/${PLUGIN_CONF_FILE}
}
