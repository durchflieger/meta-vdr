PR = "r0"
MAINTAINER = "df"
SECTION = "multimedia"

PLUGIN_BASE_NAME = "${@d.getVar('PN').replace('vdr-plugin-', '')}"
PLUGIN_CONF_FILE = "50-${PLUGIN_BASE_NAME}.conf"

SUMMARY = "Web administration plugin for VDR"

DESCRIPTION = "Live, the "Live Interactive VDR Environment", is a plugin providing the \
possibility to interactively control the Linux Video Disc Recorder VDR and \
some of it's plugins from a web interface. \
Unlike external programs, like the VDR web frontend "VDRAdmin-AM", that \
communicate  with VDR via its SVDRP socket interface, Live has direct \
access to VDR's data structures and thus is very fast."

HOMEPAGE = "https://github.com/MarkusEh/vdr-plugin-live"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=892f569a555ba9c07a568a7c0c4fa63a"

SRC_URI = "git://github.com/MarkusEh/vdr-plugin-live.git;branch=master;protocol=https \
  file://${PLUGIN_CONF_FILE} \
"

SRCREV = "82e2e7ef28f38039b3882a662785cff83993b683"

S = "${WORKDIR}/git"

PARALLEL_MAKE = "-j 1"

inherit gettext pkgconfig
DEPENDS = "vdr libtntnet libtntnet-native libpcre2"

PACKAGES = "${PN}-dbg ${PN} ${PN}-doc"

RDEPENDS:${PN} =+ "vdr"

FILES:${PN} =+ "${libdir}/vdr/plugins"
FILES:${PN} =+ "${datadir}/locale"
FILES:${PN} =+ "${datadir}/vdr/plugins/${PLUGIN_BASE_NAME}"

CONFFILES:${PN} =+ "${sysconfdir}/vdr/conf.d/${PLUGIN_CONF_FILE}"

do_install () {
  oe_runmake install-lib install-i18n DESTDIR=${D}

  mkdir -p ${D}${datadir}/vdr/plugins/${PLUGIN_BASE_NAME}
  (cd ${S} && cp -r live/* ${D}/${datadir}/vdr/plugins/${PLUGIN_BASE_NAME}/)
  find ${D}/${datadir}/vdr/plugins/${PLUGIN_BASE_NAME} -type d -exec chmod 0755 {} \;
  find ${D}/${datadir}/vdr/plugins/${PLUGIN_BASE_NAME} -type f -exec chmod 0644 {} \;

  install -D -m 0644 -t ${D}${docdir}/${PN} ${S}/README

  install -D -m 0644 -t ${D}${sysconfdir}/vdr/conf.d ${WORKDIR}/${PLUGIN_CONF_FILE}
}
