PR = "r1"
MAINTAINER = "df"
SECTION = "multimedia"

PLUGIN_BASE_NAME = "${@d.getVar('PN').replace('vdr-plugin-', '')}"
PLUGIN_CONF_FILE = "50-${PLUGIN_BASE_NAME}.conf"

SUMMARY = "Plugin to control vdr OSD by console or telnet"

DESCRIPTION = "This plugin allows to control VDR by console or telnet \
even if the systems does not have any full featured DVB card."

HOMEPAGE = "https://github.com/wirbel-at-vdr-portal/vdr-plugin-control"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263"

SRC_URI = "git://github.com/wirbel-at-vdr-portal/vdr-plugin-control.git;branch=main;protocol=https"

SRCREV = "09fb0a510445041c5df875452c736816e2a1b298"

S = "${WORKDIR}/git"

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
