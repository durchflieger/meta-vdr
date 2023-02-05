PR = "r0"
MAINTAINER = "df"
SECTION = "multimedia"

PLUGIN_BASE_NAME = "${@d.getVar('PN').replace('vdr-plugin-', '')}"
PLUGIN_CONF_FILE = "50-${PLUGIN_BASE_NAME}.conf"

SUMMARY = "VDR plugin that extends the SVDRP command set of VDR"

DESCRIPTION = "This VDR plugin adds some new OSD related commands to VDR's remote SVDRP control interface."

HOMEPAGE = "https://github.com/vdr-projects/vdr-plugin-svdrposd"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=393a5ca445f6965873eca0259a17f833"

SRC_URI = "git://github.com/vdr-projects/vdr-plugin-svdrposd.git;branch=master;protocol=https"

SRCREV = "0d9163dce422b8e9ab491897391b2b4b13c147b4"

S = "${WORKDIR}/git"

inherit gettext pkgconfig

DEPENDS = "vdr vdr-plugin-svdrpservice"

PACKAGES = "${PN}-dbg ${PN} ${PN}-doc"

RDEPENDS:${PN} =+ "vdr vdr-plugin-svdrpservice"

FILES:${PN} =+ "${libdir}/vdr/plugins"
FILES:${PN} =+ "${datadir}/locale"

CONFFILES:${PN} =+ "${sysconfdir}/vdr/conf.d/${PLUGIN_CONF_FILE}"

do_install () {
  oe_runmake install DESTDIR=${D}

  install -D -m 0644 -t ${D}${docdir}/${PN} ${S}/README

  echo "[${PLUGIN_BASE_NAME}]" > ${WORKDIR}/${PLUGIN_CONF_FILE}
  install -D -m 0644 -t ${D}${sysconfdir}/vdr/conf.d ${WORKDIR}/${PLUGIN_CONF_FILE}
}
