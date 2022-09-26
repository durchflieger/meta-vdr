PR = "r0"
MAINTAINER = "df"
SECTION = "multimedia"

PLUGIN_BASE_NAME = "${@d.getVar('PN').replace('vdr-plugin-', '')}"
PLUGIN_CONF_FILE = "50-${PLUGIN_BASE_NAME}.conf"

SUMMARY = "Channelscan plugin for VDR"

DESCRIPTION = "This plugin allows you to scan for new channels. DVB-T and DVB-C are supported \
as well as DVB-S and pvrinput/ptv (analog) scan."

HOMEPAGE = "https://www.gen2vdr.de/wirbel/wirbelscan/index2.html"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=892f569a555ba9c07a568a7c0c4fa63a"

SRC_URI = "https://www.gen2vdr.de/wirbel/wirbelscan/vdr-${PLUGIN_BASE_NAME}-${PV}.tgz \
  file://00-makefile.patch \
"

SRC_URI[sha256sum] = "8559c74838e5105016b8c0cd506d3f8721933e2bbadd5b3617f465284f702e2e"

S = "${WORKDIR}/${PLUGIN_BASE_NAME}-${PV}"

inherit gettext

DEPENDS = "pkgconfig vdr librepfunc"

PACKAGES = "${PN}-dbg ${PN} ${PN}-doc"

RDEPENDS:${PN} =+ "vdr librepfunc"

FILES:${PN} =+ "${libdir}/vdr/plugins"
FILES:${PN} =+ "${datadir}/locale"

CONFFILES:${PN} =+ "${sysconfdir}/vdr/conf.d/${PLUGIN_CONF_FILE}"

INSANE_SKIP += "file-rdeps"

do_install () {
  oe_runmake install DESTDIR=${D}

  install -D -m 0644 -t ${D}${docdir}/${PN} ${S}/README

  echo "[${PLUGIN_BASE_NAME}]" > ${WORKDIR}/${PLUGIN_CONF_FILE}
  install -D -m 0644 -t ${D}${sysconfdir}/vdr/conf.d ${WORKDIR}/${PLUGIN_CONF_FILE}
}
